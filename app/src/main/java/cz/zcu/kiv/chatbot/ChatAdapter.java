package cz.zcu.kiv.chatbot;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int SELF = 100;
    private ArrayList<Message> messageArrayList;


    ChatAdapter(ArrayList<Message> messageArrayList) {
        this.messageArrayList = messageArrayList;

    }

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

    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);
        if (message.getId() != null && message.getId().equals("1")) {
            return SELF;
        }

        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        Message message = messageArrayList.get(position);

        if (message.getMessage().startsWith("<html>")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ((ViewHolder) holder).message.setText(Html.fromHtml(message.getMessage(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                ((ViewHolder) holder).message.setText(Html.fromHtml(message.getMessage()));
            }
        }
        else {
            ((ViewHolder) holder).message.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        ImageView image;

        ViewHolder(View view) {
            super(view);
            message = itemView.findViewById(R.id.message);
            image = itemView.findViewById(R.id.image);
        }
    }


}