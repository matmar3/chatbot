package cz.zcu.kiv.chatbot.assistant;

public interface AssistantManagerInterface<T> {

    T create(String apiKey, String apiUrl, String apiVersionDate);

    T get();

}
