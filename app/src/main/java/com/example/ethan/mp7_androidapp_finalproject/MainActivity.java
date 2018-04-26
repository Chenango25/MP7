package com.example.ethan.mp7_androidapp_finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MP7:Main";
    private static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_main);
        final Button getArticle = findViewById(R.id.getArticle);
        getArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "getArticle button clicked");
                startArticle();
            }
        });
    }
    // nytimes/ttle something like this
    // enter query: return results from different sources ex:BBC NYtimes
    // trys to throw out invalid input, return msg in toast
    void startArticle() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://newsapi.org/v2/everything?q=bitcoin&apiKey="
                            + BuildConfig.API_KEY,
                    new JSONObject(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                Log.d(TAG, response.toString(2));
                                final TextView titles = findViewById(R.id.titles);
                                titles.setText(response.toString());
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
    String editText(final String json) {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(json).getAsJsonObject();
        JsonArray array = object.getAsJsonArray("articles");
        JsonObject result = array.get(0).getAsJsonObject();
        String last = result.get("description").getAsString();
        //trim "\"
        return last;
    }

}
