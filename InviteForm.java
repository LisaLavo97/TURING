package view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class InviteForm extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JLabel text;
	private JTextField username;
	private JTextField docName;
	private JButton inviteBtn;
	private JButton menuBtn;
	private String user;


	public InviteForm(String name) {
		user = name;
		initializeWindowContent();
	}
	 
	public void initializeWindowContent() {
	
		frame = new JFrame();
		frame.setTitle(user);
		frame.getContentPane().setBackground(new Color(255, 160, 122));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		text = new JLabel();
		text.setFont(new Font("Optima", Font.BOLD, 24));
		text.setBackground(new Color(255, 160, 122));
		text.setText("Which user do you want to invite?");
		text.setBounds(49, 21, 369, 37);
		frame.getContentPane().add(text);
		
		username = new JTextField();
		username.setBackground(new Color(255, 222, 173));
		username.setFont(new Font("Optima", Font.PLAIN, 23));
		username.setBounds(81, 70, 295, 37);
		frame.getContentPane().add(username);
		username.setColumns(10);
		
		docName = new JTextField();
		docName.setFont(new Font("Optima", Font.PLAIN, 17));
		docName.setBackground(new Color(255, 218, 185));
		docName.setBounds(207, 134, 221, 37);
		frame.getContentPane().add(docName);
		docName.setColumns(10);
		
		inviteBtn = new JButton("Invite");
		inviteBtn.setFont(new Font("Optima", Font.BOLD, 18));
		inviteBtn.setBackground(new Color(255, 218, 185));
		inviteBtn.setBounds(164, 183, 125, 29);
		frame.getContentPane().add(inviteBtn);
		
		menuBtn = new JButton("Return to the main menu");
		menuBtn.setFont(new Font("Optima", Font.BOLD, 16));
		menuBtn.setBackground(new Color(255, 160, 122));
		menuBtn.setBounds(115, 215, 227, 29);
		frame.getContentPane().add(menuBtn);
		
		JLabel lbl = new JLabel("Which document?");
		lbl.setFont(new Font("Optima", Font.BOLD, 22));
		lbl.setBounds(31, 125, 177, 52);
		frame.getContentPane().add(lbl);
		
	}
	
	
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
	
	public String getTitle() {
		return user;	
	}
	
	
	public JButton getInviteButton() {
		return inviteBtn;
	}

	
	public JButton getMenuButton() {
		return menuBtn;
	}
	
	
	public String getDocName() {
		return docName.getText();
	}
	
	
	public String getUsername() {
		return username.getText();
	}

	
	public void closeWindow() {
		frame.dispose();
	}
}
