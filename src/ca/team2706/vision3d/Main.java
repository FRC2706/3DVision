package ca.team2706.vision3d;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.openkinect.freenect.Context;
import org.openkinect.freenect.DepthHandler;
import org.openkinect.freenect.Device;
import org.openkinect.freenect.FrameMode;
import org.openkinect.freenect.Freenect;
import org.openkinect.freenect.LogLevel;
import org.openkinect.freenect.VideoHandler;
import org.openkinect.freenect.util.Jdk14LogHandler;

public class Main {

	private static Context ctx;
    private static Device dev;
    private static DisplayGui depth = null;
    private static DisplayGui rgb = null;
	
	public static void main(String[] args) throws InterruptedException {
		initKinect();
		
		//startDepth(); these dont work atm
		//startVideo();
		
		shutdownKinect();
	}
	
	public static void startDepth() {
		dev.startDepth(new DepthHandler() {

            @Override
            public void onFrameReceived(FrameMode mode, ByteBuffer frame, int timestamp) {
            	try {
            		BufferedImage image = byteArrayToImage(frame.array());
            		if(depth == null) {
            			depth = new DisplayGui(image,"depth",true);
            		}
            		depth.updateImage(image);
            	}catch(Exception e) {
            		e.printStackTrace();
            	}
            }
        });
	}
	
	public static void startVideo() {
		dev.startVideo(new VideoHandler() {
            @Override
            public void onFrameReceived(FrameMode mode, ByteBuffer frame, int timestamp) {
            	try {
            		BufferedImage image = byteArrayToImage(frame.array());
            		if(rgb == null) {
            			rgb = new DisplayGui(image,"rgb",true);
            		}
            		rgb.updateImage(image);
            	}catch(Exception e) {
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
            System.err.println("WARNING: No kinects detected, hardware tests will be implicitly passed.");
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
