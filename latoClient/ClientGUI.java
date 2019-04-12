package latoClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import latoClient.FormInitializer.TuringFormInitializer;
import Config.Config;

/*Classe principale del client, contiene il main nel quale si crea la socket per
* la connessione TCP verso il server e crea un oggetto di tipo TuringFormInitializer
* che inizializza la prima interfaccia grafica di Turing
* @author Lisa Lavorati mat:535658 */

public class ClientGUI {
	
	private static Socket clientSocket; //Socket lato client per le connessioni TCP
	private static DataOutputStream outToServer; //Stream per mandare dati al server
	private static BufferedReader inFromServer; //Stream per ricevere dati dal server 
	
	
	public static void main(String[] args) throws UnknownHostException, IOException {
       
       clientSocket = new Socket("localHost", Config.TCP_PORT);
       outToServer = new DataOutputStream(clientSocket.getOutputStream());
       inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
       TuringFormInitializer initializer = new TuringFormInitializer(clientSocket,outToServer,inFromServer);
       
	}
	
}
