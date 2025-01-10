package com.eletrocp.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.eletrocp.entidades.Enemy;
import com.eletrocp.entidades.Entity;
import com.eletrocp.entidades.Player;
import com.eletrocp.entidades.Projectile;
import com.eletrocp.graficos.SpriteSheet;
import com.eletrocp.graficos.UI;
import com.eletrocp.world.World;

public class Game extends Canvas implements Runnable, KeyListener {
	
private static final long serialVersionUID = 1L;
	
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning;
	public final static int WIDTH = 240;
	public final static int HEIGHT = 160;
	public static int frames = 0;
	public static int FPS;
	public final static int SCALE  = 3;
	public static int bossLife = 100, bossMaxLife = 100;
	
	private int CUR_LEVEL = 1, MAX_LEVEL = 3;	
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Projectile> projectiles;
	public static List<Integer> inventory = new ArrayList<>();
	public static SpriteSheet spritesheet;
	public static World world;
	
	public static Player player;
	
	private static int savedWeaponID = 0;
	private static double savedLife = 0;
	private static double savedMana = 0;
	private static boolean savedHasWeapon = false;
	private static boolean resetGame = false;
	private static boolean inventoryIsOpen = false;
	public static boolean hasBoss;
	
	public static Random rand;
	
	public UI ui;
	
	public static String gameState = "MENU";

	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	
	public Menu menu;
	
	public Game() {
		rand = new Random();
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		
		ui = new UI();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		projectiles = new ArrayList<Projectile>();
		menu = new Menu();
		
		spritesheet = new SpriteSheet("/spritesheet.png");
		player = new Player(0,0,16,16, spritesheet.getSprite(0, 48, 16, 16));
		entities.add(player);
		world = new World("/level1.png");
		
		
		this.addKeyListener(this);
		this.setFocusable(true);
		// Sound.musicBackground.loop();
	}
		
	public void initFrame() {
		frame = new JFrame("Clone Zelda");
		//janela sem bordas
		// frame.setUndecorated(true);
		frame.add(this);
		frame.setResizable(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public static void savePlayerState() {
        savedWeaponID = Player.weaponID;
        savedHasWeapon = player.hasWeapon;
        savedLife = player.life;
        savedMana = Player.mana;
    }

    public static void restorePlayerState() {
        Player.weaponID = savedWeaponID;
        player.hasWeapon = savedHasWeapon;
        player.life = savedLife;
        Player.mana = savedMana;
        
    }
    
    private void nextWeapon() {
        int inventorySize = inventory.size();
        int currentWeaponIndex = inventory.indexOf(Player.weaponID);

        if (inventorySize > 0) {
            if (currentWeaponIndex == -1 || currentWeaponIndex == inventorySize - 1) {
                Player.weaponID = inventory.get(0);
            } else {
                Player.weaponID = inventory.get(currentWeaponIndex + 1);
            }
        }
    }
    
    private void previousWeapon() {
        int inventorySize = inventory.size();
        int currentWeaponIndex = inventory.indexOf(Player.weaponID);

        if (inventorySize > 0) {
            if (currentWeaponIndex == -1 || currentWeaponIndex == 0) {
                Player.weaponID = inventory.get(inventorySize - 1);
            } else {
                Player.weaponID = inventory.get(currentWeaponIndex - 1);
            }
        }
    }

    public void tick() {
    	if(gameState == "NORMAL") {
    		for(int i = 0; i < entities.size(); i +=1) {
    			Entity e = entities.get(i);
    			e.tick();
    		}
    		
    		for(int i = 0; i < projectiles.size() ; i +=1) {
    			projectiles.get(i).tick();
    		}
    		
    		if(enemies.size() == 0) {
    			savePlayerState();
    			CUR_LEVEL +=1;
    			if(CUR_LEVEL > MAX_LEVEL) {
    				CUR_LEVEL = 1;
    			}
    			String newWorld = "level" + CUR_LEVEL + ".png";
    			World.restartGame(newWorld);
    			restorePlayerState();
    		} 
    	} else if (gameState == "GAMEOVER") {
			framesGameOver += 1;
			if(framesGameOver == 25) {
				framesGameOver = 0;
				if(showMessageGameOver) {
					showMessageGameOver = false;
				} else {
					showMessageGameOver = true;
				}
				
				if(resetGame) {
					resetGame = false;
					gameState = "NORMAL";
					CUR_LEVEL = 1;
					String newWorld = "level" + CUR_LEVEL + ".png";
		            World.restartGame(newWorld);
				}
			}
		} else if(gameState == "MENU") {
			menu.tick();
		}
    }
	
    public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		world.render(g);
		for(int i = 0; i < entities.size(); i +=1) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		for(int i = 0; i < projectiles.size() ; i +=1) {
			projectiles.get(i).render(g);
		}
		
		ui.render(g);
		
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		if(gameState == "GAMEOVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color (0,0,0, 100));
			g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
			g.setFont(new Font("arial", Font.BOLD, 60));
			g.setColor(Color.black);
			g.drawString("Game Over", (WIDTH*SCALE) / 2 - 150, (HEIGHT * SCALE) / 2);
			g.setFont(new Font("arial", Font.BOLD, 30));
			if(showMessageGameOver) {
				g.drawString("> Pressione Enter para reiniciar <", (WIDTH*SCALE) / 2 - 220, (HEIGHT * SCALE) / 2 + 35);				
			}
		} else if(gameState == "MENU") {
			menu.render(g);
			
		}
		
		if(inventoryIsOpen) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color (0,0,0, 100));
			g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
			g.setFont(new Font("arial", Font.BOLD, 60));
			g.drawString("Inventario", (WIDTH*SCALE) / 2 - 150, (HEIGHT * SCALE) / 2);
		}
		bs.show();
	}

	public void run() {
		//ultimo tempo em nanometros
		long lastTime = System.nanoTime();
		//numero de ticks por segundo
		double amountOfTicks = 60.0;
		//momento para fazer o update do jogo
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		double timer = System.currentTimeMillis();
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames ++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				FPS = frames;
				frames = 0;
				timer+=1000;
			}
		}
		stop();
	}

	public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_R) {
			player.jump = true;
		}
		
	    if(e.getKeyCode() == KeyEvent.VK_D) {
	        player.right = true;
	    } else if(e.getKeyCode() == KeyEvent.VK_A) {
	        player.left = true;
	    }
	    
	    if(e.getKeyCode() == KeyEvent.VK_W) {
	        player.up = true;
	    } else if (e.getKeyCode() == KeyEvent.VK_S) {
	        player.down = true;
	    }
	    
	    if(e.getKeyCode() == KeyEvent.VK_SPACE) {
	        Player.hasAtack = true;
	    }
	    
	    if(e.getKeyCode() == KeyEvent.VK_1 && Game.inventory.contains(1)) {
	        Player.weaponID = 1;
	        player.hasWeapon = true;
	    }
	    
	    if(e.getKeyCode() == KeyEvent.VK_2 && Game.inventory.contains(2)) {
	        Player.weaponID = 2;
	        player.hasWeapon = true;
	    }
	    
	    if(e.getKeyCode() == KeyEvent.VK_ENTER) {
	    	resetGame = true;
	    	menu.enter = true;
	    }
	    
	    if(e.getKeyCode() == KeyEvent.VK_I) {
	    	if(!inventoryIsOpen) {
	    		inventoryIsOpen = true;
	    	} else {
	    		inventoryIsOpen = false;
	    	}
	    }
	    
	    if(e.getKeyCode() == KeyEvent.VK_E) {
	    	nextWeapon();
	    }
	    
	    if(e.getKeyCode() == KeyEvent.VK_Q) {
	    	previousWeapon();
	    }
	    
	    if(e.getKeyCode() == KeyEvent.VK_UP) {
	    	if(gameState == "MENU") {
	    		menu.up = true;
	    	}
	    }
	    
	    if(e.getKeyCode() == KeyEvent.VK_DOWN) {
	    	if(gameState == "MENU") {
	    		menu.down = true;
	    	}
	    }
	    
	    if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	    	gameState = "MENU";
	    	menu.pause = true;
	    }
	    
	}


	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		} else if(e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
		
	}

}