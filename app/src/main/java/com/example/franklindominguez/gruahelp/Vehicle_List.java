package com.example.franklindominguez.gruahelp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Vehicle_List extends AppCompatActivity {
    private final String baseURL = "http://190.167.214.34:8080/grua/";
    List<Vehicle> vehicleList = new ArrayList<>();
    VehicleAdapter adapter; //= new ClientAdapter(clientList);
    RecyclerView recyclerView;
    Vehicle yovehicle = new Vehicle();
    Retrofit retrofit;
    Client yoClient = new Client();
    String myimei = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle__list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        yoClient = (Client) getIntent().getSerializableExtra("IMEI");
        myimei=yoClient.getImei();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
*/
                Intent intent = new Intent(Vehicle_List.this,SaveVehicle.class);
                intent.putExtra("IMEI",yoClient);
                startActivity(intent);
            }
        });



         retrofit = new Retrofit.Builder().baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recyclerView = findViewById(R.id.recyclerview1);

        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new VehicleAdapter(vehicleList);
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

        DividerItemDecoration decoration = new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());

        recyclerView.addItemDecoration(decoration);

        final GestureDetector mGestureDetector = new GestureDetector(Vehicle_List.this, new GestureDetector.SimpleOnGestureListener(){
            @Override public boolean onSingleTapUp(MotionEvent e)
            {
                return  true;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                try{
                    View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());
                    if(child != null && mGestureDetector.onTouchEvent(motionEvent))
                    {
                        int position = recyclerView.getChildAdapterPosition(child);
                        Toast.makeText(Vehicle_List.this,"You Select : "+ vehicleList.get(position).getMarca()+" 's Option",Toast.LENGTH_LONG ).show();
                        return  true;
                    }
                }
                catch (Exception ex)
                {
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

       getVehicleList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getVehicleList();
    }

    private void getVehicleList()
    {
        GruaService gruaService = retrofit.create(GruaService.class);
        Call<List<Vehicle>> lista = gruaService.getVehicle(myimei);

        lista.enqueue(new Callback<List<Vehicle>>() {
            @Override
            public void onResponse(Call<List<Vehicle>> call, Response<List<Vehicle>> response) {
                if (response.isSuccessful()) {
                    vehicleList = response.body();
                    Log.i("rodolfo", String.valueOf(response.body().size()));
                    adapter = new VehicleAdapter(vehicleList);

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
                //Log.i("rodolfoError", String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<List<Vehicle>> call, Throwable t) {
                Log.i("errorOnRetr", t.getMessage());
            }
        });
    }

}
