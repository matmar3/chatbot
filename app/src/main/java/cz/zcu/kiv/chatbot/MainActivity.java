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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ibm.watson.assistant.v2.model.DialogNodeOutputOptionsElement;
import com.ibm.watson.assistant.v2.model.RuntimeResponseGeneric;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.SessionResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cz.zcu.kiv.chatbot.assistant.WatsonAssistantManager;
import cz.zcu.kiv.chatbot.assistant.WatsonAssistantSessionManager;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private EditText inputMessage;

    private boolean initialRequest;
    private SessionResponse sessionResponse;
    private Context mContext;
    private ChatAdapter mAdapter;
    private ArrayList<Message> messageArrayList;

    private WatsonAssistantManager assistantManager;
    private WatsonAssistantSessionManager sessionManager;

    private void createServices() {
        assistantManager = new WatsonAssistantManager();
        sessionManager = new WatsonAssistantSessionManager();

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
            inputMessage.setId("1");
            messageArrayList.add(inputMessage);
        } else {
            Message inputMessage = new Message();
            inputMessage.setMessage(inputValue);
            inputMessage.setId("100");
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
                    Log.i(TAG, "run: " + response);
                    if (response != null &&
                            response.getOutput() != null &&
                            !response.getOutput().getGeneric().isEmpty()) {

                        List<RuntimeResponseGeneric> responses = response.getOutput().getGeneric();

                        for (RuntimeResponseGeneric r : responses) {
                            Message outMessage;
                            switch (r.responseType()) {
                                case "text":
                                    outMessage = new Message();
                                    outMessage.setMessage(r.text());
                                    outMessage.setId("2");

                                    messageArrayList.add(outMessage);
                                    break;

                                case "option":
                                    outMessage =new Message();
                                    String title = r.title();
                                    StringBuilder optionsOutput = new StringBuilder();
                                    for (int i = 0; i < r.options().size(); i++) {
                                        DialogNodeOutputOptionsElement option = r.options().get(i);
                                        optionsOutput.append(option.getLabel()).append("\n");

                                    }
                                    outMessage.setMessage(title + "\n" + optionsOutput.toString());
                                    outMessage.setId("2");

                                    messageArrayList.add(outMessage);
                                    break;
                                default:
                                    Log.e("Error", "Unhandled message type");
                            }
                        }

                        runOnUiThread(new Runnable() {
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                                if (mAdapter.getItemCount() > 1) {
                                    Objects.requireNonNull(recyclerView.getLayoutManager()).smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);

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



