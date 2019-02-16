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
public class SimbolosReservados {
    private String simbolo;
    private String descricao;
    private String token;

    public SimbolosReservados(String simbolo, String descricao, String token) {
        this.simbolo = simbolo;
        this.descricao = descricao;
        this.token = token;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
