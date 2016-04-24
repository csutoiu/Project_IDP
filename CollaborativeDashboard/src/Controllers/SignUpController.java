package Controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import DataBase.DataBaseManager;
import Models.User;
import NIO.MessageHandler;
import NIO.NetworkManager;
import gui.CreateAccountFrame;
import gui.DashboardFrame;
import gui.LaunchFrame;

public class SignUpController implements ActionListener, DocumentListener  {

	private static SignUpController me;
	private ApplicationController application = ApplicationController.getInstance();
	private CreateAccountFrame view;
	
	private String username, email, password, confirmPass;
	
	private SignUpController() {
    }

    public static SignUpController getInstance() {
        if (me == null) {
            me = new SignUpController();
        }

        return me;
    }
    
    public void setView(CreateAccountFrame view) {
    	this.view = view;
    }
 
    /* Action Listener Methods */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(Constants.CANCEL)) {
			this.view.getFrame().setVisible(false);
			LaunchFrame launchFrame = new LaunchFrame();
			launchFrame.getFrame().setVisible(true);
		} else {
		
			username = view.getUsername();
			email = view.getEmail();
			password = view.getPassword();
			confirmPass = view.getConfirmPass();
		
			if(username.isEmpty() || email.isEmpty()|| password.isEmpty() || confirmPass.isEmpty()) {
				this.handlerForResult(Constants.EMPTY_FIELDS);
				return;
			} else {
				ArrayList<User> users = application.getUsers();
				for(int i = 0; i < users.size();i++) {
					if(users.get(i).getUsername().equals(username)) {
						this.handlerForResult(Constants.USERNAME_EXISTS);
						return;
					} else if(users.get(i).getEmail().equals(email)){ 
						this.handlerForResult(Constants.EMAIL_EXISTS);
						return;
					}
				}
				if(!ControlUtil.validate(email)) {
					this.handlerForResult(Constants.EMAIL_FORMAT);
					return;
				}
				if(!password.equals(confirmPass)) {
					this.handlerForResult(Constants.PASSWORDS);
					return;
				}
			}
		
			NetworkManager.getInstance().notifyAllUsers(MessageHandler.getSendEventMessage(Constants.SIGN_UP_EVENT
					, username, email, password));
			DataBaseManager.addUserToDataBase(username, email, password);
			this.handlerForResult(Constants.SUCCESS);
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
			this.view.getErrorLabel().setVisible(true);
			this.view.getErrorLabel().setText(result);
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
