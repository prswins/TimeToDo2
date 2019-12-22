package com.example.timetodo.model;

import android.graphics.drawable.Drawable;

public class ItemMain {
    String campo1, campo2,campo3;
    Drawable imagem;

    public ItemMain() {
    }

    public ItemMain(String campo1, String campo2, String campo3, Drawable imagem) {
        this.campo1 = campo1;
        this.campo2 = campo2;
        this.campo3 = campo3;
        this.imagem = imagem;
    }

    public String getCampo1() {
        return campo1;
    }

    public void setCampo1(String campo1) {
        this.campo1 = campo1;
    }

    public String getCampo2() {
        return campo2;
    }

    public void setCampo2(String campo2) {
        this.campo2 = campo2;
    }

    public String getCampo3() {
        return campo3;
    }

    public void setCampo3(String campo3) {
        this.campo3 = campo3;
    }

    public Drawable getImagem() {
        return imagem;
    }

    public void setImagem(Drawable imagem) {
        this.imagem = imagem;
    }
}
