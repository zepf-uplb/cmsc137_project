import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends Thread implements ActionListener, Constants{	
	
	private Socket client; 
	private DataOutputStream out;
	private DataInputStream in;
	private OutputStream outToServer;
	private InputStream inFromServer;
	
	private JTextField messageField;
	private JTextArea chatWindow;
	
	private String name;	
	
	public ChatClient(GameWindow window, String ipServer, String name){				
		try {	
			chatWindow = window.getChatWindow();
			messageField = window.getMessageField();
			messageField.setActionCommand("enter_message");
			messageField.addActionListener(this);	
			
			client = new Socket(ipServer, TCP_PORT);
			
			outToServer = client.getOutputStream();
			out = new DataOutputStream(outToServer);
			
			inFromServer = client.getInputStream();
			in = new DataInputStream(inFromServer);		
			
			this.name = name;
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	@Override
	public void run() {				
		try {	
			while(true){			
				updateChatWindow(in.readUTF());		
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if ("enter_message".equals(ae.getActionCommand())) {
			try {
				out.writeUTF(name + ": " + messageField.getText());
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}		
	}
	
	private void updateChatWindow(String message){
		chatWindow.setText(chatWindow.getText()+"\n"+message);
	}

}
