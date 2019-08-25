package ca.team2706.vision3d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.thatmadhacker.f3ddpodr.Line;
import org.thatmadhacker.f3ddpodr.ObjectData;
import org.thatmadhacker.f3ddpodr.ObjectRecognizer;
import org.thatmadhacker.f3ddpodr.Point;
import org.thatmadhacker.f3ddpodr.SceneData;

public class DepthDumper implements Runnable {

	private static List<Integer[]> buffers = new ArrayList<Integer[]>();
	private int stamp = 0;
	private static File dumpDir = new File("data/");
	private static boolean dumpData = false;

	private static Thread thread;

	public static void add(Integer[] frame) {

		buffers.add(frame);

	}

	public static void start() {
		if (dumpData) {
			dumpDir.mkdirs();
		}
		thread = new Thread(new DepthDumper());
		thread.start();
	}

	@Override
	public void run() {
		Display edgeD = new Display();
		Display lineD = new Display();
		Display objD = new Display();
		while (true) {
			try {
				if (buffers.size() > 0) {
					
					long startTime = System.currentTimeMillis();
					
					Integer[] data = buffers.get(buffers.size()-1);
					buffers.remove(buffers.size()-1);
					if(buffers.size() > 2) {
						buffers.clear();
					}

					Point[][] points = new Point[640][480];
					for(int y = 0; y < 480; y++) {
						for(int x = 0; x < 640; x++) {
							if(data[(y*640+x) * 2] == null)
								data[(y*640+x) * 2] = -1;
							int dataInt = data[(y*640+x) * 2];
							Point p = new Point(x, y, dataInt);
							points[x][y] = p;
						}
					}
					SceneData sceneData = new SceneData(points, 640, 480);
					
					long startEdges = System.currentTimeMillis();
					SceneData edges = ObjectRecognizer.detectEdges(sceneData,100000000, 0);
					System.out.println("Edges took "+(System.currentTimeMillis()-startEdges)+" ms");
					BufferedImage image = new BufferedImage(640,480,BufferedImage.TYPE_INT_ARGB);
					Graphics g = image.getGraphics();
					for(Point[] points2 : sceneData.getData()) {
						for(Point p : points2) {
							int z = (int) (p.getZ()/1000000);
							if(z > 255) z = 255;
							g.setColor(new Color(z,0,0));
							g.fillRect((int) p.getX(), (int) p.getY(), 1, 1);
						}
					}
					g.dispose();
					edgeD.setImage(image);
					
					long startLines = System.currentTimeMillis();
					List<Line> lines = ObjectRecognizer.findLines(edges);
					image = new BufferedImage(640,480,BufferedImage.TYPE_INT_ARGB);
					g = image.getGraphics();
					for(Line l : lines) {
						g.setColor(Color.BLACK);
						g.drawLine((int)l.getStart().getX(), (int)l.getStart().getY(), (int)l.getStop().getX(), (int)l.getStop().getY());
					}
					g.dispose();
					lineD.setImage(image);
					
					System.out.println("Lines took "+(System.currentTimeMillis()-startLines)+" ms");
					long startObjects = System.currentTimeMillis();
					List<ObjectData> objects = ObjectRecognizer.findAndRecognizeObjects(lines);
					System.out.println("Objects took "+(System.currentTimeMillis()-startObjects)+" ms");
					
					image = new BufferedImage(640,480,BufferedImage.TYPE_INT_ARGB);
					g = image.getGraphics();
					
					for(ObjectData o : objects) {
						g.setColor(Color.BLACK);
						g.fillRect((int) (o.getCenterX()-(o.getWidth()/2)), (int) (o.getCenterY()-(o.getHeight()/2)), (int) o.getWidth()/2, (int) o.getHeight()/2); 
					}
					
					g.dispose();
					objD.setImage(image);
					
					System.out.println("Frame took "+(System.currentTimeMillis()-startTime)+"ms");
					if (dumpData) {
						File outFile = new File(dumpDir, stamp + ".depth");
						outFile.delete();
						outFile.createNewFile();
						PrintWriter out = new PrintWriter(new FileWriter(outFile));
						for (int y = 0; y < 480; y++) {
							for (int x = 0; x < 640; x++) {
								if(points[x][y] == null) {
									continue;
								}
								int depth = (int) points[x][y].getZ();
								if(x == 0) {
									out.print(depth);
								}else {
									out.print(" "+depth);
								}
							}
							out.println();
						}
						out.close();
						stamp++;
						System.out.println(stamp+" "+outFile.getAbsolutePath()+" "+outFile.exists());
					}
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
