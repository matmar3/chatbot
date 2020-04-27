package cz.zcu.kiv.chatbot.message;

import android.util.Log;

import com.ibm.watson.assistant.v2.model.DialogNodeOutputOptionsElement;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.RuntimeResponseGeneric;

import java.util.List;

import cz.zcu.kiv.chatbot.user.MessageOwner;

public class WatsonAssistantResponseHandler implements ResponseHandler<Message> {

    private static final String TAG = WatsonAssistantResponseHandler.class.getSimpleName();

    @Override
    public void handle(MessageResponse response, List<Message> messages) {
        Log.i(TAG, "Received response: " + response);

        if (!exists(response)) {
            Log.e(TAG, "Received response is not exists.");
        }

        List<RuntimeResponseGeneric> responses = response.getOutput().getGeneric();

        for (RuntimeResponseGeneric r : responses) {
            Message outMessage;
            switch (ResponseType.resolve(r.responseType())) {
                case TEXT:
                    outMessage = new Message();
                    outMessage.setMessage(r.text());
                    outMessage.setId(MessageOwner.ASSISTANT.getUserID());

                    messages.add(outMessage);
                    break;

                case OPTION:
                    outMessage = new Message();
                    String title = r.title();
                    StringBuilder optionsOutput = new StringBuilder();
                    for (int i = 0; i < r.options().size(); i++) {
                        DialogNodeOutputOptionsElement option = r.options().get(i);
                        optionsOutput.append(option.getLabel()).append("\n");
                    }
                    outMessage.setMessage(title + "\n" + optionsOutput.toString());
                    outMessage.setId(MessageOwner.ASSISTANT.getUserID());

                    messages.add(outMessage);
                    break;

                case NONE:
                    Log.e("Error", "Unhandled message type");
            }
        }

    }

    @Override
    public boolean exists(MessageResponse response) {
        return response != null
                && response.getOutput() != null
                && !response.getOutput().getGeneric().isEmpty();
    }

}
