package Models;
import java.awt.Color;
import java.util.ArrayList;

import Controllers.ControlUtil;

public class User {
	
	private String username;
	private String email;
	private String password;
	private Color color;
	private ArrayList<Group> groups;
	
	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
		groups = new ArrayList<Group>();
	}
	
	public User(String username, String email, String password, String color) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.color = ControlUtil.getNewColor(color);
		groups = new ArrayList<Group>();
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public ArrayList<Group> getGroups() {
		return this.groups;
	}
	
	public void joinToGroup(Group group) {
		this.groups.add(group);
		group.addUserToGroup(this);
	}
	
	public void leaveGroup(Group group) {
		if(this.groups.contains(group)) {
			this.groups.remove(group);
		}
	}
	
}
