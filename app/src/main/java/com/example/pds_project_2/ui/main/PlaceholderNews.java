package com.example.pds_project_2.ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.pds_project_2.Producer;
import com.example.pds_project_2.R;

/**
 * A placeholder fragment containing a simple view.
 */

public class PlaceholderNews extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    private static Producer producer;

    public static com.example.pds_project_2.ui.main.PlaceholderNews newInstance(int index, Producer sender) {
        producer = sender;
        com.example.pds_project_2.ui.main.PlaceholderNews fragment = new com.example.pds_project_2.ui.main.PlaceholderNews();
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
        View root = inflater.inflate(R.layout.fragment_messages, container, false);
        TextView textView = root.findViewById(R.id.news_header);
        textView.append("Receiving messages from: " +  producer.getRoutingKey().toUpperCase() + "\n");

        return root;
    }

}
