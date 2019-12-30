package com.example.timetodo.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetodo.R;
import com.example.timetodo.adapter.ListaTarefasAdapter;
import com.example.timetodo.config.ConfiguracaoFirebase;
import com.example.timetodo.helper.DatePickerFragment;
import com.example.timetodo.helper.RecyclerItemClickListener;
import com.example.timetodo.helper.UsuarioFirebase;
import com.example.timetodo.model.HistoricoAtividadesTarefas;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProjetosActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    String idEmpresario;
    Projeto projeto = new Projeto();
    TextView textViewDescricao,textViewStatus,textViewListaTarefas, textViewListaFunc, textViewDescr, textViewDataFim ;
    RecyclerView recyclerTarefas, recyclerFunc;
    Button botaoDTI, botaoDTF;
    final DatabaseReference dbRef = ConfiguracaoFirebase.getFirebaseDatabase();
    String usuarioAtual = UsuarioFirebase.getIdentificadorUsuario();
    List<Tarefa> listaTarefas = new ArrayList<>();
    List<String> listaTarefasKey = new ArrayList<>();
    ListaTarefasAdapter adapterTarefas;
    String keyProjeto;
    String currentDateString;
    List<Usuario> listaFuncionarios = new ArrayList<>();

    final Calendar c = Calendar.getInstance();
    final int cYear = c.get(Calendar.YEAR);
    final int cMonth = c.get(Calendar.MONTH);
    final int cDay = c.get(Calendar.DAY_OF_MONTH);
    final String dataAtual = String.valueOf(cDay)+"-"+String.valueOf(cMonth)+"-"+String.valueOf(cYear);

    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
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
            ArrayList<Usuario> dados = (ArrayList<Usuario>) getIntent().getSerializableExtra("listaFuncionarios");
            listaFuncionarios.addAll(dados);
            //listaFuncionarios.addAll = getIntent().getSerializableExtra("listaFuncionarios");

            for(Usuario u : listaFuncionarios){
                Log.d("listaFuncionarios", "onCreate: "+u.getEmail());
            }




            atualizarDatas();

        }
        botaoDTI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog(v);
                atualizarDI();
                Log.d("datapickerrrrrr", "botao: "+v.getId());



            }
        });
        botaoDTF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
                atualizarDF();
                projeto.setDataFim(currentDateString);
                projeto.atualizarDataFim(keyProjeto);
                atualizarDatas();






            }

        });



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


    private void atualizarDF() {
        projeto.setDataFim(currentDateString);
        projeto.atualizarDataFim(keyProjeto);
        atualizarDatas();
    }

    private void atualizarDI() {
        projeto.setDataInicio(currentDateString);
        projeto.atualizarDataInicio(keyProjeto);
        atualizarDatas();
    }

    private void atualizarDatas() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(projeto.getDataInicio() == null){
                    botaoDTI.setText("Definir data inicial.");
                }else if(projeto.getDataInicio().equals(dataAtual) ){
                    botaoDTI.setEnabled(false);
                    botaoDTI.setText("Data inicial: "+projeto.getDataInicio()+".");
                }else{
                    botaoDTI.setText("Data inicial: "+projeto.getDataInicio()+", ainda pode ser alterada");
                }

                if(projeto.getDataFim() == null){
                    botaoDTF.setText("Clique para definir a data limite do projeto");
                }else{
                    botaoDTF.setText("Finalizacao prevista para: "+projeto.getDataFim()+", clique aqui para alterar");
                }

            }
        });


    }

    private void atualizarProjeto() {
    }

    private void showDatePickerDialog(View v) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
        Log.d("datapickerrrrrr", "onDateSet: "+v.getId());


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


        final TextView TextDtIni =  (TextView) promptsView
                .findViewById(R.id.textViewDataIni);
        final TextView TextDtFim =  (TextView) promptsView
                .findViewById(R.id.textViewDataFim);
        TextDtIni.setText("Data de inicio da tarefa");
        TextDtFim.setText("data prevista para o fim do tarefa");

        final int cYear;
        final int cMonth;
        final int cDay;
        final Calendar c = Calendar.getInstance();
        cYear = c.get(Calendar.YEAR);
        cMonth = c.get(Calendar.MONTH);
        cDay = c.get(Calendar.DAY_OF_MONTH);



        final DatePicker datePicker = (DatePicker) promptsView.findViewById(R.id.datePickerDTIni);
        datePicker.init(cYear,cMonth , cDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.d("onDateChanged: ", "onDateChanged: "+view.getId());
                tarefa.setDataInicio(String.valueOf(dayOfMonth)+"-"+String.valueOf(monthOfYear)+"-"+String.valueOf(year));

            }
        });
        final DatePicker datePickerF = (DatePicker) promptsView.findViewById(R.id.datePickerProjetoDTFim);
        Log.d("onDateChanged: ", "datePickerF: "+promptsView.findViewById(R.id.datePickerProjetoDTFim).getId());
        datePickerF.init(cYear,cMonth , cDay+1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view2, int year, int monthOfYear, int dayOfMonth) {
                Log.d("onDateChanged: ", "onDateChanged: "+view2.getId());

                tarefa.setDataFim(String.valueOf(dayOfMonth)+"-"+String.valueOf(monthOfYear)+"-"+String.valueOf(year));

            }
        });





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
                                tarefa.setTempoParaTerminar(calcTempoTerminar(tarefa.getDataInicio(),tarefa.getDataFim()));
                                Usuario usuario = new Usuario();
                                for(Usuario ut:listaFuncionarios){
                                    if(ut.getEmail().equals(InputUsuario.getText().toString())){
                                        usuario = ut;
                                        break;
                                    }
                                }

                                tarefa.salvar();
                                usuario.atribuirProjeto(keyProjeto, usuario.getId());


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

    private void salvarTarefa() {
    }

    private long calcTempoTerminar(String dataI, String dataF){

        if((dataI!= null) && (dataF != null)){

            Long dias = DiferencaDatas(dataI, dataF);
            Log.d("tarefa", "setTempoParaTerminar: "+dias);
            return (((dias*8)*60)*60);

        }else{
            return Long.valueOf(0);
        }
    }
    public long DiferencaDatas(String dataInicial, String dataFinal){

        Date data1 = new Date(), data2 = new Date();

        Calendar c1 = Calendar.getInstance();

        //Pega a primeira data
        c1.set(Integer.parseInt(dataInicial.substring(6, 9)), Integer.parseInt(dataInicial.substring(3, 5)), Integer.parseInt(dataInicial.substring(0,2)));
        data1.setTime(c1.getTimeInMillis());

        //Pega a segunda data
        c1.set(Integer.parseInt(dataFinal.substring(6, 9)), Integer.parseInt(dataFinal.substring(3, 5)), Integer.parseInt(dataFinal.substring(0,2)));
        data2.setTime(c1.getTimeInMillis());

        return (data2.getTime() - data1.getTime()) /1000/60/60/24;
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

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        currentDateString = String.valueOf(dayOfMonth)+"-"+String.valueOf(monthOfYear)+"-"+String.valueOf(year);
        Log.d("datapicker", "onDateSet: "+view.getId());

    }
}
