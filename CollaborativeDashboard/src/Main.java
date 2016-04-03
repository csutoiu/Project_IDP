import java.awt.EventQueue;

import Controllers.ApplicationController;
import gui.LaunchFrame;

public class Main {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LaunchFrame window = new LaunchFrame();
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		});
		
		@SuppressWarnings("unused")
		ApplicationController loadApplication = ApplicationController.getInstance();
	}

}
