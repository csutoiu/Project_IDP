package Controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import Models.Group;
import Models.User;

public class ApplicationController {

	private static ApplicationController me;
	
	// TO DO - complete from data base //
	private ArrayList<User> users;
	private ArrayList<User> onlineUsers;
	private ArrayList<Group> groups;

    private ApplicationController() {
    }

    public static ApplicationController getInstance() {
        if (me == null) {
            me = new ApplicationController();
        	try {
        		me.users = ApplicationController.createUsersList();
        	} catch (Exception e) {
        		throw new ExceptionInInitializerError(e);
        	}
        	try {
        		me.groups = ApplicationController.createGroupsList();
        	} catch (Exception e) {
        		throw new ExceptionInInitializerError(e);
        	}
            try {
            	me.onlineUsers = ApplicationController.createOnlineUsersList();
            } catch (Exception e) {
        		throw new ExceptionInInitializerError(e);
        	} 
        }

        return me;
    }
    
    private static ArrayList<User> createUsersList() {
    	ArrayList<User> users = new ArrayList<User>();
    	
    	File f = new File("users.txt");
		if(f.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
			    String line;
			    while ((line = br.readLine()) != null) {
			       String[] fields = line.split(" ");
			       users.add(new User(fields[0], fields[1], fields[2]));
			    }
			} catch (Exception e) {
				System.out.println(e);
			}
		}

    	return users;
    }
    
    private static ArrayList<User> createOnlineUsersList() {
    	ArrayList<User> onlineUsers = new ArrayList<User>();
    	
    	File f = new File("onlineUsers.txt");
		if(f.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader("onlineUsers.txt"))) {
			    String line;
			    while ((line = br.readLine()) != null) {
			       String[] fields = line.split(" ");
			       onlineUsers.add(new User(fields[0], null, fields[1]));
			    }
			} catch (Exception e) {
				System.out.println(e);
			}
		}

    	return onlineUsers;
    }
    
    private static ArrayList<Group> createGroupsList() {
    	ArrayList<Group> groups = new ArrayList<Group>();
    	
    	File f = new File("groups.txt");
		if(f.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader("groups.txt"))) {
			    String line;
			    while ((line = br.readLine()) != null) {
			       String[] fields = line.split(" ");
			       ArrayList<User> users = new ArrayList<User>(); 
			       for(int i = 1;i < fields.length;i++) {
			    	   users.add(new User(fields[i], null, null));
			       }
			       groups.add(new Group(fields[0], users));
			    }
			} catch (Exception e) {
				System.out.println(e);
			}
		}
    	
    	return groups;
    }
    
    public ArrayList<User> getUsers() {
    	return this.users;
    }
    
    public ArrayList<User> getOnlineUsers() {
    	return this.onlineUsers;
    }
    
    public ArrayList<Group> getGroups() {
    	return this.groups;
    }
}
