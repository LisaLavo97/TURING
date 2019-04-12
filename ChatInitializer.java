package latoClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import view.ChatForm;
import Config.Config;
import latoClient.RequestSender.EndEditRequestSender;

/*Classe che si occupa di creare la finestra di chat e di inizializzare i suoi bottoni, 
 * e che quindi implementa il vero e proprio servizio di chat

* @author Lisa Lavorati mat:535658 */

public class ChatInitializer extends Thread {
	
	private ChatForm form;
	private InetAddress group;
	private boolean flag;
	private static String username;
	private Socket clientSocket;
	private DataOutputStream outToServer;
	private BufferedReader inFromServer;
	private String nomeDoc;
	private int numeroSez;
	
	public ChatInitializer(String name, InetAddress ia, Socket socket, DataOutputStream out, BufferedReader in, String docName, int secNum) throws IOException {
		username = name;
		//Crea la finestra di chat
		form = new ChatForm(username);
		form.setVisible(true);
		group = ia;
		flag = true;
		clientSocket = socket;
		outToServer = out;
		inFromServer = in;
		nomeDoc = docName;
		numeroSez = secNum;
		
		//Invoca il metodo che inizializza le azioni relative ai bottoni della finestra
		initChat();
	}
	
	   //Metodo per la ricezione dei messaggi nella chat via UDP
	   public void receiveUDPMessage() throws IOException {
		   
		   byte[] buffer = new byte[1024];
		   MulticastSocket socket = new MulticastSocket(Config.MULTICAST_PORT);
		   socket.joinGroup(group);
		   
		   while(flag){
			   DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
			   socket.receive(packet);
			   String msg = new String(packet.getData(),packet.getOffset(),packet.getLength());
			   form.getTextArea().append(msg + '\n');
		   }
		   socket.leaveGroup(group);
		   socket.close();
	   }
	   
	   
	 //Metodo per l'invio dei messaggi nella chat via UDP
	   private static void sendUDPMessage(String message, InetAddress group, int port) throws IOException {
	      DatagramSocket socket = new DatagramSocket();
	      byte[] msg = message.getBytes();
	      DatagramPacket packet = new DatagramPacket(msg, msg.length,group, port);
	      socket.send(packet);
	      socket.close();
	   }

	   
	   private void initChat() throws IOException {
		
		   form.getSendButton().addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) { 
					try {
						sendUDPMessage(form.getMessage(),group, Config.MULTICAST_PORT);
						form.resetText();
					}
					catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
		
		
		form.getEndEditButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				Thread endEdit = new EndEditRequestSender(form,clientSocket,outToServer,inFromServer,nomeDoc,numeroSez);
				endEdit.start();
				setFlag(false);
				
			}
		});
		
		
		form.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	  setFlag(false);
            }
        });

		
		receiveUDPMessage();
	}
	
	   public void setFlag(boolean b) {
		   flag = b;
	   }
}
