package cz.zcu.kiv.chatbot.message;

import com.ibm.watson.assistant.v2.model.MessageResponse;

import java.util.List;

public interface ResponseHandler<M> {

    void handle(MessageResponse response, List<M> messages);

    boolean exists(MessageResponse response);

}
