package client;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;
import server.ArgsParser;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    private static final String address = "127.0.0.1";
    private static final int port = 23456;
    private static final Map<String,String> arguments = new LinkedHashMap<>();

    private static void parseArgument(String... clientArgs) {
        ArgsParser argsParser = new ArgsParser();
        JCommander.newBuilder()
                .addObject(argsParser)
                .build()
                .parse(clientArgs);
        arguments.put("type",argsParser.getRequestType());
        if (!argsParser.getKey().equals("")) {
            arguments.put("key",argsParser.getKey());
        }
        if (!argsParser.getValue().equals("")) {
            arguments.put("value",argsParser.getValue());
        }
    }

    public static void main(String[] args) {
        System.out.println("Client started!");
        try (
                Socket socket = new Socket(address, port);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())
        ) {
            output.writeObject(args);
            parseArgument(args);
            Gson gson = new Gson();
            String out = gson.toJson(arguments);

            System.out.println("Sent: " + out);
            System.out.println("Received: " + input.readUTF());
        } catch (IOException e) {
            System.out.println("Error! The server is offline.");
        }
    }
}