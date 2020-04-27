package cz.zcu.kiv.chatbot;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import cz.zcu.kiv.chatbot.message.Message;
import cz.zcu.kiv.chatbot.message.WatsonAssistantResponseHandler;
import cz.zcu.kiv.chatbot.user.MessageOwner;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText inputMessage;

    private boolean initialRequest;
    private SessionResponse sessionResponse;
    private Context mContext;
    private ChatAdapter mAdapter;
    private ArrayList<Message> messageArrayList;

    private WatsonAssistantManager assistantManager;
    private WatsonAssistantSessionManager sessionManager;
    private WatsonAssistantResponseHandler responseHandler;

    private void createServices() {
        assistantManager = new WatsonAssistantManager();
        sessionManager = new WatsonAssistantSessionManager();
        responseHandler = new WatsonAssistantResponseHandler();

        assistantManager.create(
                mContext.getString(R.string.assistant_apikey),
                mContext.getString(R.string.assistant_url),
                mContext.getString(R.string.api_version_date)
        );
    }

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
                if (checkInternetConnection()) {
                    sendMessage();
                }
            }

        });

        // Start services and communicator
        createServices();
        sendMessage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu - adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            finish();
            startActivity(getIntent());
        }

        return(super.onOptionsItemSelected(item));
    }

    // Sending a message to Watson Assistant Service
    private void sendMessage() {

        final String inputValue = this.inputMessage.getText().toString().trim();
        if (!this.initialRequest) {
            Message inputMessage = new Message();
            inputMessage.setMessage(inputValue);
            inputMessage.setId(MessageOwner.CLIENT.getUserID());
            messageArrayList.add(inputMessage);
        } else {
            Message inputMessage = new Message();
            inputMessage.setMessage(inputValue);
            inputMessage.setId(MessageOwner.NONE.getUserID());
            this.initialRequest = false;
            Toast.makeText(getApplicationContext(), "Tap on the message for Voice", Toast.LENGTH_LONG).show();
        }

        this.inputMessage.setText("");
        mAdapter.notifyDataSetChanged();

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

    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // Check for network connections
        if (isConnected) {
            return true;
        } else {
            Toast.makeText(this, "No Internet Connection available ", Toast.LENGTH_LONG).show();
            return false;
        }
    }

}



