package ca.team2706.vision3d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;

public class Display extends Thread{
	JFrame frame = new JFrame("Image viewer");
	public Display() {
		frame.setSize(640, 480);
		frame.setVisible(true);
		start();
	}
	private BufferedImage image = null;
	private Lock lock = new ReentrantLock();
	
	@Override
	public void run() {
		while(true) {
			lock.lock();
			draw();
			lock.unlock();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private void draw() {
		if(image == null) return;
		BufferStrategy bs = frame.getBufferStrategy();
		if(bs == null) {
			frame.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 640, 480);
		
		g.drawImage(image, 0,0, null);
		
		g.dispose();
		bs.show();
		return;
	}
	public void setImage(BufferedImage image) {
		lock.lock();
		this.image = image;
		lock.unlock();
	}
}
