package latoClient.FormInitializer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;
import latoClient.RequestSender.InviteRequestSender;
import view.InviteForm;

/*Classe che si occupa di creare l'interfaccia grafica per invitare un altro utente registrato
 * a collaborare ad un documento di cui si è i creatori e di inizializzare le azioni relative ai suoi bottoni
 *
 * @author Lisa Lavorati mat:535658 
 */

public class InviteFormInitializer {

	private static InviteForm form;
	private static Socket clientSocket;
	private static DataOutputStream outToServer;
	private static BufferedReader inFromServer;
	private String username;
	
	
	public InviteFormInitializer (String name, Socket socket, DataOutputStream out, BufferedReader in) throws IOException {
		
		username = name;
		//Crea la finestra
		form = new InviteForm(username);
		form.setVisible(true);
		clientSocket = socket;
		outToServer = out;
		inFromServer = in;
		//Invoca il metodo che inizializza i bottoni
		initInviteFormListeners();
		
	}
	
	
	private void initInviteFormListeners() {
		
		//Inizializzo le operazioni relative bottone InviteButton
		form.getInviteButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				if(form.getDocName().length() == 0 || form.getUsername().length() == 0) 
					JOptionPane.showMessageDialog(null, "Enter document name and username.");
				else {
					//Faccio partire il thread per la richiesta di invito
					Thread inviteRequest = new InviteRequestSender(form,clientSocket,outToServer,inFromServer);
					inviteRequest.start();
				}
			}
		});
		
		
		//Inizializzo le operazioni relative bottone MenuButton
		form.getMenuButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				try {
					form.setVisible(false);
					form.closeWindow();
					//Creo un oggetto di tipo LoggedFormInitializer che crea ed inizializza la finestra del menu 
					LoggedFormInitializer initializer = new LoggedFormInitializer(username,clientSocket,outToServer,inFromServer);
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
	}
}
