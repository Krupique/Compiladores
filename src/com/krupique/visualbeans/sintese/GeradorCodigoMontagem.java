/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krupique.visualbeans.sintese;

import com.krupique.visualbeans.structures.Montagem;
import com.krupique.visualbeans.structures.TabelaTokens;
import java.util.ArrayList;

/**
 *
 * @author Henrique K. Secchi
 */
public class GeradorCodigoMontagem {
    
    ArrayList<TabelaTokens> tabela;
    ArrayList<Montagem> montagem;

    public GeradorCodigoMontagem(ArrayList<TabelaTokens> tabela) {
        this.tabela = tabela;
        montagem = new ArrayList<>();
    }
    
    public String geraCodigo()
    {
        
        return "hehe";
    }
    
    

    public ArrayList<TabelaTokens> getTabela() {
        return tabela;
    }
    
}
