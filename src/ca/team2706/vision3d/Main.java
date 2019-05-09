package ca.team2706.vision3d;

import java.nio.ByteBuffer;

import org.openkinect.freenect.Context;
import org.openkinect.freenect.DepthFormat;
import org.openkinect.freenect.DepthHandler;
import org.openkinect.freenect.Device;
import org.openkinect.freenect.FrameMode;
import org.openkinect.freenect.Freenect;
import org.openkinect.freenect.LogLevel;
import org.openkinect.freenect.Resolution;
import org.openkinect.freenect.util.Jdk14LogHandler;

public class Main {

	private static Context ctx;
	private static Device dev;

	public static void main(String[] args) throws InterruptedException {
		
		DepthDumper.start();
		
		initKinect();
		
		startDepth();
		
		while(true) {
			Thread.sleep(1);
		}
	}

	public static void startDepth() {
		dev.setDepthFormat(DepthFormat.MM, Resolution.MEDIUM);
		dev.startDepth(new DepthHandler() {

			@Override
			public void onFrameReceived(FrameMode mode, ByteBuffer frame, int timestamp) {
				try {
					
					Integer[] data = new Integer[frame.limit()];
					
					while(frame.hasRemaining()) {
						data[frame.position()] = frame.getInt();
					}
					
					frame.position(0);
					
					DepthDumper.add(data);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void initKinect() {
		ctx = Freenect.createContext();
		ctx.setLogHandler(new Jdk14LogHandler());
		ctx.setLogLevel(LogLevel.ERROR);
		if (ctx.numDevices() > 0) {
			try {
				dev = ctx.openDevice(0);
			}catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("WARNING: No kinects detected.");
			System.exit(1);
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
