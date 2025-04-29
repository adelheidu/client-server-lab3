package org.example.client;

import org.example.objects.GraphicObject;
import org.example.objects.ObjectList;
import org.example.objects.Serializer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client implements ClientListener {

    private static final int SERVER_PORT = 4445;

    private ObjectList objectList;
    private Frame frame;
    private Thread thread;

    private Serializer serializer;

    private DatagramSocket socket;
    private InetAddress serverAddress;

    public static void main(String[] args) {
        Client client = new Client();
    }

    public Client() {
        objectList = new ObjectList();
        serializer = new Serializer();
        try {
            serverAddress = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        try {
            socket = new DatagramSocket();
            System.out.println(socket.getLocalPort());
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        frame = new Frame(this, socket.getLocalPort());
        sendDatagram("start");
        createThread();
    }

    private void createThread() {
        thread = new Thread(() -> {
            try {
                while (true) {
                    byte[] receiveData = new byte[65507];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    socket.receive(receivePacket);
                    String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println("Incoming message: " + message);

                    if (message.startsWith("clients")){
                        List<String> list = new ArrayList<>(Arrays.asList(message.split("//")));
                        list.removeFirst();
                        frame.updateList(list);
                    } else {
                        objectList.add(serializer.deserializeXMLObject(message));
                        frame.repaintDrawingPanel();
                    }

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }


    private void sendDatagram(String message) {
        System.out.println("Send message: " + message);
        try {
            byte[] sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);
            socket.send(sendPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GraphicObject> getObjectList() {
        return objectList.getObjects();
    }

    @Override
    public void addButtonAction() {
        objectList.generateFigure();
        frame.repaintDrawingPanel();
    }

    @Override
    public void clearButtonAction() {
        objectList.clear();
        frame.repaintDrawingPanel();
    }

    @Override
    public void closeButtonAction() {
        sendDatagram("close");
        frame.dispose();
        thread.interrupt();
        System.exit(0);
    }

    @Override
    public void sendButtonAction(String name, GraphicObject object) {
        sendDatagram("send" + "//" + name + "//" + serializer.serializeXMLObject(object));
    }

}
