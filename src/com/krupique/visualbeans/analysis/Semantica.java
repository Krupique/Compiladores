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
    private ArrayList<String> logs;
    
    public Semantica(ArrayList<TabelaTokens> tabela) {
        this.tabela = tabela;
        for (int i = 0; i < tabela.size(); i++)
            if(tabela.get(i).getValor()== null)
                tabela.get(i).setValor("");
        
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
    
    public ArrayList<TabelaTokens> somenteVariaveis()
    {
        ArrayList<TabelaTokens> temp = new ArrayList<>();
        String token;
        for (int i = 0; i < tabela.size(); i++) {
            token = tabela.get(i).getToken();
            if(token.equals("identificador") || token.equals("valor_decimal") || 
                    token.equals("valor_octal") || token.equals("valor_hexadecimal") ||
                    token.equals("valor_char") || token.equals("valor_string"))
                temp.add(new TabelaTokens(tabela.get(i)));
            
        }
        return temp;
    }
    
    
    public ArrayList<TabelaTokens> removerInuteis()
    {
        tabela.remove(0);
        tabela.remove(0);
        tabela.remove(0);
        tabela.remove(0);
        tabela.remove(tabela.size() - 1);
        logs = new ArrayList<>();
        
        int i = 0;
        while(i < tabela.size())
        {
            boolean flag = false;
            if(tabela.get(i).getOrigem() != null && tabela.get(i).getOrigem().equals("declaracao") && !flag)
            {
                int j = i + 1;
                boolean achou = false;
                while(j < tabela.size() && !achou)
                {
                    String aux = tabela.get(i).getPalavra();
                    if(tabela.get(i).getPalavra().equals(tabela.get(j).getPalavra()))
                        achou = true;
                    j++;
                }
                
                if(!achou)
                {
                    logs.add(tabela.get(i).getPalavra());
                    removerLinha(i);
                    //tabela.remove(i);
                    i = 0;
                    continue;
                }
            }
            
            i++;
        }
        
        return tabela;
    }
    
    private int removerLinha(int pos)
    {
        int i = pos;
        int cont = 0;
        while(!tabela.get(i).getToken().equals("tk_ponto_virgula"))
        {
            cont++;
            i++;
        }
        for (int j = 0; j < cont + 1; j++)
            tabela.remove(pos);
        
        cont = 0;
        pos--;
        i = pos;
        while(i >= 0 && !tabela.get(i).getToken().equals("tk_ponto_virgula"))
        {
            cont++;
            i--;
        }
        
        pos = pos - cont + 1;
        for (int j = 0; j < cont; j++) {
            tabela.remove(pos);
        }
        return pos;
    }
    

    public ArrayList<String> getLogs() {
        return logs;
    }
    
    
}
