package latoServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;


/* Oggetto runnable che rappresenta il task eseguito dal threadpool.
 * Si occupa di gestire le richieste dei clients
 * 
 * @author Lisa Lavorati mat:535658 
 */

public class UserRequestHandler implements Runnable {
	
	private Socket connectionSocket; //Socket principale sulla quale si accettano le connessioni dei clients
	private Socket notificationSocket; //Socket di supporto per le notifiche online
	private ServerSocket secondSocket;
	private ConcurrentHashMap<String,User> utentiRegistrati; //Struttura dati contenente tutti gli utenti registrati al servizio di Turing
	private static ConcurrentHashMap<String,User> utentiOnline;  //Struttura dati contenente gli utenti online al momento
	private ConcurrentHashMap<String,Document> documenti; //Struttura dati contenente tutti i documenti del database di Turing

	
	public UserRequestHandler(Socket socket, ServerSocket second_socket, ConcurrentHashMap<String,User> registeredUsers, ConcurrentHashMap<String,User> onlineUsers, ConcurrentHashMap<String,Document> documents) {
		connectionSocket = socket;
		notificationSocket = null;
		secondSocket = second_socket;
		utentiRegistrati = registeredUsers;
		utentiOnline = onlineUsers;
		documenti = documents;
	}

	
	//Metodo statico per reperire un oggetto di tipo Utente tramite il suo username
	public static User getUtenteOnline (String username) {
		if(utentiOnline.containsKey(username))
			return utentiOnline.get(username);
		return null;
	}
	
	
	public void run() {
		
		BufferedReader inFromClient;
		DataOutputStream outToClient;
		User user = null;
		Section tempSection = null;
		
		try {
			
			while(true) {	
				
				//Stream in input ed output collegati alla socket principale per comunicare con il client
				inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				
				//Leggo la stringa inviatami dal client per richiedere una particolare operazione
				String op = inFromClient.readLine();
						
				if(op != null) {		
					
					//Richiesta di login
					if(op.equals("LOGIN")) {

						String username = inFromClient.readLine();
						String password = inFromClient.readLine();
						
						if(utentiRegistrati.containsKey(username)) {
							User tmp = utentiRegistrati.get(username);
							
							if(tmp.getStatoUtente() == Stato.editing)
								outToClient.writeBytes("User is editing" + '\n');
							
	
							else if(tmp.getStatoUtente() == Stato.registered) {
								
								if(tmp.getPassword().equals(password)) {
									
									user = tmp;
									//Cambio lo stato dell'utente che ha appena effettuato il login in logged_in 
									user.setStatoUtente(Stato.logged_in);
									//Inserisco l'utente nella struttura dati contenente gli utenti online
									utentiOnline.put(username, user);
									outToClient.writeBytes("Login successful" + '\n');
									
									//Controllo se l'utente abbia ricevuto inviti mentre era online
									if(user.getOfflineInvitations()  == false )
										outToClient.writeBytes("Stop" + '\n');
									
									//Se l'utente ha ricevuto inviti mentre era online, allora questi vengono inviati al client
									else {
										outToClient.writeBytes("Pending " + '\n');
										
										int i;
										String docName;
										
										for(i=0; i<user.getOfflineInvitationList().size(); i++) {
											docName = user.getOfflineInvitationList().get(i);
											outToClient.writeBytes(docName + '\n');
										}
										
										outToClient.writeBytes("END" + '\n');
										user.clearOfflineInvitations();
									}
									
									//Accetto la connessione del client sulla socket di supporto per la ricezione di inviti online
									notificationSocket = secondSocket.accept();
									//Faccio partire il thread per gestire le notifiche online
									Thread notifications = new Thread(new Notification(username,notificationSocket));
									notifications.start();
								}
								//Password sbagliata
								else
									outToClient.writeBytes("Wrong psw" + '\n');
							}
							//Utente già online
							else
								outToClient.writeBytes("User already online" + '\n');
						}
						//Utente non registrato
						else 
							outToClient.writeBytes("Error login: unknown user" + '\n');	
					}
					
					
					//Richiesta di creazione di un nuovo documento
					if(op.equals("NEWDOC")) {
						
						if(user != null) {
							
							String docName = inFromClient.readLine();
							String secNum = inFromClient.readLine();
							
							//Controllo che il documento non esista già nel database di Turing
							if(!documenti.containsKey(docName)) {
								
								int secNumber = 0;
								
								//Controllo che il numero di sequenza inserito dall'utente sia valido
								try {
									secNumber = Integer.parseInt(secNum);
									
									if(secNumber < 1)
										outToClient.writeBytes("Invalid secNumber" + '\n');
									
									else {
										
										//Creo un indirizzo multicast da associare al documento
										int tmp = (int)(Math.random()*40);
										
										while(tmp < 24 || tmp > 40) {
											tmp = (int)(Math.random()*40);
										}
										
										tmp += 200;
										String address = tmp + "." + (int)(Math.random()*256) + "." + (int)(Math.random()*256) + "." + (int)(Math.random()*256);
										//Creo il nuovo documento
										Document newDoc = new Document(user,docName,secNumber,address);
										//Inserisco il documento nel database di Turing
										documenti.put(docName, newDoc);
										//Inserisco il documento nella lista dei documenti che l'utente è autorizzato ad accedere e modificare
										user.getListaDocumenti().put(docName, newDoc);
										outToClient.writeBytes("Create successful" + '\n');
									}		
								}
								catch (NumberFormatException e){
									outToClient.writeBytes("NaN" + '\n');
								}	
							}
							else 
								outToClient.writeBytes("Already existent document" + '\n');
						}
					}
					
					
					//Richiesta di visualizzazione della lista dei documenti su cui l'utente è autorizzato a lavorare
					if(op.equals("DOCLIST")) {
						
						if(user != null) {
		
							Document tempDoc = null;
							User tempUsername = null;
							
							if(user.getListaDocumenti() != null) {
								Iterator<Entry<String, Document>> documentIterator = user.getListaDocumenti().entrySet().iterator();
								
								//Invio al client i nomi dei documenti insieme ai rispettivi creatori e collaboratori
								while(documentIterator.hasNext()) {
									tempDoc = documentIterator.next().getValue();
									outToClient.writeBytes(tempDoc.getDocumentName() + '\n');
									outToClient.writeBytes(tempDoc.getCreator().getUsername() + '\n');
									
									if(tempDoc.getUtentiAutorizzati() != null) {
										Iterator<Entry<String, User>> userIterator = tempDoc.getUtentiAutorizzati().entrySet().iterator();
										while(userIterator.hasNext()) {
											tempUsername = userIterator.next().getValue();
											outToClient.writeBytes(tempUsername.getUsername() + '\n');
										}
									}
									outToClient.writeBytes("END_DOCUMENT" + '\n');
								}
							}	
							outToClient.writeBytes("STOP" + '\n');
						}
					}
					
					
					//Richiesta di modifica di una sezione di un documento
					if(op.equals("MODIFY")) {
						
						if(user != null) {
							String docName = inFromClient.readLine();
							String secNum = inFromClient.readLine();
							
							//Controllo che il documento esista
							if(documenti.containsKey(docName)) {
								
								int secNumber = 0;
								//Controllo che il numero di sezione inserito dall'utente sia valido
								try {
									secNumber = Integer.parseInt(secNum);
	
								
								if(secNumber < user.getDocument(docName).getSecNum() && secNumber >= 0) {
									
										//Controllo che l'utente sia autorizzato a modificare il documento in questione
										if(user.containsDocument(docName)) {
											//Recupero la sezione
											Section section = user.getDocument(docName).getSection(secNumber);
											if(section != null) {
												//Controllo che nessun altro utente stia già editando la sezione
												if(!section.isInEditing()) {
													tempSection = section;
													//Prendo la lock
													section.lockSection();
													outToClient.writeBytes("Inizia" + '\n');
													section.setEditing(true);
													
													/*Invio al client la porta associata all'utente sulla quale il client creerà 
													 * il SocketAddress da associare al SocketChannel per la ricezione della sezione
													 */
													String port = user.getSendPort().toString();
													outToClient.writeBytes(port + '\n');	
													
													
													String path = section.getPath();
													
													String add = user.getDocument(docName).getInetAddress();
													add = add.substring(0);
													//Invio al client il path del file 
													outToClient.writeBytes(path + '\n');
													outToClient.writeBytes(user.getUsername() + '\n');	
													//Invio al client l'indirizzo multicast legato al documento
													outToClient.writeBytes(add + '\n');	
													
													Thread.sleep(500);
													//Invio la sezione
													section.sendSection(Integer.parseInt(port));
													//Setto lo stato dell'utente come in editing
													user.setStatoUtente(Stato.editing);
												}
												else
													outToClient.writeBytes("In editing" + '\n');	
											}
										}	
										else 
											outToClient.writeBytes("Permission denied" + '\n');	
									}
									else 
										outToClient.writeBytes("Invalid secNum" + '\n');
								}
								catch (NumberFormatException e){
									outToClient.writeBytes("NaN" + '\n');
								}	
							}
							else 
								outToClient.writeBytes("Non-existent document" + '\n');	
						}
					}
					
							
					//Richiesta di EndEdit
					if(op.equals("ENDEDIT")) {
						
						if(user != null) {
							String docName = inFromClient.readLine();
							String secNum = inFromClient.readLine();
						
							//Recupero la sezione
							Section section = user.getDocument(docName).getSection(Integer.parseInt(secNum));
							if(section != null) {
								/*Invio al client la porta associata all'utente sulla quale il client creerà 
								 * il SocketAddress da associare al SocketChannel per l'invio della sezione
								 */
								String port = user.getReceivePort().toString();
								outToClient.writeBytes(port + '\n');	
								
								//Invio al client il path del file
								String path = section.getPath();
								outToClient.writeBytes(path + '\n');
				
								outToClient.writeBytes(user.getUsername() + '\n');	
								
								Thread.sleep(500);
								//Ricevo la sezione modificata
								section.receiveSection(Integer.parseInt(port));	
								
								outToClient.writeBytes("success"+ '\n');	
								
								//Setto lo stato dell'utente a logged e rilascio la lock
								user.setStatoUtente(Stato.logged_in);
								tempSection = null;
								section.setEditing(false);
								section.unlockSection();
							}
						}
					}
						
						
					//Richiesta di visualizzazione di un documento
					if(op.equals("SHOWDOC")) {
						
						if(user != null) {
							String docName = inFromClient.readLine();
							
							//Controllo che il documento esista
							if(documenti.containsKey(docName)) {
								//Controllo che l'utente sia autorizzato ad accedere al documento
								if(user.containsDocument(docName)) {
									outToClient.writeBytes("Inizia" + '\n');
									
									String port = user.getSendPort().toString();
									outToClient.writeBytes(port + '\n');
									
									outToClient.writeBytes(user.getUsername() + '\n');
									
									Document tmp = user.getDocument(docName);
									
									if(tmp != null) {
										
										Thread.sleep(500);
										//Invio il documento
										tmp.sendDocument(Integer.parseInt(port));		
									}
								}	
								else 
									outToClient.writeBytes("Permission denied" + '\n');	
							}
							else 
								outToClient.writeBytes("Non-existent document" + '\n');	
						}
					}			
					
					
					//Richiesta di visualizzazione della sezione
					if(op.equals("SHOWSEC")) {
						
						if(user != null) {
							String docName = inFromClient.readLine();
							String secNum = inFromClient.readLine();
							
							//Controllo che il documento esista
							if(documenti.containsKey(docName)) {
								
								int secNumber = Integer.parseInt(secNum);
								
								//Controllo che il numero di sequenza fornito dall'utente sia valido
								if(secNumber < user.getDocument(docName).getSecNum() && secNumber >= 0) {
									
									//Controllo che l'utente sia autorizzato ad accedere al documento
									if(user.containsDocument(docName)) {
										
										//Recupero la sezione
										Section section = user.getDocument(docName).getSection(Integer.parseInt(secNum));
										
										if(section != null) {
											outToClient.writeBytes("Inizia" + '\n');
											String port = user.getSendPort().toString();
											outToClient.writeBytes(port + '\n');	
											
											String username = user.getUsername();
											outToClient.writeBytes(username + '\n');	
											
											String path = section.getPath();
											outToClient.writeBytes(path + '\n');	
											
											String inEditing = null;
											
											//Segnalo all'utente se la sezione sia o meno in fase di editing da parte di un altro utente
											if(section.isInEditing())
												inEditing = "true";
											else
												inEditing = "false";
											outToClient.writeBytes(inEditing + '\n');	
											
											Thread.sleep(500);
											//Invio la sezione
											section.sendSection(Integer.parseInt(port));	
										}
									}	
									else 
										outToClient.writeBytes("Permission denied" + '\n');	
								}
								else
									outToClient.writeBytes("Invalid secNum" + '\n');
							}
							else 
								outToClient.writeBytes("Non-existent document" + '\n');	
						}
					}			
					
					
					//Richiesta di invito di un utente a collaborare ad un documento
					if(op.equals("INVITE")) {
						
						if(user != null) {
							//Leggo l'username dell'utente che si vuole invitare
							String username = inFromClient.readLine();
							String docName = inFromClient.readLine();
							Document tmp = user.getDocument(docName);
							
							//Controllo che il documento esista
							if(documenti.containsKey(docName)) {
								//Controllo che l'utente sia il creatore del documento
								if(tmp.getCreator() == user) {
									
									//Controllo che l'utente invitato sia registrato
									if(utentiRegistrati.containsKey(username)) {
										User invited = utentiRegistrati.get(username);
										
										//Controllo che l'utente invitato non sia già un collaboratore
										if(!invited.getListaDocumenti().containsKey(docName)) {
											
											//Inserisco l'utente tra gli utenti autorizzati ad accedere e modificare il documento
											tmp.getUtentiAutorizzati().put(username, invited);
											//Inserisco il documento nella lista dei documenti che l'utente è autorizzato ad accedere e modificare
											invited.getListaDocumenti().put(docName, tmp);
											
											//Controllo se l'utente invitato è online
											if(utentiOnline.containsKey(username)) {
												//Inserisco il documento nella lista degli online dell'utente invitato
												invited.addOnlineInvitation(tmp);
												//Setto a true il flag relativo alle notifiche online dell'utente
												invited.setOnlineInvitations(true);
												outToClient.writeBytes("Sent" + '\n');
												
											}
												
											else {
												//Inserisco nella lista degli inviti pendenti dell'utente invitato il nome del documento
												invited.addOfflineInvitation(docName);
												//Setto a true il flag relativo alle notifiche pendenti dell'utente
												invited.setOfflineInvitations(true);
												outToClient.writeBytes("Sent" + '\n');
											}
										}
										else 
											outToClient.writeBytes("User is a collaborator" + '\n');
									}
									else
										outToClient.writeBytes("Unregistered user" + '\n');
								}
								else 
									//Non sei il creatore del documento
									outToClient.writeBytes("Permission denied" + '\n');
							}
							else
								outToClient.writeBytes("Non-existent document" + '\n');	
								
						}
							
							
					}
						
					
					
					//Richiesta di logout
					if(op.equals("LOGOUT")) {
						//Setto lo stato dell'utente a registrato
						user.setStatoUtente(Stato.registered);
						//Rimuovo l'utente dagli utenti online
						utentiOnline.remove(user.getUsername());
						user = null;
						outToClient.writeBytes("Logout successful" + '\n');
						
						//Chiudo la connessione con il client
						connectionSocket.close();
						outToClient.close();
						inFromClient.close();
						return;
					}
				}
				
				else {
					if(user != null) {
						utentiOnline.remove(user.getUsername());
						user.setStatoUtente(Stato.registered);
						user = null;
					}
					if(tempSection != null) {
						tempSection.setEditing(false);
						tempSection.unlockSection();
						tempSection = null;
					}
					connectionSocket.close();
					outToClient.close();
					inFromClient.close();
					break;
				}
					
			}	
			
		}
				
		catch (IOException e) {

			try {
				if(user != null) {
					utentiOnline.remove(user.getUsername());
					user.setStatoUtente(Stato.registered);
					user = null;
				}
				if(tempSection != null) {
					tempSection.setEditing(false);
					tempSection.unlockSection();
					tempSection = null;
				}
				connectionSocket.close();
				return;
			}
			catch (IOException e1) {
				e1.printStackTrace();
			}	
		}
		catch (InterruptedException e) {
				
			e.printStackTrace();
		}
		
		
		
	}	
}
					
						
	
		
