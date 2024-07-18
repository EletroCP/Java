package com.eletrocp.entidades;

import java.awt.image.BufferedImage;

public class ManaPotion extends Entity{
	static int mountMana = 10;

	public ManaPotion(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
	
	public static int heal(double mana, double maxMana) {
		
		if(mountMana + mana > maxMana) {
			return 100;
		}
		return (int) (mana + mountMana);
	}
	

}
