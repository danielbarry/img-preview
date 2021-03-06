package b.img;

/**
 * Element.java
 *
 * An abstraction of the SVG elements.
 **/
public abstract class Element{
  private String style;

  /**
   * addStyle()
   *
   * Adds a style to the element.
   *
   * @param style The style to be added.
   * @return A reference to this element.
   **/
  public Element addStyle(String style){
    /* Make sure the style is valid */
    if(style != null){
      /* Style already set? */
      if(this.style == null || this.style.length() <= 0){
        this.style = style;
      }else{
        this.style += ";" + style;
      }
    }else{
      /* Fail silently */
    }
    return this;
  }

  /**
   * getStyle()
   *
   * Get all style information.
   *
   * @return The style information, otherwise NULL.
   **/
  public String getStyle(){
    return style;
  }

  /**
   * toString()
   *
   * Create a String representation of this element.
   *
   * @return A printable version of this element.
   **/
  @Override
  public String toString(){
    return "<!--?-->";
  }

  /**
   * numToString()
   *
   * Convert a number to the smallest version of a String as possible.
   *
   * @param v The value to be converted.
   * @return The String representation of the value.
   **/
  public static String numToString(double v){
    if((double)((int)v) == v){
      return Integer.toString((int)v);
    }else{
      return Double.toString(v);
    }
  }
}
