package latoClient.RequestSender;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import view.DocListForm;
import view.LoggedForm;


/* Thread che invia al Server la richiesta di ricezione della lista di documenti
 * che l'utente è autorizzato ad accedere e modificare
 * 
 * @author Lisa Lavorati mat:535658 
 */


public class DocListRequestSender extends Thread{
	
	private LoggedForm logForm;
	private DocListForm form;
	private Socket clientSocket; //Socket lato client per le conn TCP
	private DataOutputStream outToServer; //Stream per mandare dati al server
	private BufferedReader inFromServer; //Stream per ricevere dati dal server
	
	public DocListRequestSender (DocListForm f, LoggedForm lForm, Socket socket, DataOutputStream out, BufferedReader in) {
		
		if(lForm == null || socket == null || out == null || in == null)
			throw new NullPointerException();
		
		if(socket.isClosed())
			throw new IllegalArgumentException();
		
		logForm = lForm;
		form = f;
		clientSocket = socket;
		outToServer = out;
		inFromServer = in;
	}
	
	
	public void run() {
		
		String op = "DOCLIST" + '\n';	
		try {
			outToServer.writeBytes(op);
			String tmp = inFromServer.readLine();
			//Ricevo dal Server la lista dei documenti con le relativi informazioni annesse
			while(!tmp.equals("STOP")) {
				form.getTextArea().append("Document: " + tmp + '\n');
				tmp = inFromServer.readLine();
				form.getTextArea().append("Creator: " + tmp + '\n');
				form.getTextArea().append("Collaborators: " + '\n');
				while(!tmp.equals("END_DOCUMENT")) {
					tmp = inFromServer.readLine();
					if(!tmp.equals("END_DOCUMENT"))
						form.getTextArea().append(tmp + "  ");
				}
				tmp = inFromServer.readLine();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		logForm.setVisible(false);
		logForm.closeWindow();
		form.setVisible(true);	
	}

}
