import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GameClient implements Constants, ActionListener {

	static Player player;
	static GameNetwork network;
	static Map<Integer,MPPlayer> players = new HashMap<Integer,MPPlayer>(); 
	
	private JButton startButton;
	private JButton manualButton;
	private JTextField nameText;
	private JTextField serverText;
	
	public static void main(String[] args) throws Exception {
		@SuppressWarnings("unused")
		GameClient cw = new GameClient();
	}
	
	public GameClient(){
		ClientWindow cw = new ClientWindow();
		startButton = cw.getStartButton();
		manualButton = cw.getManualButton();
		nameText = cw.getNameText();
		serverText = cw.getServerText();
		startButton.setActionCommand("start_client");
		startButton.addActionListener(this);
		manualButton.setActionCommand("view_manual");
		manualButton.addActionListener(this);
	}
	
	public void startClient(){
		GameWindow gw = new GameWindow();	
		gw.createGraphics();
		
		new ChatClient(gw, serverText.getText(), nameText.getText()).start();
		
		network.startNetwork();				
		gw.render(network.deathFlag, player);
		
		EventQueue.invokeLater(new Runnable()
        {
			boolean once = true;
            public void run()
            {                
            	if(network.gameStart){	
    				gw.render(network.deathFlag, player);	
    				
    				if(once){    					
    					gw.waitWindow();
    					gw.updatePlayerWindow();
    					gw.timer.stop();
    					once = false;
    				}
    				
    				if(network.roundFinished){
    					gw.updatePlayerWindow();
    					gw.clearWindow();
    					network.deathFlag = false;
    					network.roundFinished = false;
    					network.gameStart = false;
    					once = true;
    					player.initCoordinate();
    				}
    				
    				if(network.gameStart){
        				update();		
        			}	
    			}  
            	
            	else if(network.gameEnd){
            		gw.showResults(GameClient.players.get(network.myID).score);
            		network.gameEnd = false;
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
				startButton.setEnabled(false);
				startClient();
			}
		
		}else if("view_manual".equals(ae.getActionCommand())){
			ClientWindow.viewManual();
		}
	}
}
