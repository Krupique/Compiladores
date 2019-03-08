package com.krupique.visualbeans.analysis;

import com.krupique.visualbeans.structures.PalavrasSimbolosController;
import com.krupique.visualbeans.structures.TabelaTokens;
import java.util.ArrayList;

/**
 *
 * @author Henrique K. Secchi
 */
public class Lexica {

    private String texto;
    private String analise;
    private ArrayList<TabelaTokens> tabela;
    private PalavrasSimbolosController tokens;
    
    public Lexica(String texto) {
        this.texto = texto;
        tokens = new PalavrasSimbolosController();
        tabela = new ArrayList<TabelaTokens>();
    }

    public boolean validar()
    {
        return false;
    }
    
    public Object[] gerarAnalise()
    {
        Object[] obj = new Object[2];
        String str = texto.replaceAll("\\s+", " ");
        str += " ;";
        System.out.println("STR: " + str);
        String res = "";
        String ant = "";
        String aux = "";
        for (int i = 0; i < str.length(); i++) {
            ant = aux;
            aux += str.charAt(i);
            
            if(!tokens.contemToken(aux))
            {
                System.out.println("FALSE");
                if(tokens.buscaToken(ant))
                {
                    //System.out.println("ACHOU ANT");
                    //GUARDAR ANT NA TABELA DE TOKENS, BUSCAR E TRATAR ANT
                    tabela.add(new TabelaTokens(ant, tokens.buscarToken(ant), "test", 0, 0));
                    res += ant + " " + tokens.buscarToken(ant) + "\n";
                    i = i - (aux.length() - ant.length());
                    if(str.charAt(i + 1) == ' ')
                        i++;
                    
                    aux = "";
                }
                else
                {
                    aux = str.charAt(i) + "";
                    //ant += str.charAt(i);
                    while(i < str.length() && !tokens.contemSimbolo(aux) && !aux.equals(" "))
                    {
                        ant += str.charAt(i);
                        i++;
                        aux = str.charAt(i) + "";
                    }
                    if(aux.equals(" ")){
                        aux = "";
                    }
                    //GUARDAR IDENTIFICADOR
                    tabela.add(new TabelaTokens(ant, tratarIdentificador(ant), "teste", 0, 0));
                    res += ant + " " + tratarIdentificador(ant) + "\n";
                }
            }
            else
            {
                //System.out.println("TRUE");
            }
        }
        
        System.out.println("Res:\n" + res);
        //return str;
        obj[0] = res;
        obj[1] = tabela;
        return obj;
    }
    
    private String tratarIdentificador(String id)
    {
        int tipo;
        int i;
        char aux = id.charAt(0);
        boolean flag;
        
        if(aux > 47 && aux < 58 && id.length() > 1)
        {
            i = 1;
            flag = true;
            while(i < id.length() && flag)
            {
                aux = id.charAt(i);
                if(aux > 57 || aux < 48)
                    flag = false;
                i++;
            }
            if(flag)
                return "valor";
            return "invalido";
        }
        else if(aux == '_')
            return "identificador";
        else if(aux == 'o' && id.length() > 1)
        {
            i = 1;
            flag = true;
            while(i < id.length() && flag)
            {
                aux = id.charAt(i);
                if(aux > 55 || aux < 48)
                    flag = false;
                i++;
            }
            if(flag)
                return "valor_octal";
            return "identificador";
        }
        else if(aux == 'x' && id.length() > 1)
        {
            i = 1;
            flag = true;
            while(i < id.length() && flag)
            {
                aux = id.charAt(i);
                if(!((aux > 47 && aux < 58) || (aux > 96 && aux < 103) || (aux > 64 && aux < 71)))
                    flag = false;
                i++;
            }
            if(flag)
                return "valor_hexadecimal";
            return "identificador";
        }
        else if(aux == '"')
        {
            aux = id.charAt(id.length() - 1);
            if(aux == '"')
                return "valor_string";
            return "string_invalida";
        }
        else if(aux =='\'')
        {
            aux = id.charAt(id.length() - 1);
            if(aux == '\'' && id.length() == 3)
                return "valor_char";
            return "char_invalido";     
        }
        
        return "identificador";
    }
}
