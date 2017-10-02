package com.stan.androidlabs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    private ListView chatList;
    private ImageButton sendButton;
    private EditText sendEditText;
    private ArrayList<String> chatArrayList = new ArrayList<>();
    private ChatAdapter mChatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);


        chatList = findViewById(R.id.chat_listView);
        mChatAdapter = new ChatAdapter(this);
        chatList.setAdapter(mChatAdapter);
        sendButton = findViewById(R.id.send_button);
        sendEditText = findViewById(R.id.chat_editText);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatArrayList.add(sendEditText.getText().toString());
                mChatAdapter.notifyDataSetChanged();
                sendEditText.setText(" ");
            }
        });

    }

    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(@NonNull Context context) {
            super(context, 0);
        }

        @Override
        public int getCount() {
            return chatArrayList.size();
        }

        @Nullable
        @Override
        public String getItem(int position) {
            return chatArrayList.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }
            TextView message = result.findViewById(R.id.messageText);
            message.setText(getItem(position));

            return result;
        }
    }
}
