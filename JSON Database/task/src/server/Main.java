package server;


import com.beust.jcommander.JCommander;
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
                    Gson gson = new Gson();
                    ReadWriteLock lock = new ReentrantReadWriteLock();
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    String requestType, key, value, outputResult;
                    ArgsParser argsParser = new ArgsParser();
                    JCommander.newBuilder()
                            .addObject(argsParser)
                            .build()
                            .parse(clientArgs);
                    if (argsParser.getFileName().equals("")) {
                        requestType = argsParser.getType();
                        key = argsParser.getKey();
                        value = argsParser.getValue();
                    } else {
                        Map<String, String> temp = ReadJson.read("C:\\Users\\Никита\\IdeaProjects\\JSON Database\\JSON Database\\task\\src\\client\\data\\" + argsParser.getFileName());
                        requestType = temp.get("type");
                        key = temp.get("key");
                        value = temp.getOrDefault("value", "");
                    }
                    switch (requestType) {
                        case ("set"):
                            executor.submit(() -> database.set(key, value));
                            answer.put("response", "OK");
                            break;
                        case ("get"):
                            Future<String> result = executor.submit(() ->  database.get(key));
                            String res = result.get();
                            if (res.equals("")) {
                                answer.put("response", "ERROR");
                                answer.put("reason", "No such key");
                            } else {
                                answer.put("response", "OK");
                                answer.put("value", res);
                            }
                            break;
                        case ("delete"):
                            Future<Boolean> bool = executor.submit(() -> database.delete(key));
                            boolean bool1 = bool.get();
                            if (bool1) {
                                answer.put("response", "OK");
                            } else {
                                answer.put("response", "ERROR");
                                answer.put("reason", "No such key");
                            }
                            break;
                        default:
                            executor.shutdown();
                            System.exit(0);
                            answer.put("response", "OK");
                            try {
                                if (!executor.awaitTermination(200, TimeUnit.MILLISECONDS)) {
                                    executor.shutdownNow();
                                    if (!executor.awaitTermination(200, TimeUnit.MILLISECONDS)) {
                                        System.err.println("Executor did not terminate");
                                    }
                                }
                            } catch (InterruptedException ie) {
                                executor.shutdownNow();
                                Thread.currentThread().interrupt();
                            }

                    }
                    outputResult = gson.toJson(answer);
                    WriteJson.write(outputResult);
                    output.writeUTF(outputResult);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
