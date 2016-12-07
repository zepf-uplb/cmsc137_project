import java.awt.*;

import javax.swing.*;


public class ClientWindow {
	
	private JButton startButton = new JButton("Start");
	private JButton manualButton = new JButton("Game Manual");
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
		panel[2].add(manualButton);
		
		frame.setVisible(true);
	}
	
	public JButton getStartButton(){
		return this.startButton;
	}
	
	public JButton getManualButton(){
		return this.manualButton;
	}
	
	public JTextField getNameText(){
		return this.nameText;
	}
	
	public JTextField getServerText(){
		return this.serverText;
	}
	
	public static void viewManual(){
		JFrame frame = new JFrame();
		Container contentPane = frame.getContentPane();
		JTextArea manualText = new JTextArea(10, 10);
		
		frame.setTitle("Zepf Manual");
		frame.setSize(250, 250);
		frame.setResizable(false);
		
		manualText.setEditable(false);
		manualText.setText("Welcome to Zepf!\n\nYou control a line that indefinitely elongates\nuntil you bump on a line. The line can be\nyour line"
				+ ", other's line or the border-line. You\ngain points if you are the last player\nremaining. The first player to reach 3 points"
				+ "\nwould be considered the winner. Paths \ntaken during the previous rounds remain\nas distractions. Have fun!"
				+ "\n\nControls: W, A, S, D");
		contentPane.add(manualText);
		
		frame.setVisible(true);
	}
	
}
