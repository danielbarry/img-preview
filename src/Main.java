package b.img;

/**
 * Main.java
 *
 * Parse the command line parameters and decide the further operation of the
 * program.
 **/
public class Main{
  private final static int VER_MAJOR = 0;
  private final static int VER_MINOR = 1;
  private final static int VER_PATCH = 0;

  private Convert.FORMAT format;
  private String[] input;
  private int jobs;
  private Convert.METHOD method;
  private String output;
  private Convert.SPEED speed;
  private int scaleWidth;
  private int scaleHeight;

  /**
   * main()
   *
   * Main entry point into the program. The program is put into an instance
   * context and run.
   *
   * @param args The command line arguments.
   **/
  public static void main(String[] args){
    new Main(args);
  }

  /**
   * Main()
   *
   * The constructor for the Main class and main entry point into the program.
   * The command line parameters are parsed in this method and execution is
   * decided.
   *
   * @param args The command line arguments.
   **/
  public Main(String[] args){
    /* Set default values */
    format = Convert.FORMAT.JPEG;
    input = null;
    jobs = 1;
    method = Convert.METHOD.SCALE;
    output = "%f-%i-%t";
    speed = Convert.SPEED.NORMAL;
    scaleWidth = 256;
    scaleHeight = 256;
    /* Loop the command line parameters */
    for(int x = 0; x < args.length; x++){
      switch(args[x]){
        case "-a" :
        case "--about" :
          x = about(args, x);
          break;
        case "-f" :
        case "--format" :
          x = format(args, x);
          break;
        case "-i" :
        case "--input" :
          x = input(args, x);
          break;
        case "-j" :
        case "--jobs" :
          x = jobs(args, x);
          break;
        case "-h" :
        case "--help" :
          x = help(args, x);
          break;
        case "-m" :
        case "--method" :
          x = method(args, x);
          break;
        case "-o" :
        case "--output" :
          x = input(args, x);
          break;
        case "-s" :
        case "--speed" :
          x = speed(args, x);
          break;
        case "-x" :
        case "--scale" :
          x = scale(args, x);
          break;
        case "-v" :
        case "--version" :
          x = version(args, x);
          break;
        default :
          System.err.println("Unknown param '" + args[x] + "', see '--help'");
          System.exit(0);
          break;
      }
    }
    /* Check if we can actually start conversion */
    if(input != null){
      Convert convert = new Convert(
        format,
        input,
        jobs,
        method,
        output,
        speed,
        scaleWidth,
        scaleHeight
      );
      convert.process();
    }else{
      System.err.println("No input images given.");
      System.exit(0);
    }
  }

  /**
   * about()
   *
   * Display information about the program and then quit.
   *
   * @param args The command line arguments.
   * @param x Current offset into the program.
   * @return The new offset into the command line parameters.
   **/
  private int about(String[] args, int x){
    System.out.println("Written by B[], 2020");
    System.out.println("");
    System.out.println("This program converts an image into a light-weight");
    System.out.println("preview representation using one of a few selected");
    System.out.println("algorithms. One use case example is when previewing");
    System.out.println("a large number of images on a website and you don't");
    System.out.println("want to pay a high bandwidth for displaying all of");
    System.out.println("the images.");
    System.exit(0);
    return x;
  }

  /**
   * format()
   *
   * Set the desired output format.
   *
   * @param args The command line arguments.
   * @param x Current offset into the program.
   * @return The new offset into the command line parameters.
   **/
  private int format(String[] args, int x){
    /* TODO: The desired output format. */
    return x;
  }

  /**
   * input()
   *
   * Get the input images.
   *
   * @param args The command line arguments.
   * @param x Current offset into the program.
   * @return The new offset into the command line parameters.
   **/
  private int input(String[] args, int x){
    /* TODO: Get the input images. */
    return x;
  }

  /**
   * jobs()
   *
   * Get a number of jobs to be used during processing.
   *
   * @param args The command line arguments.
   * @param x Current offset into the program.
   * @return The new offset into the command line parameters.
   **/
  private int jobs(String[] args, int x){
    /* TODO: Get number of jobs. */
    return x;
  }

  /**
   * help()
   *
   * Display help for the program and then quit.
   *
   * @param args The command line arguments.
   * @param x Current offset into the program.
   * @return The new offset into the command line parameters.
   **/
  private int help(String[] args, int x){
    System.out.println("img-preview [OPT]");
    System.out.println("");
    System.out.println("  OPTions");
    System.out.println("");
    System.out.println("    -a  --about    Display information about program");
    System.out.println("    -f  --format   The desired output format");
    System.out.println("                     png  = Quality bitmap");
    System.out.println("                     jpeg = Smaller bitmap");
    System.out.println("                     svg  = Scalar");
    System.out.println("    -i  --input    Feed one or more input images");
    System.out.println("                     For example:");
    System.out.println("                       img-preview -i 1.png");
    System.out.println("                       img-preview -i 1.png 2.png");
    System.out.println("    -j  --jobs     Number of threads to use");
    System.out.println("    -h  --help     Display this help");
    System.out.println("    -m  --method   Set the method to be used");
    System.out.println("                     scale = Image scaling");
    System.out.println("    -o  --output   Define the output format");
    System.out.println("                     Use the following markers:");
    System.out.println("                       %f = filename");
    System.out.println("                       %i = counter");
    System.out.println("                       %t = current timestamp");
    System.out.println("                     For example:");
    System.out.println("                       img-preview -o %f-%i");
    System.out.println("    -s  --speed    Desired conversion speed");
    System.out.println("                     Select speed at cost of quality");
    System.out.println("                       fast   = Fast, low quality");
    System.out.println("                       normal = Default");
    System.out.println("                       slow   = Slow, high quality");
    System.out.println("    -x  --scale    The desired output scale");
    System.out.println("                     The parameters are:");
    System.out.println("                       width  = Width in pixels");
    System.out.println("                       height = Height in pixels");
    System.out.println("                     The format is:");
    System.out.println("                       img-preview -x width height");
    System.out.println("    -v  --version  Display program version");
    System.exit(0);
    return x;
  }

  /**
   * method()
   *
   * Set the method to be used.
   *
   * @param args The command line arguments.
   * @param x Current offset into the program.
   * @return The new offset into the command line parameters.
   **/
  private int method(String[] args, int x){
    /* TODO: Set the method to be used. */
    return x;
  }

  /**
   * output()
   *
   * The output image format.
   *
   * @param args The command line arguments.
   * @param x Current offset into the program.
   * @return The new offset into the command line parameters.
   **/
  private int output(String[] args, int x){
    /* TODO: The output image format. */
    return x;
  }

  /**
   * speed()
   *
   * Set the desired conversion speed.
   *
   * @param args The command line arguments.
   * @param x Current offset into the program.
   * @return The new offset into the command line parameters.
   **/
  private int speed(String[] args, int x){
    /* TODO: Get the desired conversion speed. */
    return x;
  }

  /**
   * scale)
   *
   * Set the desired output scale.
   *
   * @param args The command line arguments.
   * @param x Current offset into the program.
   * @return The new offset into the command line parameters.
   **/
  private int scale(String[] args, int x){
    /* TODO: Set the output scale. */
    return x;
  }

  /**
   * version()
   *
   * Display version information and then quit.
   *
   * @param args The command line arguments.
   * @param x Current offset into the program.
   * @return The new offset into the command line parameters.
   **/
  private int version(String[] args, int x){
    System.out.println(VER_MAJOR + "." + VER_MINOR + "." + VER_PATCH);
    System.exit(0);
    return x;
  }
}
