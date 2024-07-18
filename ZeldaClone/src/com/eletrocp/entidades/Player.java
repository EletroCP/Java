package com.eletrocp.entidades;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.eletrocp.main.Game;
import com.eletrocp.world.Camera;
import com.eletrocp.world.World;

public class Player extends Entity {

	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 1;
	public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
	public int dir = 0;
	public static boolean knockback = false;
	public static double life = 100, maxLife = 100;

	private int knockbackFrames = 0, maxKnockbackFrames = 25; // Ajuste a duração do knockback conforme necessário
	private double knockbackDistance = 60; // Distância do knockback
	private double knockbackX = 0, knockbackY = 0;

	public double spd = 1.4;

	private boolean moved = false;
	public boolean right, left, up, down;

	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	private BufferedImage[] knockbackPlayer;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		rightPlayer = new BufferedImage[2];
		leftPlayer = new BufferedImage[2];
		upPlayer = new BufferedImage[2];
		downPlayer = new BufferedImage[2];
		knockbackPlayer = new BufferedImage[2];

		for (int i = 0; i < 2; i += 1) {
			rightPlayer[i] = Game.spritesheet.getSprite(64 + (i * 16), 48, 16, 16);
		}

		for (int i = 0; i < 2; i += 1) {
			leftPlayer[i] = Game.spritesheet.getSprite(48 - (i * 16), 48, 16, 16);
		}

		for (int i = 0; i < 2; i += 1) {
			upPlayer[i] = Game.spritesheet.getSprite(96 + (i * 16), 48, 16, 16);
		}

		for (int i = 0; i < 2; i += 1) {
			downPlayer[i] = Game.spritesheet.getSprite(0 + (i * 16), 48, 16, 16);
		}

		for (int i = 0; i < 2; i += 1) {
			knockbackPlayer[i] = Game.spritesheet.getSprite(48 + (i * 16), 48, 16, 16);
		}
	}

	public void tick() {
		if (knockback) {
			knockbackFrames++;
			x += knockbackX;
			y += knockbackY;
			if (knockbackFrames >= maxKnockbackFrames) {
				knockback = false;
				knockbackFrames = 0;
				knockbackX = 0;
				knockbackY = 0;
			}
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}
		} else {
			moved = false;
			if (right && World.isFree((int) (x + spd), this.getY())) {
				moved = true;
				dir = right_dir;
				x += spd;

			} else if (left && World.isFree((int) (x - spd), this.getY())) {
				moved = true;
				dir = left_dir;
				x -= spd;
			}

			if (up && World.isFree(this.getX(), (int) (y - spd))) {
				moved = true;
				dir = up_dir;
				y -= spd;
			} else if (down && World.isFree(this.getX(), (int) (y + spd))) {
				moved = true;
				dir = down_dir;
				y += spd;
			}

			if (moved) {
				frames += 1;
				if (frames == maxFrames) {
					frames = 0;
					index += 1;
					if (index > maxIndex) {
						index = 0;
					}
				}
			}
		}
		
		this.checkCollisionItens();

		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}
	
	public void checkCollisionItens() {
		for(int i = 0; i < Game.entities.size(); i+=1) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Lifekit) {
				if(Entity.isColidding(this, atual) && life < maxLife) {
					life = Lifekit.heal(life, maxLife);
					Game.entities.remove(i);
				}
			}
		}
	}

	public void render(Graphics g) {
		if (knockback) {
			g.drawImage(knockbackPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else {
			if (dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}

			if (dir == up_dir) {
				g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if (dir == down_dir) {
				g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		}
	}

	public void applyKnockback(double enemyX, double enemyY) {
		double angle = Math.atan2(y - enemyY, x - enemyX);
		knockbackX = Math.cos(angle) * (knockbackDistance / maxKnockbackFrames);
		knockbackY = Math.sin(angle) * (knockbackDistance / maxKnockbackFrames);
		knockback = true;
		frames = 0; // Reinicia a animação de knockback
		index = 0; // Reinicia a animação de knockback
	}
}
