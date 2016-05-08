import static org.junit.Assert.*;

import org.junit.Test;

import Controllers.ApplicationController;

public class TestDataBase {

	static ApplicationController application = ApplicationController.getInstance();
	
	@Test
	public void testAddUser() {
		assertEquals(false, application.getUsers().isEmpty());
	}
	
	@Test
	public void testAddOnlineUser() {
		assertEquals(false, application.getOnlineUsers().isEmpty());
	}

}
