package cz.zcu.kiv.chatbot.assistant;

public interface SessionManagerInterface<S, A, M> {

    S createSession(A assistant, String assistantID);

    void deleteSession(A assistant, String sessionID, String assistantID);

    M sendMessage(A watsonAssistant, String sessionID, String assistantID, String message);

}
