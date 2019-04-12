package latoServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/* Thread Listener che si mette in attesa di connessioni da parte dei clients.
 * Quando riceve una nuova connessione crea un'istanza del task da eseguire e la passa al threadpool.

@author Lisa Lavorati mat:535658 */

public class Listener extends Thread {

	private ConcurrentHashMap<String,User> utentiRegistrati; //Struttura dati contenente tutti gli utenti registrati al servizio di Turing
	private ConcurrentHashMap<String,User> utentiOnline; //Struttura dati contenente gli utenti online al momento
	private static ConcurrentHashMap<String,Document> documenti; //Struttura dati contenente tutti i documenti del database di Turing
	private ServerSocket welcomeSocket; //Socket principale sulla quale si accettano le connessioni dei clients
	private ServerSocket secondSocket; //Socket di supporto per le notifiche online
	private ThreadPoolExecutor tpool;
	
	
	public Listener(ConcurrentHashMap<String,User> registeredUsers, ConcurrentHashMap<String,User> onlineUsers, ConcurrentHashMap<String,Document> documents,ServerSocket socket, ServerSocket second_socket, ThreadPoolExecutor pool) {
		
		utentiRegistrati = registeredUsers;
		utentiOnline = onlineUsers;
		documenti = documents;
		welcomeSocket = socket;
		secondSocket = second_socket;
		tpool = pool;
		
	}
	
	
	public void run() {
		
		while (true) {
			
			Socket connectionSocket = null;
		
			try {
				connectionSocket = welcomeSocket.accept();
				//Task eseguito dal threadpool
				UserRequestHandler task = new UserRequestHandler(connectionSocket,secondSocket,utentiRegistrati,utentiOnline,documenti);
				tpool.execute(task);
			}
			
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
