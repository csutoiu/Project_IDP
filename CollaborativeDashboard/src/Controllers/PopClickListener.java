package Controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class PopClickListener extends MouseAdapter {
	
	private DashboardController controller;
	private JTree tree;
	
	public PopClickListener(JTree tree, DashboardController controller) {
		this.tree = tree;
		this.controller = controller;
	}

	public void mousePressed(MouseEvent e){
        if (e.isPopupTrigger()) {
        	DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
        	if(tree.getModel().getChildCount(node) > 0) {
        		this.makePopMenu(e, node.getUserObject().toString());
        	}
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
        	DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
        	if(tree.getModel().getChildCount(node) > 0) {
        		this.makePopMenu(e, node.getUserObject().toString());
        	}
        }
    }

    private void makePopMenu(MouseEvent e, String group){
        PopMenu menu = new PopMenu(group);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
    
    @SuppressWarnings("serial")
	private class PopMenu extends JPopupMenu {
    	
    	public PopMenu(String group){
    		controller.setGroupChanged(group);
        	JMenuItem item = new JMenuItem(Constants.ADD_USER);
        	item.setActionCommand(Constants.ADD_USER);
        	item.addActionListener(controller);
            add(item);
            
            JMenuItem item1 = null;
            String[] users = controller.getUsersOfGroup(group);
            for(int i = 0; i < users.length; i++) {
            	if(controller.getCurrentUser().equals(users[i])) {
            		item1 = new JMenuItem(Constants.LEAVE_GROUP);
            		item1.setActionCommand(Constants.LEAVE_GROUP);
            		break;
            	}
            }
            if(item1 == null) {
            	item1 = new JMenuItem(Constants.JOIN_GROUP);
            	item1.setActionCommand(Constants.JOIN_GROUP);
            }
            item1.addActionListener(controller);
            add(item1);
    	}
    }
}
