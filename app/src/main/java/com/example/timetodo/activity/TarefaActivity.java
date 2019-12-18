package com.example.timetodo.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetodo.R;
import com.example.timetodo.adapter.ListaHistoricoAdapter;
import com.example.timetodo.config.ConfiguracaoFirebase;
import com.example.timetodo.helper.RecyclerItemClickListener;
import com.example.timetodo.model.HistoricoAtividadesTarefas;
import com.example.timetodo.model.Projeto;
import com.example.timetodo.model.Tarefa;
import com.example.timetodo.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TarefaActivity extends AppCompatActivity {
    Projeto projeto;
    Tarefa tarefa;
    String keyTarefa;
    Usuario usuario;
    TextView  textViewTdescricao, textViewTstatus, textViewTdataInicial, textViewTdataFinal, textViewTdataCriacao, textViewTresponsavel, textViewListaHistorico, textViewTotalHorasTrabalho;
    Button botaoAtividade;
    RecyclerView recyclerHistorico;
    private static boolean isRunning;
    private long MILLIS;
    private static final int SECS_IN_MIN = 60;
    private Chronometer chronometer;
    final DatabaseReference dbRef = ConfiguracaoFirebase.getFirebaseDatabase();
    List<HistoricoAtividadesTarefas> listaHistorico = new ArrayList<>();
    private long tempoTotalTrabalhado = 0;
    ListaHistoricoAdapter adapterHistorico;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        textViewTdescricao = findViewById(R.id.textViewAdaTDesc3);
        textViewTstatus = findViewById(R.id.textViewAdaTStatus2);
        textViewTdataInicial= findViewById(R.id.textViewAdaTDTI3);
        textViewTdataFinal= findViewById(R.id.textViewAdaTDTF3);
        textViewTdataCriacao= findViewById(R.id.textViewAdaTDTC2);
        textViewTresponsavel= findViewById(R.id.textViewAdaTUsuario3);
        textViewListaHistorico = findViewById(R.id.textViewTextoHistorico);
        botaoAtividade = findViewById(R.id.buttonIniciarTrabalho);
        recyclerHistorico = findViewById(R.id.recyclerHistorico);
        textViewTotalHorasTrabalho = findViewById(R.id.textViewTotalHorasTrabalho);
        View view = findViewById(R.id.tarefasss);
        MILLIS = 0;
        chronometer = findViewById(R.id.cronometro);

        if((getIntent().getExtras().containsKey("projeto")) && (getIntent().getExtras().containsKey("tarefa"))) {

            projeto = (Projeto) getIntent().getSerializableExtra("projeto");
            tarefa =(Tarefa) getIntent().getSerializableExtra("tarefa");
            ab.setTitle(tarefa.getTitulo());
            Log.d("tarefaAcvitity", "tarefaAcvitity " + projeto.toString() + "    " + tarefa.toString());
            textViewTdescricao.setText(tarefa.getDescricao());
            textViewTstatus.setText(tarefa.getStatus());
            textViewTdataInicial.setText(tarefa.getDataInicio());
            textViewTdataFinal.setText(tarefa.getDataFim());
            textViewTdataCriacao.setText(tarefa.getDataCriacao());
            textViewTresponsavel.setText(tarefa.getFuncionarioResponsavel());
            if (!(tarefa.getStatus()== null)) {

                if (tarefa.getStatus().equals("afazer")) {
                    view.setBackgroundColor(getResources().getColor(R.color.colorStatusAfazer));
                    //   ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("colorStatusAfazer")));
                } else if (tarefa.getStatus().equals("fazendo")) {
                    SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
                    Date data = new Date();
                    String dataFormatada = formataData.format(data);
                    if (tarefa.getDataFim().equals(dataFormatada)) {
                        botaoAtividade.setEnabled(false);
                        view.setBackgroundColor(getResources().getColor(R.color.colorStatusAtrasado));
                    } else {
                        view.setBackgroundColor(getResources().getColor(R.color.colorStatusFazendo));
                        //     ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("colorStatusFazendo")));
                    }

                } else if (tarefa.getStatus().equals("concluida")) {
                    botaoAtividade.setEnabled(false);
                    botaoAtividade.setVisibility(View.GONE);
                    view.setBackgroundColor(getResources().getColor(R.color.colorStatusConcluida));
                    //  ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("colorStatusConcluido")));
                }
            }

        }
        if((getIntent().getExtras().containsKey("keyTarefa"))){
            keyTarefa = getIntent().getExtras().getString("keyTarefa");
            Log.d("keyTarefa", "onCreate: "+keyTarefa);

        }
        if((getIntent().getExtras().containsKey("projeto"))){
            usuario =  (Usuario) getIntent().getSerializableExtra("usuario");
        }
        if((getIntent().getExtras().containsKey("empresario"))){
            boolean emp = (boolean) getIntent().getExtras().getBoolean("empresario");
            if (emp){
                botaoAtividade.setVisibility(View.GONE);
                chronometer.setVisibility(View.GONE);
            }
        }



        recuperarDetalhesTarefas();
        botaoAtividade.setBackgroundColor(getResources().getColor(R.color.colorStatusConcluida));
        botaoAtividade.setText("Inciar trabalho");
        botaoAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acionarAtividade();
            }
        });


        adapterHistorico = new ListaHistoricoAdapter(listaHistorico, getApplicationContext());
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        recyclerHistorico.setLayoutManager(layoutManager2);
        recyclerHistorico.setHasFixedSize(true);
        recyclerHistorico.setAdapter(adapterHistorico);
        recyclerHistorico.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerHistorico, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
    }

    private void recuperarDetalhesTarefas() {
        final DatabaseReference tarefas = dbRef.child("tarefas").child(tarefa.getId()).child(keyTarefa).child("historico");
        tarefas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tempoTotalTrabalhado = 0;
                if(dataSnapshot.getChildrenCount() > 0){
                    listaHistorico.clear();
                    String ultimaAtualizacao = "nao iniciado";
                    for (DataSnapshot h:dataSnapshot.getChildren()){
                        Log.d("recuperarHistorico", "onDataChange: "+h.getValue().toString());
                        listaHistorico.add(h.getValue(HistoricoAtividadesTarefas.class));
                        tempoTotalTrabalhado = tempoTotalTrabalhado + h.getValue(HistoricoAtividadesTarefas.class).getTempoDetrabalho();
                        textViewTotalHorasTrabalho.setText("Total Horas Trabalhadas "+tempoTotalTrabalhado/3600 +" horas");
                        ultimaAtualizacao = h.getValue(HistoricoAtividadesTarefas.class).getUltimaAtualizacao();
                        textViewListaHistorico.setText("Historico de Atividades na Tarefa :");
                        recyclerHistorico.setVisibility(View.VISIBLE);

                    }
                    tarefa.setTempoTotalTrabalho(tempoTotalTrabalhado);
                    tarefa.setDescricao(tarefa.getDescricao()+", data da ultima atualizacao: "+ultimaAtualizacao);
                    adapterHistorico.notifyDataSetChanged();
                }else{


                    textViewListaHistorico.setText("Nao existem atividades nessa tarefa :");
                    recyclerHistorico.setVisibility(View.GONE);
                    textViewTotalHorasTrabalho.setText("Nao foi trabalhada nessa tarefa");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void acionarAtividade() {
        if(!isRunning) {
            isRunning = true;
            botaoAtividade.setBackgroundColor(getResources().getColor(R.color.colorStatusAtrasado));
            botaoAtividade.setText("parar");
            startCronometro();


        }else{
            isRunning = false;
            botaoAtividade.setBackgroundColor(getResources().getColor(R.color.colorStatusConcluida));

            botaoAtividade.setText("iniciar");
            pauseCronometro();
        }


    }
    public void startCronometro(){
        chronometer.setBase(SystemClock.elapsedRealtime() - MILLIS);
        chronometer.start();

    }
    public void  pauseCronometro(){
        MILLIS = SystemClock.elapsedRealtime() - chronometer.getBase();
        chronometer.stop();
        HistoricoAtividadesTarefas historico = new HistoricoAtividadesTarefas();
        historico.setIdTarefa(tarefa.getId());
        historico.setProjeto(keyTarefa);
        historico.setStatus(tarefa.getStatus());
        historico.setUltimaAtualizacao();
        int tempoTotalSessaoSegundos = (int) MILLIS;
        tempoTotalSessaoSegundos = tempoTotalSessaoSegundos/1000; //segundos
        historico.setTempoDetrabalho(tempoTotalSessaoSegundos);
        Log.d("pauseCronometro", "pauseCronometro: "+historico.getProjeto()+"---"+historico.getIdTarefa());
        historico.salvar();
        tarefa.setTempoTotalTrabalho(tempoTotalTrabalhado);
        //tarefa.salvar();
        MILLIS = 0;
    }


}
