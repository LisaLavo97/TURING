package latoClient.RequestSender;

import view.TuringForm;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import latoClient.NotificationListener;
import latoClient.FormInitializer.LoggedFormInitializer;

/* Thread che invia la richiesta di login al Server.
 * Se l'operazione ha successo, viene attivato un thread listener per ricevere le notifiche online
 * degli inviti da parte del Server tramite una socket di supporto
 * 
 * @author Lisa Lavorati mat:535658 
 */

public class LoginRequestSender extends Thread {
	
	private TuringForm form;
	private Socket clientSocket; //Socket lato client per le conn TCP
	private DataOutputStream outToServer; //Stream per mandare dati al server
	private BufferedReader inFromServer; //Stream per ricevere dati dal server
	
	
	public LoginRequestSender(TuringForm tForm, Socket socket, DataOutputStream out, BufferedReader in) {
		
		if(tForm == null ||socket == null || out == null || in == null)
			throw new NullPointerException();
		
		if(socket.isClosed())
			throw new IllegalArgumentException();
		
		form = tForm;
		clientSocket = socket;
		outToServer = out;
		inFromServer = in;
		
	}
	
	
	public void run() {
		
		String op = "LOGIN" + '\n';
		
		try {
			outToServer.writeBytes(op);
			String username = form.getUsernameField() + '\n';
			String password = form.getPasswordField() + '\n';
			outToServer.writeBytes(username);
			outToServer.writeBytes(password);
			
			String temp = inFromServer.readLine();
			
			if(temp.contains("successful")) {
				form.setVisible(false);
				form.closeWindow();
				
				temp = inFromServer.readLine();
				
				//Caso in cui l'operazione di login ha successo e non ci sono inviti pendenti per l'utente
				if(temp.contains("Stop")) {
					LoggedFormInitializer initializer = new LoggedFormInitializer(username,clientSocket,outToServer,inFromServer);
				}
				
				//Caso in cui l'operazione ha successo e ci sono inviti pendenti che l'utente ha ricevuto mentre era offline
				else if(temp.contains("Pending")) {
					LinkedList<String> invitationList = new LinkedList<String>();
					String docName = inFromServer.readLine();
					
					while(!docName.equals("END")) {
						invitationList.add(docName);
						docName = inFromServer.readLine();
					}
					LoggedFormInitializer initializer = new LoggedFormInitializer(username,clientSocket,outToServer,inFromServer,invitationList);
				}
				
				//Attivo il thread listener per le notifiche online degli inviti
				Thread listener = new Thread(new NotificationListener());
				listener.start();
				
					
			}
			
			else if(temp.contains("Wrong"))
				JOptionPane.showMessageDialog(null, "Wrong password.");
			
			else if(temp.contains("already online"))
				JOptionPane.showMessageDialog(null, "This user is already online.");
			
			else if(temp.contains("User is editing"))
				JOptionPane.showMessageDialog(null, "This user is already online and editing a section.");
			
			else if(temp.contains("Error"))
				JOptionPane.showMessageDialog(null, "Username or Password unknown.");
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}


}
