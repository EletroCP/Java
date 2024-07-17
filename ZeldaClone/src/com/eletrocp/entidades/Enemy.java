package com.eletrocp.entidades;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.eletrocp.main.Game;
import com.eletrocp.world.World;

public class Enemy extends Entity{
	
	private double spd = 0.6;
	private int maskX = 8, maskY = 8, maskW = 16, maskH = 16;

	public Enemy(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
	public void tick() {
		maskX = 8;
		maskY = 8;
		maskW = 16;
		maskH = 16;
		if(
				(int)x < Game.player.getX() 
				&& World.isFree((int)(x + spd), this.getY())
				&& !isColidding((int)(x + spd), this.getY())
			) {
			x += spd;
		} else if (
				(int)x > Game.player.getX() 
				&& World.isFree((int)(x - spd), this.getY())
				&& !isColidding((int)(x - spd), this.getY())
				) {
			x -= spd;
		}
		
		if(
				(int)y < Game.player.getY() 
				&& World.isFree(this.getX(), (int)(y + spd))
				&& !isColidding(this.getX(), (int)(y + spd))
				) {
			y += spd;
		} else if (
				(int)y > Game.player.getY() 
				&& World.isFree(this.getX(), (int)(y - spd))
				&& !isColidding(this.getX(), (int)(y - spd))
				) {
			y -= spd;
		}
	}
	
	public boolean isColidding(int xNext, int yNext) {
		Rectangle enemyCurrent = new Rectangle(xNext + maskX, yNext + maskY, maskW, maskH);
		
		for(int i = 0; i < Game.enemies.size(); i += 1) {
			Enemy e = Game.enemies.get(i);
			if(e == this)
				continue;
			Rectangle targetEnemy = new Rectangle(e.getX() + maskX, e.getY() + maskY, maskW, maskH);
			if(enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void render(Graphics g) {
		super.render(g);
		//usado para ver a mascara de colisão
		// g.setColor(Color.blue);
		// g.fillRect(this.getX() + maskX - Camera.x,this.getY() + maskY - Camera.y, maskW, maskH);
	}

}
 