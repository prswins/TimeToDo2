package com.example.timetodo.model;

import com.example.timetodo.config.ConfiguracaoFirebase;
import com.example.timetodo.helper.UsuarioFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

public class Empresa implements Serializable {
    String nome,descricao, nInscricao;
    String responsavel;
    List<Projeto> projetos;
    List<Usuario> funcionarios;

    public Empresa(String nome, String descricao, String nInscricao, String responsavel, List<Projeto> projetos, List<Usuario> funcionarios) {
        this.nome = nome;
        this.descricao = descricao;
        this.nInscricao = nInscricao;
        this.responsavel = responsavel;
        this.projetos = projetos;
        this.funcionarios = funcionarios;
    }

    public List<Usuario> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<Usuario> funcionarios) {
        this.funcionarios = funcionarios;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getnInscricao() {
        return nInscricao;
    }

    public void setnInscricao(String nInscricao) {
        this.nInscricao = nInscricao;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public List<Projeto> getProjetos() {
        return projetos;
    }

    public void setProjetos(List<Projeto> projetos) {
        this.projetos = projetos;
    }


    public Empresa() {
    }


    public void salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference db = firebaseRef.child( "empresas" ).child(UsuarioFirebase.getIdentificadorUsuario());

        db.push().setValue(this);
    }
}
