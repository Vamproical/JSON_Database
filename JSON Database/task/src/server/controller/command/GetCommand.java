package server.controller.command;

import com.google.gson.JsonArray;
import server.Database;
import server.ResponseObject;
import server.controller.Command;

public class GetCommand implements Command {
    private final Database database;
    private final ResponseObject responseObj;
    private final JsonArray keyArray;

    public GetCommand(Database database, JsonArray keyArray, ResponseObject responseObj) {
        this.database = database;
        this.responseObj = responseObj;
        this.keyArray = keyArray;
    }

    @Override
    public void execute() {
        database.get(keyArray, responseObj);
    }
}
