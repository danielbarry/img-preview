package b.img;

/**
 * Convert.java
 *
 * Program for handling the batch conversion process.
 **/
public class Convert{
  /**
   * FORMAT
   *
   * The different types of images that we may need to handle.
   **/
  public enum FORMAT{
    PNG,
    JPEG,
    SVG
  }

  /**
   * METHOD
   *
   * The different supported processing methods.
   **/
  public enum METHOD{
    SCALE
  }

  /**
   * SPEED
   *
   * Desired conversion speed. This is a request rather than a command.
   **/
  public enum SPEED{
    FAST,
    NORMAL,
    SLOW
  }

  /**
   * Convert()
   *
   * Setup the conversion process and search for the specified files.
   *
   * @param
   * @param
   * @param
   * @param
   * @param
   * @param
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
    /* TODO: Do something with parameters. */
    /* TODO: Search for specified files. */
  }

  /**
   **/
  public void process(){
    /* TODO: Process images. */
  }
}
