package com.krupique.visualbeans.analysis;

import com.krupique.visualbeans.structures.PalavrasSimbolosController;
import com.krupique.visualbeans.structures.TabelaTokens;
import java.util.ArrayList;

/**
 *
 * @author Henrique K. Secchi
 */
public class Lexica {

    String texto;
    ArrayList<TabelaTokens> tabela;
    PalavrasSimbolosController tokens;
    
    public Lexica(String texto) {
        this.texto = texto;
    }

    /*public String analisar()
    {
        for (int i = 0; i < texto.length(); i++) {
            String aux = texto.charAt(i) + "";
            
        }
    }*/
}
