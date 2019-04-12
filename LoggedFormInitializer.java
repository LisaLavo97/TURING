package latoClient.FormInitializer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import latoClient.RequestSender.LogoutRequestSender;
import view.DocListForm;
import view.LoggedForm;


/*Classe che si occupa di creare l'interfaccia grafica che si presenta all'utente
 * dopo aver effettuato il login, e che inizializza le operazioni relative ai suoi bottoni.
 * 
 * @author Lisa Lavorati mat:535658 
 */



public class LoggedFormInitializer {
	
	private static LoggedForm logForm;
	private static Socket clientSocket;
	private static DataOutputStream outToServer;
	private static BufferedReader inFromServer;
	private static String username;
	private LinkedList<String> invitationList;
	
	//Costruttore usato quando non ci sono inviti pendenti sull'utente che ha appena effettuato il login
	public LoggedFormInitializer(String name, Socket socket, DataOutputStream out, BufferedReader in) throws IOException {
		username = name;
		//Crea la finestra
		logForm = new LoggedForm(username);
		logForm.setVisible(true);
		clientSocket = socket;
		outToServer = out;
		inFromServer = in;
		//Invoca il metodo che inizializza i bottoni
		initLoggedListeners();
	}
	
	//Costruttore usato quando l'utente che ha appena effettuato il login ha inviti pendenti
	public LoggedFormInitializer(String name, Socket socket, DataOutputStream out, BufferedReader in, LinkedList<String> invitationL) throws IOException {
		username = name;
		//Crea la finestra
		logForm = new LoggedForm(username);
		logForm.setVisible(true);
		clientSocket = socket;
		outToServer = out;
		inFromServer = in;
		invitationList = invitationL;
		
		int i;
		//Notifica all'utente gli inviti che ha avuto mentre era offline
		for(i=0; i<invitationList.size(); i++)
			JOptionPane.showMessageDialog(null, "You have been invited to collaborate on the document '" + invitationList.get(i) + "' while you were offline .");
		
		//Invoca il metodo che inizializza i bottoni
		initLoggedListeners();
	}
	
	
	private static void initLoggedListeners() {
		
		//Inizializza le operazioni relative bottone NewDocButton
		logForm.getNewDocButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				try {
					logForm.setVisible(false);
					logForm.closeWindow();
					/* Crea un oggetto di tipo NewDocFormInitializer che si occupa di creare ed inizializzare la finestra che permette
					all'utente di richiedere la creazione di un nuovo documento */
					NewDocFormInitializer initializer = new NewDocFormInitializer(logForm,clientSocket,outToServer,inFromServer);
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		
		//Inizializza le operazione relative al bottone DocListButton
		logForm.getDocListButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				try {
					logForm.setVisible(false);
					logForm.closeWindow();
					/* Crea un oggetto di tipo DocListFormInitializer che si occupa di creare ed inizializzare la finestra che permette
					all'utente di richiedere la lista dei documenti che è autorizzato ad accedere e modificare */
					DocListFormInitializer initializer = new DocListFormInitializer(new DocListForm(username),logForm,clientSocket,outToServer,inFromServer);
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		
		//Inizializza le operazioni relative al bottone ModifyDocButton
		logForm.getModifyDocButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				
				try {
					logForm.setVisible(false);
					logForm.closeWindow();
					/* Crea un oggetto di tipo ModifyDocFormInitializer che si occupa di creare ed inizializzare la finestra che permette
					all'utente di richiedere la modifica di una sezione */
					ModifyDocFormInitializer initializer = new ModifyDocFormInitializer(username,clientSocket,outToServer,inFromServer);
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		//Inizializza le operazioni relative al bottone InviteButton
		logForm.getInviteButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				
				try {
					logForm.setVisible(false);
					logForm.closeWindow();
					/* Crea un oggetto di tipo InviteFormInitializer che si occupa di creare ed inizializzare la finestra che permette
					all'utente di invitarne un altro a collaborare ad un documento */
					InviteFormInitializer initializer = new InviteFormInitializer(username,clientSocket,outToServer,inFromServer);
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		//Inizializza le operazioni relative al bottone ShowButton
		logForm.getShowButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				try {
					logForm.setVisible(false);
					logForm.closeWindow();
					/* Crea un oggetto di tipo ShowFormInitializer che si occupa di creare ed inizializzare la finestra che permette
					all'utente di richiedere di visualizzare un documento o una sezione */
					ShowFormInitializer initializer = new ShowFormInitializer(username,clientSocket,outToServer,inFromServer);
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		//Inizializza le operazioni relative al bottone LogoutButton
		logForm.getLogoutButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				//Fa partire il thread per la richiesta di logout
				Thread logoutRequest = new LogoutRequestSender(logForm,clientSocket,outToServer,inFromServer);
				logoutRequest.start();
			}
		});
		
	}
}
