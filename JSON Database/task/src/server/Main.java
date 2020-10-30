package server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final int PORT = 23456;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started!");
            try (
                    Socket socket = server.accept(); // accepting a new client
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream())
            ) {
                String msg = input.readUTF(); // reading a message
                System.out.println("Received: " + msg);

                String processedMessage = "A record # N was sent!";
                output.writeUTF(processedMessage); // resend it to the client
                System.out.println("Sent: " + processedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
