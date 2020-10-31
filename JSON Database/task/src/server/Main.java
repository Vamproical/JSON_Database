package server;


import com.beust.jcommander.Parameter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final String address = "127.0.0.1";
    private static final int PORT = 23456;
    private static String typeRequest;
    private static int index;
    private static String data;

    private static void parseCommand(String msg) {
        String[] msgArray = msg.split(" ");
        typeRequest = msgArray[0];
        index = Integer.parseInt(msgArray[1]);
        if (typeRequest.equals("set")) {
            StringBuilder builder = new StringBuilder();
            for (int i = 2; i < msgArray.length; i++) {
                builder.append(msgArray[i]).append(" ");
            }
            data = builder.toString();
        }
    }

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(address))) {
            Database database = new Database();
            System.out.println("Server started!");
            while (true) {
                try (
                        Socket socket = server.accept();
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    String msg = input.readUTF();
                    if (msg.contains("exit")) {
                        return;
                    }
                    parseCommand(msg);
                    String result = database.runSystem(typeRequest,index,data);
                    output.writeUTF(result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
