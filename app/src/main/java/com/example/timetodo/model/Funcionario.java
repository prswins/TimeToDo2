package com.example.timetodo.model;

import java.util.List;

public class Funcionario extends Usuario {
    public Funcionario(String status, String cargo, String situacao, List<Projeto> listaProjetos, List<Tarefa> listaTarefas) {
        this.status = status;
        this.cargo = cargo;
        this.situacao = situacao;
        this.listaProjetos = listaProjetos;
        this.listaTarefas = listaTarefas;
    }

    public Funcionario() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public List<Projeto> getListaProjetos() {
        return listaProjetos;
    }

    public void setListaProjetos(List<Projeto> listaProjetos) {
        this.listaProjetos = listaProjetos;
    }

    public List<Tarefa> getListaTarefas() {
        return listaTarefas;
    }

    public void setListaTarefas(List<Tarefa> listaTarefas) {
        this.listaTarefas = listaTarefas;
    }

    String status,cargo, situacao;
   List<Projeto> listaProjetos;
   List<Tarefa> listaTarefas;

}
