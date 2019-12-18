package com.example.timetodo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.timetodo.model.HistoricoAtividadesTarefas;
import com.example.timetodo.model.Projeto;
import com.example.timetodo.model.Tarefa;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProjetosActivity extends AppCompatActivity {

    String idEmpresario;
    Projeto projeto = new Projeto();
    TextView textViewDescricao,textViewStatus,textViewListaTarefas, textViewListaFunc ;
    RecyclerView recyclerTarefas, recyclerFunc;
    Button botaoDTI, botaoDTF;
    final DatabaseReference dbRef = ConfiguracaoFirebase.getFirebaseDatabase();
    String usuarioAtual = UsuarioFirebase.getIdentificadorUsuario();
    List<Tarefa> listaTarefas = new ArrayList<>();
    List<String> listaTarefasKey = new ArrayList<>();
    ListaTarefasAdapter adapterTarefas;
    String keyProjeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projetos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        textViewDescricao = findViewById(R.id.textViewDescricao);
        textViewStatus = findViewById(R.id.textViewStatus);
        textViewListaTarefas = findViewById(R.id.textViewListaTarefas);
        textViewListaFunc = findViewById(R.id.textViewListaFunc);
        recyclerTarefas = findViewById(R.id.recyclerTarefas);
        recyclerFunc = findViewById(R.id.recyclerFunc);
        botaoDTI = findViewById(R.id.buttonDTI);
        botaoDTF = findViewById(R.id.buttonDTF);
        recuperarTarefas();
        //recuperarFucionarios();





        Log.d("ProjetosActivity", "ProjetosActivity: "+getIntent().getExtras().getString("idEmpresario"));
        Log.d("ProjetosActivity", "ProjetosActivityProj: "+getIntent().getSerializableExtra("projeto"));
        if((getIntent().getExtras().containsKey("projeto")) && (getIntent().getExtras().containsKey("idEmpresario")) && (getIntent().getExtras().containsKey("projetoKey"))){
            // Bundle extras = getIntent().getExtras();
            projeto = (Projeto) getIntent().getSerializableExtra("projeto");
            idEmpresario = getIntent().getExtras().getString("idEmpresario");
            keyProjeto = getIntent().getExtras().getString("projetoKey");
            ab.setTitle(projeto.getTitulo());
            Log.d("empresaAcvitity", "empresaAcvitity "+projeto.toString()+"    "+idEmpresario);
            textViewDescricao.setText(projeto.getDescricao());
            textViewStatus.setText(projeto.getStatus());
            botaoDTI.setText(projeto.getDataInicio());
            if(projeto.getDataFim() == null){
                botaoDTF.setText("Clique para definir");
            }else
                botaoDTF.setText("Data Inicio: "+projeto.getDataFim());


        }



        adapterTarefas = new ListaTarefasAdapter(listaTarefas, getApplicationContext());
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        recyclerTarefas.setLayoutManager(layoutManager2);
        recyclerTarefas.setHasFixedSize(true);
        recyclerTarefas.setAdapter(adapterTarefas);
        recyclerTarefas.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerTarefas, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(ProjetosActivity.this,TarefaActivity.class);
                i.putExtra("tarefa", listaTarefas.get(position) );
                i.putExtra("projeto", projeto);
                i.putExtra("keyTarefa", listaTarefasKey.get(position));
                i.putExtra("empresario", true);
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));




        FloatingActionButton fab = findViewById(R.id.fabTarefas);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inserirNovaTarefa();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void inserirNovaTarefa() {
        final Tarefa tarefa = new Tarefa();
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompt_add_tarefa, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText InputTitulo = (EditText) promptsView.findViewById(R.id.textViewAdaTTitulo2);
        final EditText InputDescricao = (EditText) promptsView.findViewById(R.id.textViewAdaTDesc2);
        final EditText InputUsuario = (EditText) promptsView.findViewById(R.id.textViewAdaTUsuario2);




        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                tarefa.setId(keyProjeto);
                                tarefa.setStatus("afazer");
                                tarefa.setProjeto(projeto.getTitulo());
                                tarefa.setDataCriacao();
                                tarefa.setTitulo(InputTitulo.getText().toString());
                                tarefa.setDescricao(InputDescricao.getText().toString());
                                tarefa.setFuncionarioResponsavel(InputUsuario.getText().toString());
                                tarefa.setTempoTotalTrabalho((long) 0);
                                tarefa.salvar();


                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void recuperarTarefas() {
        final String projetoAtual = projeto.getTitulo();
        final DatabaseReference tarefas = dbRef.child("tarefas");

        Log.d("recuperarTarefas", "recuperarTarefas: recuperarTarefas"+tarefas.toString());
        tarefas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             //  Log.d("recuperarTarefas", "recuperarTarefas: dataSnapshot"+dataSnapshot.getValue().toString());


                if(dataSnapshot.child(keyProjeto).getChildrenCount() > 0) {

                    textViewListaTarefas.setText("Todos os Tarefas");
                    recyclerTarefas.setVisibility(View.VISIBLE);


                }else {
                    textViewListaTarefas.setText("Nao existem projetos");
                    recyclerTarefas.setVisibility(View.GONE);
                }
                listaTarefas.clear();
                listaTarefasKey.clear();


                for(DataSnapshot t: dataSnapshot.child(keyProjeto).getChildren()) {
                    Tarefa tarefa = new Tarefa();
                    String ultimaAtualizacao = "nao iniciado";
                    Long tmepoTotal = (long) 0;
                    Log.d("recuperarTarefas", "recuperarTarefas: dataSnapshot.child(keyProjeto)"+t.getValue().toString());
                    if(t.hasChild("historico")){
                        for(DataSnapshot h:t.child("historico").getChildren()){
                            Log.d("recuperarTarefas", "recuperarTarefas: dataSnapshot.child(historico)"+h.getValue(HistoricoAtividadesTarefas.class).getUltimaAtualizacao());
                            tmepoTotal = tmepoTotal + (long) h.getValue(HistoricoAtividadesTarefas.class).getTempoDetrabalho();
                            ultimaAtualizacao = h.getValue(HistoricoAtividadesTarefas.class).getUltimaAtualizacao();
                        }
                    }
                    tarefa = t.getValue(Tarefa.class);
                    tarefa.setTempoTotalTrabalho(tmepoTotal);
                    tarefa.setDescricao(tarefa.getDescricao()+", data da ultima atualizacao: "+ultimaAtualizacao);
                    listaTarefas.add(tarefa);
                    listaTarefasKey.add(t.getKey());
                }

                adapterTarefas.notifyDataSetChanged();


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
