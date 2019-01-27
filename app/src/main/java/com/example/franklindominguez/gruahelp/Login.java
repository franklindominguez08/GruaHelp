package com.example.franklindominguez.gruahelp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    Button btnSave;
    EditText nombre;
    EditText apellido;
    EditText cedula;
    String imei="";
    Client yoClient = new Client();
    private Retrofit retrofit;
    private final String baseURL = "http://190.167.214.34:8080/grua/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         retrofit = new Retrofit.Builder().baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        imei = getIntent().getStringExtra("IMEI");

       // Toast.makeText(Login.this,"yoo   "+imei,Toast.LENGTH_LONG).show();

        btnSave  = findViewById(R.id.btnGuardar);
        nombre = findViewById(R.id.txtLoginName);
        apellido = findViewById(R.id.txtLoginLastname);
        cedula = findViewById(R.id.txtLoginCedula);



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Toast.makeText(Login.this,"klk palomo",Toast.LENGTH_LONG).show();
                Client miclient = new Client();
                miclient.setNombre(nombre.getText().toString());
                miclient.setApellido(apellido.getText().toString());
                miclient.setCedula(cedula.getText().toString());
                miclient.setImei(imei);


                insertClient(miclient);



            }
        });
    }
    private void insertClient( Client client)
    {
        GruaService service = retrofit.create(GruaService.class);
        Call<Client> existsClient = service.addClient(client);
        existsClient.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if(response.isSuccessful()) {
                    Context context = getBaseContext();
                    yoClient = response.body();

                    if(yoClient.getID() != null && yoClient.getID()!=0) {
                        Log.i("El Cliente se registro con exito ", yoClient.getApellido());
                        Intent intent = new Intent(Login.this, Vehicle_List.class);
                        intent.putExtra("IMEI",yoClient);
                        //  Bundle bundle = new Bundle();

                        startActivity(intent);
                    }
                    else {
                        Log.i("El Cliente se registro con error ", yoClient.getApellido());

                    }
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Log.i("El Cliente registro error",t.getMessage());

            }
        });
    }
}
