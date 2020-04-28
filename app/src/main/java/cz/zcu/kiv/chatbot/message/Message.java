package cz.zcu.kiv.chatbot.message;

import java.io.Serializable;

/**
 * Watson assistant response wrapper. Message stores owner's response and the owner of
 * the message for later visualization.
 *
 * @author Martin Matas
 * @version 1.0
 * created on 2020-22-04
 */
public class Message extends Response implements Serializable {

    /**
     * Defines owner of the massage as userID according to owner types
     * defined in {@link cz.zcu.kiv.chatbot.user.MessageOwner}.
     */
    private String id;

    /**
     * Owner's response.
     */
    private String message;

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

