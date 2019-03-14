/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krupique.visualbeans.structures;

/**
 *
 * @author Henrique K. Secchi
 */
public class TabelaTokens {
    private String palavra;
    private String token;
    private String log;
    private boolean estado;
    private int linha;
    private int coluna;

    public TabelaTokens(String palavra, String token, String log, int linha, int coluna, boolean estado) {
        this.palavra = palavra;
        this.token = token;
        this.log = log;
        this.linha = linha;
        this.coluna = coluna;
        this.estado = estado;
    }

    public String getPalavra() {
        return palavra;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
    
    public void setLogEstado(String log, boolean estado)
    {
        this.log = log;
        this.estado = estado;
    }
}
