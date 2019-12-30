package com.example.timetodo.model;

import android.util.Log;

import com.example.timetodo.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Tarefa implements Serializable {
String id, dataInicio, dataFim, titulo, descricao, status, projeto,empresa, dataCriacao, funcionarioResponsavel, keyTarefa;
Long tempoTotalTrabalho, tempoParaTerminar;

    public Tarefa(String id, String dataInicio, String dataFim, String titulo, String descricao, String status, String projeto, String empresa, String funcionarioResponsavel, String dataCriacao, long tempoTotalTrabalho, String keyTarefa, long tempoParaTermicar) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.projeto = projeto;
        this.empresa = empresa;
        this.funcionarioResponsavel = funcionarioResponsavel;
        this.dataCriacao = dataCriacao;
        this.tempoTotalTrabalho = tempoTotalTrabalho;
        this.keyTarefa = keyTarefa;
        this.tempoParaTerminar = tempoParaTermicar;

    }

    public String getKeyTarefa() {
        return keyTarefa;
    }

    public void setKeyTarefa(String keyTarefa) {
        this.keyTarefa = keyTarefa;
    }

    public Long getTempoTotalTrabalho() {
        return tempoTotalTrabalho;
    }

    public void setTempoTotalTrabalho(Long tempoTotalTrabalho) {
        this.tempoTotalTrabalho = tempoTotalTrabalho;
    }

    public String getProjeto() {
        return projeto;
    }

    public void setProjeto(String projeto) {
        this.projeto = projeto;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }


    public Tarefa() {
    }

    public Long getTempoParaTerminar() {
        return tempoParaTerminar;
    }

    public void setTempoParaTerminar(Long tempoParaTerminar) {


        this.tempoParaTerminar = tempoParaTerminar;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao() {
        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();
        String dataFormatada = formataData.format(data);
        this.dataCriacao = dataFormatada;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFuncionarioResponsavel() {
        return funcionarioResponsavel;
    }

    public void setFuncionarioResponsavel(String funcionarioResponsavel) {
        this.funcionarioResponsavel = funcionarioResponsavel;
    }



    public void salvar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference db = firebaseRef.child("tarefas").child(getId());


        db.push().setValue(this);
    }


    public void atualizarDataInicio() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference db = firebaseRef.child("tarefas").child(getId()).child(getKeyTarefa());

       // db.child(getId()).child(getProjeto()).setValue(this);
    }
    public void atualizarDataFim(){

    }
    public void atualizarTempoTrabalhado(Long tempo){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference db = firebaseRef.child("tarefas").child(getId()).child(getKeyTarefa());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("tempoTotalTrabalho",tempo);
        db.updateChildren(childUpdates);

    }
    public void atualizarStatus(String key){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference db = firebaseRef.child("tarefas").child(getId()).child(key);

        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        Date dataAtual = new Date();
        Date convertedDateIni = new Date();
        Date convertedDateFim = new Date();
        String statusAtual = "afazer";

        if (this.getDataFim() != null ) {
            try {
                convertedDateFim = formataData.parse(this.getDataFim());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

            if (this.getDataInicio() != null ) {
                try {
                    convertedDateIni = formataData.parse(this.getDataInicio());
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            int diferencaAtualInicial = convertedDateIni.compareTo(dataAtual);
        Log.d("tarefa status", "atualizarStatus: "+diferencaAtualInicial );
            if(diferencaAtualInicial > 0){
                Log.d("tarefa status", "atualizarStatus a fazer: "+diferencaAtualInicial );

                statusAtual = "afazer";
            }else if (diferencaAtualInicial <= 0 && getTempoTotalTrabalho() > 0){
                Log.d("tarefa status", "atualizarStatus fazendo: "+diferencaAtualInicial );

                statusAtual = "fazendo";
            }else {
                int diferencaAtualFinal = convertedDateFim.compareTo(dataAtual);
                if (diferencaAtualFinal < 0){

                    Log.d("tarefa status", "atualizarStatus atrasado: "+diferencaAtualInicial );
                    statusAtual = "atrasado";

                }
            }

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("status",statusAtual);
        db.updateChildren(childUpdates);

    }

    public void concluirTarefa(String key){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference db = firebaseRef.child("tarefas").child(this.getId()).child(key);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("status","concluida");
        db.updateChildren(childUpdates);

    }

    public void cancelarTarefa(String key){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference db = firebaseRef.child("tarefas").child(getId()).child(key);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("status","cancelada");
        db.updateChildren(childUpdates);

    }




}
