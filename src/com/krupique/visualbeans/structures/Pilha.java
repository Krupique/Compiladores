/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krupique.visualbeans.structures;

import java.util.ArrayList;

/**
 *
 * @author henrique
 */
public class Pilha {
    private int tl;
    private ArrayList<String> token;
    
    public Pilha(){
        tl = 0;
        token = new ArrayList<>();
    }
    
    public void push(String str){
        tl++;
        token.add(str);
    }
    
    public String pop(){
        return token.get(--tl);
    }
    
    public boolean isEmpty(){
        return tl == 0;
    }
    
    public int getTl(){
        return tl;
    }
    
    public String getTopPilha(){
        return token.get(tl - 1);
    }
}
