package com.example.timetodo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.timetodo.R;
import com.example.timetodo.adapter.EmpresasAdapter;
import com.example.timetodo.config.ConfiguracaoFirebase;
import com.example.timetodo.helper.RecyclerItemClickListener;
import com.example.timetodo.helper.UsuarioFirebase;
import com.example.timetodo.model.Empresa;
import com.example.timetodo.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmpresarioActivity extends AppCompatActivity {
    final Usuario u = new Usuario();
    TextView texto1;
    final List<Empresa> listaEmpresas = new ArrayList<>();
    final List<String> listaKeyEmpresas = new ArrayList<>();

     String idEmpresario;
     RecyclerView recyclerEmpresas;
     private EmpresasAdapter adapter;

    final DatabaseReference dbRef = ConfiguracaoFirebase.getFirebaseDatabase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Empresas");
        carregarDadosUsuario();
        carregarListaEmpresas();

        FloatingActionButton fabEmpresa = findViewById(R.id.fabEmpresa);
        fabEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEmpresa();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        recyclerEmpresas = findViewById(R.id.recyclerEmpresas);
        texto1 = findViewById(R.id.textViewRecycler);


        StaggeredGridLayoutManager layoutManagerGrigEscalonavel =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        adapter = new EmpresasAdapter(listaEmpresas, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerEmpresas.setLayoutManager(layoutManagerGrigEscalonavel);
        recyclerEmpresas.setHasFixedSize(true);
        recyclerEmpresas.setAdapter(adapter);
        recyclerEmpresas.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerEmpresas, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Empresa emp = listaEmpresas.get(position);
                Log.d("empresaAct___", "empresaAct_: "+emp.toString());
                Intent i = new Intent(EmpresarioActivity.this, EmpresaActivity.class);
                i.putExtra("idEmpresario", idEmpresario);
                i.putExtra("empresa", emp);
                i.putExtra("keyEmpresa", listaKeyEmpresas.get(position));

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

    private void carregarListaEmpresas() {
        String usuarioAtual = UsuarioFirebase.getIdentificadorUsuario();
        final DatabaseReference empresas = dbRef.child("empresas").child(usuarioAtual);
        Query pesquisaEmpresas = empresas.orderByChild("responsavel");
        pesquisaEmpresas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.getChildrenCount() > 0) {
                        texto1.setVisibility(View.GONE);
                        recyclerEmpresas.setVisibility(View.VISIBLE);

                    }else {
                        texto1.setVisibility(View.VISIBLE);
                        recyclerEmpresas.setVisibility(View.GONE);
                    }
                    listaEmpresas.clear();
                    listaKeyEmpresas.clear();
                   for(DataSnapshot d: dataSnapshot.getChildren()) {
                        Empresa emp =  d.getValue(Empresa.class);
                        Log.d("listaemrpesas", "listaemrpesas"+emp.toString()+" ---"+d.getValue().toString());
                        listaEmpresas.add(emp);
                        listaKeyEmpresas.add(d.getKey());
                   }
                   adapter.notifyDataSetChanged();



                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void addEmpresa() {
        final Empresa emp = new Empresa();
        emp.setResponsavel(u.getNome()+"_"+idEmpresario);
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompt_add_empresa, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText nomeEmpresa = (EditText) promptsView
                .findViewById(R.id.editTextDialogNome);
        final EditText descEmpresa = (EditText) promptsView
                .findViewById(R.id.editTextDialogDescricao);
        final EditText nInscEmpresa = (EditText) promptsView
                .findViewById(R.id.editTextDialogNInsc);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                emp.setNome(nomeEmpresa.getText().toString());
                                emp.setDescricao(descEmpresa.getText().toString());
                                emp.setnInscricao(nInscEmpresa.getText().toString());
                                emp.salvar();


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

    private void addTarefa() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                               // salvarUsuario(userInput.getText().toString(), "funcionario");

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

    private void carregarDadosUsuario() {
        String usuarioAtual = UsuarioFirebase.getIdentificadorUsuario();

        final DatabaseReference usuarios = ConfiguracaoFirebase.getFirebaseDatabase();
       usuarios.child("usuarios").child(usuarioAtual).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("teste","onDataChange"+ dataSnapshot.toString());
                u.setNome(dataSnapshot.getValue(Usuario.class).getNome());

                idEmpresario = dataSnapshot.getKey();


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
