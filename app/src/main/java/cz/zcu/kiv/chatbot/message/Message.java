package cz.zcu.kiv.chatbot.message;

import java.io.Serializable;

public class Message extends Response implements Serializable {

    private String id, message;

    public Message() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

