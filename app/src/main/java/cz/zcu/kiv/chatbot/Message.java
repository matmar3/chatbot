package cz.zcu.kiv.chatbot;

import java.io.Serializable;

class Message implements Serializable {

    private String id, message;

    Message() {

    }

    String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }

}

