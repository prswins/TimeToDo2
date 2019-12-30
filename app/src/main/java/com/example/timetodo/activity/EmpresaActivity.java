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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetodo.R;
import com.example.timetodo.adapter.ListaFuncionariosAdapter;
import com.example.timetodo.adapter.ListaProjetosAdapter;
import com.example.timetodo.config.ConfiguracaoFirebase;
import com.example.timetodo.helper.RecyclerItemClickListener;
import com.example.timetodo.helper.UsuarioFirebase;
import com.example.timetodo.model.Empresa;
import com.example.timetodo.model.Projeto;
import com.example.timetodo.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EmpresaActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    Empresa empresa;
    String  idEmpresario;
    String keyEmpresa;
    TextView campoNome, campoDescricao, campoListaFunc, campoListaProjetos;
    RecyclerView recyclerFunc, recyclerProjetos;
    List<Usuario> listaFuncionarios = new ArrayList<>();
    List<Projeto> listaProjetos = new ArrayList<>();
    List<String> listaKeyProjetos = new ArrayList<>();
    final DatabaseReference dbRef = ConfiguracaoFirebase.getFirebaseDatabase();
    String usuarioAtual = UsuarioFirebase.getIdentificadorUsuario();
    List<Usuario> listaUsuarios = new ArrayList<>();
    List<Usuario> listaFuncionarioEmpresa = new ArrayList<>();
    private ListaFuncionariosAdapter adapterFunc;
    private ListaProjetosAdapter adapterProj;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        Log.d("empresaAcvitity", "empresaAcvitity: "+getIntent().getExtras().getString("idEmpresario"));
        Log.d("empresaAcvitity", "empresaAcvitityEmp: "+getIntent().getSerializableExtra("empresa"));
        if((getIntent().getExtras().containsKey("empresa")) && (getIntent().getExtras().containsKey("idEmpresario")) && (getIntent().getExtras().containsKey("keyEmpresa")) ){
           // Bundle extras = getIntent().getExtras();
          empresa = (Empresa) getIntent().getSerializableExtra("empresa");
          idEmpresario = getIntent().getExtras().getString("idEmpresario");
          keyEmpresa = getIntent().getExtras().getString("keyEmpresa");
            ab.setTitle(empresa.getNome());
            Log.d("empresaAcvitity", "empresaAcvitity "+empresa.toString()+"    "+idEmpresario+ "   "+keyEmpresa);

        }

        carregarProjetos();



        campoDescricao = findViewById(R.id.textViewDescr);
        campoNome = findViewById(R.id.textViewNome);
        campoListaFunc = findViewById(R.id.textViewListaFunc);
        campoListaProjetos = findViewById(R.id.textViewListaProjetos);

            recyclerFunc = findViewById(R.id.recyclerListaFunc);
            adapterFunc = new ListaFuncionariosAdapter(listaFuncionarioEmpresa, getApplicationContext());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerFunc.setLayoutManager(layoutManager);
            recyclerFunc.setHasFixedSize(true);
            recyclerFunc.setAdapter(adapterFunc);
            recyclerFunc.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerFunc, new RecyclerItemClickListener.OnItemClickListener() {
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


            recyclerProjetos = findViewById(R.id.recyclerProjetos);

            adapterProj = new ListaProjetosAdapter(listaProjetos, getApplicationContext());
            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
            recyclerProjetos.setLayoutManager(layoutManager2);
            recyclerProjetos.setHasFixedSize(true);
            recyclerProjetos.setAdapter(adapterProj);
            recyclerProjetos.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerProjetos, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent i = new Intent(EmpresaActivity.this,ProjetosActivity.class);
                    Projeto p = listaProjetos.get(position);
                    i.putExtra("idEmpresario", idEmpresario);
                    i.putExtra("projeto",  p);
                    i.putExtra("projetoKey", listaKeyProjetos.get(position));
                    i.putExtra("listaFuncionarios", (Serializable) listaFuncionarioEmpresa);
                    startActivity(i);
                }

                @Override
                public void onLongItemClick(View view, int position) {

                }

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            }));

        campoNome.setText(empresa.getNome());
        campoDescricao.setText(empresa.getDescricao());

        FloatingActionButton fabFunc = findViewById(R.id.fabAddFuncionario);
        fabFunc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inserirNovoUsuario();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        FloatingActionButton fabProjeto = findViewById(R.id.fabAddProjeto);
        fabProjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inserirNovaProjeto();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        FloatingActionButton fabExcluir = findViewById(R.id.fabExcluir);
        fabExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



    }

    private void inserirNovaProjeto() {
        final Projeto proj = new Projeto();
        final int cYear;
        final int cMonth;
        final int cDay;


        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompt_add_projeto, null);

        AlertDialog.Builder alertDialogProjetoBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogProjetoBuilder.setView(promptsView);

        final EditText editTextDialogNomeProj =  (EditText) promptsView
                .findViewById(R.id.editTextDialogNomeProj);
        final EditText editTextDialogDescricaoProj = (EditText)  promptsView
                .findViewById(R.id.editTextDialogDescricaoProj);

        final TextView TextDtIni =  (TextView) promptsView
                .findViewById(R.id.textViewDataIni);
        final TextView TextDtFim =  (TextView) promptsView
                .findViewById(R.id.textViewDataFim);
        TextDtIni.setText("Data de inicio do projeto");
        TextDtFim.setText("data prevista para o fim do projeto");

        final Calendar c = Calendar.getInstance();
        cYear = c.get(Calendar.YEAR);
        cMonth = c.get(Calendar.MONTH);
        cDay = c.get(Calendar.DAY_OF_MONTH);



        final DatePicker datePicker = (DatePicker) promptsView.findViewById(R.id.datePickerDTIni);
        datePicker.init(cYear,cMonth , cDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.d("onDateChanged: ", "onDateChanged: "+view.getId());
             proj.setDataInicio(String.valueOf(dayOfMonth)+"-"+String.valueOf(monthOfYear)+"-"+String.valueOf(year));

            }
        });
        final DatePicker datePickerF = (DatePicker) promptsView.findViewById(R.id.datePickerProjetoDTFim);
        Log.d("onDateChanged: ", "datePickerF: "+promptsView.findViewById(R.id.datePickerProjetoDTFim).getId());
        datePickerF.init(cYear,cMonth , cDay+1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view2, int year, int monthOfYear, int dayOfMonth) {
                Log.d("onDateChanged: ", "onDateChanged: "+view2.getId());

                proj.setDataFim(String.valueOf(dayOfMonth)+"-"+String.valueOf(monthOfYear)+"-"+String.valueOf(year));

            }
        });


        // set dialog message
        alertDialogProjetoBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                proj.setId(keyEmpresa);
                                proj.setDescricao(editTextDialogDescricaoProj.getText().toString());
                                proj.setTitulo(String.valueOf(editTextDialogNomeProj.getText()));

                                Log.d("empresaActivity", "inserirNovaProjeto: "+editTextDialogDescricaoProj.getText().toString()+ "      "+ proj.getTitulo());
                                proj.setEmpresa(empresa.getNome());
                                salvarProjeto(proj);

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialogAddProj = alertDialogProjetoBuilder.create();

        // show it
        alertDialogAddProj.show();


    }

    private void salvarProjeto(Projeto proj) {
        Log.d("empresaActivity", "salvarProjeto: "+proj.getTitulo());
        proj.salvar();
    }


    private void adicionarFuncionario(Usuario usuario) {
        empresa.setFuncionarios(listaFuncionarios);
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference db = firebaseRef.child( "empresas" ).child(UsuarioFirebase.getIdentificadorUsuario());

        db.setValue(this);

    }

    private void inserirNovoUsuario() {
        // startActivity(new Intent(this, CadastroUsuarioActivity.class));
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompts_add_funcionario, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        final EditText userImputCargo = (EditText) promptsView.findViewById(R.id.editTextDialogUserInputCargo);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                salvarUsuario(userInput.getText().toString(),userImputCargo.getText().toString());

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

    private void salvarUsuario(String email, String cargo) {
        Usuario usuario = new Usuario();

        for(Usuario u:listaUsuarios){
            if(u.getEmail().equals(email)){
                usuario = u;
                Log.d("empresaAcivi", "salvarUsuario: "+usuario.getId());
                break;
            }
        }
        if (usuario.getId() == null){

        }else{
            usuario.atribuirEmpresa(keyEmpresa, usuario.getId());
            usuario.atribuirCargo(cargo);
        }

    }

    private void carregarTdsUsuarios() {
        final DatabaseReference qUsuarios = dbRef.child("usuarios");
        Log.d("carregarUsuarios", "carregarUsuarios: "+qUsuarios.toString());
        qUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listaUsuarios.clear();
                Log.d("carregarUsuarios","carregarUsuarios "+ dataSnapshot.toString());
                int i =0;
                for (DataSnapshot d :dataSnapshot.getChildren()){

                    Log.d("carregarUsuarios","carregarUsuarios "+ d.getValue(Usuario.class).getEmail());

                    if(d.getValue(Usuario.class).getTipo().equals("funcionario"))
                    {
                        listaUsuarios.add((Usuario)d.getValue(Usuario.class));
                        Log.d("carregarUsuarios","carregarUsuarios : lista "+ listaUsuarios.get(i).getEmail());
                        i++;

                    }

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    carregarFuncionarios();
    }

    private void carregarFuncionarios() {

        Query qUsuarios = dbRef.child("usuarios").orderByChild("idEmpresa").equalTo(keyEmpresa);
        Log.d("carregarUsuarios", "carregarUsuarios: "+qUsuarios.toString());
        qUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.getChildrenCount() > 0) {
                    campoListaFunc.setText("Todos funcionarios ");
                    recyclerFunc.setVisibility(View.VISIBLE);


                }else {
                    campoListaFunc.setText("Nao existem Funcionarios");
                    recyclerFunc.setVisibility(View.GONE);
                }
                listaFuncionarioEmpresa.clear();
                Log.d("carregarUsuarios","carregarUsuarios "+ dataSnapshot.toString());
                int i =0;
                for (DataSnapshot d :dataSnapshot.getChildren()){

                    Log.d("carregarUsuarios","carregarUsuarios "+ d.getValue(Usuario.class).getEmail());

                    if(d.getValue(Usuario.class).getTipo().equals("funcionario"))
                    {
                        listaFuncionarioEmpresa.add((Usuario)d.getValue(Usuario.class));
                        Log.d("carregarUsuarios","carregarUsuarios : lista "+ listaUsuarios.get(i).getEmail());
                        i++;

                    }

                }
                adapterFunc.notifyDataSetChanged();


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void carregarProjetos() {
        final DatabaseReference projetos = dbRef.child("projetos");

        Log.d("carregarProjetos", "carregarProjetos: q projetos"+projetos.toString());
        projetos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // Log.d("carregarProjetos", "carregarProjetos: dataSnapshot"+dataSnapshot.getValue().toString());


                if(dataSnapshot.child(keyEmpresa).getChildrenCount() > 0) {
                    campoListaProjetos.setText("Todos os Projetos");
                    recyclerProjetos.setVisibility(View.VISIBLE);


                }else {
                    campoListaProjetos.setText("Nao existem projetos");
                    //recyclerProjetos.setVisibility(View.GONE);
                }
                listaProjetos.clear();
                listaKeyProjetos.clear();
                int i = 0;
                List<Projeto> listaP = new ArrayList<>();
                for(DataSnapshot d: dataSnapshot.child(keyEmpresa).getChildren()) {
                    Projeto proj =  d.getValue(Projeto.class);
                    listaP.add(proj);
                    Log.d("carregarProjetos", "onDataChange: datasnapshot: "+d.getValue().toString());
                    Log.d("carregarProjetos", "onDataChange: listaProjetos "+listaP.get(i).getTitulo());
                    i++;
                    listaKeyProjetos.add(d.getKey());
                }
                listaProjetos.addAll(listaP);

                adapterProj.notifyDataSetChanged();


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        carregarTdsUsuarios();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

       // TextView textView = (TextView) findViewById(R.id.textView);
      //  textView.setText(currentDateString);

        Log.d("dataPicker", "dataPicker "+currentDateString);
    }
}
