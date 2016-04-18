package NIO;

import java.io.StringReader;

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
import Models.OnlineUser;
import Models.User;

public class MessageHandler {
	
	public static String getSendEventMessage(int eventKey, Object arg0, Object arg1) {
		String message = "";
		
		switch (eventKey) {
		case Constants.SIGN_IN_EVENT:
			OnlineUser user = (OnlineUser)arg0;
			message = "{\"method\":\"".concat("sign_in").concat("\",\"username\":\"").
					concat(user.getUsername()).concat("\",\"ip\":\"").
					concat(user.getIp()).concat("\",\"port\":\"").
					concat(user.getPort()).concat("\"}");
			System.out.println("Message to send is : "+message);
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
			
			ApplicationController.getInstance().getOnlineUsers().add(new OnlineUser(username, ip, port));
			DashboardController.getInstance().getFrame().updateOnlineUsersList();
		}
		
	}

}
