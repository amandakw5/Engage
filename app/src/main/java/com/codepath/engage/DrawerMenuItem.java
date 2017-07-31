package com.codepath.engage;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.codepath.engage.ui.activities.SplashActivity;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

/**
 * Created by emilyz on 7/27/17.
 */

@Layout(R.layout.drawer_item)
public class DrawerMenuItem {
    public static final int DRAWER_MENU_ITEM_PROFILE = 1;
    public static final int DRAWER_MENU_ITEM_FEED = 2;
    public static final int DRAWER_MENU_ITEM_EVENTS = 3;
    public static final int DRAWER_MENU_ITEM_CREATE = 4;
    public static final int DRAWER_MENU_ITEM_LOGOUT = 5;
    public static final int DRAWER_MENU_ITEM_MESSAGE = 6;


    private int mMenuPosition;
    private Context mContext;
    private DrawerCallBack mCallBack;

    @View(R.id.itemNameTxt)
    private TextView itemNameTxt;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public DrawerMenuItem(Context context, int menuPosition) {
        mContext = context;
        mMenuPosition = menuPosition;
    }

    @Resolve
    private void onResolved() {
        switch (mMenuPosition) {
            case DRAWER_MENU_ITEM_PROFILE:
                itemNameTxt.setText("My Profile");
                break;
            case DRAWER_MENU_ITEM_FEED:
                itemNameTxt.setText("My Feed");
                break;
            case DRAWER_MENU_ITEM_EVENTS:
                itemNameTxt.setText("My Events");
                break;
            case DRAWER_MENU_ITEM_CREATE:
                itemNameTxt.setText("Create an Event");
                break;
            case DRAWER_MENU_ITEM_LOGOUT:
                itemNameTxt.setText("Log Out");
                break;
            case DRAWER_MENU_ITEM_MESSAGE:
                itemNameTxt.setText("Message");
        }
    }
    @Click(R.id.mainView)
    private void onMenuItemClick(){
        switch (mMenuPosition){
            case DRAWER_MENU_ITEM_PROFILE:
                Intent i = new Intent (mContext, ProfileActivity.class);
                i.putExtra("whichProfile", "You are ");
                mContext.startActivity(i);
                break;
            case DRAWER_MENU_ITEM_FEED:
                Intent feedInt = new Intent(mContext, UserFeed.class);
                mContext.startActivity(feedInt);
                break;
            case DRAWER_MENU_ITEM_EVENTS:
                Intent intent1 = new Intent(mContext, SplashActivity.class);
                mContext.startActivity(intent1);
                break;
            case DRAWER_MENU_ITEM_CREATE:
                Intent in = new Intent(mContext, CreateEventActivity.class);
                mContext.startActivity(in);
                break;
            case DRAWER_MENU_ITEM_LOGOUT:
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
                break;
            case DRAWER_MENU_ITEM_MESSAGE:

                break;
        }
    }


    public void setDrawerCallBack(DrawerCallBack callBack) {
        mCallBack = callBack;
    }

    public interface DrawerCallBack{
        void onProfileMenuSelected();
        void onFeedMenuSelected();
        void onEventMenuSelected();
        void onCreatedMenuSelected();
        void onLogoutMenuSelected();
    }
}

