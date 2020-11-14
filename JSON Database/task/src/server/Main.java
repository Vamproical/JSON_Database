package server;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    private static final String address = "127.0.0.1";
    private static final int port = 23456;

    public static void main(String[] args) {
        Database database = new Database();
        int poolSize = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        System.out.println("Server started!");
        boolean flag = true;
        while (flag) {
            try (ServerSocket server = new ServerSocket(port,50,  InetAddress.getByName(address))) {
                Session session = new Session(server.accept(), database);
                Future<Boolean> exitFlag = executor.submit(session);
                System.out.println(exitFlag.get());
                if (exitFlag.get()) {
                    executor.shutdown();
                    flag = false;
                }
            } catch (IOException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}