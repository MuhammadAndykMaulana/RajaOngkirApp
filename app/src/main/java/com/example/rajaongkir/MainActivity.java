package com.example.rajaongkir;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Add Attribute inisialisasi variable
    private Spinner mSpinnerProvOrigin;
    private Spinner mSpinnerCityOrigin;
    private Spinner mSpinnerProvDes;
    private Spinner mSpinnerCityDes;
    private Button mBtnCekCost;
    private JSONArray mProvinceList;
    private String mCityOriginId, mCityDesId;
    public static final String MESSAGE_EXTRA="TEXT_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Inisialisasi Mula" lifecycle saat on create, on start, on resume
        mSpinnerCityDes=findViewById(R.id.SpinnerCityDes);
        mSpinnerProvDes=findViewById(R.id.SpinnerProvDes);
        mSpinnerCityOrigin=findViewById(R.id.SpinnerCity);
        mSpinnerProvOrigin=findViewById(R.id.SpinnerProv);
        mBtnCekCost=findViewById(R.id.btnSubmit);
        fetchProvinceList();
    }

    private void fetchProvinceList() {
        //Add Methode Get and Key
        AndroidNetworking
                .get("https://api.rajaongkir.com/starter/province")
                .addHeaders("Key","20139e59ac8fa73f121cb25a4d7e9415")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE PROVINCE",response.toString());
                        try {
                            JSONArray provinceList=response
                                    .getJSONObject("rajaongkir")
                                    .getJSONArray("results");
                            mProvinceList=provinceList;
                            showProvince();
                            showProvinceDes();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("RESPONSE PROVINCE","JSON Error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    private void showProvinceDes() throws JSONException {
        List <String> provinceNameList= new ArrayList<>();

        for (int i=0; i<mProvinceList.length();i++){
            JSONObject province= mProvinceList.getJSONObject(i);
            provinceNameList.add(province.getString("province"));
        }
        ArrayAdapter <String> adapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_spinner_item,
                android.R.id.text1,
                provinceNameList
        );
        mSpinnerProvDes.setAdapter(adapter);
        mSpinnerProvDes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String provinceId=null;
                try {
                    provinceId=mProvinceList
                            .getJSONObject(i)
                            .getString("province_id");
                    fetchCityList(provinceId,R.id.SpinnerCityDes);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void fetchCityList(String provinceId, final int citySpinnerId) {
        AndroidNetworking
                .get("https://api.rajaongkir.com/starter/city")
                .addHeaders("Key","20139e59ac8fa73f121cb25a4d7e9415")
                .addQueryParameter("province",provinceId)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE CITY",response.toString());
                        try {
                            JSONArray cityList= response
                                    .getJSONObject("rajaongkir")
                                    .getJSONArray("results");
                            if (citySpinnerId ==R.id.SpinnerCity){
                                showCity(cityList);
                            }
                            else if(citySpinnerId==R.id.SpinnerCityDes){
                                showCityDes(cityList);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("RESPONSE CITY","ERROR while fetching JSON");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }


    private void showProvince() throws JSONException {
        List <String> provinceNameList= new ArrayList<>();

        for (int i=0; i<mProvinceList.length();i++){
            JSONObject province= mProvinceList.getJSONObject(i);
            provinceNameList.add(province.getString("province"));
        }
        ArrayAdapter <String> adapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_spinner_item,
                android.R.id.text1,
                provinceNameList
        );
        mSpinnerProvOrigin.setAdapter(adapter);
        mSpinnerProvOrigin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String provinceId=null;
                try {
                    provinceId=mProvinceList
                            .getJSONObject(i)
                            .getString("province_id");
                    fetchCityList(provinceId,R.id.SpinnerCity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void showCity(final JSONArray cityList) throws JSONException {
        List <String> cityNameList= new ArrayList<>();
        for (int i=0; i<cityList.length();i++){
            JSONObject city= cityList.getJSONObject(i);
            cityNameList.add(city.getString("city_name"));
        }
        Log.d("RESPONSE E KOTA",cityNameList.toString());
        ArrayAdapter <String> adapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_spinner_item,
                android.R.id.text1,
                cityNameList
        );
        mSpinnerCityOrigin.setAdapter(adapter);
        mSpinnerCityOrigin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    JSONObject city = cityList.getJSONObject(i);
                    mCityOriginId= city.getString("city_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void showCityDes(final JSONArray cityList) throws JSONException {
        List <String> cityNameList= new ArrayList<>();
        for (int i=0; i<cityList.length();i++){
            JSONObject city= cityList.getJSONObject(i);
            cityNameList.add(city.getString("city_name"));
        }
        Log.d("RESPONSE E KOTA",cityNameList.toString());
        ArrayAdapter <String> adapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_spinner_item,
                android.R.id.text1,
                cityNameList
        );
        mSpinnerCityDes.setAdapter(adapter);
        mSpinnerCityDes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                JSONObject city = null;
                try {
                    city = cityList.getJSONObject(i);
                    mCityDesId=city.getString("city_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void checkcost(View view) {
        Intent checkcoast= new Intent(this, DetailActivity.class);
        checkcoast.putExtra("ORIGIN_ID",mCityOriginId);
        checkcoast.putExtra("DESTINATION_ID",mCityDesId);
        startActivity(checkcoast);
    }
}
