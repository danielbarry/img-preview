package b.img;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * ProcessScale.java
 *
 * Scale an image to a given size.
 **/
public class ProcessScale implements Process, Runnable{
  private Convert.FORMAT format;
  private BufferedImage input;
  private File output;
  private Convert.SPEED speed;
  private int width;
  private int height;
  private boolean complete = false;

  @Override
  public void setFormat(Convert.FORMAT format){
    this.format = format;
  }

  @Override
  public void setInput(File input){
    try{
      this.input = ImageIO.read(input);
    }catch(IOException e){
      this.input = null;
    }
  }

  @Override
  public void setOutput(File output){
    this.output = output;
  }

  @Override
  public void setSpeed(Convert.SPEED speed){
    this.speed = speed;
  }

  @Override
  public void setWidth(int width){
    this.width = width;
  }

  @Override
  public void setHeight(int height){
    this.height = height;
  }

  @Override
  public boolean isReady(){
    /* Make sure format provided */
    if(format == null){
      return false;
    }
    /* Make sure input loaded */
    if(input == null){
      return false;
    }
    /* Make sure output is set, but doesn't exist */
    if(output == null || output.exists()){
      return false;
    }
    /* Make sure speed provided */
    if(speed == null){
      return false;
    }
    /* Make sure width valid for this mode */
    if(width < 1){
      return false;
    }
    /* Make sure height valid for this mode */
    if(height < 1){
      return false;
    }
    return true;
  }

  public boolean isComplete(){
    return complete;
  }

  /**
   * run()
   *
   * Perform the conversion.
   **/
  @Override
  public void run(){
    complete = false;
    /* Calculate best width and height */
    double widthRatio = (double)width / (double)input.getWidth();
    double heightRatio = (double)height / (double)input.getHeight();
    double ratio = Math.min(widthRatio, heightRatio);
    width = (int)(input.getWidth() * ratio);
    height = (int)(input.getHeight() * ratio);
    /* Perform conversion */
    BufferedImage img = null;
    switch(speed){
      case FAST :
        img = processFast(input, width, height);
        break;
      case NORMAL :
        img = processNormal(input, width, height);
        break;
      case SLOW :
        img = processSlow(input, width, height);
        break;
      default :
        System.err.println("(internal) Unsupported speed for conversion.");
        break;
    }
    /* Save the image */
    switch(format){
      case PNG :
      case JPEG :
        try{
          ImageIO.write(img, format.getType(), output);
        }catch(IOException e){
          System.err.println("(internal) Unable to write image to disk.");
        }
        break;
      case SVG :
        /* TODO: Handle this case. */
        break;
      default :
        System.err.println("(internal) Unsupported format during save.");
        break;
    }
    complete = true;
  }

  /**
   * processFast()
   *
   * Perform a fast conversion and care little for quality.
   *
   * @param input The input image.
   * @param width The width of the target image.
   * @param hieght The height of the target image.
   * @return The processed image.
   **/
  private BufferedImage processFast(BufferedImage input, int width, int height){
    BufferedImage i = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
    Graphics2D g = i.createGraphics();
    g.drawImage(input, 0, 0, width, height, null);
    g.dispose();
    return i;
  }

  /**
   * processNormal()
   *
   * Perform a normal conversion using rendering hints.
   *
   * @param input The input image.
   * @param width The width of the target image.
   * @param hieght The height of the target image.
   * @return The processed image.
   **/
  private BufferedImage processNormal(BufferedImage input, int width, int height){
    BufferedImage i = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
    Graphics2D g = i.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.drawImage(input, 0, 0, width, height, null);
    g.dispose();
    return i;
  }

  /**
   * processSlow()
   *
   * Perform a slow conversion using progressive scaling.
   *
   * @param input The input image.
   * @param width The width of the target image.
   * @param hieght The height of the target image.
   * @return The processed image.
   **/
  private BufferedImage processSlow(BufferedImage input, int width, int height){
    BufferedImage i = input;
    int w = input.getWidth() / 2;
    int h = input.getHeight() / 2;
    while(w > width && h > height){
      i = processNormal(i, w, h);
      w = w / 2;
      h = h / 2;
    }
    return processNormal(i, width, height);
  }
}
