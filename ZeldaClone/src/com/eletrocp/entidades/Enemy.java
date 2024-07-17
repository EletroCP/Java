package com.eletrocp.entidades;

import java.awt.image.BufferedImage;

import com.eletrocp.main.Game;
import com.eletrocp.world.World;

public class Enemy extends Entity{
	
	private double spd = 0.6;

	public Enemy(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
	public void tick() {
		if((int)x < Game.player.getX() && World.isFree((int)(x + spd), this.getY())) {
			x += spd;
		} else if ((int)x > Game.player.getX() && World.isFree((int)(x - spd), this.getY())) {
			x -= spd;
		}
		
		if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y + spd))) {
			y += spd;
		} else if ((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y - spd))) {
			y -= spd;
		}
	}

}
 