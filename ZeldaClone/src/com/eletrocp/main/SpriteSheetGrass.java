package com.eletrocp.main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheetGrass {
	
	private BufferedImage grassSheet;
	
	public SpriteSheetGrass(String path) {
		try {
			grassSheet = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getSprite(int x, int y, int width, int height) {
		BufferedImage sprite = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = sprite.getGraphics();
		g.drawImage(grassSheet.getSubimage(x, y, width, height), 0, 0, null);
		g.dispose();
		return sprite;
	}
	
}
