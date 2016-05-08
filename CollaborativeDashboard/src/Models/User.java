package Models;

public class User {
	
	private String username;
	private String email;
	private String password;
	
	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
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
	
	/*public ArrayList<Group> getGroups() {
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
	}*/
	
}
