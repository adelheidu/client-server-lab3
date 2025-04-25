package org.example.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({Rectangle.class, Oval.class, Triangle.class})
@XmlRootElement

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Rectangle.class, name = "rectangle"),
        @JsonSubTypes.Type(value = Oval.class, name = "oval"),
        @JsonSubTypes.Type(value = Triangle.class, name = "triangle")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class GraphicObject {

    private int x;
    private int y;

    public GraphicObject() {}

    public GraphicObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @XmlAttribute
    public int getX() {
        return x;
    }

    @XmlAttribute
    public int getY() {
        return y;
    }

    @XmlAttribute
    public abstract String getTypeName();

}
