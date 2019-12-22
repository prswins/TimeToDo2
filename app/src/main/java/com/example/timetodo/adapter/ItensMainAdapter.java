package com.example.timetodo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetodo.R;
import com.example.timetodo.model.ItemMain;

import java.util.List;

public class ItensMainAdapter extends RecyclerView.Adapter<ItensMainAdapter.MyViewHolder> {
    public ItensMainAdapter(List<ItemMain> listaItens, Context context) {
        this.listaItens = listaItens;
        this.context = context;
    }

    List<ItemMain> listaItens;
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter_main, parent, false);

        return new ItensMainAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        listaItens.get(position).getCampo1();
                holder.campo1.setText(listaItens.get(position).getCampo1());
        holder.campo2.setText(listaItens.get(position).getCampo2());
        holder.campo3.setText(listaItens.get(position).getCampo3());
        holder.imagem.setImageResource(R.drawable.tarefa);

    }

    @Override
    public int getItemCount() {
        return listaItens.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView campo1, campo2, campo3;
        ImageView imagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.campo1  = itemView.findViewById(R.id.textViewCampo1);
            this.campo2 = itemView.findViewById(R.id.textViewCampo2);
            this.campo3 = itemView.findViewById(R.id.textViewCampo3);
            this.imagem = itemView.findViewById(R.id.imageView4);
        }
    }
}
