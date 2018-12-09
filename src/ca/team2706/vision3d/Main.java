package ca.team2706.vision3d;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.openkinect.freenect.Context;
import org.openkinect.freenect.DepthFormat;
import org.openkinect.freenect.DepthHandler;
import org.openkinect.freenect.Device;
import org.openkinect.freenect.FrameMode;
import org.openkinect.freenect.Freenect;
import org.openkinect.freenect.LogLevel;
import org.openkinect.freenect.VideoFormat;
import org.openkinect.freenect.VideoHandler;
import org.openkinect.freenect.util.Jdk14LogHandler;

public class Main {

	private static Context ctx;
	private static Device dev;

	public static void main(String[] args) throws InterruptedException {
		
		RGBHandler.start();
		
		initKinect();
		
		startDepth();
		startVideo();
		
		while(true) {
			Thread.sleep(1);
		}
	}

	public static void startDepth() {
		dev.setDepthFormat(DepthFormat.D11BIT);
		dev.startDepth(new DepthHandler() {

			@Override
			public void onFrameReceived(FrameMode mode, ByteBuffer frame, int timestamp) {
				try {

					

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void startVideo() {
		dev.setVideoFormat(VideoFormat.RGB);
		dev.startVideo(new VideoHandler() {
			@Override
			public void onFrameReceived(FrameMode mode, ByteBuffer frame, int timestamp) {
				try {
					
					RGBHandler.add(frame);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static BufferedImage byteArrayToImage(byte[] array) throws IOException {
		InputStream in = new ByteArrayInputStream(array);
		BufferedImage bImageFromConvert = ImageIO.read(in);
		return bImageFromConvert;
	}

	public static void initKinect() {
		ctx = Freenect.createContext();
		ctx.setLogHandler(new Jdk14LogHandler());
		ctx.setLogLevel(LogLevel.SPEW);
		if (ctx.numDevices() > 0) {
			dev = ctx.openDevice(0);
		} else {
			System.err.println("WARNING: No kinects detected.");
		}
	}

	public static void shutdownKinect() {
		if (ctx != null)
			if (dev != null) {
				dev.close();
			}
		ctx.shutdown();
	}

}
