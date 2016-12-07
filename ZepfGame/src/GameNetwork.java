import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class GameNetwork implements Runnable, Constants {

	private DatagramSocket client = null;	
	private Thread t = new Thread(this);
	private String playerData, name;
	private InetAddress ipServer;
	private int myID;
	boolean gameStart = false, deathFlag = false, roundFinished = false;
	
	public GameNetwork(String ip, String name){
		try{
			this.ipServer = InetAddress.getByName(ip);
			this.name = name;
		}
		catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	public void startNetwork(){		
		try {
			client = new DatagramSocket();
			client.setSoTimeout(0);			
			String msg = "CONNECT,"+name;
    		byte[] buf = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, ipServer, UDP_PORT);
			client.send(packet);			
			t.start();						
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void run() {
		while(true){
			try{
				byte[] buf = new byte[256];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
     			client.receive(packet);
     			playerData = new String(buf);
     			playerData = playerData.trim();
     			String[] playerInfo = playerData.split(",");
     			
     			if(playerData.startsWith("DEATH_FLAG")){  
     				deathFlag = true;
     				
     			} else if(playerData.startsWith("ROUND_WON")){
     				int id = Integer.parseInt(playerInfo[1].trim());
     				GameClient.players.get(id).score++;
     				roundFinished = true;
     				
     			} else if(playerData.startsWith("UPDATE")){
     				int id = Integer.parseInt(playerInfo[1].trim());
     				float x = Float.parseFloat(playerInfo[2].trim());
					float y = Float.parseFloat(playerInfo[3].trim());
     				GameClient.players.get(id).x = x;
     				GameClient.players.get(id).y = y;  		
     				
     			} else if(playerData.startsWith("ADD")){     		
     				int id = Integer.parseInt(playerInfo[1].trim());
     				String name = playerInfo[2].trim();
 					MPPlayer newPlayer = new MPPlayer();  
 					newPlayer.id = id;
 					newPlayer.name = name;
     				GameClient.players.put(id, newPlayer);   				
     				
     			} else if(playerData.startsWith("ID")){
     				int id = Integer.parseInt(playerInfo[1].trim());
     				myID = id;
     			
     			} else if(playerData.startsWith("GAME_START")){
     				gameStart = true;	
     			}
     			
			}catch(Exception e){
				e.printStackTrace();
			}
		}			
	}
	
	public void sendCoordinate(float x, float y){
		try {	
			String msg = "UPDATE," + myID + "," + x + "," + y;
			
    		byte[] buf = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, ipServer, UDP_PORT);
			client.send(packet);						
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}

