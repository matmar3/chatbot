package cz.zcu.kiv.chatbot.assistant;

import android.util.Log;

import com.ibm.cloud.sdk.core.http.HttpConfigOptions;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;

/**
 * Implementation for managing WatsonAssistant.
 *
 * @author Martin Matas
 * @version 1.0
 * created on 2020-27-04
 */
public class WatsonAssistantManager implements AssistantManagerInterface<Assistant> {

    private Assistant assistant;

    public WatsonAssistantManager() {

    }

    @Override
    public Assistant create(String apiKey, String apiUrl, String apiVersionDate) {
        Log.d(WatsonAssistantManager.class.getSimpleName(), "Creating assistant " + this.toString() + ".");

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
