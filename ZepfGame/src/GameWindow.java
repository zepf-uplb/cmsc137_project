import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class GameWindow implements Constants, ActionListener {	
	
	private Canvas c;
	private JTextArea chatWindow = new JTextArea(15, 15);
	private JTextField messageField = new JTextField(15);
	
	public GameWindow(){

		JFrame frame = new JFrame();
		Container contentPane = frame.getContentPane();
		JPanel panel = new JPanel();
		
		frame.setTitle("Zepf Game");
		frame.setSize(DIM+180, DIM+29);
		frame.setResizable(false);
		frame.addWindowListener(new FrameWindowListener());
		
		/****/
		chatWindow.setEditable(false);		
		messageField.setActionCommand("enter_message");
		messageField.addActionListener(this);
		panel.setLayout(new BorderLayout());
		panel.add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		panel.add(messageField, BorderLayout.PAGE_END);
		/****/
		
		c = new Canvas();
		c.setSize(DIM, DIM);
	
		contentPane.setLayout(new BorderLayout());
		contentPane.add(c, BorderLayout.CENTER);
		contentPane.add(panel, BorderLayout.WEST);

		frame.setVisible(true);


	}	
	
	public void createGraphics(){
		try {			
			Display.setParent(c);
			Display.setDisplayMode(new DisplayMode(DIM,DIM));
			Display.create();	
			
			//Init GL
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, Display.getWidth(), 0, Display.getHeight(), -1, 1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);


		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	/*public void startGame(){
		
	}
	
	public Boolean isRunning(){
		//return !Display.isCloseRequested();
		return !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE);
	}*/
	
	public void foo(){
		GL11.glLoadIdentity();
		Display.update();
		Display.sync(60);
		//update window
		//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void exit(){
		Display.destroy();
		System.exit(0);
	}
	
	public void render(Boolean deathFlag, Player player){
		
		if(deathFlag){
			player.isAlive = false;	
		}

		if(player.isAlive){
			switch(player.head){
			case 0: if(player.position.y < DIM-1) player.position.y++;
				break;
			case 1:	if(player.position.y > 0) player.position.y--;
				break;
			case 2: if(player.position.x > 0) player.position.x--;
				break;
			case 3: if(player.position.x < DIM-1) player.position.x++;
				break;
			}
		}		
		
		//Render player		
		GL11.glColor3f(1,1,1);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(player.position.x, player.position.y);
		GL11.glVertex2f(player.position.x+4, player.position.y);
		GL11.glVertex2f(player.position.x+4, player.position.y-4);
		GL11.glVertex2f(player.position.x, player.position.y-4);
		GL11.glEnd();
		
		//Render other players
		GL11.glColor3f(1, 1, 0);
		GL11.glBegin(GL11.GL_QUADS);
		for(MPPlayer mpPlayer : ClientProgram.players.values()){
			GL11.glVertex2f(mpPlayer.x, mpPlayer.y);
			GL11.glVertex2f(mpPlayer.x+4, mpPlayer.y);
			GL11.glVertex2f(mpPlayer.x+4, mpPlayer.y-4);
			GL11.glVertex2f(mpPlayer.x, mpPlayer.y-4);
		}
		GL11.glEnd();		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if ("enter_message".equals(ae.getActionCommand())) {
			messageField.setText("");
		}		
	}
	
	public JTextField getMessageField(){
		return this.messageField;
	}
	
	public JTextArea getChatWindow(){
		return this.chatWindow;
	}
	
}

class FrameWindowListener extends WindowAdapter
{
    @Override
    public void windowClosing(WindowEvent e)
    {
        Display.destroy();
        System.exit(0);
    }
}
