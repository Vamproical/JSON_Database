package server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteJson {
    public static void write(String content) {
        File file = new File("C:\\Users\\Никита\\IdeaProjects\\JSON Database\\JSON Database\\task\\src\\server\\data\\db.json");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            System.out.printf("An exception occurs %s", e.getMessage());
        }
    }
}
