package com.londonappbrewery.bitcointicker;

import org.json.JSONException;
import org.json.JSONObject;

public class BitcoinData {

    private double bitcoinPrice;

    public BitcoinData(JSONObject Json){
        try {
            this.bitcoinPrice = Json.getDouble("last");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public double getBitcoinPrice(){
        return this.bitcoinPrice;
    }


}
