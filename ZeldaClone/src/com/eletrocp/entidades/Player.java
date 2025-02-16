package com.eletrocp.entidades;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.eletrocp.main.Game;
import com.eletrocp.world.Camera;
import com.eletrocp.world.World;

public class Player extends Entity {

    private int frames = 0, maxFrames = 5;
    private int index = 0;
    private int damageFrames = 0, maxDamageFrames = 20;
    private int knockbackFrames = 0, maxKnockbackFrames = 25;
    public static int weaponID = 0;
    public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
    public int dir = 0;
    private int attackCooldown = 20;
    private int currentAttackCooldown = 0;

    private double knockbackDistance = 60;
    private double knockbackX = 0, knockbackY = 0;
    public double spd = 1.2;
    public double life = 100, maxLife = 100;
    public static double mana = 100;
	public static double maxMana = 100;
    

    private boolean moved = false;
    public boolean hasWeapon = false;
    public static boolean isDamaged = false;
    public boolean right, left, up, down;
    public static boolean knockback = false;
    public static boolean hasAtack = false;
    public boolean jump = false;
    public boolean jumpUp = false, jumpDown = false;
    public static int z = 0;
    public int jumpFrames = 50, jumpCur = 0;
    public boolean isjumping = false;
    
    private boolean attacking = false;
    private int attackFrames = 0;
    private int maxAttackFrames = 10;
    
    private BufferedImage cetro;
    private BufferedImage cetroRotatedLeft;
    private BufferedImage cetroRotatedRight;
    
    private BufferedImage sword;
    private BufferedImage swordRotatedLeft;
    private BufferedImage swordRotatedRight;

    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;
    private BufferedImage[] upPlayer;
    private BufferedImage[] downPlayer;
    private BufferedImage[] knockbackPlayer;
    private BufferedImage[] playerDamage;
    
    private BufferedImage[] playerWithSword;
    private BufferedImage[] playerWithCepter;
    private BufferedImage[] rightPlayerSword;
    private BufferedImage[] leftPlayerSword;
    private BufferedImage[] rightPlayerCepter;
    private BufferedImage[] leftPlayerCepter;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        rightPlayer = new BufferedImage[2];
        leftPlayer = new BufferedImage[2];
        upPlayer = new BufferedImage[2];
        downPlayer = new BufferedImage[2];
        knockbackPlayer = new BufferedImage[2];
        playerDamage = new BufferedImage[4];     
        
        playerWithSword = new BufferedImage[1];
        playerWithCepter = new BufferedImage[1];

        playerWithSword[0] = Game.spritesheet.getSprite(16 + (0 * 16), 80, 16, 16);
        playerWithCepter[0] = Game.spritesheet.getSprite(64 + (0 * 16), 80, 16, 16);
        
        rightPlayerSword = new BufferedImage[2];
        leftPlayerSword = new BufferedImage[2];
        rightPlayerCepter = new BufferedImage[2];
        leftPlayerCepter = new BufferedImage[2];
        
        cetro = Game.spritesheet.getSprite(64, 80, 16, 16);
        cetroRotatedLeft = Game.spritesheet.getSprite(80, 96, 16, 16);
        cetroRotatedRight = Game.spritesheet.getSprite(80, 80, 16, 16);
        
        sword = Game.spritesheet.getSprite(16, 80, 16, 16);
        swordRotatedLeft = Game.spritesheet.getSprite(32, 96, 16, 16);
        swordRotatedRight = Game.spritesheet.getSprite(32, 80, 16, 16);

        for (int i = 0; i < 2; i++) {
            rightPlayer[i] = Game.spritesheet.getSprite(64 + (i * 16), 48, 16, 16);
            leftPlayer[i] = Game.spritesheet.getSprite(48 - (i * 16), 48, 16, 16);
            upPlayer[i] = Game.spritesheet.getSprite(96 + (i * 16), 48, 16, 16);
            downPlayer[i] = Game.spritesheet.getSprite(0 + (i * 16), 48, 16, 16);
            knockbackPlayer[i] = Game.spritesheet.getSprite(48 + (i * 16), 48, 16, 16);
        }

        for (int i = 0; i < 4; i++) {
            playerDamage[i] = Game.spritesheet.getSprite(0 + (i * 16), 64, 16, 16);
        }
        

        for (int i = 0; i < 1; i++) {
            rightPlayerSword[i] = Game.spritesheet.getSprite(32 + (i * 16), 80, 16, 16);
            leftPlayerSword[i] = Game.spritesheet.getSprite(32 - (i * 16), 96, 16, 16);
            rightPlayerCepter[i] = Game.spritesheet.getSprite(80 + (i * 16), 80, 16, 16);
            leftPlayerCepter[i] = Game.spritesheet.getSprite(80 - (i * 16), 96, 16, 16);
        }
    }

    public void tick() {
    	
    	if (!isjumping && jump) {
	    		jump = false; // Reseta o estado de pulo
	    		isjumping = true; // Inicia o pulo
	    		jumpUp = true; // Define direção para cima
	    		jumpDown = false; // Garante que não está descendo
	    		jumpCur = 0; // Inicializa a posição atual do pulo
    		}

    		if (isjumping) {
	    		if (jumpUp) {
		    		jumpCur += 2; // Sobe no pulo
		    		if (jumpCur >= jumpFrames) { // Altura máxima atingida
			    		jumpUp = false;
			    		jumpDown = true;
		    		}
	    		} else if (jumpDown) {
		    		jumpCur -= 2; // Desce no pulo
		    		if (jumpCur <= 0) { // Volta ao chão
			    		isjumping = false; // Termina o pulo
			    		jumpDown = false; // Reseta estado de descida
			    		jumpCur = 0; // Garante que está no chão
		    		}
	    		}

	    		z = jumpCur; // Atualiza a posição vertical
    		}

    		
    	
        if (knockback) {
            handleKnockback();
        } else {
            moved = false;
            handleMovement();
        }

        checkCollisionLifePack();
        checkCollisionManaPotion();
        checkCollisionSword();
        checkCollisionSepter();
        manaRegen();

        Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
        Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);

        if (isDamaged) {
            handleDamageAnimation();
        }

        if (life <= 0) {
        	Game.gameState = "GAMEOVER";
        	Game.entities.remove(this);
        }

        if (currentAttackCooldown > 0) {
            currentAttackCooldown--;
        }

        if (hasAtack && hasWeapon && currentAttackCooldown <= 0) {
            hasAtack = false;
            currentAttackCooldown = attackCooldown;

            if (weaponID == 2 && mana >= 10) {
                mana -= 10;
                attacking = true;
                attackFrames = 0;
                
                int lifeTime = 25;
                int dx = 0;
                int dy = 0;
                int px = 0;
                int py = 0;
                int damage = 3;

                if (dir == right_dir) {
                    dx = 1;
                    dy = 0;
                    px = 20;
                } else if (dir == left_dir) {
                    dx = -1;
                    dy = 0;
                    px = -20;
                } else if (dir == up_dir) {
                    dy = -1;
                    dx = 0;
                    py = -15;
                } else {
                    dy = +1;
                    dx = 0;
                    py = 15;
                }

                int projectileWidth = 12;
                int projectileHeight = 12;

                Projectile projectile = new Projectile(this.getX() + px, this.getY() + py, projectileWidth, projectileHeight, null, dx, dy, lifeTime, damage, weaponID);
                Game.projectiles.add(projectile);
            } else if (weaponID == 1) {
            	attacking = true;
                attackFrames = 0;
                
                int lifeTime = 5;
                int dx = 0;
                int dy = 0;
                int px = 0;
                int py = 0;
                
                int projectileWidth = 20;
                int projectileHeight = 15;
                int damage = 7;
                
                if (dir == right_dir) {
                    dx = 0;
                    dy = 0;
                    px = 20;
                } else if (dir == left_dir) {
                    dx = 0;
                    dy = 0;
                    px = -20;
                } else if (dir == up_dir) {
                    dy = 0;
                    dx = 0;
                    py = -15;
                } else {
                    dy = 0;
                    dx = 0;
                    py = 15;
                }
                Projectile projectile = new Projectile(this.getX() + px, this.getY() + py, projectileWidth, projectileHeight, null, dx, dy, lifeTime, damage, weaponID);
                Game.projectiles.add(projectile);
                
            }
        }
    }

    private void handleKnockback() {
        knockbackFrames++;
        x += knockbackX;
        y += knockbackY;
        if (knockbackFrames >= maxKnockbackFrames) {
            knockback = false;
            knockbackFrames = 0;
            knockbackX = 0;
            knockbackY = 0;
        }
        animate(knockbackPlayer);
    }

    private void handleMovement() {
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
            animate(getCurrentAnimation());
        }
    }

    private BufferedImage[] getCurrentAnimation() {
        switch (dir) {
            case 0: return rightPlayer;
            case 1: return leftPlayer;
            case 2: return upPlayer;
            case 3: return downPlayer;
            default: return downPlayer;
        }
    }
    
    private int weaponPositionX() {
        switch (weaponID) {
            case 1:
                switch (dir) {
                    case 0: return this.getX() - Camera.x + 1;
                    case 1: return this.getX() - Camera.x + 1;
                    case 2: return this.getX() - Camera.x - 4;
                    case 3: return this.getX() - Camera.x + 6;
                }
            case 2:
                switch (dir) {
                    case 0: return this.getX() - Camera.x - 1;
                    case 1: return this.getX() - Camera.x - 1;
                    case 2: return this.getX() - Camera.x - 4;
                    case 3: return this.getX() - Camera.x + 4;
                }
            default: return 1;
        }
    }

    private int weaponPositionY() {
        switch (weaponID) {
            case 1:
                switch (dir) {
                    case 0: return this.getY() - (Camera.y + z) - 4;
                    case 1: return this.getY() - (Camera.y + z)  - 3;
                    case 2: return this.getY() - (Camera.y + z)  - 4;
                    case 3: return this.getY() - (Camera.y + z)  - 4;
                }
            case 2:
                switch (dir) {
                    case 0: return this.getY() - (Camera.y + z)  - 2;
                    case 1: return this.getY() - (Camera.y + z)  - 2;
                    case 2: return this.getY() - (Camera.y + z)  - 3;
                    case 3: return this.getY() - (Camera.y + z)  - 3;
                }
            default: return 1;
        }
    }
    
    private BufferedImage[] getCurrentAnimationWithWeapon() {
        switch (weaponID) {
            case 1:
                switch (dir) {
                    case 0: return playerWithSword;
                    case 1: return playerWithSword;
                    case 2: return playerWithSword;
                    case 3: return playerWithSword;
                }
            case 2:
                switch (dir) {
                    case 0: return playerWithCepter;
                    case 1: return playerWithCepter;
                    case 2: return playerWithCepter;
                    case 3: return playerWithCepter;
                }
            default: return getCurrentAnimation();
        }
    }

    private void animate(BufferedImage[] animationFrames) {
        frames++;
        if (frames == maxFrames) {
            frames = 0;
            index++;
            if (index >= animationFrames.length) {
                index = 0;
            }
        }
    }

    private void handleDamageAnimation() {
        damageFrames++;
        if (damageFrames >= maxDamageFrames) {
            isDamaged = false;
            damageFrames = 0;
            index = 0;
        }

        frames++;
        if (frames >= maxFrames) {
            frames = 0;
            index++;
            if (index >= playerDamage.length) {
                index = 0;
            }
        }
    }

    public void manaRegen() {
        if (mana < maxMana) {
           mana += 0.015;
        }
    }

    public void checkCollisionLifePack() {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity atual = Game.entities.get(i);
            if (atual instanceof Lifekit) {
                if (Entity.isColidding(this, atual) && life < maxLife) {
                    life = Lifekit.heal(life, maxLife);
                    Game.entities.remove(i);
                }
            }
        }
    }

    public void checkCollisionManaPotion() {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity atual = Game.entities.get(i);
            if (atual instanceof ManaPotion) {
                if (Entity.isColidding(this, atual) && mana < maxMana) {
                    mana = Lifekit.heal(mana, maxMana);
                    Game.entities.remove(i);
                }
            }
        }
    }
    
    public void checkCollisionSword() {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity atual = Game.entities.get(i);
            if(!Game.inventory.contains(1)) {            	
            	if (atual instanceof Sword) {
            		if(Entity.isColidding(this, atual)) {
            			Game.entities.remove(i);
            			hasWeapon = true;
            			weaponID = 1;
            			Game.inventory.add(weaponID);
            		}
            	}
            }
        }
    }
    
    public void checkCollisionSepter() {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity atual = Game.entities.get(i);
            if(!Game.inventory.contains(2)) {            	
            	if (atual instanceof Septer) {
            		if(Entity.isColidding(this, atual)) {
            			Game.entities.remove(i);
            			hasWeapon = true;
            			weaponID = 2;
            			Game.inventory.add(weaponID);
            		}
            	}
            }
        }
    }

    public void render(Graphics g) {
    	int animationIndex = index % getCurrentAnimation().length;
    	g.drawImage(getCurrentAnimation()[animationIndex], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
    	if (hasWeapon) {
    	    if (!attacking) {
    	        int weaponIndex = index % getCurrentAnimationWithWeapon().length;
    	        g.drawImage(getCurrentAnimationWithWeapon()[weaponIndex], weaponPositionX(), weaponPositionY(), null);
    	    } else {
    	        if (weaponID == 1) {
    	            int weaponX = weaponPositionX();
    	            int weaponY = weaponPositionY();
    	            BufferedImage swordImage = sword;
    	            if (dir == right_dir) {
    	                weaponX += 6;
    	                weaponY += 5;
    	                swordImage = swordRotatedRight;
    	            } else if (dir == left_dir) {
    	                weaponX -= 6;
    	                weaponY += 4;
    	                swordImage = swordRotatedLeft;
    	            } else if (dir == up_dir) {
    	                weaponY -= 6;
    	            } else if (dir == down_dir) {
    	                weaponY += 3;
    	            }

    	            g.drawImage(swordImage, weaponX, weaponY, null);

    	            attackFrames++;
    	            if (attackFrames >= maxAttackFrames) {
    	                attacking = false;
    	            }
    	        } else if (weaponID == 2) {
    	            int weaponX = weaponPositionX();
    	            int weaponY = weaponPositionY();
    	            BufferedImage cetroImage = cetro;

    	            if (dir == right_dir) {
    	                weaponX += 6;
    	                weaponY += 5;
    	                cetroImage = cetroRotatedRight;
    	            } else if (dir == left_dir) {
    	                weaponX -= 6;
    	                weaponY += 4;
    	                cetroImage = cetroRotatedLeft;
    	            } else if (dir == up_dir) {
    	                weaponY -= 6;
    	            } else if (dir == down_dir) {
    	                weaponY += 3;
    	            }

    	            g.drawImage(cetroImage, weaponX, weaponY, null);

    	            attackFrames++;
    	            if (attackFrames >= maxAttackFrames) {
    	                attacking = false;
    	            }
    	        }
    	    }
    	}
    	
    	if (isDamaged) {
    		if(knockback) {
    			int knockbackIndex = index % knockbackPlayer.length;
    			g.drawImage(knockbackPlayer[knockbackIndex], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
    		} else {
				int damageIndex = index % playerDamage.length;
				g.drawImage(playerDamage[damageIndex], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
    		}
		}
    	
    	if(isjumping) {
    		g.setColor(Color.black);
    		g.fillOval(this.getX() - Camera.x + 2, this.getY() - Camera.y + 14, 14, 6);
    	}
    }

    public void applyKnockback(double enemyX, double enemyY) {
        double angle = Math.atan2(y - enemyY, x - enemyX);
        knockbackX = Math.cos(angle) * (knockbackDistance / maxKnockbackFrames);
        knockbackY = Math.sin(angle) * (knockbackDistance / maxKnockbackFrames);
        knockback = true;
        frames = 0;
        index = 0;
    }
}