package com.nimico.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.nimico.entities.Comerciante;
import com.nimico.entities.Entity;
import com.nimico.entities.Flor;
import com.nimico.entities.Pessoa;
import com.nimico.entities.Player;
import com.nimico.entities.Vida;
import com.nimico.graficos.Spritesheet;
import com.nimico.main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH;
	public static int  HEIGHT;
	public static final int TILE_SIZE = 16;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			
			int[] pixels = new int[map.getWidth()*map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth()*map.getHeight()];
			
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			
			for(int xx = 0; xx < map.getWidth(); xx++) {
				for(int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];

					BufferedImage chao = Tile.CHAO;
					BufferedImage parede = Tile.PAREDE;
					BufferedImage grama = Tile.GRAMA;
					BufferedImage arbusto = Tile.ARBUSTO;
					BufferedImage agua = Tile.AGUA;
					BufferedImage tijolo = Tile.TIJOLO;
					BufferedImage tabua = Tile.TABUA;
					BufferedImage arvore1 = Tile.ARVORE1;
					BufferedImage arvore2 = Tile.ARVORE2;
					BufferedImage arvore3 = Tile.ARVORE3;
					
					
					//RENDERIZA PRIMEIRO O CHÃO COMO PADRÃO 
					tiles[xx+ (yy*WIDTH)] = new FloorTile(xx*16,yy*16,grama);
					
					if(pixelAtual == 0xFF69645B) {
						//PAREDE
						tiles[xx+ (yy*WIDTH)] = new WallTile(xx*16,yy*16,parede);
						
					} 
					else if(pixelAtual == 0xFF0C3106) {
						//ARBUSTO
						tiles[xx+ (yy*WIDTH)] = new WallTile(xx*16,yy*16,arbusto);
						
					} 
					else if(pixelAtual == 0xFF249BA7) {
						//AGUA
						tiles[xx+ (yy*WIDTH)] = new WallTile(xx*16,yy*16,agua);
						
					} 
					else if(pixelAtual == 0xFF6B4D35) {
						//TIJOLO
						tiles[xx+ (yy*WIDTH)] = new FloorTile(xx*16,yy*16,tijolo);
						
					}
					else if(pixelAtual == 0xFF858585) {
						//CHAO
						tiles[xx+ (yy*WIDTH)] = new FloorTile(xx*16,yy*16,chao);
						
					}
					else if(pixelAtual == 0xFF311405) {
						//TABUA
						tiles[xx+ (yy*WIDTH)] = new FloorTile(xx*16,yy*16,tabua);
					}
					else if(pixelAtual == 0xFF90E087) {
						//ARVORE1
						tiles[xx+ (yy*WIDTH)] = new WallTile(xx*16,yy*16,arvore1);
					}
					else if(pixelAtual == 0xFFA2DD71) {
						//ARVORE2
						tiles[xx+ (yy*WIDTH)] = new WallTile(xx*16,yy*16,arvore2);
					}
					else if(pixelAtual == 0xFF60DD2A) {
						//ARVORE3
						tiles[xx+ (yy*WIDTH)] = new WallTile(xx*16,yy*16,arvore3);
					}
					
					// JOGADOR
					
					else if(pixelAtual == 0xFF030C41) {
						//JOGADOR
						Game.player.setX(xx*16); 
						Game.player.setY(yy*16);
					//PERSONAGENS
					} else if(pixelAtual == 0xFFE6D21B) {
						Game.entities.add(new Pessoa(xx*16,yy*16,16,16, Entity.MOCO_L));
					} else if(pixelAtual == 0xFFAF1277) {
						Game.entities.add(new Pessoa(xx*16,yy*16,16,16, Entity.MOCA_R));
					} else if(pixelAtual == 0xFFFFFFFF) {
						Game.entities.add(new Pessoa(xx*16,yy*16,16,16, Entity.IDOSO_L));
					} else if(pixelAtual == 0xFFE69D1B) {
						Game.entities.add(new Pessoa(xx*16,yy*16,16,16, Entity.CRIANCA_R));
					} else if(pixelAtual == 0xFF848484) {
						Game.entities.add(new Pessoa(xx*16,yy*16,16,16, Entity.IDOSA_L));
					} else if(pixelAtual == 0xFF694506) {
						Game.entities.add(new Comerciante(xx*16,yy*16,16,16, Entity.COMERCIANTE));
					}
					
					// OBJETOS
					
					else if(pixelAtual == 0xFFB22D6D) {
						//TEMPO
						Game.entities.add(new Vida(xx*16,yy*16,16,16, Entity.TEMPO));
					}else if(pixelAtual == 0xFF870B0B) {
						//Flor1
						Game.entities.add(new Flor(xx*16,yy*16,16,16, Entity.PRODUTO1));
					} else if(pixelAtual == 0xFFBF28D4) {
						//Flor2
						Game.entities.add(new Flor(xx*16,yy*16,16,16, Entity.PRODUTO2));
					} else if(pixelAtual == 0xFF22041F) {
						//Flor3
						Game.entities.add(new Flor(xx*16,yy*16,16,16, Entity.PRODUTO3));
					} else if(pixelAtual == 0xFFB0A013) {
						//Flor4
						Game.entities.add(new Flor(xx*16,yy*16,16,16, Entity.PRODUTO4));
					} else if(pixelAtual == 0xFF1574BE) {
						//Flor5
						Game.entities.add(new Flor(xx*16,yy*16,16,16, Entity.PRODUTO5));
					} 
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	///////// VERIFICA SE O BLOCO ESTÁ LIVRE OU NÃO /////////
	public static boolean isFree(int xnext, int ynext, int zplayer) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;

		int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;

		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		int x4 = (xnext + TILE_SIZE - 1)  / TILE_SIZE;
		int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;
		
		if (!((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*World.WIDTH)] instanceof WallTile) )) {
					return true;
				}
		if(zplayer>0) {
			return true;
		}
		return false;
		
	}
	
	
	public static void restartGame(String newWorld) {
		Game.entities = new ArrayList<Entity>();
		Game.spritesheet = new Spritesheet("/sprite.png");
		Game.player = new Player(16,16,16,16,Game.spritesheet.getSprite(0, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/"+newWorld);
		return;
	}
	
	///////// RENDERIZAÇÃO COM MELHORIAS NA CÂMERA /////////
	public void render(Graphics g) {
		
		int xstart = Camera.x/16;
		int ystart = Camera.y/16;
		
		int xfinal = xstart + (Game.WIDTH / 8);
		int yfinal = ystart + (Game.HEIGHT / 8);
		
		for(int xx = xstart; xx <= xfinal ; xx++) {
			for(int yy = ystart; yy <= yfinal ; yy++) {
				
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}
	
}
