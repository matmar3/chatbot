package cz.zcu.kiv.chatbot.assistant;

public interface AssistantInterface<T> {

    T create(String assistantID, String apiKey, String apiUrl, String apiVersionDate);

}
