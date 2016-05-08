package NIO;

import java.util.Iterator;
import java.util.Map;

import Controllers.ApplicationController;
import Models.Group;
import Models.OnlineUser;

public class NetworkManager {
	
	private static NetworkManager me;
	private OnlineUser currentUser;
	
	private NetworkManager() {
    }

    public static NetworkManager getInstance() {
        if (me == null) {
            me = new NetworkManager();
        }

        return me;
    }
    
    public void setCurrentUser(OnlineUser currentUser) {
    	this.currentUser = currentUser;
    }
    
    public void startServer(OnlineUser currentUser) {
    	Thread serverThread = new Thread(new Server("localhost", currentUser.getPort()));
    	serverThread.start();
    }
    
    public void notifyAllUsers(String message) {
    	for(OnlineUser user : ApplicationController.getInstance().getOnlineUsers()) {
    		if(currentUser != null && user.getUsername().equals(currentUser.getUsername())) {
    			continue;
    		}
    		
    		Thread clientSever = new Thread(new Client("localhost", user.getPort(), message));
    		clientSever.start();
    	}
    }
    
    public void notifyAllUsersOfGroup(String message, Group group) {
		for (Iterator<Map.Entry<String,OnlineUser>> it = group.getUsers().entrySet().iterator(); it.hasNext();) {
			 Map.Entry<String,OnlineUser> e = it.next();
			 OnlineUser user = e.getValue();
			 
			 if(currentUser != null && currentUser.getUsername().equals(user.getUsername())) {
				 continue;
			 }
			 
			 Thread clientSever = new Thread(new Client("localhost", user.getPort(), message));
			 clientSever.start();
			 
		}
    }
    
    public void sendRequestToUser(String message, OnlineUser user) {
    	Thread clientSever = new Thread(new Client("localhost", user.getPort(), message));
    	clientSever.start();
    }
}
