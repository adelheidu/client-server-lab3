package org.example.objects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Triangle extends GraphicObject {

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public Triangle() {}

    @Override
    public String getTypeName() {
        return "triangle";
    }

    public Triangle(int x, int y, int x1, int y1, int x2, int y2) {
        super(x,y);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @XmlAttribute
    public int getX1() {
        return x1;
    }

    @XmlAttribute
    public int getY1() {
        return y1;
    }

    @XmlAttribute
    public int getX2() {
        return x2;
    }

    @XmlAttribute
    public int getY2() {
        return y2;
    }

    @Override
    public String toString() {
        return getTypeName() + " " + getX() + " " + getY() + " " + x1 + " " + y1 + " " + x2 + " " + y2;
    }

}
