package cz.zcu.kiv.chatbot.assistant;

import com.ibm.cloud.sdk.core.http.HttpConfigOptions;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;

public class WatsonAssistantManager implements AssistantManagerInterface<Assistant> {

    private Assistant assistant;

    public WatsonAssistantManager() {

    }

    @Override
    public Assistant create(String apiKey, String apiUrl, String apiVersionDate) {
        IamAuthenticator authenticator = new IamAuthenticator(apiKey);
        assistant = new Assistant(apiVersionDate, authenticator);
        assistant.setServiceUrl(apiUrl);

        HttpConfigOptions configOptions = new HttpConfigOptions.Builder()
                .disableSslVerification(true)
                .build();
        assistant.configureClient(configOptions);

        return assistant;
    }

    @Override
    public Assistant get() {
        return assistant;
    }

}
