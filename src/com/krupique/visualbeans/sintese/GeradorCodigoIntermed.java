/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krupique.visualbeans.sintese;

import com.krupique.visualbeans.structures.TabelaTokens;
import java.util.ArrayList;

/**
 *
 * @author Henrique K. Secchi
 */
public class GeradorCodigoIntermed {
    TabelaTokens _temp;
    ArrayList<TabelaTokens> _resfinal;
    
    
    public GeradorCodigoIntermed(ArrayList<TabelaTokens> tabela)
    {
        _resfinal = new ArrayList<>();
        int i = 0;
        while(i < tabela.size())
        {
            if(tabela.get(i).getValor().equals("exp"))
                i = executa(tabela, i);
            i++;
        }
        atribTab(tabela);
    }
    
    private void atribTab(ArrayList<TabelaTokens> tab)
    {
        for (int i = 0; i < tab.size(); i++)
            _resfinal.add(new TabelaTokens(tab.get(i)));
    }

    public ArrayList<TabelaTokens> getResfinal() {
        return _resfinal;
    } 
    
    private int executa(ArrayList<TabelaTokens> tabela, int pos)
    {
        ArrayList<TabelaTokens> aux = new ArrayList<>();
        while(!tabela.get(pos).getToken().equals("tk_ponto_virgula"))
        {
            aux.add(new TabelaTokens(tabela.get(pos).getTudo()));
            tabela.remove(pos);
        }
        
        /*Codigo intermed aqui*/
        TabelaTokens _temp;
        int cont_temp = 1;
        TabelaTokens _res = new TabelaTokens(aux.get(0).getTudo());
        ArrayList<TabelaTokens> resultado = new ArrayList<>();
        String tipo = aux.get(0).getTipo();
        aux.remove(0);
        aux.remove(0);
        
        int init;
        while(aux.size() > 1)
        {
            init = possuiParenteses(aux);
            if(init == -1) //Possui e encontra qual é o parenteses mais interno    
            {
                init = encontraPos(aux); //Encontra a posicao ideal
                
                //Primeira parte da expressão: 'temp1 ='
                _temp = new TabelaTokens("temp" + cont_temp, "tk_temp", "", aux.get(init).getLinha(), 5, 0, "Temporário", tipo, "", "");
                resultado.add(_temp);
                _temp = new TabelaTokens("=", "tk_atribuicao", "", aux.get(init).getLinha(), 7, 0);
                resultado.add(_temp);
                
                //Segunda parte da expressão 'x * 3'
                _temp = new TabelaTokens(aux.get(init + 1));
                resultado.add(_temp);
                aux.remove(init + 1);
                
                _temp = new TabelaTokens(aux.get(init + 1));
                resultado.add(_temp);
                aux.remove(init + 1);
                
                _temp = new TabelaTokens(aux.get(init + 1));
                resultado.add(_temp);
                aux.remove(init + 1);
                
                //Remover do aux;
                _temp = new TabelaTokens("temp" + cont_temp++, "tk_temp", "", aux.get(init).getLinha(), 5, 0, "Temporário", tipo, "", "");
                if(aux.get(init + 1).getToken().equals("tk_fechar_parenteses"))
                {
                    aux.add(init, _temp);
                    aux.remove(init + 1);
                    aux.remove(init + 1);
                }
                else{
                    aux.add(init + 1, _temp);
                }
            }
            else
            {
                init--;
                //Primeira parte da expressão: 'temp1 ='
                _temp = new TabelaTokens("temp" + cont_temp, "tk_temp", "", aux.get(init).getLinha(), 5, 0, "Temporário", tipo, "", "");
                resultado.add(_temp);
                _temp = new TabelaTokens("=", "tk_atribuicao", "", aux.get(init).getLinha(), 7, 0);
                resultado.add(_temp);
                
                //Segunda parte da expressão 'x * 3'
                _temp = new TabelaTokens(aux.get(init));
                resultado.add(_temp);
                aux.remove(init);
                
                _temp = new TabelaTokens(aux.get(init));
                resultado.add(_temp);
                aux.remove(init);
                
                _temp = new TabelaTokens(aux.get(init));
                resultado.add(_temp);
                aux.remove(init);
                
                //Inserir em aux
                _temp = new TabelaTokens("temp" + cont_temp++, "tk_temp", "", 0, 5, 0, "Temporário", tipo, "", "");
                aux.add(init, _temp);
            }
            
            _temp = new TabelaTokens(";", "tk_ponto_virgula", "", 0, 0, 0);
            resultado.add(_temp);
        }
        //Ultima atribuição
        resultado.add(_res);
        _temp = new TabelaTokens("=", "tk_atribuicao", "", _res.getLinha(), 7, 0);
        resultado.add(_temp);
        _temp = new TabelaTokens(aux.get(0));
        resultado.add(_temp);
        _temp = new TabelaTokens(";", "tk_ponto_virgula", "", 0, 0, 0);
            resultado.add(_temp);
        aux.remove(0);
        
        print(resultado);
        System.out.println("\n\nBreak");
        
        
        for (int i = 0; i < resultado.size(); i++) {
            tabela.add(pos++, new TabelaTokens(resultado.get(i)));
        }
        return pos; //lembrar do ponto e virgula
    }
    
    private void print(ArrayList<TabelaTokens> resultado)
    {
        for (int w = 0; w < resultado.size(); w++) {
            if(resultado.get(w).getPalavra().equals(";"))
                System.out.println(";");
            else System.out.print(resultado.get(w).getPalavra() + " ");
        }
        
        
    }
    
    private void separa(ArrayList<TabelaTokens> aux)
    {
        if(aux.size() > 0)
        {
            int init = possuiParenteses(aux);
            if(init == -1)
                init  = encontraPos(aux);
            
            
        }
    }
    
    private int possuiParenteses(ArrayList<TabelaTokens> aux)
    {
        for (int i = 0; i < aux.size(); i++) {
            if(aux.get(i).getToken().equals("tk_abrir_parenteses"))
                return -1;
        }
        
        String str;
        for (int i = 0; i < aux.size(); i++) {
            str = aux.get(i).getToken();
            if(str.equals("tk_mult") || str.equals("tk_div") || str.equals("tk_resto"))
                return i;
        }
        return 1;
    }
    
    private int encontraPos(ArrayList<TabelaTokens> aux)
    {
        int j = 0;
        int pos = 0;
        while(j < aux.size())
        {
            if(aux.get(j).getToken().equals("tk_abrir_parenteses"))
                pos = j;
            j++;
        }
        
        return pos;
    }
    
    
    public ArrayList<String> realocarArray(ArrayList<String> original, ArrayList<String> sublista, int pos)
    {
        ArrayList<String> resultado = new ArrayList<>();
        for (int i = 0; i < pos; i++) 
            resultado.add(original.get(i));
        
        for (int i = 0; i < sublista.size(); i++) 
            resultado.add(sublista.get(i));
        
        for (int i = pos; i < original.size(); i++)
            resultado.add(original.get(i));
       
        return resultado;
    }
    
    
    
}
