/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import java.util.HashMap;
import java.util.LinkedHashMap;
import rmaster.assets.ModelBase;

/**
 *
 * @author Arbor
 */
public class StalniGost extends ModelBase {
    
    public static String TABLE_NAME = "stalnigost";
    public static String PRIMARY_KEY = "id";
    
    public static String POPUST = "popust";
    public static String NAZIV = "naziv";
    public static String SIFRA = "sifra";
    public static String BLOKIRAN = "blokiran";
    public static String GRUPA_ID = "GRUPA_ID";
    
    public String id;
    public String popust;
    public String naziv;
    
    @Override
    public String getTableName()
    {
        return TABLE_NAME;
    }    
    
    @Override
    public String getPrimaryKeyName()
    {
        return PRIMARY_KEY;
    }
    
    @Override
    public void makeFromHashMap(HashMap<String, String> stalniGostMap) 
    {
        this.id = stalniGostMap.get(PRIMARY_KEY);

        this.naziv = stalniGostMap.get(NAZIV);
               
        this.popust = stalniGostMap.get(POPUST);

    }
    
    @Override
    public LinkedHashMap<String, String> toHashMap(boolean includeId)
    {
        LinkedHashMap<String, String> stalniGostMap = new LinkedHashMap();

        stalniGostMap.put(NAZIV, this.naziv);
        stalniGostMap.put(POPUST, this.popust);
        
        if (includeId) {
            stalniGostMap.put(PRIMARY_KEY, this.id);
        }
        
        return stalniGostMap;
    }
    
    public LinkedHashMap<String, String> makeMapForTableOutput() {
        LinkedHashMap<String, String> stalniGostMap = new LinkedHashMap();
        
        stalniGostMap.put(PRIMARY_KEY, this.id);
        stalniGostMap.put(NAZIV, this.naziv);
        stalniGostMap.put(POPUST, this.popust);

        return stalniGostMap;
    }
    
}
