package com.example.franklindominguez.gruahelp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.sql.ClientInfoStatus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SaveVehicle extends AppCompatActivity  {

    Client yoClient = new Client();
    Vehicle yoVehicle=new Vehicle();
    Spinner marcas,modelos;
    String veano,veplaca,vemarca,vemodelo;
    EditText ano,placa;
    Button btnguardar;
    String[] mismarcas = new String[]{"Honda","Toyota","Hyundai","Mitsubishi","Nissan"};
    String[] mismodelos = new String[]{};
    private Retrofit retrofit;
    private final String baseURL = "http://190.167.214.34:8080/grua/";

    public String[] getModelos(String marca)
    {
        switch (marca)
        {
            case "Honda":
                return  new String[]{"Civic","Accord","CRV","PILOT"};
            case "Toyota":
                return  new String[]{"Corolla","Camry","4Runner","Prado"};

            case "Hyundai":
                return  new String[]{"Sonata N20","Sonata Y20","Santa Fe","Tucson"};

            case "Mitsubishi":
                return  new String[]{"Montero","Pajero"};

            case "Nissan":
                return  new String[]{"Sentra","Murano","Primera","Qaskai"};

            default:
                return new String[]{"Otro"};

        }
    }
    private void insertVehicle( Vehicle vehicle)
    {
        GruaService service = retrofit.create(GruaService.class);
        Call<Vehicle> existsVehicle = service.addVehicle(vehicle);
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(SaveVehicle.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Completando....");
        progressDoalog.setTitle("Status de la Creacion del Vehiculo");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // show it
        progressDoalog.show();
        existsVehicle.enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                progressDoalog.dismiss();
                if(response.isSuccessful()) {
                    Context context = getBaseContext();
                    yoVehicle = response.body();

                    if(yoVehicle.getID() != null && yoVehicle.getID()!=0) {

                     //   Toast.makeText(getBaseContext(),"El Vehiculo: "+yoVehicle.getMarca()+" se registro con exito ",Toast.LENGTH_LONG).show();
                        finish();
                           /* Intent intent = new Intent(SaveVehicle.this, Vehicle_List.class);
                            intent.putExtra("IMEI", yoClient);
                            //  Bundle bundle = new Bundle();
                            context.startActivity(intent);*/

                    }
                    else {
                        //Toast.makeText(getBaseContext(),"El Vehiculo no pudo ser registrado",Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Vehicle> call, Throwable t) {
                progressDoalog.dismiss();
                Log.i("El Cliente registro error",t.getMessage());

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_vehicle);

        retrofit = new Retrofit.Builder().baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        btnguardar = findViewById(R.id.btnGuardarVehicle);
        ano = findViewById(R.id.txtVehicleYear);
        placa = findViewById(R.id.txtVehicelePlaca);
        marcas = findViewById(R.id.ddlMarcas);
        modelos = findViewById(R.id.ddlModelo);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mismarcas);

        marcas.setAdapter(adapter);
        marcas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vemarca=mismarcas[position];
                mismodelos = getModelos(mismarcas[position]);
                ArrayAdapter<String> adaptermodelo = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, mismodelos);
                modelos.setAdapter(adaptermodelo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        modelos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vemodelo = mismodelos[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        yoClient = (Client) getIntent().getSerializableExtra("IMEI");
        Log.i("clientid",yoClient.getID().toString());

        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( placa.getText().toString().equals(""))
                {
                    Snackbar.make(v, "Favor Colocar la Placa", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                Vehicle vehicle = new Vehicle();
                vehicle.setClientID(yoClient.getID());

                vehicle.setAno(ano.getText().toString());
                vehicle.setPlaca(placa.getText().toString());
                vehicle.setMarca(vemarca);
                vehicle.setModelo(vemodelo);

                insertVehicle(vehicle);

            }
        });
       // Log.i("mirame",yoClient.getImei());
       // Toast.makeText(this,"Klk  "+yoClient.getNombre(),Toast.LENGTH_LONG).show();
        /*
        Snackbar.make(getApplicationContext(), "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/
    }


}
