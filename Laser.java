/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;

public class Laser extends GameObj {
	public static final int SIZE = 5;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 10;

	// Implement a simple laser
	public Laser(int courtWidth, int courtHeight, int INIT_X, int INIT_Y) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_X, INIT_Y, SIZE, SIZE, courtWidth,
				courtHeight);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(pos_x, pos_y, width, height * 2);
	}

}
