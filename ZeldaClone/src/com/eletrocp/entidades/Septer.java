package com.eletrocp.entidades;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Septer extends Entity{

	
	public Septer(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		this.maskW = 4;
		this.maskH = 16;
		this.maskX = 6;
	}
	
	public static boolean isColidding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskX, e1.getY() + e1.maskY, e1.maskW, e1.maskH);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskX, e2.getY() + e2.maskY, e2.maskW, e2.maskH);
		
		return e1Mask.intersects(e2Mask);
	}	
}
