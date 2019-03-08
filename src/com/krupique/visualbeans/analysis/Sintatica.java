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
    
    public Sintatica(String codigo){
        this.codigo = codigo;
    }
    
    public void analisar()
    {
        Lexica lexica = new Lexica(codigo);
        Object[] obj = new Object[2];
        obj = lexica.gerarAnalise();
        
        ArrayList<TabelaTokens> tabela = (ArrayList<TabelaTokens>)obj[1];
        
        /*Ta funcionando a parada de retornar objects
        for (int i = 0; i < tabela.size(); i++) {
            System.out.println("i:" + i + " Token: " + tabela.get(i).getToken()); 
        }*/
        
        pilha = new Pilha();
    }
}
