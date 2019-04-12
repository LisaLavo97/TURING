package latoClient.FormInitializer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;
import latoClient.RequestSender.NewDocRequestSender;
import view.LoggedForm;
import view.NewDocForm;


/*Classe che si occupa di creare l'interfaccia grafica per la richiesta di creazione di un nuovo
 * documento e di inizializzare le azioni relative ai suoi bottoni
 *
 * @author Lisa Lavorati mat:535658 
 */


public class NewDocFormInitializer {

	private static LoggedForm logForm;
	private static NewDocForm newDocForm;
	private static Socket clientSocket;
	private static DataOutputStream outToServer;
	private static BufferedReader inFromServer;
	
	
	public NewDocFormInitializer (LoggedForm lForm, Socket socket, DataOutputStream out, BufferedReader in) throws IOException {
		logForm = lForm;
		//Crea la finestra
		newDocForm = new NewDocForm(logForm.getTitle());
		newDocForm.setVisible(true);
		clientSocket = socket;
		outToServer = out;
		inFromServer = in;
		//Invoca il metodo che inizializza i bottoni
		initNewDocFormListeners();
	}
	
	
	private void initNewDocFormListeners() {
		
		//Inizializzo le operazioni relative bottone Crea
		newDocForm.getCreateButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				if(newDocForm.getDocName().length() == 0 || newDocForm.getSecNumber().length() == 0) 
					JOptionPane.showMessageDialog(null, "Enter document name and number of sections.");
				else {
					//Faccio partire il thread per la richiesta di creazione di un nuovo documento
					Thread newDocRequest = new NewDocRequestSender(newDocForm,clientSocket,outToServer,inFromServer);
					newDocRequest.start();
				}
			}
		});
		
		
		//Inizializzo le operazioni relative bottone MenuButton
		newDocForm.getMenuButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				try {
					newDocForm.setVisible(false);
					newDocForm.closeWindow();
					//Creo un oggetto di tipo LoggedFormInitializer che crea ed inizializza la finestra del menu 
					LoggedFormInitializer initializer = new LoggedFormInitializer(logForm.getTitle(),clientSocket,outToServer,inFromServer);
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
	}
}
