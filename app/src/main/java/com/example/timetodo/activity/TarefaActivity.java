package com.example.timetodo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TarefaActivity extends AppCompatActivity {
    Projeto projeto;
    Tarefa tarefa;
    String keyTarefa;
    Usuario usuario;
    TextView  textViewTdescricao, textViewTstatus, textViewTdataInicial, textViewTdataFinal, textViewTdataCriacao, textViewTresponsavel, textViewListaHistorico, textViewTotalHorasTrabalho, textViewTotalTrabalho, textViewTrabalhadas, textViewRestante  ;
    Button botaoAtividade, botaoFinalizar;
    RecyclerView recyclerHistorico;
    LinearLayout linearLayoutTarefas, linearLayoutDatas, linearLayoutTempoTrabalho;
    private static boolean isRunning;
    private long SEGUNDOS;
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
        ActionBar ab = getSupportActionBar();
        textViewTdescricao = findViewById(R.id.textViewAdaTDesc3);
        textViewTstatus = findViewById(R.id.textViewAdaTStatus2);
        textViewTdataInicial= findViewById(R.id.textViewAdaTDTI3);
        textViewTdataFinal= findViewById(R.id.textViewAdaTDTF3);
        textViewTotalTrabalho = findViewById(R.id.textViewTotalTrabalho);
        textViewTrabalhadas = findViewById(R.id.textViewTrabalhadas);
        textViewRestante = findViewById(R.id.textViewRestante);


        textViewTresponsavel= findViewById(R.id.textViewAdaTUsuario3);
        textViewListaHistorico = findViewById(R.id.textViewTextoHistorico);
        botaoAtividade = findViewById(R.id.buttonIniciarTrabalho);
        botaoFinalizar = findViewById(R.id.buttonFinalizarTrabalho);
        botaoFinalizar.setVisibility(View.GONE);
        botaoFinalizar.setEnabled(false);
        recyclerHistorico = findViewById(R.id.recyclerHistorico);
        textViewTotalHorasTrabalho = findViewById(R.id.textViewTotalHorasTrabalho);
        linearLayoutTarefas = findViewById(R.id.linearLayoutTarefas);
        linearLayoutDatas = findViewById(R.id.linearLayoutDatas);
        View view = findViewById(R.id.tarefasss);
        SEGUNDOS = 0;
        chronometer = findViewById(R.id.cronometro);

        botaoFinalizar.setVisibility(View.VISIBLE);
        botaoFinalizar.setText("concluir tarefa");
        botaoFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tarefa.concluirTarefa();
                Log.d("tarefa", "onClick:  concluir tarefa");
            }
        });

        if((getIntent().getExtras().containsKey("projeto")) && (getIntent().getExtras().containsKey("tarefa"))) {

            projeto = (Projeto) getIntent().getSerializableExtra("projeto");
            tarefa =(Tarefa) getIntent().getSerializableExtra("tarefa");
            ab.setTitle(tarefa.getTitulo());
            Log.d("tarefaAcvitity", "tarefaAcvitity " + projeto.toString() + "    " + tarefa.toString());
            textViewTdescricao.setText(tarefa.getDescricao()+", data de atribuicao :"+tarefa.getDataCriacao());
            textViewTstatus.setText(tarefa.getStatus());
            if(tarefa.getDataInicio() ==null){
                textViewTdataInicial.setVisibility(View.GONE);
            }
            if (tarefa.getDataFim() == null){
                textViewTdataFinal.setVisibility(View.GONE);
            }
            if ((tarefa.getDataInicio() ==null) && (tarefa.getDataFim() == null) ){
                linearLayoutDatas.setVisibility(View.GONE);

            }

            textViewTdataInicial.setText("Data do inicio da tarefa: "+tarefa.getDataInicio());
            textViewTdataFinal.setText("Data estimada para o fim da tarefa: "+tarefa.getDataFim());
           // textViewTdataCriacao.setText("Data da atribuicao da atividade: "+tarefa.getDataCriacao());
            textViewTresponsavel.setText("Funcionario responsaval: "+tarefa.getFuncionarioResponsavel());
            if (!(tarefa.getStatus()== null)) {
                if (tarefa.getStatus().equals("afazer")) {
                   linearLayoutTarefas.setBackground(getResources().getDrawable(R.drawable.layout_afazer));
                    botaoFinalizar.setEnabled(false);
                    botaoFinalizar.setVisibility(View.GONE);

                } else if (tarefa.getStatus().equals("fazendo")) {
                    linearLayoutTarefas.setBackground(getResources().getDrawable(R.drawable.layout_fazendo));
                    botaoFinalizar.setVisibility(View.VISIBLE);
                    botaoFinalizar.setEnabled(true);


                    SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
                    Date dataAtual = new Date();
                    Date convertedDate = new Date();
                    if (tarefa.getDataFim()!= null){


                    try {
                        convertedDate = formataData.parse(tarefa.getDataFim());
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Date dataFim;
                    dataFim = convertedDate;


                    if (dataAtual.after(dataFim)) {
                        botaoAtividade.setEnabled(false);
                        linearLayoutTarefas.setBackground(getResources().getDrawable(R.drawable.layout_atrasado));

                    } else {
                        linearLayoutTarefas.setBackground(getResources().getDrawable(R.drawable.layout_fazendo));
                        //     ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("colorStatusFazendo")));
                    }
                    }
                } else if (tarefa.getStatus().equals("concluido")) {
                    botaoAtividade.setEnabled(false);
                    botaoAtividade.setVisibility(View.GONE);
                    botaoFinalizar.setEnabled(false);
                    botaoFinalizar.setVisibility(View.GONE);
                    linearLayoutTarefas.setBackground(getResources().getDrawable(R.drawable.layout_concluido));
                    //  ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("colorStatusConcluido")));
                }
            }else{
                linearLayoutTarefas.setBackground(getResources().getDrawable(R.drawable.layout_nenhum));
            }
            linearLayoutTempoTrabalho = findViewById(R.id.linearLayoutTempoTrabalho);
            Long tempoTotal, tempoTrabalhado, restante;
            if (tarefa.getTempoParaTerminar() != null){
                tempoTotal = (8*tarefa.getTempoParaTerminar());

                textViewTotalTrabalho.setText(String.valueOf(tempoTotal));
                if (tarefa.getTempoParaTerminar()!=null){
                    tempoTrabalhado = tarefa.getTempoTotalTrabalho();
                    textViewTrabalhadas.setText(String.valueOf(tempoTrabalhado));
                    restante = tempoTotal-tempoTrabalhado;
                    textViewRestante.setText(String.valueOf(restante));
                }
                else {
                    textViewTrabalhadas.setText("Nao trabalhado");
                    textViewRestante.setText(String.valueOf(tempoTotal));

                }
            }else {
                linearLayoutTempoTrabalho.setVisibility(View.GONE);
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
                botaoFinalizar.setEnabled(true);
                botaoFinalizar.setVisibility(View.VISIBLE);
                botaoFinalizar.setText("Cancelar Tarefa");
                botaoFinalizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // tarefa.cancelarTarefa();
                        Log.d("tarefa", "onClick:  cancelar tarefa");
                    }
                });
            }
        }



        recuperarDetalhesTarefas();
        botaoAtividade.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        botaoAtividade.setText("Iniciar trabalho");
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
                        textViewTotalHorasTrabalho.setText("Total Horas Trabalhadas: "+tempoTotalTrabalhado/3600 +" horas");
                        ultimaAtualizacao = h.getValue(HistoricoAtividadesTarefas.class).getUltimaAtualizacao();
                        textViewListaHistorico.setText("Historico de Atividades na Tarefa :");
                        recyclerHistorico.setVisibility(View.VISIBLE);

                    }
                    tarefa.setTempoTotalTrabalho(tempoTotalTrabalhado);
                    textViewTstatus.setText(textViewTstatus.getText()+", ultima atualizacao: "+ultimaAtualizacao);
                    adapterHistorico.notifyDataSetChanged();
                }else{


                    textViewListaHistorico.setText("Nao existem atividades nessa tarefa !");
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
            botaoAtividade.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary_dark));
            botaoAtividade.setText("parar");
            startCronometro();


        }else{
            isRunning = false;
            botaoAtividade.setBackgroundColor(getResources().getColor(R.color.colorAccent));


            botaoAtividade.setText("iniciar");
            pauseCronometro();
        }


    }
    public void startCronometro(){
        chronometer.setBase(SystemClock.elapsedRealtime() - SEGUNDOS);
        chronometer.start();

    }
    public void  pauseCronometro(){
        SEGUNDOS = SystemClock.elapsedRealtime() - chronometer.getBase();
        chronometer.stop();
       final HistoricoAtividadesTarefas historico = new HistoricoAtividadesTarefas();
        historico.setIdTarefa(tarefa.getId());
        historico.setProjeto(keyTarefa);
        historico.setStatus(tarefa.getStatus());
        historico.setUltimaAtualizacao();
        int tempoTotalSessaoSegundos = (int) SEGUNDOS;
        tempoTotalSessaoSegundos = tempoTotalSessaoSegundos/1000; //segundos
        historico.setTempoDetrabalho(tempoTotalSessaoSegundos);
        Log.d("pauseCronometro", "pauseCronometro: "+historico.getProjeto()+"---"+historico.getIdTarefa());

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompts2, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setView(promptsView);
        final TextView textTitulo = (TextView) promptsView.findViewById(R.id.textView1);
        textTitulo.setText("Comentario");

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);


        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                atualizarDadosHistorico(userInput.getText().toString(), historico, tarefa);



                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void atualizarDadosHistorico(String msg, HistoricoAtividadesTarefas historico, Tarefa tarefa) {

        historico.setComentario(msg);
        historico.salvar();
        tarefa.setTempoTotalTrabalho(tempoTotalTrabalhado);
        tarefa.setKeyTarefa(keyTarefa);
        tarefa.atualizarTempoTrabalhado((tarefa.getTempoTotalTrabalho()+(SEGUNDOS /3600)));//segundos em horas
        tarefa.atualizarStatus();
        SEGUNDOS = 0;


    }


}
