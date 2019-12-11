package com.example.timetodo.model;

import java.sql.Time;
import java.util.List;

public class Projeto {
    String status, titulo, descricao;
    Time tempoTotalEstimado;
    Administrador admProjeto;


    List<Funcionario> listaFuncionarios;
    List<Tarefa> listaTarefas;
}
