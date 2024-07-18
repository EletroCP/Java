package com.eletrocp.entidades;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.eletrocp.main.Game;
import com.eletrocp.world.Camera;

public class Entity {
	
	public static BufferedImage LIFEKIT_ENT = Game.spritesheet.getSprite(0, 80, 16, 16);
	public static BufferedImage SWORD_ENT = Game.spritesheet.getSprite(16, 80, 16, 16);
	public static BufferedImage ENEMY_ENT = Game.spritesheet.getSprite(0, 112, 16, 16);
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	private int maskX, maskY, maskW, maskH;
	
	private BufferedImage sprite;
	
	public Entity(double x, double y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskX = 0;
		this.maskY = 0;
		this.maskW = width;
		this.maskH = height;
	}
	
	public void setMask(int maskX, int maskY, int maskW, int maskH) {
		this.maskX = maskX;
		this.maskY = maskY;
		this.maskW = maskW;
		this.maskH = maskH;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
	
	public void tick() {
		// Comportamento da entidade no jogo
	}
	
	public static boolean isColidding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskX, e1.getY() + e1.maskY);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskX, e2.getY() + e2.maskY);
		
		return e1Mask.intersects(e2Mask);
	}
}
