package com.shenaitty.friendlychat.ui;

import com.shenaitty.friendlychat.pojo.FriendlyMessage;

public interface MainActivityView {
    void postAddingMessageToDatabase(boolean isSuccessful, String errorMessage);
    void onGetMessage(FriendlyMessage message);
}
