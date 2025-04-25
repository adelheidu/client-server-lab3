package org.example.server;

import org.example.objects.GraphicObject;
import org.example.objects.Serializer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

    private static final int PORT = 4445;
    private InetAddress address;
    private DatagramSocket socket;

    private Serializer serializer;

    private List<Integer> clients;
    private static final Map<String, GraphicObject> objects = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Server is running!");
        Server server = new Server();
    }

    public Server() {
        try {
            clients = new ArrayList<>();
            serializer = new Serializer();
            address = InetAddress.getByName("localhost");
            socket = new DatagramSocket(PORT);
            byte[] receiveData = new byte[65507];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Incoming message: " + message);

                if (message.equals("start")){
                    clients.add(receivePacket.getPort());
                    notifyClients();
                    System.out.println("Active clients: " + clients);
                } else if (message.equals("close")) {
                    clients.remove(Integer.valueOf(receivePacket.getPort()));
                    System.out.println("Active clients: " + clients);
                } else if (message.startsWith("post")) {
                    post(message);
                    notifyClients();
                } else if (message.startsWith("get")) {
                    String name = message.split("//")[1];
                    sendDatagram(serializer.serializeXMLObject(objects.get(name)), receivePacket.getPort());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendDatagram(String message, int clientPort) {
        System.out.println("Send message: " + message);
        try {
            byte[] sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, clientPort);
            socket.send(sendPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void post(String message) {
        String[] parts = message.split("//");
        objects.put(parts[1], serializer.deserializeXMLObject(parts[2]));
    }

    private void notifyClients() {
        StringBuilder stringBuilder = new StringBuilder("objects//");
        objects.keySet().forEach(key -> stringBuilder.append(key).append("//"));
        String message = stringBuilder.toString();
        clients.forEach(client -> sendDatagram(message, client));
    }


}
