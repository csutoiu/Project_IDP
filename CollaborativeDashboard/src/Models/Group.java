package Models;
import java.util.HashMap;

public class Group {

	private String groupName;
	private HashMap<String, OnlineUser> users;
	
	public Group(String groupName) {
		this.groupName = groupName;
		this.users = new HashMap<String, OnlineUser>();
	}
	
	public Group(String groupName, HashMap<String, OnlineUser> users) {
		this.groupName = groupName;
		this.users = users;
	}
	
	public String getGroupName() {
		return this.groupName;
	}
	
	public HashMap<String, OnlineUser> getUsers() {
		return this.users;
	}
	
	public Boolean setOnlineUser(String color, OnlineUser user) {
		OnlineUser check_user = this.users.get(color);
		if(check_user != null)
			return false;
		this.users.put(color, user);
		return true;
	}
}
