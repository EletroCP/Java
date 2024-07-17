package com.eletrocp.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.eletrocp.entidades.Player;

public class UI {
	
	public void lifeBar(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(4, 4, 52, 7);
		g.setColor(Color.red);
		g.fillRect(5, 5, 50, 5);
		g.setColor(Color.green);
		g.fillRect(5, 5, (int)((Player.life / Player.maxLife) * 50), 5);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 8));
		g.drawString((int)Player.life+"/"+(int)Player.maxLife, 58, 10);
	}
	
	public void render(Graphics g)  {
		lifeBar(g);
		
	}

}
