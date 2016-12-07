import org.lwjgl.input.Keyboard;

import org.lwjgl.util.vector.Vector2f;
import java.util.Random;

public class Player implements Constants{
	private static Random rand = new Random();
	static final int DIM = 500;
	float speed = 2f;
	int xPos = rand.nextInt(DIM-1);
	int yPos = rand.nextInt(DIM-1);
	Vector2f position = new Vector2f(xPos,yPos);
	Vector2f networkPosition = new Vector2f(-1,-1);
	
	int head=rand.nextInt(4);
	boolean isAlive = true;
	String name;
	public int score=0;
	public Player(String name){
		this.name = name;
	}
	
	public void update(){		
		if(isAlive){
			if(Keyboard.isKeyDown(Keyboard.KEY_W) && head!=1){
				head=0;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_S) && head!=0){
				head=1;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_A) && head!=3){
				head=2;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_D) && head!=2){
				head=3;
			}			
		}
	}
	
	public void initCoordinate(){
		xPos = rand.nextInt(DIM-1);
		yPos = rand.nextInt(DIM-1);
		head = rand.nextInt(4);
		isAlive = true;
		position = new Vector2f(xPos,yPos);
		networkPosition = new Vector2f(-1,-1);
	}
}
