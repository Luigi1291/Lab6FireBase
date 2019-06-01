package com.example.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.firebase.Fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .add(android.R.id.content, new MainFragment())
                .commit();
    }
}