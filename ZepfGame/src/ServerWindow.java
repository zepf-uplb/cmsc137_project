import java.awt.*;
import javax.swing.*;


public class ServerWindow {	
	
	private JLabel statusText = new JLabel("OFFLINE");
	private JSpinner playerNum = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
	private JButton startButton = new JButton("Start");
	private JButton endButton = new JButton("End");
	
	public ServerWindow(){		
		
		JFrame frame = new JFrame();
		Container contentPane = frame.getContentPane();
		JPanel[] panel = new JPanel[3];	
		JLabel statusLabel = new JLabel("Status : ");
		
		JLabel playerLabel = new JLabel("Number of Players : ");		
		
		frame.setTitle("Zepf Server");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(250, 175);
		frame.setResizable(false);
		
		contentPane.setLayout(new GridLayout(3,0));
		
		for(int i = 0; i < 3; i++){
			panel[i] = new JPanel();
			contentPane.add(panel[i]);
		}
		
		panel[0].setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
		panel[0].add(statusLabel);
		panel[0].add(statusText);
		
		playerNum.setPreferredSize(new Dimension(40, 25));		
		
		panel[1].add(playerLabel);
		panel[1].add(playerNum);
		
		panel[2].add(startButton);
		//panel[2].add(endButton);
		
		frame.setVisible(true);
	}
	
	public JButton getStartButton(){
		return this.startButton;
	}
	
	public JButton getEndButton(){
		return this.endButton;
	}
	
	public JLabel getStatusText(){
		return this.statusText;
	}
	
	public JSpinner getPlayerNum(){
		return this.playerNum;
	}
	
}
