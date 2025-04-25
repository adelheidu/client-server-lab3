package org.example.objects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Rectangle extends GraphicObject {

    private int width;
    private int height;

    public Rectangle() {}

    @Override
    public String getTypeName() {
        return "rectangle";
    }

    public Rectangle(int x, int y, int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    @XmlAttribute
    public int getWidth() {
        return width;
    }

    @XmlAttribute
    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return getTypeName() + " " + getX() + " " + getY() + " " + width + " " + height;
    }

}
