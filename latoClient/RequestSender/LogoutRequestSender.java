package latoClient.RequestSender;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;
import view.LoggedForm;


/* Thread che invia la richiesta di logout al Server.

 * @author Lisa Lavorati mat:535658 
 */


public class LogoutRequestSender extends Thread {
	
	private LoggedForm logForm;
	private Socket clientSocket; //Socket lato client per le conn TCP
	private DataOutputStream outToServer; //Stream per mandare dati al server
	private BufferedReader inFromServer; //Stream per ricevere dati dal server
	
	
	public LogoutRequestSender(LoggedForm lForm, Socket socket, DataOutputStream out, BufferedReader in) {
		
		if(lForm == null || socket == null || out == null || in == null)
			throw new NullPointerException();
		
		if(socket.isClosed())
			throw new IllegalArgumentException();
		
		logForm = lForm;
		clientSocket = socket;
		outToServer = out;
		inFromServer = in;
		
	}
	
	
	public void run() {
		
		String op = "LOGOUT" + '\n';
		
		try {
			
			outToServer.writeBytes(op);
			String temp = inFromServer.readLine();
			
			if(temp.contains("successful")) {
				JOptionPane.showMessageDialog(null, "Logout success");
				logForm.setVisible(false);
				logForm.closeWindow();
				
				clientSocket.close();
				outToServer.close();
				inFromServer.close();
				
			}
			
			else if(temp.contains("Error"))
				JOptionPane.showMessageDialog(null, "Error in logout");
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}


}

