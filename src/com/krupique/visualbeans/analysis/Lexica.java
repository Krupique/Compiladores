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
    }

    public boolean validar()
    {
        return false;
    }
    
    public String gerarAnalise()
    {
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
                    res += ant + ": TOKEN\n";
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
                    res += ant + ": IDENTIFICADOR\n";
                }
            }
            else
            {
                //System.out.println("TRUE");
            }
        }
        
        System.out.println("Res:\n" + res);
        return str;
    }
}
