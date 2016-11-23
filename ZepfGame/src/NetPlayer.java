import java.net.InetAddress;

public class NetPlayer {
	InetAddress address;
	int port;
	String name;
	float x, y;
	
	public NetPlayer(String name,InetAddress address, int port){
		this.address = address;
		this.port = port;
		this.name = name;
	}
}
