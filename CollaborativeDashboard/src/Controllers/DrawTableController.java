package Controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import Models.CanvasInfo;
import Models.Group;
import Models.MyCanvas;
import NIO.MessageHandler;
import NIO.NetworkManager;
import gui.GUIHelper;

public class DrawTableController extends MouseAdapter implements ActionListener {
	
	private MyCanvas canvas;
	private DashboardController controller;
	
	public DrawTableController(MyCanvas canvas, DashboardController controller) {
		this.canvas = canvas;
		this.controller = controller;
    }
	
	public MyCanvas getCanvas() {
		return this.canvas;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(Constants.SQUARE)) {
			canvas.setFigure(Constants.SQUARE);
		} else if(e.getActionCommand().equals(Constants.CIRCLE)) {
			canvas.setFigure(Constants.CIRCLE);
		} else if(e.getActionCommand().equals(Constants.LINE)) {
			canvas.setFigure(Constants.LINE);
		} else if(e.getActionCommand().equals(Constants.ARROW)) {
			canvas.setFigure(Constants.ARROW);
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(!canvas.getFigure().isEmpty()) {
			canvas.drawFigure(e.getX(), e.getY());
			
			CanvasInfo info = this.controller.getCanvasInfo(canvas);
			Group group = ApplicationController.getInstance().getGroup(info.getGroupName());
			//info.saveShape(canvas.getFigure(), ControlUtil.getStringColor(canvas.getColor()), e.getX(), e.getY());
			
			
			
			NetworkManager.getInstance().notifyAllUsersOfGroup(MessageHandler.getSendEventMessage
					(Constants.ADD_SHAPE_EVENT, info.getGroupName(), canvas.getFigure(),
					ControlUtil.getStringColor(canvas.getColor()), String.valueOf(e.getX()), 
					String.valueOf(e.getY())), group);
			
			canvas.setFigure(new String());
		}
	}
}
