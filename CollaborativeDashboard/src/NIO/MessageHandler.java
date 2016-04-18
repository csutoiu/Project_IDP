package NIO;

import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import Controllers.ApplicationController;
import Controllers.Constants;
import Controllers.DashboardController;
import DataBase.DataBaseManager;
import Models.Group;
import Models.OnlineUser;
import Models.User;

public class MessageHandler {
	
	public static String getSendEventMessage(int eventKey, Object...args) {
		String message = "";
		
		switch (eventKey) {
		case Constants.SIGN_IN_EVENT:
			OnlineUser user = (OnlineUser)args[0];
			message = "{\"method\":\"".concat("sign_in").concat("\",\"username\":\"").
					concat(user.getUsername()).concat("\",\"ip\":\"").
					concat(user.getIp()).concat("\",\"port\":\"").
					concat(user.getPort()).concat("\"}");
			System.out.println("Message to send is : "+message);
			break;
		
		case Constants.SIGN_UP_EVENT:
			message = "{\"method\":\"".concat("sign_up").concat("\",\"username\":\"").
					concat((String) args[0]).concat("\",\"email\":\"").
					concat((String) args[1]).concat("\",\"password\":\"").
					concat((String) args[2]).concat("\"}");
			System.out.println("Message to send is : "+message);
			break;
		
		case Constants.LOGOUT_EVENT:
			message = "{\"method\":\"".concat("logout").concat("\",\"username\":\"").
					concat((String) args[0]).concat("\"}");
			break;
		
		case Constants.CREATE_GROUP_EVENT:
			message = "{\"method\":\"".concat("create_group").concat("\",\"username\":\"").
			concat((String) args[0]).concat("\",\"groupname\":\"").
			concat((String) args[1]).concat("\"}");
			break;
			
		case Constants.ADD_USER_TO_GROUP_EVENT:
			message = "{\"method\":\"".concat("add_uToG").concat("\",\"username\":\"").
					concat((String) args[0]).concat("\",\"groupname\":\"").
					concat((String) args[1]).concat("\",\"color\":\"").
					concat((String) args[2]).concat("\"}");
			System.out.println("Message to send is : "+ message);
			break;
			
		case Constants.LEAVE_GROUP_EVENT:
			message = "{\"method\":\"".concat("leave_group").concat("\",\"username\":\"").
			concat((String) args[0]).concat("\",\"groupname\":\"").
			concat((String) args[1]).concat("\"}");
			System.out.println("Message to send is : "+ message);
			break;

		default:
			break;
		}
		
		return message;
	}
	
	public static void getReceiveEventMessage(String message) throws JSONException {
		JSONObject json = new JSONObject(message);
		
		String method = json.getString("method");
		if(method.equals("sign_in")) {
			String username = json.getString("username");
			String ip = json.getString("ip");
			String port = json.getString("port");
			
			OnlineUser user = new OnlineUser(username, ip, port);
			
			if(!ApplicationController.getInstance().getOnlineUsers().contains(user)) {
				ApplicationController.getInstance().getOnlineUsers().add(user);
				DashboardController.getInstance().getFrame().updateOnlineUsersList();
			}
			
			System.out.println("New user: "+username);
			
		} else if(method.equals("sign_up")) {
			String username = json.getString("username");
			String email = json.getString("email");
			String password = json.getString("password");
			
			User user = new User(username, email, password);
			if(!ApplicationController.getInstance().getUsers().contains(user)) {
				ApplicationController.getInstance().getUsers().add(new User(username, email, password));
			}
			
		} else if(method.equals("logout")) {
			String username = json.getString("username");
			
			OnlineUser user = ApplicationController.getInstance().getOnlineUser(username);
			if(user != null) {
				ApplicationController.getInstance().getOnlineUsers().remove(user);
				DashboardController.getInstance().getFrame().updateOnlineUsersList();
			}
			
			DashboardController.getInstance().removeUserToGroups(user);
			for(Group group : user.getGroups()) {
				if(group.getUsers().size() > 0) {
					DashboardController.getInstance().getFrame().removeUserToGroup(username, group.getGroupName());
				} else {
					DashboardController.getInstance().getFrame().deleteGroup(group.getGroupName());
				}
			}
			
			
			
		} else if(method.equals("create_group")) {
			String username = json.getString("username");
			String groupname = json.getString("groupname");
			
			OnlineUser user = ApplicationController.getInstance().getOnlineUser(username);
			Group newGroup = new Group(groupname);
			user.getGroups().add(newGroup);
			newGroup.setOnlineUser("red", user);
			ApplicationController.getInstance().getGroups().add(newGroup);
			DashboardController.getInstance().getFrame().addNewGroup(groupname);
		}
		
		else if(method.equals("add_uToG")) {
			String username = json.getString("username");
			String groupname = json.getString("groupname");
			String color = json.getString("color");
			
			OnlineUser user = ApplicationController.getInstance().getOnlineUser(username);
			Group group = ApplicationController.getInstance().getGroup(groupname);
			
			System.out.println("------------ username " + user.getUsername() + "group " + group.getGroupName());
			
			user.getGroups().add(group);
			group.setOnlineUser(color, user);
			
			DashboardController.getInstance().getFrame().addNewUserToGroup(username, groupname);
		}
		
		else if(method.equals("leave_group")) {
			String username = json.getString("username");
			String groupname = json.getString("groupname");
			
			OnlineUser user = ApplicationController.getInstance().getOnlineUser(username);
			Group group = ApplicationController.getInstance().getGroup(groupname);
			
			user.getGroups().remove(group);
			
			String key = "";
			for (Iterator<Map.Entry<String,OnlineUser>> it = group.getUsers().entrySet().iterator(); it.hasNext();) {
				 Map.Entry<String,OnlineUser> elem = it.next();
				 if(user.equals(elem.getValue())) {
					 key = elem.getKey();
				 }
			}
			group.getUsers().remove(key);

			if(group.getUsers().size() > 0) {
				DashboardController.getInstance().getFrame().removeUserToGroup(username, groupname);
			} else {
				ApplicationController.getInstance().getGroups().remove(group);
				DashboardController.getInstance().getFrame().deleteGroup(groupname);
			}
		}
		
	}

}
