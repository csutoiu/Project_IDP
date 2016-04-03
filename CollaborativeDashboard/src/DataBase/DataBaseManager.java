package DataBase;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DataBaseManager {

	public static void addUserToDataBase(String username, String email, String password) {
		PrintWriter writer = null;
		try {
			File f = new File("users.txt");
			if(f.exists()) {
				writer = new PrintWriter(new BufferedWriter(new FileWriter("users.txt", true))); 
			} else {
				writer = new PrintWriter("users.txt", "UTF-8");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		writer.println(username+" "+email+" "+password);
		writer.close();
	}
	
	public static void addOnlineUserToDataBase(String username, String password) {
		PrintWriter writer = null;
		try {
			File f = new File("onlineUsers.txt");
			if(f.exists()) {
				writer = new PrintWriter(new BufferedWriter(new FileWriter("onlineUsers.txt", true))); 
			} else {
				writer = new PrintWriter("onlineUsers.txt", "UTF-8");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		writer.println(username+" "+password);
		writer.close();
	}
	
	public static void removeOnlineUserToDataBase(String username) {
		
		File inputFile = new File("onlineUsers.txt");
		File tempFile = new File("onlineUsersTemp.txt");

		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile));
		    writer = new BufferedWriter(new FileWriter(tempFile));
		    
			String currentLine;
		    while((currentLine = reader.readLine()) != null) {
		    	String[] fields = currentLine.split(" ");
			    if(fields[0].equals(username)) continue;
			    writer.write(currentLine + System.getProperty("line.separator"));
			}
		    writer.close(); 
			reader.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}

		tempFile.renameTo(inputFile);
	}
	
	public static void addGroupToDataBase(String name, ArrayList<String> users) {
		PrintWriter writer = null;
		try {
			File f = new File("groups.txt");
			if(f.exists()) {
				writer = new PrintWriter(new BufferedWriter(new FileWriter("groups.txt", true))); 
			} else {
				writer = new PrintWriter("groups.txt", "UTF-8");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String names = users.get(0);
		for(int i = 1;i < users.size();i++) {
			names.concat(" ").concat(users.get(i));
		}
		writer.println(name+" "+names);
		writer.close();
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
