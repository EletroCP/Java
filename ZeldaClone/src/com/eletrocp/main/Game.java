package com.eletrocp.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
	private final int SCALE  = 3;
	public static int bossLife, bossMaxLife;
	
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
    public static boolean hasBoss = false;
    
	
	public static Random rand;
	
	public UI ui;
	
	public Game() {
		rand = new Random();
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		
		ui = new UI();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		projectiles = new ArrayList<Projectile>();
		spritesheet = new SpriteSheet("/spritesheet.png");
		player = new Player(0,0,16,16, spritesheet.getSprite(0, 48, 16, 16));
		entities.add(player);
		world = new World("/level1.png");
		
		
		this.addKeyListener(this);
		this.setFocusable(true);
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

    public void tick() {
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
