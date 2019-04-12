package view;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JTextArea;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;

public class ChatForm extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JTextArea textArea;
	private JButton sendButton; 
	private JButton endEditButton;
	private JTextField textField;
	private String username;
	private String sender;
	
	
	public ChatForm(String name) throws FileNotFoundException, IOException {
		
		username = name;
		sender = "[" + username + "]: ";
		initializeWindowContent();
	}
	
	private void initializeWindowContent() throws FileNotFoundException, IOException {
		
		frame = new JFrame();
		frame.setTitle(username);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		textArea = new JTextArea();
		frame.getContentPane().setBackground(new Color(255, 160, 122));
		frame.getContentPane().setLayout(null);
		textArea.setEditable(false);
		textArea.setBackground(new Color(255, 218, 185));
		textArea.setBounds(18, 17, 399, 187);
		frame.getContentPane().add(textArea);
		
		Image sendIcon = ImageIO.read(new FileInputStream("icons/send.png"));
		Image endEditIcon = ImageIO.read(new FileInputStream("icons/endEdit.png"));
		sendIcon = sendIcon.getScaledInstance(65,56, Image.SCALE_DEFAULT);
		endEditIcon = endEditIcon.getScaledInstance(54,59, Image.SCALE_DEFAULT);
		
		sendButton = new JButton("");
		sendButton.setIcon(new ImageIcon(sendIcon));
		sendButton.setBounds(379, 216, 65, 56);
		frame.getContentPane().add(sendButton);
		
		endEditButton = new JButton("");
		endEditButton.setIcon(new ImageIcon(endEditIcon));
		endEditButton.setBounds(6, 216, 54, 59);
		frame.getContentPane().add(endEditButton);
		
		textField = new JTextField();
		textField.setBackground(new Color(255, 218, 185));
		textField.setBounds(72, 216, 295, 55);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
	}
	

	
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
	
	public JButton getSendButton() {
		return sendButton;
	}
	
	
	public String getMsg() {
		return textField.getText();
	}
	
	
	public JButton getEndEditButton() {
		return endEditButton;
	}
	
	
	public JTextArea getTextArea() {
		return textArea;
	}
	
	
	public String getMessage() {
		String message = textField.getText();
		return sender + message;
	}
	
	
	public void resetText() {
		textField.setText(null);
	}
	
	
	public void closeWindow() {
		frame.dispose();
	}
}
