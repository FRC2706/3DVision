package ca.team2706.vision3d;

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
	private static boolean dumpData = true;

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
		while (true) {
			try {
				if (buffers.size() > 0) {
					
					long startTime = System.currentTimeMillis();
					
					Integer[] data = buffers.get(0);
					buffers.remove(0);

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
					for(int x = 0; x < 640; x++) {
						for(int y = 0; y < 480; y++) {
							if(points[x][y] == null) {
								System.out.println(x+" "+y);
							}
						}
					}

					SceneData sceneData = new SceneData(points, 640, 480);
					
					long startEdges = System.currentTimeMillis();
					SceneData edges = ObjectRecognizer.detectEdges(sceneData);
					System.out.println("Edges took "+(System.currentTimeMillis()-startEdges)+" ms");
					long startLines = System.currentTimeMillis();
					List<Line> lines = ObjectRecognizer.findLines(edges);
					System.out.println("Lines took "+(System.currentTimeMillis()-startLines)+" ms");
					long startObjects = System.currentTimeMillis();
					List<ObjectData> objects = ObjectRecognizer.findAndRecognizeObjects(lines);
					System.out.println("Objects took "+(System.currentTimeMillis()-startObjects)+" ms");
					
					for (ObjectData d : objects) {

						System.out.println(d.getCenterX() + " " + d.getCenterY() + " " + d.getWidth() + " "
								+ d.getHeight() + " " + d.getNumSides());

					}
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
