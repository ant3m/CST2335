package com.stan.androidlabs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    private static SQLiteDatabase chatDB;
    private static String ACTIVITY_NAME = "ChatWindow.java";
    private ListView chatList;
    private ImageButton sendButton;
    private EditText sendEditText;
    private ArrayList<String> chatArrayList = new ArrayList<>();
    private ChatAdapter mChatAdapter;

    @SuppressLint("Recycle")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        ChatDatabaseHelper mChatDatabaseHelper = new ChatDatabaseHelper(this);

        chatDB = mChatDatabaseHelper.getWritableDatabase();

        final ContentValues contentValues = new ContentValues();

        chatList = findViewById(R.id.chat_listView);
        mChatAdapter = new ChatAdapter(this);
        chatList.setAdapter(mChatAdapter);
        sendButton = findViewById(R.id.send_button);
        sendEditText = findViewById(R.id.chat_editText);

        Cursor mCursor = chatDB.rawQuery("Select * from " + ChatDatabaseHelper.TABLE_NAME, null);

        mCursor.moveToFirst();

        while (!mCursor.isAfterLast()) {
            String newMessage = mCursor.getString(mCursor.getColumnIndex(ChatDatabaseHelper.COLUMN_MESSAGE));
            chatArrayList.add(newMessage);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + mCursor.getString(mCursor.getColumnIndex(ChatDatabaseHelper.COLUMN_MESSAGE)));

            mCursor.moveToNext();
        }

        for (int i = 0; i < mCursor.getColumnCount(); i++) {
            Log.i("Cursor column name: ", mCursor.getColumnName(i));
        }
        Log.i(ACTIVITY_NAME, "Cursors column count: " + mCursor.getColumnCount());

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String chatString = sendEditText.getText().toString();
                chatArrayList.add(chatString);

                contentValues.put(ChatDatabaseHelper.COLUMN_MESSAGE, chatString);
                chatDB.insert(ChatDatabaseHelper.TABLE_NAME, null, contentValues);

                sendEditText.setText(" ");
                mChatAdapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatDB.close();
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
