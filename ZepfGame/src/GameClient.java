import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GameClient implements Constants, ActionListener {

	static Player player;
	static GameNetwork network;
	static Map<Integer,MPPlayer> players = new HashMap<Integer,MPPlayer>(); 
	
	private JButton startButton;
	private JTextField nameText;
	private JTextField serverText;
	
	public static void main(String[] args) throws Exception {
		@SuppressWarnings("unused")
		GameClient cw = new GameClient();
	}
	
	public GameClient(){
		ClientWindow cw = new ClientWindow();
		startButton = cw.getStartButton();
		nameText = cw.getNameText();
		serverText = cw.getServerText();
		startButton.setActionCommand("start_client");
		startButton.addActionListener(this);
	}
	
	public void startClient(){
		GameWindow gw = new GameWindow();	
		gw.createGraphics();
		
		new ChatClient(gw, serverText.getText(), nameText.getText()).start();
		
		network.startNetwork();				
		gw.render(network.deathFlag, player);
		
		EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {                
            	if(network.gameStart){	
    				gw.render(network.deathFlag, player);	
    				
    				if(player.isAlive){
    					//System.out.println("Alive");
        				update();				
        			}	
    				else{
    					//System.out.println("Dead");
    				}
    			}    					
    			gw.foo();
                EventQueue.invokeLater(this);
            }
        });
	}
	
	public static void update(){
		player.update();		
		network.sendCoordinate(player.position.x, player.position.y);
	}	

	@Override
	public void actionPerformed(ActionEvent ae) {
		if ("start_client".equals(ae.getActionCommand())) {			
			if(nameText.getText().equals("")){
				nameText.setBackground(Color.RED);
			}
			else if(serverText.getText().equals("")){
				nameText.setBackground(Color.WHITE);
				serverText.setBackground(Color.RED);
			}
			else{
				serverText.setBackground(Color.WHITE);
				player = new Player(nameText.getText());
				network = new GameNetwork(serverText.getText(), nameText.getText());
				startClient();
			}
		}			
	}
}