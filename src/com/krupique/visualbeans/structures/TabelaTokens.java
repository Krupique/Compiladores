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
    
    private String categoria; //Variável, Declaração ou valor
    private String tipo; //Tipo da variável ou do valor
    private String valor; 
    private String origem; //É declaração de variável ou não?

    public TabelaTokens(String palavra, String token, String log, int linha, int coluna, int estado) {
        this.palavra = palavra;
        this.token = token;
        this.log = log;
        this.linha = linha;
        this.coluna = coluna;
        this.estado = estado;
    }

    public TabelaTokens(String palavra, String token, String log, int linha, int coluna, int estado, String categoria, String tipo, String valor, String origem) {
        this.palavra = palavra;
        this.token = token;
        this.log = log;
        this.linha = linha;
        this.coluna = coluna;
        this.estado = estado;
        this.categoria = categoria;
        this.tipo = tipo;
        this.valor = valor;
        this.origem = origem;
    }
    
    public TabelaTokens(TabelaTokens tk)
    {
        this.palavra = tk.getPalavra();
        this.token = tk.getToken();
        this.log = tk.getLog();
        this.linha = tk.getLinha();
        this.coluna = tk.getColuna();
        this.estado = tk.getEstado();
        this.categoria = tk.getCategoria();
        this.tipo = tk.getTipo();
        this.valor = tk.getValor();
        this.origem = tk.getOrigem();
    }
    
    public TabelaTokens getTudo()
    {
        return new TabelaTokens(palavra, token, log, linha, coluna, estado, categoria, tipo, valor, origem);
    }
    
    public void setCatTipVal(String cat, String tipo, String valor)
    {
        this.categoria = cat;
        this.tipo = tipo;
        this.valor = valor;
    }
    
    public void setCatTipValOri(String cat, String tipo, String valor, String origem)
    {
        this.categoria = cat;
        this.tipo = tipo;
        this.valor = valor;
        this.origem = origem;
    }
    
    public void setTipoValOri(String tipo, String valor, String origem){
        this.tipo = tipo;
        this.valor = valor;
        this.origem = origem;
    }
    
    public void setTipoVal(String tipo, String valor){
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
        /*
        this.categoria = "";
        this.tipo = "";
        this.palavra = "";*/
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }
}
