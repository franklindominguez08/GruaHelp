package com.example.franklindominguez.gruahelp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final String baseURL = "http://190.167.214.34:8080/grua/";
     List<Client> clientList = new ArrayList<>();
    ClientAdapter adapter; //= new ClientAdapter(clientList);
    RecyclerView recyclerView;
    boolean intconection =false;
    Client yoClient = new Client();
    private ProgressBar mLoadingIndicator;

    String myimei = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        myimei = getCellImei();
       // Toast.makeText(MainActivity.this, myimei, Toast.LENGTH_LONG).show();

        Uri builtUri = Uri.parse("https://www.google.com").buildUpon()
                .build();

        URL url = null;
        try {
            Log.i("url",builtUri.toString());
            url = new URL(builtUri.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        new GithubQueryTask().execute(url);
Log.i("resultInter",String.valueOf(intconection));


    Retrofit retrofit = new Retrofit.Builder().baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    recyclerView = findViewById(R.id.recyc);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
    recyclerView.setLayoutManager(linearLayoutManager);
    adapter = new ClientAdapter(clientList);
        /*
        adapter = new ClientAdapter(clientList, new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                Log.i("position", String.valueOf(position));
                String ced = clientList.get(position).getCedula();
                Log.i("position2", ced);

                Toast.makeText(getApplicationContext(),"You was selected : "+ced,Toast.LENGTH_LONG).show();
            }
        });
        */
    recyclerView.setAdapter(adapter);

    DividerItemDecoration decoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());

    recyclerView.addItemDecoration(decoration);

    final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }
    });

    recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            try {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    int position = recyclerView.getChildAdapterPosition(child);
                    Toast.makeText(MainActivity.this, "You Select : " + clientList.get(position).getNombre() + " 's Option", Toast.LENGTH_LONG).show();
                    return true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }
    });

    GruaService gruaService = retrofit.create(GruaService.class);
    Log.i("El Cliente es el Sr. imei :", myimei);


    Call<Client> existsClient = gruaService.getClietByImei(myimei);
    existsClient.enqueue(new Callback<Client>() {
        @Override
        public void onResponse(Call<Client> call, Response<Client> response) {
            if (response.isSuccessful()) {
                Context context = getBaseContext();
                yoClient = response.body();
                if (yoClient.getID() != null && yoClient.getID() != 0) {
                    Log.i("El Cliente es el Sr. Master", yoClient.getApellido());
                    if (context != null) {
                        Intent intent = new Intent(MainActivity.this, Vehicle_List.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        intent.putExtra("IMEI", yoClient);
                        //  Bundle bundle = new Bundle();

                        context.startActivity(intent);
                    }
                } else {
                    if (context != null) {
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("IMEI", myimei);
                        context.startActivity(intent);
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<Client> call, Throwable t) {
            Log.i("El Cliente error", t.getMessage());

        }
    });

    Log.i("El Cliente es el Sr.", String.valueOf(yoClient.getID()));

    if (yoClient.getID() != null) {
        Log.i("El Cliente es el Sr.", yoClient.getApellido());

        Call<List<Client>> lista = gruaService.getCliets();

        lista.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                if (response.isSuccessful()) {
                    clientList = response.body();
                    Log.i("rodolfo", String.valueOf(response.body().size()));
                    adapter = new ClientAdapter(clientList);

                    /*adapter = new ClientAdapter(clientList, new RecyclerViewOnItemClickListener() {
                        @Override
                        public void onClick(View v, int position) {
                            String ced = clientList.get(position).getCedula();
                            Toast.makeText(getApplicationContext(),"You was selected : "+ced,Toast.LENGTH_LONG).show();
                        }
                    });*/
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                Log.i("errorOnRetr", t.getMessage());
            }
        });
    }

    }

    class GithubQueryTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (26) Override onPreExecute to set the loading indicator to visible
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);

            //  mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String resul="0";
                ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                try {
                    if (activeNetwork != null) {
                        Log.i("conectado", String.valueOf(activeNetwork.isConnected()));
                        if (activeNetwork.isConnected()) {
                            try {
                                Log.i("url2", searchUrl.toString());

                                HttpURLConnection urlc = (HttpURLConnection) searchUrl.openConnection();
                                urlc.setRequestProperty("User-Agent", "Test");
                                urlc.setRequestProperty("Connection", "close");
                                urlc.setConnectTimeout(1500);
                                urlc.connect();
                                int conn=0;
                                if(urlc != null) {
                                    conn = urlc.getResponseCode();
                                    Log.i("ERRORINTERTNET", String.valueOf(conn));
                                }
                                if(conn == 200)
                                {
                                    resul = "1";
                                }
                                return resul;
                            } catch (IOException e) {
                                Log.i("ERRORINTERNET", "Error checking internet connection " + e);
                                return resul;
                            }
                        } else {
                            return resul;
                        }
                        // connected to the internet

          /*  switch (activeNetwork.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    // connected to wifi
                    Toast.makeText(MainActivity.this,"Has WIfi internet",Toast.LENGTH_LONG).show();
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    Toast.makeText(MainActivity.this,"Has Mobile Data internet",Toast.LENGTH_LONG).show();

                    // connected to mobile data
                    break;
                default:
                    break;
            }*/
                        // return true;
                    } else {
                        // not connected to the internet

                        Toast.makeText(MainActivity.this, "No internet", Toast.LENGTH_LONG).show();
                        return resul;
                    }
                }
                catch (NetworkOnMainThreadException ex)
                {
                    mLoadingIndicator.setVisibility(View.INVISIBLE);

                    Log.i("ERRORINTER Exe", ex.getMessage());
                    return resul;
                }

        }

        @Override
        protected void onPostExecute(String statuss) {
            // COMPLETED (27) As soon as the loading is complete, hide the loading indicator
           // mLoadingIndicator.setVisibility(View.INVISIBLE);
            Log.i("staa",statuss);
           intconection = statuss.equals("0")?false:true;
           if(!intconection)
           {
               noInternetConn();
           }


           }

    }

    private void noInternetConn() {
        Log.i("entro al post","Entro");
        Toast.makeText(MainActivity.this,"Necesita Internet para acceder.",Toast.LENGTH_LONG).show();
      /*  Snackbar.make(this.findViewById(android.R.id.content), "Necesita Internet para acceder.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/

    }


    public String getCellImei()
    {
        String imei = null;
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.READ_PHONE_STATE );
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso.");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE }, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso!");
        }

        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            if(telephonyManager != null)
            {
                imei = telephonyManager.getImei();
                Log.i("imei 1", imei);
            }
            if(imei == null || imei.length()==0)
            {
                imei = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
                Log.i("imei 2", imei);

            }
        }
        catch (SecurityException ex)
        {
            Log.i("Error en getID2",ex.getMessage());
        }

        return  imei;
    }
}
