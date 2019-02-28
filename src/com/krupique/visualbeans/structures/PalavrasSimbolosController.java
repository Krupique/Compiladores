/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krupique.visualbeans.structures;

import java.util.ArrayList;

/**
 *
 * @author Henrique K. Secchi
 */
public class PalavrasSimbolosController {
    private ArrayList<PalavrasReservadas> listPalavras;
    private ArrayList<SimbolosReservados> listSimbolos;
    
    public PalavrasSimbolosController()
    {
        listPalavras = new ArrayList<PalavrasReservadas>();
        listSimbolos = new ArrayList<SimbolosReservados>();
        
        iniciarPalavras();
        iniciarSimbolos();
    }

    private void iniciarSimbolos() {
        SimbolosReservados simbolos;
        simbolos = new SimbolosReservados("[", "abertura de conchetes", "tk_abrir_conchetes");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("]", "fechamento de conchetes", "tk_fechar_conchetes");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("{", "abertura de chaves", "tk_abrir_chaves");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("}", "fechamento de chaves", "tk_fechar_chaves");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados(";", "ponto e vírgula", "tk_ponto_virgula");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("=", "atribuição", "tk_atribuicao");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("*=", "atribuição e multiplicação", "tk_atribuicao_mult");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("/=", "atribuição e divisão", "tk_atribuicao_div");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("%=", "atribuição e resto", "tk_atribuicao_resto");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("+=", "atribuição e adição", "tk_atribuicao_add");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("-=", "atribuição e subtração", "tk_atribuicao_sub");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("<<", "deslocamento de bits à esquerda", "tk_deslocamento_esq");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados(">>", "deslocamento de bits à direita", "tk_deslocamento_dir");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("?", "operador ternário", "tk_oper_ternario");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados(":", "se não ternário", "tk_else_ternario");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("||", "or lógico", "tk_or");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("&&", "and lógico", "tk_and");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("|", "or inclusivo", "tk_or_inclusivo");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("^", "or exclusivo", "tk_or_exclusivo");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("&", "and inclusivo", "tk_and_inclusivo");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("==", "igualdade", "tk_igualdade");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("!=", "diferençã", "tk_diferenca");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("<", "menor", "tk_menor");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados(">", "maior", "tk_maior");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("<=", "menor ou igual", "tk_menor_igual");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados(">=", "maior ou igual", "tk_maior_igual");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("<<<", "deslocamento de bits à esquerda sem sinal", "tk_deslocamento_esq_unsigned");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados(">>>", "deslocamento de bits à direita sem sinal", "tk_deslocamento_dir_unsigned");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("+", "adição", "tk_add");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("-", "subtração", "tk_sub");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("*", "multiplicação", "tk_mult");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("/", "divisão", "tk_div");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("%", "resto da divisão", "tk_resto");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("++", "incremento", "tk_inc");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("--", "decremento", "tk_dec");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("~", "negação de bits", "tk_negacao_bits");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("!", "negação", "tk_negacao");
        listSimbolos.add(simbolos);
        /*simbolos = new SimbolosReservados("'\\n'", "quebra de linha", "tk_barra_n");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("'\\0'", "final de linha", "tk_barra_zero");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("'\\t'", "tabulação", "tk_tab");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("'\\\"'", "barra aspas", "tk_barra_aspas");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("\"", "leitura de string", "tk_leitura_string");
        listSimbolos.add(simbolos);
        simbolos = new SimbolosReservados("'", "leitura de caracter", "tk_leitura_char");
        listSimbolos.add(simbolos);*/
        simbolos = new SimbolosReservados(",", "vírgula", "tk_virgula");
        listSimbolos.add(simbolos);
        /*simbolos = new SimbolosReservados(".", "ponto", "tk_ponto");
        listSimbolos.add(simbolos);*/
    }    

    private void iniciarPalavras() {
        PalavrasReservadas palavra;
        palavra = new PalavrasReservadas("and", "operador", "tk_operador_and");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("boolean", "tipo", "tk_tipo_bool");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("catch", "comando", "tk_comando_catch");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("char", "tipo", "tk_tipo_char");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("double", "tipo", "tk_tipo_double");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("else", "comando", "tk_comando_else");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("false", "afirmação", "tk_afirmacao_false");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("for", "comando", "tk_comando_for");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("if", "comando", "tk_comando_if");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("int", "tipo", "tk_tipo_int");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("mentira", "afirmação", "tk_afirmacao_false");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("new", "declaração", "tk_operador_new");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("null", "tipo", "tk_tipo_null");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("nulo", "tipo", "tk_tipo_null");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("NULL", "tipo", "tk_tipo_null");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("or", "operador", "tk_operador_or");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("program", "declaração", "tk_declaracao_program");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("string", "tipo", "tk_tipo_string");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("true", "afirmação", "tk_afirmacao_true");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("try", "comando", "tk_comando_try");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("var", "declaração", "tk_declaracao_var");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("verdade", "afirmação", "tk_afirmacao_true");
        listPalavras.add(palavra);
        palavra = new PalavrasReservadas("while", "comando", "tk_comando_while");
        listPalavras.add(palavra);
    }

    public ArrayList<PalavrasReservadas> getListPalavras() {
        return listPalavras;
    }
    
    public ArrayList<SimbolosReservados> getListSimbolos() {
        return listSimbolos;
    }
    
    public boolean contemToken(String token)
    {
        int i = 0;
        while(i < listPalavras.size() && !listPalavras.get(i).getPalavra().contains(token))
            i++;
        
        if(i < listPalavras.size())
            return true;
        else
        {
            i = 0;
            while(i < listSimbolos.size() && !listSimbolos.get(i).getSimbolo().contains(token))
                i++;
            
            if(i < listSimbolos.size())
                return true;
        }
        return false;
    }
    
    public boolean buscaToken(String token)
    {
        int i = 0;
        while(i < listPalavras.size() && !listPalavras.get(i).getPalavra().equals(token))
            i++;
        
        if(i < listPalavras.size())
            return true;
        else
        {
            i = 0;
            while(i < listSimbolos.size() && !listSimbolos.get(i).getSimbolo().equals(token))
                i++;
            
            if(i < listSimbolos.size())
                return true;
        }
        return false;
    }
    
    public boolean buscaSimbolo(String token)
    {
        int i = 0;
        while(i < listSimbolos.size() && !listSimbolos.get(i).getSimbolo().equals(token))
            i++;

        if(i < listSimbolos.size())
            return true;
        return false;
    }
    
    public boolean buscaPalavra (String token)
    {
        int i = 0;
        while(i < listPalavras.size() && !listPalavras.get(i).getPalavra().equals(token))
            i++;
        
        if(i < listPalavras.size())
            return true;
        return false;
    }
    
    public boolean contemSimbolo(String token)
    {
        int i = 0;
        while(i < listSimbolos.size() && !listSimbolos.get(i).getSimbolo().contains(token))
            i++;

        if(i < listSimbolos.size())
            return true;
        return false;
    }
    
    public boolean contemPalavra (String token)
    {
        int i = 0;
        while(i < listPalavras.size() && !listPalavras.get(i).getPalavra().contains(token))
            i++;
        
        if(i < listPalavras.size())
            return true;
        return false;
    }
    
    public String buscarToken(String token)
    {
        int i = 0;
        while(i < listSimbolos.size() && !listSimbolos.get(i).getSimbolo().equals(token))
            i++;
        
        if(i < listSimbolos.size())
            return listSimbolos.get(i).getToken();
        else
        {
            i = 0;
            while(i < listPalavras.size() && !listPalavras.get(i).getPalavra().equals(token))
                i++;
            if(i < listPalavras.size())
                return listPalavras.get(i).getToken();
        }
        return null;
    }
}
