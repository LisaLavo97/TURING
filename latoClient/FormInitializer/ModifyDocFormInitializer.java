package latoClient.FormInitializer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;
import latoClient.RequestSender.ModifyRequestSender;
import view.ModifyDocForm;


/*Classe che si occupa di creare l'interfaccia grafica per la richiesta di modifica di una
 * sezione di un documento e di inizializzare le azioni relative ai suoi bottoni
 *
 * @author Lisa Lavorati mat:535658 
 */


public class ModifyDocFormInitializer {

	private static ModifyDocForm modifyDocForm;
	private static Socket clientSocket;
	private static DataOutputStream outToServer;
	private static BufferedReader inFromServer;
	private String username;
	
	
	public ModifyDocFormInitializer (String name, Socket socket, DataOutputStream out, BufferedReader in) throws IOException {
		username = name;
		//Crea la finestra
		modifyDocForm = new ModifyDocForm(username);
		modifyDocForm.setVisible(true);
		clientSocket = socket;
		outToServer = out;
		inFromServer = in;
		//Invoca il metodo che inizializza i bottoni
		initModifyDocFormListeners();
	}
	
	
	private void initModifyDocFormListeners() {
		
		//Inizializzo le operazioni relative bottone ModifyButton
		modifyDocForm.getModifyButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				if(modifyDocForm.getDocName().length() == 0 || modifyDocForm.getSecNumber().length() == 0) 
					JOptionPane.showMessageDialog(null, "Enter document name and number of section.");
				else {
					//Faccio partire il thread per la richiesta di modifica di una sezione
					Thread modifyRequest = new ModifyRequestSender(modifyDocForm,clientSocket,outToServer,inFromServer);
					modifyRequest.start();
				}
			}
		});
		
		//Inizializzo le operazioni relative bottone MenuButton
		modifyDocForm.getMenuButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				try {
					modifyDocForm.setVisible(false);
					modifyDocForm.closeWindow();
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
