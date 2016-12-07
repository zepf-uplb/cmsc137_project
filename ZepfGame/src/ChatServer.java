import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer extends Thread implements Constants{
	
	private static int currentClients;	
	private static DataOutputStream[] out;
	private static DataInputStream[] in;

	@SuppressWarnings("unused")
	private Socket socket;
	private int id;	
	
	public static void init(int numOfPlayers) {
		try {					
			out = new DataOutputStream[numOfPlayers]; 
			in = new DataInputStream[numOfPlayers];
			
			ServerSocket serverSocket  = new ServerSocket(TCP_PORT);
			serverSocket.setSoTimeout(0);
			
			Socket s = null;			
			currentClients = 0;
			
			for(int i = 0; i < numOfPlayers; i++, currentClients++){
				s = serverSocket.accept();		
				new ChatServer(s, i).start();
			}
			
			serverSocket.close();		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ChatServer(Socket socket, int id){		
		this.socket = socket;
		this.id = id;
		try {
			in[id] = new DataInputStream(socket.getInputStream());
			out[id] = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			while(true){			
				String message = in[id].readUTF();				
				broadCastMessage(message);			
			}
		} catch (IOException e) {
			//e.printStackTrace();
		}				
	}
	
	private void broadCastMessage(String message){
		for(int i = 0; i < currentClients; i++){
			try {
				out[i].writeUTF(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
}
