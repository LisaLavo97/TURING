package latoClient.RequestSender;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;
import latoClient.FormInitializer.LoggedFormInitializer;
import view.InviteForm;

/* Thread che invia al Server la richiesta di invito di un altro utente a collaborare ad un documento
 * 
 * @author Lisa Lavorati mat:535658 
 */


public class InviteRequestSender extends Thread {
	
	private InviteForm form;
	private Socket clientSocket; //Socket lato client per le conn TCP
	private DataOutputStream outToServer; //Stream per mandare dati al server
	private BufferedReader inFromServer; //Stream per ricevere dati dal server
	private String title;
	
	
	public InviteRequestSender(InviteForm f, Socket socket, DataOutputStream out, BufferedReader in) {
		
		if(f == null || socket == null || out == null || in == null)
			throw new NullPointerException();
		
		if(socket.isClosed())
			throw new IllegalArgumentException();
		
		form = f;
		clientSocket = socket;
		outToServer = out;
		inFromServer = in;
		title = f.getTitle();
		
	}
	
	
	public void run() {
		
		String op = "INVITE" + '\n';
		try {
			outToServer.writeBytes(op);
			
			String username = form.getUsername() + '\n';
			String docName = form.getDocName() + '\n';
			outToServer.writeBytes(username);
			outToServer.writeBytes(docName);
			
			String tmp = inFromServer.readLine();
			
			if(tmp.contains("Unregistered"))
				JOptionPane.showMessageDialog(null, "This user is not registered.");
			
			else if(tmp.contains("Permission denied"))
				JOptionPane.showMessageDialog(null, "Permission denied, you are not the document creator");
			
			else if(tmp.contains("Non-existent"))
				JOptionPane.showMessageDialog(null, "This document doesn't exist");
			
			else if(tmp.contains("User is a collaborator"))
				JOptionPane.showMessageDialog(null, "This user is already a collaborator");
			
			else if(tmp.contains("Sent")){
				JOptionPane.showMessageDialog(null, "Invitation successfully sent");
				form.setVisible(false);
				form.closeWindow();
				LoggedFormInitializer logForm = new LoggedFormInitializer(title,clientSocket,outToServer,inFromServer);
			}
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
}
