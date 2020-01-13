package com.example.pds_project_2;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pds_project_2.ui.main.SectionsPagerAdapter;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    private ConnectionFactory factory;
    private String ROUTING_KEY = "cnn";
    private String EXCHANGE_NAME = "pds_project";
    private Consumer consumer;
    private Producer producer;

    private void setupConnectionFactory () {
        try {
            factory = new ConnectionFactory();
//            factory.setUri("amqp://famous:famous@192.168.0.206:15672/");
            factory.setAutomaticRecoveryEnabled(false);
            factory.setUsername("famous");
            factory.setPassword("famous");
            factory.setHost("192.168.0.206");
            factory.setPort(5672);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpConsumerAndProducer () {
        consumer = new Consumer(this, factory, ROUTING_KEY, EXCHANGE_NAME);
        producer = new Producer(factory, ROUTING_KEY, EXCHANGE_NAME);
        executorService.execute(consumer);
        executorService.execute(producer);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupConnectionFactory();
        setUpConsumerAndProducer();

        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), producer, consumer);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

//        FloatingActionButton fab = findViewById(R.id.fab);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdownNow();
        executorService.shutdown();
    }
}