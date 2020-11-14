package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    private static final String address = "127.0.0.1";
    private static final int port = 23456;
    @Parameter(names = {"-t"})
    private static String type;
    @Parameter(names = {"-k"})
    private static String key;
    @Parameter(names = {"-v"})
    private static String value;
    @Parameter(names = {"-in"})
    private static String fileName;

    public static void main(String[] args) {
        Main main = new Main();

        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);
        main.run();
    }

    public void run() {
        System.out.println("Client started!");
        try (
                Socket socket = new Socket(InetAddress.getByName(address), port);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String fileContent = "";
            if (fileName == null) {
                fileContent = JsonBuilder.newBuilder()
                        .addValue("type", type)
                        .addValue("key", key)
                        .addValue("value", value).getAsString();
            } else {
                String relativePath = "src/client/data/";
                try {
                    fileContent = new String(Files.readAllBytes(Paths.get(relativePath + fileName)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            output.writeUTF(fileContent);
            System.out.println("Sent: " + fileContent);
            String msgReceived = input.readUTF().replace("\\\\", "");
            System.out.println("Received: " + msgReceived);
        } catch (IOException e) {
            System.out.println("Error! The server is offline.");
        }
    }
}