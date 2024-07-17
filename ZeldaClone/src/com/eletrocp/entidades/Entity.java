package com.eletrocp.entidades;

import java.awt.Graphics;
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
	
	private BufferedImage sprite;
	
	public Entity(double x, double y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
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
}
