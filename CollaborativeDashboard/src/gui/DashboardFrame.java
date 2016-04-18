package gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import Controllers.Constants;
import Controllers.ControlUtil;
import Controllers.DashboardController;
import Controllers.PopClickListener;
import Controllers.TabbedPaneController;
import Models.OnlineUser;
import Controllers.DrawTableController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DashboardFrame {
	
	private DashboardController controller = DashboardController.getInstance();
	
	private JFrame frame;
	private JLabel username;
	
	private JButton logoutBtn, createGroupBtn, saveWorkBtn, sendBtn;
	private JList<Object> onlineUsers;
	private JTree groups;
	
	private ArrayList<JLabel> userLabels;
	
	private JProgressBar progressBar;
	
	private JTabbedPane tabbedPane;
	
	private JComboBox<Integer> fontList;
	private JComboBox<String> colorList;
	private Integer[] numbers = {12, 13, 14, 15, 16, 17, 18};
	private String[] colors = {"red", "blue", "yellow", "pink", "green", "orange", "magenta", "gray", "black"};
	private JTextArea userText;
	private JTextPane chatText;

	Image newImg;
	GUIHelper guiHelper = new GUIHelper();
	
	/**
	 * Create the frame.
	 */
	public DashboardFrame() {
		initialize();
		this.controller.setView(this);
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
	
	public void setUsername(String username) {
		this.username.setText(username);
		this.controller.setInfoUser(username);
	}
	
	public String getUsername() {
		return username.getText();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(0, 0, 1180, 720);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Logged as:");
		lblNewLabel.setForeground(new Color(0, 102, 204));
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 16 ));
		lblNewLabel.setBounds(20, 10, 100, 24);
		frame.getContentPane().add(lblNewLabel);
		
		username = new JLabel("", SwingConstants.LEFT);
		username.setBounds(120, 10, 100, 24);
		username.setFont(new Font("Dialog", Font.BOLD, 16));
		frame.getContentPane().add(username);
		
		logoutBtn = new JButton("Logout");
		logoutBtn.setFont(new Font("Dialog", Font.PLAIN, 20));
		logoutBtn.setActionCommand(Constants.LOGOUT);
		logoutBtn.addActionListener(controller);
		logoutBtn.setBounds(1040, 10, 120, 40);
		frame.getContentPane().add(logoutBtn);
		
		createGroupBtn = new JButton("Create Group");
		createGroupBtn.setBounds(20, 45, 140, 26);
		createGroupBtn.setActionCommand(Constants.CREATE_GROUP);
		createGroupBtn.addActionListener(controller);
		frame.getContentPane().add(createGroupBtn);
		
		saveWorkBtn = new JButton(Constants.SAVE_WORK);
		saveWorkBtn.setBounds(180, 45, 180, 26);
		saveWorkBtn.setActionCommand(Constants.SAVE_WORK);
		saveWorkBtn.addActionListener(controller);
		frame.getContentPane().add(saveWorkBtn);
		
		progressBar = new JProgressBar(1,100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setForeground(Color.blue);   
		progressBar.setBackground(Color.white); 
		progressBar.setBounds(400, 45, 200, 26);
		progressBar.setVisible(false);
		frame.getContentPane().add(progressBar);
		
		JLabel lblNewLabel_1 = new JLabel("Online Users");
		lblNewLabel_1.setBounds(20, 100, 120, 15);
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 16));
		lblNewLabel_1.setForeground(new Color(0, 102, 204));
		frame.getContentPane().add(lblNewLabel_1);
		
		JScrollPane scroll = new JScrollPane();
		scroll.setBounds(20, 130, 200, 220);
		onlineUsers = new JList<Object>(controller.getOnlineUsers());
		onlineUsers.setVisibleRowCount(6);
		scroll.setViewportView(onlineUsers);
		frame.getContentPane().add(scroll);
		
		JLabel lblNewLabel_2 = new JLabel("Groups");
		lblNewLabel_2.setBounds(20, 380, 70, 15);
		lblNewLabel_2.setFont(new Font("Dialog", Font.BOLD, 16));
		lblNewLabel_2.setForeground(new Color(0, 102, 204));
		frame.getContentPane().add(lblNewLabel_2);
		
		groups = new JTree();
	    scroll = new JScrollPane();
		scroll.setBounds(20, 410, 200, 260);
		groups.setVisibleRowCount(6);
		scroll.setViewportView(groups);
		this.initializeGroups();
		
		groups.addMouseListener(new PopClickListener(groups, controller));
		frame.getContentPane().add(scroll);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(300, 130, 700, 330);
		tabbedPane.addChangeListener(new TabbedPaneController(tabbedPane, this.controller));
		frame.getContentPane().add(tabbedPane);
		
		JLabel legendLabel = new JLabel("Legend");
		legendLabel.setBounds(1050, 140, 70, 30);
		legendLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		legendLabel.setForeground(new Color(0, 102, 204));
		frame.getContentPane().add(legendLabel);
		
		this.makeUserLabels();
	
		chatText = new JTextPane();
		JScrollPane editorScrollPane = new JScrollPane(chatText);
		editorScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editorScrollPane.setBounds(300, 480, 600, 100);
		chatText.setEditable(false);
		frame.getContentPane().add(editorScrollPane);
		
		JButton fontBtn = new JButton("Font");
		fontBtn.setHorizontalAlignment(SwingConstants.CENTER);
		fontBtn.setBounds(300, 595, 70, 24);
		frame.getContentPane().add(fontBtn);
		
		fontList = new JComboBox<Integer>(numbers);
		fontList.setSelectedIndex(0);
		fontList.setBounds(390, 595, 50, 24);
		fontList.setActionCommand(Constants.CHANGE_FONT);
		fontList.addActionListener(controller);
		frame.getContentPane().add(fontList);
		
		JButton colorBtn = new JButton("Color");
		colorBtn.setHorizontalAlignment(SwingConstants.CENTER);
		colorBtn.setBounds(460, 595, 80, 24);
		frame.getContentPane().add(colorBtn);
		
		colorList = new JComboBox<String>(colors);
		colorList.setSelectedIndex(0);
		colorList.setBounds(560, 595, 100, 24);
		colorList.setActionCommand(Constants.CHANGE_COLOR);
		colorList.addActionListener(controller);
		frame.getContentPane().add(colorList);
		
		userText = new JTextArea();
		userText.setBounds(300, 630, 600, 40);
		frame.getContentPane().add(userText);
		
		sendBtn = new JButton(Constants.SEND);
		sendBtn.setBounds(940, 630, 70, 40);
		sendBtn.setActionCommand(Constants.SEND);
		sendBtn.addActionListener(controller);
		frame.getContentPane().add(sendBtn);
	}
	
	public void makeUserLabels() {
		userLabels = new ArrayList<JLabel>();
		int y = 180;
		for(int i = 0;i < 10;i++) {
			JLabel label = new JLabel("");
			label.setBounds(1045, y, 80, 20);
			label.setBackground(new Color(132, 136, 233));
			label.setOpaque(true);
			label.setVisible(false);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			userLabels.add(label);
			frame.getContentPane().add(label);
			y += 35;
		}
	}
	
	public void updateOnlineUsersList() {
		System.out.println("Updata online list\n");
		DefaultListModel<Object> listModel = new DefaultListModel<Object>();
		for(String str : this.controller.getOnlineUsers()) {
			listModel.addElement(str);
		}

		onlineUsers.setModel(listModel);
	}
	
	/* ########################################### */
	/* JTree methods */
	public void initializeGroups() {
		groups.setRootVisible( false );
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("root node, should be invisible");
		DefaultTreeModel defaultTreeModel = new DefaultTreeModel(rootNode);
		groups.setModel(defaultTreeModel);
		
		DefaultMutableTreeNode parentNode; 
		DefaultMutableTreeNode node;
		
		String[] groups = controller.getGroups();
		for(int i = 0;i < groups.length;i++) {
			parentNode = (DefaultMutableTreeNode)defaultTreeModel.getRoot();
			node = new DefaultMutableTreeNode(groups[i]);
			
			addNodeToDefaultTreeModel(defaultTreeModel, parentNode, node);
			parentNode = node;
			String[] users = controller.getUsersOfGroup(groups[i]);
			for(int j = 0;j < users.length;j++) {
				node = new DefaultMutableTreeNode(users[j]);
				addNodeToDefaultTreeModel(defaultTreeModel, parentNode, node);
			}
		}
	}
	
	private static void addNodeToDefaultTreeModel(DefaultTreeModel treeModel, DefaultMutableTreeNode parentNode, DefaultMutableTreeNode node) {
		treeModel.insertNodeInto(node, parentNode, parentNode.getChildCount());
		if(parentNode == treeModel.getRoot()) {
			treeModel.nodeStructureChanged((TreeNode) treeModel.getRoot());
		}
	}
	
	public void addNewGroup(String groupName) {
		DefaultTreeModel defaultTreeModel = (DefaultTreeModel) groups.getModel();
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)defaultTreeModel.getRoot();
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(groupName);
		addNodeToDefaultTreeModel(defaultTreeModel, parentNode, node);
		String[] users = controller.getUsersOfGroup(groupName);
		parentNode = node;
		node = new DefaultMutableTreeNode(users[0]);
		addNodeToDefaultTreeModel(defaultTreeModel, parentNode, node);
	}
	
	public void addNewUserToGroup(String user, String groupName) {
		
		DefaultTreeModel defaultTreeModel = (DefaultTreeModel) groups.getModel();
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)defaultTreeModel.getRoot();
		DefaultMutableTreeNode node;
		
		for(int i = 0;i < groups.getModel().getChildCount(parentNode);i++) {
			node = (DefaultMutableTreeNode) groups.getModel().getChild(parentNode, i);
			if(node.getUserObject().toString().equals(groupName)) {
				parentNode = node;
				break;
			}
		}
		node = new DefaultMutableTreeNode(user);
		addNodeToDefaultTreeModel(defaultTreeModel, parentNode, node);
	}
	
	public void removeUserToGroup(String user, String groupName) {
		
		DefaultTreeModel defaultTreeModel = (DefaultTreeModel) groups.getModel();
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)defaultTreeModel.getRoot();
		DefaultMutableTreeNode node;
		
		for(int i = 0;i < groups.getModel().getChildCount(parentNode);i++) {
			node = (DefaultMutableTreeNode) groups.getModel().getChild(parentNode, i);
			if(node.getUserObject().toString().equals(groupName)) {
				parentNode = node;
				break;
			}
		}
		for(int i = 0;i < groups.getModel().getChildCount(parentNode);i++) {
			node = (DefaultMutableTreeNode) groups.getModel().getChild(parentNode, i);
			if(node.getUserObject().toString().equals(user)) {
				defaultTreeModel.removeNodeFromParent(node);
			}
		}
	}
	
	public void deleteGroup(String groupName) {
		DefaultTreeModel defaultTreeModel = (DefaultTreeModel) groups.getModel();
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)defaultTreeModel.getRoot();
		DefaultMutableTreeNode node;
		
		for(int i = 0;i < groups.getModel().getChildCount(parentNode);i++) {
			node = (DefaultMutableTreeNode) groups.getModel().getChild(parentNode, i);
			if(node.getUserObject().toString().equals(groupName)) {
				defaultTreeModel.removeNodeFromParent(node);
				break;
			}
		}
	}
	/* ################################################# */
	
	/* JTabbedPane methods */
	public void insertNewTab(String groupName) {
		JComponent panel = this.makeDrawTable(groupName);
		tabbedPane.addTab(groupName, panel);
		for(int i = 0; i < tabbedPane.getTabCount();i++) {
			if(groupName.equals(tabbedPane.getTitleAt(i))) {
				tabbedPane.setSelectedIndex(i);
			}
		}
	}
	
	public void removeGroupTab(String groupName) {
		int count = tabbedPane.getTabCount();
		for(int i = 0; i < count;i++) {
			if(groupName.equals(tabbedPane.getTitleAt(i))) {
				tabbedPane.remove(i);
			}
		}
	}
	
	public JComponent makeDrawTable(String groupName) {
		
		JPanel panel = new JPanel(false);
		panel.setLayout(null);
		
		JButton square = new JButton("");
		square.setBounds(10, 10, 25, 25);
	    newImg = guiHelper.getImage("/resources/square.png").getScaledInstance(22, 22, java.awt.Image.SCALE_SMOOTH) ;  
	    square.setIcon(new ImageIcon(newImg));
		square.setActionCommand(Constants.SQUARE);

	    JButton circle = new JButton("");
	    circle.setBounds(45, 10, 25, 25);
	    newImg = guiHelper.getImage("/resources/circle.png").getScaledInstance(23, 23, java.awt.Image.SCALE_SMOOTH) ;  
	    circle.setIcon(new ImageIcon(newImg));
	    circle.setActionCommand(Constants.CIRCLE);
	    
	    JButton line = new JButton("");
	    line.setBounds(80, 10, 25, 25);
	    newImg = guiHelper.getImage("/resources/line.png").getScaledInstance(23, 23, java.awt.Image.SCALE_SMOOTH) ;  
	    line.setIcon(new ImageIcon(newImg));
	    line.setActionCommand(Constants.LINE);
	    
	    JButton arrow = new JButton("");
	    arrow.setBounds(115, 10, 25, 25);
	    newImg = guiHelper.getImage("/resources/arrow.png").getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH) ;  
	    arrow.setIcon(new ImageIcon(newImg));
	    arrow.setActionCommand(Constants.ARROW);
	    
        MyCanvas canvas = new MyCanvas();
        canvas.setBounds(0, 40, 900, 300);
        canvas.setBackground(Color.white);
        
        DrawTableController controller = new DrawTableController(canvas);
        square.addActionListener(controller);
        circle.addActionListener(controller);
        line.addActionListener(controller);
        arrow.addActionListener(controller);
        canvas.addMouseListener(controller);
        
        panel.add(square);
        panel.add(circle);
        panel.add(line);
        panel.add(arrow);
        
        panel.add(canvas);
     
        this.controller.getCanvasOfGroups().put(groupName, canvas);
        return panel;
	}
	/* ############################################## */
	
	/* ############################################## */
	/* Legend methods */
	public void clearLegend() {
		for(int i = 0; i < this.userLabels.size();i++) {
			this.userLabels.get(i).setVisible(false);
		}
	}
	
	public void updateLegend(HashMap<String, String> users) {
		int i = 0;
		for (Iterator<Map.Entry<String, String>> it = users.entrySet().iterator(); it.hasNext();) {
			 Map.Entry<String, String> e = it.next();
			 this.userLabels.get(i).setText(e.getValue());
			 this.userLabels.get(i).setBackground(ControlUtil.getNewColor(e.getKey()));
			 this.userLabels.get(i).setVisible(true);
			 i++;
		}
		for(;i < this.userLabels.size();i++) {
			this.userLabels.get(i).setVisible(false);
		}
	}
	/* ########################################## */
	
	/* ########################################## */
	/* Progress Bar methods */
	public void showProgressBar() {
		progressBar.setVisible(true);
		int i = 0;
		try {
			while(i <= 100) {
				Thread.sleep(30);
				progressBar.paintImmediately(0, 0, 200, 25);
				progressBar.setValue(i);
				i++;
			}
		} catch (Exception e) {
		}
		progressBar.setVisible(false);
	}
	/* ############################################ */
	
	/* ############################################ */
	/* Text Area Methods */
	public void changeFont() {
		Integer size = numbers[fontList.getSelectedIndex()];
		Font oldFont = userText.getFont();
		userText.setFont(new Font(oldFont.getName(), oldFont.getStyle(), size.intValue()));
	}
	
	public void changeColor() {
		String color = colors[colorList.getSelectedIndex()];
		userText.setForeground(ControlUtil.getNewColor(color));
	}
	
	public void addTextToChat() {
		this.appendToPane(chatText, this.userText.getText(), this.userText.getForeground(), this.userText.getFont().getSize());
	}
	
	private void appendToPane(JTextPane textPane, String msg, Color c, int size) {
		
        SimpleAttributeSet aset = new SimpleAttributeSet();
        aset.addAttribute(StyleConstants.FontFamily, "Dialog");
        aset.addAttribute(StyleConstants.Size, size);
        aset.addAttribute(StyleConstants.Foreground, c);
        
        StyledDocument doc = textPane.getStyledDocument();
        String string = this.username.getText().concat(": ").concat(msg).concat("\n");
        try {
            doc.insertString(doc.getLength(), string, aset );
        } catch(Exception e) { 
        	System.out.println(e); 
        }
       
    }
}
