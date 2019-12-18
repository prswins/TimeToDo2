package com.example.timetodo.activity;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroUsuarioActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    TextView campoNome, campoEmail, campoSenha, campoTipo;
    Button botaoSalvar;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fabEmpresa);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        if (usuario == null ){
            usuario = new Usuario();
            usuario.setEmail((String) getIntent().getSerializableExtra("usuarioEmail"));
            usuario.setSenha((String) getIntent().getSerializableExtra("usuarioSenha"));
            usuario.setTipo((String) getIntent().getSerializableExtra("usuarioTipo"));

            Log.d("intent", "onCreate: "+usuario.getEmail()+ "  "+ usuario.getTipo()+ " "+usuario.getSenha());
        }
        campoEmail = findViewById(R.id.editTextEmail);
        campoNome = findViewById(R.id.editTextNome);
        campoSenha = findViewById(R.id.editTextSenha);
        campoTipo = findViewById(R.id.editTextTipo);
        botaoSalvar = findViewById(R.id.buttonSalvar);
        campoEmail.setText(usuario.getEmail());
        campoTipo.setText(usuario.getTipo());
        campoSenha.setText(usuario.getSenha());

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCadastroUsuario(v);
            }
        });



    }


    public void validarCadastroUsuario(View view){

        //Recuperar textos dos campos
        String textoNome  = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        if( !textoNome.isEmpty() ) {//verifica nome
            if( !textoEmail.isEmpty() ) {//verifica e-mail
                if( !textoSenha.isEmpty() ) {//verifica senha

                    usuario.setNome( textoNome );
                    usuario.setSenha( textoSenha );


                    cadastrarUsuario( usuario );

                }else {
                    Toast.makeText(CadastroUsuarioActivity.this,
                            "Preencha a senha!",
                            Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(CadastroUsuarioActivity.this,
                        "Preencha o email!",
                        Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(CadastroUsuarioActivity.this,
                    "Preencha o nome!",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void cadastrarUsuario(final Usuario usuario ){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if ( task.isSuccessful() ){


                    try{

                        String idUsuario = task.getResult().getUser().getUid();
                        usuario.setId( idUsuario );
                        usuario.setDataCriacaoConta();
                        usuario.salvar();

                        //Atualizar nome no UserProfile
                        UsuarioFirebase.atualizarNomeUsuario( usuario.getNome() );
                        UsuarioFirebase.redirecionaUsuarioLogado(CadastroUsuarioActivity.this);

                        // Redireciona o usuário com base no seu campoTipo
                        // Se o usuário for passageiro chama a activity maps
                        // senão chama a activity requisicoes

                        Toast.makeText(CadastroUsuarioActivity.this,
                                "Sucesso ao cadastrar !",
                                Toast.LENGTH_SHORT).show();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {

                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch ( FirebaseAuthInvalidCredentialsException e){
                        excecao= "Por favor, digite um e-mail válido";
                    }catch ( FirebaseAuthUserCollisionException e){
                        excecao = "Este conta já foi cadastrada";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: "  + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuarioActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();

                }

            }
        });

    }


}
