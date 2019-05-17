/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krupique.visualbeans.sintese;

import java.util.ArrayList;

/**
 *
 * @author Henrique K. Secchi
 */
public class GeradorCodigoIntermed {
    ArrayList<String> _temp;
    
    
    public GeradorCodigoIntermed()
    {
        _temp = new ArrayList<>();
        
    }
    
    
    
    public ArrayList<String> realocarArray(ArrayList<String> original, ArrayList<String> sublista, int pos)
    {
        ArrayList<String> resultado = new ArrayList<>();
        for (int i = 0; i < pos; i++) 
            resultado.add(original.get(i));
        
        for (int i = 0; i < sublista.size(); i++) 
            resultado.add(sublista.get(i));
        
        for (int i = pos; i < original.size(); i++)
            resultado.add(original.get(i));
       
        return resultado;
    }
    
}
