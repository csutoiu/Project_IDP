package Models;
import java.util.ArrayList;

public class Group {

	private String groupName;
	private ArrayList<User> users;
	
	public Group(String groupName) {
		this.groupName = groupName;
		this.users = new ArrayList<User>();
	}
	
	public Group(String groupName, ArrayList<User> users) {
		this.groupName = groupName;
		this.users = users;
	}
	
	public String getGroupName() {
		return this.groupName;
	}
	
	public ArrayList<User> getUsers() {
		return this.users;
	}
	
	public void addUserToGroup(User user) {
		if(!this.users.contains(user)) {
			this.users.add(user);
		}
	}
}
