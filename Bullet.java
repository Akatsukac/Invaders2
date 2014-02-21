/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;

public class Bullet extends GameObj {
	public static final int SIZE = 5;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = -10;
	
	// create a bullet at some initial x and y coordinates
	public Bullet(int courtWidth, int courtHeight, int INIT_X, int INIT_Y) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_X, INIT_Y, SIZE, SIZE, courtWidth,
				courtHeight);
	}

	@Override
	// Draw the bullet
	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(pos_x, pos_y, width, height * 2);
	}

}
