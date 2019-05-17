/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krupique.visualbeans.analysis;

import com.krupique.visualbeans.structures.TabelaTokens;
import java.util.ArrayList;

/**
 *
 * @author Henrique K. Secchi
 */
public class Semantica {

    private ArrayList<TabelaTokens> tabela;
    
    public Semantica(ArrayList<TabelaTokens> tabela) {
        this.tabela = tabela;
    }
    
    public ArrayList<TabelaTokens> validar()
    {
        for (int i = 0; i < tabela.size(); i++) {
            if(tabela.get(i).getToken().equals("tk_declaracao_program"))
                tabela.get(i).setCatTipVal("Declaração", "-", "-");
            else if(tabela.get(i).getToken().equals("identificador"))
            {
                tabela.get(i).setCatTipVal("Variável", tabela.get(i).getTipo(), tabela.get(i).getValor());
            }
            else if(tabela.get(i).getToken().equals("valor_decimal") || 
                    tabela.get(i).getToken().equals("valor_octal") || 
                    tabela.get(i).getToken().equals("valor_hexadecimal"))
                tabela.get(i).setCatTipVal("-", "int", tabela.get(i).getPalavra());
            
            else if(tabela.get(i).getToken().equals("valor_char"))
                tabela.get(i).setCatTipVal("-", "char", tabela.get(i).getPalavra());
            
            else if(tabela.get(i).getToken().equals("valor_string"))
                tabela.get(i).setCatTipVal("-", "string", tabela.get(i).getPalavra());
            /*
            Fazer para operadores
            Fazer para ifs, else, while, etc.
            */
        }
        
        return tabela;
    }
    
}
