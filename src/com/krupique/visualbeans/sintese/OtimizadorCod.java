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
public class OtimizadorCod {

    private ArrayList<TabelaTokens> tabela;
    
    public OtimizadorCod(ArrayList<TabelaTokens> tabela) {
        this.tabela = tabela;
        otim1(); //Regras 1,2,3,4,5
        otim2(); //Regra 6
        otim3();
        
        removerInuteis();
        print();
    }
    
    
    public void print()
    {
        //Exibir codigo intermediario
        System.out.println("\nINIT");
        for (int j = 0; j < tabela.size(); j++) {
            if(tabela.get(j).getPalavra().equals(";"))
                System.out.println(";");
            else if(tabela.get(j).getPalavra().equals("{"))
                System.out.println("{");
            else
                System.out.print(tabela.get(j).getPalavra() + " ");
        }
        System.out.println("\nBREAK");
    }
    
    //Otimizações de expressões algébricas - 5 regras
    private void otim1()
    {
        int i = 0;
        String token;
        String aux;
        while (i < tabela.size())
        {
            token = tabela.get(i).getToken();
            //Regra 1 e 2 - Remover soma ou subtração com 0
            if(token.equals("tk_add") || token.equals("tk_sub")){
                aux = tabela.get(i + 1).getPalavra();
                if(aux.equals("0")){
                    tabela.remove(i);
                    tabela.remove(i);
                }
                else{
                    aux = tabela.get(i - 1).getPalavra();
                    if(aux.equals("0")){
                        tabela.remove(i - 1);
                        tabela.remove(i - 1);
                    }
                }
            }
            
            //Regra 3 - Remover multiplicação por 1
            if(token.equals("tk_mult")){
                aux = tabela.get(i + 1).getPalavra();
                if(aux.equals("1")){
                    tabela.remove(i);
                    tabela.remove(i);
                }
                else{
                    aux = tabela.get(i - 1).getPalavra();
                    if(aux.equals("1")){
                        tabela.remove(i - 1);
                        tabela.remove(i - 1);
                    }
                }
            }
            
            //Regra 4 - Remover divisão por 1
            if(token.equals("tk_div")){
                aux = tabela.get(i + 1).getPalavra();
                if(aux.equals("1")){
                    tabela.remove(i);
                    tabela.remove(i);
                }
            }
            
            //Regra 5 - Aplicar x + x quando acontecer 2 * x ou x * 2
            if(token.equals("tk_mult")){
                aux = tabela.get(i + 1).getPalavra();
                if(aux.equals("2")){
                    tabela.get(i).setPalavra("+");
                    tabela.get(i).setToken("tk_add");
                    tabela.get(i + 1).setTudo(new TabelaTokens(tabela.get(i - 1).getTudo()));
                }
                else{
                    aux = tabela.get(i - 1).getPalavra();
                    if(aux.equals("2")){
                        tabela.get(i).setPalavra("+");
                        tabela.get(i).setToken("tk_add");
                        tabela.get(i - 1).setTudo(new TabelaTokens(tabela.get(i + 1).getTudo()));
                    }
                }
            }
            
            i++;
        }
    }
    
    //Remover condições que nunca serão satisfeitas
    private void otim2()
    {
        int i = 0;
        String token;
        String aux, aux2;
        while(i < tabela.size())
        {
            token = tabela.get(i).getToken();
            if(token.equals("tk_comando_if") || token.equals("tk_comando_while"))
            {
                i += 2;
                aux = tabela.get(i++).getPalavra();
                aux2 = tabela.get(i++).getToken();
                if((aux.equals("0") && aux2.equals("tk_fechar_parenteses")) || 
                        (aux.equals("false") && aux2.equals("tk_fechar_parenteses")))
                {
                    i -= 4;
                    tabela.remove(i);
                    tabela.remove(i);
                    tabela.remove(i);
                    tabela.remove(i);
                    remover(i);
                }
            }
            i++;
        }
    }
    
    //Subrotina de otim2()
    private void remover(int pos)
    {
        String token = tabela.get(pos).getToken();
        int cont = 1;
        if(token.equals("tk_abrir_chaves"))
        {
            tabela.remove(pos);
            while(cont != 0) {
                if(tabela.get(pos).getToken().equals("tk_fechar_chaves"))
                    cont--;
                else if(tabela.get(pos).getToken().equals("tk_abrir_chaves"))
                    cont++;
                tabela.remove(pos);
            }
            
        }
        else
            while(!tabela.get(pos).getToken().equals("tk_ponto_virgula"))
                tabela.remove(pos);
        tabela.remove(pos);
    }
    
    private void otim3()
    {
        
    }

    private void removerInuteis() {
        
        
    }
    
    public ArrayList<TabelaTokens> getTabela() {
        return tabela;
    }
}
