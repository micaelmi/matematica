package com.nimico.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.nimico.main.Game;
import com.nimico.main.Sound;
import com.nimico.world.Camera;
import com.nimico.world.World;

public class Player extends Entity {
	
	public boolean right,up,left,down;
	public boolean buy = false, sell = false, alreadySold = false;
	
	public double life = 20, maxLife = 20;
	public double speed = 1.5;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	
	private int frames = 0, maxFrames = 15, index = 0, maxIndex = 1;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	public int receita = Game.receita;
	public int custo = Game.custo;
	public int saldo = Game.saldo;
	public int flores = Game.flores;
	public int vendas = Game.vendas;
	
	public boolean isDamaged = false;
	
	
	public int mx,my;
	
	public int damageFrames = 0;
	
	public boolean jump = false, isJumping = false, Rising = false, Falling = false;
	
	public int z = 0;
	
	public int jumpFrames = 20, jumpCur = 0, jumpSpeed = 2;
	
	public int sellFrames = 60, sellCur = 0;
	public boolean isSelling;
	

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		// ANIMAÇÃO DO JOGADOR
		rightPlayer = new BufferedImage[2];
		leftPlayer = new BufferedImage[2];
		
		leftPlayer[0] = Game.spritesheet.getSprite(0*16, 0, 16, 16);
		leftPlayer[1] = Game.spritesheet.getSprite(1*16, 0, 16, 16);
		rightPlayer[0] = Game.spritesheet.getSprite(2*16, 0, 16, 16);
		rightPlayer[1] = Game.spritesheet.getSprite(3*16, 0, 16, 16);
		
	}
	
	///////// MOVIMENTAÇÃO E COLISÃO /////////       TICK
	public void tick() {
		
		if(jump) {
			if(isJumping == false) {
				jump = false;
				isJumping  = true;
				Rising = true;
			}
		}
		
		if(isJumping == true) {
				if(Rising) {
					jumpCur+=2;
				} else if(Falling) {
					jumpCur-=2;
					if(jumpCur <= 0) {
						isJumping = false;
						Rising = false;
						Falling = false;
					}
				}
				z = jumpCur;
				if(jumpCur >= jumpFrames ) {
					Rising = false;
					Falling = true;
				}
		}
		
		if(right && World.isFree((int)(x+speed),this.getY(), z)) {
			dir = right_dir;
			x+=speed;
		}
		else if(left && World.isFree((int)(x-speed),this.getY(), z)) {
			dir = left_dir;
			x-=speed;
		}
		if(up && World.isFree(this.getX(),(int)(y-speed), z)) {
			y-=speed;
		}
		else if(down && World.isFree(this.getX(),(int)(y+speed), z)) {
			y+=speed;
		}
		
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
			
			checkVenda();
			checkCompra();
			
			
			if(life <= 0) {
				Game.gameState = "GAME_OVER";
			}
			
			///////// CAMERA SEGUINDO O JOGADOR /////////
			Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH) ;
			Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT) ;
			
	}
	
	public void checkCompra() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Comerciante) {
				if(Entity.isColliding(this, e)) {
					if(buy == true && saldo >=10) {
						Sound.cash.play();
						double random = (Math.random() * (12 - 8)) + 8;
						buy = false;
						flores++;
						saldo-=random;
						custo+=random;
					return;
					}
				}
			}
		}
	}
	
	
	public void checkVenda() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Pessoa) {
				if(Entity.isColliding(this, e)) {
					if(sell == true && flores > 0 && isSelling == false) {
						Sound.handing.play();
						isSelling = true;
						double random = (Math.random() * (35 - 5)) + 5;
						sell = false;
						receita += random;
						saldo += random;
						flores--;
						vendas++;
						return;
					}
					// DELAY ENTRE UMA VENDA E OUTRA
					sellCur++;
					if(sellCur >= sellFrames) {
						sellCur = 0;
						isSelling = false;
					}
				}
			}
		}
	}
	
	
	///////// RENDERIZAÇÃO DO JOGADOR COM A CÂMERA /////////
	public void render(Graphics g) {
		
		if(dir == right_dir) {
			g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
		} else if(dir == left_dir) {
			g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
		}
		
		if(isJumping) {
			g.setColor(Color.black);
			g.fillOval(this.getX() - Camera.x, this.getY() + 24 - Camera.y, 22, 8);
		}

	}
}
