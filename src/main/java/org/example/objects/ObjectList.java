package org.example.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ObjectList {

    private List<GraphicObject> objects;

    public ObjectList() {
        objects = new ArrayList<>();
    }

    public void generateFigure() {
        Random random = new Random();
        int x, y, width, height;
        int figureType = random.nextInt(3);

        width = random.nextInt(71) + 30;
        height = random.nextInt(71) + 30;
        x = random.nextInt(750 - width);
        y = random.nextInt(590 - height);

        if (figureType == 0) {
            objects.add(new Rectangle(x, y, width, height));
        } else if (figureType == 1) {
            objects.add(new Oval(x, y, width, height));
        } else {
            objects.add(new Triangle(
                    x, y, x, y + height, x + width, y + height
            ));
        }
    }

    public List<GraphicObject> getObjects() {
        return objects;
    }

    public void clear() {
        objects.clear();
    }

    public void add(GraphicObject object) {
        objects.add(object);
    }

}
