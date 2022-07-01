package com.matrix_maeny.randomactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;

    private String activityName;
    private String type;
    private int participants;

    private ProgressDialog progressDialog;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    private void initialize() {
        textView = findViewById(R.id.textView);
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Fetching...");
        progressDialog.setCancelable(false);

        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                // go to about activity
                startActivity(new Intent(MainActivity.this,AboutActivity.class));
                break;
            case R.id.refresh_data:
                // refresh data;
                getData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getData(){

        requestQueue.getCache().clear();

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        String url = "https://www.boredapi.com/api/activity";

        progressDialog.show();
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {

            try {
                activityName = response.getString("activity");
                type = response.getString("type");
                participants = response.getInt("participants");
                postDetails();
            }catch (Exception e){
                e.printStackTrace();
            }
            progressDialog.dismiss();

        }, error -> {

            progressDialog.dismiss();
            String msg = error.toString();
            if(msg.contains("UnknownHost"))
                msg = "Error: No internet";

            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();


        });

        queue.add(objectRequest);
    }

    private void postDetails() {
        String totalTxt = activityName+"\n\nType: "+type+"\n\nParticipants: "+participants;

        textView.setText(totalTxt);
    }
}