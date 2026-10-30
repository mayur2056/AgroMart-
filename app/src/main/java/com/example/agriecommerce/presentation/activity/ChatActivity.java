package com.example.agriecommerce.presentation.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.agriecommerce.R;
import com.example.agriecommerce.model.Message;
import com.example.agriecommerce.presentation.adapter.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView messageRecyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private EditText messageInput;
    private Button sendButton;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String senderId, receiverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageRecyclerView = findViewById(R.id.messageRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        senderId = auth.getCurrentUser().getUid();
        receiverId = getIntent().getStringExtra("receiverId");

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageRecyclerView.setAdapter(messageAdapter);

        loadMessages();

        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        if (!text.isEmpty()) {
            DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats");

            // Unique chat ID between sender and receiver
            String chatId = senderId + "_" + receiverId;

            // Create a unique message ID
            String messageId = chatRef.child(chatId).child("messages").push().getKey();

            Message message = new Message(senderId, receiverId, text, System.currentTimeMillis());

            // Save message under "chats/{chatId}/messages/{messageId}"
            chatRef.child(chatId).child("messages").child(messageId).setValue(message)
                    .addOnSuccessListener(aVoid -> messageInput.setText(""))  // Clear input on success
                    .addOnFailureListener(e -> Toast.makeText(ChatActivity.this, "Message Failed", Toast.LENGTH_SHORT).show());
        }
    }

    private void loadMessages() {
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats");

        String chatId = senderId + "_" + receiverId;

        chatRef.child(chatId).child("messages").orderByChild("timestamp")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageList.clear(); // Clear old messages
                        for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                            Message message = messageSnapshot.getValue(Message.class);
                            messageList.add(message);
                        }
                        messageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ChatActivity.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
