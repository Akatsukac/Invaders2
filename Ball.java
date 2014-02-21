import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Ball extends GameObj {

	public static final String img_file1 = "newball.png";
	public static String img_file2 = "explosion.png";
	public static final int SIZE = 20;
	public static final int INIT_VEL_X = 4;
	public static final int INIT_VEL_Y = 4;
	// Has to get hit 3 times to die
	public int Lives = 3;
	public boolean Alive = true;

	private static BufferedImage img1;
	private static BufferedImage img2;

	// Needs to take in an initial x and y coordinates in addition to court w/h
	public Ball(int courtWidth, int courtHeight, int INIT_X, int INIT_Y) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_X, INIT_Y, SIZE, SIZE, courtWidth,
				courtHeight);
		// set img1 to the ball image
		try {
			if (img1 == null) {
				img1 = ImageIO.read(new File(img_file1));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		// set img2 to the explosion
		try {
			img2 = ImageIO.read(new File(img_file2));
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}

	@Override
	public void draw(Graphics g) {
		// If the ball is alive, draw the ball image
		if (Alive) {
			g.drawImage(img1, pos_x, pos_y, width, height, null);
		}
		// Otherwise draw the explosion
		else {
			g.drawImage(img2, pos_x, pos_y, width, height, null);
		}

	}

}
