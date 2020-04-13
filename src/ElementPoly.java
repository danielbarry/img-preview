package b.img;

/**
 * ElementPoly.java
 *
 * Abstraction of the SVG polygon.
 **/
public class ElementPoly extends Element{
  private double[][] pts;

  /**
   * ElementPoly()
   *
   * Construct minimum polygon.
   *
   * @param pts The polygon points.
   **/
  public ElementPoly(double[][] pts){
    this.pts = pts;
  }

  /**
   * ElementPoly()
   *
   * Construct styled polygon.
   *
   * @param pts The polygon points.
   * @param s A style to be assigned.
   **/
  public ElementPoly(double[][] pts, String s){
    this.pts = pts;
    addStyle(s);
  }

  @Override
  public String toString(){
    String p = "";
    for(int y = 0; y < pts.length; y++){
      if(y > 0){
        p += " ";
      }
      p += Element.numToString(pts[y][0]) + "," + Element.numToString(pts[y][1]);
    }
    String add = "";
    if(getStyle() != null && getStyle().length() > 0){
      add += " style=\"" + getStyle() + "\"";
    }
    return "<polygon" +
      " points=\"" + p + "\"" +
      add +
    "/>";
  }
}
