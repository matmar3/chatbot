package cz.zcu.kiv.chatbot.assistant;

import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.DeleteSessionOptions;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.SessionResponse;

public class WatsonAssistantSessionManager implements SessionManagerInterface<SessionResponse, Assistant, MessageResponse> {

    private static final String MESSAGE_TYPE = "text";

    public WatsonAssistantSessionManager() {

    }

    @Override
    public SessionResponse createSession(Assistant assistant, String assistantID) {
        CreateSessionOptions options = new CreateSessionOptions
                .Builder(assistantID).build();

        return assistant.createSession(options).execute().getResult();
    }

    @Override
    public void deleteSession(Assistant assistant, String sessionID, String assistantID) {
        DeleteSessionOptions options = new DeleteSessionOptions
                .Builder(assistantID, sessionID).build();

        assistant.deleteSession(options).execute();
    }

    @Override
    public MessageResponse sendMessage(Assistant watsonAssistant, String sessionID, String assistantID, String message) {
        MessageInput input = new MessageInput.Builder()
                .messageType(MESSAGE_TYPE)
                .text(message)
                .build();

        MessageOptions options = new MessageOptions
                .Builder(assistantID, sessionID)
                .input(input)
                .build();

        return watsonAssistant.message(options).execute().getResult();
    }

}
