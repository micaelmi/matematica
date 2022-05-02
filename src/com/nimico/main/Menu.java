package com.nimico.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.nimico.world.World;

public class Menu {
	
	public String[] options = {"Novo Jogo","Carregar Jogo","Sair"};
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean up, down, enter;
	public static boolean pause = false;
	
	public static boolean saveExists = false, saveGame = false;

	public void tick() {
		
		File file  = new File("save.txt");
		if(file.exists()) {
			saveExists = true;
		} else {
			saveExists = false;
		}
		
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0) {
				currentOption = maxOption;
			}
		}
		
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}
		
		if(enter) {
			enter = false;
			if(options[currentOption] == "Novo Jogo") {
				Game.gameState = "NORMAL";
				pause = false;	
				file = new File("save.txt");
				file.delete();
			} 
			else if(options[currentOption] == "Continuar") {
				Game.gameState = "NORMAL";
				pause = false;	
				file = new File("save.txt");
				file.delete();
			}
			else if(options[currentOption] == "Carregar Jogo") {
				file = new File("save.txt");
				if(file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}
			}
			
			else if(options[currentOption] == "Sair") {
				System.exit(1);
			}
		}
	}
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch(spl2[0]) {
			case "level":
				World.restartGame("level"+spl2[1]+".png");
				Game.gameState = "NORMAL";
				pause = false;
				break;
			case "life":
				Game.player.life = Integer.parseInt(spl2[1]); 
				break;
			
			}
		}
	}
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":");
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						for(int i = 0; i < val.length; i++) {
							val[i] -= encode;
							trans[1] += val[i];
						}
						line+=trans[0];
						line+=":";
						line+=trans[1];
						line+="/";
					}
				}catch(IOException e) {}
			}catch(FileNotFoundException e) {}
		}
		return line;
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current+=":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int n = 0; n < value.length; n++) {
				value[n]+=encode;
				current+=value[n];
			}
			try {
				write.write(current);
				if(i < val1.length - 1)
					write.newLine();
			}catch(IOException e) {}

			try {
				write.flush();
				write.close();
			}catch(IOException e) {}
			
		}
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(0f,0f,0f,.7f));
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		//===== TITULO =====
		g.setColor(new Color(255, 217, 48));
		g.setFont(new Font("arial", Font.BOLD,72));
		g.drawString("Lucre ou Perca!", 140,120);
		//===== OPÇÕES =====
		if(pause == false) {
		//NOVO JOGO
		g.setColor(new Color(6, 156, 16));
		g.setFont(new Font("arial", Font.BOLD,28));
		g.drawString("Novo Jogo", 345,200);
		} else {
			g.setColor(new Color(6, 156, 16));
			g.setFont(new Font("arial", Font.BOLD,28));
			g.drawString("Continuar", 345,200);
		}
		//CARREGAR
		g.setColor(new Color(24, 24, 186));
		g.setFont(new Font("arial", Font.BOLD,28));
		g.drawString("Carregar jogo", 320,260);
		//SAIR
		g.setColor(new Color(214, 21, 11));
		g.setFont(new Font("arial", Font.BOLD,28));
		g.drawString("Sair", 385,320);
		
		if(options[currentOption] == "Novo Jogo") {
			g.setColor(new Color(6, 156, 16));
			g.setFont(new Font("arial", Font.BOLD,28));
			g.drawString(">", 325,200);
		} else 
			if(options[currentOption] == "Carregar Jogo") {
				g.setColor(new Color(24, 24, 186));
				g.setFont(new Font("arial", Font.BOLD,28));
				g.drawString(">", 300,260);
			} else 
				if(options[currentOption] == "Sair") {
					g.setColor(new Color(214, 21, 11));
					g.setFont(new Font("arial", Font.BOLD,28));
					g.drawString(">", 365,320);
				}
	}
	
}
