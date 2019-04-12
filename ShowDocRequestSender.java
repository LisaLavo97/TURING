package latoClient.RequestSender;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.swing.JOptionPane;
import view.ShowForm;


/* Thread che invia al Server la richiesta di visualizzazione di un documento.
 * Contiene un metodo per ricevere il documento dal Server tramite un SocketChannel.
 * 
 * @author Lisa Lavorati mat:535658 
 */


public class ShowDocRequestSender extends Thread {
	
	private ShowForm form;
	private Socket clientSocket; //Socket lato client per le conn TCP
	private DataOutputStream outToServer; //Stream per mandare dati al server
	private BufferedReader inFromServer; //Stream per ricevere dati dal server
	
	
	public ShowDocRequestSender(ShowForm f, Socket socket, DataOutputStream out, BufferedReader in) {
		if(socket == null || out == null || in == null)
			throw new NullPointerException();
		
		if(socket.isClosed())
			throw new IllegalArgumentException();
		
		form = f;
		clientSocket = socket;
		outToServer = out;
		inFromServer = in;
		
	}
	
	
	public void run() {
		
		String op = "SHOWDOC" + '\n';
		
		try {
			outToServer.writeBytes(op);
	          
			String docName = form.getDocName();
			outToServer.writeBytes(docName + '\n');
			
			String tmp = inFromServer.readLine();
			
			if(tmp.contains("Permission denied"))
				JOptionPane.showMessageDialog(null, "Permission denied, you do not have the rights to access this document");
			
			else if(tmp.contains("Non-existent document"))
				JOptionPane.showMessageDialog(null, "This document doesn't exist");
			
			else {	
				//Se non ci sono errori creo un ServerSocketChannel
				String port = inFromServer.readLine();
				ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
				serverSocketChannel.bind(new InetSocketAddress(Integer.parseInt(port)));
				//Accetto la connessione dal Server
		        SocketChannel socketChannel = serverSocketChannel.accept();
		        
		        String username = inFromServer.readLine();
		        //Invoco il metodo per ricevere il documento dal Server
				receiveDocument(socketChannel,docName,username);
				
				JOptionPane.showMessageDialog(null, "Document has been successfully received");
				serverSocketChannel.close();
				serverSocketChannel = null;
			}
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
	//Metodo per ricevere un documento dal Server tramite un SocketChannel
	private void receiveDocument(SocketChannel socketChannel, String docName, String username) throws IOException {
		   
		   FileChannel fileChannel = null;
		   
		   //Creo la directory nella quale salvare il docuemento ricevuto
		   File dir = new File("Show/" + username + "/" + docName + ".txt");
		   
		   if(!dir.exists())
			   Files.createDirectories(Paths.get("Show/" + username + "/" + docName + ".txt"));
		   
		   //Creo il file su cui scrivere il documento
           File file = new File("Show/" + username + "/" + docName + ".txt");
           
           if(file.exists())
        	   	file.delete();
           file.createNewFile();

           //Creo il FileChannel
           fileChannel = FileChannel.open(Paths.get("Show/" + username + "/" + docName + ".txt"), StandardOpenOption.WRITE);
           
           ByteBuffer buffer = ByteBuffer.allocate(1024);
           while(socketChannel.read(buffer) > 0) {
               buffer.flip();
               fileChannel.write(buffer);
               buffer.clear();
           }
	   
	       fileChannel.close();
	       socketChannel.close();
           fileChannel = null;
           socketChannel = null;
		
	}
}
