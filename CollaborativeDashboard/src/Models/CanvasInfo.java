package Models;

import java.awt.image.BufferedImage;
import java.util.ResourceBundle.Control;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Controllers.ControlUtil;

public class CanvasInfo {

	MyCanvas canvas;
	String groupName;
	String image;
	
	public CanvasInfo (MyCanvas canvas, String groupName) {
		this.canvas = canvas;
		this.groupName = groupName;
		image = new String();
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
		
		return temp.concat("]");
	}
	
	public void loadImage() {
		try {
			JSONObject json = new JSONObject(image);
			JSONArray jsonArray = json.getJSONArray("images");
			for (int i = 0; i < jsonArray.length(); i++) {
		        JSONObject jsonObject = jsonArray.getJSONObject(i);
		        System.out.println("Image " + jsonObject.toString());
		        
		        String figure = jsonObject.getString("figure");
				String color = jsonObject.getString("color");
				String x = jsonObject.getString("x");
				String y = jsonObject.getString("y");
				
				System.out.println("figure " + figure + " color " + color + " " + x + " " + y);
				
				//this.canvas.drawFigure(figure, ControlUtil.getNewColor(color), Integer.parseInt(x), Integer.parseInt(y));
		    }
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
