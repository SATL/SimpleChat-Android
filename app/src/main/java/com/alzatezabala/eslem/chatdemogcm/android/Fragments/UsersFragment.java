package com.alzatezabala.eslem.chatdemogcm.android.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.alzatezabala.eslem.chatdemogcm.R;
import com.alzatezabala.eslem.chatdemogcm.android.MainActivity;
import com.alzatezabala.eslem.chatdemogcm.android.adapter.UsersAdapter;
import com.alzatezabala.eslem.chatdemogcm.dommain.User;
import com.alzatezabala.libreria.LibSharedPreferences;
import com.alzatezabala.libreria.NotifyPost;
import com.alzatezabala.libreria.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersFragment extends Fragment implements NotifyPost {
    User[] users;
    private ProgressBar progressBar;
    private LibSharedPreferences libSharedPreferences = MainActivity.libSharedPreferences;
    private ListView listView;

    public UsersFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_users2, container, false);

        listView = (ListView) rootView.findViewById(R.id.listView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("wth", "getAll");

        params.put("id", libSharedPreferences.getValue("idPush"));
        Post post = new Post("http://push.alzatezabala.com/chat/android/users.php", params);
        post.setNotifyPost(this);
        post.post();

        return rootView;
    }


    @Override
    public void postFinished(String message) {
        Log.d("JSON", message);
        progressBar.setVisibility(View.GONE);
        if (message.length() > 0 && !message.equals("0")) {
            setList(message);
        }
    }

    public void setList(String json) {
        if (json.length() > 1 && !json.equals("0")) {
            JSONArray jsonArray = null;
            List<User> userList = new ArrayList<User>();
            try {
                jsonArray = new JSONArray(json);
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject jsonUse = jsonArray.getJSONObject(x);
                    User user = new User(Integer.parseInt(jsonUse.getString("id")), jsonUse.getString("name"));
                    userList.add(user);
                }

                UsersAdapter usersAdapter = new UsersAdapter(getActivity(), userList);
                listView.setAdapter(usersAdapter);

            } catch (JSONException e) {
                throw new RuntimeException(e.toString());
            }
        }
    }
}
