package view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class TuringForm extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JTextField usernameField;
	private JTextField pswField;
	private JButton btnLogin;
	private JButton btnRegister;
	
	public TuringForm() {
		initializeWindowContent();
	}
	
	private void initializeWindowContent() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 160, 122));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblTuring = new JLabel("TURING");
		lblTuring.setFont(new Font("Bradley Hand", Font.BOLD, 49));
		lblTuring.setBounds(115, 10, 225, 99);
		frame.getContentPane().add(lblTuring);
		
		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setBounds(72, 121, 87, 16);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setBounds(72, 166, 91, 16);
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		frame.getContentPane().add(lblNewLabel_1);
		
		usernameField = new JTextField();
		usernameField.setBackground(new Color(255, 222, 173));
		usernameField.setBounds(192, 118, 183, 26);
		frame.getContentPane().add(usernameField);
		usernameField.setColumns(10);
		
		pswField = new JTextField();
		pswField.setBackground(new Color(255, 222, 173));
		pswField.setBounds(192, 163, 183, 26);
		frame.getContentPane().add(pswField);
		pswField.setColumns(10);
		
		btnLogin = new JButton("Login");
		btnLogin.setBackground(Color.WHITE);
		btnLogin.setForeground(new Color(0, 0, 0));
		btnLogin.setBounds(91, 223, 111, 26);
		btnLogin.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		frame.getContentPane().add(btnLogin);
		
		btnRegister = new JButton("Register");
		btnRegister.setBounds(243, 222, 117, 29);
		btnRegister.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		frame.getContentPane().add(btnRegister);
		
	}
	
	
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
	
	public JButton getLoginButton() {
		return btnLogin;
	}

	
	public JButton getRegisterButton() {
		return btnRegister;
	}
	

	public String getUsernameField() {
		return usernameField.getText();
	}

	
	public String getPasswordField() {
		return pswField.getText();
	}
	
	
	public void closeWindow() {
		frame.dispose();
	}
	
	
}
