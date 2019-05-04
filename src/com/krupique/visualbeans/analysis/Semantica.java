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
        int x;
        String str;
        
        for (int i = 0; i < tabela.size(); i++) {
            str = tabela.get(i).getToken();
            if(str.equals("identificador"))
            {
                for (int j = i + 1; j < tabela.size(); j++) {
                    String tabi = tabela.get(i).getPalavra();
                    String tabj = tabela.get(j).getPalavra();
                    
                    if(tabi.equals(tabj))
                    {
                        tabela.get(j).setLogEstado("[Erro]: Esta variável já foi declarada anteriormente!\n", 2);
                    }
                }
            }
        }
        return tabela;
    }
    
}
