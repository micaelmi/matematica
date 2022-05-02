package com.nimico.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.nimico.main.Game;
import com.nimico.world.Camera;

public class Entity {
	
	///////// IMAGENS DOS ITENS E NPCs /////////
	public static BufferedImage IDOSA_L = Game.spritesheet.getSprite(16*4, 0, 16, 16);
	public static BufferedImage IDOSA_R = Game.spritesheet.getSprite(16*5, 0, 16, 16);
	public static BufferedImage MOCO_R = Game.spritesheet.getSprite(16*6, 0, 16, 16);
	public static BufferedImage MOCO_L = Game.spritesheet.getSprite(16*7, 0, 16, 16);
	public static BufferedImage MOCA_R = Game.spritesheet.getSprite(16*8, 0, 16, 16);
	public static BufferedImage MOCA_L = Game.spritesheet.getSprite(16*9, 0, 16, 16);
	public static BufferedImage IDOSO_R = Game.spritesheet.getSprite(16*10, 0, 16, 16);
	public static BufferedImage IDOSO_L = Game.spritesheet.getSprite(16*11, 0, 16, 16);
	public static BufferedImage CRIANCA_R = Game.spritesheet.getSprite(16*12, 0, 16, 16);
	public static BufferedImage CRIANCA_L = Game.spritesheet.getSprite(16*13, 0, 16, 16);
	public static BufferedImage COMERCIANTE = Game.spritesheet.getSprite(16*14, 0, 16, 16);
	
	public static BufferedImage PRODUTO1 = Game.spritesheet.getSprite(16*10, 16, 16, 16);
	public static BufferedImage PRODUTO2 = Game.spritesheet.getSprite(16*11, 16, 16, 16);
	public static BufferedImage PRODUTO3 = Game.spritesheet.getSprite(16*12, 16, 16, 16);
	public static BufferedImage PRODUTO4 = Game.spritesheet.getSprite(16*13, 16, 16, 16);
	public static BufferedImage PRODUTO5 = Game.spritesheet.getSprite(16*14, 16, 16, 16);
	public static BufferedImage TEMPO = Game.spritesheet.getSprite(16*15, 16, 16, 16);
	
	///////// TAMANHO E POSIÇÃO /////////
	protected int width, height, z;
	protected double x, y;
	
	private BufferedImage sprite;
	
	private int maskx,masky,mwidth,mheight;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}
	
	public void setMask(int maskx, int masky, int mwidth, int mheight) {
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getWidth() {
		return (int)this.width;
	}
	
	public int getHeight() {
		return (int)this.height;
	}
	
	public void tick() {
		
	}
	
	public double calculateDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1-x2) * (x1-x2) + (y1-y2) * (y1-y2));
	}
	
	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.mwidth, e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.mwidth, e2.mheight);
		if(e1Mask.intersects(e2Mask) && e1.z == e2.z) {
			return true;
		}
		return false;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
	
	
	
}
