package com.shenaitty.friendlychat.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shenaitty.friendlychat.Data.Constants;
import com.shenaitty.friendlychat.pojo.FriendlyMessage;

public class MainActivityPresenter {

    private MainActivityView view;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public MainActivityPresenter(MainActivityView view){
        this.view = view;
    }

    public void addMessageToDatabase(FriendlyMessage message){
        addMessage(message);
    }

    private void addMessage(FriendlyMessage message) {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child(Constants.MESSAGES);
        databaseReference.push().setValue(message)
                .addOnSuccessListener(unused -> view.postAddingMessageToDatabase(true,null))
                .addOnFailureListener(e -> view.postAddingMessageToDatabase(false,e.getMessage()));
    }

    public void getMessages() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.MESSAGES);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                view.onGetMessage(snapshot.getValue(FriendlyMessage.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
