package latoClient.FormInitializer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import latoClient.RequestSender.ShowDocRequestSender;
import latoClient.RequestSender.ShowSecRequestSender;
import view.ShowForm;

/*Classe che si occupa di creare l'interfaccia grafica per la richiesta di visualizzazione di un 
 * documento o di una sezione e di inizializzare le azioni relative ai suoi bottoni
 *
 * @author Lisa Lavorati mat:535658 
 */

public class ShowFormInitializer {

	private ShowForm form;
	private static Socket clientSocket;
	private static DataOutputStream outToServer;
	private static BufferedReader inFromServer;
	private String username;
	
	
	public ShowFormInitializer (String name, Socket socket, DataOutputStream out, BufferedReader in) throws IOException {
		//Crea la finestra
		username = name;
		form = new ShowForm(username);
		form.setVisible(true);
		clientSocket = socket;
		outToServer = out;
		inFromServer = in;
		//Invoca il metodo che inizializza i bottoni
		initShowFormListeners();	
	}


	private void initShowFormListeners() {
		
		//Inizializzo le operazioni relative bottone ShowDocument
		form.getShowDocumentButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				//Faccio partire il thread per la richiesta di visualizzazione di un documento
				Thread showDocRequest = new Thread(new ShowDocRequestSender(form,clientSocket,outToServer,inFromServer));
				showDocRequest.start();
			}
		});
		
		//Inizializzo le operazioni relative bottone ShowSection
		form.getShowSectionButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				//Faccio partire il thread per la richiesta di visualizzazione di una sezione
				Thread showSecRequest = new Thread(new ShowSecRequestSender(form,clientSocket,outToServer,inFromServer));
				showSecRequest.start();
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
