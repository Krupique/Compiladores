package com.krupique.visualbeans.analysis;

import com.krupique.visualbeans.structures.ListaVariaveis;
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
    private ArrayList<ListaVariaveis> listVariaveis;
    
    public Sintatica(String codigo){
        this.codigo = codigo;
    }
    
    public Object[] analisar()
    {   
        Object[] obj = new Object[3];
        Object[] objaux;
        listVariaveis = new ArrayList<ListaVariaveis>();
        
        String aux = codigo.replaceAll("\n", " ℡ "); //Substitui todos os "\n" por ℡ (Serve para a análise léxica).
        codigo = codigo.replaceAll("\\s*\n+", "\n");//.replaceAll(" +", " ");//.replaceAll("\t", "      "); //Substitui todas as ocorrências de um ou mais "\n" por um único "\n", depois remove todos os espaços extras e por fim substitui todos os tabs por 6 espaços.
        
        Lexica lexica = new Lexica(aux);
        objaux = lexica.gerarAnalise();
        
        tabela = (ArrayList<TabelaTokens>)objaux[1];
        for (int j = 0; j < tabela.size(); j++) {
            if(tabela.get(j).getToken().equals("identificador"))
                listVariaveis.add(new ListaVariaveis(tabela.get(j).getPalavra()));
            
            System.out.println(tabela.get(j).getPalavra() + " linha: " + tabela.get(j).getLinha() + " coluna: " + tabela.get(j).getColuna());
        }
        
        
        //identificarProgram();
        
        for (int j = 0; j < tabela.size(); j++) {
            if(!tabela.get(j).getEstado())
            {
                System.out.println("Erro: " + tabela.get(j).getLog()+ " linha: " + tabela.get(j).getLinha());
            }
        }
        
        for (int j = 0; j < listVariaveis.size(); j++) {
            System.out.println("VAR: " + listVariaveis.get(j).getNome());
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
        else
            tabela.get(0).setLogEstado("[Erro]: Declaração inválida!", false);
        
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
            {
                pos--;
                tabela.get(pos).setLogEstado("[Erro]: Não foi declarado fechar chaves!", false);
            }
            else
            {
                pilha.push(tabela.get(pos++).getToken());
                if(pilha.getTl() < 2)
                    tabela.get(auxpos).setLogEstado("[Erro]: Você deveria ter declarado abrir chaves!", false);
                if(!pilha.pop().equals("tk_fechar_chaves"))
                    tabela.get(pos - 1).setLogEstado("[Erro]: Fechar chaves declarado incorretamente!", false);            
            }
            
        }
        return pos;
    }

    private int identificarStatement(int pos) {
        int flag = qualStatement(pos);
        
        if(flag == 1) //Declaração de variável.
        {
            System.out.println("Declaração de variável");
            pos = validarVariaveis(pos);
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
        else if(qualStatement(pos) != -1)
            return 1; //É statement
        else
            return -1; //É erro
    }

    private int qualStatement(int pos) {
        String token = tabela.get(pos).getToken();
        
        if(token.equals("tk_tipo_int") || token.equals("tk_tipo_char") || token.equals("tk_tipo_bool") || token.equals("tk_tipo_double") || token.equals("tk_tipo_string"))
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
            pilha.push(tabela.get(pos++).getToken());
            
            if(pilha.getTl() == 3 && pilha.getTopPilha().equals("tk_ponto_virgula"))
            {
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

    private int validarVariaveis(int pos)
    {
        Pilha pilha = new Pilha();
        int auxpos = pos;
        
        while(pos < tabela.size() && !tabela.get(pos).getToken().equals("tk_ponto_virgula"))
            pilha.push(tabela.get(pos++).getToken());
        
        if(pos < tabela.size())
        {
            pilha.push(tabela.get(pos++).getToken());
            if(pilha.getTl() < 3 || pilha.getTl() == 4) //Está errado.
            {
                if(pilha.pop().equals("tk_ponto_virgula"))
                    tabela.get(pos - 1).setLogEstado("[Erro]: Está faltando parâmetros na declaração!", false);
                else
                    tabela.get(auxpos).setLogEstado("[Erro]: Operação inválida!", false);
            }
            else if(pilha.getTl() == 3) //Deve ser uma simples declaração.
            {
                //Fazer a validação ser a variável já foi declarada anteriormente.
                
                if(!pilha.pop().equals("tk_ponto_virgula"))
                    tabela.get(pos - 1).setLogEstado("[Erro]: Ponto e vírgula declarado incorretamente!", false);
                if(!pilha.pop().equals("identificador"))
                    tabela.get(pos - 2).setLogEstado("[Erro]: Identificador declarado incorretamente!", false);
                pilha.pop();
            }
            else if(pilha.getTl() == 5) //Deve ser uma declaração e uma atribuição.
            {
                if(!pilha.pop().equals("tk_ponto_virgula"))
                    tabela.get(pos - 1).setLogEstado("[Erro]: Ponto e vírgula declarado incorretamente!", false);
                String valor = pilha.pop();
                
                if(!pilha.pop().equals("tk_atribuicao"))
                    tabela.get(pos - 3).setLogEstado("[Erro]: Atribuição inválida!", false);
                
                String var = pilha.pop();
                String tipo = pilha.pop();
                
                validarValorTipo(tipo, var, valor, pos);
                
            }
            else //Deve ser uma declaração seguida de uma expressão aritmética.
            {
                if(!pilha.pop().equals("tk_ponto_virgula"))
                    tabela.get(pos - 1).setLogEstado("[Erro]: Ponto e vírgula declarado incorretamente!", false);
                
                
                int j = 2;
                String valor = pilha.pop();
                Pilha pinv = new Pilha();
                while(!pilha.isEmpty() && !valor.equals("tk_atribuicao"))
                {
                    pinv.push(valor);
                    valor = pilha.pop();
                    j++;
                }
                
                if(!pilha.isEmpty())
                {
                    if(!valor.equals("tk_atribuicao"))
                        tabela.get(pos - j).setLogEstado("[Erro]: Atribuição inválida!", false);
                    
                    if(!pilha.pop().equals("identificador"))
                        tabela.get(pos - j - 1).setLogEstado("[Erro]: Identificador declarado incorretamente!", false);
                    
                    pilha.pop();
                    validarExpressaoMatematica(pinv, j);
                }
                else
                {
                    tabela.get(auxpos).setLogEstado("[Erro]: Operação inválida!", false);
                }
            }
            
            
            //Pode ser uma declaração e uma atribuição.
            //Pode ser uma declaração seguida de uma expressão matemática. 
                //cuidado para(*=, /=, +=, -= e %=)
        }
        else
        {
            tabela.get(auxpos).setLogEstado("[Erro]: Operação inválida!", false);
            pos--;
        }
        return pos;
    }

    private void validarValorTipo(String tipo, String var, String valor, int pos) {
        if(validarDeclaracaoVar(var)) //Fazer essa função na análise semântica
        {
            if(tipo.equals("tk_tipo_int"))
            {
                if(valor.equals("identificador"))
                {
                    //validar identificador.
                    //validarVariavel("tk_tipo_int", pos - 2);
                }
                else if(!(valor.equals("valor_decimal") || valor.equals("valor_octal") || valor.equals("valor_hexadecimal")))
                    tabela.get(pos - 2).setLogEstado("[Erro]: O valor não condiz com o tipo declarado! Insira um valor inteiro!", false);

            }
            else if(tipo.equals("tk_tipo_char"))
            {
                if(valor.equals("identificador"))
                {
                    //validar identificador.
                    //validarVariavel("tk_tipo_char", pos - 2);
                }
                else if(!valor.equals("valor_char"))
                    tabela.get(pos - 2).setLogEstado("[Erro]: O valor não condiz com o tipo declarado! Insira um caracter entre ''!", false);
            }
            else if(tipo.equals("tk_tipo_double"))
            {
                if(valor.equals("identificador"))
                {
                    //validar identificador.
                    //validarVariavel("tk_tipo_double", pos - 2);
                }
                else if(!(valor.equals("valor_double") || valor.equals("valor_decimal")))
                    tabela.get(pos - 2).setLogEstado("[Erro]: O valor não condiz com o tipo declarado! Insira um valor inteiro ou de ponto flutante!", false);
            }
            else if(tipo.equals("tk_tipo_bool"))
            {
                if(valor.equals("identificador"))
                {
                    //validar identificador.
                    //validarVariavel("tk_tipo_bool", pos - 2);
                }
                else if(!(valor.equals("tk_afirmacao_false") || valor.equals("tk_afirmacao_true")))
                    tabela.get(pos - 2).setLogEstado("[Erro]: O valor não condiz com o tipo declarado! Insira um valor booleano!", false);
            }
            else if(tipo.equals("tk_tipo_string"))
            {
                if(valor.equals("identificador"))
                {
                    //validar identificador.
                    //validarVariavel("tk_tipo_string", pos - 2);
                }
                else if(!valor.equals("valor_string"))
                    tabela.get(pos - 2).setLogEstado("[Erro]: O valor não condiz com o tipo declarado! Insira um ou mais caracteres entre \"\"", false);
            }
            else //Erro de tipo
            {
                tabela.get(pos - 4).setLogEstado("[Erro]: Tipo de variável declarado incorretamente!", false);
            }
        }
        else //Erro na declaração de variável
        {
            tabela.get(pos - 3).setLogEstado("[Erro]: A variável já foi declarada anteriormente!", false);
        }
        
    }

    
    //Análise Semântica
    private void validarVariavel(String tipo, int pos) 
    {
        String var = tabela.get(pos).getPalavra();
    }

    //Análise Semântica
    private boolean validarDeclaracaoVar(String var) {
        return true;
    }

    private void validarExpressaoMatematica(Pilha pinv, int j) {
        System.out.println("Expressão matemática!");
    }
}
    
    
