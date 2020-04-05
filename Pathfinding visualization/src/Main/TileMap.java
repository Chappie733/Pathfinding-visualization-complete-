package Main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

public class TileMap {
	
	// 0 -> normal, 1 -> obstacle, 
	// 2 -> starting point, 3 -> destination
	// 4 -> visited, 5 -> path
	public static final int tileSize = 40;
	private int width, height;
	private int map[][];
	private int selectedMaterial = 0;
	private Color colours[];
	
	private Node start, end;
	private LinkedList<Node> OPEN, EVALUATED, FINAL_PATH;
	private boolean should_search;
	
	public TileMap(int width, int height) {
		this.width = width;
		this.height = height;
		map = new int[width][height];
		colours = new Color[7];
		OPEN = new LinkedList<Node>();
		EVALUATED = new LinkedList<Node>();
		FINAL_PATH = new LinkedList<Node>();
	}
	
	public void LoadMap(String map_as_string) {
		String split[] = map_as_string.split("\\s+");
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				map[x][y] = Integer.parseInt(split[x*height+y]);
				
				if (map[x][y] == 2) start = new Node(new Position(x,y), null);
				else if (map[x][y] == 3) end = new Node(new Position(x,y), null);
			}
		}
		
		colours[1] = colours[0] = Color.black;
		colours[2] = Color.orange;
		colours[3] = Color.red;
		colours[4] = Color.green;
		colours[5] = Color.blue;
		colours[6] = Color.magenta;
		curr = start;
		OPEN.add(start);
	}
	
	private Node curr;
	public void update() {
		if (!should_search) {
			if (FINAL_PATH.isEmpty() && end.getParent() != null) {
				Node n = end;
				while (!Position.areEqual(n.getPos(), start.getPos())) {
					map[n.getX()][n.getY()] = 6;
					FINAL_PATH.add(n);
					n = n.getParent();
				}
			}
			return;
		}
		curr = lowestF();
		OPEN.remove(curr); // open is the list of nodes to be evaluated, which we know the F cost of
		EVALUATED.add(curr); // list of the already evaluated nodes
		map[curr.getX()][curr.getY()] = 5; // set the node to evaluated (only for rendering purpose)
		
		if (Position.areEqual(curr.getPos(), end.getPos())) {
			should_search = false;
			end.setParent(curr);
		}
		
		// check the neighbours
		for (int y=-1; y<=1; y++) {
			for (int x=-1;x<=1;x++) {
				Position p = new Position(curr.getX()+x, curr.getY()+y); // the current neighbor being checked
				
				if (!isContained(EVALUATED, p)) { // skip it if it's already part of the path
					// check if it's inside the array and if it isn't an impenetrable terrain
					if (isInBounds(p) && (map[curr.getX()+x][curr.getY()+y] != 1)) { 
						if (!isContained(OPEN, p)) {
							Node n = new Node(p, curr);
							n.setG(Position.getDist(start.getPos(), p));
							n.setH(Position.getDist(end.getPos(), p));
							n.setF(n.getG()+n.getH());
							OPEN.add(n); // add the node to the ones to be evaluated/possible next steps
							map[p.getX()][p.getY()] = 4; // set the node to "checked" (only for rendering purpose)
						} else {
							float tempG = Position.getDist(start.getPos(), curr.getPos()) + Position.getDist(curr.getPos(), p);
							if (OPEN.get(OPEN.indexOf(getNode(OPEN, p))).getG() < tempG) OPEN.get(OPEN.indexOf(getNode(OPEN, p))).setG(tempG);
						}
					}
				}
			}
		}
	}
	
	public void render(Graphics2D g) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				g.setColor(colours[map[x][y]]);
				if (map[x][y] != 0) g.fillRect(x*tileSize, y*tileSize, tileSize, tileSize);
				else g.drawRect(x*tileSize, y*tileSize, tileSize, tileSize);
			}
		}
		
		for (Node n : EVALUATED) {
			g.setColor(Color.BLACK);
			g.drawString(String.valueOf((int) (Position.getDist(n.getPos(), start.getPos()))), n.getX()*tileSize+5, n.getY()*tileSize+15);
			g.drawString(String.valueOf((int) (Position.getDist(n.getPos(), end.getPos()))), n.getX()*tileSize+25, n.getY()*tileSize+15);
			g.drawString(String.valueOf((int) (Position.getDist(n.getPos(), start.getPos())+Position.getDist(n.getPos(), end.getPos()))), n.getX()*tileSize+15, n.getY()*tileSize+30);
		} for (Node n : OPEN) {
			g.setColor(Color.BLACK);
			g.drawString(String.valueOf((int) (Position.getDist(n.getPos(), start.getPos()))), n.getX()*tileSize+5, n.getY()*tileSize+15);
			g.drawString(String.valueOf((int) (Position.getDist(n.getPos(), end.getPos()))), n.getX()*tileSize+25, n.getY()*tileSize+15);
			g.drawString(String.valueOf((int) (Position.getDist(n.getPos(), start.getPos())+Position.getDist(n.getPos(), end.getPos()))), n.getX()*tileSize+15, n.getY()*tileSize+30);
		}
	}
	
	private boolean isInBounds(Position p) {
		return (p.getX() >= 0 && p.getX() < width)&&(p.getY() >= 0 && p.getY() < height);
	}
	
	public void onMouseReleased(MouseEvent e) {
		map[e.getX()/tileSize][e.getY()/tileSize] = selectedMaterial;
		if (selectedMaterial == 2) {
			start.setX(e.getX()/tileSize);
			start.setY(e.getY()/tileSize);
			curr = start;
		} else if (selectedMaterial == 3) {
			end.setX(e.getX()/tileSize);
			end.setY(e.getY()/tileSize);
		}
	}
	
	public void onKeyPressed(KeyEvent e) {
		if (e.getKeyCode() >= 48 && e.getKeyCode() <= 52) selectedMaterial = e.getKeyCode()-49;
		// clear the map
		if (e.getKeyCode() == KeyEvent.VK_C) {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (map[x][y] != 0) map[x][y] = 0;
				}
			}
			OPEN.clear();
			EVALUATED.clear();
			end.setParent(null);
		}
		if (e.getKeyCode() == KeyEvent.VK_F && !Position.areEqual(curr.getPos(), end.getPos())) {
			should_search = true;
		}
	}
	
	public void onKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_F) {
			should_search = false;
		}
	}
	
	private Node lowestF() {
		float min_f = Integer.MAX_VALUE;
		Node min = new Node(new Position(0,0), null);
		for (Node p : OPEN) {
			if (p.getF() < min_f) {
				min = p;
				min_f = p.getF();
			}
		}
		return min;
	}
	
	public Node getNode(List<Node> list, Position p) {
		for (Node n : list) if (Position.areEqual(n.getPos(), p)) return n;
		return new Node(new Position(-1,-1), null);
	}
	
	public boolean isContained(List<Node> list, Position p) {
		for (Node curr : list) if (Position.areEqual(curr.getPos(), p)) return true;
		return false;
	}
}
