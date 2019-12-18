package com.example.timetodo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetodo.R;
import com.example.timetodo.model.HistoricoAtividadesTarefas;

import java.util.List;

public class ListaHistoricoAdapter extends RecyclerView.Adapter<ListaHistoricoAdapter.MyViewHolder> {
    List<HistoricoAtividadesTarefas> listaHistorico;
    Context context;

    public ListaHistoricoAdapter(List<HistoricoAtividadesTarefas> listaHistorico, Context context) {
        this.listaHistorico = listaHistorico;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter_historico_tarefa, parent, false);

        return new ListaHistoricoAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HistoricoAtividadesTarefas historico = listaHistorico.get(position);
        holder.dtUltimaModificacao.setText("Data :"+historico.getUltimaAtualizacao());
        holder.tempo.setText("Tempo de trabalho :"+historico.getTempoDetrabalho());

    }

    @Override
    public int getItemCount() {
        return listaHistorico.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView dtUltimaModificacao, tempo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dtUltimaModificacao = itemView.findViewById(R.id.textViewAHTData);
            tempo = itemView.findViewById(R.id.textViewAHTTempo);

        }




    }
}


