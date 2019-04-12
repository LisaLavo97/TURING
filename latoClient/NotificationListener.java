package latoClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.JOptionPane;
import Config.Config;

/* Classe che gestisce la notifica degli inviti online all'utente.
 * Crea una socket di supporto sulla quale gli inviti vengono inviati

* @author Lisa Lavorati mat:535658 */

public class NotificationListener extends Thread {

	private static Socket notificationSocket;
	private static BufferedReader in;
	
	public NotificationListener() {
	
	}
	
	
	public void run() {
		
		try {
			notificationSocket = new Socket("localHost", Config.TCP_SECOND_PORT);
		    in = new BufferedReader(new InputStreamReader(notificationSocket.getInputStream()));
		    
		    while(true) {
		    		String notifications = in.readLine();
		    		if(notifications != null) {
		    			if(notifications.equals("INIZIO")) {
			    			String username = in.readLine();
			    			String document = in.readLine();
			    			JOptionPane.showMessageDialog(null, "You've been invited to collaborate on document '" + document + "' by the user "+ username);
		    			}
		    		}	
		    		else
		    			break;
		    }
		}
		
		catch (IOException e) {
			try {
				in.close();
				notificationSocket.close();
			}
			catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}
}
