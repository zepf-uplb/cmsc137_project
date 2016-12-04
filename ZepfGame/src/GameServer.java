import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;


public class GameServer implements Runnable, Constants, ActionListener{
	
	private DatagramSocket server = null;
	static Map<Integer, NetPlayer> players = new HashMap<Integer, NetPlayer>();
	static int maxPlayers = 2, currentPlayers = 0;
	static boolean startGame = false, online = true;	
	private int[][] state;
	private Thread t = new Thread(this);	
	private String playerData;	

	private JLabel statusText;
	private JSpinner playerNum;
	private JButton startButton;
	private JButton endButton;
	
	public static void main(String[] args) throws IOException{
		@SuppressWarnings("unused")
		GameServer gs = new GameServer();
	}
	
	public GameServer(){
		ServerWindow window = new ServerWindow();	
		statusText = window.getStatusText();
		playerNum = window.getPlayerNum();
		
		startButton = window.getStartButton();
		startButton.setActionCommand("start_server");
		startButton.addActionListener(this);
		
		endButton = window.getEndButton();
		endButton.setActionCommand("end_server");
		endButton.setEnabled(false);	
		endButton.addActionListener(this);
	}
	
	public void startServer(){		
		try {
			server = new DatagramSocket(UDP_PORT);
			server.setSoTimeout(0);
			t.start();						
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		EventQueue.invokeLater(new Runnable(){
            public void run(){             
            	ChatServer.init(maxPlayers);
            }
        });	
	}

	@Override
	public void run() {
		boolean once = true;
		while(online){
			try{
				byte[] buf = new byte[256];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
     			server.receive(packet);
     			playerData = new String(buf);
     			playerData = playerData.trim();
     			String[] playerInfo = playerData.split(",");
     			
     			if(currentPlayers == 0){
     				state = new int[DIM][DIM];
     			}
     			
     			if(playerData.startsWith("CONNECT")){     				
     				NetPlayer player = new NetPlayer(playerInfo[1].trim(), packet.getAddress(),packet.getPort());
     				player.x = 256;
     				player.y = 256;
     				players.put(currentPlayers, player);
     				send("ID,"+currentPlayers, player);
     				currentPlayers++;
     				
     			} else if(playerData.startsWith("UPDATE")){
     				int id = Integer.parseInt(playerInfo[1].trim());
     				float x = Float.parseFloat(playerInfo[2].trim());
					float y = Float.parseFloat(playerInfo[3].trim());
					if(state[(int)x][(int)y] == 1){
						send("DEATH_FLAG", players.get(id));
					}
					
					state[(int)x][(int)y] = 1;	
					broadcast(playerData);
					
     			}
     			
     			if(currentPlayers == maxPlayers){
     				if(once){
     					for(int i = 0; i < currentPlayers; i++){
     						broadcast("ADD,"+i+","+players.get(i).name);
     					}
     					once = false;
     				}
     				broadcast("GAME_START");
     			}
			}catch(Exception e){
				//e.printStackTrace();
			}
		}		
	}
	
	private void send(String msg, NetPlayer p){
		try{
			DatagramPacket packet;	
			byte buf[] = msg.getBytes();		
			packet = new DatagramPacket(buf, buf.length, p.address, p.port);
			server.send(packet);
		}
		catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	private void broadcast(String msg){
		for(NetPlayer p : players.values()){
			try{
				DatagramPacket packet;	
				byte buf[] = msg.getBytes();		
				packet = new DatagramPacket(buf, buf.length, p.address, p.port);
				server.send(packet);
			}
			catch(Exception e){
				e.printStackTrace();
			}			
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if ("start_server".equals(ae.getActionCommand())) {		
			online = true;
			maxPlayers = (int) playerNum.getValue();
			playerNum.setEnabled(false);
			startButton.setEnabled(false);		
			endButton.setEnabled(true);	
			this.startServer();			
			statusText.setText("ONLINE");
		}
		else if ("end_server".equals(ae.getActionCommand())) {	
			online = false;
			playerNum.setEnabled(true);
			startButton.setEnabled(true);	
			endButton.setEnabled(false);	
			server.close();					
			statusText.setText("OFFLINE");
		}		
	}

}
