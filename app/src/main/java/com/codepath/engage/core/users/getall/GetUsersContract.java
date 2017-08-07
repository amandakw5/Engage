package com.codepath.engage.core.users.getall;

import com.codepath.engage.models.UserChat;

import java.util.List;



public interface GetUsersContract {
    interface View {
        void onGetAllUsersSuccess(List<UserChat> users);

        void onGetAllUsersFailure(String message);

        void onGetChatUsersSuccess(List<UserChat> users);

        void onGetChatUsersFailure(String message);
    }

    interface Presenter {
        void getAllUsers();

        void getChatUsers();
    }

    interface Interactor {
        void getAllUsersFromFirebase();

        void getChatUsersFromFirebase();
    }

    interface OnGetAllUsersListener {
        void onGetAllUsersSuccess(List<UserChat> users);

        void onGetAllUsersFailure(String message);
    }

    interface OnGetChatUsersListener {
        void onGetChatUsersSuccess(List<UserChat> users);

        void onGetChatUsersFailure(String message);
    }
}
