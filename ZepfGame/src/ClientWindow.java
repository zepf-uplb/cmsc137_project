import java.awt.*;

import javax.swing.*;


public class ClientWindow {
	
	private JButton startButton = new JButton("Start");
	private JTextField nameText = new JTextField(17);
	private JTextField serverText = new JTextField(15);

	public ClientWindow(){
		JFrame frame = new JFrame();
		Container contentPane = frame.getContentPane();
		JPanel[] panel = new JPanel[3];	
		JLabel nameLabel = new JLabel("Name : ");		
		JLabel serverLabel = new JLabel("Server IP : ");
		
		frame.setTitle("Zepf Client");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(250, 175);
		frame.setResizable(false);
		
		contentPane.setLayout(new GridLayout(3,0));
		
		for(int i = 0; i < 3; i++){
			panel[i] = new JPanel();
			contentPane.add(panel[i]);
		}
		
		panel[0].setLayout(new FlowLayout(FlowLayout.CENTER, 5, 25));
		panel[0].add(nameLabel);
		panel[0].add(nameText);
		
		panel[1].add(serverLabel);
		panel[1].add(serverText);
		
		panel[2].add(startButton);
		
		frame.setVisible(true);
	}
	
	public JButton getStartButton(){
		return this.startButton;
	}
	
	public JTextField getNameText(){
		return this.nameText;
	}
	
	public JTextField getServerText(){
		return this.serverText;
	}
	
}