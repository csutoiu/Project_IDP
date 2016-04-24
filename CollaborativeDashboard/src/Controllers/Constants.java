package Controllers;

public interface Constants {

	public static String SUCCESS				= "SUCCESS";
	
	/* Login Results */
	public static String ERROR_LOGIN			= "Invalid credentials, please try again.";
	public static String ERROR_LOGIN_ALREADY	= "User already logged in.";
	
	/* Login Button Results */
	public static String SIGN_IN				= "Sing In";
	
	/* Create Account Results */
	public static String EMPTY_FIELDS 			= "Please fill out all fields.";
	public static String USERNAME_EXISTS 		= "Sorry, that username already exists!";
	public static String EMAIL_EXISTS 			= "Sorry, that email already exists!";
	public static String EMAIL_FORMAT			= "Please enter a valid email address.";
	public static String PASSWORDS				= "These passwords don't match.";
	
	/* Create Account Button Actions */
	public static String SIGN_UP 				= "Sign up";
	public static String CANCEL					= "Cancel";
	
	/* Dashboard Button Actions */
	public static String LOGOUT					= "Logout";
	public static String CREATE_GROUP			= "Create Group";
	public static String ADD_USER				= "Add user";
	public static String JOIN_GROUP				= "Join Group";
	public static String LEAVE_GROUP			= "Leave Group";
	
	public static String SQUARE					= "Square";
	public static String CIRCLE					= "Circle";
	public static String LINE					= "Line";
	public static String ARROW					= "Arrow";
	
	public static int FIGURE_SIZE				= 50;
	
	public static String SAVE_WORK 				= "Save group work";
	public static String CHANGE_FONT			= "Change Font";
	public static String CHANGE_COLOR			= "Change Color";
	
	public static String SEND					= "Send";
	
	/* Event Constants */
	public static int	SIGN_IN_EVENT				= 1;
	public static int 	SIGN_UP_EVENT				= 2;
	public static int 	LOGOUT_EVENT				= 3;
	public static int 	ADD_USER_TO_GROUP_EVENT		= 4;
	public static int 	CREATE_GROUP_EVENT			= 5;
	public static int 	LEAVE_GROUP_EVENT			= 6;
	public static int 	ADD_SHAPE_EVENT				= 7;
	public static int 	CANVAS_REQUEST_EVENT		= 8;
	public static int 	CANVAS_RESPONSE_EVENT		= 9;
}
