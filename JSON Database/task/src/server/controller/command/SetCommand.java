package server.controller.command;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import server.Database;
import server.ResponseObject;
import server.controller.Command;

public class SetCommand implements Command {
    private final Database database;
    private final ResponseObject responseObj;
    private final JsonArray keyArray;
    private final JsonElement value;

    public SetCommand(Database database, JsonArray KeyArray, JsonElement value, ResponseObject responseObj) {
        this.database = database;
        this.responseObj = responseObj;
        this.keyArray = KeyArray;
        this.value = value;
    }

    @Override
    public void execute() {
        database.set(keyArray, value, responseObj);
    }
}
