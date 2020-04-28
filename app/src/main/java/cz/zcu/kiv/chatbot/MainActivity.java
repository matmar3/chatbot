package cz.zcu.kiv.chatbot;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.SessionResponse;

import java.util.ArrayList;
import java.util.Objects;

import cz.zcu.kiv.chatbot.assistant.WatsonAssistantManager;
import cz.zcu.kiv.chatbot.assistant.WatsonAssistantSessionManager;
import cz.zcu.kiv.chatbot.connection.InternetConnection;
import cz.zcu.kiv.chatbot.message.Message;
import cz.zcu.kiv.chatbot.message.WatsonAssistantResponseHandler;
import cz.zcu.kiv.chatbot.message.MessageOwner;

/**
 * Defines main application activity.
 *
 * @author Martin Matas
 * @version 1.0
 * created on 2020-22-04
 */
public class MainActivity extends AppCompatActivity {

    /*
     Class variables
     */
    private RecyclerView recyclerView;
    private EditText inputMessage;
    private boolean initialRequest;
    private SessionResponse sessionResponse;
    private Context mContext;
    private ChatAdapter mAdapter;
    private ArrayList<Message> messageArrayList;

    /*
     References to managers and handlers.
     */
    private WatsonAssistantManager assistantManager;
    private WatsonAssistantSessionManager sessionManager;
    private WatsonAssistantResponseHandler responseHandler;

    /**
     * Initializes all necessary services.
     */
    private void createServices() {
        assistantManager = new WatsonAssistantManager();
        sessionManager = new WatsonAssistantSessionManager();
        responseHandler = new WatsonAssistantResponseHandler();

        // initialize connection with Watson Assistant
        assistantManager.create(
                mContext.getString(R.string.assistant_apikey),
                mContext.getString(R.string.assistant_url),
                mContext.getString(R.string.api_version_date)
        );
    }

    /**
     * Creates activity and start communication with Watson Assistant.
     * @param savedInstanceState - saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // define view
        setContentView(R.layout.activity_main);

        // prepare context
        mContext = getApplicationContext();

        // Message font
        inputMessage = findViewById(R.id.message);
        String customFont = "Montserrat-Regular.ttf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), customFont);
        inputMessage.setTypeface(typeface);
        inputMessage.setText("");

        // Adapter configuration
        messageArrayList = new ArrayList<>();
        mAdapter = new ChatAdapter(messageArrayList);

        // Recycler view configuration
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        this.initialRequest = true;

        // Send button configuration
        ImageButton btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetConnection.check(getApplicationContext())) {
                    startCommunication();
                }
                else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Internet not available, check your internet connectivity and try again.",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });

        // Start services and communicator
        createServices();
        startCommunication();
    }

    /**
     * See {@link AppCompatActivity} for more details.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu - adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Defines refresh button in menu.
     * See {@link AppCompatActivity} for more details.
     *
     * @param item - menu item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            finish();
            startActivity(getIntent());
        }

        return(super.onOptionsItemSelected(item));
    }

    /**
     * Starts communication with Watson Assistant.
     */
    private void startCommunication() {

        final String inputValue = this.inputMessage.getText().toString().trim();
        logMessage(inputValue);

        this.inputMessage.setText("");
        mAdapter.notifyDataSetChanged();

        // thread for handling responses
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    Assistant watsonAssistant = assistantManager.get();
                    final String assistantID = mContext.getString(R.string.assistant_id);

                    if (sessionResponse == null) {
                        sessionResponse = sessionManager.createSession(watsonAssistant, assistantID);
                    }

                    MessageResponse response = sessionManager.sendMessage(watsonAssistant, sessionResponse.getSessionId(), assistantID, inputValue);

                    if (responseHandler.exists(response)) {
                        responseHandler.handle(response, messageArrayList);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                                if (mAdapter.getItemCount() > 1) {
                                    Objects.requireNonNull(recyclerView.getLayoutManager())
                                            .smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                                }

                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    /**
     * Log messages into list of messages.
     * @param inputValue - message
     */
    private void logMessage(String inputValue) {
        Message inputMessage = new Message();
        inputMessage.setMessage(inputValue);

        if (!this.initialRequest) {
            inputMessage.setId(MessageOwner.CLIENT.getUserID());
            messageArrayList.add(inputMessage);
        } else {
            inputMessage.setId(MessageOwner.INIT_MESSAGE.getUserID());
            this.initialRequest = false;
        }
    }

}



