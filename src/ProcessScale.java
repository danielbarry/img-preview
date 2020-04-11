package b.img;

import java.io.File;

/**
 * ProcessScale.java
 *
 * Scale an image to a given size.
 **/
public class ProcessScale implements Process, Runnable{
  private Convert.FORMAT format;
  private File input;
  private File output;
  private Convert.SPEED speed;
  private int width;
  private int height;

  @Override
  public void setFormat(Convert.FORMAT format){
    this.format = format;
  }

  @Override
  public void setInput(File input){
    this.input = input;
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
    /* TODO: Check if conversion is ready. */
    return false;
  }

  /**
   * run()
   *
   * Perform the conversion.
   **/
  @Override
  public void run(){
    /* TODO: Perform conversion. */
  }
}
