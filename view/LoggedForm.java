package view;

import java.awt.Image;
import javax.swing.JFrame;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.io.FileInputStream;
import java.io.IOException;
import java.awt.Color;
import java.awt.Font;

public class LoggedForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JFrame logFrame;
	private JButton btnCreateNewDoc;
	private JButton btnGetDocList;
	private JButton btnModifyDoc;
	private JButton btnInvite ;
	private JButton btnShow;
	private JButton btnLogout;
	private String username;
	
	public LoggedForm(String name) throws IOException {
		username = name;
		initLoggedForm();
	}

	
	public void initLoggedForm() throws IOException {
		
		logFrame = new JFrame();
		logFrame.setTitle(username);
		logFrame.getContentPane().setBackground(new Color(255, 160, 122));
		logFrame.setBounds(100, 100, 450, 300);
		logFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		logFrame.getContentPane().setLayout(null);
		

		Image newDocIcon = ImageIO.read(new FileInputStream("icons/newDoc.png"));
		Image docListIcon = ImageIO.read(new FileInputStream("icons/docList.png"));
		Image modifyDocIcon = ImageIO.read(new FileInputStream("icons/modifyDoc.png"));
		Image inviteIcon = ImageIO.read(new FileInputStream("icons/invite.png"));
		Image showIcon = ImageIO.read(new FileInputStream("icons/show.png"));
		Image logoutIcon = ImageIO.read(new FileInputStream("icons/logoutIcon.png"));
		
		newDocIcon = newDocIcon.getScaledInstance(100,100, Image.SCALE_DEFAULT);
		docListIcon = docListIcon.getScaledInstance(100,100, Image.SCALE_DEFAULT);
		modifyDocIcon = modifyDocIcon.getScaledInstance(100,100, Image.SCALE_DEFAULT);
		inviteIcon = inviteIcon.getScaledInstance(100,100, Image.SCALE_DEFAULT);
		showIcon = showIcon.getScaledInstance(100,100, Image.SCALE_DEFAULT);
		logoutIcon = logoutIcon.getScaledInstance(100,100, Image.SCALE_DEFAULT);
		
		
		btnCreateNewDoc = new JButton();
		btnCreateNewDoc.setBackground(new Color(255, 218, 185));
		btnCreateNewDoc.setIcon(new ImageIcon(newDocIcon));
		btnCreateNewDoc.setForeground(Color.WHITE);
		btnCreateNewDoc.setBounds(30, 24, 100, 108);
		logFrame.getContentPane().add(btnCreateNewDoc);
		
		btnGetDocList = new JButton();
		btnGetDocList.setBackground(new Color(255, 218, 185));
		btnGetDocList.setIcon(new ImageIcon(docListIcon));
		btnGetDocList.setForeground(Color.WHITE);
		btnGetDocList.setBounds(180, 24, 100, 108);
		logFrame.getContentPane().add(btnGetDocList);
		
		btnModifyDoc = new JButton();
		btnModifyDoc.setBackground(new Color(255, 218, 185));
		btnModifyDoc.setIcon(new ImageIcon(modifyDocIcon));
		btnModifyDoc.setForeground(Color.WHITE);
		btnModifyDoc.setBounds(30, 144, 100, 108);
		logFrame.getContentPane().add(btnModifyDoc);
		
		btnInvite = new JButton();
		btnInvite.setBackground(new Color(255, 218, 185));
		btnInvite.setIcon(new ImageIcon(inviteIcon));
		btnInvite.setForeground(Color.WHITE);
		btnInvite.setBounds(180, 144, 100, 108);
		logFrame.getContentPane().add(btnInvite);
		
		btnShow = new JButton();
		btnShow.setBackground(new Color(255, 218, 185));
		btnShow.setIcon(new ImageIcon(showIcon));
		btnShow.setForeground(Color.WHITE);
		btnShow.setBounds(315, 24, 100, 108);
		logFrame.getContentPane().add(btnShow);
		
		btnLogout = new JButton();
		btnLogout.setBackground(new Color(255, 218, 185));
		btnLogout.setIcon(new ImageIcon(logoutIcon));
		btnLogout.setForeground(Color.WHITE);
		btnLogout.setBounds(315, 144, 100, 108);
		logFrame.getContentPane().add(btnLogout);
		
	}
	
	
	public void setVisible(boolean b) {
		logFrame.setVisible(b);
	}

	
	public String getTitle() {
		return username;
	}
	
	public JButton getNewDocButton() {
		return btnCreateNewDoc;
	}

	
	public JButton getDocListButton() {
		return btnGetDocList;
	}
	
	
	public JButton getModifyDocButton() {
		return btnModifyDoc;
	}
	
	
	public JButton getInviteButton() {
		return btnInvite;
	}
	
	
	public JButton getShowButton() {
		return btnShow;
	}
	
	
	public JButton getLogoutButton() {
		return btnLogout;
	}

	
	public void closeWindow() {
		logFrame.dispose();
	}
	
}
