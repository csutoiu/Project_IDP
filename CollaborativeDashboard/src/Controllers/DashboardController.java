package Controllers;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;
import javax.xml.crypto.Data;

import DataBase.DataBaseManager;
import Models.Group;
import Models.OnlineUser;
import NIO.Client;
import NIO.MessageHandler;
import NIO.NetworkManager;
import NIO.Server;
import gui.DashboardFrame;
import gui.LaunchFrame;
import gui.MyCanvas;

public class DashboardController implements ActionListener {

	private static DashboardController me;
	private ApplicationController application = ApplicationController.getInstance();
	private DashboardFrame view;
	
	private OnlineUser currentUser;
	private ArrayList<Group> myGroups;
	
	private String groupChanged;
	
	
	private HashMap<String, MyCanvas> canvasOfGroups;
	
	private DashboardController() {
    }

    public static DashboardController getInstance() {
        if (me == null) {
            me = new DashboardController();
            me.myGroups = new ArrayList<Group>();
            me.canvasOfGroups = new HashMap<String, MyCanvas>();
        }

        return me;
    }
    
    public void setView(DashboardFrame view) {
    	this.view = view;
    }
    
    public DashboardFrame getFrame() {
    	return this.view;
    }
    
    public void setInfoUser(String username) {
    	this.currentUser = application.getOnlineUser(username);
    	this.myGroups = this.currentUser.getGroups();
    	
    	try {
    		
    		System.out.println("DashBoard set info with user " + this.currentUser.getUsername());
			application.setInfo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	/* start server */
    	NetworkManager.getInstance().startServer(currentUser);
    	NetworkManager.getInstance().setCurrentUser(currentUser);
    	NetworkManager.getInstance().notifyAllUsers(MessageHandler.getSendEventMessage(Constants.SIGN_IN_EVENT, this.currentUser));
    }
    
    public ArrayList<Group> getMyGroups() {
    	return this.myGroups;
    }
    
    public void setGroupChanged(String groupChanged) {
    	this.groupChanged = groupChanged;
    }

    public HashMap<String, MyCanvas> getCanvasOfGroups() {
    	return this.canvasOfGroups;
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(Constants.LOGOUT)) {
			DataBaseManager.removeOnlineUserToDataBase(this.currentUser);
			this.removeUserToGroups(this.currentUser);
			LaunchFrame frame = new LaunchFrame();
			this.view.getFrame().setVisible(false);
			frame.getFrame().setVisible(true);
			
			NetworkManager.getInstance().notifyAllUsers(MessageHandler.getSendEventMessage(Constants.LOGOUT_EVENT, 
					this.currentUser.getUsername()));
			
		} else if(e.getActionCommand().equals(Constants.CREATE_GROUP)) {
			String response = JOptionPane.showInputDialog
					(this.view.getFrame(),"Enter Group Name.","");
			String message = "";
			Group group;
			if(response.isEmpty()) {
				message = "Please enter a valid name.";
			} else {
				group = application.getGroup(response);
				if(group != null)
					message = "Group already exists.";
			}
			if(message.isEmpty()) {
				group = new Group(response);
				group.setOnlineUser("red", currentUser);
				this.currentUser.getGroups().add(group);
				application.getGroups().add(group);

				DataBaseManager.addGroupToDataBase(response, this.currentUser.getUsername(), "red");
				this.view.addNewGroup(response);
				this.view.insertNewTab(response);
				this.view.updateLegend(this.getUsersAndColors(group));
				this.getCanvasOfGroups().get(response).setColor(Color.red);
				
				NetworkManager.getInstance().notifyAllUsers(MessageHandler.getSendEventMessage
						(Constants.CREATE_GROUP_EVENT, this.currentUser.getUsername(), response));
			} else {
				JOptionPane.showMessageDialog(this.view.getFrame(), message, null, JOptionPane.ERROR_MESSAGE);
			}
			
		} else if(e.getActionCommand().equals(Constants.ADD_USER)) {
			String response = JOptionPane.showInputDialog
					(this.view.getFrame(),"Enter User Name.","");
			String message = "";
			if(response.isEmpty()) {
				message = "Please enter a valid name.";
			} else {
				OnlineUser onlineUser = application.getOnlineUser(response);
				if(onlineUser == null) {
					message = "User is not online.";
				} else {
					if(response.equals(this.currentUser.getUsername())) {
						message = "Pick other user.";
					} else {
						for(int j = 0;j < this.getUsersOfGroup(groupChanged).length;j++) {
							if(response.equals(this.getUsersOfGroup(groupChanged)[j])) {
								message = "User already exists in group";
								break;
							}	
						}
					}
				}
			}
			
			if(message.isEmpty()) {
				Group group = application.getGroup(groupChanged);
				OnlineUser user = application.getOnlineUser(response);
				Object[] possibilities = {"red", "blue", "yellow", "pink", "green", "orange", "magenta", "gray"};
				String color = (String)JOptionPane.showInputDialog(this.view.getFrame(),
				                    "Pick a color:\n",
				                    null, JOptionPane.PLAIN_MESSAGE,
				                    null,
				                    possibilities,
				                    "red");
				if(!group.setOnlineUser(color, user)) {
					JOptionPane.showMessageDialog(this.view.getFrame(), "Pick other color.", null, JOptionPane.ERROR_MESSAGE);
					return;
				}
				user.getGroups().add(group);
				DataBaseManager.addGroupToDataBase(groupChanged, response, color);
				this.view.addNewUserToGroup(response, groupChanged);
				if(this.currentUser.getGroups().contains(group)) {
					this.getFrame().updateLegend(this.getUsersAndColors(group));
				}
				
				NetworkManager.getInstance().notifyAllUsers(MessageHandler.getSendEventMessage(Constants.ADD_USER_TO_GROUP_EVENT, 
						response, groupChanged, color));
				
			} else {
				JOptionPane.showMessageDialog(this.view.getFrame(), message, null, JOptionPane.ERROR_MESSAGE);
			}
		}
		
		else if(e.getActionCommand().equals(Constants.JOIN_GROUP)) {
			Object[] possibilities = {"red", "blue", "yellow", "pink", "green", "orange", "magenta", "gray"};
			String color = (String)JOptionPane.showInputDialog(this.view.getFrame(),
			                    "Pick a color:\n",
			                    null, JOptionPane.PLAIN_MESSAGE,
			                    null,
			                    possibilities,
			                    "red");
			
			
			Group group = application.getGroup(groupChanged);
			if(!group.setOnlineUser(color, currentUser)) {
				JOptionPane.showMessageDialog(this.view.getFrame(), "Pick other color.", null, JOptionPane.ERROR_MESSAGE);
				return;
			}
			currentUser.getGroups().add(group);
			DataBaseManager.addGroupToDataBase(groupChanged, this.currentUser.getUsername(), color);
			this.view.addNewUserToGroup(this.currentUser.getUsername(), groupChanged);
			this.view.insertNewTab(groupChanged);
			this.view.updateLegend(this.getUsersAndColors(group));
			this.canvasOfGroups.get(groupChanged).setColor(ControlUtil.getNewColor(color));
			
			NetworkManager.getInstance().notifyAllUsers(MessageHandler.getSendEventMessage(Constants.ADD_USER_TO_GROUP_EVENT, 
														this.currentUser.getUsername(), groupChanged, color));
		}
		
		else if(e.getActionCommand().equals(Constants.LEAVE_GROUP)) {
			Group group = application.getGroup(groupChanged);
			String key = "";
			for (Iterator<Map.Entry<String,OnlineUser>> it = group.getUsers().entrySet().iterator(); it.hasNext();) {
				 Map.Entry<String,OnlineUser> elem = it.next();
				 if(this.currentUser.equals(elem.getValue())) {
					 key = elem.getKey();
				 }
			}
			group.getUsers().remove(key);
			this.currentUser.getGroups().remove(group);
			
			if(group.getUsers().size() > 0) {
				this.view.removeUserToGroup(this.currentUser.getUsername(), groupChanged);
			} else {
				application.getGroups().remove(group);
				this.view.deleteGroup(groupChanged);
			}
			DataBaseManager.removeUserFromGroup(this.currentUser.getUsername(), groupChanged);
			this.view.removeGroupTab(groupChanged);
			this.getCanvasOfGroups().remove(groupChanged);
		
			NetworkManager.getInstance().notifyAllUsers(MessageHandler.getSendEventMessage
					(Constants.LEAVE_GROUP_EVENT, this.currentUser.getUsername(), groupChanged));
		}
		
		else if(e.getActionCommand().equals(Constants.SAVE_WORK)) {
			this.view.showProgressBar();
		}
		
		else if(e.getActionCommand().equals(Constants.CHANGE_FONT)) {
			this.view.changeFont();
		}
		
		else if(e.getActionCommand().equals(Constants.CHANGE_COLOR)) {
			this.view.changeColor();
		}
		
		else if(e.getActionCommand().equals(Constants.SEND)) {
			this.view.addTextToChat();
		}
	}
	
	/* remove user to data base at logout */
	public void removeUserToGroups(OnlineUser user) {
		for(Group group : user.getGroups()) {
			for (Iterator<Map.Entry<String,OnlineUser>> it = group.getUsers().entrySet().iterator(); it.hasNext();) {
				 Map.Entry<String,OnlineUser> e = it.next();
				 if (user.equals(e.getValue())) {
					 it.remove();
				 }
			}
			if(group.getUsers().isEmpty()) {
				application.getGroups().remove(group);
			}
		}
		DataBaseManager.removeUserFromGroups(user.getUsername());
	}
	
	/* Methods used by frame */
	public String getCurrentUser() {
		return this.view.getUsername();
	}
	
	public String[] getOnlineUsers() {
		String[] onlineUsers = new String[application.getOnlineUsers().size()];
		for(int i = 0; i < application.getOnlineUsers().size(); i++) {
			onlineUsers[i] = application.getOnlineUsers().get(i).getUsername();
		} 
		
		return onlineUsers;
	}
	
	public String[] getGroups() {
		String[] groups = new String[application.getGroups().size()];
		for(int i = 0;i < application.getGroups().size();i++) {
			groups[i] = application.getGroups().get(i).getGroupName();
		}
		return groups;
	}
	
	public String[] getUsersOfGroup(String groupName) {
		Group group = application.getGroup(groupName);
		String[] users = new String[group.getUsers().size()];
		int i = 0;
		for (Iterator<Map.Entry<String,OnlineUser>> it = group.getUsers().entrySet().iterator(); it.hasNext();i++) {
			 Map.Entry<String,OnlineUser> e = it.next();
			 users[i] = e.getValue().getUsername();
			 
		}
		return users;
	}
	
	public HashMap<String, String> getUsersAndColors(Group group) {
		HashMap<String, String> hash = new HashMap<String, String>();
		for (Iterator<Map.Entry<String,OnlineUser>> it = group.getUsers().entrySet().iterator(); it.hasNext();) {
			 Map.Entry<String,OnlineUser> e = it.next();
			 hash.put(e.getKey(), e.getValue().getUsername());
		}
		return hash;
	}
}
