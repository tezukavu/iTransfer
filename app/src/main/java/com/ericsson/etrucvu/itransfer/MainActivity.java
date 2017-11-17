package com.ericsson.etrucvu.itransfer;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    AlertDialog.Builder builder;
    EditText receiverId;
    EditText senderId;
    EditText amount;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {

        String server_url = "http://192.168.43.55/dbconfig.php";

        receiverId = (EditText) findViewById(R.id.editReceiverId);
        final String receiverIdStr = receiverId.getText().toString();

        senderId = (EditText) findViewById(R.id.editSenderId);
        final String senderIdStr = senderId.getText().toString();

        amount = (EditText) findViewById(R.id.editAmount);
        final String amountStr = senderId.getText().toString();

        builder = new AlertDialog.Builder(MainActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                builder.setTitle("Server Response");
                builder.setMessage("Response :"+response);
                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // reset all the fields
                        receiverId.setText("");
                        senderId.setText("");
                        amount.setText("");
                    }
                };

                builder.setPositiveButton("OK", onClickListener);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"some error found..." + error.toString(),Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> Params = new HashMap<String, String>();
                Params.put("Sender",senderIdStr);
                Params.put("Receiver",receiverIdStr);
                Params.put("Amount",amountStr);
                return Params;
            }
        };
        Mysingleton.getInstance(MainActivity.this).addTorequestque(stringRequest);

        /*
        Intent intent;
        intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editSenderId);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        */
    }


}
