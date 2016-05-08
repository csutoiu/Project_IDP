package Controllers;

import java.util.HashMap;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Models.GroupInfo;
import Models.Group;

public class TabbedPaneController implements ChangeListener {
	
	private JTabbedPane tabbedPane;
	DashboardController controller;
	
	public TabbedPaneController(JTabbedPane tabbedPane, DashboardController controller) {
		this.tabbedPane = tabbedPane;
		this.controller = controller;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		
		if(tabbedPane.getSelectedIndex() >= 0) {
			String groupName = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
			Group group = ApplicationController.getInstance().getGroup(groupName);
			HashMap<String, String> usersAndColors = this.controller.getUsersAndColors(group);
			this.controller.getFrame().updateLegend(usersAndColors);
			
			GroupInfo info = this.controller.getGroupInfo(groupName);
			this.controller.updateChatArea(info);
			this.controller.getFrame().getUserText().setText("");
			
		} else {
			this.controller.getFrame().clearLegend();
			this.controller.getFrame().getTextPane().setText("");
			this.controller.getFrame().getUserText().setText("");
		}
		
		controller.setSelectedIndexTab(tabbedPane.getSelectedIndex());
	}

}
