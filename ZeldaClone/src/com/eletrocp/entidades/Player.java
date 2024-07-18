package com.eletrocp.entidades;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.eletrocp.main.Game;
import com.eletrocp.world.Camera;
import com.eletrocp.world.World;

public class Player extends Entity {

    // Variáveis de animação e estado
    private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
    public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
    public int dir = 0;
    public static boolean knockback = false;
    public static double life = 100, maxLife = 100;
    public static double mana = 100, maxMana = 100;
    public boolean isDamaged = false;
    private int damageFrames = 0, maxDamageFrames = 20; // Duração da animação de dano

    private int knockbackFrames = 0, maxKnockbackFrames = 25;
    private double knockbackDistance = 60;
    private double knockbackX = 0, knockbackY = 0;

    public double spd = 1.4;

    private boolean moved = false;
    public boolean right, left, up, down;

    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;
    private BufferedImage[] upPlayer;
    private BufferedImage[] downPlayer;
    private BufferedImage[] knockbackPlayer;
    private BufferedImage[] playerDamage;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        rightPlayer = new BufferedImage[2];
        leftPlayer = new BufferedImage[2];
        upPlayer = new BufferedImage[2];
        downPlayer = new BufferedImage[2];
        knockbackPlayer = new BufferedImage[2];
        playerDamage = new BufferedImage[4]; // Ajuste para 4 sprites de dano

        for (int i = 0; i < 2; i++) {
            rightPlayer[i] = Game.spritesheet.getSprite(64 + (i * 16), 48, 16, 16);
            leftPlayer[i] = Game.spritesheet.getSprite(48 - (i * 16), 48, 16, 16);
            upPlayer[i] = Game.spritesheet.getSprite(96 + (i * 16), 48, 16, 16);
            downPlayer[i] = Game.spritesheet.getSprite(0 + (i * 16), 48, 16, 16);
            knockbackPlayer[i] = Game.spritesheet.getSprite(48 + (i * 16), 48, 16, 16);
        }

        for (int i = 0; i < 4; i++) { // Ajuste para 4 sprites de dano
            playerDamage[i] = Game.spritesheet.getSprite(0 + (i * 16), 64, 16, 16);
        }
    }

    public void tick() {
        if (knockback) {
            handleKnockback();
        } else {
            moved = false;
            handleMovement();
        }

        checkCollisionLifePack();
        checkCollisionManaPotion();
        manaRegen();

        Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
        Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);

        if (isDamaged) {
            damageFrames++;
            
            if (damageFrames >= maxDamageFrames) {
                isDamaged = false;
                damageFrames = 0;
                index = 0; // Resetar o índice após a animação de dano
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

    public void render(Graphics g) {
        if (knockback) {
            g.drawImage(knockbackPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
        } else if (isDamaged) {
            int damageIndex = index % playerDamage.length; // Garantir que o índice esteja dentro dos limites
            g.drawImage(playerDamage[damageIndex], this.getX() - Camera.x, this.getY() - Camera.y, null);
        } else {
            g.drawImage(getCurrentAnimation()[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
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
