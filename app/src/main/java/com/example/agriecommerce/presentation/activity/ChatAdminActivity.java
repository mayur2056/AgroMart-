package com.example.agriecommerce.presentation.activity;


import com.example.agriecommerce.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agriecommerce.model.ChatMessage;
import com.example.agriecommerce.presentation.adapter.ChatAdapter;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class ChatAdminActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private Button sendMessageButton;
    private DatabaseReference chatRef;
    private String userId;
    private ChatAdapter adapter;
    private List<ChatMessage> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.agriecommerce.R.layout.activity_chat_admin);

        chatRecyclerView = findViewById(com.example.agriecommerce.R.id.chatRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendMessageButton = findViewById(R.id.sendMessageButton);

        userId = getIntent().getStringExtra("userId");
        chatRef = FirebaseDatabase.getInstance().getReference("Chats").child(userId).child("messages");

        messageList = new ArrayList<>();
        adapter = new ChatAdapter(messageList, "Admin");
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(adapter);

        sendMessageButton.setOnClickListener(view -> sendMessage());

        loadMessages();
    }

    private void sendMessage() {
        String message = messageInput.getText().toString().trim();
        if (!message.isEmpty()) {
            String messageId = chatRef.push().getKey();
            ChatMessage chatMessage = new ChatMessage("Admin", message, System.currentTimeMillis());
            chatRef.child(messageId).setValue(chatMessage);
            messageInput.setText("");
        }
    }

    private void loadMessages() {
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    ChatMessage chatMessage = data.getValue(ChatMessage.class);
                    messageList.add(chatMessage);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
