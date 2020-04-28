package cz.zcu.kiv.chatbot.assistant;

import android.util.Log;

import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.DeleteSessionOptions;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.SessionResponse;

/**
 * Implementation of session manager for Watson Assistant.
 *
 * @author Martin Matas
 * @version 1.0
 * created on 2020-27-04
 */
public class WatsonAssistantSessionManager implements SessionManagerInterface<SessionResponse, Assistant, MessageResponse> {

    /**
     * The only type of message that can be send.
     */
    private static final String MESSAGE_TYPE = "text";

    /**
     * Class tag for logger.
     */
    private static final String TAG = WatsonAssistantSessionManager.class.getSimpleName();

    public WatsonAssistantSessionManager() {

    }

    @Override
    public SessionResponse createSession(Assistant assistant, String assistantID) {
        Log.d(TAG, "Creating session " + this.toString() + ".");

        CreateSessionOptions options = new CreateSessionOptions
                .Builder(assistantID).build();

        return assistant.createSession(options).execute().getResult();
    }

    @Override
    public void deleteSession(Assistant assistant, String sessionID, String assistantID) {
        Log.d(TAG, "Deleting session " + this.toString() + ".");

        DeleteSessionOptions options = new DeleteSessionOptions
                .Builder(assistantID, sessionID).build();

        assistant.deleteSession(options).execute();
    }

    @Override
    public MessageResponse sendMessage(Assistant watsonAssistant, String sessionID, String assistantID, String message) {
        Log.d(TAG, "Sending message via assistant with ID = " + assistantID + " and session ID =  " + sessionID + ".");

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
