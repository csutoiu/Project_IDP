package gui;

import java.awt.Image;
import java.awt.Toolkit;

public class GUIHelper {

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
				e.printStackTrace();
			}
		image = Toolkit.getDefaultToolkit().getImage(url);
		return image;
	}
}
