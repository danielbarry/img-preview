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
    /* Loop the command line parameters */
    for(int x = 0; x < args.length; x++){
      switch(args[x]){
        case "-a" :
        case "--about" :
          x = about(args, x);
          break;
        case "-h" :
        case "--help" :
          x = help(args, x);
          break;
        case "-v" :
        case "--version" :
          x = version(args, x);
          break;
        default :
          /* TODO: Parse default parameters. */
          break;
      }
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
    System.out.println("    -h  --help     Display this help");
    System.out.println("    -v  --version  Display program version");
    System.exit(0);
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
