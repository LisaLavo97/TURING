package latoServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* Classe Section
 * Memorizza tutte le informazioni relative ad una sezione, e, tra gli altri, contiene un metodo
 * che permette di inviare ed uno di ricevere una sezione da un Client tramite un SocketChannel
 * 
 * @author Lisa Lavorati mat:535658 
 */

public class Section {
	
	private String nomeDocumento; //Nome del documento a cui appartiene la sezione
	private Integer numSezione; //Numero della sezione 
	private Lock lock; //Lock associata alla sezione
	private ReadWriteLock readWriteLock; //ReadWriteLock per le operazioni di lettura e scrittura sulla sezione
	private Lock read;
	private Lock write;
	private File fileSezione;
	private String dir; 
	private Path path; //Path del file della sezione
	private boolean inEditing; //Booleano che indica se la sezione è o meno in fase di editing da parte di un utente
	
	
	public Section(String nome, String directory, int num) throws IOException {
		nomeDocumento = nome;
		numSezione = num;
		dir = directory;
		
		lock = new ReentrantLock();
		readWriteLock = new ReentrantReadWriteLock();
		read = readWriteLock.readLock();
		write = readWriteLock.writeLock();
		
		path = Paths.get(dir, this.numSezione.toString()+ ".txt");
		fileSezione = new File(path.toString());
		fileSezione.createNewFile();
		
		inEditing = false;
		
	}
	
	
	
	//Metodo per inviare una sezione tramite un SocketChannel
	public void sendSection(int port) throws IOException {
		
		//Prendo la lock usata in lettura
		read.lock();
		InetSocketAddress address = new InetSocketAddress(port);
		//Creo un SocketChannel che si connette al Client
	    SocketChannel socketChannel = SocketChannel.open(address);
	       
	    //Creo un FileChannel
	    FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
	    long position = 0L;
	    long size = channel.size();
	       
	    while (position < size)
	    		position += channel.transferTo(position, 1024L*1024L, socketChannel);
	    
	    channel.close();
	    socketChannel.close();
        channel = null;
        socketChannel = null;
        //Rilascio la lock in lettura
        read.unlock();
        
	}

		
	//Metodo che permette di ricevere una sezione tramite un SocketChannel
	public void receiveSection(int port) throws IOException {
		
		//Prendo la lock usata in scrittura
		write.lock();
		InetSocketAddress address = new InetSocketAddress(port);
		//Creo un SocketChannel che si connette al Client
	    SocketChannel socketChannel = SocketChannel.open(address);
		
		FileChannel fileChannel = null;
		
		//Creo il file nel quale ricevere la sezione modificata dal Client
        File file = new File(path.toString());
        if(file.exists())
        		file.delete();
        file.createNewFile();

      //Creo un FileChannel
        fileChannel = FileChannel.open((path), StandardOpenOption.WRITE);
           
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
	    
	    //Rilascio la lock in scrittura
	    write.unlock();
	    
	}
	
	
	//Metodo per prendere la lock sulla sezione
	public void lockSection() {
		lock.lock();
	}
    
	
	//Metodo che rilascia la lock sulla sezione
	public void unlockSection() {
		lock.unlock();
	}
	
	
	//Metodo che restituisce il path del file della sezione sotto forma di stringa
	public String getPath() {
		return path.toString();
	}
	
	
	//Metodo che restituisce il booleano che indica se la sezione è o meno in fase di editing da parte di un utente
	public boolean isInEditing() {
		return inEditing;
	}
	
	
	//Metodo che setta il booleano inEditing
	public void setEditing(boolean bool) {
		inEditing = bool;
	}

}

