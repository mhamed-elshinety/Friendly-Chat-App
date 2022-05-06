/**
 * Copyright Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shenaitty.friendlychat.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shenaitty.friendlychat.pojo.FriendlyMessage;
import com.shenaitty.friendlychat.adapters.MessageAdapter;
import com.shenaitty.friendlychat.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainActivityView{

    private static final String TAG = "MainActivity";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private static final String MESSAGES = "messages";

    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;

    private String mUsername;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUsername = ANONYMOUS;
        initializeFirebaseDatabase();
        initializePresenter();
        defineFields();
        initializeListView();
        initializeEditTextLength();
        hideProgressBar();
        setListeners();
        presenter.getMessages();
    }

    private void initializePresenter() {
        presenter = new MainActivityPresenter(this);
    }

    private void defineFields() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child(MESSAGES);
    }

    private void initializeEditTextLength() {
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
    }

    private void setListeners() {
        mSendButton.setOnClickListener(this);
        mPhotoPickerButton.setOnClickListener(this);
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


    private void hideProgressBar() {
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    private void initializeListView() {
        List<FriendlyMessage> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);
    }

    private void initializeFirebaseDatabase() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageListView = (ListView) findViewById(R.id.messageListView);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.sendButton){
            presenter.addMessageToDatabase(new FriendlyMessage(mMessageEditText.getText().toString(),mUsername,null));
        }else if(viewId == R.id.photoPickerButton){

        }
    }

    @Override
    public void postAddingMessageToDatabase(boolean isSuccessful, String errorMessage) {
        if(isSuccessful) {
            mMessageEditText.setText("");
            Toast.makeText(this, getString(R.string.message_send), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onGetMessage(FriendlyMessage message) {
        mMessageAdapter.add(message);
    }
}
