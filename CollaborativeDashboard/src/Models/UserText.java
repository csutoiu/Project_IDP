package Models;

public class UserText {
	
	private String text;
	private String color;
	private String size;
	
	public UserText(String text, String color, String size) {
		this.text = text;
		this.color = color;
		this.size = size;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getSize() {
		return size;
	}
	
	public void setSize(String size) {
		this.size = size;
	}
	
	

}
