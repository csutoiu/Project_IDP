package gui;
import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import Controllers.Constants;
import Controllers.SignUpController;

import javax.swing.JTextField;
import javax.swing.JButton;

public class CreateAccountFrame {

	private SignUpController controller = SignUpController.getInstance();
	
	private JFrame frame;
	private JLabel imageBkg, errorLabel;
	
	GUIHelper guiHelper = new GUIHelper();
	private JTextField username, email, password, confirmPass;
	private JButton signUpBtn, cancelBtn;
	
	/**
	 * Create the frame.
	 */
	public CreateAccountFrame() {
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
		
		Image newImg = guiHelper.getImage("/resources/logo2.jpg").getScaledInstance(390, 135, java.awt.Image.SCALE_SMOOTH);
		imageBkg = new JLabel(new ImageIcon(newImg));
		imageBkg.setHorizontalAlignment(SwingConstants.LEFT);
		imageBkg.setBounds(0, 0, frame.getWidth(), 135);
		frame.getContentPane().add(imageBkg);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setBounds(30, 160, 80, 24);
		lblNewLabel.setForeground(new Color(0, 102, 204));
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Email");
		lblNewLabel_1.setBounds(30, 198, 80, 24);
		lblNewLabel_1.setForeground(new Color(0, 102, 204));
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Password");
		lblNewLabel_2.setForeground(new Color(0, 102, 204));
		lblNewLabel_2.setBounds(30, 236, 80, 24);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Confirm Password");
		lblNewLabel_3.setForeground(new Color(0, 102, 204));
		lblNewLabel_3.setBounds(30, 274, 130, 24);
		frame.getContentPane().add(lblNewLabel_3);
		
		username = new JTextField();
		username.setBounds(180, 160, 180, 24);
		username.setColumns(10);
		username.getDocument().addDocumentListener(controller);
		frame.getContentPane().add(username);

		email = new JTextField();
		email.setColumns(10);
		email.setBounds(180, 198, 180, 24);
		email.getDocument().addDocumentListener(controller);
		frame.getContentPane().add(email);

		password = new JTextField();
		password.setColumns(10);
		password.setBounds(180, 236, 180, 24);
		password.getDocument().addDocumentListener(controller);
		frame.getContentPane().add(password);
		
		confirmPass = new JTextField();
		confirmPass.setBounds(180, 274, 180, 24);
		confirmPass.setColumns(10);
		confirmPass.getDocument().addDocumentListener(controller);
		frame.getContentPane().add(confirmPass);
		
		signUpBtn = new JButton("");
		signUpBtn.setBounds(145, 320, 100, 30);
		newImg = guiHelper.getImage("/resources/signup.png").getScaledInstance(100, 30, java.awt.Image.SCALE_SMOOTH) ;  
		signUpBtn.setIcon(new ImageIcon(newImg));
		signUpBtn.setActionCommand(Constants.SIGN_UP);
		signUpBtn.addActionListener(controller);
		frame.getContentPane().add(signUpBtn);
		
		errorLabel = new JLabel("");
		errorLabel.setBounds(40, 360, 300, 20);
		errorLabel.setForeground(Color.red);
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(errorLabel);
		
		cancelBtn = new JButton("Already have an account?");
		cancelBtn.setActionCommand(Constants.CANCEL);
		cancelBtn.addActionListener(controller);
		cancelBtn.setForeground(new Color(0, 102, 204));
		cancelBtn.setBounds(75, 390, 240, 30);
		cancelBtn.setOpaque(false);
		cancelBtn.setContentAreaFilled(false);
		cancelBtn.setBorderPainted(false);
		frame.getContentPane().add(cancelBtn);
		
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
	
	public String getUsername() {
		return this.username.getText();
	}
	
	public String getEmail() {
		return this.email.getText();
	}
	
	public String getPassword() {
		return this.password.getText();
	}
	
	public String getConfirmPass() {
		return this.confirmPass.getText();
	}
	
	public JLabel getErrorLabel() {
		return this.errorLabel;
	}
}
