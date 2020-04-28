package cz.zcu.kiv.chatbot.assistant;

/**
 * Defines methods to manage assistant's active session.
 *
 * @param <S> - Session type
 * @param <A> - Assistant type
 * @param <M> - Message type
 *
 * @author Martin Matas
 * @version 1.0
 * created on 2020-27-04
 */
public interface SessionManagerInterface<S, A, M> {

    /**
     * Creates session for given assistant.
     *
     * @param assistant - instance of assistant
     * @param assistantID - assistant ID
     * @return - assistant's session
     */
    S createSession(A assistant, String assistantID);

    /**
     * Destroy active session based on sessionID.
     *
     * @param assistant - instance of assistant
     * @param assistantID - assistant ID
     * @param sessionID - ID of session to delete
     */
    void deleteSession(A assistant, String sessionID, String assistantID);

    /**
     * Sends synchronous message to assistant using active
     * session and receives response from assistant.
     *
     * @param assistant - instance of assistant
     * @param assistantID - assistant ID
     * @param sessionID - ID of active session
     * @param message - message to send
     * @return response
     */
    M sendMessage(A assistant, String sessionID, String assistantID, String message);

}
