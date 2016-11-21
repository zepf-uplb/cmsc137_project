import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


public class Network extends Listener implements Constants {

	Client client;
	String ip;
	boolean gameStart = false, deathFlag = false;
	
	public Network(String ip){
		this.ip = ip;
	}
	
	public void connect(){
		client = new Client();
		client.getKryo().register(PacketUpdateXY.class);
		client.getKryo().register(PacketAddPlayer.class);
		client.getKryo().register(PacketRemovePlayer.class);
		client.getKryo().register(GameStart.class);
		client.getKryo().register(DeathFlag.class);
		client.addListener(this);
		
		client.start();
		try {
			client.connect(5000, ip, UDP_PORT, UDP_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void received(Connection c, Object o){
		
		if(o instanceof DeathFlag){
			deathFlag = true;
			
		}else if(o instanceof PacketUpdateXY){
			PacketUpdateXY packet = (PacketUpdateXY) o;
			ClientProgram.players.get(packet.id).x = packet.x;
			ClientProgram.players.get(packet.id).y = packet.y;
			
		}else if(o instanceof GameStart){
			gameStart = true;	
			
		}else if(o instanceof PacketAddPlayer){
			PacketAddPlayer packet = (PacketAddPlayer) o;
			MPPlayer newPlayer = new MPPlayer();
			ClientProgram.players.put(packet.id, newPlayer);
			
		}else if(o instanceof PacketRemovePlayer){
			PacketRemovePlayer packet = (PacketRemovePlayer) o;
			ClientProgram.players.remove(packet.id);
			
		}			
	}
}
