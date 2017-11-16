package com.ericsson.etrucvu.itransfer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // get the intent msg sent from Main
        Intent gotIntent = getIntent();
        String msg = gotIntent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        TextView txResultText = findViewById(R.id.transactionResultText);
        txResultText.setText(msg);

    }
}
