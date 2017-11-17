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

        String server_url = "http://localhost:8080/transfer.php";

        receiverId = (EditText) findViewById(R.id.editReceiverId);
        final String receiverIdStr = receiverId.getText().toString();

        senderId = (EditText) findViewById(R.id.editSenderId);
        final String senderIdStr = senderId.getText().toString();

        amount = (EditText) findViewById(R.id.editAmount);
        final String amountStr = amount.getText().toString();

        //server_url += "?senderid=" + senderIdStr + "&receiverid=" + receiverIdStr + "&amount=" + amountStr;

        //Toast.makeText(MainActivity.this,"senderid: " + senderIdStr,Toast.LENGTH_SHORT).show();

        builder = new AlertDialog.Builder(MainActivity.this);

        Response.Listener resListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(MainActivity.this,"RESPONSE: " + response,Toast.LENGTH_SHORT).show();

                builder.setTitle("Server Response");
                builder.setMessage(response);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        receiverId.setText("");
                        senderId.setText("");
                        amount.setText("");
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        };

        Response.ErrorListener errListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(MainActivity.this, "ERROR: " + error.toString(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();

                builder.setTitle("Transfer failed");
                builder.setMessage(error.toString());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        receiverId.setText("");
                        senderId.setText("");
                        amount.setText("");
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, resListener, errListener)
         {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> Params = new HashMap<String, String>();
                Params.put("senderid",senderIdStr);
                Params.put("receiverid",receiverIdStr);
                Params.put("amount",amountStr);
                return Params;
            }

        };

        //Toast.makeText(MainActivity.this,"REQUEST: " + stringRequest.toString(),Toast.LENGTH_SHORT).show();
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
