import java.awt.EventQueue;
import org.apache.log4j.*;

import Controllers.ApplicationController;
import Controllers.Constants;
import DataBase.DataBaseManager;
import NIO.MessageHandler;
import NIO.NetworkManager;
import gui.DashboardFrame;
import gui.LaunchFrame;

public class Main {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		final Logger logger = Logger.getLogger(Main.class);
		logger.info("Start applicatation");
		
		ApplicationController loadApplication = ApplicationController.getInstance();
		try {
			loadApplication.setInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(args.length == 0 || Integer.parseInt(args[0]) == 0) {
		
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						LaunchFrame window = new LaunchFrame();
						window.getFrame().setVisible(true);
					} catch (Exception e) {
						logger.error(e);
					}
				}
			});
			
		} else {
			final String username = args[1];
			
			NetworkManager.getInstance().notifyAllUsers(MessageHandler.getSendEventMessage(Constants.SIGN_UP_EVENT
					, username, username.concat("@idp.com"), "pass"));
			DataBaseManager.addUserToDataBase(username, username.concat("@idp.com"), "pass");
			
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						DashboardFrame window = new DashboardFrame();
						window.setUsername(username);
						window.getFrame().setVisible(true);
						
						logger.info("User " + username + " was logged in.");
					} catch (Exception e) {
						logger.error(e);
					}
				}
			});
		}
		
		

	}

}
