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
        
        String str = texto;
        str = texto.replaceAll("\\@{1}.*\\@{1}", ""); //Remover comentarios
        str = str.replaceAll("\t", " ");
        str = str.replaceAll("\\s+", " ");
        str = str.replaceAll("\\s*\\℡+", "℡");
        str = str.replaceAll("\\℡+", " ℡");
        str = str.replaceAll("\\s+", " ");
        str += " ;";
        System.out.println("REGEX: " + str);
        String res = "";
        String ant = "";
        String aux = "";
        int l = 1, c = 1;
        
        for (int i = 0; i < str.length(); i++) {
            ant = aux;
            aux += str.charAt(i);
            
            if(!tokens.contemToken(aux) && !isLetra(aux, aux.length() - 1) && aux.charAt(aux.length() - 1) != '℡' && aux.charAt(0) != ' ')
            {
                if(tokens.buscaToken(ant))
                {
                    tabela.add(new TabelaTokens(ant, tokens.buscarToken(ant), "valido", l, c, true));
                    res += ant + " " + tokens.buscarToken(ant) + "\n";
                    i = i - (aux.length() - ant.length());
                    if(str.charAt(i + 1) == ' ')
                        i++;
                    
                    aux = "";
                }
                else
                {
                    aux = str.charAt(i) + "";
                    while(i < str.length() && (!tokens.contemSimbolo(aux) || (i > 2 && (aux.equals("+") || aux.equals("-")) && ant.charAt(ant.length() - 1) == 'E' && isNumber(ant.charAt(ant.length() - 2)) )) && !aux.equals(" "))
                    {
                        ant += str.charAt(i);
                        i++;
                        aux = str.charAt(i) + "";
                    }
                    if(aux.equals(" ")){
                        aux = "";
                    }
                    //GUARDAR IDENTIFICADOR
                    tabela.add(new TabelaTokens(ant, tratarIdentificador(ant), "valido", l, c, true));
                    res += ant + " " + tratarIdentificador(ant) + "\n";
                    
                }
            }
            else if(aux.contains("℡"))
            {
                aux = ant;
                c = 1;
                l++;
            }
            else if(aux.charAt(0) == ' ')
            {
                aux = "";
            }
            else
            {
                //System.out.println("TRUE");
            }
        }
        
        System.out.println("Res:\n" + res);
        
        obj[0] = res;
        obj[1] = tabela;
        return obj;
    }
    
    private String tratarIdentificador(String id)
    {
        int tipo;
        int i;
        id = id.replace("℡", "");
        char aux = id.charAt(0);
        boolean flag;
        
        if(aux > 47 && aux < 58 && id.length() > 1)
        {
            //Validar double
            if(id.contains(".") || id.contains("E")) //Pode ser double
            {
                try
                {
                    System.out.println("VALOR: DOUBLE");
                    double db = Double.parseDouble(id);
                    return "valor_double";
                }catch(Exception er)
                {
                    return "invalido";
                }
            }
            else
            {
                try
                {
                    int db = Integer.parseInt(id);
                    System.out.println("VALOR: INT");
                    return "valor_decimal";
                }catch(Exception er)
                {
                    return "invalido";
                }
            }
        }
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
            else if(isIdentificador(id))
                return "identificador";
        
            return "invalido";
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
            else if(isIdentificador(id))
                return "identificador";
        
            return "invalido";
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
        else if(isIdentificador(id))
            return "identificador";
        
        return "invalido";
    }
    
    private boolean isIdentificador(String str)
    {
        char aux = str.charAt(0);
        if((aux > 64 && aux < 91) || (aux > 96 && aux < 123))
        {
            for (int i = 1; i < str.length(); i++) {
                aux = str.charAt(i);
                if(!((aux > 64 && aux < 91) || (aux > 96 && aux < 123) || (aux > 47 && aux < 58)))
                    return false;
            }
            return true;
        }
        return false;
    }

    private boolean isLetra(String aux, int i) {
        if((aux.charAt(i) > 64 && aux.charAt(i) < 91) || (aux.charAt(i) > 96 && aux.charAt(i) < 123))
            return true;
        return false;
    }

    private boolean isNumber(char letra) {
        if(letra > 47 && letra < 58)
            return true;
        return false;
    }
}
