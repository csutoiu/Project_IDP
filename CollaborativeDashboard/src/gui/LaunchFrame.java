package gui;

import java.awt.*;
import javax.swing.*;

import Controllers.Constants;
import Controllers.SignInController;

public class LaunchFrame {
	
	private SignInController controller = SignInController.getInstance();

	private JFrame frame;
	private JLabel imageBkg, errorLabel;
	
	private JTextField usernameTxtField;
	private JTextField passwordTxtField;
	
	private JButton signInBtn, signUpBtn;
	
	private GUIHelper guiHelper = new GUIHelper();
	Image newImg;
	
	/**
	 * Create the application.
	 */
	public LaunchFrame() {
		initialize();
		this.controller.setView(this);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(0, 100, 390, 460);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		imageBkg = new JLabel(new ImageIcon(guiHelper.getImage("/resources/logo1.png")));
		imageBkg.setHorizontalAlignment(SwingConstants.LEFT);
		imageBkg.setBounds(0, 0, frame.getWidth(), 135);
		frame.getContentPane().add(imageBkg);
		
		JLabel username = new JLabel("Username");
		username.setBounds(30, 160, 80, 24);
		username.setForeground(new Color(0, 102, 204));
		frame.getContentPane().add(username);
		
		JLabel password = new JLabel("Password");
		password.setBounds(30, 198, 80, 24);
		password.setForeground(new Color(0, 102, 204));
		frame.getContentPane().add(password);
		
		usernameTxtField = new JTextField();
		usernameTxtField.setColumns(10);
		usernameTxtField.setBounds(140, 160, 220, 24);
		usernameTxtField.getDocument().addDocumentListener(controller);
		frame.getContentPane().add(usernameTxtField);
		
		passwordTxtField = new JTextField();
		passwordTxtField.setBounds(140, 198, 220, 24);
		passwordTxtField.setColumns(10);
		passwordTxtField.getDocument().addDocumentListener(controller);
		frame.getContentPane().add(passwordTxtField);
		
		signInBtn = new JButton();
		signInBtn.setActionCommand(Constants.SIGN_IN);
		signInBtn.addActionListener(controller);
		signInBtn.setForeground(Color.WHITE);
		signInBtn.setBounds(105, 260, 180, 40);
		newImg = guiHelper.getImage("/resources/login.jpg").getScaledInstance(180, 40, java.awt.Image.SCALE_SMOOTH) ;  
		signInBtn.setIcon(new ImageIcon(newImg));
		frame.getContentPane().add(signInBtn);
		
		errorLabel = new JLabel("");
		errorLabel.setBounds(40, 320, 320, 20);
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setForeground(Color.red);
		errorLabel.setVisible(false);
		frame.getContentPane().add(errorLabel);
		
		JLabel noAccount = new JLabel("Don't have an account?");
		noAccount.setForeground(new Color(0, 102, 204));
		noAccount.setBounds(30, 362, 196, 15);
		frame.getContentPane().add(noAccount);
		
		signUpBtn = new JButton();
		signUpBtn.setActionCommand(Constants.SIGN_UP);
		signUpBtn.addActionListener(controller);
		signUpBtn.setBounds(30, 390, 100, 30);
		newImg = guiHelper.getImage("/resources/signup.png").getScaledInstance(100, 30, java.awt.Image.SCALE_SMOOTH) ;  
		signUpBtn.setIcon(new ImageIcon(newImg));
		frame.getContentPane().add(signUpBtn);
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
	
	public String getUsername() {
		return this.usernameTxtField.getText();
	}
	
	public String getPassword() {
		return this.passwordTxtField.getText();
	}
	
	public JLabel getErrorLabel() {
		return this.errorLabel;
	}
}

