package com.example.timetodo.helper;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.timetodo.activity.AdministradorActivity;
import com.example.timetodo.activity.EmpresarioActivity;
import com.example.timetodo.activity.FuncionarioActivity;
import com.example.timetodo.config.ConfiguracaoFirebase;
import com.example.timetodo.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;



public class UsuarioFirebase {

    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }

    public static boolean atualizarNomeUsuario(String nome){

        try {

            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName( nome )
                    .build();
            user.updateProfile( profile ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if( !task.isSuccessful() ){
                        Log.d("Perfil", "Erro ao atualizar nome de perfil.");
                    }
                }
            });

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
    public static void redirecionaUsuarioCriado(final Activity activity ){

        FirebaseUser user = getUsuarioAtual();

        if(user != null ){
            DatabaseReference usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase()
                    .child("permitidos")
                    .child( getIdentificadorUsuario() );
            usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Usuario usuario = dataSnapshot.getValue( Usuario.class );
                    // Log.d("usuario", "onDataChange: "+ usuario.getTipo());
                    // String tipoUsuario = dataSnapshot.getChildren().toString();

                    String tipoUsuario = usuario.getTipo();
                    if( tipoUsuario.equals("admGeral") ){
                        Intent i = new Intent(activity, AdministradorActivity.class);
                        activity.startActivity(i);
                    }else if (tipoUsuario.equals("empresario")) {
                        Intent i = new Intent(activity, EmpresarioActivity.class);
                        activity.startActivity(i);
                    }else {
                        Intent i = new Intent(activity, FuncionarioActivity.class);
                        i.putExtra("usuario", usuario);
                        activity.startActivity(i);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }



    public static void redirecionaUsuarioLogado(final Activity activity ){

        FirebaseUser user = getUsuarioAtual();

       if(user != null ){
            DatabaseReference usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase()
                    .child("usuarios")
                    .child( getIdentificadorUsuario() );
            usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Usuario usuario = dataSnapshot.getValue( Usuario.class );
                    Log.d("usuario", "onDataChange: "+ usuario.getTipo());
                   // String tipoUsuario = dataSnapshot.getChildren().toString();

                    String tipoUsuario = usuario.getTipo();
                    Intent i;
                    if( tipoUsuario.equals("admGeral") ){
                        i = new Intent(activity, AdministradorActivity.class);

                    }else if (tipoUsuario.equals("empresario")) {
                         i = new Intent(activity, EmpresarioActivity.class);

                    }else {
                          i = new Intent(activity, FuncionarioActivity.class);

                    }
                    activity.startActivity(i);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    public static String getIdentificadorUsuario(){
        return getUsuarioAtual().getUid();
    }

}
