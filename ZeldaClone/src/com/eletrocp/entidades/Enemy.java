package com.eletrocp.entidades;

import java.awt.image.BufferedImage;

import com.eletrocp.main.Game;

public class Enemy extends Entity{
	
	private double spd = 0.6;

	public Enemy(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
	public void tick() {
		if((int)x < Game.player.getX()) {
			x += spd;
		} else if ((int)x > Game.player.getX()) {
			x -= spd;
		}
		
		if((int)y < Game.player.getY()) {
			y += spd;
		} else if ((int)y > Game.player.getY()) {
			y -= spd;
		}
	}

}
 