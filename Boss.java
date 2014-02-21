import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Boss extends GameObj {
	public static final String img_file1 = "rag.gif";
	public static String img_file2 = "explosion.png";
	public static final int SIZE = 40;
	public static final int INIT_X = 220;
	public static final int INIT_Y = 0;
	public static final int INIT_VEL_X = 4;
	public static final int INIT_VEL_Y = 0;
	// Must hit 10 times to kill
	public int Lives = 10;
	public boolean Alive = true;

	private static BufferedImage img1;
	private static BufferedImage img2;

	public Boss(int courtWidth, int courtHeight) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_X, INIT_Y, SIZE, SIZE, courtWidth,
				courtHeight);
		// Set img1 to the picture of ragnaros
		try {
			if (img1 == null) {
				img1 = ImageIO.read(new File(img_file1));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		// Set img2 to the picture of explosion
		try {
			img2 = ImageIO.read(new File(img_file2));
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}

	@Override
	public void draw(Graphics g) {
		// If alive, draw picture of ragnaros
		if (Alive) {
			g.drawImage(img1, pos_x, pos_y, width, height, null);
		}
		// If dead, draw explosion
		else {
			g.drawImage(img2, pos_x, pos_y, width, height, null);
		}

	}

}
