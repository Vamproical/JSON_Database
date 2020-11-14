package server;

import com.google.gson.JsonElement;

public class ResponseObject {
    private String response;
    private String reason;
    private JsonElement value;

    public ResponseObject() {
        this.response = null;
        this.reason = null;
        this.value = null;
    }

    public ResponseObject(String response) {
        this.response = response;
        this.reason = null;
        this.value = null;
    }

    public ResponseObject(String response, String reason) {
        this.response = response;
        this.reason = reason;
        this.value = null;
    }

    public ResponseObject(String response, String reason, JsonElement value) {
        this.response = response;
        this.reason = reason;
        this.value = value;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setValue(JsonElement value) {
        this.value = value;
    }
}
