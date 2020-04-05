package Main;

public class Node {
	
	private Position pos;
	private Node parent;
	private float f, g, h;
	
	public Node(Position pos, Node parent) {
		this.pos = pos;
		this.parent = parent;
	}
	
	public Node(Position pos, Node parent, int f) {
		this.pos = pos;
		this.parent = parent;
		this.f = f;
	}
	
	public void setF(float f) { this.f = f; }
	public float getF() { return this.f; }
	public void setG(float g) { this.g = g; }
	public float getG() { return this.g; }
	public void setH(float h) { this.h = h; }
	public float getH() { return this.h; }
	
	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node p) {
		this.parent = p;
	}
	
	public Position getPos() {
		return pos;
	}
	
	public void setX(int x) {
		pos.setX(x);
	}
	
	public void setY(int y) {
		pos.setY(y);
	}
	
	public int getX() { return pos.getX(); }
	public int getY() { return pos.getY(); }
	
}
