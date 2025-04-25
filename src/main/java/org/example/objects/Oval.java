package org.example.objects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Oval extends GraphicObject {

    private int radiusX;
    private int radiusY;

    public Oval() {}

    @Override
    public String getTypeName() {
        return "oval";
    }

    public Oval(int x, int y, int radiusX, int radiusY) {
        super(x, y);
        this.radiusX = radiusX;
        this.radiusY = radiusY;
    }

    @XmlAttribute
    public int getRadiusX() {
        return radiusX;
    }

    @XmlAttribute
    public int getRadiusY() {
        return radiusY;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return getTypeName() + " " + getX() + " " + getY() + " " + radiusX + " " + radiusY;
    }

}
