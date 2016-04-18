package Models;

import java.util.ArrayList;
import java.util.HashMap;

public class OnlineUser {

	String username;
	String ip;
	String port;
	ArrayList<Group> groups;
	
	public OnlineUser(String username, String ip, String port) {
		this.username = username;
		this.ip = ip;
		this.port = port;
		groups = new ArrayList<Group>();
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getIp() {
		return this.ip;
	}
	
	public String getPort() {
		return this.port;
	}
	
	public ArrayList<Group> getGroups() {
		return this.groups;
	}
}
