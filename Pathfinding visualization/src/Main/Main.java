package Main;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;


public class Main implements Runnable, MouseListener, KeyListener {
	
	private JFrame frame;
	private Canvas canvas;
	
	private Thread thread;
	private boolean running;
	
	private BufferStrategy bs;
	private Graphics2D g;
	
	private int width, height;
	private TileMap tileMap;
	
	private void init(int width, int height, String title) {
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		canvas = new Canvas();
		canvas.setSize(width, height);
		canvas.addMouseListener(this);
		canvas.addKeyListener(this);
		canvas.setFocusable(true);
		frame.add(canvas);
		running = true;
		frame.setVisible(true);
		this.width = width;
		this.height = height;
		
		tileMap = new TileMap(20, 15);
		tileMap.LoadMap("2 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
					   +"0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
					   +"0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
					   +"0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
					   +"0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
					   +"0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
					   +"0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
					   +"0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
					   +"0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
					   +"0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
					   +"0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
					   +"0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
					   +"0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
					   +"0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "
					   +"0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 3");
	}
	
	@Override
	public void run() {
		
		long last = 0, curr = 0;
		
		while(running) {
			// game loop
			
			if (curr >= 1000000/60) {
				update();
				render();
			}
			
			curr += System.nanoTime() - last;
			last = System.nanoTime();
		}
		
		frame.dispose();
		stop();
	}
	
	private void update() {
		tileMap.update();
	}
	
	private void render() {
		bs = canvas.getBufferStrategy();
		if (bs == null) {
			canvas.createBufferStrategy(2);
			bs = canvas.getBufferStrategy();
		}
		g = (Graphics2D) bs.getDrawGraphics();
		g.clearRect(0, 0, width, height);
		
		tileMap.render(g);
		
		g.dispose();
		bs.show();
	}
	
	private void stop() {
		running = false;
		if (thread != null) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void start() {
		running = true;
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public static void main(String[] args) {
		Main m = new Main();
		m.init(800, 620, "Pathfinding visualization");
		m.start();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		tileMap.onMouseReleased(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void keyPressed(KeyEvent e) {
		tileMap.onKeyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		tileMap.onKeyReleased(e);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
	
}
