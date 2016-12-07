
public class Powerup {

	public static final int SLOW = 1;
	public static final int IMMUNE = 2;
	public static final int FLIP_CONTROLS = 3;
	
	public static int ICON_SIZE = 15;
	
	public static int OFFSET = 100;
	
	public int xPosition, yPosition;
	
	public Powerup(int x, int y){
		xPosition = x;
		yPosition = y;
	}
}
