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
public class Alien extends GameObj {
	public static String img_file1 = "alien.png";
	public static String img_file2 = "explosion.png";
	public static final int SIZE = 30;
	public static final int INIT_VEL_X = 1;
	public static final int INIT_VEL_Y = 0;
	public boolean Alive = true;

	private static BufferedImage img1;
	private static BufferedImage img2;

	// Take in the initial x and y coordinates in addition to court w/h
	public Alien(int courtWidth, int courtHeight, int INIT_X, int INIT_Y) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_X, INIT_Y, SIZE, SIZE, courtWidth,
				courtHeight);
		// Set img1 to the alien ship file
		try {
			img1 = ImageIO.read(new File(img_file1));
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		// Set img2 to the dead alien file
		try {
			img2 = ImageIO.read(new File(img_file2));
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}

	@Override
	public void draw(Graphics g) {
		// If the alien is alive, draw the ship
		if (Alive) {
			g.drawImage(img1, pos_x, pos_y, width, height, null);
		}
		// Otherwise draw an explosion
		else {
			g.drawImage(img2, pos_x, pos_y, width, height, null);
		}
	}
}
