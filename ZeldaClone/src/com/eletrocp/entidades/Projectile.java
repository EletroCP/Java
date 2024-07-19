package com.eletrocp.entidades;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.eletrocp.main.Game;
import com.eletrocp.world.Camera;

public class Projectile extends Entity {
    
    private BufferedImage[] cepterProjectile;
    private int dx;
    private int dy;
    private int life = 25;
    private int curLife = 0;
    
    private double spd = 3;
    
    public Projectile(double x, double y, int width, int height, BufferedImage sprite, int dx, int dy) {
        super(x, y, width, height, sprite);
        
        this.dx = dx;
        this.dy = dy;
        
        cepterProjectile = new BufferedImage[1];
        
        switch(Game.player.dir) {
            case 0:
                cepterProjectile[0] = Game.spritesheet.getSprite(128, 80, 16, 16);
                break;
            case 1:
                cepterProjectile[0] = Game.spritesheet.getSprite(144, 80, 16, 16);
                break;
            case 2:
                cepterProjectile[0] = Game.spritesheet.getSprite(96, 80, 16, 16);
                break;
            case 3:
                cepterProjectile[0] = Game.spritesheet.getSprite(112, 80, 16, 16);
                break;
            default:
                cepterProjectile[0] = Game.spritesheet.getSprite(128, 80, 16, 16);
                dx = 1;
                dy = 0;
                break;
        }
    }

    public void tick() {
        x += dx * spd;
        y += dy * spd;
        curLife +=1;
        if(curLife == life) {
        	Game.projectiles.remove(this);
        }
    }
    
    public void render(Graphics g) {
        g.drawImage(cepterProjectile[0], (int) x - Camera.x, (int) y - Camera.y, null);
    }
}
