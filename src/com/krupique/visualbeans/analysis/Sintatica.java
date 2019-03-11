package com.krupique.visualbeans.analysis;

import com.krupique.visualbeans.structures.Pilha;
import com.krupique.visualbeans.structures.TabelaTokens;
import java.util.ArrayList;

/**
 * @author Henrique K. Secchi
 */
public class Sintatica {
   
    private String codigo;
    private Pilha pilha;
    private int i;
    private ArrayList<TabelaTokens> tabela;
    
    public Sintatica(String codigo){
        this.codigo = codigo;
    }
    
    public Object[] analisar()
    {   
        codigo = codigo.replace("\n", " £ ");
        
        Lexica lexica = new Lexica(codigo);
        Object[] obj = new Object[2];
        obj = lexica.gerarAnalise();
        
        tabela = (ArrayList<TabelaTokens>)obj[1];
        
        identificarProgram();
        
        return obj;
    }
    
    public void identificarProgram() //Fazer retorno de erro.
    {
        pilha = new Pilha();
        i = 0;
        
        String aux = tabela.get(i).getToken();
        if(aux.equals("tk_declaracao_program"))
        {
            pilha.push(aux);
            i++;
            aux = tabela.get(i).getToken();
            if(aux.equals("identificador"))
            {
                pilha.push(aux);
                i++;
                aux = tabela.get(i).getToken();
                if(aux.equals("tk_ponto_virgula"))
                {
                    pilha.push(aux);
                    i++;
                    pilha.pop(); 
                    pilha.pop(); 
                    pilha.pop(); 
                    
                    if(pilha.isEmpty())
                        identificarBloco();
                }
            }
        }
    }

    private void identificarBloco() 
    {
        String aux;
        aux = tabela.get(i++).getToken();
        if(aux.equals("tk_abrir_chaves"))
        {
            pilha.push(aux);
            //aux = tabela.get(i++).getToken();
            
            identificarResto();
            
            aux = tabela.get(i++).getToken();
            if(aux.equals("tk_fechar_chaves"))
            {
                pilha.pop();
            }
            else{
                i--;
                identificarResto();
            }
            
        }
    }
    
    private void identificarResto()
    {
        String aux = tabela.get(i).getToken();
        
        if(aux.equals("tk_abrir_chaves"))
        {
            identificarBloco();
        }
        else if(aux.equals("tk_declaracao_var") || aux.equals("tk_tipo_int") || aux.equals("tk_tipo_double") || aux.equals("tk_tipo_char") || aux.equals("tk_tipo_string") || aux.equals("tk_tipo_boolean"))
        {
            i++;
            identificarVariaveis(aux); //Validar as virgulas e as entradas invalidas e declaracoes sem atribuicoes.
            identificarResto();
        }
        else
        {
            identificarStatements();
        }
    }
    
    private boolean identificarVariaveis(String tipo)
    {
        boolean flag;
        String aux, ant;
        int auxPos = i, temp;
        
        //Validar inteiros
        if(tipo.equals("tk_tipo_int") || tipo.equals("tk_declaracao_var"))
        {
            flag = true;
            ant = tipo;
            aux = tabela.get(i++).getToken();
            while(!aux.equals("tk_ponto_virgula") && flag)
            {
                if(aux.equals("identificador"))
                {
                    aux = ant = tabela.get(i++).getToken();
                    if(aux.equals("tk_atribuicao")) /*verificar *=, /=, %=, +=, -=, <<, >>*/
                    {
                        ///FAZER UMA FUNÇÃO PARA VALIDAR EXPRESSÕES ARITMÉTICAS.
                        aux = tabela.get(i++).getToken();
                        if(aux.equals("valor") || aux.equals("valor_octal") || aux.equals("valor_hexadecimal")) //Ta certo a declaracao de int (decimal, octal ou hexadecimal)
                        {
                            aux = ant =tabela.get(i).getToken();
                            if(!(aux.equals("tk_virgula") || aux.equals("tk_ponto_virgula")))
                                flag = false;
                        }
                        else
                            flag = false;
                        
                    } else if(aux.equals("tk_virgula") && !ant.equals(tipo))
                    {
                        //Uso de virgulas depois de identificadores.
                        if(!tabela.get(i).getToken().equals("identificador"))
                            flag = false;
                    }
                    else if(aux.equals("tk_ponto_virgula"))
                        i--;
                    else
                        flag = false;
                }
                else if(aux.equals("tk_virgula") && !ant.equals(tipo))
                {
                    //Uso de virgulas depois dos valores.
                    if(!tabela.get(i).getToken().equals("identificador"))
                        flag = false;
                }
                else
                    flag = false;
                
                aux = ant = tabela.get(i++).getToken();
            }
            
            if(flag) //Retorna se int foi declarado corretamente
                return flag;
        }
        temp = i;
        
        //Validar char
        i = auxPos;
        
        //Validar double
        i = auxPos;
        
        //Validar string
        i = auxPos;
        
        //Validar boolean
        i = auxPos;
        
        i = temp; //Se a(s) variável(is) foram declaradas de maneira errada serve para continuar o fluxo da análise sintática.
        return false;
    }
    
    private void identificarStatements()
    {
        //If -- pode ter else
        //While
        //For
        //Operador ternário
        
    }
}
