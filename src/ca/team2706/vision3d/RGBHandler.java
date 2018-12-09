package ca.team2706.vision3d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class RGBHandler implements Runnable {

	private static List<ByteBuffer> buffers = new ArrayList<ByteBuffer>();
	
	private static Thread thread;
	
	public static void add(ByteBuffer frame) {
		
		buffers.add(frame);
		
	}
	
	public static void start() {
		thread = new Thread(new RGBHandler());
		thread.start();
	}
	
	private RGBHandler() {
		
	}

	private int i1 = 0;
	
	@Override
	public void run() {
		while(true) {
			if(buffers.size() > 0) {
				ByteBuffer frame = buffers.get(0);
				BufferedImage image = new BufferedImage(640,480,BufferedImage.TYPE_3BYTE_BGR);
				Graphics g = image.getGraphics();
				int x = 0;
				int y = 0;
				while(frame.remaining() > 0) {
					
					byte b1 = frame.get();
					byte b2 = frame.get();
					byte b3 = frame.get();
					
					Color c = new Color(Math.abs(b1),Math.abs(b2),Math.abs(b3));
					
					g.setColor(c);
					g.fillRect(x, y, 1, 1);
					
					x++;
					if(x >= 640) {
						x = 0;
						y++;
					}
					
				}
				g.dispose();
				try {
					ImageIO.write(image, "PNG", new File("out"+i1+".png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				i1++;
				
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
