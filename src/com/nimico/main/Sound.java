package com.nimico.main;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {

	@SuppressWarnings("removal")
	private AudioClip clip;
	
	public static final Sound cash = new Sound("/cash.wav");
	public static final Sound handing = new Sound("/giving-money.wav");
	
	private Sound(String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		} catch(Throwable e) {
			
		}
	}
	
	public void loop() {
		try {
			new Thread() {
				public void run() {
					clip.loop();
				}
			}.start();
		} catch(Throwable e) {
			
		}
	}
	
	public void play() {
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();
		} catch(Throwable e) {
			
		}
	}
	
}
