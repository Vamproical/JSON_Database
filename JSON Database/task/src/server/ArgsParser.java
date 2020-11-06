package server;

import com.beust.jcommander.Parameter;

public class ArgsParser {
    @Parameter(names = "-t", description = "Type of the request")
    private String requestType;

    @Parameter(names = "-k", description = "Key")
    private String key = "";

    @Parameter(names = "-v", description = "Value to save in the database")
    private String value = "";

    public String getRequestType() {
        return requestType;
    }

    public String getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }
}