package com.krupique.visualbeans.analysis;

import com.krupique.visualbeans.sintese.GeradorCodigoIntermed;
import com.krupique.visualbeans.sintese.OtimizadorCod;
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
    private ArrayList<TabelaTokens> tabelaSintatica;
    private ArrayList<TabelaTokens> tabelaSemantica;
    private ArrayList<TabelaTokens> tabelaCodIntermed;
    private ArrayList<TabelaTokens> tabelaRes;
    
    public Sintatica(String codigo){
        this.codigo = codigo;
    }
    
    public Object[] analisar()
    {   
        Object[] obj = new Object[5];
        Object[] objaux;
        
        String aux = codigo.replaceAll("\n", " ℡ "); //Substitui todos os "\n" por ℡ (Serve para a análise léxica).
        codigo = codigo.replaceAll("\\s*\n+", "\n");//.replaceAll(" +", " ");//.replaceAll("\t", "      "); //Substitui todas as ocorrências de um ou mais "\n" por um único "\n", depois remove todos os espaços extras e por fim substitui todos os tabs por 6 espaços.
        
        Lexica lexica = new Lexica(aux);
        objaux = lexica.gerarAnalise(); //Faz a análise léxica
        
        tabela = (ArrayList<TabelaTokens>)objaux[1];
        
        tabela = resolveAttrib();
        identificarProgram(); //Faz a análise sintática.
        tabelaSintatica = atribTabela(tabela);
        
        Semantica semantica = new Semantica(tabela);
        tabela = semantica.validar();
        tabela = semantica.removerInuteis();
        tabelaSemantica = semantica.somenteVariaveis();
        
        boolean erro = false;
        for (int j = 0; j < tabela.size() && !erro; j++) {
            if(tabela.get(j).getEstado() != 0)
                erro = true;
        }
        
        tabelaCodIntermed = new ArrayList<>();
        if(!erro) //Nenhuma das análises pode estar errado
        {
            GeradorCodigoIntermed intermed = new GeradorCodigoIntermed(tabela);
            tabela = intermed.getResfinal();
            tabelaCodIntermed = atribTabela(tabela);
            
            OtimizadorCod otim = new OtimizadorCod(tabela);
            
        }
        
        
        
        //Empacota os dados em um vetor de objetos. Famoso MVC.
        obj[0] = objaux[0]; //Texto léxico.
        obj[1] = tabelaSintatica; //Tabela com a informação sobre todos os tokens.
        obj[2] = codigo; //Texto formatado. (Foram removidos os espaços e \n's sobressalente)
        /*Fazer isso depois */
        obj[3] = tabelaSemantica; //Lista com todas as variáveis. (Serve para a análise semântica).
        obj[4] = tabelaCodIntermed; //Lista com todas as variáveis. (Serve para a análise semântica).
        return obj;
    }
    
    private ArrayList<TabelaTokens> atribTabela(ArrayList<TabelaTokens> tab)
    {
        ArrayList<TabelaTokens> temp = new ArrayList<>();
        for (int j = 0; j < tab.size(); j++) {
            temp.add(new TabelaTokens(tab.get(j)));
        }
        
        return temp;
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
                    tabela.get(0).setLogEstado("[Erro]: Declaração de programa inválida!", 1);
                
                if(!aux.pop().equals("identificador"))
                    tabela.get(1).setLogEstado("[Erro]: Declaração de nome de programa inválida!", 1);
                
                if(!aux.pop().equals("tk_ponto_virgula"))
                    tabela.get(2).setLogEstado("[Erro]: Ponto e vírgula inválido!", 1);
            } 
            else if(aux.getTl() < 3)
                tabela.get(0).setLogEstado("[Erro]: Está faltando parâmetros na declaração!", 1);
            else
                tabela.get(0).setLogEstado("[Erro]: Parâmetros excedentes na declaração!", 1);
            /**/
            
            identificarBloco(i);
        }
        else
            tabela.get(0).setLogEstado("[Erro]: Declaração inválida!", 1);
        
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
                tabela.get(pos).setLogEstado("[Erro]: Abrir chaves declarado incorretamente!", 1);
            }

            //Verificar o pos...
            pos = identificarStatement(pos);
        
            if(pos >= tabela.size())
            {
                pos--;
                tabela.get(pos).setLogEstado("[Erro]: Não foi declarado fechar chaves!", 1);
            }
            else
            {
                pilha.push(tabela.get(pos++).getToken());
                if(pilha.getTl() < 2)
                    tabela.get(auxpos).setLogEstado("[Erro]: Você deveria ter declarado abrir chaves!", 1);
                if(!pilha.pop().equals("tk_fechar_chaves"))
                    tabela.get(pos - 1).setLogEstado("[Erro]: Fechar chaves declarado incorretamente!", 1);            
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
            tabela.get(pos++).setLogEstado("[Erro]: Operação desconhecida!", 1);
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
            tabela.get(pos++).setLogEstado("[Erro]: Operação desconhecida!", 1);
        }
        
        //Valida para ver se nao tem nada pra baixo.
        int prox = isStatOrBlock(pos);
        if(prox == 0)
            pos = identificarBloco(pos);
        else if(prox == 1)
            pos = identificarStatement(pos);
        else if(prox == -1) //Achou coisas que não fazem sentido
        {
           tabela.get(pos++).setLogEstado("[Erro]: Operação desconhecida!", 1);
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
                    tabela.get(pos - 1).setLogEstado("[Erro]: Declaração de ponto vírgula inválido!", 1);
                
                if(flag == 1) //Pre incremento/decremento
                {
                    if(!pilha.pop().equals("identificador"))
                        tabela.get(pos - 2).setLogEstado("[Erro]: Declaração de identificador inválido!", 1);
                    String auxp = pilha.pop();
                    if(!(auxp.equals("tk_inc") || auxp.equals("tk_dec")))
                        tabela.get(pos - 3).setLogEstado("[Erro]: Declaração de operador (++|--) inválido!", 1);
                }
                else if(flag == 2) //Post incremento/decremento
                {
                    String auxp = pilha.pop();
                    if(!(auxp.equals("tk_inc") || auxp.equals("tk_dec")))
                        tabela.get(pos - 2).setLogEstado("[Erro]: Declaração de operador (++|--) inválido!", 1);
                    if(!pilha.pop().equals("identificador"))
                        tabela.get(pos - 3).setLogEstado("[Erro]: Declaração de identificador inválido!", 1);
                }
                
                
                String var = tabela.get(pos - 3).getPalavra();
                if(foiDeclarado(var, pos - 4, false)){ //Análise Semântica
                    tabela.get(pos - 3).setTipoValOri(tabela.get(pos - 3).getPalavra(), "--", "uso");
                    procuraValor(var, pos - 3);
                }
                else
                    tabela.get(pos - 3).setLogEstado("[Erro]: A variável "+tabela.get(pos - 3).getPalavra()+" não foi declarada nesse escopo!", 2);
                
                
            }
            else if(pilha.getTl() > 3)
                tabela.get(pos - 3).setLogEstado("[Erro]: Parâmetros excedentes na operação (++V|--V)!", 1);
            else
                tabela.get(pos - 3).setLogEstado("[Erro]: Está faltando parâmetros na operação (++V|--V)!", 1);
        }
        else
        {
            tabela.get(auxpos).setLogEstado("[Erro]: Operação inválida!", 1);
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
                    tabela.get(pos - 1).setLogEstado("[Erro]: Está faltando parâmetros na declaração!", 1);
                else
                    tabela.get(auxpos).setLogEstado("[Erro]: Operação inválida!", 1);
            }
            else if(pilha.getTl() == 3) //Deve ser uma simples declaração.
            {
                //Fazer a validação ser a variável já foi declarada anteriormente.
                String var = tabela.get(pos - 2).getPalavra();
                if(!foiDeclarado(var, pos - 3, true)){ //Análise Semântica
                    tabela.get(pos - 2).setTipoValOri(tabela.get(pos - 3).getPalavra(), "--", "declaracao");
                }
                else
                    tabela.get(pos - 2).setLogEstado("[Erro]: A variável "+tabela.get(pos - 2).getPalavra()+" já foi declarada nesse escopo!", 2);
                
                if(!pilha.pop().equals("tk_ponto_virgula"))
                    tabela.get(pos - 1).setLogEstado("[Erro]: Ponto e vírgula declarado incorretamente!", 1);
                if(!pilha.pop().equals("identificador"))
                    tabela.get(pos - 2).setLogEstado("[Erro]: Identificador declarado incorretamente!", 1);
                pilha.pop();
            }
            else if(pilha.getTl() == 5) //Deve ser uma declaração e uma atribuição.
            {
                String var = tabela.get(pos - 4).getPalavra();
                if(!foiDeclarado(var, pos - 5, true)){
                    tabela.get(pos - 4).setTipoValOri(tabela.get(pos - 5).getPalavra(), "--", "declaracao");
                } 
                else
                    tabela.get(pos - 4).setLogEstado("[Erro]: A variável "+tabela.get(pos - 4).getPalavra()+" já foi declarada nesse escopo!", 2);
                
                if(!pilha.pop().equals("tk_ponto_virgula"))
                    tabela.get(pos - 1).setLogEstado("[Erro]: Ponto e vírgula declarado incorretamente!", 1);
                String valor = pilha.pop();
                
                if(!pilha.pop().equals("tk_atribuicao"))
                    tabela.get(pos - 3).setLogEstado("[Erro]: Atribuição inválida!", 1);
                
                String ident = pilha.pop();
                if(!ident.equals("identificador"))
                    tabela.get(pos - 4).setLogEstado("[Erro]: Identificador inválido!", 1);
                
                String tipo = pilha.pop();
                validarValorTipo(tipo, ident, valor, pos);
                
            }
            else //Deve ser uma declaração seguida de uma expressão aritmética.
            {
                if(!pilha.pop().equals("tk_ponto_virgula"))
                    tabela.get(pos - 1).setLogEstado("[Erro]: Ponto e vírgula declarado incorretamente!", 1);
                
                
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
                        tabela.get(pos - j).setLogEstado("[Erro]: Atribuição inválida!", 1);
                    
                    String auxstr = pilha.pop();
                    if(!auxstr.equals("identificador"))
                        tabela.get(pos - j - 1).setLogEstado("[Erro]: Identificador declarado incorretamente!", 1);
                    
                    String tipo;
                    if(pilha.getTl() == 1)
                        tipo = pilha.pop();
                    else if(pilha.getTl() > 1)
                    {
                        int k = 0;
                        while(pilha.getTl() > 1)
                        {
                            tabela.get(pos - j - k++).setLogEstado("[Erro]: Parâmetros exdentes na operação!", 1);
                            pilha.pop();
                        }
                        tipo = pilha.pop();
                    }
                    else
                        tipo = auxstr;
                    
                    String var = tabela.get(auxpos + 1).getPalavra();
                    if(!foiDeclarado(var, auxpos, true)){ //Análise Semântica
                        tabela.get(auxpos + 1).setTipoValOri(tabela.get(auxpos).getPalavra(), "exp", "declaracao");
                    }
                    else
                        tabela.get(auxpos + 1).setLogEstado("[Erro]: A variável "+tabela.get(auxpos + 1).getPalavra()+" já foi declarada nesse escopo!", 2);
                    validarExpressaoMatematica(pinv, pos - j + 1, tipo, 4);
                }
                else
                {
                    tabela.get(auxpos).setLogEstado("[Erro]: Operação inválida! Não foi encontrado o comando atribuição!", 1);
                }
            }
            int cu = 10;
            int buce = cu + 1;
            
            //Pode ser uma declaração e uma atribuição.
            //Pode ser uma declaração seguida de uma expressão matemática. 
                //cuidado para(*=, /=, +=, -= e %=)
        }
        else
        {
            tabela.get(auxpos).setLogEstado("[Erro]: Operação inválida! Era esperado ponto e vírgula no final da operação!", 1);
            pos--;
        }
        return pos;
    }

    private void validarValorTipo(String tipo, String var, String valor, int pos) 
    {
        if(tipo.equals("tk_tipo_int"))
        {
            if(valor.equals("identificador"))
            {
                System.out.println("Entrou aqui");
                //validar identificador.
                //validarVariavel("tk_tipo_int", pos - 2);
            }
            else if(!(valor.equals("valor_decimal") || valor.equals("valor_octal") || valor.equals("valor_hexadecimal")))
                tabela.get(pos - 2).setLogEstado("[Erro]: O valor não condiz com o tipo declarado! Insira um valor inteiro!", 2);
            else
                tabela.get(pos - 4).setTipoVal(tabela.get(pos - 5).getPalavra(), tabela.get(pos - 2).getPalavra());
        }
        else if(tipo.equals("tk_tipo_char"))
        {
            if(valor.equals("identificador"))
            {
                //validar identificador.
                //validarVariavel("tk_tipo_char", pos - 2);
            }
            else if(!valor.equals("valor_char"))
                tabela.get(pos - 2).setLogEstado("[Erro]: O valor não condiz com o tipo declarado! Insira um caracter entre ''!", 2);
            else
                tabela.get(pos - 4).setTipoVal(tabela.get(pos - 5).getPalavra(), tabela.get(pos - 2).getPalavra());

        }
        else if(tipo.equals("tk_tipo_double"))
        {
            if(valor.equals("identificador"))
            {
                //validar identificador.
                //validarVariavel("tk_tipo_double", pos - 2);
            }
            else if(!(valor.equals("valor_double") || valor.equals("valor_decimal")))
                tabela.get(pos - 2).setLogEstado("[Erro]: O valor não condiz com o tipo declarado! Insira um valor inteiro ou de ponto flutante!", 2);
            else
                tabela.get(pos - 4).setTipoVal(tabela.get(pos - 5).getPalavra(), tabela.get(pos - 2).getPalavra());

        }
        else if(tipo.equals("tk_tipo_bool"))
        {
            if(valor.equals("identificador"))
            {
                //validar identificador.
                //validarVariavel("tk_tipo_bool", pos - 2);
            }
            else if(!(valor.equals("tk_afirmacao_false") || valor.equals("tk_afirmacao_true")))
                tabela.get(pos - 2).setLogEstado("[Erro]: O valor não condiz com o tipo declarado! Insira um valor booleano!", 2);
            else
                tabela.get(pos - 4).setTipoVal(tabela.get(pos - 5).getPalavra(), tabela.get(pos - 2).getPalavra());

        }
        else if(tipo.equals("tk_tipo_string"))
        {
            if(valor.equals("identificador"))
            {
                //validar identificador.
                //validarVariavel("tk_tipo_string", pos - 2);
            }
            else if(!valor.equals("valor_string"))
                tabela.get(pos - 2).setLogEstado("[Erro]: O valor não condiz com o tipo declarado! Insira um ou mais caracteres entre \"\"", 2);
            else
                tabela.get(pos - 4).setTipoVal(tabela.get(pos - 5).getPalavra(), tabela.get(pos - 2).getPalavra());

        }
        else //Erro de tipo
        {
            tabela.get(pos - 4).setLogEstado("[Erro]: Tipo de variável declarado incorretamente!", 2);
        }
        
        
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
    private void validarExpressaoMatematica(Pilha pinv, int j, String type, int sub) 
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
                        boolean achou = false;
                        int k = j - sub;
                        while(k > 0 && !achou){
                            if(tabela.get(k).getPalavra().equals(tabela.get(auxj).getPalavra())){
                                //Achou
                                if(tabela.get(k).getValor().equals("--"))
                                    tabela.get(auxj).setLogEstado("[Erro]: A variável " + tabela.get(auxj).getPalavra() + " não possui valor!", 2);
                                else
                                {
                                    tabela.get(auxj).setCatTipValOri(tabela.get(k).getCategoria(), tabela.get(k).getTipo(), tabela.get(k).getValor(), "-");
                                }
                                achou = true;
                            }
                            
                            k--;
                        }
                        if(!achou)
                        {
                            tabela.get(auxj).setLogEstado("[Erro]: A variável " + tabela.get(auxj).getPalavra() + " não foi declarada nesse escopo!", 2);
                        }
                        else
                            tabela.get(auxj).setOrigem("uso");
                        
                        flag = 1;
                    }
                    else{
                        tabela.get(auxj).setLogEstado("[Erro]: Valor inválido! Era esperado abrir parênteses, identificador ou um valor!", 1);
                        
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
                        tabela.get(auxj).setLogEstado("[Erro]: Valor inválido! Era esperado fechar parênteses ou uma operação matemática!", 1);
                       
                    }
                    auxj++;
                }
            }
            if(cont > 0)
                tabela.get(auxj).setLogEstado("[Erro]: Abrir parênteses excedentes na expressão!", 1);
            else if(cont < 0)
                tabela.get(auxj).setLogEstado("[Erro]: Fechar parênteses excedentes na expressão!", 1);
        }
        else
            tabela.get(auxj).setLogEstado("[Erro]: Operação inválida! Impossível realizar expressões com esse tipo de variável (" + type +")!", 2);

        System.out.println("Test");
    }

    private int validarExpressaoAritmetica(int pos) {
        Pilha pilha, pinv;
        pilha = new Pilha();
        pinv = new Pilha();
        String attrib;
        int auxpos = pos, j;
        
        if(!tabela.get(pos).getToken().equals("identificador"))
            tabela.get(pos).setLogEstado("[Erro]: Operação inválida!", 1);
        else
        {
            pos++;
            attrib = tabela.get(pos).getToken();
            
            if(!(attrib.contains("atribuicao")))
                tabela.get(pos).setLogEstado("[Erro]: Atribuição inválida!", 1);
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
                    String var = tabela.get(auxpos).getPalavra();
                    if(foiDeclarado(var, auxpos - 1, false)){ //Análise Semântica
                        int k = auxpos - 1;
                        boolean achou = false;
                        while(k > 0 && !achou){
                            if(tabela.get(k).getPalavra().equals(var) && tabela.get(k).getValor().equals(""))
                            {
                                tabela.get(auxpos).setLogEstado("[Erro]: A variável "+tabela.get(auxpos).getPalavra()+" não possui valor!", 2);
                                achou = true;
                            }else
                            {
                                tabela.get(auxpos).setTipoValOri("--", "exp", "uso");
                                achou = true;
                            }
                                
                                
                            k--;
                        }
                        
                    }
                    else
                        tabela.get(auxpos).setLogEstado("[Erro]: A variável "+tabela.get(auxpos).getPalavra()+" não foi declarada nesse escopo!", 2);
                    
                    validarExpressaoMatematica(pinv, j, tipo, 3);
                }
                else
                {
                    tabela.get(auxpos).setLogEstado("[Erro]: Operação inválida! Era esperado ponto e vírgula no final da operação!", 1);
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
        String token = tabela.get(pos).getToken();
        
        if(token.equals("tk_comando_for"))
        {
           pos = validarFor(pos);
        }
        else if(!token.equals("tk_comando_else"))
        {
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
                    tabela.get(--pos).setLogEstado("[Erro]: Operação inválida!", 1);
                }

            }
            else
            {
                tabela.get(pos - 1).setLogEstado("[Erro]: Erro na declaração de comando! Era esperado abrir parênteses!", 1);
            }
            
        }
        else
        {
            int prox = isStatOrBlock(pos + 1);
            if(prox == 0)
            {
                pos = identificarBloco(++pos);
            }else if(prox == 1)
            {
                if(!tabela.get(pos + 1).equals("tk_comando_else"))
                    pos = identificarStatement(++pos);
                else
                    tabela.get(pos + 1).setLogEstado("[Erro]: Não era esperado um else seguido de outro else!", 1);
            }
            else
                tabela.get(pos++).setLogEstado("[Erro]: Operação de else inválida!", 1);
        }
        return pos;
    }

    private int validarFor(int pos)    
    {
        Pilha p = new Pilha();
        int aux, flag;
        String token = tabela.get(++pos).getToken();
        
        if(token.equals("tk_abrir_parenteses"))
        {
            aux = ++pos;
            while(pos < tabela.size() && !tabela.get(pos).getToken().equals("tk_ponto_virgula"))
                pos++;

            if(pos < tabela.size())//for init
            {
                flag = qualStatement(aux);
                if(flag == 1)
                    aux = validarVariaveis(aux);
                else if(flag == 2)
                    aux = validarExpressaoAritmetica(aux);
                else if(flag == 4)
                    aux = validarIncDec(aux, 1);
                else if(flag == 5)
                    aux = validarIncDec(aux, 2);
                else
                    tabela.get(aux).setLogEstado("[Erro]: Operação inválida no primeiro parâmetro do for!", 1);
            
                if(aux == pos + 1) //primeiro ; do for.
                {
                    aux = ++pos;
                    while(pos < tabela.size() && !tabela.get(pos).getToken().equals("tk_ponto_virgula"))
                        pos++;
                    
                    if(pos < tabela.size()) //Verifica se pode continuar a verificar o for (segunda parte)
                    {
                        pos = validarLogicaFor(aux);
                        if(pos < tabela.size() && tabela.get(pos).getToken().equals("tk_ponto_virgula")) //Verifica se voltou da segunda validação do for.
                        {
                            aux = ++pos;
                            while(pos < tabela.size() && !tabela.get(pos).getToken().equals("tk_ponto_virgula"))
                                pos++;
                            
                            if(pos < tabela.size())
                            {
                                flag = qualStatement(aux);
                                if(flag == 1)
                                    aux = validarVariaveis(aux);
                                else if(flag == 2)
                                    aux = validarExpressaoAritmetica(aux);
                                else if(flag == 4)
                                    aux = validarIncDec(aux, 1);
                                else if(flag == 5)
                                    aux = validarIncDec(aux, 2);
                                else
                                    tabela.get(aux).setLogEstado("[Erro]: Operação inválida no terceiro parâmetro do for!", 1);

                                pos = aux;
                                
                                if(pos < tabela.size() && !tabela.get(pos).getToken().equals("tk_fechar_parenteses"))
                                    tabela.get(pos).setLogEstado("[Erro]: Não foi encontrado a operação fechar parênteses!", 1);
                                
                                return ++pos;
                                
                            }
                            else
                                tabela.get(aux).setLogEstado("[Erro]: Operação de for inválida! Erro no terceiro parâmetro do for!", 1);   
                            
                        }
                        else
                            tabela.get(aux).setLogEstado("[Erro]: Operação de for inválida! Erro no terceiro parâmetro do for!", 1);   
                        
                    }
                    else
                        tabela.get(aux).setLogEstado("[Erro]: Operação de for inválida! Erro no segundo parâmetro do for!", 1);   
                }
            }
            else
                tabela.get(aux).setLogEstado("[Erro]: Operação de for inválida! Erro no primeiro parâmetro do for", 1);
        }
        else
            tabela.get(pos - 1).setLogEstado("[Erro]: Não foi encontrado abrir chaves!", 1);

        return pos;
    }
    
    private int validarLogicaFor(int pos)
    {
        String token;
        int flag = 0;
        int cont = 0;
        int auxj = pos;
        
        
        while(pos < tabela.size() && !tabela.get(pos).getToken().equals("tk_ponto_virgula"))
        {
            token = tabela.get(pos).getToken();
            if(flag == 0)
            {
                if(token.equals("tk_abrir_parenteses"))
                    cont++;
                else if(token.contains("valor")) //é um valor (inteiro, double, string ou char)
                    flag = 1;
                else if(token.equals("identificador"))
                {
                    //Na análise semântica ir na lista de variáveis e validar se:
                        //-> São do mesmo tipo.
                        //-> Se a variável tem valor atribuido a ela.
                    boolean achou = false;
                    int k = pos - 1;
                    while(k > 0 && !achou){
                        if(tabela.get(k).getPalavra().equals(tabela.get(auxj).getPalavra())){
                            //Achou
                            if(tabela.get(k).getValor().equals("--"))
                                tabela.get(auxj).setLogEstado("[Erro]: A variável " + tabela.get(auxj).getPalavra() + " não possui valor!", 2);
                            else
                            {
                                tabela.get(auxj).setCatTipValOri(tabela.get(k).getCategoria(), tabela.get(k).getTipo(), tabela.get(k).getValor(), "-");
                            }
                            achou = true;
                        }

                        k--;
                    }
                    if(!achou)
                    {
                        tabela.get(auxj).setLogEstado("[Erro]: A variável " + tabela.get(auxj).getPalavra() + " não foi declarada nesse escopo!", 2);
                    }
                    else
                        tabela.get(auxj).setOrigem("uso");
                        
                    
                    flag = 1;
                }
                else if(token.equals("tk_afirmacao_true") || token.equals("tk_afirmacao_false"))
                {
                    flag = 1;
                }
                else{
                    tabela.get(pos).setLogEstado("[Erro]: Valor inválido! Era esperado abrir parênteses, identificador ou um valor!", 1);
                    flag = 1;
                }
                pos++;
            }
            else if(flag == 1)
            {
                if(token.equals("tk_fechar_parenteses"))
                    cont--;
                else if(token.equals("tk_and") || token.equals("tk_or") || token.equals("tk_igualdade") || token.equals("tk_diferenca") || token.equals("tk_menor") || token.equals("tk_maior") || token.equals("tk_menor_igual") || token.equals("tk_maior_igual"))
                {
                    flag = 0;
                }
                else{
                    tabela.get(pos).setLogEstado("[Erro]: Valor inválido! Era esperado fechar parênteses ou uma operação matemática!", 1);
                    flag = 0;
                }
                pos++;
            }
        }
        
        if(cont > 0)
            tabela.get(pos).setLogEstado("[Erro]: Abrir parênteses excedentes na expressão!", 1);
        else if(cont < 0)
            tabela.get(pos).setLogEstado("[Erro]: Fechar parênteses excedentes na expressão!", 1);
    
    
        return pos;
    }
    
    private void validarOperacaoLogica(Pilha pilha, int auxj) {
        String var;
        int flag, cont;
        flag = cont = 0;
        int j = auxj;
        
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
                    boolean achou = false;
                    int k = j - 1;
                    while(k > 0 && !achou){
                        if(tabela.get(k).getPalavra().equals(tabela.get(auxj).getPalavra())){
                            //Achou
                            if(tabela.get(k).getValor().equals("--"))
                                tabela.get(auxj).setLogEstado("[Erro]: A variável " + tabela.get(auxj).getPalavra() + " não possui valor!", 2);
                            else
                            {
                                if(!tabela.get(k).getValor().equals("exp"))
                                    tabela.get(auxj).setCatTipValOri(tabela.get(k).getCategoria(), tabela.get(k).getTipo(), tabela.get(k).getValor(), "-");
                                else    
                                    tabela.get(auxj).setCatTipValOri(tabela.get(k).getCategoria(), tabela.get(k).getTipo(), "ope", "-");
                            }
                            achou = true;
                        }

                        k--;
                    }
                    if(!achou)
                    {
                        tabela.get(auxj).setLogEstado("[Erro]: A variável " + tabela.get(auxj).getPalavra() + " não foi declarada nesse escopo!", 2);
                    }
                    else
                        tabela.get(auxj).setOrigem("uso");
                    
                    flag = 1;
                }
                else if(var.equals("tk_afirmacao_true") || var.equals("tk_afirmacao_false"))
                {
                    flag = 2;
                }
                else{
                    tabela.get(auxj).setLogEstado("[Erro]: Valor inválido! Era esperado abrir parênteses, identificador ou um valor!", 1);
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
                    tabela.get(auxj).setLogEstado("[Erro]: Valor inválido! Era esperado fechar parênteses ou uma operação matemática!", 1);
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
                    tabela.get(auxj).setLogEstado("[Erro]: Era esperado um operador de igualdede ou um operador lógico!", 1);
                    flag = 0;
                }

                auxj++;
            }
        }
        if(cont > 0)
            tabela.get(auxj).setLogEstado("[Erro]: Abrir parênteses excedentes na expressão!", 1);
        else if(cont < 0)
            tabela.get(auxj).setLogEstado("[Erro]: Fechar parênteses excedentes na expressão!", 1);
    }

    private boolean foiDeclarado(String var, int pos, boolean flag) {
        String aux;
        for (int j = 0; j < pos; j++) {
            aux = tabela.get(j).getPalavra();
            if(aux.equals(var))
            {
                if(flag)
                    tabela.get(pos + 1).setLogEstado("[Erro] Esta variável já foi declarada anteriormente!", 2);
                return true;
            }
        }
        
        return false;
    }
    
    private boolean procuraValor(String var, int pos)
    {
        boolean achou = false;
        int k = pos - 1;
        while(k > 0 && !achou){
            if(tabela.get(k).getPalavra().equals(tabela.get(pos).getPalavra())){
                //Achou
                if(tabela.get(k).getValor().equals("--"))
                    tabela.get(pos).setLogEstado("[Erro]: A variável " + tabela.get(pos).getPalavra() + " não possui valor!", 2);
                else
                {
                    tabela.get(pos).setCatTipValOri(tabela.get(k).getCategoria(), tabela.get(k).getTipo(), tabela.get(k).getValor(), "-");
                }
                achou = true;
            }

            k--;
        }
        if(!achou)
        {
            tabela.get(pos).setLogEstado("[Erro]: A variável " + tabela.get(pos).getPalavra() + " não foi declarada nesse escopo!", 2);
        }
        else{
            tabela.get(pos).setOrigem("uso");
        }
        
        return achou;
        
    }
    
    private ArrayList<TabelaTokens> resolveAttrib()
    {
        ArrayList<TabelaTokens> temp = new ArrayList<>();
        int i = 0;
        while (i < tabela.size())
        {
            if(tabela.get(i).getToken().equals("tk_atribuicao_add"))
            {
                temp.add(new TabelaTokens("=", "tk_atribuicao", tabela.get(i).getLog(),
                        tabela.get(i).getLinha(), tabela.get(i).getColuna(), tabela.get(i).getEstado()));
                temp.add(new TabelaTokens(tabela.get(i - 1).getTudo()));
                temp.add(new TabelaTokens("+", "tk_add", tabela.get(i).getLog(), 
                        tabela.get(i).getLinha(), tabela.get(i).getColuna(), tabela.get(i).getEstado()));
            }
            
            else if(tabela.get(i).getToken().equals("tk_atribuicao_sub"))
            {
                temp.add(new TabelaTokens("=", "tk_atribuicao", tabela.get(i).getLog(),
                        tabela.get(i).getLinha(), tabela.get(i).getColuna(), tabela.get(i).getEstado()));
                temp.add(new TabelaTokens(tabela.get(i - 1).getTudo()));
                temp.add(new TabelaTokens("-", "tk_sub", tabela.get(i).getLog(), 
                        tabela.get(i).getLinha(), tabela.get(i).getColuna(), tabela.get(i).getEstado()));
            }
            
            else if(tabela.get(i).getToken().equals("tk_atribuicao_resto"))
            {
                temp.add(new TabelaTokens("=", "tk_atribuicao", tabela.get(i).getLog(),
                        tabela.get(i).getLinha(), tabela.get(i).getColuna(), tabela.get(i).getEstado()));
                temp.add(new TabelaTokens(tabela.get(i - 1).getTudo()));
                temp.add(new TabelaTokens("%", "tk_resto", tabela.get(i).getLog(), 
                        tabela.get(i).getLinha(), tabela.get(i).getColuna(), tabela.get(i).getEstado()));
            }
            
            else if(tabela.get(i).getToken().equals("tk_atribuicao_div"))
            {
                temp.add(new TabelaTokens("=", "tk_atribuicao", tabela.get(i).getLog(),
                        tabela.get(i).getLinha(), tabela.get(i).getColuna(), tabela.get(i).getEstado()));
                temp.add(new TabelaTokens(tabela.get(i - 1).getTudo()));
                temp.add(new TabelaTokens("/", "tk_div", tabela.get(i).getLog(), 
                        tabela.get(i).getLinha(), tabela.get(i).getColuna(), tabela.get(i).getEstado()));
            }
            
            else if(tabela.get(i).getToken().equals("tk_atribuicao_mult"))
            {
                temp.add(new TabelaTokens("=", "tk_atribuicao", tabela.get(i).getLog(),
                        tabela.get(i).getLinha(), tabela.get(i).getColuna(), tabela.get(i).getEstado()));
                temp.add(new TabelaTokens(tabela.get(i - 1).getTudo()));
                temp.add(new TabelaTokens("*", "tk_mult", tabela.get(i).getLog(), 
                        tabela.get(i).getLinha(), tabela.get(i).getColuna(), tabela.get(i).getEstado()));
            }
            else
            {
                TabelaTokens aux = new TabelaTokens(tabela.get(i).getPalavra(), tabela.get(i).getToken(), 
                        tabela.get(i).getLog(), tabela.get(i).getLinha(), tabela.get(i).getColuna(), 
                        tabela.get(i).getEstado(), tabela.get(i).getCategoria(), tabela.get(i).getTipo(),
                        tabela.get(i).getValor(), tabela.get(i).getOrigem());
                temp.add(aux);
            }
            i++;
        }
        
        
        return temp;
    }
                            
}