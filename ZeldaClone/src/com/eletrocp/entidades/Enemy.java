package com.eletrocp.entidades;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.eletrocp.main.Game;
import com.eletrocp.world.Camera;
import com.eletrocp.world.World;

public class Enemy extends Entity {

	private int maskX = 0, maskY = 0, maskW = 16, maskH = 16;
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 1;
	public int right_dir = 0;
	public int left_dir = 1;
	public int up_dir = 2;
	public int down_dir = 3;
	public int dir = 0;
	private int life = 20;

	private double spd = 0.2;
	
	private boolean moved = false;
	private boolean push = true;


	private BufferedImage[] rightEnemy;
	private BufferedImage[] leftEnemy;
	private BufferedImage[] upEnemy;
	private BufferedImage[] downEnemy;

	public Enemy(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		rightEnemy = new BufferedImage[2];
		leftEnemy = new BufferedImage[2];
		upEnemy = new BufferedImage[2];
		downEnemy = new BufferedImage[2];

		for (int i = 0; i < 2; i += 1) {
			rightEnemy[i] = Game.spritesheet.getSprite(64 + (i * 16), 112, 16, 16);
			leftEnemy[i] = Game.spritesheet.getSprite(48 - (i * 16), 112, 16, 16);
			upEnemy[i] = Game.spritesheet.getSprite(96 + (i * 16), 112, 16, 16);
			downEnemy[i] = Game.spritesheet.getSprite(0 + (i * 16), 112, 16, 16);
		}
	}

	public void tick() {
		moved = false;

		if (isColiddingWithPlayer() == false) {

			if ((int) x < Game.player.getX() && World.isFree((int) (x + spd), this.getY())
					&& !isColidding((int) (x + spd), this.getY())) {
				x += spd;
				dir = right_dir;
				moved = true;
			} else if ((int) x > Game.player.getX() && World.isFree((int) (x - spd), this.getY())
					&& !isColidding((int) (x - spd), this.getY())) {
				x -= spd;
				dir = left_dir;
				moved = true;
			}

			if ((int) y < Game.player.getY() && World.isFree(this.getX(), (int) (y + spd))
					&& !isColidding(this.getX(), (int) (y + spd))) {
				y += spd;
				dir = down_dir;
				moved = true;
			} else if ((int) y > Game.player.getY() && World.isFree(this.getX(), (int) (y - spd))
					&& !isColidding(this.getX(), (int) (y - spd))) {
				y -= spd;
				dir = up_dir;
				moved = true;
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
		} else {
			if (Game.rand.nextInt(100) < 10) {
				Game.player.life -= Game.rand.nextInt(1,3);
				Game.player.isDamaged = true;
				if(push) {
					Game.player.applyKnockback(x, y); // Aplicar knockback ao jogador
				}
			}
			if (Game.player.life <= 0) {
				// gameover
			}
		}
		
		collidingProjectile();
		
		if(life <= 0) {
			destroySelf();
		}
	}
	
	public void destroySelf() {
		Game.entities.remove(this);
		return;
	}
	
	public void collidingProjectile() {
		for(int i = 0; i < Game.projectiles.size(); i += 1) {
			Entity e = Game.projectiles.get(i);
			if(e instanceof Projectile) {
				if(Entity.isColidding(this, e)) {
					life -= Projectile.damage;
					Game.projectiles.remove(e);
					return;
				}
			}
		}
	}

	public boolean isColiddingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskX, this.getY() + maskY, maskW, maskH);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);

		return enemyCurrent.intersects(player);
	}

	public boolean isColidding(int xNext, int yNext) {
		Rectangle enemyCurrent = new Rectangle(xNext + maskX, yNext + maskY, maskW, maskH);

		for (int i = 0; i < Game.enemies.size(); i += 1) {
			Enemy e = Game.enemies.get(i);
			if (e == this)
				continue;
			Rectangle targetEnemy = new Rectangle(e.getX() + maskX, e.getY() + maskY, maskW, maskH);
			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		return false;
	}

	public void render(Graphics g) {
		if (dir == right_dir) {
			g.drawImage(rightEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if (dir == left_dir) {
			g.drawImage(leftEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}

		if (dir == up_dir) {
			g.drawImage(upEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if (dir == down_dir) {
			g.drawImage(downEnemy[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}
}
