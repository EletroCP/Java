package com.eletrocp.entidades;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.eletrocp.main.Game;
import com.eletrocp.world.Camera;
import com.eletrocp.world.World;

public class Player extends Entity {
	
	public boolean right, left, up, down;
	public int right_dir = 0;
	public int left_dir = 1;
	public int up_dir = 2;
	public int down_dir = 3;
	public int dir = 0;
	public double spd = 1.4;

	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 1;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	
	public Player(int x, int y, int widht, int height, BufferedImage sprite) {
		super(x, y, widht, height, sprite);
		
		rightPlayer = new BufferedImage[2];
		leftPlayer = new BufferedImage[2];
		upPlayer = new BufferedImage[2];
		downPlayer = new BufferedImage[2];
		
		for(int i = 0; i < 2; i += 1) {			
			rightPlayer[i] = Game.spritesheet.getSprite(64 + (i * 16), 48, 16, 16);
		}
		
		for(int i = 0; i < 2; i += 1) {			
			leftPlayer[i] = Game.spritesheet.getSprite(48 - (i * 16), 48, 16, 16);
		}
		
		for(int i = 0; i < 2; i += 1) {			
			upPlayer[i] = Game.spritesheet.getSprite(96 + (i * 16), 48, 16, 16);
		}
		
		for(int i = 0; i < 2; i += 1) {			
			downPlayer[i] = Game.spritesheet.getSprite(0 + (i * 16), 48, 16, 16);
		}
	}
	
	public void tick() {
		moved = false;
		if(right && World.isFree((int)(x+spd), this.getY())) {
			moved = true;
			dir = right_dir;
			x += spd;
			
		} else if (left && World.isFree((int)(x-spd), this.getY())) {
			moved = true;
			dir = left_dir;
			x -= spd;
		}
		
		if(up && World.isFree(this.getX(), (int)(y-spd))) {
			moved = true;
			dir = up_dir;
			y -= spd;
		} else if (down && World.isFree(this.getX(), (int)(y+spd))) {
			moved = true;
			dir = down_dir;
			y += spd;
		}
		
		if(moved) {
			frames +=1;
			if(frames == maxFrames) {
				frames = 0;
				index +=1;
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}
	
	public void render(Graphics g) {
		if(dir == right_dir) {			
			g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if(dir == left_dir) {
			g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		
		if(dir == up_dir) {			
			g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if(dir == down_dir) {
			g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}

}
