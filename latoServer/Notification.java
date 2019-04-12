package latoServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

/* Listener che si mette in attesa di ricezione di inviti online all'utente.

* @author Lisa Lavorati mat:535658 */

public class Notification extends Thread {

	private String username;
	private Socket socket;
	private DataOutputStream out;
	
	public Notification(String name, Socket notificationSocket) {
		username = name;
		socket = notificationSocket;
	}
	
	public void run() {
		
		try {
			out = new DataOutputStream(socket.getOutputStream());
			
			while(true) {
				
				User user = UserRequestHandler.getUtenteOnline(username);
				
				if(user == null) {
					break;
				}	
				
				if(user.getOnlineInvitations()) {
					int i;
					LinkedList<Document> list = user.getOnlineInvitationList();
					for(i=0; i< user.getOnlineInvitationList().size(); i++) {
						out.writeBytes("INIZIO" + '\n');
						String creator = list.get(i).getCreator().getUsername();
						String document = list.get(i).getDocumentName();
						out.writeBytes(creator + '\n');
						out.writeBytes(document + '\n');
						
					}
					user.clearOnlineInvitations();
					user.setOnlineInvitations(false);
				}
			}
			out.close();
			socket.close();
		}
		
		catch (IOException e) {
			try {
				out.close();
				socket.close();
				return;
			}
			catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		try {
			out.close();
			socket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
