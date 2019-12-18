package com.example.timetodo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetodo.R;
import com.example.timetodo.model.Usuario;

import java.util.List;

public class ListaFuncionariosAdapter extends RecyclerView.Adapter<ListaFuncionariosAdapter.MyViewHolder> {
   List<Usuario> funcionarios;
    private Context context;

    public ListaFuncionariosAdapter(List<Usuario> funcionarios, Context context) {
        this.funcionarios = funcionarios;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter_funcionarios, parent, false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Usuario funcionario = funcionarios.get(position);
        holder.nome.setText(funcionario.getNome());
        holder.email.setText(funcionario.getEmail());
        holder.cargo.setText(funcionario.getTipo());

    }

    @Override
    public int getItemCount() {
        return funcionarios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nome, email, cargo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textViewAdapterFuncNome);
            email = itemView.findViewById(R.id.textViewAdapterFuncEmail);
            cargo = itemView.findViewById(R.id.textViewAdapterFuncCargo);


        }
    }




}
