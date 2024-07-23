package com.eletrocp.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.eletrocp.main.Game;

public class Tile {
	
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 0, 32, 32);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(32, 0, 32, 32);
	public static BufferedImage TILE_GRASS_UP_DOWN = Game.spritesheet.getSprite(64, 0, 32, 32);
	public static BufferedImage TILE_GRASS_LEFT_RIGHT = Game.spritesheet.getSprite(96, 0, 32, 32);
	public static BufferedImage TILE_GRASS_PLUS = Game.spritesheet.getSprite(128, 0, 32, 32);
	public static BufferedImage TILE_DOWN_TO_LEFT = Game .spritesheet.getSprite(160, 0, 32, 32);
	public static BufferedImage TILE_LEFT_TO_UP = Game .spritesheet.getSprite(192, 0, 32, 32);
	public static BufferedImage TILE_UP_TO_RIGHT = Game .spritesheet.getSprite(224, 0, 32, 32);
	public static BufferedImage TILE_RIGHT_TO_DOWN = Game .spritesheet.getSprite(256, 0, 32, 32);
	
	
	private BufferedImage sprite;
	private int x,y;
	
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
		
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, 16, 16, null);
	}
	
}
