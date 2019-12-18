package com.example.timetodo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.timetodo.R;
import com.example.timetodo.config.ConfiguracaoFirebase;
import com.example.timetodo.helper.UsuarioFirebase;
import com.example.timetodo.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    TextView textViewNome, textViewSenha;
    String emailUsuario, senhaUsuario;
    Button buttonEntrar;
    private FirebaseAuth autenticacao;
    final List<Usuario> listaUsuarios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        textViewNome = findViewById(R.id.textViewUsuario);
        textViewSenha = findViewById(R.id.textViewSenha);


        buttonEntrar = findViewById(R.id.buttonEntrar);
        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarLoginUsuario(v);

            }
        });

        final DatabaseReference usuarios = ConfiguracaoFirebase.getFirebaseDatabase();
        Query qUsuarios = usuarios.child("permitidos");
        qUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Query query = usuarios.child("usuarios").child(dataSnapshot.getKey());
                Log.d("teste", dataSnapshot.toString());
                int i = 0;
                for (DataSnapshot d :dataSnapshot.getChildren()){
                    Log.d("teste", d.getValue(Usuario.class).getEmail());
                    Usuario u = new Usuario();

                    u.setEmail( d.getValue(Usuario.class).getEmail());
                    u.setTipo( d.getValue(Usuario.class).getTipo());
                    listaUsuarios.add(u);

                    Log.d("teste",listaUsuarios.get(i).getEmail() + "  " +listaUsuarios.get(i).getTipo());
                    i++;
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (listaUsuarios.size() < 1){
            Log.i("lista", "vazio"+listaUsuarios.size());
        }else {
            for(Usuario u : listaUsuarios){
                Log.i("lista", "onCreate: "+u.getEmail());
            }

        }




    }

    public void validarLoginUsuario(View view){


        //Recuperar textos dos campos
        emailUsuario = textViewNome.getText().toString();
        senhaUsuario  = textViewSenha.getText().toString();
        if( !emailUsuario.isEmpty() ) {//verifica e-mail
            if( !emailUsuario.isEmpty() ) {//verifica senha
                Usuario usuario = new Usuario();
                usuario.setEmail(emailUsuario );
                usuario.setSenha( senhaUsuario );

                logarUsuario( usuario );

            }else{
                Toast.makeText(LoginActivity.this,
                        "Preencha a senha!",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(LoginActivity.this,
                    "Preencha o email!",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void logarUsuario(final Usuario usuario ){
        for(Usuario u : listaUsuarios){
            if (u.getEmail().equals(usuario.getEmail())){
                Log.d("entrou", "entrou if"+ u.getEmail()+ "   "+usuario.getEmail());
                usuario.setTipo(u.getTipo());


                autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
                autenticacao.signInWithEmailAndPassword(
                        usuario.getEmail(), usuario.getSenha()
                ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if( task.isSuccessful() ){

                            //Verificar o campoTipo de usuário logado
                            // "Motorista" / "Passageiro"
                            UsuarioFirebase.redirecionaUsuarioLogado(LoginActivity.this);

                        }else {

                            String excecao = "";
                            try {
                                throw task.getException();
                            }catch ( FirebaseAuthInvalidUserException e ) {
                                    Intent i = new Intent(getApplicationContext(), CadastroUsuarioActivity.class);
                                   // Log.d("autenticacao", usuario.getTipo());
                                    i.putExtra("usuarioEmail",  usuario.getEmail());
                                    i.putExtra("usuarioSenha",  usuario.getSenha());
                                    i.putExtra("usuarioTipo",  usuario.getTipo());
                                    startActivity(i);

                                excecao = "Usuário não está cadastrado.";
                            }catch ( FirebaseAuthInvalidCredentialsException e ){
                                excecao = "E-mail e senha não correspondem a um usuário cadastrado";
                            }catch (Exception e){
                                excecao = "Erro ao cadastrar usuário: "  + e.getMessage();
                                e.printStackTrace();
                            }
                            Toast.makeText(LoginActivity.this,
                                    excecao,
                                    Toast.LENGTH_SHORT).show();

                        }
                    }

                });

            }




        }
        }






}
