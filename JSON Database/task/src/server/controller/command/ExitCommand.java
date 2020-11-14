package server.controller.command;

import server.ResponseObject;
import server.controller.Command;

public class ExitCommand implements Command {
    private final ResponseObject responseObj;
    public ExitCommand(ResponseObject responseObj) {
        this.responseObj = responseObj;
    }

    @Override
    public void execute() {
        responseObj.setResponse("OK");
    }
}
