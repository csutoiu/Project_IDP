package NIO;

import java.awt.Color;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Controllers.ApplicationController;
import Controllers.Constants;
import Controllers.ControlUtil;
import Controllers.DashboardController;
import Models.GroupInfo;
import Models.Group;
import Models.OnlineUser;
import Models.Shape;
import Models.User;
import Models.UserText;

public class MessageHandler {
	private static Logger logger = Logger.getLogger(MessageHandler.class);
	
	public static String getSendEventMessage(int eventKey, Object...args) {
		String message = "";
		
		switch (eventKey) {
		case Constants.SIGN_IN_EVENT:
			OnlineUser user = (OnlineUser)args[0];
			message = "{\"method\":\"".concat("sign_in").concat("\",\"username\":\"").
					concat(user.getUsername()).concat("\",\"ip\":\"").
					concat(user.getIp()).concat("\",\"port\":\"").
					concat(user.getPort()).concat("\"}");
			break;
		
		case Constants.SIGN_UP_EVENT:
			message = "{\"method\":\"".concat("sign_up").concat("\",\"username\":\"").
					concat((String) args[0]).concat("\",\"email\":\"").
					concat((String) args[1]).concat("\",\"password\":\"").
					concat((String) args[2]).concat("\"}");
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
			break;
			
		case Constants.LEAVE_GROUP_EVENT:
			message = "{\"method\":\"".concat("leave_group").concat("\",\"username\":\"").
					concat((String) args[0]).concat("\",\"groupname\":\"").
					concat((String) args[1]).concat("\"}");
			break;
			
		case Constants.ADD_SHAPE_EVENT:
			message = "{\"method\":\"".concat("add_shape").concat("\",\"groupname\":\"").
					concat((String) args[0]).concat("\",\"form\":\"").
					concat((String) args[1]).concat("\",\"color\":\"").
					concat((String) args[2]).concat("\",\"x\":\"").
					concat((String) args[3]).concat("\",\"y\":\"").
					concat((String) args[4]).concat("\"}");
			break;
		
		case Constants.CANVAS_REQUEST_EVENT:
			message = "{\"method\":\"".concat("canvas_request").concat("\",\"username\":\"").
					concat((String) args[0]).concat("\",\"groupname\":\"").
					concat((String) args[1]).concat("\"}");
			break;
			
		case Constants.CANVAS_RESPONSE_EVENT:
			message = "{\"method\":\"".concat("canvas_response").concat("\",\"groupname\":\"").
					concat((String) args[0]).concat("\",\"shapes\":").
					concat((String) args[1]).concat("}");
			break;
			
		case Constants.CHAT_REQUEST_EVENT:
			message = "{\"method\":\"".concat("chat_request").concat("\",\"username\":\"").
					concat((String) args[0]).concat("\",\"groupname\":\"").
					concat((String) args[1]).concat("\"}");
			break;
		
		case Constants.CHAT_RESPONSE_EVENT:
			message = "{\"method\":\"".concat("chat_response").concat("\",\"groupname\":\"").
					concat((String) args[0]).concat("\",\"chat\":").
					concat((String) args[1]).concat("}");
			break;
		
		case Constants.SEND_MESSAGE_EVENT:
			message = "{\"method\":\"".concat("chat_message").concat("\",\"groupname\":\"").
					concat((String) args[0]).concat("\",\"text\":\"").
					concat((String) args[1]).concat("\",\"color\":\"").
					concat((String) args[2]).concat("\",\"size\":\"").
					concat((String) args[3]).concat("\"}");
			break;

		default:
			break;
		}
		
		logger.debug("Message to send is " + message);
		return message;
	}
	
	public static void getReceiveEventMessage(String message) throws JSONException {
		JSONObject json = new JSONObject(message);
		logger.debug("Message received is " + message);
		
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
			
			int index = DashboardController.getInstance().getSelectedIndexTab();
			if(index != -1) {
				String groupName = DashboardController.getInstance().getFrame().getTabbedPane().getTitleAt(index);
				Group  group = ApplicationController.getInstance().getGroup(groupName);
				DashboardController.getInstance().getFrame().updateLegend(DashboardController.getInstance().getUsersAndColors(group));
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
			
			user.getGroups().add(group);
			group.setOnlineUser(color, user);
			
			DashboardController.getInstance().getFrame().addNewUserToGroup(username, groupname);

			if(DashboardController.getInstance().getCurrentUser().equals(user.getUsername())) {
				logger.debug("Add me in " + groupname + " group.");
				
				DashboardController.getInstance().getFrame().insertNewTab(groupname);
				DashboardController.getInstance().getCanvasOfGroup(groupname).setColor(ControlUtil.getNewColor(color));
				DashboardController.getInstance().getGroupInfo(group);
				
			}

			int index = DashboardController.getInstance().getSelectedIndexTab();
			if(index != -1) {
				String groupName = DashboardController.getInstance().getFrame().getTabbedPane().getTitleAt(index);
				if(groupName.equals(groupname)) {
					logger.debug("Need update to legend.");
					DashboardController.getInstance().getFrame().updateLegend(DashboardController.getInstance().getUsersAndColors(group));
				}
			}
			

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
			
			int index = DashboardController.getInstance().getSelectedIndexTab();
			if(index != -1) {
				String groupName = DashboardController.getInstance().getFrame().getTabbedPane().getTitleAt(index);
				if(groupName.equals(groupname)) {
					logger.debug("Need update to legend.");
					DashboardController.getInstance().getFrame().updateLegend(DashboardController.getInstance().getUsersAndColors(group));
				}
			}
		}
		
		else if(method.equals("add_shape")) {
			String groupname = json.getString("groupname");
			String figure = json.getString("form");
			Color color = ControlUtil.getNewColor(json.getString("color"));
			int x = Integer.parseInt(json.getString("x"));
			int y = Integer.parseInt(json.getString("y"));
			
			DashboardController.getInstance().updateCanvas(groupname, figure, color, x, y);
		}
		
		else if(method.equals("canvas_request")) {
			String groupname = json.getString("groupname");
			String username = json.getString("username");
			
			OnlineUser user = ApplicationController.getInstance().getOnlineUser(username);
			GroupInfo info = DashboardController.getInstance().getGroupInfo(groupname);
			String canvasShapes = info.getCanvasShapes();
			
			NetworkManager.getInstance().sendRequestToUser(MessageHandler.getSendEventMessage(Constants.CANVAS_RESPONSE_EVENT, 
														 groupname, canvasShapes), user);
		}
		
		else if(method.equals("canvas_response")) {
			String groupname = json.getString("groupname");
			
			GroupInfo info = DashboardController.getInstance().getGroupInfo(groupname);
			JSONArray jsonArray = json.getJSONArray("shapes");
			for (int i = 0; i < jsonArray.length(); i++) {
		        JSONObject jsonObject = jsonArray.getJSONObject(i);
		        
		        String figure = jsonObject.getString("form");
				String color = jsonObject.getString("color");
				String x = jsonObject.getString("x");
				String y = jsonObject.getString("y");
				
				logger.debug("Need update canvas.");
				info.getCanvas().getShapes().add(new Shape(figure, ControlUtil.getNewColor(color), Integer.parseInt(x), 
						Integer.parseInt(y)));
				
				info.getCanvas().repaint();
			}
		}
		
		else if(method.equals("chat_request")) {
			String groupname = json.getString("groupname");
			String username = json.getString("username");
			
			OnlineUser user = ApplicationController.getInstance().getOnlineUser(username);
			GroupInfo info = DashboardController.getInstance().getGroupInfo(groupname);
			String chat = info.getHistoryChat();
			
			NetworkManager.getInstance().sendRequestToUser(MessageHandler.getSendEventMessage(Constants.CHAT_RESPONSE_EVENT, 
														 groupname, chat), user);
		}
		
		else if(method.equals("chat_response")) {
			String groupname = json.getString("groupname");
			
			logger.debug("Need update chat.");
			
			GroupInfo info = DashboardController.getInstance().getGroupInfo(groupname);
			JSONArray jsonArray = json.getJSONArray("chat");
			for (int i = 0; i < jsonArray.length(); i++) {
		        JSONObject jsonObject = jsonArray.getJSONObject(i);
		        
		        String text = jsonObject.getString("text");
				String color = jsonObject.getString("color");
				String size = jsonObject.getString("size");
				
				info.getChat().add(new UserText(text, color, size));
			}
			
			DashboardController.getInstance().updateChatArea(info);
		}
		
		else if(method.equals("chat_message")) {
			String groupname = json.getString("groupname");
			String text = json.getString("text");
			String color = json.getString("color");
			String size = json.getString("size");
			
			GroupInfo info = DashboardController.getInstance().getGroupInfo(groupname);
			UserText txt = new UserText(text, color, size);
			info.getChat().add(txt);
			DashboardController.getInstance().addReceivedTextInChat(txt);
		}
	}
}
