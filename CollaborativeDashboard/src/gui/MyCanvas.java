package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import Controllers.Constants;


public class MyCanvas extends Canvas {

	private static final long serialVersionUID = 1L;
	private Color color;
	
	private String figure;
	
	public MyCanvas() {
		figure = new String();
	}
	
	public void setFigure(String figure) {
		this.figure = figure;
	}
	
	public String getFigure() {
		return this.figure;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void drawFigure(int x, int y) {
		
		Graphics g = this.getGraphics();
		g.setColor(color);
		if(this.figure.equals(Constants.SQUARE)) {
			g.drawRect(x, y, Constants.FIGURE_SIZE, Constants.FIGURE_SIZE);
		} else if(this.figure.equals(Constants.CIRCLE)) {
			g.drawOval(x, y, Constants.FIGURE_SIZE, Constants.FIGURE_SIZE);
		} else if(this.figure.equals(Constants.LINE)) {
			g.drawLine(x, y, (x + Constants.FIGURE_SIZE), (y + Constants.FIGURE_SIZE));
		} else if(this.figure.equals(Constants.ARROW)) {
			g.drawLine(x, y, (x + Constants.FIGURE_SIZE), (y + Constants.FIGURE_SIZE));
			g.drawLine(x, y, x - 3, y + 15);
			g.drawLine(x, y, x + 22, y + 1);
		}
	}
}
