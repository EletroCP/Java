package com.eletrocp.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Menu {
	
	public String[] options = {"Novo Jogo", "Salvar Jogo","Carregar Jogo", "Configurações", "Sair"};
	public String[] configuracoes = {"Video", "Audio", "Voltar"};
	public String menuState = "NORMAL";
	public int currentOption = 0;
	public int maxOption = updateMaxOption(options);
	
	public boolean up, down, enter;
	
	public boolean pause = false;
	
	public int updateMaxOption(String[] array) {
		int currentMaxOption = array.length - 1;
		return currentMaxOption;
	}
	
	public void tick() {
		if(up) {
			up = false;
			currentOption -= 1;
			if(currentOption < 0) {
				currentOption = maxOption;
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
			if(options[currentOption] == "Novo Jogo" || options[currentOption] == "Continuar") {
				Game.gameState = "NORMAL";
				pause = false;
			}  else if(options[currentOption] == "Configurações") {
				menuState = "CONFIGURACOES";
				currentOption = 0;
				maxOption = updateMaxOption(configuracoes);
			}  else if(configuracoes[currentOption] == "Voltar") {
				menuState = "NORMAL";
				currentOption = 0;
				maxOption = updateMaxOption(options);
			}
				else if(options[currentOption] == "Sair") {
				System.exit(0);
			}
		}
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.WIDTH, Game.WIDTH);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 36));
		
		g.drawString("RPG CLONE", (Game.WIDTH) / 2 - 100, (Game.HEIGHT) / 2 - 180);		
		switch(menuState) {
		case "CONFIGURACOES":
			g.drawString("Video", (Game.WIDTH) / 2 - 300, (Game.HEIGHT) / 2 - 50);
			
			g.drawString("Audio", (Game.WIDTH) / 2 - 300, (Game.HEIGHT) / 2 - 6);
			
			g.drawString("Voltar", (Game.WIDTH) / 2 - 300, (Game.HEIGHT) / 2 + 36);
			
			switch(configuracoes[currentOption]) {
			case "Audio": g.drawString(">", (Game.WIDTH) / 2 - 330, (Game.HEIGHT) / 2 - 6);
				break;
			case "Voltar": g.drawString(">", (Game.WIDTH) / 2 - 330, (Game.HEIGHT) / 2 + 36);
				break;
			default: g.drawString(">", (Game.WIDTH) / 2 - 330, (Game.HEIGHT) / 2 - 50);	
    			break;
			}
		break;
		default:
			if(pause == false) {
				g.drawString("Novo Jogo", (Game.WIDTH) / 2 - 300, (Game.HEIGHT) / 2 - 50);			
			} else {
				g.drawString("Continuar", (Game.WIDTH) / 2 - 300, (Game.HEIGHT) / 2 - 50);
			}
			//Salvar Jogo
			g.drawString("Salvar Jogo", (Game.WIDTH) / 2 - 300, (Game.HEIGHT) / 2 - 6);
			//Carregar Jogo
			g.drawString("Carregar Jogo", (Game.WIDTH) / 2 - 300, (Game.HEIGHT) / 2 + 36);
			// Configurações
			g.drawString("Configurações", (Game.WIDTH) / 2 - 300, (Game.HEIGHT) / 2 + 78);
			//Sair
			g.drawString("Sair", (Game.WIDTH) / 2 - 300, (Game.HEIGHT) / 2 + 120);
			
			switch(options[currentOption]) {
			case "Salvar Jogo": g.drawString(">", (Game.WIDTH) / 2 - 330, (Game.HEIGHT) / 2 - 6);
				break;
	        case "Carregar Jogo": g.drawString(">", (Game.WIDTH) / 2 - 330, (Game.HEIGHT) / 2 + 36);
	        	break;
	        case "Configurações": g.drawString(">", (Game.WIDTH) / 2 - 330, (Game.HEIGHT) / 2 + 78);
	        	break;
	        case "Sair": g.drawString(">", (Game.WIDTH) / 2 - 330, (Game.HEIGHT) / 2 + 120);
	    		break;
	        default: g.drawString(">", (Game.WIDTH) / 2 - 330, (Game.HEIGHT) / 2 - 50);
	    		break;
			}
			break;
		}
		
	}
}
