package com.eletrocp.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.eletrocp.entidades.Enemy;
import com.eletrocp.entidades.Entity;
import com.eletrocp.entidades.Javali;
import com.eletrocp.entidades.Lifekit;
import com.eletrocp.entidades.ManaPotion;
import com.eletrocp.entidades.Player;
import com.eletrocp.entidades.Septer;
import com.eletrocp.entidades.Sword;
import com.eletrocp.graficos.SpriteSheet;
import com.eletrocp.main.Game;

public class World {
	
	private static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			
			for(int xx = 0; xx < map.getWidth(); xx++) {
				for(int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR); // Valor padrão
					
					if(pixelAtual == 0xff000000) {
						
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
					
					} else if (pixelAtual == 0xffffffff) {
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WALL);
						
					
					} else if (pixelAtual == 0xff216e21) { // start
						tiles[xx + (yy * WIDTH)] = new PathTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_GRASS_UP_DOWN);
						
					
					} else if (pixelAtual == 0xff2b5f34) {
						tiles[xx + (yy * WIDTH)] = new PathTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_GRASS_LEFT_RIGHT);
						
					
					} else if (pixelAtual == 0xff225a2c) {
						tiles[xx + (yy * WIDTH)] = new PathTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_GRASS_PLUS);
						
					
					} else if (pixelAtual == 0xff195523) {
						tiles[xx + (yy * WIDTH)] = new PathTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_DOWN_TO_LEFT);
						
					
					} else if (pixelAtual == 0xff267d36) {
						tiles[xx + (yy * WIDTH)] = new PathTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_LEFT_TO_UP);
						
					
					} else if (pixelAtual == 0xff173c1d) {
						tiles[xx + (yy * WIDTH)] = new PathTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_UP_TO_RIGHT);
						
					
					} else if (pixelAtual == 0xff009038) {
						tiles[xx + (yy * WIDTH)] = new PathTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_RIGHT_TO_DOWN);
						
					// final
					} else if (pixelAtual == 0xff5fcde4) {
						
						Game.player.setX(xx * TILE_SIZE);
						Game.player.setY(yy * TILE_SIZE);
					
					} else if (pixelAtual == 0xffff0000) {
						
						Enemy en = new Enemy(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.ENEMY_ENT);
						Game.entities.add(en);
						Game.enemies.add(en);						
					
					} else if (pixelAtual == 0xfffbf236) {
						
						Game.entities.add(new Sword(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.SWORD_ENT));
					
					}  else if (pixelAtual == 0xff60cfad) {
						
						Septer en = new Septer(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.SEPTER_ENT);
						Game.entities.add(en);
					
					} else if (pixelAtual == 0xff6abe30) {
						
						Lifekit en = new Lifekit(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.LIFEKIT_ENT);
						Game.entities.add(en);
					
					} else if (pixelAtual == 0xff76428a) {
						
						ManaPotion en = new ManaPotion(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.MANA_POTION_ENT);
						Game.entities.add(en);
					
					} else  if (pixelAtual == 0xffaa7256) {
						Javali en = new Javali(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, Entity.JAVALI_ENT);
						Game.entities.add(en);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isFree(int xNext, int yNext) {
	    int x1 = xNext / TILE_SIZE;
	    int y1 = yNext / TILE_SIZE;

	    int x2 = (xNext + TILE_SIZE - 2) / TILE_SIZE;
	    int y2 = yNext / TILE_SIZE;

	    int x3 = xNext / TILE_SIZE;
	    int y3 = (yNext + TILE_SIZE - 2) / TILE_SIZE;

	    int x4 = (xNext + TILE_SIZE - 2) / TILE_SIZE;
	    int y4 = (yNext + TILE_SIZE - 2) / TILE_SIZE;

	    return !(tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile
	            || tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile
	            || tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile
	            || tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile);
	}
	
    public static void restartGame(String level) {
        Game.entities.clear();
        Game.enemies.clear();
        Game.projectiles.clear();

        Game.spritesheet = new SpriteSheet("/spritesheet.png");
        Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(0, 48, 16, 16));
        Game.entities.add(Game.player);
        Game.world = new World("/"+ level);
        
        Game.restorePlayerState();
    }
		
	public void render(Graphics g) {
		
		int xStart = Camera.x >> 4;
		int yStart = Camera.y >> 4;
		int xFinal = xStart + (Game.WIDTH >> 4);
		int yFinal = yStart + (Game.HEIGHT >> 4);
		
		for(int xx = xStart; xx <= xFinal; xx++) {
			for(int yy = yStart; yy <= yFinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy * WIDTH)];
					tile.render(g);
			}
		}
	}
}
