package cz.zcu.kiv.chatbot;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cz.zcu.kiv.chatbot.imageloader.PicassoImageGetter;
import cz.zcu.kiv.chatbot.message.Message;
import cz.zcu.kiv.chatbot.message.MessageOwner;

/**
 * Adapter for displaying messages in chat.
 *
 * @author Martin Matas
 * @version 1.0
 * created on 2020-22-04
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * Defines which userID belongs to client.
     */
    private int SELF = 100;

    /**
     * List of all messages from active session.
     */
    private ArrayList<Message> messageArrayList;

    ChatAdapter(ArrayList<Message> messageArrayList) {
        this.messageArrayList = messageArrayList;

    }

    /**
     * Render each message in view based on massage owner. Client's messages are aligned right and
     * assistant's messages left.
     *
     * @param parent - parent view group
     * @param viewType - type of view that must be used for rendering message
     * @return - message's view holder
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        // view type is to identify where to render the chat message
        // left or right
        if (viewType == SELF) {
            // self message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_self, parent, false);
        } else {
            // WatBot message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_cloud, parent, false);
        }


        return new ViewHolder(itemView);
    }

    /**
     * Returns items viewType based on owner.
     * @param position - position inside the list of messages
     * @return - position
     */
    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);
        if (message.getId() != null && message.getId().equals(MessageOwner.CLIENT.getUserID())) {
            return SELF;
        }

        return position;
    }

    /**
     * Defines content of each view holder and handles way of rendering the view. If message
     * contains HTML code, it makes viewable HTML from the message.
     * @param holder - specific message's view holder
     * @param position - message's position inside list
     */
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        Message message = messageArrayList.get(position);
        TextView textView = ((ViewHolder) holder).message;

        if (message.getMessage().startsWith("<html>")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView.setText(Html.fromHtml(message.getMessage(), Html.FROM_HTML_MODE_COMPACT, new PicassoImageGetter(textView), null));
            } else {
                textView.setText(Html.fromHtml(message.getMessage(), new PicassoImageGetter(textView), null));
            }
        }
        else {
            ((ViewHolder) holder).message.setText(message.getMessage());
        }
    }

    /**
     * Returns count of all messages in this session.
     * @return - count of messages in active session
     */
    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    /**
     * Defines view holder for message.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView message;

        ViewHolder(View view) {
            super(view);
            message = itemView.findViewById(R.id.message);
        }
    }

}