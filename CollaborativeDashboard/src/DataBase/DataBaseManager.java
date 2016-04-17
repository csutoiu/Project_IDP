package DataBase;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import Controllers.ApplicationController;
import Models.Group;
import Models.OnlineUser;
import Models.User;

public class DataBaseManager {
	
	private static String baseUrl = "http://idp.16mb.com/index.php";
	private static String USER_AGENT = "Mozilla/5.0";
	
	private static String GET(String additionalUrl) throws IOException {
		
		URL url = new URL(baseUrl.concat(additionalUrl));
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		// optional default is GET
		con.setRequestMethod("GET");
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
		
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
	
	private static org.w3c.dom.Document loadXMLFromString(String xml) throws Exception {
		
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

            User user = new User(eElement.getElementsByTagName("username").item(0).getTextContent(), 
            					 eElement.getElementsByTagName("email").item(0).getTextContent(), 
            					 eElement.getElementsByTagName("password").item(0).getTextContent());
            ApplicationController.getInstance().getUsers().add(user);
		}
	}
	
	public static void setOnlineUsers() throws Exception {
		String xml = DataBaseManager.GET("?cmd=get_online_users");
		org.w3c.dom.Document document = DataBaseManager.loadXMLFromString(xml);
		document.getDocumentElement().normalize();
		
		NodeList nList = document.getElementsByTagName("user");
		for (int temp = 0; temp < nList.getLength(); temp++) {
            Element eElement = (Element) nList.item(temp);
            OnlineUser onlineUser = new OnlineUser(eElement.getElementsByTagName("username").item(0).getTextContent(),
            					 eElement.getElementsByTagName("ip").item(0).getTextContent(),
            					 eElement.getElementsByTagName("port").item(0).getTextContent());
            ApplicationController.getInstance().getOnlineUsers().add(onlineUser);
		}
	}
	
	public static void setGroups() throws Exception {
		String xml = DataBaseManager.GET("?cmd=get_groups");
		System.out.println(xml);
		org.w3c.dom.Document document = DataBaseManager.loadXMLFromString(xml);
		document.getDocumentElement().normalize();

		NodeList nList = document.getElementsByTagName("group");
		for (int temp = 0; temp < nList.getLength(); temp++) {
            Element eElement = (Element) nList.item(temp);
            String groupname = eElement.getElementsByTagName("groupname").item(0).getTextContent();
            String user = eElement.getElementsByTagName("user").item(0).getTextContent();
            String color = eElement.getElementsByTagName("color").item(0).getTextContent();
            System.out.println(groupname + " " + user + " " + color);
            
            Group group = ApplicationController.getInstance().getGroup(groupname);
            if(group == null) {
            	group = new Group(groupname);
            	ApplicationController.getInstance().getGroups().add(group);
            }
            
            System.out.println(group.getGroupName());
            OnlineUser onlineUser = ApplicationController.getInstance().getOnlineUser(user);
            System.out.println(onlineUser.getUsername());
            onlineUser.getGroups().add(group);
            group.setOnlineUser(color, onlineUser);
		}
	}

	public static void addUserToDataBase(String username, String email, String password) {
		String cmd = "?cmd=add_user&username=".concat(username).concat("&email=").concat(email).concat("&password=").concat(password);
		try {
			String xml = DataBaseManager.GET(cmd);
		} catch (IOException e) {
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}
	
	public static void addNewUserToGroup(String username, String groupName) {
		
		File inputFile = new File("groups.txt");
		File tempFile = new File("groupsTemp.txt");

		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile));
		    writer = new BufferedWriter(new FileWriter(tempFile));
		    
			String currentLine;
		    while((currentLine = reader.readLine()) != null) {
		    	String[] fields = currentLine.split(" ");
			    if(fields[0].equals(groupName)) {
			    	String line = fields[0];
			    	for(int i = 1;i < fields.length;i++) {
			    		line = line.concat(" ").concat(fields[i]);
			    	}
			    	line = line.concat(" ").concat(username);
			    	currentLine = line;
			    }
			    writer.write(currentLine + System.getProperty("line.separator"));
			}
		    writer.close(); 
			reader.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}

		tempFile.renameTo(inputFile);
	}
	
	public static void removeUserToGroup(String username, String groupName) {
		
		File inputFile = new File("groups.txt");
		File tempFile = new File("groupsTemp.txt");

		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile));
		    writer = new BufferedWriter(new FileWriter(tempFile));
		    
			String currentLine;
		    while((currentLine = reader.readLine()) != null) {
		    	String[] fields = currentLine.split(" ");
			    if(fields[0].equals(groupName)) {
			    	String line = fields[0];
			    	for(int i = 1;i < fields.length;i++) {
			    		if(!fields[i].equals(username)) {
			    			line = line.concat(" ").concat(fields[i]);
			    		}
			    	}
			    	currentLine = line;
			    }
			    writer.write(currentLine + System.getProperty("line.separator"));
			}
		    writer.close(); 
			reader.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}

		tempFile.renameTo(inputFile);
	}
	
	public static void removeUserFromGroups(String username) {
		String cmd = "?cmd=remove_user_from_groups&username=".concat(username);
		try {
			String xml = DataBaseManager.GET(cmd);
			System.out.println(xml);
			org.w3c.dom.Document document = DataBaseManager.loadXMLFromString(xml);
			document.getDocumentElement().normalize();
			Element eElement = (Element) document.getElementsByTagName("response").item(0);
            //String ip = eElement.getElementsByTagName("ip").item(0).getTextContent();
            //String port = eElement.getElementsByTagName("port").item(0).getTextContent();
            
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteGroup(String groupName) {
		
		File inputFile = new File("groups.txt");
		File tempFile = new File("groupsTemp.txt");

		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile));
		    writer = new BufferedWriter(new FileWriter(tempFile));
		    
			String currentLine;
		    while((currentLine = reader.readLine()) != null) {
		    	String[] fields = currentLine.split(" ");
			    if(fields[0].equals(groupName)) continue;
			    writer.write(currentLine + System.getProperty("line.separator"));
			}
		    writer.close(); 
			reader.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}

		tempFile.renameTo(inputFile);
	}
}
