package latoServer;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface RMIServerInterface extends Remote {
	
	 public void registrationRequest(String username, String password) throws RemoteException;
	 
	 public boolean isRegistered(String username) throws RemoteException;
	
	 
}
