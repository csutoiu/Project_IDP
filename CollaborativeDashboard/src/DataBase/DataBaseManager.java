package DataBase;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import Controllers.ApplicationController;
import Models.Group;
import Models.OnlineUser;
import Models.User;

public class DataBaseManager {
	
	private static String baseUrl = "http://idp.16mb.com/index.php";
	private static String USER_AGENT = "Mozilla/5.0";
	public int port = 11001;
	
	private static Logger logger = Logger.getLogger(DataBaseManager.class);
	
	private static String GET(String additionalUrl) throws IOException {
		
		URL url = new URL(baseUrl.concat(additionalUrl));
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		// optional default is GET
		con.setRequestMethod("GET");
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		return response.toString();
	}
	
	public static org.w3c.dom.Document loadXMLFromString(String xml) throws Exception {
		
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return builder.parse(is);
	}
	
	public static void setUsers() throws Exception {
		String xml = DataBaseManager.GET("?cmd=get_users");
		org.w3c.dom.Document document = DataBaseManager.loadXMLFromString(xml);
		document.getDocumentElement().normalize();
		
		NodeList nList = document.getElementsByTagName("user");
		for (int temp = 0; temp < nList.getLength(); temp++) {
            Element eElement = (Element)nList.item(temp);
            String username = eElement.getElementsByTagName("username").item(0).getTextContent();
            User user = ApplicationController.getInstance().getUser(username);
            
            if(user == null) {
                user = new User(username, 
   					 		eElement.getElementsByTagName("email").item(0).getTextContent(), 
   					 		eElement.getElementsByTagName("password").item(0).getTextContent());
                ApplicationController.getInstance().getUsers().add(user);
            }
		}
	}
	
	public static void setOnlineUsers() throws Exception {
		String xml = DataBaseManager.GET("?cmd=get_online_users");
		org.w3c.dom.Document document = DataBaseManager.loadXMLFromString(xml);
		document.getDocumentElement().normalize();
		
		String log = "Online users:";
		NodeList nList = document.getElementsByTagName("user");
		for (int temp = 0; temp < nList.getLength(); temp++) {
            Element eElement = (Element) nList.item(temp);
            String username = eElement.getElementsByTagName("username").item(0).getTextContent();
            log = log.concat(" " + username);
            OnlineUser onlineUser = ApplicationController.getInstance().getOnlineUser(username);
            
            
            if(onlineUser == null) {
            	 onlineUser = new OnlineUser(username,
    					 		eElement.getElementsByTagName("ip").item(0).getTextContent(),
    					 		eElement.getElementsByTagName("port").item(0).getTextContent());
                ApplicationController.getInstance().getOnlineUsers().add(onlineUser);
            }
		}
		
		logger.debug(log);
	}
	
	public static void setGroups() throws Exception {
		String xml = DataBaseManager.GET("?cmd=get_groups");
		org.w3c.dom.Document document = DataBaseManager.loadXMLFromString(xml);
		document.getDocumentElement().normalize();

		String log = "Groups are:";
		NodeList nList = document.getElementsByTagName("group");
		for (int temp = 0; temp < nList.getLength(); temp++) {
            Element eElement = (Element) nList.item(temp);
            String groupname = eElement.getElementsByTagName("groupname").item(0).getTextContent();
            String user = eElement.getElementsByTagName("user").item(0).getTextContent();
            String color = eElement.getElementsByTagName("color").item(0).getTextContent();
            
            log = log.concat(" " + groupname + " " + user + " " + color);
            
            Group group = ApplicationController.getInstance().getGroup(groupname);
            if(group == null) {
            	group = new Group(groupname);
            	ApplicationController.getInstance().getGroups().add(group);
            }
            
            OnlineUser onlineUser = ApplicationController.getInstance().getOnlineUser(user);
            onlineUser.getGroups().add(group);
            group.setOnlineUser(color, onlineUser);
		}
		logger.debug(log);
	}

	public static void addUserToDataBase(String username, String email, String password) {
		String cmd = "?cmd=add_user&username=".concat(username).concat("&email=").concat(email).concat("&password=").concat(password);
		try {
			String xml = DataBaseManager.GET(cmd);
		} catch (IOException e) {
			logger.error(e);
		}
		ApplicationController.getInstance().getUsers().add(new User(username, email, password));
		DataBaseManager.addOnlineUserToDataBase(username);
	}
	
	public static void addOnlineUserToDataBase(String username) {
		String cmd = "?cmd=add_online_user&username=".concat(username);
		try {
			String xml = DataBaseManager.GET(cmd);
			org.w3c.dom.Document document = DataBaseManager.loadXMLFromString(xml);
			document.getDocumentElement().normalize();
			Element eElement = (Element) document.getElementsByTagName("response").item(0);
            String ip = eElement.getElementsByTagName("ip").item(0).getTextContent();
            String port = eElement.getElementsByTagName("port").item(0).getTextContent();
            
			ApplicationController.getInstance().getOnlineUsers().add(new OnlineUser(username, ip, port));
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	public static void removeOnlineUserToDataBase(OnlineUser onlineUser) {
		String cmd = "?cmd=remove_online_user&username=".concat(onlineUser.getUsername());
		try {
			String xml = DataBaseManager.GET(cmd);
			org.w3c.dom.Document document = DataBaseManager.loadXMLFromString(xml);
			document.getDocumentElement().normalize();
			Element eElement = (Element) document.getElementsByTagName("response").item(0);
            //String ip = eElement.getElementsByTagName("ip").item(0).getTextContent();
            //String port = eElement.getElementsByTagName("port").item(0).getTextContent();
            
			ApplicationController.getInstance().getOnlineUsers().remove(onlineUser);
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	public static void addGroupToDataBase(String groupname, String user, String color) {
		String cmd = "?cmd=add_group&groupname=".concat(groupname).concat("&user=").concat(user).concat("&color=").concat(color);
		try {
			String xml = DataBaseManager.GET(cmd);
			org.w3c.dom.Document document = DataBaseManager.loadXMLFromString(xml);
			document.getDocumentElement().normalize();
			Element eElement = (Element) document.getElementsByTagName("response").item(0);
            //String ip = eElement.getElementsByTagName("ip").item(0).getTextContent();
            //String port = eElement.getElementsByTagName("port").item(0).getTextContent();
           
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	public static void removeUserFromGroup(String username, String groupname) {
		String cmd = "?cmd=remove_user_from_group&username=".concat(username).concat("&groupname=").concat(groupname);
		try {
			String xml = DataBaseManager.GET(cmd);
			
			org.w3c.dom.Document document = DataBaseManager.loadXMLFromString(xml);
			document.getDocumentElement().normalize();
			Element eElement = (Element) document.getElementsByTagName("response").item(0);
            //String ip = eElement.getElementsByTagName("ip").item(0).getTextContent();
            //String port = eElement.getElementsByTagName("port").item(0).getTextContent();
           
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	public static void removeUserFromGroups(String username) {
		String cmd = "?cmd=remove_user_from_groups&username=".concat(username);
		try {
			String xml = DataBaseManager.GET(cmd);
			org.w3c.dom.Document document = DataBaseManager.loadXMLFromString(xml);
			document.getDocumentElement().normalize();
			Element eElement = (Element) document.getElementsByTagName("response").item(0);
            //String ip = eElement.getElementsByTagName("ip").item(0).getTextContent();
            //String port = eElement.getElementsByTagName("port").item(0).getTextContent();
            
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
