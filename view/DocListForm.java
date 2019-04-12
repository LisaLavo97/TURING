package view;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Font;


public class DocListForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JFrame frame;	
	private JTextArea textArea;
	private JButton menuBtn;
	private String username;
	
	public DocListForm(String name) {
		
		username = name;
		initializeWindowContent();
	}
	
	
	private void initializeWindowContent() {
		
		frame = new JFrame();
		frame.setTitle(username);
		frame.getContentPane().setBackground(new Color(255, 160, 122));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(new Color(255, 218, 185));
		textArea.setBounds(35, 28, 380, 198);
		/*JScrollPane scroll = new JScrollPane (textArea);
	    scroll.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
	    frame.getContentPane().add(scroll);*/
		frame.getContentPane().add(textArea);
		
		menuBtn = new JButton("Return to the main menu");
		menuBtn.setFont(new Font("Optima", Font.BOLD, 16));
		menuBtn.setBounds(120, 238, 205, 29);
		frame.getContentPane().add(menuBtn);
		
	}
	
	
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}
	
	public JButton getMenuButton() {
		return menuBtn;
	}
	
	public void closeWindow() {
		frame.dispose();
	}
}
