/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.HashSet;
import java.util.Iterator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * GameCourt
 * 
 * This class holds the primary game logic of how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 * 
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

	// the state of the game logic
	private Fighter fighter; // the Black fighter, keyboard control
	public boolean playing = false; // whether the game is running
	private JLabel status; // Current status text (i.e. Running...)
	private Image background = Toolkit.getDefaultToolkit().createImage(
			"background.png");
	// Game constants
	public static final int COURT_WIDTH = 480;
	public static final int COURT_HEIGHT = 360;
	public static int fighter_VELOCITY = 4;
	// Extra Life HashSet
	private HashSet<ExtraLife> life = new HashSet<ExtraLife>();
	// Power up HashSet
	private HashSet<PowerUp> power = new HashSet<PowerUp>();
	// Power up Container
	private HashSet<PowerUp> powerhold = new HashSet<PowerUp>();
	// HashSet of Bullets
	private HashSet<Bullet> bullet = new HashSet<Bullet>();
	// HashSet of Lasers
	private HashSet<Laser> laser = new HashSet<Laser>();
	// HashSet of Aliens
	private HashSet<Alien> alien = new HashSet<Alien>();
	// HashSet of Balls
	private HashSet<Ball> ball = new HashSet<Ball>();
	// HashSet of walls
	private HashSet<Wall> wall = new HashSet<Wall>();
	// Boss
	private Boss boss;
	// Update interval for timer in milliseconds
	public static final int INTERVAL = 35;
	// Interval for alien death animation to show
	private int acounter = 0;
	// Interval for ship death animation to show
	private int rcounter = 0;
	// Interval for ball death animation to show
	private int bcounter = 0;
	// Score
	private int score = 0;
	// Check for if the game is over
	private boolean gameover = false;
	// Check if the ship has resurrected
	private boolean rezz = false;
	// Check if a wall was hit
	private boolean wall_hit = false;
	// Check for level 1
	private boolean level1 = false;
	// Check for level 2
	private boolean level2 = false;

	public GameCourt(JLabel status) {
		// creates border around the court area, JComponent method
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// The timer is an object which triggers an action periodically
		// with the given INTERVAL. One registers an ActionListener with
		// this timer, whose actionPerformed() method will be called
		// each time the timer triggers. We define a helper method
		// called tick() that actually does everything that should
		// be done in a single timestep.
		Timer timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		timer.start(); // MAKE SURE TO START THE TIMER!

		// Enable keyboard focus on the court area
		// When this component has the keyboard focus, key
		// events will be handled by its key listener.
		setFocusable(true);

		// this key listener allows the fighter to move as long
		// as an arrow key is pressed, by changing the fighter's
		// velocity accordingly. (The tick method below actually
		// moves the fighter.)
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT && fighter.Alive)
					fighter.v_x = -fighter_VELOCITY;
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT && fighter.Alive)
					fighter.v_x = fighter_VELOCITY;
				else if (e.getKeyCode() == KeyEvent.VK_UP && fighter.Alive
						&& alien.isEmpty())
					fighter.v_y = -fighter_VELOCITY;
				else if (e.getKeyCode() == KeyEvent.VK_DOWN && fighter.Alive
						&& alien.isEmpty())
					fighter.v_y = fighter_VELOCITY;
				// Cheat code to start Level 2
				else if (e.getKeyCode() == KeyEvent.VK_0) {
					alien.clear();
				}
				// Cheat code to start Level 3
				else if (e.getKeyCode() == KeyEvent.VK_9) {
					ball.clear();
					alien.clear();
					level1 = true;
				}
				// if press spacebar, create a new bullet
				else if (e.getKeyCode() == KeyEvent.VK_SPACE && playing == true
						&& fighter.Alive) {
					// Plays the laser.wav sound file
					File file = new File("laser.wav");
					URI uri = file.toURI();
					try {
						uri.toURL();
					} catch (MalformedURLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					java.applet.AudioClip clip = null;
					try {
						URL url = uri.toURL();
						clip = java.applet.Applet.newAudioClip(url);
					} catch (Exception e1) {
						System.out.println("Exception: " + e1);
					}
					if (clip != null) {
						clip.play();
					}

					bullet.add(new Bullet(
							COURT_WIDTH,
							COURT_HEIGHT,
							fighter.pos_x + fighter.width / 2 - Bullet.SIZE / 2,
							fighter.pos_y));
					// Use the power up if available
					if (!powerhold.isEmpty()) {
						// Iterator to go through the power up's
						Iterator<PowerUp> iter = powerhold.iterator();
						// Initialize p
						PowerUp p = null;
						// p will always enter one of these if's b/c powerhold
						// is not empty; want to always use last powerup first
						if (iter.hasNext()) {
							p = iter.next();
						}
						if (iter.hasNext() && p.remaining == 10) {
							p = iter.next();
						}
						if (iter.hasNext() && p.remaining == 10) {
							p = iter.next();
						}
						if (p != null && p.remaining > 0) {
							p.remaining--;
							bullet.add(new Bullet(COURT_WIDTH, COURT_HEIGHT,
									fighter.pos_x - Bullet.SIZE / 2,
									fighter.pos_y));
							bullet.add(new Bullet(COURT_WIDTH, COURT_HEIGHT,
									fighter.pos_x + fighter.width - Bullet.SIZE
											/ 2, fighter.pos_y));
							// So keep decrementing the remaining from first p
							if (p.remaining <= 0) {
								iter.remove();
								fighter.width = fighter.width - 5;
								fighter.height = fighter.height - 5;
								fighter_VELOCITY = fighter_VELOCITY - 2;

							}
						}
					}
					// Pause button!
				} else if (e.getKeyCode() == KeyEvent.VK_P && !gameover) {
					playing = !playing;
				}
			}

			// Reset fighter velocities to 0 if no keys pressed
			public void keyReleased(KeyEvent e) {
				fighter.v_x = 0;
				fighter.v_y = 0;
			}
		});
		this.status = status;
	}

	/**
	 * (Re-)set the state of the game to its initial state.
	 */
	public void reset() {
		if (alien != null) {
			alien.clear();
		}
		if (ball != null) {
			ball.clear();
		}
		if (wall != null) {
			wall.clear();
		}
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 3; j++) {
				alien.add(new Alien(COURT_WIDTH, COURT_HEIGHT, i * Alien.SIZE,
						j * 2 * Alien.SIZE));
			}
		}
		for (int i = 0; i < 3; i++) {
			wall.add(new Wall(COURT_WIDTH, COURT_HEIGHT, i * 215));
		}
		fighter = new Fighter(COURT_WIDTH, COURT_HEIGHT);
		if (life != null) {
			life.clear();
		}
		if (power != null) {
			power.clear();
		}
		if (powerhold != null) {
			powerhold.clear();
		}
		if (bullet != null) {
			bullet.clear();
		}
		if (laser != null) {
			laser.clear();
		}
		score = 0;
		playing = true;
		gameover = false;
		rezz = false;
		wall_hit = false;
		level1 = false;
		level2 = false;
		fighter_VELOCITY = 4;
		bcounter = 0;
		acounter = 0;
		rcounter = 0;
		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}

	/**
	 * This method is called every time the timer defined in the constructor
	 * triggers.
	 */
	void tick() {
		// Counter for the number of 'power up' shots left
		int holder = 0;
		for (PowerUp p : powerhold) {
			holder = holder + p.remaining;
		}
		// If Paused, display pause text
		if (!playing && !gameover) {
			status.setText("Paused");
		}
		// Execute while in play mode
		if (playing) {
			status.setText("Number of Lives remaining: " + fighter.Lives +
					" PowerUps: " + powerhold.size() + " (" + holder + 
                    " shots remaining) Score: " + score);
			// Advance fighter in designated direction
			fighter.move();

			// Alien handler
			for (Iterator<Alien> iter = alien.iterator(); iter.hasNext();) {
				Alien a = iter.next();
				// Move the alien
				a.move();
				// Shoot lasers occasionally
				if (Math.random() < 0.01 && a.Alive) {
					laser.add(new Laser(COURT_WIDTH, COURT_HEIGHT, a.pos_x
							+ a.width / 2 - Laser.SIZE / 2, a.pos_y
							+ Laser.SIZE / 2));
					// Play sound EnemyLaser.wav when a laser is shot
					File file = new File("EnemyLaser.wav");
					URI uri = file.toURI();
					try {
						uri.toURL();
					} catch (MalformedURLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					java.applet.AudioClip clip = null;
					try {
						URL url = uri.toURL();
						clip = java.applet.Applet.newAudioClip(url);
					} catch (Exception e1) {
						System.out.println("Exception: " + e1);
					}
					if (clip != null) {
						clip.play();
					}
				}
				// If an alien hits a wall, bounce back and head towards the
				// fighter
				if (a.hitWall() != null) {
					a.pos_y = a.pos_y + Alien.SIZE;
					a.bounce(a.hitWall());
				}
				// If an alien touches the fighter, lose a life for fighter
				if (a.intersects(fighter) && fighter.Alive) {
					fighter.Lives--;
					fighter.Alive = false;
					fighter.v_x = 0;
					fighter.v_y = 0;
					// Play Fighterdead.wav sound file
					File file = new File("Fighterdead.wav");
					URI uri = file.toURI();
					try {
						uri.toURL();
					} catch (MalformedURLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					java.applet.AudioClip clip = null;
					try {
						URL url = uri.toURL();
						clip = java.applet.Applet.newAudioClip(url);
					} catch (Exception e1) {
						System.out.println("Exception: " + e1);
					}
					if (clip != null) {
						clip.play();
					}
				}
				// If alien is dead, remove after a few seconds to let
				// explosion image show up
				if (!a.Alive) {
					acounter++;
					if (acounter > 50) {
						iter.remove();
						acounter = 0;
					}
				}
			}
			// Ball handler
			for (Iterator<Ball> iter = ball.iterator(); iter.hasNext();) {
				Ball b = iter.next();
				// Move ball in current direction
				b.move();
				// Occasionally fire
				if (Math.random() < 0.05 && b.Alive) {
					laser.add(new Laser(COURT_WIDTH, COURT_HEIGHT, b.pos_x
							+ b.width / 2 - Laser.SIZE / 2, b.pos_y
							+ Laser.SIZE / 2));
					// When fires, play EnemyLaser.wav
					File file = new File("EnemyLaser.wav");
					URI uri = file.toURI();
					try {
						uri.toURL();
					} catch (MalformedURLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					java.applet.AudioClip clip = null;
					try {
						URL url = uri.toURL();
						clip = java.applet.Applet.newAudioClip(url);
					} catch (Exception e1) {
						System.out.println("Exception: " + e1);
					}
					if (clip != null) {
						clip.play();
					}
				}
				// If ball hits a wall, bounce off
				for (Wall w : wall) {
					b.bounce(b.hitObj(w));
					// Take away a life from the wall
					if (b.intersects(w)) {
						w.Lives--;
					}
				}
				// If ball hits a side of the frame, bounce off
				b.bounce(b.hitWall());
				for (Ball ba : ball) {
					if (ba != b && ba.Alive && b.Alive) {
						// Balls should bounce off each other
						ba.bounce(ba.hitObj(b));
					}
				}
				// If a ball hits a fighter and the fighter is alive, take
				// life from fighter and the ball
				if (b.intersects(fighter) && fighter.Alive && b.Alive) {
					fighter.Lives--;
					fighter.Alive = false;
					fighter.v_x = 0;
					fighter.v_y = 0;
					b.Lives--;
					score++;
					// Play the Fighterdead.wav sound file
					File file = new File("Fighterdead.wav");
					URI uri = file.toURI();
					try {
						uri.toURL();
					} catch (MalformedURLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					java.applet.AudioClip clip = null;
					try {
						URL url = uri.toURL();
						clip = java.applet.Applet.newAudioClip(url);
					} catch (Exception e1) {
						System.out.println("Exception: " + e1);
					}
					if (clip != null) {
						clip.play();
					}
				}
				// See if the ball ran out of lives
				if (b.Lives < 0 && b.Alive) {
					// Kill ball if not alive
					b.Alive = false;
					b.v_x = 0;
					b.v_y = 0;
					// Play the Fighterdead.wav sound
					File file = new File("Fighterdead.wav");
					URI uri = file.toURI();
					try {
						uri.toURL();
					} catch (MalformedURLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					java.applet.AudioClip clip = null;
					try {
						URL url = uri.toURL();
						clip = java.applet.Applet.newAudioClip(url);
					} catch (Exception e1) {
						System.out.println("Exception: " + e1);
					}
					if (clip != null) {
						clip.play();
					}
				}
				// If the ball is not alive, leave on screen for
				// explosion picture to show up
				if (!b.Alive) {
					acounter++;
					if (acounter > 50) {
						iter.remove();
						acounter = 0;
					}
				}
			}
			// Wall handler
			for (Iterator<Wall> iter = wall.iterator(); iter.hasNext();) {
				Wall w = iter.next();
				// If the wall is out of lives, remove from screen
				if (w.Lives < 0) {
					iter.remove();
					continue;
				}
				// If the fighter resurrects, then replace walls with new ones
				if (rezz) {
					if (wall != null) {
						wall.clear();
					}
					for (int i = 0; i < 3; i++) {
						wall.add(new Wall(COURT_WIDTH, COURT_HEIGHT, i * 215));
					}
					rezz = false;
					break;
				}
			}
			// Bullet handler
			for (Iterator<Bullet> iter = bullet.iterator(); iter.hasNext();) {
				Bullet b = iter.next();
				// Move bullets
				b.move();
				// If the bullet goes off the screen, remove it
				if (b.pos_y == 0) {
					iter.remove();
					continue;
				}
				// If on level 3, check to see if bullets hit boss
				if (level1 && level2) {
					if (b.intersects(boss)) {
						// If a bullet hits boss, remove a life from him
						boss.Lives--;
						score++;
						if (boss.Lives < 0 && boss.Alive) {
							boss.Alive = false;
							boss.v_x = 0;
							boss.v_y = 0;
							// Play Fighterdead.wav sound file
							File file = new File("Fighterdead.wav");
							URI uri = file.toURI();
							try {
								uri.toURL();
							} catch (MalformedURLException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
							java.applet.AudioClip clip = null;
							try {
								URL url = uri.toURL();
								clip = java.applet.Applet.newAudioClip(url);
							} catch (Exception e1) {
								System.out.println("Exception: " + e1);
							}
							if (clip != null) {
								clip.play();
							}
						}
						iter.remove();
						continue;
					}
				}
				// Check to see if the bullets hit the alien
				for (Alien a : alien) {
					if (b.intersects(a) && a.Alive) {
						// Remove the bullet animation if hit an alien
						iter.remove();
						score++;
						a.Alive = false;
						a.v_x = 0;
						// Play the Fighterdead.wav soundfile
						File file = new File("Fighterdead.wav");
						URI uri = file.toURI();
						try {
							uri.toURL();
						} catch (MalformedURLException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						java.applet.AudioClip clip = null;
						try {
							URL url = uri.toURL();
							clip = java.applet.Applet.newAudioClip(url);
						} catch (Exception e1) {
							System.out.println("Exception: " + e1);
						}
						if (clip != null) {
							clip.play();
						}
						break;
					}
				}
				// Check to see if a bullet hit a ball
				for (Ball ba : ball) {
					if (ba.intersects(b) && ba.Alive) {
						// remove the bullet if hits a ball
						iter.remove();
						ba.Lives--;
						score++;
						break;
					}
				}
			}
			// Laser handler
			for (Iterator<Laser> iter = laser.iterator(); iter.hasNext();) {
				Laser l = iter.next();
				// Make laser move appropriately
				l.move();
				// Check to see if the Laser hits a wall
				for (Wall w : wall) {
					if (w.intersects(l)) {
						// Take away a life if laser hits wall + remove if
						// out of lives
						w.Lives--;
						iter.remove();
						wall_hit = true;
						break;
					}
				}
				if (wall_hit) {
					wall_hit = false;
					break;
				}
				// If the laser goes out of bounds, remove it
				if (l.pos_y == COURT_HEIGHT - Laser.SIZE) {
					iter.remove();
					continue;
					// If a laser hits a fighter...
				} else if (l.intersects(fighter)) {
					// Remove the laser
					iter.remove();
					// Take away a life from the fighter if it Alive
					if (fighter.Alive) {
						fighter.Lives--;
						// Play the Fighterdead.wav sound file
						File file = new File("Fighterdead.wav");
						URI uri = file.toURI();
						try {
							uri.toURL();
						} catch (MalformedURLException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						java.applet.AudioClip clip = null;
						try {
							URL url = uri.toURL();
							clip = java.applet.Applet.newAudioClip(url);
						} catch (Exception e1) {
							System.out.println("Exception: " + e1);
						}
						if (clip != null) {
							clip.play();
						}
					}
					fighter.Alive = false;
					fighter.v_x = 0;
					fighter.v_y = 0;
				}
			}
			// Extra Life handler
			for (Iterator<ExtraLife> iter = life.iterator(); iter.hasNext();) {
				ExtraLife l = iter.next();
				// Move the life buff
				l.move();
				// if the life buff goes out of bounds, remove it
				if (l.pos_y == COURT_HEIGHT - ExtraLife.SIZE) {
					iter.remove();
					continue;
				}
				// if the life buff intersects the fighter + the fighter is
				// alive...
				else if (l.intersects(fighter) && fighter.Alive) {
					// Remove the life buff
					iter.remove();
					// Add a life to the fighter
					fighter.Lives++;
					// Play the ExtraLife.wav file
					File file = new File("ExtraLife.wav");
					URI uri = file.toURI();
					try {
						uri.toURL();
					} catch (MalformedURLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					java.applet.AudioClip clip = null;
					try {
						URL url = uri.toURL();
						clip = java.applet.Applet.newAudioClip(url);
					} catch (Exception e1) {
						System.out.println("Exception: " + e1);
					}
					if (clip != null) {
						clip.play();
					}
				}
			}
			// Power up handler
			for (Iterator<PowerUp> iter = power.iterator(); iter.hasNext();) {
				PowerUp p = iter.next();
				// Move the powerup
				p.move();
				// If the power up goes out of bounds, remove it.
				if (p.pos_y == COURT_HEIGHT - PowerUp.SIZE) {
					iter.remove();
					continue;
				}
				// If the power up intersects the fighter and fighter is alive
				else if (p.intersects(fighter) && fighter.Alive) {
					// Check to see if player has fewer than three powerups
					if (powerhold.size() < 3) {
						powerhold.add(p);
						// increase fighter size + speed
						fighter.width = fighter.width + 5;
						fighter.height = fighter.height + 5;
						fighter_VELOCITY = fighter_VELOCITY + 2;
						// Play powerup.wav
						File file = new File("ExtraLife.wav");
						URI uri = file.toURI();
						try {
							uri.toURL();
						} catch (MalformedURLException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						java.applet.AudioClip clip = null;
						try {
							URL url = uri.toURL();
							clip = java.applet.Applet.newAudioClip(url);
						} catch (Exception e1) {
							System.out.println("Exception: " + e1);
						}
						if (clip != null) {
							clip.play();
						}
					}
					iter.remove();
				}
			} 
			// If the fighter is dead...
			if (!fighter.Alive) {
				// Increment rezz counter
				rcounter++;
				if (rcounter > 75) {
					for (@SuppressWarnings("unused")
					PowerUp p : powerhold) {
						// Make fighter original size and speed
						fighter.width = fighter.width - 5;
						fighter.height = fighter.height - 5;
						fighter_VELOCITY = fighter_VELOCITY - 2;
					}
					// Clear power up
					powerhold.clear();
					fighter.Alive = true;
					fighter.pos_x = 225;
					fighter.pos_y = 300;
					rcounter = 0;
					rezz = true;
					// Clear field of lasers
					laser.clear();
				}
				// If fighter runs out of lives, you lose!
				if (fighter.Lives <= 0) {
					status.setText("You lose! Final Score: " + score);
					gameover = true;
					playing = false;
				}

			}
			// Check to see if level1 and level2 completed...
			if (level1 && level2) {
				// Check to see if the fighter intersects the boss
				if (boss.intersects(fighter) && fighter.Alive && boss.Alive) {
					fighter.Lives--;
					fighter.Alive = false;
					boss.Lives--;
					score++;
					fighter.v_x = 0;
					fighter.v_y = 0;
					// Play the Fighterdead.wav sound file
					File file = new File("Fighterdead.wav");
					URI uri = file.toURI();
					try {
						uri.toURL();
					} catch (MalformedURLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					java.applet.AudioClip clip = null;
					try {
						URL url = uri.toURL();
						clip = java.applet.Applet.newAudioClip(url);
					} catch (Exception e1) {
						System.out.println("Exception: " + e1);
					}
					if (clip != null) {
						clip.play();
					}
				}
				// If the boss is alive, update movement and bounce off walls
				if (boss.Alive) {
					boss.move();
					boss.bounce(boss.hitWall());
				}
				// If no balls exist and the boss is dead, you win!
				if (ball.isEmpty() && !boss.Alive) {
					status.setText("You win! Final Score: " + score);
					playing = false;
					gameover = true;
				}
			}
			// If the boss is alive, on third level, and less than three balls
			// Chance to make a new one :O devious.
			if (Math.random() < 0.025 && level1 && level2 && boss.Alive
					&& ball.size() < 3) {
				ball.add(new Ball(COURT_WIDTH, COURT_HEIGHT, boss.pos_x,
						boss.pos_y));
			}
			// If the boss is alive, on third level, chance to shoot a laser
			if (Math.random() < 0.025 && level1 && level2 && boss.Alive) {
				laser.add(new Laser(COURT_WIDTH, COURT_HEIGHT, boss.pos_x
						+ Boss.SIZE / 2, boss.pos_y + Boss.SIZE));
				laser.add(new Laser(COURT_WIDTH, COURT_HEIGHT, boss.pos_x
						+ Boss.SIZE / 2 - 2 * Laser.SIZE, boss.pos_y
						+ Boss.SIZE));
				laser.add(new Laser(COURT_WIDTH, COURT_HEIGHT, boss.pos_x
						+ Boss.SIZE / 2 + 2 * Laser.SIZE, boss.pos_y
						+ Boss.SIZE));
			}
			// Chance to add a free life; capped at one free life, must have
			// fewer than three lives
			if (Math.random() < 0.005 && life.size() < 1 && fighter.Lives < 3) {
				life.add(new ExtraLife(COURT_WIDTH, COURT_HEIGHT, (int) (Math
						.random() * COURT_WIDTH)));
			}
			// Chance to give power ups; can only have three power ups at a time
			if (Math.random() < 0.005 && powerhold.size() <= 3) {
				power.add(new PowerUp(COURT_WIDTH, COURT_HEIGHT, (int) (Math
						.random() * COURT_WIDTH)));
			}
			// If all aliens are dead and you are on level 1...
			if (alien.isEmpty() && !level1 && !level2) {
				for (int i = 0; i < 5; i++) {
					ball.add(new Ball(COURT_WIDTH, COURT_HEIGHT, (int) (Math
							.random() * COURT_WIDTH), 0));
				}
				// Start level 2!
				level1 = true;
			}
			// Start level 3!
			if (ball.isEmpty() && alien.isEmpty() && level1 && !level2) {
				level2 = true;
				boss = new Boss(COURT_WIDTH, COURT_HEIGHT);
			}

			// update the display
			repaint();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Draw background image
		g.drawImage(background, 0, 0, null);
		// Draw the fighter
		fighter.draw(g);
		// Draw the boss and death animation for boss
		if (level2 && level1) {
			if (boss.Alive) {
				boss.draw(g);
			} else if (!boss.Alive && boss.pos_x >= 0 && boss.pos_y >= 0) {
				bcounter++;
				if (bcounter < 100) {
					boss.draw(g);
				}
				if (bcounter >= 100) {
					boss.pos_x = -100;
					boss.pos_y = -100;
				}
			}
		}
		// Draw the ball
		for (Ball b : ball) {
			b.draw(g);
		}
		// Draw the aliens
		for (Alien a : alien) {
			a.draw(g);
		}
		// Draw extra life
		for (ExtraLife l : life) {
			l.draw(g);
		}
		// Draw bullet
		for (Bullet b : bullet) {
			b.draw(g);
		}
		// Draw Laser
		for (Laser l : laser) {
			l.draw(g);
		}
		// Draw walls
		for (Wall w : wall) {
			w.draw(g);
		}
		// Draw powerups
		for (PowerUp p : power) {
			p.draw(g);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH, COURT_HEIGHT);
	}
}
