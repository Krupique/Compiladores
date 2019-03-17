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
        Object[] obj = new Object[4];
        Object[] objaux;
        listVariaveis = new ArrayList<ListaVariaveis>();
        
        String aux = codigo.replaceAll("\n", " ℡ "); //Substitui todos os "\n" por ℡ (Serve para a análise léxica).
        codigo = codigo.replaceAll("\\s*\n+", "\n");//.replaceAll(" +", " ");//.replaceAll("\t", "      "); //Substitui todas as ocorrências de um ou mais "\n" por um único "\n", depois remove todos os espaços extras e por fim substitui todos os tabs por 6 espaços.
        
        Lexica lexica = new Lexica(aux);
        objaux = lexica.gerarAnalise(); //Faz a análise léxica
        
        tabela = (ArrayList<TabelaTokens>)objaux[1];
        for (int j = 0; j < tabela.size(); j++) {
            if(tabela.get(j).getToken().equals("identificador"))
                listVariaveis.add(new ListaVariaveis(tabela.get(j).getPalavra()));
        }
        
        identificarProgram(); //Faz a análise sintática.
        
        //Empacota os dados em um vetor de objetos. Famoso MVC.
        obj[0] = objaux[0]; //Texto léxico.
        obj[1] = tabela; //Tabela com a informação sobre todos os tokens.
        obj[2] = codigo; //Texto formatado. (Foram removidos os espaços e \n's sobressalente)
        obj[3] = listVariaveis; //Lista com todas as variáveis. (Serve para a análise semântica).
        return obj;
    }
    
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
        //Valida pra ver se nao tem nada pra baixo.
        int prox = isStatOrBlock(pos);
        if(prox == 0)
            pos = identificarBloco(pos);
        else if(prox == 1)
            pos = identificarStatement(pos);
        else if(prox == -1)
        {
            tabela.get(pos++).setLogEstado("[Erro]: Operação desconhecida!", false);
            pos = identificarStatement(pos);
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
            pos = validarExpressaoAritmetica(pos);
        }
        else if(flag == 3) //Expressão comando (if, else, for ou while)
        {
            System.out.println("Expressão comando");
            pos = validarComando(pos);
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
        else if(!tabela.get(pos).getToken().equals("tk_fechar_chaves"))//ERRO
        {
            tabela.get(pos++).setLogEstado("[Erro]: Operação desconhecida!", false);
        }
        
        //Valida para ver se nao tem nada pra baixo.
        int prox = isStatOrBlock(pos);
        if(prox == 0)
            pos = identificarBloco(pos);
        else if(prox == 1)
            pos = identificarStatement(pos);
        else if(prox == -1) //Achou coisas que não fazem sentido
        {
           tabela.get(pos++).setLogEstado("[Erro]: Operação desconhecida!", false);
           pos = identificarStatement(pos);
        }
        
        return pos;
    }
    
    private int isStatOrBlock(int pos)
    {
        if(pos < tabela.size())
        {
            if(tabela.get(pos).getToken().equals("tk_abrir_chaves"))
                return 0; //É bloco
            else if(qualStatement(pos) != -1)
                return 1; //É statement
            else if(tabela.get(pos).getToken().equals("tk_fechar_chaves"))
                return 2; //É fechar chaves
            return -1;
        }
        return -2;
    }

    private int qualStatement(int pos)
    {
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
                if(!var.equals("identificador"))
                    tabela.get(pos - 4).setLogEstado("[Erro]: Identificador inválido!", false);
                
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
                while(!pilha.isEmpty() && !valor.equals("tk_atribuicao")) //Inverte a pilha.
                {
                    pinv.push(valor);
                    valor = pilha.pop();
                    j++;
                }
                
                if(!pilha.isEmpty())
                {
                    if(!valor.equals("tk_atribuicao"))
                        tabela.get(pos - j).setLogEstado("[Erro]: Atribuição inválida!", false);
                    
                    String auxstr = pilha.pop();
                    if(!auxstr.equals("identificador"))
                        tabela.get(pos - j - 1).setLogEstado("[Erro]: Identificador declarado incorretamente!", false);
                    
                    String tipo;
                    if(pilha.getTl() == 1)
                        tipo = pilha.pop();
                    else if(pilha.getTl() > 1)
                    {
                        int k = 0;
                        while(pilha.getTl() > 1)
                        {
                            tabela.get(pos - j - k++).setLogEstado("[Erro]: Parâmetros exdentes na operação!", false);
                            pilha.pop();
                        }
                        tipo = pilha.pop();
                    }
                    else
                        tipo = auxstr;
                    validarExpressaoMatematica(pinv, pos - j + 1, tipo);
                }
                else
                {
                    tabela.get(auxpos).setLogEstado("[Erro]: Operação inválida! Não foi encontrado o comando atribuição!", false);
                }
            }
            
            
            //Pode ser uma declaração e uma atribuição.
            //Pode ser uma declaração seguida de uma expressão matemática. 
                //cuidado para(*=, /=, +=, -= e %=)
        }
        else
        {
            tabela.get(auxpos).setLogEstado("[Erro]: Operação inválida! Era esperado ponto e vírgula no final da operação!", false);
            pos--;
        }
        return pos;
    }

    private void validarValorTipo(String tipo, String var, String valor, int pos) 
    {
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
        //Serve para validar se a variável pode receber outra variável.
            //->Se são do mesmo tipo.
            //->Se a variável possui valor.
        String var = tabela.get(pos).getPalavra();
    }

    //Análise Semântica
    private boolean validarDeclaracaoVar(String var) 
    {
        //Serve para validar se a variável já não foi declarada anteriormente.
        return true;
    }
    
    //Pega o tipo e retorna o valor daquele tipo. EX: tk_tipo_int -> valor_decimal
    private String validarTipo(String tipo)
    {
        if(tipo.equals("tk_tipo_int"))
            return "valor_decimal";
        else if(tipo.equals("tk_tipo_double"))
            return "valor_double";
        else if(tipo.equals("tk_tipo_char"))
            return "valor_char";
        else if(tipo.equals("tk_tipo_string"))
            return "valor_string";
    
        return "inválido";
    }

    //Verifica se a expressão matemática é valida.
    private void validarExpressaoMatematica(Pilha pinv, int j, String type) 
    {
        int flag, cont, auxj;
        flag = cont = 0;
        auxj = j;
        String var, tipo;
        
        tipo = validarTipo(type);
        
        if(tipo.equals("valor_decimal") || tipo.equals("valor_double"))
        {
            while(!pinv.isEmpty())
            {
                var = pinv.pop();
                if(flag == 0)
                {
                    if(var.equals("tk_abrir_parenteses"))
                        cont++;
                    else if(var.equals(tipo) || var.equals("valor_decimal")) //numero
                        flag = 1;
                    else if(var.equals("identificador"))
                    {
                        //Na análise semântica ir na lista de variáveis e validar se:
                            //-> São do mesmo tipo.
                            //-> Se a variável tem valor atribuido a ela.
                        flag = 1;
                    }
                    else{
                        tabela.get(auxj).setLogEstado("[Erro]: Valor inválido! Era esperado abrir parênteses, identificador ou um valor!", false);
                        
                    }
                    auxj++;
                }
                else if(flag == 1)
                {
                    if(var.equals("tk_fechar_parenteses"))
                        cont--;
                    else if(var.equals("tk_add") || var.equals("tk_sub") || var.equals("tk_mult") || var.equals("tk_div") || var.equals("tk_resto"))
                        flag = 0;
                    else{
                        tabela.get(auxj).setLogEstado("[Erro]: Valor inválido! Era esperado fechar parênteses ou uma operação matemática!", false);
                       
                    }
                    auxj++;
                }
            }
            if(cont > 0)
                tabela.get(auxj).setLogEstado("[Erro]: Abrir parênteses excedentes na expressão!", false);
            else if(cont < 0)
                tabela.get(auxj).setLogEstado("[Erro]: Fechar parênteses excedentes na expressão!", false);
        }
        else
            tabela.get(auxj).setLogEstado("[Erro]: Operação inválida! Impossível realizar expressões com esse tipo de variável (" + type +")!", true);

        System.out.println("Test");
    }

    private int validarExpressaoAritmetica(int pos) {
        Pilha pilha, pinv;
        pilha = new Pilha();
        pinv = new Pilha();
        String attrib;
        int auxpos = pos, j;
        
        if(!tabela.get(pos).getToken().equals("identificador"))
            tabela.get(pos).setLogEstado("[Erro]: Operação inválida!", false);
        else
        {
            pos++;
            attrib = tabela.get(pos).getToken();
            
            if(!(attrib.contains("atribuicao")))
                tabela.get(pos).setLogEstado("[Erro]: Atribuição inválida!", false);
            else
            {
                pos++;
                j = pos;
                while(pos < tabela.size() && !tabela.get(pos).getToken().equals("tk_ponto_virgula"))
                    pilha.push(tabela.get(pos++).getToken());
                
                if(pos < tabela.size())//Achou ponto e virgula.
                {
                    while(!pilha.isEmpty())
                        pinv.push(pilha.pop());
                    
                    //Validar se identificador tem valor em caso de: (+=, -=, *=, /= e %=)
                    //Validar tipo de identificador
                    String tipo = "tk_tipo_int";
                    validarExpressaoMatematica(pinv, j, tipo);
                }
                else
                {
                    tabela.get(auxpos).setLogEstado("[Erro]: Operação inválida! Era esperado ponto e vírgula no final da operação!", false);
                    pos -= 2;
                }
            }
        }
        pos++;
        return pos;
    }

    private int validarComando(int pos) {
        Pilha pilha = new Pilha();
        Pilha pinv = new Pilha();
        int cont = 0, auxpos;
        String token;
        
        pos++;
        auxpos = pos;
        token = tabela.get(pos).getToken();
        pilha.push(tabela.get(pos++).getToken());
        
        if(token.equals("tk_abrir_parenteses"))
        {
            cont++;
            while(pos < tabela.size() && cont != 0)
            {
                if(tabela.get(pos).getToken().equals("tk_abrir_parenteses"))
                    cont++;
                else if(tabela.get(pos).getToken().equals("tk_fechar_parenteses"))
                    cont--;
            
                pilha.push(tabela.get(pos++).getToken());
            }
            
            if(pos < tabela.size())
            {
                while(!pilha.isEmpty())
                    pinv.push(pilha.pop());
                
                validarOperacaoLogica(pinv, auxpos);
            }
            else
            {
                tabela.get(--pos).setLogEstado("[Erro]: Operação inválida!", false);
            }
            
        }
        else
        {
            tabela.get(pos - 1).setLogEstado("[Erro]: Erro na declaração de comando! Era esperado abrir parênteses!", false);
        }
        return pos;
    }

    private void validarOperacaoLogica(Pilha pilha, int auxj) {
        String var;
        int flag, cont;
        flag = cont = 0;
        
        while(!pilha.isEmpty())
        {
            var = pilha.pop();
            if(flag == 0)
            {
                if(var.equals("tk_abrir_parenteses"))
                    cont++;
                else if(var.contains("valor")) //é um valor (inteiro, double, string ou char)
                    flag = 1;
                else if(var.equals("identificador"))
                {
                    //Na análise semântica ir na lista de variáveis e validar se:
                        //-> São do mesmo tipo.
                        //-> Se a variável tem valor atribuido a ela.
                    flag = 1;
                }
                else if(var.equals("tk_afirmacao_true") || var.equals("tk_afirmacao_false"))
                {
                    flag = 2;
                }
                else{
                    tabela.get(auxj).setLogEstado("[Erro]: Valor inválido! Era esperado abrir parênteses, identificador ou um valor!", false);
                    flag = 1;
                }
                auxj++;
            }
            else if(flag == 1)
            {
                if(var.equals("tk_fechar_parenteses"))
                    cont--;
                else if(var.equals("tk_add") || var.equals("tk_sub") || var.equals("tk_mult") || var.equals("tk_div") || var.equals("tk_resto"))
                    flag = 0;
                else if(var.equals("tk_and") || var.equals("tk_or") || var.equals("tk_igualdade") || var.equals("tk_diferenca") || var.equals("tk_menor") || var.equals("tk_maior") || var.equals("tk_menor_igual") || var.equals("tk_maior_igual"))
                {
                    flag = 0;
                }
                else{
                    tabela.get(auxj).setLogEstado("[Erro]: Valor inválido! Era esperado fechar parênteses ou uma operação matemática!", false);
                    flag = 0;
                }
                auxj++;
            }
            else if(flag == 2)
            {
                if(var.equals("tk_fechar_parenteses"))
                    cont--;
                else if(var.equals("tk_and") || var.equals("tk_or") || var.equals("tk_igualdade") || var.equals("tk_diferenca") || var.equals("tk_menor") || var.equals("tk_maior") || var.equals("tk_menor_igual") || var.equals("tk_maior_igual"))
                    flag = 0;
                else
                {
                    tabela.get(auxj).setLogEstado("[Erro]: Era esperado um operador de igualdede ou um operador lógico!", false);
                    flag = 0;
                }

                auxj++;
            }
        }
        if(cont > 0)
            tabela.get(auxj).setLogEstado("[Erro]: Abrir parênteses excedentes na expressão!", false);
        else if(cont < 0)
            tabela.get(auxj).setLogEstado("[Erro]: Fechar parênteses excedentes na expressão!", false);
    }
}
    
    
