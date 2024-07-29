package com.eletrocp.entidades;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.eletrocp.main.Game;
import com.eletrocp.world.Camera;
import com.eletrocp.world.World;

public class Javali extends Enemy {
    
    private boolean moved = false;
    private boolean push = true;
    private boolean canRun = true;
    public int life;
    public int maxLife;
    
    protected int maskX;
    protected int maskY;
    protected int maskW;
    protected int maskH;
    
    private int frames = 0, maxFrames = 5, index = 0, maxIndex = 1;
    private double spd = 0;
    private static final double RUN_DISTANCE = 60;
    
    private static int cooldown = 10*60, curCooldown = 0;
    
    private BufferedImage javali = Game.spritesheet.getSprite(0, 144, 64, 41);

    public Javali(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
        
        this.life = 100;
        this.maxLife = 100;
        this.maskX = 0;
        this.maskY = 0;
        this.maskW = width;
        this.maskH = height;
        
        Game.hasBoss = true;

    }
    
    public void collidingProjectile() {
        for(int i = 0; i < Game.projectiles.size(); i += 1) {
            Entity e = Game.projectiles.get(i);
            if(e instanceof Projectile) {
                if(Entity.isColidding(this, e)) {
                    isDamaged = true;
                    life -= Projectile.damage;
                    Game.projectiles.remove(e);
                    return;
                }
            }
        }
    }
    
    public void destroySelf() {
        Game.enemies.remove(this);
        Game.entities.remove(this);
        Game.hasBoss = false;
        return;
    }
    
    public void checkCooldown() {
        if (!canRun) {
            curCooldown++;
            if (curCooldown >= cooldown) {
                canRun = true;
                curCooldown = 0;
            }
        }
    }
    
    public void startRunning() {
        double playerX = Game.player.x;
        double playerY = Game.player.y;
        double javaliX = this.x;
        double javaliY = this.y;
        
        double distanceX = playerX - javaliX;
        double distanceY = playerY - javaliY;

        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
        if (distance < RUN_DISTANCE && canRun) {
            this.spd = 1.5;
        } else {
            this.spd = 0;
        }
    }
    
    public void tick() {
        moved = false;
        System.out.println(spd);
        System.out.println(canRun);
        
        if (!isColiddingWithPlayer()) {
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
                    Game.player.applyKnockback(x, y);
                }
                if (spd > 0.3) {
                    canRun = false;
                }
            }
            if (Game.player.life <= 0) {
            
            }
        }
        
        if(life <= 0) {
            destroySelf();
        }
        
        collidingProjectile();
        checkCooldown();
        startRunning();
        
        Game.bossLife = this.life;
        Game.bossMaxLife = this.maxLife;
    }
    
    public void render(Graphics g) {
        g.drawImage(javali, this.getX() - Camera.x, this.getY() - Camera.y, null);
        
        g.setColor(Color.red);
        g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskW, maskH);
    }
}
