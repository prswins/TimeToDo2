package com.example.timetodo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.timetodo.R;
import com.example.timetodo.adapter.ItensMainAdapter;
import com.example.timetodo.config.ConfiguracaoFirebase;
import com.example.timetodo.model.ItemMain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Boolean login;
    Button botaoLogin;
    List<ItemMain>listaitens = new ArrayList<ItemMain>();
    RecyclerView recyclerMain;
    ItensMainAdapter adapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        botaoLogin =findViewById(R.id.buttonMainEntrar);
        carregarDados();


        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        recyclerMain = findViewById(R.id.recyclerMain);


        adapter = new ItensMainAdapter(listaitens, getApplicationContext());
        StaggeredGridLayoutManager layoutManagerGrigEscalonavel =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerMain.setLayoutManager(layoutManagerGrigEscalonavel);
        recyclerMain.setHasFixedSize(true);
        recyclerMain.setAdapter(adapter);
    }

    private void carregarDados() {
        listaitens.clear();
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuarios = firebaseRef.child( "usuarios" );
        DatabaseReference empresas = firebaseRef.child( "empresas" );
        DatabaseReference projetos = firebaseRef.child( "projetos" );
        DatabaseReference tarefas = firebaseRef.child( "tarefas" );

        usuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ItemMain item = new ItemMain();
                if(dataSnapshot.getChildrenCount() > 0){
                    item.setCampo1(String.valueOf(dataSnapshot.getChildrenCount()));
                    item.setCampo2("Cliente registrados na plataforma");
                    item.setCampo3("Dentre esses estao administradores, funcionarios e etc");
                    listaitens.add(item);
                    adapter.notifyDataSetChanged();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        empresas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ItemMain item = new ItemMain();
                if(dataSnapshot.getChildrenCount() > 0) {
                    item.setCampo1(String.valueOf(dataSnapshot.getChildrenCount()));
                    item.setCampo2(String.valueOf(dataSnapshot.getChildrenCount()) + " empresas utilizando a plataforma");
                    item.setCampo3("Com todos tipos de finalidades");
                    listaitens.add(item);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        projetos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ItemMain item = new ItemMain();
                if(dataSnapshot.getChildrenCount() > 0) {
                    item.setCampo1(String.valueOf(dataSnapshot.getChildrenCount()));
                    item.setCampo2(String.valueOf(dataSnapshot.getChildrenCount()) + " peojetos estao sendo gerenciados");
                    item.setCampo3("Com todos tipos de finalidades");
                    listaitens.add(item);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        tarefas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ItemMain item = new ItemMain();
                if(dataSnapshot.getChildrenCount() > 0){
                item.setCampo1(String.valueOf(dataSnapshot.getChildrenCount()));
                item.setCampo2(String.valueOf(dataSnapshot.getChildrenCount())+" tarefas ");
                item.setCampo3("em media estao sendo trabalhadas no momento esse total de tarefas");
                item.setImagem(getResources().getDrawable(R.drawable.tarefa));
                listaitens.add(item);}
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
