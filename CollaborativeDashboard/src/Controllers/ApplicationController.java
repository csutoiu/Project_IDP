package Controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.xml.crypto.Data;

import DataBase.DataBaseManager;
import Models.Group;
import Models.OnlineUser;
import Models.User;

public class ApplicationController {

	private static ApplicationController me;
	
	private ArrayList<User> users;
	private ArrayList<OnlineUser> onlineUsers;
	private ArrayList<Group> groups;

    private ApplicationController() {
    }

    public static ApplicationController getInstance() {
        if (me == null) {
            me = new ApplicationController();
        	me.users = new ArrayList<User>();
        	me.onlineUsers = new ArrayList<OnlineUser>();
            me.groups = new ArrayList<Group>();
        }
        return me;
    }
    
    public void setInfo() throws Exception {
    	DataBaseManager.setUsers();
    	DataBaseManager.setOnlineUsers();
    	DataBaseManager.setGroups();
    }
    
    public ArrayList<User> getUsers() {
    	return this.users;
    }
    
    public ArrayList<OnlineUser> getOnlineUsers() {
    	return this.onlineUsers;
    }
    
    public ArrayList<Group> getGroups() {
    	return this.groups;
    }
    
    public Group getGroup(String groupname) {
    	for(Group group : this.groups) {
    		if(group.getGroupName().equals(groupname))
    			return group;
    	}
    	return null;
    }
    
    public User getUser(String username) {
    	for(User user : this.users) {
    		if(user.getUsername().equals(username))
    			return user;
    	}
    	return null;
    }
    
    public OnlineUser getOnlineUser(String username) {
    	for(OnlineUser user : this.onlineUsers) {
    		if(user.getUsername().equals(username))
    			return user;
    	}
    	return null;
    }
 
}
