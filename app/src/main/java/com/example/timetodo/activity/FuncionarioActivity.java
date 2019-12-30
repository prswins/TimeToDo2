package com.example.timetodo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetodo.R;
import com.example.timetodo.adapter.ListaTarefasAdapter;
import com.example.timetodo.config.ConfiguracaoFirebase;
import com.example.timetodo.helper.RecyclerItemClickListener;
import com.example.timetodo.helper.UsuarioFirebase;
import com.example.timetodo.model.Empresa;
import com.example.timetodo.model.Funcionario;
import com.example.timetodo.model.Projeto;
import com.example.timetodo.model.Tarefa;
import com.example.timetodo.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FuncionarioActivity extends AppCompatActivity {
    final Funcionario funcionario = new Funcionario();
    final List<Tarefa> listaTarefas = new ArrayList<>();
    final List<String> listaKeyTarefas = new ArrayList<>();
    String idUsuarioAtual = UsuarioFirebase.getIdentificadorUsuario();
    final DatabaseReference dbRef = ConfiguracaoFirebase.getFirebaseDatabase();
    Usuario usuario = new Usuario();
    ListaTarefasAdapter adapterTarefas;
    RecyclerView recyclerViewTarefas;
    Projeto projetoUsuario = new Projeto();
    Empresa empresaUsuario = new Empresa();
    TextView textViewProjeto, textViewEmpresa, textViewTarefas;
    ActionBar ab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funcionario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        textViewEmpresa = findViewById(R.id.textViewEmpresa);
        textViewProjeto = findViewById(R.id.textViewProjeto);
        recuperarDadosUsuario();

            recuperarTarefas();
            recuperarProjeto();
            recuperarEmpresa();


        String nomeUsuario = usuario.getNome();




        FloatingActionButton fab = findViewById(R.id.fabEmpresa);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recyclerViewTarefas = findViewById(R.id.recyclerViewTarefas);
        textViewTarefas =findViewById(R.id.textViewTarefas);
        adapterTarefas = new ListaTarefasAdapter(listaTarefas, getApplicationContext());
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        recyclerViewTarefas.setLayoutManager(layoutManager2);
        recyclerViewTarefas.setHasFixedSize(true);
        recyclerViewTarefas.setAdapter(adapterTarefas);
        recyclerViewTarefas.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerViewTarefas, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(FuncionarioActivity.this,TarefaActivity.class);
                i.putExtra("tarefa", listaTarefas.get(position) );
                i.putExtra("projeto", projetoUsuario);
                i.putExtra("usuario", usuario);
                i.putExtra("keyTarefa", listaKeyTarefas.get(position));
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));


    }

    private void recuperarEmpresa() {



        final DatabaseReference empresas = dbRef.child("empresas");
        empresas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot empresas:dataSnapshot.getChildren()){
                    Log.d("recuperarEmpresa", "onDataChange: dataSnapshot : for :"+empresas.toString());
                    for(DataSnapshot empresa:empresas.getChildren()){
                        Log.d("recuperarEmpresa", "onDataChange: empresas : for :"+empresa.toString());
                        if(usuario.getIdEmpresa() != null) {
                            if (empresa.getKey().equals(usuario.getIdEmpresa())) {

                                Log.d("recuperarEmpresa", "onDataChange: recuperarEmpresa : if" + empresa.getValue(Empresa.class).getNome());
                                empresaUsuario = empresa.getValue(Empresa.class);
                                textViewEmpresa.setText("Funcionario da "+empresaUsuario.getNome()+".");
                            }
                        }else {
                            textViewEmpresa.setText("Caro sr. "+usuario.getNome()+", lamentamos mas voce nao esta matriculado em uma empresa, entre em contato com seu gestor ");
                        }

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void recuperarProjeto() {

            final DatabaseReference projetos = dbRef.child("projetos");
            projetos.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (usuario.getIdEmpresa()!=null){


                    for (DataSnapshot projeto : dataSnapshot.child(usuario.getIdEmpresa()).getChildren()) {


                        if (projeto.getKey().equals(usuario.getIdProjeto())) {

                            projetoUsuario = projeto.getValue(Projeto.class);
                            Log.d("recuperarProjeto", "onDataChange: projeto" + projeto.getValue(Projeto.class).toString());
                            Log.d("recuperarProjeto", "onDataChange: projetoUsuario" + projetoUsuario.toString());

                        }
                        if (projetoUsuario == null) {
                            textViewProjeto.setText("Nao esta a trabalhar em um projeto");
                        } else {
                            textViewProjeto.setText(projetoUsuario.getTitulo());
                        }
                        break;
                    }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    private void recuperarTarefas() {


            final DatabaseReference tarefas = dbRef.child("tarefas");

            Log.d("recuperarTarefas", "recuperarTarefas: recuperarTarefas" + tarefas.toString());
            tarefas.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("recuperarTarefas", "recuperarTarefas: dataSnapshot" + dataSnapshot.getValue().toString());
                    listaTarefas.clear();
                    listaKeyTarefas.clear();
                    if (usuario.getIdProjeto()!=null){



                    for (DataSnapshot tarefa : dataSnapshot.child(usuario.getIdProjeto()).getChildren()) {
                        if (tarefa.getValue(Tarefa.class).getFuncionarioResponsavel().equals(usuario.getEmail())) {
                            Log.i("recuperarTarefas", "onDataChange: for 1__" + tarefa.getValue(Empresa.class).toString());
                            listaTarefas.add(tarefa.getValue(Tarefa.class));
                            listaKeyTarefas.add(tarefa.getKey());
                        }
                    }
                    }
                    if (listaTarefas.size() == 0) {
                        textViewTarefas.setText("Nao existem tarefas");
                        recyclerViewTarefas.setVisibility(View.GONE);
                    } else {
                        textViewTarefas.setText("Lista de tarefas");
                        recyclerViewTarefas.setVisibility(View.VISIBLE);
                    }
                    adapterTarefas.notifyDataSetChanged();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



    private void recuperarDadosUsuario() {
        final DatabaseReference usuarios = ConfiguracaoFirebase.getFirebaseDatabase();
        usuarios.child("usuarios").child(idUsuarioAtual).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("FuncionarioActivity", "onDataChange: dataSnapshot: "+dataSnapshot.getValue(Usuario.class).toString());
                usuario = (Usuario) dataSnapshot.getValue(Usuario.class);
                if (dataSnapshot.getValue(Usuario.class).getNome() == null) {
                    ab.setTitle("Funcionario");
                }else{
                    ab.setTitle(dataSnapshot.getValue(Usuario.class).getNome());
                }
                Log.d("FuncionarioActivity", "onDataChange: usuario: "+usuario.getNome().toString());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
