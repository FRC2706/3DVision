package com.team2706.vision;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class Vision extends JFrame implements NativeKeyListener{
static Vision vision;
	public Vision() {
		// Clear previous logging configurations.
		LogManager.getLogManager().reset();

		// Get the logger for "org.jnativehook" and set the level to off.
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}

		GlobalScreen.addNativeKeyListener(this);

		setSize(new Dimension(640, 480));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		vision = new Vision();

	}

	public void onRecieve() throws Exception {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		ArrayList<String> depths = new ArrayList<String>();
		Scanner in = new Scanner(new File(System.getProperty("user.home") + "/depth.txt"));
		while (in.hasNextLine()) {
			String depth = in.nextLine();
			depths.add(depth);
			System.out.println(depth);
		}
		System.out.println();
		System.out.println();
		System.out.println("file is empty, waiting");
		System.out.println();
		System.out.println();
		BufferedImage imageFrame = ImageIO.read(new File(System.getProperty("user.home") + "/rgb.png"));
        g.drawImage(imageFrame, 0, 0, null);
        g.dispose();
        bs.show();
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		System.out.println(arg0.getKeyCode()+" "+NativeKeyEvent.VC_F24);
        if(arg0.getKeyCode() == NativeKeyEvent.VC_F24){
        	try {
        		System.out.println("recieved");
				onRecieve();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {}
}
