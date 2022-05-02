package com.nimico.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.nimico.entities.Entity;
import com.nimico.entities.Player;
import com.nimico.graficos.Spritesheet;
import com.nimico.world.Tile;
import com.nimico.world.World;


public class Game extends Canvas implements Runnable, KeyListener {

	///////// VARIÁVEIS E CONSTANTES ////////////
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 210;
	public static final int HEIGHT = 140;
	public static final int SCALE = 4;
	
	public static int CUR_LEVEL = 1, MAX_LEVEL = 2;
	private BufferedImage image;
	
	public static List<Entity> entities;
	
	public static Spritesheet spritesheet;
	public static Spritesheet stars;
	public static Spritesheet minimap;
	
	public static World world;
	
	public static Player player;

	public static Random rand;
	
	public int xx,yy;
	
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelart.ttf");
	public Font newFont;
	
	public static String gameState = "MENU";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	
	public boolean saveGame = false;
	
	public Menu menu;
	
	
	public int[] pixels;
	public BufferedImage lightmap;
	public int[] lightMapPixels;
	
	public int mx, my;
	
	public static int 
		receita = 0, 
		custo = 125, 
		lucro = 0, 
		saldo = 30, 
		flores = 0, 
		vendas = 0;

	public static int minOriginal = 2, secOriginal = 0;
	public static int min = minOriginal, sec = secOriginal, secFrames = 0;
	public static String secCompleter = "0", minCompleter = "0";	
	
	public Game() {
		/////// DIMENSIONAMENTO DO JOGO /////////
		addKeyListener(this);
		rand = new Random();
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		
		//////// INICIALIZANDO OBJETOS ///////////
		image = new BufferedImage(WIDTH,HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		entities = new ArrayList<Entity>();
		
		spritesheet = new Spritesheet("/sprite.png");
		stars = new Spritesheet("/stars.png");
		minimap = new Spritesheet("/minimap.png");
		player = new Player(16,16,16,16,spritesheet.getSprite(16, 16, 16, 16));
		entities.add(player);
		world = new World("/level1.png");
		
		try {
			newFont  = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(40f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		menu = new Menu();

		
	}
	///////// CONFIGURAÇÕES DA TELA /////////////
	public void initFrame() {
		frame = new JFrame("Lucre ou Perca!");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	///////// INICIA O JOGO /////////
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	///////// PARA O JOGO /////////
	public synchronized void stop() {
		isRunning = false;
		try {
		thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	///////// CHAMA OS MÉTODOS DO JOGO /////////
	public static void main(String args[]) {
		Game game = new Game();
		game.start();
	}
	///////// FUNÇÕES GAME /////////   TICK
	public void tick() {
		if(gameState == "MENU") {
			menu.tick();
		}
		else if(gameState == "NORMAL") {
			if(this.saveGame) {
				this.saveGame = false;
				String[] opt1 = {"level","life"};
				int[] opt2 = {CUR_LEVEL,(int) player.life};
				Menu.saveGame(opt1, opt2, 10);
				System.out.println("JOGO SALVO! :)");
			}
			this.restartGame = false;
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
		
		// ===== TIMER ===== //
		if(secFrames == 60) {
			secFrames = 0;
			sec--;
			if(sec <= 0) {
				min--;
				sec = 59;	
			}
			if(sec > 9) {
				secCompleter = "";
			} else {
				secCompleter = "0";
			}
			if(min > 9) {
				minCompleter = "";
			} else {
				minCompleter = "0";
			}
			if(min < 0 ) {
				gameState = "GAME_OVER";
			}
		}
		secFrames++;
		
		// ===== CONFIGURAÇÕES DE FIM DE JOGO ===== //
		} else if(gameState == "GAME_OVER") {
			framesGameOver++;
			if(framesGameOver == 35) {
				framesGameOver = 0;
				if(showMessageGameOver)
					showMessageGameOver = false;
				else
					showMessageGameOver = true;
				}
			if(restartGame) {
				restartGame = false;
				gameState = "NORMAL";
				CUR_LEVEL = 1;
				sec = secOriginal;
				min = minOriginal;
				String newWorld = "level"+CUR_LEVEL+".png";
				World.restartGame(newWorld);
			}
		}
		}
	
	////////// RENDERIZAÇÃO DO JOGO //////////
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		world.render(g);
		
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		g.dispose();
		g = bs.getDrawGraphics();
		
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		
		if(CUR_LEVEL == 999) {
			// FUNDO PRETO
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
		}
		
		if(CUR_LEVEL != 999) {
			lucro = player.receita - player.custo;
			g.setFont(newFont);
			g.setColor(Color.WHITE);
			
			g.drawString("Custo: R$"+ player.custo + ",00",16,60);
			g.drawString("Receita: R$"+ player.receita + ",00",16,90);
			g.drawString("Lucro: R$"+ lucro + ",00",16,120);
			g.drawString("Saldo: R$"+ player.saldo + ",00",16,150);
			g.drawString("Flores: "+ player.flores ,16,190);
			g.drawString("Vendas: "+ player.vendas ,16,220);
			
			g.setFont(new Font("arial", Font.BOLD,80));
			g.drawString(minCompleter + min + ":" + secCompleter + sec, 320 , 80);
			
			g.drawImage(Tile.Map, 719, 0, 121, 121, null);
		}
		if(gameState == "GAME_OVER") {
			// FUNDO PRETO
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(new Color(0,0,0));
				g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
				// CONTEÚDO
				g.setFont(new Font("arial", Font.BOLD,50));
				g.setColor(new Color(255, 190, 22));
				if(lucro > 0) {
					g.drawString("Você venceu! Parabéns!!!",130,180);
					if(lucro <= 100) {
						g.drawImage(Tile.Stars1, 230, 200, 96*SCALE, 32*SCALE, null);
					}
					if(lucro > 100 && lucro <= 300) {
						g.drawImage(Tile.Stars2, 230, 200, 96*SCALE, 32*SCALE, null);
					}
					if(lucro > 300) {
						g.drawImage(Tile.Stars3, 230, 200, 96*SCALE, 32*SCALE, null);
					}
				} else {
					g.drawString("Essa não, você perdeu!!!",130,180);
					g.drawImage(Tile.Stars0, 230, 200, 96*SCALE, 32*SCALE, null);
				}
			if(showMessageGameOver) {
				g.setFont(new Font("arial", Font.BOLD,25));
				g.setColor(new Color(255, 190, 22));
				g.drawString("Pressione ENTER para reiniciar",230,400);
			}
		} else if(gameState == "MENU") {
			menu.render(g);
		}
		
		//////// ROTACIONANDO COM O MOUSE ///////
		/*
		Graphics2D g2 = (Graphics2D) g;
		double angleMouse = Math.atan2(200+25 - my, 200+25 - mx);
		g2.rotate(angleMouse, 225, 225);
		g.setColor(Color.RED);
		g.fillRect(200,200,50,50);
		*/
		
		bs.show();
	}
	////////// RODA O JOGO /////////
	//**** DEFINE O FPS ***//
	public void run() {

		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		
		while(isRunning) {
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				//System.out.println("FPS: " + frames);
				frames = 0;
				timer+=1000;
			}
		}
		stop();
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void keyTyped(KeyEvent e) {
		
		
		
	}

	///////// MOVIMENTAÇÃO DO JOGADOR /////////
	public void keyPressed(KeyEvent e) {

		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			//MOVE PARA A DIREITA
			//System.out.println("DIREITA");
			player.right = true;
			
			if(gameState == "PERSONAGEM") {
				player.down = true;
			}
			
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			//MOVE PARA A ESQUERDA
			//System.out.println("ESQUERDA");
			player.left = true;
			
			if(gameState == "PERSONAGEM") {
				player.up = true;
			}
			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			//MOVE PARA CIMA
			//System.out.println("CIMA");
			player.up = true;

			if(gameState == "MENU") {
				menu.up = true;
			}
			
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			//MOVE PARA BAIXO
			//System.out.println("BAIXO");
			player.down = true;
			
			if(gameState == "MENU") {
				menu.down = true;
			}
			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			restartGame = true;
			
			if(gameState == "MENU") {
				menu.enter = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_P) {
			gameState = "MENU";
			menu.pause = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_Z) {
			//player.jump = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_C) {
			if(gameState == "NORMAL")
			this.saveGame = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_R) {
			restartGame = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.buy = true;
			player.sell = true;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {

		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			//MOVE PARA A DIREITA
			//System.out.println("DIREITA");
			player.right = false;
			
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			//MOVE PARA A ESQUERDA
			//System.out.println("ESQUERDA");
			player.left = false;
			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			//MOVE PARA CIMA
			//System.out.println("CIMA");
			player.up = false;

			if(gameState == "MENU") {
				menu.up = false;
			}
			
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			//MOVE PARA BAIXO
			//System.out.println("BAIXO");
			player.down = false;
			
			if(gameState == "MENU") {
				menu.down = false;
			}
			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			restartGame = false;
			
			if(gameState == "MENU") {
				menu.enter = false;
			}
			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.buy = false;
			player.sell = false;
		}
		
	}
	
}
