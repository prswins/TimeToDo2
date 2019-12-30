package com.example.timetodo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetodo.R;
import com.example.timetodo.adapter.ListaAdapterItemAdm;
import com.example.timetodo.config.ConfiguracaoFirebase;
import com.example.timetodo.model.Empresa;
import com.example.timetodo.model.ItemAdm;
import com.example.timetodo.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdministradorActivity extends AppCompatActivity {


    List<ItemAdm> listaitensEmpresas = new ArrayList<ItemAdm>();
    List<ItemAdm> listaitensEmpresasarios = new ArrayList<ItemAdm>();
    List<ItemAdm> listaitensProjetos = new ArrayList<ItemAdm>();
    List<ItemAdm> listaitensFuncionarios = new ArrayList<ItemAdm>();
    RecyclerView recyclerEmp, recyclerUF, recyclerUE;
    ListaAdapterItemAdm adapterUF, adapterUE, adapterEmp;
    


    @Override
    protected void onDestroy() {







        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fabEmpresa);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inserirNovoUsuario();
                
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                inserirNovoEmpresa();

            }
        });
        carregarDados();
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(getApplicationContext());

        recyclerEmp = findViewById(R.id.recyclerEmp);
        adapterEmp = new ListaAdapterItemAdm(listaitensEmpresas, this);
        recyclerEmp.setLayoutManager(layoutManager2);
        recyclerEmp.setHasFixedSize(true);
        recyclerEmp.setAdapter(adapterEmp);

        recyclerUE = findViewById(R.id.recyclerUE);
        adapterUE = new ListaAdapterItemAdm(listaitensEmpresasarios, this);
        recyclerUE.setLayoutManager(layoutManager3);
        recyclerUE.setHasFixedSize(true);
        recyclerUE.setAdapter(adapterUE);

        recyclerUF = findViewById(R.id.recyclerUF);
        adapterUF = new ListaAdapterItemAdm(listaitensFuncionarios, this);
        recyclerUF.setLayoutManager(layoutManager4);
        recyclerUF.setHasFixedSize(true);
        recyclerUF.setAdapter(adapterUF);


    }


    private void carregarDados() {
        listaitensEmpresas.clear();
        listaitensEmpresasarios.clear();
        listaitensProjetos.clear();
        listaitensFuncionarios.clear();
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuarios = firebaseRef.child( "usuarios" );
        DatabaseReference empresas = firebaseRef.child( "empresas" );
        DatabaseReference projetos = firebaseRef.child( "projetos" );
        DatabaseReference tarefas = firebaseRef.child( "tarefas" );

        usuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot u: dataSnapshot.getChildren()){
                            if (u.getValue(Usuario.class).getTipo().equals("funcionario")){
                                listaitensFuncionarios.add(
                                        new ItemAdm(
                                                "Nome: "+u.getValue(Usuario.class).getNome()+" ",
                                                "Email: "+u.getValue(Usuario.class).getEmail()+" ",
                                                "Data da Conta: "+u.getValue(Usuario.class).getDataCriacaoConta()+" "

                                ));
                            }else if (u.getValue(Usuario.class).getTipo().equals("empresario")){
                                listaitensEmpresasarios.add(
                                        new ItemAdm(
                                                "Nome empresario: "+u.getValue(Usuario.class).getNome()+" ",
                                                "Email: "+u.getValue(Usuario.class).getEmail()+" ",
                                                "Data da Conta: "+u.getValue(Usuario.class).getDataCriacaoConta()+" "
                                                )
                                );
                        }
                            adapterUE.notifyDataSetChanged();
                            adapterUF.notifyDataSetChanged();
                    }


                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        empresas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount() > 0) {

                    for (DataSnapshot e: dataSnapshot.getChildren()){
                        for (DataSnapshot e1:e.getChildren()){
                            if (e1.getChildrenCount() > 0){
                                listaitensEmpresas.add(
                                        new ItemAdm(
                                                "Empresa: "+e1.getValue(Empresa.class).getNome()+" ",
                                                "Descricao: "+e1.getValue(Empresa.class).getDescricao()+" ",
                                                "Numero Inscricao: "+e1.getValue(Empresa.class).getnInscricao()+" "
                                        )
                                );
                            }
                        }
                    }
                    adapterEmp.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        projetos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0) {


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        tarefas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount() > 0){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    private void inserirNovoEmpresa() {
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
                                salvarUsuario(userInput.getText().toString(), "empresario");

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

    private void inserirNovoUsuario() {
       // startActivity(new Intent(this, CadastroUsuarioActivity.class));
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
                                salvarUsuario(userInput.getText().toString(), "funcionario");

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

    private void salvarUsuario(String email, String tipo) {
        Usuario usuario = new Usuario();
        usuario.setTipo(tipo);
        usuario.setEmail(email);
        usuario.setDataCriacaoConta();


       DatabaseReference usuarios = ConfiguracaoFirebase.getFirebaseDatabase();
       usuarios.child("permitidos").push().setValue(usuario);

    }

}
