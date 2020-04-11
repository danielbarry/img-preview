package b.img;

import java.io.File;
import java.util.ArrayList;

/**
 * Convert.java
 *
 * Program for handling the batch conversion process.
 **/
public class Convert implements Runnable{
  /**
   * FORMAT
   *
   * The different types of images that we may need to handle.
   **/
  public enum FORMAT{
    PNG("png"),
    JPEG("jpg"),
    SVG("svg");

    private final String type;

    FORMAT(String type){
      this.type = type;
    }

    String getType(){
      return type;
    }
  }

  /**
   * METHOD
   *
   * The different supported processing methods.
   **/
  public enum METHOD{
    SCALE;
  }

  /**
   * SPEED
   *
   * Desired conversion speed. This is a request rather than a command.
   **/
  public enum SPEED{
    FAST,
    NORMAL,
    SLOW;
  }

  private Convert.FORMAT format;
  private int startedTasks;
  private int totalTasks;
  private ArrayList<File> input;
  private int jobs;
  private Convert.METHOD method;
  private String output;
  private Convert.SPEED speed;
  private int scaleWidth;
  private int scaleHeight;
  private boolean ready;

  /**
   * Convert()
   *
   * Setup the conversion process and search for the specified files.
   *
   * @param format The desired output format.
   * @param input An array of input images.
   * @param jobs The number of jobs to use in processing the images.
   * @param method The algorithm to use when processing the images.
   * @param output The output string format.
   * @param speed The desired speed to run the algorithm (if applicable).
   * @param scaledWidth The desired output width.
   * @param scaledHeight The desired output height.
   **/
  public Convert(
    FORMAT format,
    String[] input,
    int jobs,
    METHOD method,
    String output,
    SPEED speed,
    int scaleWidth,
    int scaleHeight
  ){
    ready = false;
    /* Store parameters */
    this.format = format;
    this.jobs = jobs;
    this.method = method;
    this.output = output;
    this.speed = speed;
    this.scaleWidth = scaleWidth;
    this.scaleHeight = scaleHeight;
    /* Check for files and store */
    startedTasks = 0;
    totalTasks = input.length;
    this.input = new ArrayList<File>();
    for(int x = 0; x < input.length; x++){
      File file = new File(input[x]);
      if(file.exists() || !file.isDirectory()){
        this.input.add(file);
      }else{
        return;
      }
    }
    ready = true;
  }

  /**
   * isReady()
   *
   * Check whether the class was constructed correctly.
   *
   * @return True if ready to begin conversion, otherwise false.
   **/
  public boolean isReady(){
    return ready;
  }

  /**
   * process()
   *
   * Start the conversion process.
   **/
  public void process(){
    /* TODO: Start conversion handling thread. */
  }

  /**
   * run()
   *
   * The conversion handling thread.
   **/
  @Override
  public void run(){
    /* TODO: Process images. */
  }

  /**
   * progress()
   *
   * Return the progress of the currently running conversion.
   *
   * @return Progress of current conversion, 0.0 indicates zero progress and
   * 1.0 indicates completion.
   **/
  public float progress(){
    /* TODO: Calculate progress. */
    return 0.0f;
  }
}
