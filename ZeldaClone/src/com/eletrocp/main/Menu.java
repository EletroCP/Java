package com.eletrocp.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Menu {
	
	public String[] options = {"Novo Jogo", "Salvar Jogo","Carregar Jogo", "Sair"};
	
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean up, down, enter;
	
	public void tick() {
		if(up) {
			up = false;
			currentOption -= 1;
			if(currentOption < 0) {
				currentOption = 0;
			}
		}
		
		if(down) {
			down = false;
			currentOption += 1;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}
		
		if(enter) {
			enter = false;
			if(options[currentOption] == "Novo Jogo") {
				Game.gameState = "NORMAL";
			}
		}
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.WIDTH*Game.SCALE);
		g.setColor(Color.white);
		//Nome
		g.setFont(new Font("arial", Font.BOLD, 36));
		g.drawString("RPG CLONE", (Game.WIDTH * Game.SCALE) / 2 - 100, (Game.HEIGHT * Game.SCALE) / 2 - 180);
		//Novo Jogo
		g.setFont(new Font("arial", Font.BOLD, 26));
		g.drawString("Novo Jogo", (Game.WIDTH * Game.SCALE) / 2 - 300, (Game.HEIGHT * Game.SCALE) / 2 - 50);
		//Salvar Jogo
		g.setFont(new Font("arial", Font.BOLD, 26));
		g.drawString("Salvar Jogo", (Game.WIDTH * Game.SCALE) / 2 - 300, (Game.HEIGHT * Game.SCALE) / 2 - 10);
		//Carregar Jogo
		g.setFont(new Font("arial", Font.BOLD, 26));
		g.drawString("Carregar Jogo", (Game.WIDTH * Game.SCALE) / 2 - 300, (Game.HEIGHT * Game.SCALE) / 2 + 30);
		//Sair
		g.setFont(new Font("arial", Font.BOLD, 26));
		g.drawString("Sair", (Game.WIDTH * Game.SCALE) / 2 - 300, (Game.HEIGHT * Game.SCALE) / 2 + 70);
		
		switch(options[currentOption]) {
		case "Salvar Jogo": g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 330, (Game.HEIGHT * Game.SCALE) / 2 - 10);
			break;
        case "Carregar Jogo": g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 330, (Game.HEIGHT * Game.SCALE) / 2 + 30);
        	break;
        case "Sair": g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 330, (Game.HEIGHT * Game.SCALE) / 2 + 70);
    		break;
        default: g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 330, (Game.HEIGHT * Game.SCALE) / 2 - 50);
    		break;
		}
	}
}
