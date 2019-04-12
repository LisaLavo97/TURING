package latoClient.RequestSender;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
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
import latoClient.ChatInitializer;
import view.ModifyDocForm;

/* Thread che invia la richiesta di modifica di una sezione al Server.
 * Contiene un metodo che permette di ricevere la sezione dal server tramite un SocketChannel
 * 
 * @author Lisa Lavorati mat:535658 
 */


public class ModifyRequestSender extends Thread {

	private ModifyDocForm form;
	private Socket clientSocket; //Socket lato client per le conn TCP
	private DataOutputStream outToServer; //Stream per mandare dati al server
	private BufferedReader inFromServer; //Stream per ricevere dati dal server
	
	public ModifyRequestSender(ModifyDocForm f, Socket socket, DataOutputStream out, BufferedReader in) {
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
		
		String op = "MODIFY" + '\n';
		try {
			outToServer.writeBytes(op);
	        
			String docName = form.getDocName();
			String secNum = form.getSecNumber();
			outToServer.writeBytes(docName + '\n');
			outToServer.writeBytes(secNum + '\n');
			
			String temp = inFromServer.readLine();
			
			if(temp.contains("Permission denied")) {
				JOptionPane.showMessageDialog(null, "Permission denied, you can't edit this document");
			}
			
			else if(temp.contains("In editing"))
				JOptionPane.showMessageDialog(null, "Permission denied, another user is editing this section");
			
			else if(temp.contains("Invalid secNum") || temp.contains("NaN") )
				JOptionPane.showMessageDialog(null, "Permission denied, invalid section number");
			
			else if(temp.contains("Non-existent document"))
				JOptionPane.showMessageDialog(null, "This document doesn't exist");
				
			else {
				String port = inFromServer.readLine();
				//Creo il ServerSocketChannel
				ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
				serverSocketChannel.bind(new InetSocketAddress(Integer.parseInt(port)));
				//Accetto la connessione dal server 
		        SocketChannel socketChannel = serverSocketChannel.accept();
				
				String path = inFromServer.readLine();
				String username = inFromServer.readLine();
				String address = inFromServer.readLine();
				
				//Invoco il metodo per ricevere la sezione dal server
				receiveSection(socketChannel,path,username);
				JOptionPane.showMessageDialog(null, "Section has been successfully received and can now be edit");
				
				serverSocketChannel.close();
				serverSocketChannel = null;
				
				form.setVisible(false);
				form.closeWindow();
				//Inizializzo un oggetto di tipo ChatInitializer che inizializza il servizio di chat
				ChatInitializer chat = new ChatInitializer(username,InetAddress.getByName(address),clientSocket,outToServer,inFromServer,docName,Integer.parseInt(secNum));
			}
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	//Metodo per ricevere la sezione dal Server tramite un SocketChannel
	private void receiveSection(SocketChannel socketChannel, String path, String username) throws IOException {

		   FileChannel fileChannel = null;
		   
		   //Creo la directory nella quale salvare la sezione inviata dal Server
		   File dir = new File("Editing/" + username + "/" + path);
		   
		   if(!dir.exists())
			   Files.createDirectories(Paths.get("Editing/" + username + "/" + path));
		   
		   //Creo il file nel quale scrivere il file relativo alla sezione inviata dal Server
           File file = new File("Editing/" + username + "/" + path);

           if(file.exists()) {
        	   	    file.delete();
           }
           file.createNewFile();
           
           //Creo il FileChannel
           fileChannel = FileChannel.open(Paths.get("Editing/" + username + "/" + path), StandardOpenOption.WRITE);
           
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


