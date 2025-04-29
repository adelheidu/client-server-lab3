package org.example.server;

import org.example.objects.Serializer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final int PORT = 4445;
    private InetAddress address;
    private DatagramSocket socket;

    private Serializer serializer;

    private List<Integer> clients;

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
                    System.out.println("Active clients: " + clients);
                    notifyClients();
                } else if (message.equals("close")) {
                    clients.remove(Integer.valueOf(receivePacket.getPort()));
                    System.out.println("Active clients: " + clients);
                    notifyClients();
                } else if (message.startsWith("send")) {
                    send(message);
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

    private void send(String message) {
        String[] parts = message.split("//");
        sendDatagram(parts[2], Integer.parseInt(parts[1]));
    }

    private void notifyClients() {
        clients.forEach(client -> {
            StringBuilder stringBuilder = new StringBuilder("clients//");
            clients.forEach(port ->{
                if (client != port){
                    stringBuilder.append(port).append("//");
                }
            });
            String message = stringBuilder.toString();
            System.out.println(message);
            sendDatagram(message, client);
        });
    }

}
