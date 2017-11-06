import SimpleOpenNI.*;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

SimpleOpenNI context;
Robot robot;
  public void setup() {
    size(640,480,P3D);
    context = new SimpleOpenNI(this);
    // enable depthMap generation
    context.enableDepth();
    context.enableRGB();
try { 
    robot = new Robot();
  } catch (AWTException e) {
    e.printStackTrace();
    exit();
  }
  }

  public void draw(){
    context.update();
    image(context.rgbImage(),0,0);
    drawImg();
    try{
    update();
    }catch(Exception e){
     e.printStackTrace(); 
    }

  }

  public void update() throws Exception{
    try{
        ImageIO.write((BufferedImage) context.rgbImage().getNative(),"png",new File(System.getProperty("user.home")+"/rgb.png"));
        //ImageIO.write((BufferedImage) context.depthImage().getNative(),"png",new File(System.getProperty("user.home")+"/depth.png"));
        //Depth code
        PrintWriter out = createWriter(System.getProperty("user.home")+"/depth.txt");
        PImage depth = context.depthImage();
        int skip = 4;
        for(int x = 0; x < depth.width;x+=skip){
         for(int y = 0; y < depth.height;y+=skip){
          int index = x + y * depth.width;
          float  b = brightness(depth.pixels[index]);
          float z = map(b,0,255,250,-250);
          out.println(x+":"+y+":"+z);
         } 
        }
        out.close();
        robot.keyPress(KeyEvent.VK_F24);
        robot.keyRelease(KeyEvent.VK_F24);
        Thread.sleep(400);
    }catch(IOException e){
     e.printStackTrace(); 
    }
  }
  
  public void drawImg() {
  }
  
