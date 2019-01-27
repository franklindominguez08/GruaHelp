package com.example.franklindominguez.gruahelp;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {
    List<Client> lista;
    private RecyclerViewOnItemClickListener onItemClickListener;

    public ClientAdapter(List<Client> clients)
    {
        lista = clients;
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.client_view,viewGroup,false);


        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder clientViewHolder, int i) {
           clientViewHolder.bindClient(lista.get(i));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ClientViewHolder extends RecyclerView.ViewHolder{

        TextView textname;
        TextView textcedula;

        public ClientViewHolder(View itemView)
        {
            super(itemView);

            textname = itemView.findViewById(R.id.txtName);
            textcedula = itemView.findViewById(R.id.txtCedula);
        }


        public void bindClient(Client client)
        {
            String name = client.getNombre()+" "+client.getApellido();
            textname.setText(name);
            textcedula.setText(client.getCedula());
        }


    }
}
