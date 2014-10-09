package com.alzatezabala.eslem.chatdemogcm.android;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alzatezabala.eslem.chatdemogcm.R;
import com.alzatezabala.eslem.chatdemogcm.android.adapter.ConversationsAdapter;
import com.alzatezabala.eslem.chatdemogcm.android.adapter.TabsPageAdapter;
import com.alzatezabala.eslem.chatdemogcm.dommain.Conversation;
import com.alzatezabala.eslem.chatdemogcm.dommain.Message;
import com.alzatezabala.eslem.chatdemogcm.dommain.ObserverBroadCast;
import com.alzatezabala.libreria.LibSharedPreferences;
import com.alzatezabala.libreria.NotifyPost;
import com.alzatezabala.push.RegisterPush;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends FragmentActivity implements
        ActionBar.TabListener, NotifyPost {

    private String TAG = "test database";
    private ActionBar actionBar;
    private ViewPager viewPager;
    private TabsPageAdapter tabsPageAdapter;
    private String[] tabs = {"Users", "Chat"};
    public static final String SHAREDNAME = "chatDemo";
    private ProgressBar progressBar;
    public static LibSharedPreferences libSharedPreferences;


    //---Reciever

    private BroadcastReceiver mReceiver;
    public static ObserverBroadCast observerBroadCast;

    @Override
    protected void onResume() {
        super.onResume();
        setUpReciever();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //unregister our receiver
        unregisterReceiver(mReceiver);
    }

    public void setUpReciever() {

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                observerBroadCast.onRecieve(intent);
            }
        };
        IntentFilter filter = new IntentFilter("com.alzatezabala.eslem.chatedemogcm.Broadcast");
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        libSharedPreferences = new LibSharedPreferences(this, SHAREDNAME);

        if (libSharedPreferences.getValue("idPush").length() == 0) {
            setContentView(R.layout.start_activity_layout);
            setUpRegister();
        } else {
            setContentView(R.layout.activity_my);
            setConfigTabs();
        }
//        /
    }


    public void setUpRegister() {
        final EditText editText = (EditText) findViewById(R.id.username);
        Button button = (Button) findViewById(R.id.send_button);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().length() > 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    sendPush(editText.getText().toString());
                }
            }
        });
    }

    public void sendPush(String name) {
        libSharedPreferences.setValue("username", name);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("name", name);
        RegisterPush regPush = new RegisterPush(this, SHAREDNAME,
                "http://push.alzatezabala.com/chat/android/register.php",
                this, param,
                "8096093960");
        regPush.setNotifyPost(this);
        regPush.register();

    }


    public void setConfigTabs() {
        actionBar = getActionBar();
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabsPageAdapter = new TabsPageAdapter(getSupportFragmentManager());

        viewPager.setAdapter(tabsPageAdapter);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void postFinished(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "push Finished: " + message, Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_my);
        setConfigTabs();
    }
}
