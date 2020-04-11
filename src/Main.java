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
  private boolean quiet;

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
    quiet = false;
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
        case "-q" :
        case "--quiet" :
          x = quiet(args, x);
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
          error("Unknown param '" + args[x] + "', see '--help'");
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
      if(!convert.isReady()){
        error("Unable to start the conversion process.");
      }
      convert.process();
      if(!quiet){
        /* Display conversion progress */
        while(convert.progress() < 1.0f){
          System.out.println("Progress: " + (100.0f * convert.progress()) + "%");
          try{
            Thread.sleep(1000);
          }catch(InterruptedException e){
            /* Do nothing */
          }
        }
      }
    }else{
      error("No input images given.");
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
    /* Make sure enough parameters exist */
    if(x + 1 >= args.length){
      error("Not enough parameters provided.");
    }
    /* Perform conversion */
    ++x;
    switch(args[x]){
      case "png" :
        format = Convert.FORMAT.PNG;
        break;
      case "jpeg" :
        format = Convert.FORMAT.JPEG;
        break;
      case "svg" :
        format = Convert.FORMAT.SVG;
        break;
      default :
        error("Unknown format '" + args[x] + "'.");
        break;
    }
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
    ++x;
    /* Count input files */
    int count = 0;
    for(int i = x; i < args.length; i++){
      /* Make sure parameter indicator doesn't exist */
      if(args[i].charAt(0) != '-'){
        ++count;
      }else{
        break;
      }
    }
    /* Make sure there is something to input */
    if(count < 1){
      error("No inputs provided.");
    }
    /* Create new input array and store */
    int offset;
    if(input == null){
      offset = 0;
      input = new String[count];
    }else{
      offset = input.length;
      String[] temp = new String[input.length + count];
      /* Copy existing */
      for(int i = 0; i < input.length; i++){
        temp[i] = input[i];
      }
      /* Finalize */
      input = temp;
    }
    /* Copy new */
    for(int i = 0; i < count; i++){
      input[offset + i] = args[x + i];
    }
    return x + count - 1;
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
    /* Make sure enough parameters exist */
    if(x + 1 >= args.length){
      error("Not enough parameters provided.");
    }
    /* Perform conversion */
    ++x;
    try{
      jobs = Integer.parseInt(args[x]);
    }catch(NumberFormatException e){
      error("Unable to convert number '" + args[x] + "'.");
    }
    /* Check output is sane */
    if(jobs < 1 || jobs > 1024){
      error("Invalid number of jobs '" + jobs + "'.");
    }
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
    System.out.println("    -q  --quiet    No conversion progress printing");
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
    /* Make sure enough parameters exist */
    if(x + 1 >= args.length){
      error("Not enough parameters provided.");
    }
    /* Perform conversion */
    ++x;
    switch(args[x]){
      case "scale" :
        method = Convert.METHOD.SCALE;
        break;
      default :
        error("Unknown method '" + args[x] + "'.");
        break;
    }
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
    /* Make sure enough parameters exist */
    if(x + 1 >= args.length){
      error("Not enough parameters provided.");
    }
    /* Perform conversion */
    ++x;
    output = args[x];
    return x;
  }

  /**
   * quiet()
   *
   * Set the quiet flag.
   *
   * @param args The command line arguments.
   * @param x Current offset into the program.
   * @return The new offset into the command line parameters.
   **/
  private int quiet(String[] args, int x){
    quiet = true;
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
    /* Make sure enough parameters exist */
    if(x + 1 >= args.length){
      error("Not enough parameters provided.");
    }
    /* Perform conversion */
    ++x;
    switch(args[x]){
      case "fast" :
        speed = Convert.SPEED.FAST;
        break;
      case "normal" :
        speed = Convert.SPEED.NORMAL;
        break;
      case "slow" :
        speed = Convert.SPEED.SLOW;
        break;
      default :
        error("Unknown speed '" + args[x] + "'.");
        break;
    }
    return x;
  }

  /**
   * scale()
   *
   * Set the desired output scale.
   *
   * @param args The command line arguments.
   * @param x Current offset into the program.
   * @return The new offset into the command line parameters.
   **/
  private int scale(String[] args, int x){
    /* Make sure enough parameters exist */
    if(x + 1 >= args.length){
      error("Not enough parameters provided.");
    }
    /* Perform conversion */
    try{
      ++x;
      scaleWidth = Integer.parseInt(args[x]);
      ++x;
      scaleHeight = Integer.parseInt(args[x]);
    }catch(NumberFormatException e){
      error("Unable to convert number '" + args[x] + "'.");
    }
    /* Check output is sane */
    if(scaleWidth < 1 || scaleHeight < 1){
      error("Invalid scale width or height.");
    }
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

  /**
   * error()
   *
   * Display an error and then exit the program.
   *
   * @param msg The message to be displayed.
   **/
  private static void error(String msg){
    System.err.println("[!!] " + msg);
    System.exit(0);
  }
}
