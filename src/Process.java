package b.img;

import java.io.File;

/**
 * Process.java
 *
 * Define how a process is interacted with.
 **/
public interface Process{
  /**
   * setFormat()
   *
   * Set the format of the output image.
   *
   * @param format The output format.
   **/
  public void setFormat(Convert.FORMAT format);

  /**
   * setInput()
   *
   * Set the input image file to be used.
   *
   * @param input The input file to be used.
   **/
  public void setInput(File input);

  /**
   * setOutput()
   *
   * Set the output file to be used for the conversion.
   *
   * @param output The output file to be used.
   **/
  public void setOutput(File output);

  /**
   * setSpeed()
   *
   * Set the desired speed of the conversion.
   *
   * @param The desired speed of the conversion.
   **/
  public void setSpeed(Convert.SPEED speed);

  /**
   * setWidth()
   *
   * Set the desired output width of the conversion.
   *
   * @param width Set the desired width of the output image.
   **/
  public void setWidth(int width);

  /**
   * setHeight()
   *
   * Set the desired output height of the conversion.
   *
   * @param height Set the desired height of the output image.
   **/
  public void setHeight(int height);

  /**
   * isReady()
   *
   * Check whether the conversion process is ready to be performed.
   *
   * @return True if the conversion process is ready, otherwise false.
   **/
  public boolean isReady();
}
