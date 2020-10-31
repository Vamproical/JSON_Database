package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Main {
    private static final String address = "127.0.0.1";
    private static final int port = 23456;

    @Parameter(names={"--typeRequest", "-t"})
    String typeRequest;
    @Parameter(names={"--index", "-i"})
    int index;
    @Parameter(names={"--data", "-m"})
    String data;

    private void server() {
        try (
                Socket socket = new Socket(InetAddress.getByName(address), port);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String msg = typeRequest + " " + index;
            if (typeRequest.equals("set")) {
                msg = typeRequest + " " + index + " " + data;
            }

            output.writeUTF(msg);
            System.out.println("Sent: " + msg);
            String result = input.readUTF();
            System.out.println("Received: " + result);
        } catch (IOException ignored) {

        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        System.out.println("Client started!");
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);
        main.server();
    }
}