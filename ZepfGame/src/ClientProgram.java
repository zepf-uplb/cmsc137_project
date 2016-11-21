import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class ClientProgram implements Constants, ActionListener {

	static Player player;
	static Network network;
	static Map<Integer,MPPlayer> players = new HashMap<Integer,MPPlayer>(); 
	
	private JButton startButton;
	private JTextField nameText;
	private JTextField serverText;
	
	public static void main(String[] args) throws Exception {
		@SuppressWarnings("unused")
		ClientProgram cw = new ClientProgram();
	}
	
	public ClientProgram(){
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
		
		network.connect();				
		gw.render(network.deathFlag, player);
		
		EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {                
            	if(network.gameStart){	
    				gw.render(network.deathFlag, player);	
    				
    				if(player.isAlive){
    					System.out.println("Alive");
        				update();				
        			}	
    				else{
    					System.out.println("Dead");
    				}
    			}    					
    			gw.foo();
                EventQueue.invokeLater(this);
            }
        });
	}
	
	public static void update(){
		player.update();		

		if(player.networkPosition.x != player.position.x || player.networkPosition.y != player.position.y){
			PacketUpdateXY packet = new PacketUpdateXY();
			packet.x = player.position.x;
			packet.y = player.position.y;
			network.client.sendUDP(packet);
		}
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
				network = new Network(serverText.getText());
				startClient();
			}
		}			
	}
}
