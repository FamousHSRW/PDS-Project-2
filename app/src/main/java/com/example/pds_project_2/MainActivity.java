package com.example.pds_project_2;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.pds_project_2.ui.main.SectionsPagerAdapter;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class MainActivity extends AppCompatActivity {
    private BlockingDeque<String> queue = new LinkedBlockingDeque<>();
    ConnectionFactory factory = new ConnectionFactory();
    Thread subscribeThread;
    Thread publishThread;

    void publishMessage(String message) {
        try {
            Log.d("", "[q] " + message);
            queue.putLast(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setupConnectionFactory() {
        String uri = "amqp://guest:guest@192.168.0.109:15672";
        factory.setAutomaticRecoveryEnabled(false);
        factory.setUsername("famous");
        factory.setPassword("famous");
        factory.setHost("10.222.2.16");
        factory.setPort(5672);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}