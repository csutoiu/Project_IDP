package Controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import gui.MyCanvas;

public class DrawTableController extends MouseAdapter implements ActionListener {
	
	private MyCanvas canvas;
	
	public DrawTableController(MyCanvas canvas) {
		this.canvas = canvas;
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
		System.out.println("Click on canvas");
		if(!canvas.getFigure().isEmpty()) {
			canvas.drawFigure(e.getX(), e.getY());
		}
	}
}
