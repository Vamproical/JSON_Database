package server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import server.controller.Command;
import server.controller.Controller;
import server.controller.command.DeleteCommand;
import server.controller.command.ExitCommand;
import server.controller.command.GetCommand;
import server.controller.command.SetCommand;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

public class Session implements Callable<Boolean> {
    private final Socket socket;
    private final Database database;
    private boolean exitFlag = false;

    public Session(Socket socket, Database database) {
        this.socket = socket;
        this.database = database;
    }

    @Override
    public Boolean call() {
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String msgReceived = input.readUTF();
            System.out.println(msgReceived);
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(msgReceived, JsonObject.class);
            ResponseObject responseObj = new ResponseObject();
            interactWithDatabase(obj, responseObj);
            if (obj.get("type").getAsString().equals("exit")) {
                exitFlag = true;
            }
            String msgSent = gson.toJson(responseObj);
            output.writeUTF(msgSent);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exitFlag;
    }

    private void interactWithDatabase(JsonObject object, ResponseObject responseObject) {
        Controller controller = new Controller();
        JsonElement value = object.get("value");
        JsonArray keyArray = null;
        if (object.get("key").isJsonArray()) {
            keyArray = object.get("key").getAsJsonArray();
        } else if (object.get("key").isJsonPrimitive()) {
            String keyStr = object.get("key").getAsString();
            keyArray = new JsonArray();
            keyArray.add(keyStr);
        }
        try {
            switch (object.get("type").getAsString()) {
                case "get":
                    Command get = new GetCommand(database, keyArray, responseObject);
                    controller.setCommand(get);
                    controller.executeCommand();
                    break;

                case "set":
                    Command set = new SetCommand(database, keyArray, value, responseObject);
                    controller.setCommand(set);
                    controller.executeCommand();
                    break;

                case "delete":
                    Command delete = new DeleteCommand(database, keyArray, responseObject);
                    controller.setCommand(delete);
                    controller.executeCommand();
                    break;

                case "exit":
                    Command exit = new ExitCommand(responseObject);
                    controller.setCommand(exit);
                    controller.executeCommand();
                    break;

                default:
                    System.out.println("Unrecognized command!");
            }
        } catch (NumberFormatException e) {
            responseObject.setResponse("ERROR");
            responseObject.setReason("No such key");
        }
    }
}
