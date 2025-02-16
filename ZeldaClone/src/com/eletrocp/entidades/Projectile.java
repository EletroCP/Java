package com.eletrocp.entidades;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.eletrocp.main.Game;
import com.eletrocp.world.Camera;

public class Projectile extends Entity {
    
    private BufferedImage[] cepterProjectile;
    private BufferedImage[] swordProjectile;
    private int dx;
    private int dy;
    private int life;
    private int curLife = 0;
    static int damage;
    
    private double spd = 3;
    
    public Projectile(double x, double y, int width, int height, BufferedImage sprite, int dx, int dy, int life, int damage,int weaponID) {
        super(x, y, width, height, sprite);
        
        this.width = width;
        this.height = height;
        this.life = life;
        Projectile.damage = damage;
        
        
        this.dx = dx;
        this.dy = dy;
        
        cepterProjectile = new BufferedImage[1];
        swordProjectile = new BufferedImage[1];
        
        switch(weaponID) {
	        case 1:
		        switch(Game.player.dir) {
		        case 0:
		        	swordProjectile[0] = Game.spritesheet.getSprite(176, 80, 16, 16);
		        	break;
		        case 1:
		        	swordProjectile[0] = Game.spritesheet.getSprite(208, 80, 16, 16);
		        	break;
		        case 2:
		        	swordProjectile[0] = Game.spritesheet.getSprite(160, 80, 16, 16);
		        	break;
		        case 3:
		        	swordProjectile[0] = Game.spritesheet.getSprite(192, 80, 16, 16);
		        	break;
		        default:
		        	break;
		        }
	        case 2:
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
		        	break;
		        }    
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
    	if(Player.weaponID == 1){
    		g.drawImage(swordProjectile[0], (int) x - Camera.x, (int) y - Camera.y, width, height, null);
    	} else if (Player.weaponID == 2) {    		
    		g.drawImage(cepterProjectile[0], (int) x - Camera.x, (int) y - Camera.y, width, height, null);
    	}
    }
}
