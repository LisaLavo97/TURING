package latoServer;

import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;
import java.util.concurrent.ConcurrentHashMap;

/* Classe RMIServerImpl
 * Implementa i metodi dell'interfaccia RMIServerInterface.
 * Contiene i metodi per registrare un utente e per verificare se un utente sia o meno registrato.
 * 
 * @author Lisa Lavorati mat:535658 
 */



public class RMIServerImpl extends RemoteObject implements RMIServerInterface {
	
	private static final long serialVersionUID = 1L;
	private ConcurrentHashMap<String,User> utentiRegistrati;
	
	
	public RMIServerImpl(ConcurrentHashMap<String,User> utenti) throws RemoteException {
		
		super();
		utentiRegistrati = utenti;
		
	}
	
	
	//Metodo per la registrazione di un utente
	public void registrationRequest(String username, String password) throws RemoteException {
		
		if(utentiRegistrati.containsKey(username))
			System.out.println("Already registered user");
		
		else {
			User newUser = new User(username, password);
			utentiRegistrati.put(username, newUser);
		}
		
	}

	
	//Metodo per verificare se un utente sia o meno registrato
	public boolean isRegistered(String username) throws RemoteException {
		
		return utentiRegistrati.containsKey(username);
		
	}
	
	
}
