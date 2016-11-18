import org.lwjgl.input.Keyboard;

import org.lwjgl.util.vector.Vector2f;
import java.util.Random;



public class Player {
	private static Random rand = new Random();
	float speed = 2f;
	int xPos = rand.nextInt(512);
	int yPos = rand.nextInt(512);
	Vector2f position = new Vector2f(xPos,yPos);
	Vector2f networkPosition = new Vector2f(-1,-1);
	
	public void update(){
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			position.y += speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			position.y -= speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			position.x -= speed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			position.x += speed;
		}
	}
}
