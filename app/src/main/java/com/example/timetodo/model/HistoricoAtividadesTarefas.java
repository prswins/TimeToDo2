package com.example.timetodo.model;

import com.example.timetodo.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoricoAtividadesTarefas {
    String id, idTarefa, status, ultimaAtualizacao, projeto;
    int tempoDetrabalho;

    public HistoricoAtividadesTarefas() {
    }

    public HistoricoAtividadesTarefas(String id, String idTarefa, String status, String ultimaAtualizacao, String projeto, int tempoDetrabalho) {
        this.id = id;
        this.idTarefa = idTarefa;
        this.status = status;
        this.ultimaAtualizacao = ultimaAtualizacao;
        this.projeto = projeto;
        this.tempoDetrabalho = tempoDetrabalho;
    }

    public String getIdTarefa() {
        return idTarefa;
    }

    public void setIdTarefa(String idTarefa) {
        this.idTarefa = idTarefa;
    }

    public String getProjeto() {
        return projeto;
    }

    public void setProjeto(String projeto) {
        this.projeto = projeto;
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

    public String getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao() {

        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy HH:MM:SS");
        Date data = new Date();
        String dataFormatada = formataData.format(data);

        this.ultimaAtualizacao = dataFormatada;
    }

    public int getTempoDetrabalho() {
        return tempoDetrabalho;
    }

    public void setTempoDetrabalho(int tempoDetrabalho) {
        this.tempoDetrabalho = tempoDetrabalho;
    }

    public void salvar() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference db = firebaseRef.child("tarefas");

        db.child(getIdTarefa()).child(getProjeto()).child("historico").push().setValue(this);


    }
}
