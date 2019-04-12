package view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class NewDocForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JLabel text;
	private JTextField textField;
	private JTextField secNum;
	private JButton createBtn;
	private JButton menuBtn;
	private String username;

	
	
	public NewDocForm(String name) { 
		username = name;
		initializeWindowContent();
	}
	
	
	public void initializeWindowContent() {
		
		frame = new JFrame();
		frame.setTitle(username);
		frame.getContentPane().setBackground(new Color(255, 160, 122));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		text = new JLabel();
		text.setFont(new Font("Optima", Font.BOLD, 25));
		text.setBackground(new Color(255, 160, 122));
		text.setText("Insert the document name:");
		text.setBounds(81, 20, 334, 37);
		frame.getContentPane().add(text);
		
		textField = new JTextField();
		textField.setBackground(new Color(255, 222, 173));
		textField.setFont(new Font("Optima", Font.PLAIN, 23));
		textField.setBounds(81, 70, 295, 37);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		secNum = new JTextField();
		secNum.setFont(new Font("Optima", Font.PLAIN, 17));
		secNum.setBackground(new Color(255, 218, 185));
		secNum.setBounds(272, 133, 81, 37);
		frame.getContentPane().add(secNum);
		secNum.setColumns(10);
		
		createBtn = new JButton("Create");
		createBtn.setFont(new Font("Optima", Font.BOLD, 18));
		createBtn.setBackground(new Color(255, 218, 185));
		createBtn.setBounds(164, 183, 125, 29);
		frame.getContentPane().add(createBtn);
		
		menuBtn = new JButton("Return to the main menu");
		menuBtn.setFont(new Font("Optima", Font.BOLD, 16));
		menuBtn.setBackground(new Color(255, 160, 122));
		menuBtn.setBounds(115, 215, 227, 29);
		frame.getContentPane().add(menuBtn);
		
		JLabel label = new JLabel("Number of sections:");
		label.setFont(new Font("Optima", Font.BOLD, 20));
		label.setBounds(91, 141, 180, 16);
		frame.getContentPane().add(label);
		
		
	}
	
	
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
	public String getTitle() {
		return username;
	}
	
	public JButton getCreateButton() {
		return createBtn;
	}

	
	public JButton getMenuButton() {
		return menuBtn;
	}
	
	
	public String getDocName() {
		return textField.getText();
	}
	
	
	public String getSecNumber() {
		return secNum.getText();
	}

	
	public void closeWindow() {
		frame.dispose();
	}
}


