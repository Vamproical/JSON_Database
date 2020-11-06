package server;


import com.beust.jcommander.JCommander;
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    private static final String address = "127.0.0.1";
    private static final int PORT = 23456;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(address))) {
            Database database = new Database();
            System.out.println("Server started!");
            while (true) {
                try (
                        Socket socket = server.accept();
                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    Map<String, String> answer = new LinkedHashMap<>();
                    String[] clientArgs = (String[]) input.readObject();

                    ArgsParser argsParser = new ArgsParser();
                    JCommander.newBuilder()
                            .addObject(argsParser)
                            .build()
                            .parse(clientArgs);
                    String requestType = argsParser.getRequestType();
                    String key = argsParser.getKey();
                    String value = argsParser.getValue();
                    switch (requestType) {
                        case ("set"):
                            if (database.set(key,value)) {
                                answer.put("response", "OK");
                            } else {
                                answer.put("response", "ERROR");
                            }
                            break;
                        case ("get"):
                            String result = database.get(key);
                            if (result.equals("")) {
                                answer.put("response", "ERROR");
                                answer.put("reason", "No such key");
                            } else {
                                answer.put("response", "OK");
                                answer.put("value", result);
                            }
                            break;
                        case ("delete"):
                            if (database.delete(key)) {
                                answer.put("response", "OK");
                            } else {
                                answer.put("response", "ERROR");
                                answer.put("reason", "No such key");
                            }
                            break;
                        default:
                            System.exit(0);
                    }
                    Gson gson = new Gson();
                    String outputResult = gson.toJson(answer);
                    output.writeUTF(outputResult);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
