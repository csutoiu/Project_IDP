package gui;

import java.awt.Image;
import java.awt.Toolkit;

import org.apache.log4j.Logger;

public class GUIHelper {

	private static Logger logger = Logger.getLogger(GUIHelper.class);
	/**
	 * Get image localized at the "path"
	 */
	public Image getImage(String path) {
		java.net.URL url = getClass().getResource(path);
		Image image = null;
		if(url == null )
			try {
				throw new Exception( "ERR cannot find resource: " + path );
			} catch (Exception e) {
				logger.error(e);
			}
		image = Toolkit.getDefaultToolkit().getImage(url);
		return image;
	}
	
	/*public static BufferedImage saveCanvasImage(Canvas canvas) {
		BufferedImage image=new BufferedImage(canvas.getWidth(), canvas.getHeight(),BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g2=(Graphics2D)image.getGraphics();
		
		canvas.paint(g2);
		try {
			ImageIO.write(image, "png", new File("canvas.png"));
		} catch (Exception e) {
			
		}
		    
		return image;
	}*/
}
