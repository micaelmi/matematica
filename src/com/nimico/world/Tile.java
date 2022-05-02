package com.nimico.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.nimico.main.Game;

public class Tile {

	public static BufferedImage CHAO = Game.spritesheet.getSprite(0, 16, 16, 16);
	public static BufferedImage PAREDE = Game.spritesheet.getSprite(16, 16, 16, 16);
	public static BufferedImage GRAMA = Game.spritesheet.getSprite(32, 16, 16, 16);
	public static BufferedImage ARBUSTO = Game.spritesheet.getSprite(48, 16, 16, 16);
	public static BufferedImage AGUA = Game.spritesheet.getSprite(64, 16, 16, 16);
	public static BufferedImage TIJOLO = Game.spritesheet.getSprite(80, 16, 16, 16);
	public static BufferedImage TABUA = Game.spritesheet.getSprite(96, 16, 16, 16);
	public static BufferedImage ARVORE1 = Game.spritesheet.getSprite(112, 16, 16, 16);
	public static BufferedImage ARVORE2 = Game.spritesheet.getSprite(128, 16, 16, 16);
	public static BufferedImage ARVORE3 = Game.spritesheet.getSprite(144, 16, 16, 16);
	
	public static BufferedImage Stars3 = Game.stars.getSprite(0, 0, 96, 32);
	public static BufferedImage Stars2 = Game.stars.getSprite(0, 32, 96, 32);
	public static BufferedImage Stars1 = Game.stars.getSprite(0, 64, 96, 32);
	public static BufferedImage Stars0 = Game.stars.getSprite(0, 96, 96, 32);
	
	public static BufferedImage Map = Game.minimap.getSprite(0, 0, 121, 121);
	
	
	
	private BufferedImage sprite;
	private int x,y;
	
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
	
}
