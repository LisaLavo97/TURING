package view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class ShowForm extends JFrame{


	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JTextField docName;
	private JTextField secNum;
	private JButton btnDocument;
	private JButton btnSection;
	private JButton menuBtn;
	private String username;
	
	
	public ShowForm(String name) {
		
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
		
		JLabel text = new JLabel();
		text.setFont(new Font("Optima", Font.BOLD, 24));
		text.setBackground(new Color(255, 160, 122));
		text.setText("Which document do you want to read?");
		text.setBounds(23, 20, 438, 37);
		frame.getContentPane().add(text);
		
		docName = new JTextField();
		docName.setBackground(new Color(255, 222, 173));
		docName.setFont(new Font("Optima", Font.PLAIN, 23));
		docName.setBounds(74, 55, 295, 37);
		frame.getContentPane().add(docName);
		docName.setColumns(10);
		
		secNum = new JTextField();
		secNum.setFont(new Font("Optima", Font.PLAIN, 17));
		secNum.setBackground(new Color(255, 218, 185));
		secNum.setBounds(249, 158, 81, 37);
		frame.getContentPane().add(secNum);
		secNum.setColumns(10);
		
		menuBtn = new JButton("Return to the main menu");
		menuBtn.setFont(new Font("Optima", Font.BOLD, 16));
		menuBtn.setBackground(new Color(255, 160, 122));
		menuBtn.setBounds(113, 243, 227, 29);
		frame.getContentPane().add(menuBtn);
		
		JLabel lblWhichSection = new JLabel("Select a section");
		lblWhichSection.setFont(new Font("Optima", Font.BOLD, 22));
		lblWhichSection.setBounds(90, 167, 180, 16);
		frame.getContentPane().add(lblWhichSection);
		
		btnDocument = new JButton("Get the whole document");
		btnDocument.setFont(new Font("Optima", Font.BOLD, 16));
		btnDocument.setBounds(113, 104, 227, 29);
		frame.getContentPane().add(btnDocument);
		
		btnSection = new JButton("Get the section");
		btnSection.setFont(new Font("Optima", Font.BOLD, 16));
		btnSection.setBounds(133, 207, 177, 24);
		frame.getContentPane().add(btnSection);
		
	}
	
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
	
	public JButton getShowDocumentButton() {
		return btnDocument;
	}

	
	public JButton getShowSectionButton() {
		return btnSection;
	}
	
	
	public JButton getMenuButton() {
		return menuBtn;
	}
	
	
	public String getDocName() {
		return docName.getText();
	}
	
	
	public String getSecNumber() {
		return secNum.getText();
	}

	
	public void closeWindow() {
		frame.dispose();
	}
}