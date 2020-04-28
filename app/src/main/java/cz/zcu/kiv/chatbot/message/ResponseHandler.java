package cz.zcu.kiv.chatbot.message;

import com.ibm.watson.assistant.v2.model.MessageResponse;

import java.util.List;

/**
 * Generic response handler for handling incoming messages from assistant.
 *
 * @param <M> - Response type
 *
 * @author Martin Matas
 * @version 1.0
 * created on 2020-07-04
 */
public interface ResponseHandler<M> {

    /**
     * Handles incoming message from assistant.
     *
     * @param response - assistant's response
     * @param messages - list of tracked messages
     */
    void handle(MessageResponse response, List<M> messages);

    /**
     * Check if received response is valid and if contains something.
     *
     * @param response - received response
     * @return - status if message is valid
     */
    boolean exists(MessageResponse response);

}
