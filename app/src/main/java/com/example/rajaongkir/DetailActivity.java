package com.example.rajaongkir;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {
    private TextView mTextCostDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mTextCostDetail=findViewById(R.id.textViewCostDetail);
        String cityOriginId=getIntent().getStringExtra("ORIGIN_ID");
        String cityDestinationId=getIntent().getStringExtra("DESTINATION_ID");
        fetchCost(cityOriginId,cityDestinationId,1000,"jne");
    }

    private void fetchCost(String originId,
                           String destinationId,
                           double weight,
                           String courier)
    {
//        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        RequestBody body = RequestBody.create(mediaType, "origin=501&destination=114&weight=1700&courier=jne");
//        Request request = new Request.Builder()
        AndroidNetworking
                .post("https://api.rajaongkir.com/starter/cost")
                .addHeaders("key", "20139e59ac8fa73f121cb25a4d7e9415")
//                .addHeaders("content-type", "application/x-www-form-urlencoded")
                .addBodyParameter("origin",originId)
                .addBodyParameter("destination",destinationId)
                .addBodyParameter("weight",String.valueOf(weight))
                .addBodyParameter("courier", courier)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            showCostDetail(response
                            .getJSONObject("rajaongkir")
                            .getJSONArray("results"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("RESPONSE DetailActivity",response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void showCostDetail(JSONArray results) throws JSONException {
        JSONObject courier=results.getJSONObject(0);
        JSONArray costs= courier.getJSONArray("costs");
        mTextCostDetail.setText("");
        for (int i =0; i<costs.length();i++){
            JSONObject pack=costs.getJSONObject(i);
            JSONObject cost=pack.getJSONArray("cost").getJSONObject(0);

            String service=pack.getString("service");
            String description=pack.getString("description");
            String value =cost.getString("value");
            String etd = cost.getString("etd");

            String lineFormat1="%s (%s)";
            String lineFormat2="IDR %s | ETD %s";

            String line1=String.format(lineFormat1,service,description);
            String line2=String.format(lineFormat2,value,etd);

            String content=line1+"\n"+line2+"\n\n";
            mTextCostDetail.append(content);

        }
    }
}
