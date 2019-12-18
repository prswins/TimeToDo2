package com.example.timetodo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetodo.R;
import com.example.timetodo.model.Projeto;

import java.util.List;

public class ListaProjetosAdapter extends RecyclerView.Adapter<ListaProjetosAdapter.MyViewHolder> {
    List<Projeto> projetos;
    Context context;

    public ListaProjetosAdapter(List<Projeto> projetos, Context applicationContext) {
        this.projetos = projetos;
        this.context = applicationContext;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter_projetos, parent, false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Projeto projeto = projetos.get(position);
        holder.nome.setText(projeto.getTitulo());
        holder.desc.setText(projeto.getDescricao());
        holder.dataInicio.setText(projeto.getDataInicio());
        holder.dataFim.setText(projeto.getDataFim());

    }

    @Override
    public int getItemCount() {
        return projetos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nome,desc,dataInicio, dataFim;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textViewAdapterPTitulo);
            desc = itemView.findViewById(R.id.textViewAdapterPDesc);
            dataInicio = itemView.findViewById(R.id.textViewAdapterPDI);
            dataFim = itemView.findViewById(R.id.textViewAdaperPDF);
        }
    }
}
