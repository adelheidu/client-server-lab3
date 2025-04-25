package org.example.objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class Serializer {

    private XmlMapper xmlMapper;

    public Serializer() {
        xmlMapper = new XmlMapper();
    }

    public String serializeXMLObject(GraphicObject object) {
        String xmlObject;
        try {
            xmlObject = xmlMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return xmlObject;
    }

    public GraphicObject deserializeXMLObject(String xmlObject) {
        GraphicObject object;
        try {
            object = xmlMapper.readValue(xmlObject, GraphicObject.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

}
