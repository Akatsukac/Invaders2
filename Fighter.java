/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Fighter extends GameObj {
	public static String img_file1 = "Fighter.png";
	public static String img_file2 = "dead.png";
	public static int SIZE = 30;
	public static final int INIT_X = 225;
	public static final int INIT_Y = 300;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	// Need to hit 3 times to kill
	public int Lives = 3;
	public boolean Alive = true;

	private static BufferedImage img1;
	private static BufferedImage img2;

	public Fighter(int courtWidth, int courtHeight) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_X, INIT_Y, SIZE, SIZE, courtWidth,
				courtHeight);
		// Set img1 to picture of the fighter jet
		try {
			img1 = ImageIO.read(new File(img_file1));
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		// Set img2 to the picture of blood
		try {
			img2 = ImageIO.read(new File(img_file2));
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}

	@Override
	public void draw(Graphics g) {
		// If alive, draw the fighter jet
		if (Alive) {
			g.drawImage(img1, pos_x, pos_y, width, height, null);
		}
		// If dead, draw blood
		else {
			g.drawImage(img2, pos_x, pos_y, width, height, null);
		}
		
	}

}
