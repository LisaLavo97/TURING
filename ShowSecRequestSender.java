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

/* Thread che invia al Server la richiesta di visualizzazione una sezione di un documento.
 * Contiene un metodo per ricevere la sezione dal Server tramite un SocketChannel.
 * 
 * @author Lisa Lavorati mat:535658 
 */


public class ShowSecRequestSender extends Thread {

	private ShowForm form;
	private Socket clientSocket; //Socket lato client per le conn TCP
	private DataOutputStream outToServer; //Stream per mandare dati al server
	private BufferedReader inFromServer; //Stream per ricevere dati dal server
	
	
	public ShowSecRequestSender(ShowForm f, Socket socket, DataOutputStream out, BufferedReader in) {
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
		
		String op = "SHOWSEC" + '\n';
		
		try {
			outToServer.writeBytes(op);
	           
			String docName = form.getDocName();
			String secNum = form.getSecNumber();
			outToServer.writeBytes(docName + '\n');
			outToServer.writeBytes(secNum + '\n');
			
			String temp =inFromServer.readLine();
			
			if(temp.contains("Invalid secNum"))
				JOptionPane.showMessageDialog(null, "Permission denied, invalid section number.");
			
			else if(temp.contains("Permission denied"))
				JOptionPane.showMessageDialog(null, "Permission denied, you do not have the rights to access this document.");
			
			else if(temp.contains("Non-existent document"))
				JOptionPane.showMessageDialog(null, "This document doesn't exist.");
			
			else {
				
				String port = inFromServer.readLine();
				String username = inFromServer.readLine();
				String path = inFromServer.readLine();
				
				String inEditing = inFromServer.readLine();
				
				//Creo la ServerSocketChannel
				ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
				serverSocketChannel.bind(new InetSocketAddress(Integer.parseInt(port)));
				//Accetto la connessione dal Server
		        SocketChannel socketChannel = serverSocketChannel.accept();
	
		        //Invoco il metodo per ricevere la sezione dal server
				receiveSection(socketChannel,path,username);
				
				if(inEditing.contains("true"))
					JOptionPane.showMessageDialog(null, "Section has been successfully received.This section is being edited right now.");
				else
					JOptionPane.showMessageDialog(null, "Section has been successfully received.");
				serverSocketChannel.close();
				serverSocketChannel = null;
			}
			
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
	//Metodo per ricevere una sezione dal Server tramite un SocketChannel
	private void receiveSection(SocketChannel socketChannel, String path, String username) throws IOException {

		FileChannel fileChannel = null;
		
		//Creo la directory nella quale salvare la sezione ricevuta
		File dir = new File("Show/" + username + "/" + path);
		
		if(!dir.exists())
			Files.createDirectories(Paths.get("Show/" + username + "/" + path));
		
		//Creo il file su cui scrivere il file relativo alla sezione ricevuta
        File file = new File("Show/" + username + "/" + path);
        
        if(file.exists())
     	   	file.delete();
        file.createNewFile();

        //Creo il FileChannel
        fileChannel = FileChannel.open(Paths.get("Show/" + username + "/" + path), StandardOpenOption.WRITE);
        
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
