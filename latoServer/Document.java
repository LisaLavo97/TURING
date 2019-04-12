package latoServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/* Classe Document
 * Memorizza tutte le informazioni relative ad un documento, e, tra gli altri, contiene un metodo
 * che permette di inviare un documento ad un Client tramite un SocketChannel
 * 
 * @author Lisa Lavorati mat:535658 
 */

public class Document {
	private String docName; //Nome del documento
	private User creator; //Utente che ha creato il documento
	private int secNumber; //Numero delle sezioni del documento non modificabile dopo la creazione del documento
	private String group; //Indirizzo multicast associato al documento sotto forma di stringa
	private ConcurrentHashMap<String,User> utentiAutorizzati; //Struttura dati contenente gli utenti autorizzati a leggere e modificare il documento
	private ArrayList<Section> documento; //ArrayList delle sezioni del documento
	private Path dir; //Path dei file del documento
	
	
	public Document(User utente, String name, int numSezioni, String address) throws IOException {
		
		docName = name;
		creator = utente;
		secNumber = numSezioni;
		group = address;
	
		utentiAutorizzati = new ConcurrentHashMap<String,User>();
		utentiAutorizzati.put(utente.getUsername(), utente);
		
		
		dir = Paths.get(docName);
		if (!Files.exists(dir)) {
			try {
				Files.createDirectory(dir); 
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		documento = new ArrayList<Section>();
		int i;
		for(i=0; i<secNumber; i++)
			documento.add(new Section(docName,dir.toString(),i));
		
	}
	
	
	//Metodo per inviare al Client un documento tramite un SocketChannel
	public void sendDocument(int port) throws IOException {
		InetSocketAddress address = new InetSocketAddress(port);
	    SocketChannel socketChannel = SocketChannel.open(address);
	    FileChannel channel = null;
	    
	    for(int i=0; i<secNumber; i++) {
	    	
	    		Section sec = documento.get(i);
	    		Path path = Paths.get(sec.getPath());
		    channel = FileChannel.open(path, StandardOpenOption.READ);
		    long position = 0L;
		    long size = channel.size();
		       
		    while (position < size)
		    		position += channel.transferTo(position, 1024L*1024L, socketChannel);
		    
	    }
	    channel.close();
	    socketChannel.close();
        channel = null;
        socketChannel = null;
	}
	
	//Metodo che restituisce il nome del documento
	public String getDocumentName() {
		return this.docName;
	}
	
	
	//Metodo che restituisce l'utente creatore del documento
	public User getCreator() {
		return creator;
	}
	
	
	//Metodo che restituisce il numero di sezioni del documento
	public Integer getSecNum() {
		return secNumber;
	}
	
	
	//Metodo che restituisce la sezione i-esima del documento
	public Section getSection(int i) {
		return documento.get(i);
	}
	
	
	//Metodo che restituisce la struttura dati contenente gli utenti autorizzati a lavorare sul documento
	public ConcurrentHashMap<String,User> getUtentiAutorizzati(){
		return utentiAutorizzati;
	}
	
	
	//Metodo che restituisce l'indirizzo multicast associato al documento sotto forma di stringa
	public String getInetAddress() {
		return group;
	}
		
}
	
	
