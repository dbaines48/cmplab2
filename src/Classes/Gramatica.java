/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.ArrayList;

/**
 *
 * @author Daniel
 */

public class Gramatica {
    ArrayList<NonTerminal> NTs; 
    Terminal epsilon;
    public ArrayList<Terminal> terminals;
    String[][] tabla_M;
    
    public Gramatica(ArrayList<String> gram){
        NTs = new ArrayList<NonTerminal>();
        epsilon = new Terminal("€",true);
        terminals = getAllTerminals(gram);
        terminals.add(new Terminal("&",true));
        CrearGramatica(gram);
        tabla_M = new String[50][50];
    }
    
    public Terminal findTerminal(String name){
        for (Terminal t : terminals) {
            if(t.name.compareTo(name)==0)
                return t;
        }
        return null;
    }
    
    public ArrayList<Terminal> getAllTerminals(ArrayList<String> lines){
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<Terminal> terminals = new ArrayList<Terminal>();
        String[] part;
        for (String line : lines) {
            part = line.split("->");
            String[] div = part[1].split("\\|");
            for (String d : div) {
                result.add(part[0]+"->"+d);
            }
        }
        
        
        
        for (String res : result) {
            String p = res.split("->")[1];
            for (int i = 0; i < p.length(); i++) {
                String c = p.substring(i,i+1);
                if(!isUpperCase(c) && c.compareTo("&")!=0){
                    
                    if(terminals.indexOf(c)==-1)
                        terminals.add(new Terminal(c,true));
                }
            }
        }
        return terminals;
    }
    
    
    public boolean isUpperCase(String letra){
        return ((int)letra.charAt(0) >= 65 && (int)letra.charAt(0) <=90 ) ? true : false;
    }
    
    public NonTerminal findNT(String name){
        for (NonTerminal nt : NTs) {
            if(nt.name.compareTo(name)==0)
                return NTs.get(NTs.indexOf(nt));
        }
        return null;
    }
    
    public void CrearGramatica(ArrayList<String> gramatica){
        int pos = -1;
        
        //Añadir no terminales primero
        for (String gr : gramatica) {
            pos = -1;
            String[] part = gr.split("->");
            pos = NTs.indexOf(part[0]);
            if(pos == -1)
                NTs.add(new NonTerminal(part[0],false));
        }
        
        //Añadir lado derecho
        for (String gr : gramatica) {
            pos = -1;
            String[] producciones = gr.split("->")[1].split("\\|");
            for (String prod : producciones) {
                Produccion produccion = new Produccion();
                findNT(gr.split("->")[0]).Producciones.add(produccion);
                for (int i = 0; i < prod.length(); i++) {
                    String cre = prod.substring(i,i+1);
                    if(isUpperCase(cre)){ //Es un No Terminal
                        boolean entra = true;
                        int j=i+1;
                        while(j<prod.length() && entra){
                            if(prod.substring(j,j+1).compareTo("'")==0){
                                cre+="'";
                                j++;
                            }else
                                entra = false;
                        }
                        i=j-1;
                        produccion.addSymbol(findNT(cre));
                    }else{ //Es un terminal, o en su defecto una cadena de terminales
                        boolean entra = true;
                        int j=i+1;
                        while(j<prod.length() && entra){
                            if(!isUpperCase(prod.substring(j,j+1))){
                                cre+=prod.substring(j,j+1);
                                j++;
                            }else
                                entra = false;
                        }
                        i=j-1;
                        produccion.addSymbol(cre.compareTo("€")==0 ? epsilon : new Terminal(cre,true));
                    }
                }
            }
            
        }
        System.out.println("ya");
    }
    
    public boolean primHasTer(String name, NonTerminal nter){
        for (Terminal term : nter.Primero) {
            if(term.name.compareTo(name)==0)
                return true;
        }
        return false;
    }
    
    public String[][] HacerTablaM(){
        int filas, columnas;
        tabla_M = new String[50][50];
        filas = NTs.size();
        columnas = terminals.size();
        //llenar en blanco la tabla
        for (int i = 0; i <=filas ; i++) {
            for (int j = 0; j < columnas; j++) {
                tabla_M[i][j] = null;
            }
        }
        
        
        return tabla_M;
    }
}

