package com.example.timetodo.model;

import com.example.timetodo.config.ConfiguracaoFirebase;
import com.example.timetodo.helper.UsuarioFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Usuario implements Serializable {
    public Usuario() {
    }

    public Usuario(String email, String id, String nome, String tipo, String senha, String dataCriacaoConta, String cargo) {
        this.email = email;
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.senha = senha;
        this.dataCriacaoConta = dataCriacaoConta;
        this.cargo = cargo;
    }

    String email,id, nome, tipo, senha,dataCriacaoConta, idEmpresa, idProjeto, cargo;

    public String getDataCriacaoConta() {
        return dataCriacaoConta;
    }

    public void setDataCriacaoConta() {
        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();
        String dataFormatada = formataData.format(data);

        this.dataCriacaoConta = dataFormatada;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(String idProjeto) {
        this.idProjeto = idProjeto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    public void salvar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuarios = firebaseRef.child( "usuarios" );

        usuarios.child(UsuarioFirebase.getIdentificadorUsuario()).setValue(this);

    }
    public void atribuirEmpresa(String idEmpresa, String idUsuario){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuarios = firebaseRef.child( "usuarios" ).child(idUsuario);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("idEmpresa",idEmpresa);
        usuarios.updateChildren(childUpdates);
    }
    public void atribuirProjeto(String idProjeto, String idUsuario){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuarios = firebaseRef.child( "usuarios" ).child(this.getId());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("idProjeto",idProjeto);
        usuarios.updateChildren(childUpdates);
    }
    public void atribuirCargo(String cargo){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuarios = firebaseRef.child( "usuarios" ).child(this.getId());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("cargo",cargo);
        usuarios.updateChildren(childUpdates);
    }



}
