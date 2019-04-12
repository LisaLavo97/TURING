package latoClient.FormInitializer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
import latoClient.RequestSender.LoginRequestSender;
import latoClient.RequestSender.RegistrationRequestSender;
import view.TuringForm;

/*Classe che si occupa di creare la prima interfaccia grafica di Turing e di inizializzare 
 * le azioni relative ai suoi bottoni
 *
 * @author Lisa Lavorati mat:535658 
 */


public class TuringFormInitializer {

	private static TuringForm form;
	private static Socket clientSocket;
	private static DataOutputStream outToServer;
	private static BufferedReader inFromServer;
	
	
	public TuringFormInitializer(Socket socket, DataOutputStream out, BufferedReader in){
		//Crea la finestra
		form = new TuringForm();
		form.setVisible(true);
		clientSocket = socket;
		outToServer = out;
		inFromServer = in;
		//Invoca il metodo che inizializza i bottoni
		initTuringListeners();
	}
	
	
	private static void initTuringListeners() {
		//Inizializzo le operazioni relative bottone Login
		form.getLoginButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(form.getUsernameField().length() == 0 || form.getPasswordField().length() == 0) 
					JOptionPane.showMessageDialog(null, "Enter username and password.");
				else {
					//Faccio partire il thread per la richiesta di login
					Thread loginRequest = new LoginRequestSender(form,clientSocket,outToServer,inFromServer);
					loginRequest.start();
				}
			}
		});
		
		//Inizializzo le operazioni relative bottone Registrazione
		form.getRegisterButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(form.getUsernameField().length() == 0 || form.getPasswordField().length() == 0) 
					JOptionPane.showMessageDialog(null, "Enter username and password.");
				else {
					//Faccio partire il thread per la richiesta di registrazione
					Thread registrationRequest = new RegistrationRequestSender(form);
					registrationRequest.start();
				}
					
			}
		});
		
	}
}
