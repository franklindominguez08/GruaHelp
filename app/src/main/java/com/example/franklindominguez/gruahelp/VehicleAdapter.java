package com.example.franklindominguez.gruahelp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;




public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {
    List<Vehicle> lista;
    private RecyclerViewOnItemClickListener onItemClickListener;

    public VehicleAdapter(List<Vehicle> vehicles)
    {
        lista = vehicles;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vehicle_view,viewGroup,false);


        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder vehicleViewHolder, int i) {
        vehicleViewHolder.bindClient(lista.get(i));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder{

        TextView textmarca;
        TextView textmodelo;
        TextView textano;
        TextView textplaca;

        public VehicleViewHolder(View itemView)
        {
            super(itemView);

            textmarca = itemView.findViewById(R.id.txtMarca);
            textmodelo = itemView.findViewById(R.id.txtModelo);
            textano = itemView.findViewById(R.id.txtAno);
            textplaca = itemView.findViewById(R.id.txtPlaca);
        }


        public void bindClient(Vehicle vehicle)
        {

            textmarca.setText(vehicle.getMarca());
            textmodelo.setText(vehicle.getModelo());
            textano.setText(vehicle.getAno());
            textplaca.setText(vehicle.getPlaca());
        }


    }
}
