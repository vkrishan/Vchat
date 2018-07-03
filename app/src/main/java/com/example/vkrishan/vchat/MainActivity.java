package com.example.vkrishan.vchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.*;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth FirebaseAuth;
    private FirebaseListAdapter<ChatMessageModel> adapter;
    int SIGN_IN_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Check if user is already signed-in
        // If yes, then continue or else redirect to Sign-In page

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),
                    SIGN_IN_REQUEST_CODE    // replaces SIGN_IN_REQUEST_CODE from 1
            );
        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();

            // Load chat room contents
            displayChatMessages();
        }



        // Add chat logic here...

        Button sendButton = (Button) findViewById(R.id.button3);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText message = (EditText) findViewById(R.id.editText);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database

                FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessageModel(message.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));

                message.setText("");



            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {  // replaces SIGN_IN_REQUEST_CODE from 1
            if(resultCode == RESULT_OK) {
                Toast.makeText(this,
                        "Successfully signed in. Welcome!",
                        Toast.LENGTH_LONG)
                        .show();
                  displayChatMessages();
            } else {
                Toast.makeText(this,
                        "We couldn't sign you in. Please try again later.",
                        Toast.LENGTH_LONG)
                        .show();

                // Close the app
                finish();
            }
        }

    }


    void displayChatMessages(){


        ListView chatList = (ListView) findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessageModel>(this, ChatMessageModel.class, R.layout.message, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessageModel model, int position) {
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);


                messageText.setText(model.getMessage());
                messageUser.setText(model.getUsername());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getTime()));


            }
        };

        chatList.setAdapter(adapter);

    }
}
