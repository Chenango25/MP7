package com.example.ethan.mp7_androidapp_finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MP7:Main";
    private static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_main);
        final Button getArticle = findViewById(R.id.getArticle);
        final EditText topic = findViewById(R.id.Topic);
        final EditText date = findViewById(R.id.Date);
        Log.d(TAG, "hihihi");
        getArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "getArticle button clicked");
                if (topic.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"Topic cannot be empty",Toast.LENGTH_LONG).show();
                }
                startArticle(topic.getText().toString(), date.getText().toString());
            }
        });
    }
    // nytimes/ttle something like this
    // enter query: return results from different sources ex:BBC NYtimes
    // trys to throw out invalid input, return msg in toast
    void startArticle(String topic, String date) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                   "https://newsapi.org/v2/everything?q=" + topic + "&from=" + date
                           + "&sortBy=popularity&apiKey="
                           + BuildConfig.API_KEY,
                    new JSONObject(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                Log.d(TAG, response.toString(2));
                                final TextView context = findViewById(R.id.context);
                                context.setText(editText(response));
                            } catch (JSONException ignored) { }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    String editText(JSONObject n) {
        JsonParser parser = new JsonParser();
        String result = n.toString();
        JsonObject first = parser.parse(result).getAsJsonObject();
        JsonArray array = first.getAsJsonArray("articles");
        if (array.toString() == null || array.toString().length() == 0
                || first.get("totalResults").getAsInt() == 0
                || !first.get("status").getAsString().equals("ok")) {
            return "Invalid Input: Check date or use a different word.";
        }
        String[] array2 = new String[18];
        String intent = "\n";
        for (int i = 0; i < array2.length; i++) {
            if (i % 3 == 0) {
                JsonObject second = array.get(i).getAsJsonObject();
                StringBuffer temp = new StringBuffer();
                temp = temp.append("Title: ");
                temp = temp.append(second.get("title").getAsString());
                temp = temp.append(intent);
                array2[i] = temp.toString();
            } else if (i % 3 == 1) {
                JsonObject second = array.get(i).getAsJsonObject();
                StringBuffer temp2 = new StringBuffer();
                temp2 = temp2.append("Description: ");
                temp2 = temp2.append(second.get("description").getAsString());
                array2[i] = temp2.toString();
            } else {
                StringBuffer temp1 = new StringBuffer();
                JsonObject second = array.get(i).getAsJsonObject();
                temp1 = temp1.append(second.get("url").getAsString());
                temp1 = temp1.append(intent);
                array2[i] = temp1.toString();
            }

        }
        StringBuffer sb = new StringBuffer();
        for (String a : array2) {
            sb = sb.append(a);
            sb = sb.append("\n");
        }
        //trim "\"
        return sb.toString();
    }

}
