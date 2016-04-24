package Models;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

import Controllers.Constants;


public class MyCanvas extends JPanel {

	private static final long serialVersionUID = 1L;
	private Color color;
	private String figure;
	
	private ArrayList<Shape> shapes;

	public MyCanvas() {
		figure = new String();
		this.setBackground(Color.WHITE);
		this.setOpaque(true);
		shapes = new ArrayList<Shape>();
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
	
	public Color getColor() {
		return this.color;
	}
	
	public void setShapes(ArrayList<Shape> shapes) {
		this.shapes = shapes;
	}
	
	public ArrayList<Shape> getShapes() {
		return this.shapes;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		for(Shape shape : this.shapes) {
			g.setColor(shape.getColor());
			String figure = shape.getForm();
			int x = shape.getX();
			int y = shape.getY();

			System.out.println("Shape " + figure + x + y);
			
			if(figure.equals(Constants.SQUARE)) {
				g.drawRect(x, y, Constants.FIGURE_SIZE, Constants.FIGURE_SIZE);
			} else if(figure.equals(Constants.CIRCLE)) {
				g.drawOval(x, y, Constants.FIGURE_SIZE, Constants.FIGURE_SIZE);
			} else if(figure.equals(Constants.LINE)) {
				g.drawLine(x, y, (x + Constants.FIGURE_SIZE), (y + Constants.FIGURE_SIZE));
			} else if(figure.equals(Constants.ARROW)) {
				g.drawLine(x, y, (x + Constants.FIGURE_SIZE), (y + Constants.FIGURE_SIZE));
				g.drawLine(x, y, x - 3, y + 15);
				g.drawLine(x, y, x + 22, y + 1);
			}
		}
	}
	
	public void drawFigure(int x, int y) {
		this.shapes.add(new Shape(this.figure, this.color, x, y));
		repaint();
	}
}
