package com.example.timetodo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetodo.R;
import com.example.timetodo.model.ItemAdm;

import java.util.List;



public class ListaAdapterItemAdm extends RecyclerView.Adapter<ListaAdapterItemAdm.MyViewHolder> {
    private List<ItemAdm> ListaItemAdm;
    private Context context;



    public ListaAdapterItemAdm(List<ItemAdm> ListaItemAdm,  Context context) {
        this.ListaItemAdm = ListaItemAdm;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter_empresas_adm, parent, false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemAdm item = ListaItemAdm.get(position);

        holder.nome.setText(item.getItem1());
        holder.descricao.setText(item.getItem2());
        holder.outro.setText(item.getItem3());
    }

    @Override
    public int getItemCount() {
        return ListaItemAdm.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nome, descricao, outro;
        RecyclerView recyclerEmpresas;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textViewTitulo);
            descricao = itemView.findViewById(R.id.textViewDescricao);
            outro = itemView.findViewById(R.id.textViewNProj);


        }
    }


}