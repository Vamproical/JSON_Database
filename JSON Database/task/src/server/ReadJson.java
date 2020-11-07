package server;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class ReadJson {
    public static Map<String, String> read(String filename) {
        File file = new File(filename);
        Map<String, String> result = new LinkedHashMap<>();
        Gson gson = new Gson();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String parse = scanner.nextLine();
                System.out.println(parse);
                ArgsParser parser = gson.fromJson(parse,ArgsParser.class);
                result.put("type",parser.getType());
                if (!parser.getKey().equals("")) {
                    result.put("key",parser.getKey());
                }
                if (!parser.getValue().equals("")) {
                    result.put("value",parser.getValue());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + filename);
        }
        System.out.println(result);
        return result;
    }
}
