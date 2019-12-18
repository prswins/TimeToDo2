package com.example.timetodo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetodo.R;
import com.example.timetodo.model.Tarefa;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListaTarefasAdapter extends RecyclerView.Adapter<ListaTarefasAdapter.MyViewHolder> {
    List<Tarefa> tarefas;
    Context context;

    public ListaTarefasAdapter(List<Tarefa> tarefas, Context context) {
        this.tarefas = tarefas;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter_tarefas, parent, false);



        return new ListaTarefasAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Tarefa t = tarefas.get(position);
        holder.titulo.setText(t.getTitulo());
        holder.status.setText(t.getStatus());
        holder.descricao.setText(t.getDescricao());
        holder.dataAtribuicao.setText(t.getDataCriacao());
        String dataIni, dataFim;
        dataIni = "Data de inicio da Tarefa :";
        dataFim = "Data prevista para fim da tarefa :";
        holder.dataInicial.setText(dataIni  +t.getDataInicio());
        holder.dataFinal.setText(dataFim+t.getDataFim() );
        holder.usuario.setText(t.getFuncionarioResponsavel());
        holder.tempo.setText("Tempo total trabalhado: "+String.valueOf((t.getTempoTotalTrabalho())+" horas."));


        holder.fundo.setBackgroundColor(context.getResources().getColor(R.color.colorStatusFazendo));

        if (!(t.getStatus()== null)) {

            if (t.getStatus().equals("afazer") && t.getTempoTotalTrabalho() == 0) {
                holder.fundo.setBackgroundColor(context.getResources().getColor(R.color.colorStatusAfazer));
                //   ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("colorStatusAfazer")));
            } else if (t.getStatus().equals("fazendo") || t.getTempoTotalTrabalho() > 0) {
                SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
                Date data = new Date();
                String dataFormatada = formataData.format(data);
                if (t.getDataFim() != null && t.getDataFim().equals(dataFormatada)) {
                    holder.fundo.setBackgroundColor(context.getResources().getColor(R.color.colorStatusAtrasado));
                } else {
                    holder.fundo.setBackgroundColor(context.getResources().getColor(R.color.colorStatusFazendo));
                }

            } else if (t.getStatus().equals("concluida")) {
                holder.fundo.setBackgroundColor(context.getResources().getColor(R.color.colorStatusConcluida));
                //  ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("colorStatusConcluido")));
            }
        }else{
            holder.fundo.setBackgroundColor(context.getResources().getColor(R.color.colorStatusNenhum));
        }





    }

    @Override
    public int getItemCount() {
        return tarefas.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView titulo, descricao,status, usuario, dataAtribuicao, dataInicial, dataFinal, tempo;
        LinearLayout fundo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textViewAdaTTitulo);
            descricao = itemView.findViewById(R.id.textViewAdaTDesc);
            status = itemView.findViewById(R.id.textViewAdaTStatus);
            dataAtribuicao = itemView.findViewById(R.id.textViewAdaTDTC);
            dataInicial = itemView.findViewById(R.id.textViewAdaTDTI);
            dataFinal = itemView.findViewById(R.id.textViewAdaTDTF);
            usuario = itemView.findViewById(R.id.textViewAdaTUsuario);
            fundo = itemView.findViewById(R.id.fundoTarefas);
            tempo = itemView.findViewById(R.id.textViewAdaTTempo);


        }
    }
}
