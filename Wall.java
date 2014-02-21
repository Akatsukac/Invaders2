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

/**
 * A game object displayed using an image.
 * 
 * Note that the image is read from the file when the object is constructed, and
 * that all objects created by this constructor share the same image data (i.e.
 * img is static). This important for efficiency, your program will go very
 * slowing if you try to create a new BufferedImage every time the draw method
 * is invoked.
 */
public class Wall extends GameObj {
	public static String img_file1 = "wall1.png";
	public static String img_file2 = "wall2.png";
	public static String img_file3 = "wall3.png";
	public static final int SIZE = 50;
	public static final int INIT_Y = 250;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	// Takes 5 shots to kill
	public int Lives = 5;

	private static BufferedImage img1;
	private static BufferedImage img2;
	private static BufferedImage img3;

	// set BufferedImages with respective png files
	public Wall(int courtWidth, int courtHeight, int INIT_X) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_X, INIT_Y, SIZE, SIZE, courtWidth,
				courtHeight);
		try {
			img1 = ImageIO.read(new File(img_file1));
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		try {
			img2 = ImageIO.read(new File(img_file2));
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		try {
			img3 = ImageIO.read(new File(img_file3));
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}

	@Override
	// Draw certain pictures based on # of lives remaining
	public void draw(Graphics g) {
		if (Lives >= 3) {
			g.drawImage(img1, pos_x, pos_y, width, height, null);
		} else if (Lives == 2) {
			g.drawImage(img2, pos_x, pos_y, width, height, null);
		} else if (Lives == 1) {
			g.drawImage(img3, pos_x, pos_y, width, height, null);
		}
	}
}
