package com.eletrocp.entidades;

import java.awt.image.BufferedImage;

public class Lifekit extends Entity{
	static int mountHealing = 10;

	public Lifekit(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
	
	public static int heal(double life, double maxLife) {
		
		if(mountHealing + life > maxLife) {
			return 100;
		}
		return (int) (life + mountHealing);
	}
	

}
