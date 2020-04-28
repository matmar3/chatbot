package cz.zcu.kiv.chatbot.assistant;

/**
 * Defines methods to manage assistant manager.
 *
 * @param <T> - Assistant type
 *
 * @author Martin Matas
 * @version 1.0
 * created on 2020-27-04
 */
public interface AssistantManagerInterface<T> {

    /**
     * Creates connection to assistant based on given params.
     * 
     * @param apiKey - key of assistant service API
     * @param apiUrl - URL of assistant service API
     * @param apiVersionDate - version of assistant API
     * @return instance of assistant
     */
    T create(String apiKey, String apiUrl, String apiVersionDate);

    /**
     * Returns created assistant.
     *
     * @return instance of assistant.
     */
    T get();

}
