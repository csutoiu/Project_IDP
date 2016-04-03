package Controllers;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TabbedPaneController implements ChangeListener {
	
	private JTabbedPane tabbedPane;
	
	public TabbedPaneController(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		String groupName = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
		String[] users = DashboardController.getInstance().getUsersOfGroup(groupName);
		DashboardController.getInstance().getFrame().updateUserLabels(users);
		
	}

}
