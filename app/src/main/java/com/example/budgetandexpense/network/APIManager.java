package com.example.budgetandexpense.network;

import org.json.JSONObject;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.budgetandexpense.network.APIConstant.WRONG_RATE;

public class APIManager {
    /* get the dollar transfer rate API CALL */
    public static void getDataAsync(final Callback callback) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder httpBuilder = HttpUrl.parse(APIConstant.BASE_URL).newBuilder();

        httpBuilder.addQueryParameter(APIConstant.ACCESS_KEY, APIConstant.ACCESS_VALUE);
        httpBuilder.addQueryParameter(APIConstant.CURRIENCIES_KEY, APIConstant.CURRIENCIES_VALUE);
        httpBuilder.addQueryParameter(APIConstant.FORMAT_KEY, APIConstant.FORMAT_VALUE);

        Request request = new Request.Builder().url(httpBuilder.build()).build();
        client.newCall(request).enqueue(callback);
    }

    /* get the transfer rate between USD and NZD */
    public static double getRate(Response response) {
        try {
            String in = response.body().string();
            JSONObject reader= new JSONObject(in);
            JSONObject quotes = reader.getJSONObject("quotes");
            String usd_to_nzd_transfer_string = quotes.getString("USDNZD");
            Double usd_to_nzd_transfer = Double.parseDouble(usd_to_nzd_transfer_string);
            return usd_to_nzd_transfer;
        } catch (Exception e) {
            return WRONG_RATE;
        }
    }
}
