package server;

import com.beust.jcommander.Parameter;

public class ArgsParser {
    @Parameter(names = "-t", description = "Type of the request")
    private String type = "";

    @Parameter(names = "-k", description = "Key")
    private String key = "";

    @Parameter(names = "-v", description = "Value to save in the database")
    private String value = "";

    @Parameter(names = "-in", description = "File name")
    private String fileName = "";

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return type + " " + key + " " + value + " " + fileName;
    }
}