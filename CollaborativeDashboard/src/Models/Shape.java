package Models;

import java.awt.Color;

public class Shape {
	
	private String form;
	private Color color;
	private int x;
	private int y;
	
	public Shape(String form, Color color, int x, int y) {
		this.setForm(form);
		this.setColor(color);
		this.setX(x);
		this.setY(y);
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	
}
