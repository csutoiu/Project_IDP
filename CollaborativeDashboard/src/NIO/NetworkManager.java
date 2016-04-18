package NIO;

import Controllers.ApplicationController;
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
    		if(!user.getUsername().equals(currentUser.getUsername())) {
    			Thread clientSever = new Thread(new Client("localhost", user.getPort(), message));
    			clientSever.start();
    		}
    	}
    }
}
