package latoClient.RequestSender;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

import latoClient.FormInitializer.LoggedFormInitializer;
import view.NewDocForm;

/* Thread che invia al Server la richiesta di creazione di un nuovo documento
 * 
 * @author Lisa Lavorati mat:535658 
 */


public class NewDocRequestSender extends Thread {

	private NewDocForm form;
	private Socket clientSocket; //Socket lato client per le conn TCP
	private DataOutputStream outToServer; //Stream per mandare dati al server
	private BufferedReader inFromServer; //Stream per ricevere dati dal server
	
	
	public NewDocRequestSender(NewDocForm f, Socket socket, DataOutputStream out, BufferedReader in) {
		
		if(f == null || socket == null || out == null || in == null)
			throw new NullPointerException();
		
		if(socket.isClosed())
			throw new IllegalArgumentException();
		
		form = f;
		clientSocket = socket;
		outToServer = out;
		inFromServer = in;
		
	}
	
	
	public void run() {
		
		String op = "NEWDOC" + '\n';
		
		try {
			
			outToServer.writeBytes(op);
			
			String docName = form.getDocName() + '\n';
			String secNum = form.getSecNumber() + '\n';
			
			//Invio nome del documento e numero di sezioni inseriti dall'utente
			outToServer.writeBytes(docName);
			outToServer.writeBytes(secNum);
			
			String temp = inFromServer.readLine();
			
			if(temp.contains("Already"))
				JOptionPane.showMessageDialog(null, "Already existent document.");
			
			if(temp.contains("Invalid secNumber"))
				JOptionPane.showMessageDialog(null, "Number of sections must be greater than 0.");
			
			else if(temp.contains("successful")) {
				JOptionPane.showMessageDialog(null, "Document created successfully.");
				form.setVisible(false);
				form.closeWindow();
				LoggedFormInitializer initializer = new LoggedFormInitializer(form.getTitle(),clientSocket,outToServer,inFromServer);
			}
			
			else if(temp.contains("NaN"))
				JOptionPane.showMessageDialog(null, "Number of sections must contain a number");
			
			
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
