package latoServer;

import java.util.HashMap;
import java.util.LinkedList;

/* Classe User
 * Memorizza tutte le informazioni relative ad un utente
 * 
 * @author Lisa Lavorati mat:535658 
 */


public class User {
	
	private String username; //Username dell'utente, non modificabile ed univoco
	private String psw; //Password dell'utente
	private HashMap<String,Document> listaDocumenti; //Struttura dati contenente i documenti che l'utente è autorizzato a leggere e modificare
	private Stato statoUtente; //Stato dell'utente
	private boolean offlineInvitations; //Booleano che indica se ci siano o meno richieste di invito offline pendenti
	private boolean onlineInvitations; //Booleano che indica se ci siano o meno richieste di invito online pendenti
	private LinkedList<String> offlineInvitationList; //Lista degli inviti offline pendenti
	private LinkedList<Document> onlineInvitationList; //Lista degli inviti online pendenti
	private int sendPort; //Porta associata all'utente sulla quale si crea il SocketAddress da associare al SocketChannel per l'invio di una sezione
	private int receivePort; //Porta associata all'utente sulla quale si crea il SocketAddress da associare al SocketChannel per la ricezione di una sezione
	
	
	public User(String username, String password) {
		this.username = username;
		this.psw = password;
		this.listaDocumenti = new HashMap<String,Document>();
		statoUtente = Stato.registered;
		offlineInvitations = false;
		onlineInvitations = false;
		offlineInvitationList = new LinkedList<String>();
		onlineInvitationList = new LinkedList<Document>();
		sendPort = hashCode() % 10000;
		receivePort = hashCode() % 10000;
	}

	
	//Metodo che restituisce l'username dell'utente
	public String getUsername() {
		return this.username;
	}
	
	
	//Metodo che restituisce la password dell'utente
	public String getPassword() {
		return this.psw;
	}
	
	
	public Integer getSendPort() {
		return sendPort;
	}
	
	
	public Integer getReceivePort() {
		return receivePort;
	}
	
	
	public HashMap<String,Document> getListaDocumenti(){
		return this.listaDocumenti;
	}
	
	
	//Metodo che restituisce lo stato dell'utente
	public Stato getStatoUtente() {
		return this.statoUtente;
	}
	
	
	//Metodo per settare lo stato dell'utente
	public void setStatoUtente(Stato stato) {
		this.statoUtente = stato;
	}
	
	
	//Metodo che restituisce un documento della lista dell'utente
	public Document getDocument(String documentName) {
		return listaDocumenti.get(documentName);
	}
	
	
	//Metodo che indica se un documento è presente o meno nella lista dell'utente
	public boolean containsDocument(String documentName) {
		return listaDocumenti.containsKey(documentName);
	}
	
	
	//Metodo per verificare se ci siano o meno inviti offline pendenti per l'utente
	public boolean getOfflineInvitations() {
		return offlineInvitations;
	}
	
	
	//Metodo per settare il booleano relativo alla presenza di inviti offline per un utente
	public void setOfflineInvitations(boolean bool) {
		offlineInvitations = bool;
	}
	
	
	//Metodo per verificare se ci siano o meno inviti online pendenti per l'utente
	public boolean getOnlineInvitations() {
		return onlineInvitations;
	}
	
	
	//Metodo per settare il booleano relativo alla presenza di inviti online per un utente
	public void setOnlineInvitations(boolean bool) {
		onlineInvitations = bool;
	}
	
	
	//Metodo per aggiungere un invito alla lista degli inviti online pendenti dell'utente
	public void addOnlineInvitation(Document doc){
		onlineInvitationList.add(doc);
	}
	
	
	//Metodo per aggiungere un invito alla lista degli inviti offline pendenti dell'utente
	public void addOfflineInvitation(String doc){
		offlineInvitationList.add(doc);
	}
	
	
	//Metodo che restituisce la lista degli inviti online pendenti dell'utente
	public LinkedList<Document> getOnlineInvitationList(){
		return onlineInvitationList;
	}
	
	
	//Metodo che restituisce la lista degli inviti offline pendenti dell'utente
	public LinkedList<String> getOfflineInvitationList(){
		return offlineInvitationList;
	}
	
	
	//Metodo che elimina gli inviti offline presenti nella lista
	public void clearOfflineInvitations() {
		offlineInvitationList.clear();
	}
	
	
	//Metodo che elimina gli inviti online presenti nella lista
	public void clearOnlineInvitations() {
		onlineInvitationList.clear();
	}
	
	
}

