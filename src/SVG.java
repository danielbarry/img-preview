package b.img;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * SVG.java
 *
 * An abstraction of the SVG data format.
 **/
public class SVG{
  private int width;
  private int height;
  private ArrayList<String> defines;
  private ArrayList<Element> elements;

  /**
   * SVG()
   *
   * Create an instance of a new SVG.
   *
   * @param width Image width.
   * @param hieght Image height.
   **/
  public SVG(int width, int height){
    this.width = width;
    this.height = height;
    defines = new ArrayList<String>();
    elements = new ArrayList<Element>();
  }

  /**
   * addDefine()
   *
   * Add a raw definition to the SVG header.
   *
   * @param define The raw element to be added.
   * @return A pointer to this object for stringing commands.
   **/
  public SVG addDefine(String define){
    defines.add(define);
    return this;
  }

  /**
   * addElement()
   *
   * Add an element to the SVG.
   *
   * @param e The element to be added.
   * @return An instance of this SVG object.
   **/
  public SVG addElement(Element e){
    elements.add(e);
    return this;
  }

  /**
   * getElement()
   *
   * Get a specific element by index.
   *
   * @param index The index to retrieve the element.
   * @return The specific element, otherwise NULL.
   **/
  public Element getElement(int index){
    if(index > 0 && index < elements.size()){
      return elements.get(index);
    }else{
      return null;
    }
  }

  /**
   * getElemments()
   *
   * Get the raw elements for this SVG.
   *
   * @return An array of raw elements.
   **/
  public ArrayList<Element> getElements(){
    return elements;
  }

  /**
   * getNumElements()
   *
   * Get the current number of elements.
   *
   * @return The current number of elements in this SVG.
   **/
  public int getNumElements(){
    return elements.size();
  }

  /**
   * save()
   *
   * Save the SVG to disk.
   *
   * @param out The file to be written to.
   **/
  public void save(File out){
    try{
      FileWriter fw = new FileWriter(out.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write("<svg width=\"" + width + "\" height=\"" + height + "\" ");
      bw.write("xmlns=\"http://www.w3.org/2000/svg\">");
      if(defines.size() > 0){
        bw.write("<defs>");
        for(String d : defines){
          bw.write(d);
        }
        bw.write("</defs>");
      }
      for(Element e : elements){
        bw.write(e.toString());
      }
      bw.write("</svg>");
      bw.close();
    }catch(IOException e){
      System.err.println("(internal) Unable to save SVG.");
    }
  }

  /**
   * toString()
   *
   * A printable version of this object. This is very slow and should be
   * avoided, it can consume a lot of RAM!
   *
   * @return A printable version of this object.
   **/
  @Override
  public String toString(){
    String add = "";
    for(Element e : elements){
      add += e;
    }
    if(defines.size() > 0){
      for(String d : defines){
        add += d;
      }
    }
    return
      "<svg width=\"" + width + "\" height=\"" + height + "\" " +
        "xmlns=\"http://www.w3.org/2000/svg\">" +
        add +
      "</svg>";
  }
}
