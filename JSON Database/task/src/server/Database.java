package server;

public class Database {
    private final String[] json;

    public Database() {
        json = new String[1000];
        for (int i = 0; i < 1000; i++) {
            json[i] = "";
        }
    }

    private String set(int index, String data) {
        if (index > 1000 || index < 1) {
            return "ERROR";
        } else {
            json[index - 1] = data;
            return "OK";
        }
    }

    private String get(int index) {
        if (index > 1000 || index < 1 || json[index - 1].equals("")) {
            return "ERROR";
        } else {
            return json[index - 1];
        }
    }

    private String delete(int index) {
        if (index > 1000 || index < 1) {
            return "ERROR";
        } else {
            json[index - 1] = "";
            return "OK";
        }
    }

    public String runSystem(String typeRequest, int index, String data) {
        switch (typeRequest) {
            case "get":
                return get(index);
            case "set":
                return set(index,data);
            case "delete":
                return delete(index);
        }
        return null;
    }

}
