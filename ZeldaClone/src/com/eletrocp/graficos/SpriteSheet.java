package com.eletrocp.graficos;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	
	private BufferedImage sheet;
	
	public SpriteSheet(String path) {
		try {
			sheet = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getSprite(int x, int y, int width, int height) {
		// Certifique-se de que a subimagem preserva a transparência
		BufferedImage sprite = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = sprite.getGraphics();
		g.drawImage(sheet.getSubimage(x, y, width, height), 0, 0, null);
		g.dispose();
		return sprite;
	}
}
