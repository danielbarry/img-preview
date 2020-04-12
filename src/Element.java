package b.img;

/**
 * Element.java
 *
 * An abstraction of the SVG elements.
 **/
public abstract class Element{
  public String style;

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
}
