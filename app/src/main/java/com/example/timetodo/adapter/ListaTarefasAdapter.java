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

import java.text.ParseException;
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
        dataIni = "Data de inicio da Tarefa: ";
        dataFim = "Data prevista para fim da tarefa: ";
        holder.dataInicial.setText(dataIni +t.getDataInicio());
        holder.dataFinal.setText(dataFim +t.getDataFim() );
        holder.usuario.setText(t.getFuncionarioResponsavel());
        holder.tempo.setText("Tempo total trabalhado: "+String.valueOf((t.getTempoTotalTrabalho()/3600)+" horas."));


        if (!(t.getStatus()== null)){
            if(t.getStatus().equals("afazer")){
                holder.fundo.setBackground(context.getResources().getDrawable(R.drawable.layout_afazer));
            }else if (t.getStatus().equals("fazendo")){
                holder.fundo.setBackground(context.getResources().getDrawable(R.drawable.layout_fazendo));
                SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
                Date dataAtual = new Date();
                if (t.getDataFim() != null  ) {
                    Date convertedDateFim = new Date();
                    try {
                        convertedDateFim = formataData.parse(t.getDataFim());
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if(dataAtual.after(convertedDateFim)){
                        holder.fundo.setBackground(context.getResources().getDrawable(R.drawable.layout_atrasado));
                    }else{
                        holder.fundo.setBackground(context.getResources().getDrawable(R.drawable.layout_fazendo));
                    }
                }


            }else if(t.getStatus().equals("concluido")){
                holder.fundo.setBackground(context.getResources().getDrawable(R.drawable.layout_concluido));
            }
        }else if (t.getDataFim() == null){
            holder.fundo.setBackground(context.getResources().getDrawable(R.drawable.layout_nenhum));
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
