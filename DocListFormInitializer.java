package latoClient.FormInitializer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import latoClient.RequestSender.DocListRequestSender;
import view.DocListForm;
import view.LoggedForm;


/*Classe che si occupa di creare l'interfaccia grafica dove si visualizza la lista dei documenti
 * che l'utente è autorizzato ad accedere e modificare con rispettivi creatori e collaboratori
 *
 * @author Lisa Lavorati mat:535658 
 */

public class DocListFormInitializer {
	
	private static DocListForm form;
	private static LoggedForm logForm;
	private static Socket clientSocket;
	private static DataOutputStream outToServer;
	private static BufferedReader inFromServer;
	
	
	public  DocListFormInitializer (DocListForm f, LoggedForm lForm, Socket socket, DataOutputStream out, BufferedReader in) throws IOException {
		form = f;
		logForm = lForm;
		clientSocket = socket;
		outToServer = out;
		inFromServer = in;
		//Invoca il metodo che inizializza i bottoni
		initFormListeners();
		//Fa partire il thread per la richiesta di visualizzazione della lista dei documenti dell'utente
		Thread docListRequest = new DocListRequestSender(form,logForm,clientSocket,outToServer,inFromServer);
		docListRequest.start();
	}
	
	
	private static void initFormListeners() {
		
		//Inizializzo le operazioni relative bottone MenuButton
		form.getMenuButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				try {
					form.setVisible(false);
					form.closeWindow();
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
