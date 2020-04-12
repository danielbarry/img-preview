package b.img;

/**
 * ElementRect.java
 *
 * Abstraction of the SVG rectangle.
 **/
public class ElementRect extends Element{
  private double x;
  private double y;
  private double w;
  private double h;

  /**
   * ElementRect()
   *
   * Construct minimum rectangle.
   *
   * @param x Upper left corner.
   * @param y Upper left corner.
   * @param w Width of element.
   * @param h Height of element.
   **/
  public ElementRect(double x, double y, double w, double h){
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }

  /**
   * ElementRect()
   *
   * Construct styled rectangle.
   *
   * @param x Upper left corner.
   * @param y Upper left corner.
   * @param w Width of element.
   * @param h Height of element.
   * @param s A style to be assigned.
   **/
  public ElementRect(double x, double y, double w, double h, String s){
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    addStyle(s);
  }

  @Override
  public String toString(){
    String add = "";
    if(style != null && style.length() > 0){
      add += " style=\"" + style + "\"";
    }
    return "<rect" +
      " x=\"" + x + "\"" +
      " y=\"" + y + "\"" +
      " width=\"" + w + "\"" +
      " height=\"" + h + "\"" +
      add +
    "/>";
  }
}
