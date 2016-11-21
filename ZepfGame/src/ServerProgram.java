import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;


public class ServerProgram extends Listener implements Constants, ActionListener{

	static Server server;
	static Map<Integer, Playerr> players = new HashMap<Integer, Playerr>();
	static int maxPlayers = 2, currentPlayers = 0;
	static boolean startGame = false;	
	private int[][] state;
	
	private JLabel statusText;
	private JSpinner playerNum;
	private JButton startButton;
	private JButton endButton;
	
	public static void main(String[] args) throws IOException{
		@SuppressWarnings("unused")
		ServerProgram sp = new ServerProgram(true);
	}
	
	public ServerProgram(){}
	
	public ServerProgram(Boolean b){
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
			server = new Server();
			server.getKryo().register(PacketUpdateXY.class);
			server.getKryo().register(PacketAddPlayer.class);
			server.getKryo().register(PacketRemovePlayer.class);
			server.getKryo().register(GameStart.class);
			server.getKryo().register(DeathFlag.class);
			server.bind(UDP_PORT, UDP_PORT);
			server.start();
			server.addListener(new ServerProgram());
			
			EventQueue.invokeLater(new Runnable(){
	            public void run(){             
	            	try{Thread.sleep(10);}catch(Exception e){}
	            	ChatServer.init(maxPlayers);
	            }
	        });			
			
			statusText.setText("ONLINE");
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void connected(Connection c){		
		Playerr player = new Playerr();
		player.x = 256;
		player.y = 256;
		player.c = c;
		
		PacketAddPlayer packet = new PacketAddPlayer();
		packet.id = c.getID();
		server.sendToAllExceptUDP(c.getID(), packet);
		
		if(currentPlayers == 0){
			state = new int[DIM][DIM];
		}
		
		currentPlayers++;
		
		if(currentPlayers == maxPlayers){
			GameStart gs = new GameStart();
			server.sendToAllUDP(gs);
		}
		
		for(Playerr p : players.values()){
			PacketAddPlayer packet2 = new PacketAddPlayer();
			packet2.id = p.c.getID();
			c.sendUDP(packet2);
		}
		
		players.put(c.getID(), player);
	}
	
	public void received(Connection c, Object o){
		if(o instanceof PacketUpdateXY){
			PacketUpdateXY packet = (PacketUpdateXY) o;
			
			if(state[(int)packet.x][(int)packet.y]==1){
				DeathFlag df = new DeathFlag();
				server.sendToUDP(c.getID(), df);
			}
			
			state[(int)packet.x][(int)packet.y]=1;	
			
			packet.id = c.getID();
			server.sendToAllExceptUDP(c.getID(), packet);		
		}
	}
	
	public void disconnected(Connection c){		
		players.remove(c.getID());
		PacketRemovePlayer packet = new PacketRemovePlayer();
		packet.id = c.getID();
		server.sendToAllExceptTCP(c.getID(), packet);
		currentPlayers--;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if ("start_server".equals(ae.getActionCommand())) {			
			maxPlayers = (int) playerNum.getValue();
			playerNum.setEnabled(false);
			startButton.setEnabled(false);		
			endButton.setEnabled(true);	
			this.startServer();			
		}
		else if ("end_server".equals(ae.getActionCommand())) {		
			playerNum.setEnabled(true);
			startButton.setEnabled(true);	
			endButton.setEnabled(false);	
			server.close();					
			statusText.setText("OFFLINE");
		}		
	}
}
