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
        
        for (int j = 0; j < tabela.size(); j++) {
            if(!tabela.get(j).getEstado())
            {
                System.out.println("Erro: " + tabela.get(j).getLog()+ " linha: " + tabela.get(j).getLinha());
            }
        }
        
        obj[0] = objaux[0];
        obj[1] = objaux[1];
        obj[2] = codigo;
        return obj;
    }
    
    //Identificar program (não é rec)
    //Identificar bloco (não é rec)
    //Identificar statements (chama identificar bloco)
        //-> Declaração de variáveis.
                //-> expressões aritméticas.
        //-> Chamar expressões
                //-> if
                    //-> if-else.
                //-> while.
                //-> for.
        //-> Expressões de pre e post inc/dec
    
    public void identificarProgram()
    {
        int erro, pos;
        erro = pos = 0;
        
        if(!tabela.get(pos).getToken().equals("tk_declaracao_program"))
            tabela.get(pos).setLogEstado("Declaração de program inválida!", false);
        pos++;
        
        if(!tabela.get(pos).getToken().equals("identificador"))
            tabela.get(pos).setLogEstado("Declaração de nome de program inválido!", false);
        pos++;
        
        if(!tabela.get(pos).getToken().equals("tk_ponto_virgula"))
            tabela.get(pos).setLogEstado("Declaração de ponto e vírgula inválido!", false);
        pos++;
        
        identificarBloco(pos);
    }
    
    public void identificarBloco(int i)
    {   
        String token = tabela.get(i).getToken();
        
        if(!token.equals("tk_abrir_chaves"))
            tabela.get(i).setLogEstado("Não foi encontrdo abrir chaves do bloco", false);
        else
            i++;
    }
}
    
    
