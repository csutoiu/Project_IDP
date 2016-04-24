package Controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import DataBase.DataBaseManager;
import Models.OnlineUser;
import Models.User;
import gui.CreateAccountFrame;
import gui.DashboardFrame;
import gui.LaunchFrame;

public class SignInController implements ActionListener, DocumentListener {

	private static SignInController me;
	private ApplicationController application = ApplicationController.getInstance();
	private LaunchFrame view;
	
	private String username, password;
	
	private SignInController() {
    }

    public static SignInController getInstance() {
        if (me == null) {
            me = new SignInController();
        }

        return me;
    }
    
    public void setView(LaunchFrame view) {
    	this.view = view;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		username = this.view.getUsername();
		password = this.view.getPassword();
		
		if(e.getActionCommand().equals(Constants.SIGN_IN)) {
			if(username.isEmpty() || password.isEmpty()) {
				return;
			}
			
			ArrayList<OnlineUser> onlineUsers = application.getOnlineUsers();
			for(int i = 0; i < onlineUsers.size();i++) {
				if(onlineUsers.get(i).getUsername().equals(username)) {
					this.handlerForResult(Constants.ERROR_LOGIN_ALREADY);
					return;
				}
			}
			
			User user = application.getUser(username);
			if(user != null) {
				if(user.getPassword().equals(password)) {
					DataBaseManager.addOnlineUserToDataBase(username);
					this.handlerForResult(Constants.SUCCESS);
					return;
				}
			}

			this.handlerForResult(Constants.ERROR_LOGIN);
			
		} else if(e.getActionCommand().equals(Constants.SIGN_UP)) {
			this.view.getFrame().setVisible(false);
			CreateAccountFrame createAccountFrame = new CreateAccountFrame();
			createAccountFrame.getFrame().setVisible(true);
		}
	}
	
	public void handlerForResult(String result) {
		if(result.equals(Constants.SUCCESS)) {
			this.view.getFrame().setVisible(false);
			
	    	try {
				application.setInfo();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			DashboardFrame dashboard = new DashboardFrame();
			dashboard.setUsername(username);
			dashboard.getFrame().setVisible(true);
		} else {
			this.view.getErrorLabel().setText(result);
			this.view.getErrorLabel().setVisible(true);
		}
	}
	

	/* Document Listener Methods */
	@Override
	public void changedUpdate(DocumentEvent e) {
		if(this.view.getErrorLabel().isVisible()) {
			this.view.getErrorLabel().setVisible(false);
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if(this.view.getErrorLabel().isVisible()) {
			this.view.getErrorLabel().setVisible(false);
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		if(this.view.getErrorLabel().isVisible()) {
			this.view.getErrorLabel().setVisible(false);
		}
	}
}
