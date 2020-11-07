package server;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private final Map<String,String> json;

    public Database() {
        json = new HashMap<>();
    }

    public void set(String key, String data) {
        json.put(key,data);
    }

    public String get(String key) {
        if (json.get(key) == null) {
            return "";
        } else {
            return json.get(key);
        }
    }

    public boolean delete(String key) {
        if (json.containsKey(key)) {
            json.remove(key);
            return true;
        } else {
            return false;
        }
    }
}
