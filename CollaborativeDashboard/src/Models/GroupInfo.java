package Models;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import Controllers.ControlUtil;

public class GroupInfo {

	private MyCanvas canvas;
	private String groupName;
	private String image;
	private String history;
	private ArrayList<UserText> chat;
	private static Logger logger = Logger.getLogger(GroupInfo.class);
	
	
	public GroupInfo (MyCanvas canvas, String groupName) {
		this.canvas = canvas;
		this.groupName = groupName;
		image = new String();
		history = new String();
		chat = new ArrayList<UserText>();
	}
	
	public String getGroupName() {
		return this.groupName;
	}
	
	public MyCanvas getCanvas() {
		return this.canvas;
	}
	
	public String getImage() {
		return this.image;
	}
	
	public String getHistory() {
		return this.history;
	}
	
	public ArrayList<UserText> getChat() {
		return this.chat;
	}
	
	
	public String getCanvasShapes() {
		if(this.canvas.getShapes().isEmpty()) {
			return "[]";
		}
		
		String canvasShapes = "[";
		
		for(Shape shape : this.canvas.getShapes()) {
			canvasShapes = canvasShapes.concat("{ \"form\" : \"").concat(shape.getForm()).
										concat("\", \"color\" : \"").concat(ControlUtil.getStringColor(shape.getColor())).
										concat("\", \"x\" : \"").concat(String.valueOf(shape.getX())).
										concat("\", \"y\" : \"").concat(String.valueOf(shape.getY())).
										concat("\" },");
		}
		String temp = canvasShapes.substring(0, canvasShapes.length() - 1);
		
		logger.info("Canvas shapes are " + temp.concat("]"));
		return temp.concat("]");
	}
	
	public String getHistoryChat() {
		if(this.chat.isEmpty()) {
			return "[]";
		}
		
		String chat = "[";
		
		for(UserText text : this.chat) {
			chat = chat.concat("{ \"text\" : \"").concat(text.getText()).
						concat("\", \"color\" : \"").concat(text.getColor()).
						concat("\", \"size\" : \"").concat(text.getSize()).
						concat("\" },");
		}
		String temp = chat.substring(0, chat.length() - 1);
		
		logger.info("Chat history is " + temp.concat("]"));
		return temp.concat("]");
	}
}
