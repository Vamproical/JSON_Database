package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private final String[] json;

    public Client() {
        json = new String[100];
        for (int i = 0; i < 100; i++) {
            json[i] = "";
        }
    }

    public static void connection() throws IOException {
        String address = "127.0.0.1";
        int port = 23456;
        Socket socket = new Socket(InetAddress.getByName(address), port);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        System.out.println("Client started!");
        String msg = "Give me a record # 12";

        output.writeUTF(msg);
        System.out.println("Sent: " + msg);

        String receivedMsg = input.readUTF(); // response message
        System.out.println("Received: " + receivedMsg);
    }

    public void runSystem() {
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        while (!command.equals("exit")) {
            if (command.startsWith("set")) {
                String[] parse = command.split(" ");
                int index = Integer.parseInt(parse[1]);
                if (index > 100 || index < 1) {
                    System.out.println("ERROR");
                } else {
                    json[index - 1] = command.substring(6);
                    System.out.println("OK");
                }
            } else if (command.startsWith("get")) {
                String[] parse = command.split(" ");
                int index = Integer.parseInt(parse[1]);
                if (index > 100 || index < 1 || json[index - 1].equals("")) {
                    System.out.println("ERROR");
                } else {
                    System.out.println(json[index - 1]);
                }
            } else if (command.startsWith("delete")) {
                String[] parse = command.split(" ");
                int index = Integer.parseInt(parse[1]);
                if (index > 100 || index < 1) {
                    System.out.println("ERROR");
                } else {
                    json[index - 1] = "";
                    System.out.println("OK");
                }
            }
            command = scanner.nextLine();
        }
    }

}
