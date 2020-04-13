package b.img;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import javax.imageio.ImageIO;

/**
 * ProcessScale.java
 *
 * Scale an image to a given size.
 **/
public class ProcessScale implements Process, Runnable{
  /**
   * ProcessScale.BitField.java
   *
   * Generate a bit field for the purpose of generating compact SVG graphics.
   **/
  private class BitField{
    private int id;
    private int colour;
    private boolean[][] mask;
    private HashSet<Integer> children;
    private Element elem;

    /**
     * BitField()
     *
     * Generate a bit field from a multi-mask.
     *
     * @param multi The multi-mask to search for our bit field.
     * @param id The ID of this bit field.
     * @param colour Set the colour of the represented object.
     **/
    public BitField(int[][] multi, int id, int colour){
      /* Store variables and initiate variables */
      this.id = id;
      this.colour = colour;
      this.colour = ((colour & 0x0000F0) >>  4) |
                    ((colour & 0x00F000) >>  8) |
                    ((colour & 0xF00000) >> 12);
      children = new HashSet<Integer>();
      /* Generate bit field */
      mask = new boolean[multi.length][];
      ArrayList<double[]> pts = new ArrayList<double[]>();
      int globalX1 = multi[0].length;
      int globalY1 = -1;
      int globalX2 = -1;
      int globalY2 = -1;
      for(int y = 0; y < multi.length; y++){
        int start = -1;
        int end = -1;
        /* Store bit field information */
        mask[y] = new boolean[multi[y].length];
        for(int x = 0; x < multi[y].length; x++){
          /* Update bit mask */
          mask[y][x] = multi[y][x] == id;
          /* Store information about shape */
          if(mask[y][x]){
            if(start < 0){
              start = x;
            }
            end = x;
          }
        }
        /* Search for children */
        if(start >= 0){
          /* Update global measurements */
          if(start < globalX1){
            globalX1 = start;
          }
          if(globalY1 < 0){
            globalY1 = y;
          }
          if(end > globalX2){
            globalX2 = end;
          }
          globalY2 = y;
          /* Check whether there are children */
          for(int x = start; x <= end; x++){
            if(!mask[y][x]){
              children.add(multi[y][x]);
            }
          }
        }
        /* Store polygon points */
        pts.add(0, new double[]{start, y});
        pts.add(new double[]{end + 1, y});
        pts.add(0, new double[]{start, y + 1});
        pts.add(new double[]{end + 1, y + 1});
      }
      int width = globalX2 - globalX1;
      int height = globalY2 - globalY1;
      if(width == 0 || height == 0){
        /* Generate rectangle SVG element */
        elem = new ElementRect(
          globalX1,
          globalY1,
          width + 1,
          height + 1,
          "fill:#" + Integer.toHexString(colour)
        );
      }else{
        /* Generate polygon SVG element */
        elem = new ElementPoly(
          pts.toArray(new double[pts.size()][]),
          "fill:#" + Integer.toHexString(colour)
        );
      }
    }

    /**
     * getOverlayChildren()
     *
     * It's possible that some bit fields overlay this one and they must be
     * drawn after (on top).
     *
     * @return A set of unique children contained within.
     **/
    public HashSet<Integer> getOverlayChildren(){
      return children;
    }

    /**
     * getElement()
     *
     * Get an element that represents the bit field.
     *
     * @return The element representing this bit field.
     **/
    public Element getElement(){
      return elem;
    }
  }

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
        System.out.println("(warning) SVG scaling support is experimental.");
        /* Perform scalar sampling */
        SVG svg = null;
        switch(speed){
          case FAST :
            svg = svgFast(img, width, height);
            break;
          case NORMAL :
            svg = svgNormal(img, width, height);
            break;
          case SLOW :
            svg = svgSlow(img, width, height);
            break;
          default :
            System.err.println("(internal) Unsupported speed for SVG conversion.");
            break;
        }
        /* Save if it was generated successfully */
        if(svg != null){
          svg.save(output);
        }
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
   * @param height The height of the target image.
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
   * @param height The height of the target image.
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
   * @param height The height of the target image.
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

  /**
   * svgFast()
   *
   * Perform a fast conversion to scalar SVG format.
   *
   * @param input The input image.
   * @param width The width of the target image.
   * @param height The height of the target image.
   * @return The processed image.
   **/
  private SVG svgFast(BufferedImage input, int width, int height){
    SVG svg = new SVG(width, height);
    for(int y = 0; y < height; y++){
      for(int x = 0; x < width; x++){
        int c = input.getRGB(x, y) & 0xFFFFFF;
        c = ((c & 0x0000F0) >>  4) |
            ((c & 0x00F000) >>  8) |
            ((c & 0xF00000) >> 12);
        svg.addElement(
          new ElementRect(
            x,
            y,
            1,
            1,
            "fill:#" + Integer.toHexString(c)
          )
        );
      }
    }
    return svg;
  }

  /**
   * svgNormal()
   *
   * Perform a normal conversion to scalar SVG format.
   *
   * @param input The input image.
   * @param width The width of the target image.
   * @param height The height of the target image.
   * @return The processed image.
   **/
  private SVG svgNormal(BufferedImage input, int width, int height){
    SVG svg = new SVG(width, height);
    for(int y = 0; y < height; y++){
      for(int x = 0; x < width; x++){
        int c = input.getRGB(x, y) & 0xFFFFFF;
        int a = c;
        int w = 1;
        while(w + x < width && dist(c, input.getRGB(x + w, y) & 0xFFFFFF) < 32){
          a = avg(a, w, input.getRGB(x + w, y) & 0xFFFFFF);
          ++w;
        }
        svg.addElement(
          new ElementRect(
            x,
            y,
            w,
            1,
            "fill:#" + Integer.toHexString(a)
          )
        );
        x += w - 1;
      }
    }
    return svg;
  }

  /**
   * svgSlow()
   *
   * Perform a slow conversion to scalar SVG format.
   *
   * @param input The input image.
   * @param width The width of the target image.
   * @param height The height of the target image.
   * @return The processed image.
   **/
  private SVG svgSlow(BufferedImage input, int width, int height){
    /* Perform blocking */
    int block[][] = new int[height][];
    int count = 0;
    ArrayList<Integer> col = new ArrayList<Integer>();
    ArrayList<int[]> merge = new ArrayList<int[]>();
    for(int y = 0; y < height; y++){
      block[y] = new int[width];
      for(int x = 0; x < width; x++){
        int c = input.getRGB(x, y) & 0xFFFFFF;
        /* Up check */
        if(
          y > 0 &&
          dist(c, input.getRGB(x, y - 1) & 0xFFFFFF) < 32 &&
          dist(c, col.get(block[y - 1][x])) < 32
        ){
          block[y][x] = block[y - 1][x];
          /* Is merge required? */
          if(
            x > 0 &&
            dist(c, input.getRGB(x - 1, y) & 0xFFFFFF) < 32 &&
            dist(c, col.get(block[y][x - 1])) < 32
          ){
            /* Dominant (to replace), weak (to be replaced) */
            merge.add(new int[]{block[y][x], block[y][x - 1]});
          }
        /* Left check */
        }else if(
          x > 0 &&
          dist(c, input.getRGB(x - 1, y) & 0xFFFFFF) < 32 &&
          dist(c, col.get(block[y][x - 1])) < 32
        ){
          block[y][x] = block[y][x - 1];
        /* Default */
        }else{
          block[y][x] = count;
          col.add(c);
          ++count;
        }
      }
    }
    /* Perform merging */
    for(int y = 0; y < height; y++){
      for(int x = 0; x < width; x++){
        /* Merge count blocks */
        for(int i = 0; i < merge.size(); i++){
          if(block[y][x] == merge.get(i)[1]){
            block[y][x] = merge.get(i)[0];
            break;
          }
        }
      }
    }
  }

  /**
   * dist()
   *
   * Calculate the distance between two RGB pixels.
   *
   * @param i The first pixel.
   * @param j The second pixel.
   * @return The distance between the pixels.
   **/
  private double dist(int i, int j){
    double ir = (i >> 16) & 0xFF;
    double ig = (i >>  8) & 0xFF;
    double ib = (i      ) & 0xFF;
    double jr = (j >> 16) & 0xFF;
    double jg = (j >>  8) & 0xFF;
    double jb = (j      ) & 0xFF;
    return Math.sqrt(
      Math.pow(ir - jr, 2) +
      Math.pow(ig - jg, 2) +
      Math.pow(ib - jb, 2)
    );
  }

  /**
   * avg()
   *
   * Calculate the weighted average between two pixels.
   *
   * @oaram i The first pixel representing one more pixels.
   * @param w The weight of the first pixel based on how many pixels are
   * represented.
   * @param j The second pixel representing just itself.
   **/
  private int avg(int i, int w, int j){
    long ir = ((i >> 16) & 0xFF) * w;
    long ig = ((i >>  8) & 0xFF) * w;
    long ib = ((i      ) & 0xFF) * w;
    long jr = (j >> 16) & 0xFF;
    long jg = (j >>  8) & 0xFF;
    long jb = (j      ) & 0xFF;
    long r = (((ir + jr) / (w + 1)) & 0xFF) << 16;
    long g = (((ig + jg) / (w + 1)) & 0xFF) <<  8;
    long b = (((ib + jb) / (w + 1)) & 0xFF);
    return (int)(r | g | b);
  }
}
