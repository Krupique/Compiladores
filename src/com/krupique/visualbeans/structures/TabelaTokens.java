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
    private String log; //0 = sem erros, 1 = erro sintático, 2 = erro semântico
    private int linha;
    private int coluna;
    private int estado;
    private String categoria;
    private String tipo;
    private String valor;

    public TabelaTokens(String palavra, String token, String log, int linha, int coluna, int estado) {
        this.palavra = palavra;
        this.token = token;
        this.log = log;
        this.linha = linha;
        this.coluna = coluna;
        this.estado = estado;
    }

    public TabelaTokens(String palavra, String token, String log, int linha, int coluna, int estado, String categoria, String tipo, String valor) {
        this.palavra = palavra;
        this.token = token;
        this.log = log;
        this.linha = linha;
        this.coluna = coluna;
        this.estado = estado;
        this.categoria = categoria;
        this.tipo = tipo;
        this.valor = valor;
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
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
    
    public void setLogEstado(String log, int estado)
    {
        this.log = log;
        this.estado = estado;
    }
}
