package ru.leberamai.sstest.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import ru.leberamai.sstest.R;

public class MessagesListActivity extends AppCompatActivity {

    private MessagesList messagesList;
    MessageInput inputView;
    private FirebaseUser user;
    private String userName;
    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list);

        // вместо findViewById можно также использовать ButterKnife
        this.messagesList = findViewById(R.id.messagesList);

        user = FirebaseAuth.getInstance().getCurrentUser();

        // user.getUid() может вернуть NullPointerException
        final MessagesListAdapter<Message> adapter = new MessagesListAdapter<>(user.getUid(), null);
        messagesList.setAdapter(adapter);

        final MessagesRepository messagesRepository = new MessagesRepository();
        messagesRepository.loadMessages(new MessagesRepository.MessagesLoadListener() {
            @Override
            public void onMessagesReceived(Message message) {
                adapter.addToStart(message, true);
            }

            @Override
            public void onError(Throwable error) {
                // ошибку хорошо бы обработать
            }
        });

        inputView = findViewById(R.id.input);
        inputView.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                messagesRepository.addMessage(input.toString(), userName);
                return true;
            }
        });


        usersRef.child(user.getUid()).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
