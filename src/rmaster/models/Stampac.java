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
 * @author Bosko
 */
public class Stampac extends ModelBase {
    public static String TABLE_NAME = "stampac";
    public static String PRIMARY_KEY = "id";
   
    public static String NAZIV = "naziv";
    public static String BROJ_KOPIJA_ZBIRNE = "brojKopijaZbirne";
    public static String KODNA_STRANA = "kodnaStrana";
    public static String STAMPA_IZVESTAJE = "stampaIzvestaje";
    public static String STAMPA_ZBIRNU = "stampaZbirnu";
    public static String TIP = "tip";
    
    public String id;
    public String naziv;
    public String brojKopijaZbirne;
    public String kodnaStrana;
    public String stampaIzvestaje;
    public String stampaZbirnu;
    public String tip;
    
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
    public void makeFromHashMap(HashMap<String, String> stampac) 
    {
        this.id = stampac.get(PRIMARY_KEY);
        this.naziv = stampac.get(NAZIV);
        this.brojKopijaZbirne = stampac.get(BROJ_KOPIJA_ZBIRNE);
        this.kodnaStrana = stampac.get(KODNA_STRANA);
        this.stampaIzvestaje = stampac.get(STAMPA_IZVESTAJE);
        this.stampaZbirnu = stampac.get(STAMPA_ZBIRNU);
        this.tip = stampac.get(TIP);
    }
    
    
    @Override
    public LinkedHashMap<String, String> toHashMap(boolean includeId)
    {
        LinkedHashMap<String, String> stampac = new LinkedHashMap();

        stampac.put(NAZIV, this.naziv);
        stampac.put(BROJ_KOPIJA_ZBIRNE, this.brojKopijaZbirne);
        stampac.put(KODNA_STRANA, this.kodnaStrana);
        stampac.put(STAMPA_IZVESTAJE, this.stampaIzvestaje);
        stampac.put(STAMPA_ZBIRNU, this.stampaZbirnu);
        stampac.put(TIP, this.tip);
        
        if (includeId) {
            stampac.put(PRIMARY_KEY, this.id);
        }
        
        return stampac;
    }
    
    public LinkedHashMap<String, String> makeMapForTableOutput() {
        LinkedHashMap<String, String> stampac = new LinkedHashMap();
        
        stampac.put(PRIMARY_KEY, this.id);
        stampac.put(NAZIV, this.naziv);
        stampac.put(BROJ_KOPIJA_ZBIRNE, this.brojKopijaZbirne);
        stampac.put(KODNA_STRANA, this.kodnaStrana);
        stampac.put(STAMPA_IZVESTAJE, this.stampaIzvestaje);
        stampac.put(STAMPA_ZBIRNU, this.stampaZbirnu);
        stampac.put(TIP, this.tip);
        return stampac;
    }
}
