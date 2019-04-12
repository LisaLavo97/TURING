package latoClient.RequestSender;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JOptionPane;
import latoServer.RMIServerInterface;
import view.TuringForm;
import Config.Config;

/* Thread che invia al Server la richiesta di registrazione tramite RMI
 * 
 * @author Lisa Lavorati mat:535658 
 */

public class RegistrationRequestSender extends Thread {
	
	private TuringForm form;
	private int RMI_PORT;
	
	
	public RegistrationRequestSender(TuringForm tForm) {
		form = tForm;
		RMI_PORT = Config.RMI_PORT;
	}
	
	
	public void run() {
	
		try {
		
			String newUsername = form.getUsernameField();
			String newPassword = form.getPasswordField();
			
			//Cerco lo stub dell'oggetto remoto
			Registry registry = LocateRegistry.getRegistry(RMI_PORT);
			RMIServerInterface server = (RMIServerInterface) registry.lookup("REGISTRATION_SERVICE");
			
			//Adesso posso invocare i metodi dell'oggetto remoto come se fossero locali
			
			if(server.isRegistered(newUsername))
				JOptionPane.showMessageDialog(null, "Already registered user.");
			
			else {

				server.registrationRequest(newUsername, newPassword);
				JOptionPane.showMessageDialog(null, "Successful registration.");
				
			}
	
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
	}
}

