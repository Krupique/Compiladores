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
    private ArrayList<String> listVariaveis;
    
    public Sintatica(String codigo){
        this.codigo = codigo;
    }
    
    public Object[] analisar()
    {   
        Object[] obj = new Object[3];
        Object[] objaux;
        listVariaveis = new ArrayList<String>();
        
        String aux = codigo.replaceAll("\n", " ℡ "); //Substitui todos os "\n" por ℡ (Serve para a análise léxica).
        codigo = codigo.replaceAll("\\s*\n+", "\n").replaceAll(" +", " ").replaceAll("\t", "      "); //Substitui todas as ocorrências de um ou mais "\n" por um único "\n", depois remove todos os espaços extras e por fim substitui todos os tabs por 6 espaços.
        
        Lexica lexica = new Lexica(aux);
        objaux = lexica.gerarAnalise();
        
        tabela = (ArrayList<TabelaTokens>)objaux[1];
        for (int j = 0; j < tabela.size(); j++) {
            if(tabela.get(j).getToken().equals("identificador"))
                listVariaveis.add(tabela.get(j).getPalavra());
        }
        
        
        identificarProgram();
        
        for (int j = 0; j < tabela.size(); j++) {
            if(!tabela.get(j).getEstado())
            {
                System.out.println("Erro: " + tabela.get(j).getLog()+ " linha: " + tabela.get(j).getLinha());
            }
        }
        
        for (int j = 0; j < listVariaveis.size(); j++) {
            System.out.println("VAR: " + listVariaveis.get(j));
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
    
    private void identificarProgram()
    {
        Pilha pilha = new Pilha();
        Pilha aux = new Pilha();
        
        int i = 0;
        while(i < tabela.size() && !tabela.get(i).getToken().equals("tk_ponto_virgula"))
            pilha.push(tabela.get(i++).getToken());
        
        if(i < tabela.size())
        {
            pilha.push(tabela.get(i++).getToken());
            while(!pilha.isEmpty())
                aux.push(pilha.pop());
            
            if(aux.getTl() == 3)
            {
                if(!aux.pop().equals("tk_declaracao_program"))
                    tabela.get(0).setLogEstado("[Erro]: Declaração de programa inválida!", false);
                
                if(!aux.pop().equals("identificador"))
                    tabela.get(1).setLogEstado("[Erro]: Declaração de nome de programa inválida!", false);
                
                if(!aux.pop().equals("tk_ponto_virgula"))
                    tabela.get(2).setLogEstado("[Erro]: Ponto e vírgula inválido!", false);
            } 
            else if(aux.getTl() < 3)
                tabela.get(0).setLogEstado("[Erro]: Está faltando parâmetros na declaração!", false);
            else
                tabela.get(0).setLogEstado("[Erro]: Parâmetros excedentes na declaração!", false);
            /**/
            
            identificarBloco(i);
        }
        
    }
    
    private int identificarBloco(int pos)
    {
        Pilha pilha;
        String tkaux;
        if(pos < tabela.size())
        {
            pilha = new Pilha();
            tkaux = tabela.get(pos).getToken();
            int auxpos = pos;
            
            if(tkaux.equals("tk_abrir_chaves"))
                pilha.push(tabela.get(pos++).getToken());
            else
            {
                pilha.push(tabela.get(pos).getToken());
                tabela.get(pos).setLogEstado("[Erro]: Abrir chaves declarado incorretamente!", false);
            }

            //Verificar o pos...
            pos = identificarStatement(pos);
        
            if(pos >= tabela.size())
                pos--;
            
            pilha.push(tabela.get(pos++).getToken());
            if(pilha.getTl() < 2)
                tabela.get(auxpos).setLogEstado("[Erro]: Você deveria ter declarado abrir chaves!", false);
            if(!pilha.pop().equals("tk_fechar_chaves"))
                tabela.get(pos - 1).setLogEstado("[Erro]: Fechar chaves declarado incorretamente!", false);
            
        }
        return pos;
    }

    private int identificarStatement(int pos) {
        int flag = qualStatement(pos);
        
        if(flag == 1) //Declaração de variável.
        {
            System.out.println("Declaração de variável");
        }
        else if(flag == 2) //Expressão aritmética
        {
            System.out.println("Expressão aritmética");
        }
        else if(flag == 3) //Expressão comando (if, else, for ou while)
        {
            System.out.println("Expressão comando");
        }
        else if(flag == 4) //Operação pre incremento/decremento
        {
            System.out.println("Operação pre inc/dec");
            pos = validarIncDec(pos, 1);
        }
        else if(flag == 5) //Operação post incremento/decremento
        {
            System.out.println("Operação post inc/dec");
            pos = validarIncDec(pos, 2);
        }
        else //ERRO
        {
            
            //Pensar nisso aqui depois
            /*if(isStatOrBlock(++pos) == 1)
                identificarBloco(pos);
            else
                identificarStatement(pos);*/
        }
        
        return pos;
    }
    
    private int isStatOrBlock(int pos)
    {
        if(tabela.get(pos).getToken().equals("tk_abrir_chaves"))
            return 0; //É bloco
        return 1; //É statement
    }

    private int qualStatement(int pos) {
        String token = tabela.get(pos).getToken();
        
        if(token.equals("tk_tipo_int") || token.equals("tk_tipo_char") || token.equals("tk_tipo_boolean") || token.equals("tk_tipo_double") || token.equals("token_tipo_string"))
            return 1; //Declaração de variável
        else if(token.equals("identificador"))
        {
            String token2 = tabela.get(pos + 1).getToken();
            if(token2.contains("atribuicao") || token2.contains("deslocamento"))
                return 2; //Expressão aritmética
            else if(token2.equals("tk_inc") || token2.equals("tk_dec"))
                return 5; //Operação post incremento/decremento
        }
        else if(token.equals("tk_comando_if") || token.equals("tk_comando_else") || token.equals("tk_comando_while") || token.equals("tk_comando_for"))
            return 3; //Expressão comando (if, else, while ou for)
        else if(token.equals("tk_inc") || token.equals("tk_dec"))
            return 4; //Operação pre incremento/decremento
        
        return -1; //Erro
    }
    
    private int validarIncDec(int pos, int flag)
    {
        Pilha pilha = new Pilha();
        int auxpos;
        while(pos < tabela.size() && !tabela.get(pos).getToken().equals("tk_ponto_virgula"))
            pilha.push(tabela.get(pos++).getToken());
        
        auxpos = pos - 1;
        if(pos < tabela.size())
        {
            pilha.push(tabela.get(pos).getToken());
            
            if(pilha.getTl() == 3 && pilha.getTopPilha().equals("tk_ponto_virgula"))
            {
                pos++;
                if(!pilha.pop().equals("tk_ponto_virgula"))
                    tabela.get(pos - 1).setLogEstado("[Erro]: Declaração de ponto vírgula inválido!", false);
                
                if(flag == 1) //Pre incremento/decremento
                {
                    if(!pilha.pop().equals("identificador"))
                        tabela.get(pos - 2).setLogEstado("[Erro]: Declaração de identificador inválido!", false);
                    String auxp = pilha.pop();
                    if(!(auxp.equals("tk_inc") || auxp.equals("tk_dec")))
                        tabela.get(pos - 3).setLogEstado("[Erro]: Declaração de operador (++|--) inválido!", false);
                }
                else if(flag == 2) //Post incremento/decremento
                {
                    String auxp = pilha.pop();
                    if(!(auxp.equals("tk_inc") || auxp.equals("tk_dec")))
                        tabela.get(pos - 2).setLogEstado("[Erro]: Declaração de operador (++|--) inválido!", false);
                    if(!pilha.pop().equals("identificador"))
                        tabela.get(pos - 3).setLogEstado("[Erro]: Declaração de identificador inválido!", false);
                }
            }
            else if(pilha.getTl() > 3)
                tabela.get(pos - 3).setLogEstado("[Erro]: Parâmetros excedentes na operação (++V|--V)!", false);
            else
                tabela.get(pos - 3).setLogEstado("[Erro]: Está faltando parâmetros na operação (++V|--V)!", false);
        }
        else
        {
            tabela.get(auxpos).setLogEstado("[Erro]: Operação inválida!", false);
        }
        
        return pos;
    }

}
    
    
