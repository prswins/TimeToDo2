package com.example.timetodo.model;

import com.example.timetodo.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

public class Projeto implements Serializable {
    String status, titulo, descricao,empresa, dataInicio, dataFim, id;
    List<Funcionario> listaFuncionarios;
    List<Tarefa> listaTarefas;

    public Projeto(String status, String titulo, String descricao, String empresa,String id, String dataInicio, String dataFim, List<Funcionario> listaFuncionarios, List<Tarefa> listaTarefas) {
        this.status = status;
        this.titulo = titulo;
        this.descricao = descricao;
        this.empresa = empresa;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.listaFuncionarios = listaFuncionarios;
        this.listaTarefas = listaTarefas;
    }

    public Projeto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public List<Funcionario> getListaFuncionarios() {
        return listaFuncionarios;
    }

    public void setListaFuncionarios(List<Funcionario> listaFuncionarios) {
        this.listaFuncionarios = listaFuncionarios;
    }

    public List<Tarefa> getListaTarefas() {
        return listaTarefas;
    }

    public void setListaTarefas(List<Tarefa> listaTarefas) {
        this.listaTarefas = listaTarefas;
    }
    public void salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference projetos = firebaseRef.child( "projetos" );

        projetos.child(getId()).push().setValue(this);
    }
    public void excluir(String key){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference projetos = firebaseRef.child( "projetos" ).child(this.getId());
        projetos.child(key).removeValue();

    }

}
