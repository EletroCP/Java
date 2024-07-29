package com.eletrocp.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.eletrocp.entidades.Player;
import com.eletrocp.main.Game;

public class UI {
    
    public void lifeBar(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(4, 4, 52, 7);
        g.setColor(Color.red);
        g.fillRect(5, 5, 50, 5);
        g.setColor(Color.green);
        g.fillRect(5, 5, (int)((Game.player.life / Game.player.maxLife) * 50), 5);
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 8));
        g.drawString((int)Game.player.life +"/"+(int)Game.player.maxLife, 58, 10);
    }
    
    public void manaBar(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(4, 13, 52, 7);
        g.setColor(Color.red);
        g.fillRect(5, 14, 50, 5);
        g.setColor(new Color(96, 103, 207));
        g.fillRect(5, 14, (int)((Player.mana / Player.maxMana) * 50), 5);
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 8));
        g.drawString((int)Player.mana+"/"+(int)Player.maxMana, 58, 20);
    }
    
    public void bossLifeBar(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(4, 150, 233, 7);
        g.setColor(Color.red);
        g.fillRect(5, 151, 231, 5);
        g.setColor(Color.green);
        g.fillRect(5, 151, (int)((Game.bossLife / (double)Game.bossMaxLife) * 231), 5);
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 9));
        g.drawString((int)Game.bossLife +"/"+(int)Game.bossMaxLife, 100, 149);
    }
    
    public void FPS(Graphics g) {
        String frames = String.valueOf(Game.FPS);
        g.setColor(Color.yellow);
        g.setFont(new Font("arial", Font.BOLD, 8));
        g.drawString(frames, 230, 8);
    }
    
    public void render(Graphics g)  {
        lifeBar(g);
        manaBar(g);
        FPS(g);
        if(Game.hasBoss) {
            bossLifeBar(g);
        }
    }

}
