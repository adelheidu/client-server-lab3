package org.example.client;

import org.example.objects.GraphicObject;

import java.util.List;

public interface ClientListener {

    void addButtonAction();
    void clearButtonAction();
    void closeButtonAction();
    void sendButtonAction(String name, GraphicObject object);
    void getButtonAction(String name);

    List<GraphicObject> getObjectList();

}
