package Controllers;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import DataBase.DataBaseManager;
import Models.Group;
import Models.User;
import gui.DashboardFrame;
import gui.LaunchFrame;
import gui.MyCanvas;

public class DashboardController implements ActionListener {

	private static DashboardController me;
	private ApplicationController application = ApplicationController.getInstance();
	private DashboardFrame view;
	
	private String groupChanged, currentUser;
	private ArrayList<Group> myGroups;
	
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
    
    public void setCurrentUser(String username) {
    	this.currentUser = username;
    	this.setMyGroups();
    }
    
    public void setMyGroups() {
    	ArrayList<Group> groups = application.getGroups();
    	
    	for(int i = 0;i < groups.size();i++) {
    		ArrayList<User> users = groups.get(i).getUsers();
    		for(int j = 0; j < users.size();j++) {
    			
    			if(this.currentUser.equals(users.get(j).getUsername())) {
    				this.myGroups.add(groups.get(i));
    			}
    		}	
    	}
    	this.view.initializeTabbedPane();
    }
    
    public ArrayList<Group> getMyGroups() {
    	return this.myGroups;
    }
    
    public void setGroupChanged(String groupChanged) {
    	this.groupChanged = groupChanged;
    }

    public HashMap<String, MyCanvas> getCanvasOfGropus() {
    	return this.canvasOfGroups;
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(Constants.LOGOUT)) {
			for(int i = 0; i < application.getOnlineUsers().size();i++) {
				if(application.getOnlineUsers().get(i).getUsername().equals(this.view.getUsername())) {
					application.getOnlineUsers().remove(i);
					break;
				}
			}
			
			DataBaseManager.removeOnlineUserToDataBase(this.view.getUsername());
			//this.removeUserToGroups();
			LaunchFrame frame = new LaunchFrame();
			this.view.getFrame().setVisible(false);
			frame.getFrame().setVisible(true);
			
		} else if(e.getActionCommand().equals(Constants.CREATE_GROUP)) {
			String response = JOptionPane.showInputDialog
					(this.view.getFrame(),"Enter Group Name.","");
			String message = "";
			if(response.isEmpty()) {
				message = "Please enter a valid name.";
			} else {
				for(int i = 0;i < application.getGroups().size();i++) {
					if(response.equals(application.getGroups().get(i).getGroupName())) {
						message = "Group already exists.";
					}
				}
			}
			if(message.isEmpty()) {
				ArrayList<User> users = new ArrayList<>();
				users.add(new User(this.view.getUsername(), null, null));
				Group newGroup = new Group(response, users);
				application.getGroups().add(newGroup);
				ArrayList<String> usersTemp = new ArrayList<>();
				usersTemp.add(this.view.getUsername());
				DataBaseManager.addGroupToDataBase(response, usersTemp);
				this.view.addNewGroup(response);
				this.view.insertNewTab(response);
				this.view.addUserInLegend(this.view.getUsername(), Color.red);
				this.getCanvasOfGropus().get(response).setColor(Color.red);
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
				int i;
				for(i = 0;i < application.getOnlineUsers().size();i++) {
					if(response.equals(application.getOnlineUsers().get(i).getUsername())) {
						break;
					}
				}
				if(i == application.getOnlineUsers().size()) {
					message = "User is not online.";
				} else {
					for(int j = 0;j < this.getUsersOfGroup(groupChanged).length;j++) {
						if(response.equals(this.getUsersOfGroup(groupChanged)[j])) {
							message = "User already exists in group";
							break;
						}	
					}
				}
			}
			
			if(message.isEmpty()) {
				Group group = null;
				for(int i = 0;i < application.getGroups().size();i++) {
					if(groupChanged.equals(application.getGroups().get(i).getGroupName())) {
						group = application.getGroups().get(i);
						break;
					}
				}
				group.getUsers().add(new User(response, null, null));
				DataBaseManager.addNewUserToGroup(response, groupChanged);
				this.view.addNewUserToGroup(response, groupChanged);
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
			
			
			Group group = null;
			for(int i = 0;i < application.getGroups().size();i++) {
				if(groupChanged.equals(application.getGroups().get(i).getGroupName())) {
					group = application.getGroups().get(i);
					break;
				}
			}
			User newUser = new User(this.view.getUsername(), null, null, color);
			group.getUsers().add(newUser);
			DataBaseManager.addNewUserToGroup(this.view.getUsername(), groupChanged);
			this.view.addNewUserToGroup(this.view.getUsername(), groupChanged);
			this.view.insertNewTab(groupChanged);
			this.view.addUserInLegend(this.view.getUsername(), ControlUtil.getNewColor(color));
			this.canvasOfGroups.get(groupChanged).setColor(ControlUtil.getNewColor(color));
		}
		
		else if(e.getActionCommand().equals(Constants.LEAVE_GROUP)) {
			Group group = null;
			for(int i = 0;i < application.getGroups().size();i++) {
				if(groupChanged.equals(application.getGroups().get(i).getGroupName())) {
					group = application.getGroups().get(i);
					break;
				}
			}
			for(int i = 0;i < group.getUsers().size();i++) {
				if(group.getUsers().get(i).getUsername().equals(this.view.getUsername())) {
					group.getUsers().remove(i);
					break;
				}
			}
			if(group.getUsers().size() > 0) {
				DataBaseManager.removeUserToGroup(this.view.getUsername(), groupChanged);
				this.view.removeUserToGroup(this.view.getUsername(), groupChanged);
			} else {
				application.getGroups().remove(group);
				DataBaseManager.deleteGroup(groupChanged);
				this.view.deleteGroup(groupChanged);
			}
			
			this.view.removeGroupTab(groupChanged);
			this.getCanvasOfGropus().remove(groupChanged);
			
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
	public void removeUserToGroups() {
		ArrayList<Group> groups = application.getGroups();
		for(int i = 0; i < groups.size();i++) {
			DataBaseManager.removeUserToGroup(this.view.getUsername(), groups.get(i).getGroupName());
			ArrayList<User> users = groups.get(i).getUsers();
			for(int j = 0;j < users.size();j++) {
				if(users.get(j).getUsername().equals(this.view.getUsername())) {
					users.remove(j);
					break;
				}
			}
			if(users.isEmpty()) {
				DataBaseManager.deleteGroup(groups.get(i).getGroupName());
				groups.remove(i);
			}
		}
	}
	
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
		String[] users = null;
		for(int i = 0;i < application.getGroups().size();i++) {
			if(groupName.equals(application.getGroups().get(i).getGroupName())) {
				Group group = application.getGroups().get(i);
				users = new String[group.getUsers().size()];
				for(int j = 0;j < group.getUsers().size();j++) {
					users[j] = group.getUsers().get(j).getUsername();
				}
				break;
			}
		}
		return users;
	}
}
