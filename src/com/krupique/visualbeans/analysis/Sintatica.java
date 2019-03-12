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
        Object[] obj = new Object[3];
        Object[] objaux;
        String aux = codigo.replaceAll("\n", " ℡ "); //Substitui todos os "\n" por ℡ (Serve para a análise léxica).
        codigo = codigo.replaceAll("\\s*\n+", "\n").replaceAll(" +", " ").replaceAll("\t", "      "); //Substitui todas as ocorrências de um ou mais "\n" por um único "\n", depois remove todos os espaços extras e por fim substitui todos os tabs por 6 espaços.
        
        Lexica lexica = new Lexica(aux);
        objaux = lexica.gerarAnalise();
        
        tabela = (ArrayList<TabelaTokens>)objaux[1];
        
        identificarProgram();
        
        obj[0] = objaux[0];
        obj[1] = objaux[1];
        obj[2] = codigo;
        return obj;
    }
    
    public void identificarProgram()
    {
        
    }
}
    
    
