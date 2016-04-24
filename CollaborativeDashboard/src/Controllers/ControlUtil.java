package Controllers;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControlUtil {
	
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	
	public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
	}
	
	public static Color getNewColor(String color) {
		if(color.equals("red"))
			return Color.red;
		if(color.equals("blue"))
			return Color.blue;
		if(color.equals("pink"))
			return Color.pink;
		if(color.equals("yellow"))
			return Color.yellow;
		if(color.equals("green"))
			return Color.green;
		if(color.equals("orange"))
			return Color.orange;
		if(color.equals("magenta"))
			return Color.magenta;
		if(color.equals("gray"))
			return Color.gray;
		if(color.equals("black"))
			return Color.black;
		return null;
	}
	
	public static String getStringColor(Color color) {
		if(color.equals(Color.RED))
			return "red";
		if(color.equals(Color.BLUE))
			return "blue";
		if(color.equals(Color.PINK))
			return "pink";
		if(color.equals(Color.YELLOW))
			return "yellow";
		if(color.equals(Color.GREEN))
			return "green";
		if(color.equals(Color.ORANGE))
			return "orange";
		if(color.equals(Color.MAGENTA))
			return "magenta";
		if(color.equals(Color.GRAY))
			return "gray";
		if(color.equals(Color.BLACK))
			return "black";
		return null;
	}
}
