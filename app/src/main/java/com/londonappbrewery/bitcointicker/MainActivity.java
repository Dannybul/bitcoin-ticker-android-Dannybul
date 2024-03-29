package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";
    private String currentTicker;
    private String totalUrl;

    // Member Variables:
    TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
//I will want to make a attach the current ticker string to the url
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Bitcoin", "you selected something" + adapterView.getItemAtPosition(i));
                currentTicker = adapterView.getItemAtPosition(i).toString();
                Log.d("Bitcoin", "this is a string" + currentTicker);
                totalUrl = BASE_URL+currentTicker;
                totalUrl = totalUrl.trim();
                letsDoSomeNetworking(totalUrl);
                Log.d("Bitcoin", "this is the total url: "+ totalUrl);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("Bitcoin", "nothing selected");
            }
        });

    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String url) {

        AsyncHttpClient client = new AsyncHttpClient();
//https://stackoverflow.com/questions/19858251/http-status-code-0-error-domain-nsurlerrordomain
        //could just be a problem with the request
//the link works but it is still failing
        //If it works in clima then it should work here
        //change lets dosomenetworking
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onStart(){
                Log.d("Bitcoin", "onstart called");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                Log.d("Bitcoin", "JSON: " + response.toString());
                BitcoinData jsonThing = new BitcoinData(response);
                double price = jsonThing.getBitcoinPrice();
                String thePrice = Double.toString(price);
                mPriceTextView.setText(thePrice);


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject l) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Bitcoin", "Request fail! Status code: " + statusCode);
                Log.d("Bitcoin", "Fail response: " + e.toString());
                Log.e("Bitcoin", e.toString());
                Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo){
                Log.d("Bitcoin", "it was retried"+ retryNo + " times");
            }




        });


    }


}
