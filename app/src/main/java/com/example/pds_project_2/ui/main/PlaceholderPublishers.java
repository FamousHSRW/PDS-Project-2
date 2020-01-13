package com.example.pds_project_2.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.pds_project_2.Consumer;
import com.example.pds_project_2.Producer;
import com.example.pds_project_2.R;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderPublishers extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private static Producer producer;
    private static Consumer consumer;

    public static PlaceholderPublishers newInstance(int index, Producer sender, Consumer receiver) {
        producer = sender;
        consumer = receiver;
        PlaceholderPublishers fragment = new PlaceholderPublishers();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        Button cnn_pub = root.findViewById(R.id.publish_cnn);
        Button bbc_pub = root.findViewById(R.id.publish_bbc);
        Button bbc_sub = root.findViewById(R.id.subscribe_bbc);
        Button cnn_sub = root.findViewById(R.id.subscribe_cnn);

        bbc_pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = root.findViewById(R.id.bbc_message);
                producer.setRoutingKey("bbc");
                producer.publishMessage(et.getText().toString());
                et.setText("");
            }
        });

        cnn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    consumer.setRoutingKey("cnn");
            }
        });

        bbc_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    consumer.setRoutingKey("bbc");
            }
        });


        cnn_pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = root.findViewById(R.id.cnn_message);
                producer.setRoutingKey("cnn");
                producer.publishMessage(et.getText().toString());
                et.setText("");
            }
        });

        return root;
    }

}