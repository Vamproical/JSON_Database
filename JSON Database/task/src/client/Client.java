package client;

import java.util.Scanner;

public class Client {
    private final String[] json;

    public Client() {
        json = new String[100];
        for (int i = 0; i < 100; i++) {
            json[i] = "";
        }
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
                }  else {
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
