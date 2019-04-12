package latoServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import Config.Config;


/*Classe principale del lato Server.
* Crea le strutture dati per contenere gli utenti registrati, gli utenti online 
* e i documenti.
* Offre un servizio remoto per permettere ai clients di effettuare la registrazione.
* Fa partire un thread Listener che si occupa di accettare le connessioni dei clients,
* che vengono poi passate al threadpool per essere gestite.

* @author Lisa Lavorati mat:535658 */

public class TuringServer {
	
	private static ConcurrentHashMap<String,User> utentiRegistrati; //Struttura dati contenente tutti gli utenti registrati al servizio di Turing
	private static ConcurrentHashMap<String,User> utentiOnline;  //Struttura dati contenente gli utenti online al momento
	private static ConcurrentHashMap<String,Document> documenti; //Struttura dati contenente tutti i documenti del database di Turing
	private static ThreadPoolExecutor pool; 
	private static ServerSocket welcomeSocket; //Socket principale sulla quale si accettano le connessioni dei clients
	private static ServerSocket secondSocket; //Socket di supporto per le notifiche online
	private static Registry registry;


	
	
	public static void main(String[] args) throws InterruptedException, IOException, AlreadyBoundException {
		
		utentiRegistrati = new ConcurrentHashMap<String,User>();
		utentiOnline = new ConcurrentHashMap<String,User>();
		documenti = new ConcurrentHashMap<String,Document>();
		pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
		welcomeSocket = new ServerSocket(Config.TCP_PORT);
		secondSocket = new ServerSocket(Config.TCP_SECOND_PORT);
		
		try {
			//Creo l'istanza dell'oggetto remoto
            RMIServerImpl serverRMI = new RMIServerImpl(utentiRegistrati);
            //Esporto l'oggetto remoto
            RMIServerInterface stub = (RMIServerInterface) UnicastRemoteObject.exportObject(serverRMI, 0);
            
            //Registro l'oggetto remoto stub nel registry
            LocateRegistry.createRegistry(Config.RMI_PORT);
	        registry = LocateRegistry.getRegistry(Config.RMI_PORT);
	        registry.bind("REGISTRATION_SERVICE", stub);
		}
		catch(RemoteException e) {
			e.printStackTrace();
		}
		
		Thread listenerThread = new Thread (new Listener(utentiRegistrati,utentiOnline,documenti,welcomeSocket,secondSocket,pool)); 
		listenerThread.start();
		
		
		
        
	}


}
