package Main;

public class Position {
	private int x,y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	
	public static float getDist(Position a, Position b) {
		return (float)Math.sqrt(Math.pow((a.x-b.x), 2)+Math.pow((a.y-b.y), 2));
	}
	
	public static boolean areEqual(Position a, Position b) {
		return (a.getX()==b.getX())&&(a.getY()==b.getY());
	}
	
	public String toString() { return "[" + x + ", " + y + "]"; }
}
