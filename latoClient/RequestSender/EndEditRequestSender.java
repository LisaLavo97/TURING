package latoClient.RequestSender;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.swing.JOptionPane;
import latoClient.FormInitializer.LoggedFormInitializer;
import view.ChatForm;

/* Thread che invia al Server la richiesta di EndEdit.
 * Notifica la fine dell'operazione di modifica sulla sezione ed invia al Server la sezione modificata
 * tramite un SocketChannel
 * 
 * @author Lisa Lavorati mat:535658 
 */

public class EndEditRequestSender extends Thread {
	private ChatForm chatForm;
	private Socket clientSocket; //Socket lato client per le conn TCP
	private DataOutputStream outToServer; //Stream per mandare dati al server
	private BufferedReader inFromServer; //Stream per ricevere dati dal server
	private String nomeDoc;
	private Integer numeroSez;
	
	public EndEditRequestSender(ChatForm f, Socket socket, DataOutputStream out, BufferedReader in, String docName, int numSec) {
		if(socket == null || out == null || in == null)
			throw new NullPointerException();
		
		if(socket.isClosed())
			throw new IllegalArgumentException();
		
		chatForm = f;
		clientSocket = socket;
		outToServer = out;
		inFromServer = in;
		nomeDoc = docName;
		numeroSez = numSec;
	}
	
	public void run() {
		
		String op = "ENDEDIT" + '\n';
		
		try {
				outToServer.writeBytes(op);
				
				outToServer.writeBytes(nomeDoc + '\n');
				outToServer.writeBytes(numeroSez.toString() + '\n');
				
				String port = inFromServer.readLine();
				String path = inFromServer.readLine();
				String username = inFromServer.readLine();
				
				//Creo la ServerSocketChannel
				ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
				serverSocketChannel.bind(new InetSocketAddress(Integer.parseInt(port)));
				//Accetto la connessione dal server 
		        SocketChannel socketChannel = serverSocketChannel.accept();
		        
		        //Invoco il metodo per inviare la sezione al Server
				sendSection(socketChannel,username, path);
				
				if(inFromServer.readLine().equals("success"))
					JOptionPane.showMessageDialog(null, "Section has been loaded successfully.");
				serverSocketChannel.close();
				serverSocketChannel = null;
				
				//Chiudo la finestra di chat
				chatForm.setVisible(false);
				chatForm.closeWindow();
				LoggedFormInitializer initializer = new LoggedFormInitializer(username,clientSocket,outToServer,inFromServer);
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}


	}
	
	
	//Metodo per inviare la sezine modificata al Server
	public void sendSection(SocketChannel socketChannel, String username, String path) throws IOException {
		
		FileChannel channel = null;
	       
	    Path p = Paths.get("Editing/" + username + "/" + path);
	    
	    channel = FileChannel.open(p, StandardOpenOption.READ);
	    long position = 0L;
	    long size = channel.size();
	       
	    while (position < size)
	    		position += channel.transferTo(position, 1024L*1024L, socketChannel);
	    
	    channel.close();
	    socketChannel.close();
        channel = null;
        socketChannel = null;
        
	}
	
}
