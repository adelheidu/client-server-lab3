package org.example.client;

import org.example.objects.GraphicObject;
import org.example.objects.Oval;
import org.example.objects.Rectangle;
import org.example.objects.Triangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

public class Frame extends JFrame {

    JPanel buttonPanel;
    JPanel drawingPanel;
    JPanel serverPanel;
    JScrollPane scrollPane;
    JList<String> list;
    DefaultListModel<String> listModel;

    JButton addButton;
    JButton sendButton;
    JButton clearButton;
    JButton closeButton;

    GraphicObject selectedObject = null;

    private ClientListener clientListener;

    public Frame(ClientListener clientListener, int port) {

        super("Клиент " + port);
        this.clientListener = clientListener;
        initialize();

    }

    private void initialize() {
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.setPreferredSize(new Dimension(500, 40));

        addDrawingPanel();

        serverPanel = new JPanel();
        serverPanel.setBackground(Color.LIGHT_GRAY);
        serverPanel.setPreferredSize(new Dimension(200, 700));

        addButton = new JButton("Добавить");
        sendButton = new JButton("Отправить");
        clearButton = new JButton("Удалить");
        closeButton = new JButton("Закрыть");

        buttonPanel.add(addButton);
        buttonPanel.add(sendButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(closeButton);

        onAddButtonClick();
        onSendButtonClick();
        onClearButtonClick();
        onCloseButtonClick();
        onMouseButtonClick();

        list = new JList<>();
        listModel = new DefaultListModel<>();
        list.setModel(listModel);
        scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(185, 550));

        serverPanel.add(scrollPane, BorderLayout.NORTH);


        add(buttonPanel, BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);
        add(serverPanel, BorderLayout.EAST);

        setVisible(true);
    }

    public void addDrawingPanel() {
        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                clientListener.getObjectList().forEach(object -> {
                    if (object instanceof Rectangle) drawRectangle(g, (Rectangle) object);
                    else if (object instanceof Oval) drawOval(g, (Oval) object);
                    else if (object instanceof Triangle) drawTriangle(g, (Triangle) object);
                });
                if (selectedObject != null) {
                    if (selectedObject instanceof Rectangle) drawRectangleBorder(g, (Rectangle) selectedObject);
                    if (selectedObject instanceof Oval) drawOvalBorder(g, (Oval) selectedObject);
                    if (selectedObject instanceof Triangle) drawTriangleBorder(g, (Triangle) selectedObject);
                }
            }
        };
        drawingPanel.setBackground(Color.WHITE);
    }

    private void drawRectangle(Graphics g, Rectangle rectangle) {
        g.setColor(Color.PINK);
        g.fillRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
    }

    private void drawOval(Graphics g, Oval oval) {
        g.setColor(Color.ORANGE);
        g.fillOval(oval.getX(), oval.getY(), oval.getRadiusX(), oval.getRadiusY());
    }

    private void drawTriangle(Graphics g, Triangle triangle) {
        g.setColor(Color.CYAN);
        g.fillPolygon(
                new int[]{triangle.getX(), triangle.getX1(), triangle.getX2()},
                new int[]{triangle.getY(), triangle.getY1(), triangle.getY2()},
                3
        );
    }

    private void drawRectangleBorder(Graphics g, Rectangle rectangle) {
        g.setColor(Color.RED);
        g.drawRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
    }

    private void drawTriangleBorder(Graphics g, Triangle triangle) {
        g.setColor(Color.RED);
        g.drawPolygon(
                new int[]{triangle.getX(), triangle.getX1(), triangle.getX2()},
                new int[]{triangle.getY(), triangle.getY1(), triangle.getY2()},
                3
        );
    }

    private void drawOvalBorder(Graphics g, Oval oval) {
        g.setColor(Color.RED);
        g.drawOval(oval.getX(), oval.getY(), oval.getRadiusX(), oval.getRadiusY());
    }

    public void repaintDrawingPanel() {
        drawingPanel.repaint();
    }

    public void updateList(List<String> clients) {
        Collections.sort(clients);
        listModel.removeAllElements();
        listModel.addAll(clients);
    }

    private void onAddButtonClick() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientListener.addButtonAction();
            }
        });
    }

    private void onSendButtonClick() {
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = list.getSelectedValue();
                if (selectedObject != null && name != null) {
                    clientListener.sendButtonAction(name, selectedObject);
                }
            }
        });
    }

    private void onClearButtonClick() {
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedObject = null;
                clientListener.clearButtonAction();
            }
        });
    }

    private void onCloseButtonClick() {
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientListener.closeButtonAction();
            }
        });
    }

    private void onMouseButtonClick() {
        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (e.getButton() == MouseEvent.BUTTON3) {
                    selectedObject = null;
                    drawingPanel.repaint();
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    clientListener.getObjectList().forEach(object -> {
                        if (object instanceof Rectangle) isRectangle(x, y, (Rectangle) object);
                        if (object instanceof Oval) isOval(x, y, (Oval) object);
                        if (object instanceof Triangle) isTriangle(x, y, (Triangle) object);
                    });
                }

            }
        });
    }

    private void isRectangle(int x, int y, Rectangle rectangle) {
        if (x > rectangle.getX() && x < rectangle.getX() + rectangle.getWidth()
                && y > rectangle.getY() && y < rectangle.getY() + rectangle.getHeight()) {
            selectedObject = rectangle;
            drawingPanel.repaint();
        }
    }

    private void isOval(int x, int y, Oval oval) {
        double a = oval.getRadiusX() / 2.0;
        double b = oval.getRadiusY() / 2.0;
        double centerX = oval.getX() + a;
        double centerY = oval.getY() + b;

        double dx = (x - centerX) / a;
        double dy = (y - centerY) / b;

        if ((dx * dx + dy * dy) <= 1.0) {
            selectedObject = oval;
            drawingPanel.repaint();
        }
    }

    private void isTriangle(int x, int y, Triangle triangle) {
        int x1 = triangle.getX();
        int y1 = triangle.getY();
        int x2 = triangle.getX1();
        int y2 = triangle.getY1();
        int x3 = triangle.getX2();
        int y3 = triangle.getY2();
        double epsilon = 1e-10;
        double sumAreas = triangleArea(x, y, x1, y1, x2, y2)
                + triangleArea(x, y, x1, y1, x3, y3)
                + triangleArea(x, y, x3, y3, x2, y2);
        double area = triangleArea(x3, y3, x1, y1, x2, y2);
        if (Math.abs(area - sumAreas) < epsilon) {
            selectedObject = triangle;
            drawingPanel.repaint();
        }
    }

    private double triangleArea(int x1, int y1, int x2, int y2, int x3, int y3) {
        return Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0);
    }

}
