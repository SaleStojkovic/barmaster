/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets.items;

import java.util.HashMap;
import java.util.LinkedHashMap;
import rmaster.assets.ModelBase;

/**
 *
 * @author Bosko
 */
public class Artikal extends ModelBase {
    public static String TABLE_NAME = "getArtikliFavorite";
 
    public static String PRIMARY_KEY = "id";

    public static String BAR_CODE = "barCode";
    public static String CENA = "cena";
    public static String DOZVOLJEN_POPUST = "dozvoljenPopust";
    public static String JEDINICA_MERE = "jedinicaMere";
    public static String NAZIV = "naziv";
    public static String PRIORITET = "prioritet";
    public static String SKRACENI_NAZIV = "skrNaziv";
    public static String SLIKA = "slika";
    public static String TIP = "tip";
    public static String STAMPAC_ID = "stampacID";
    
    public String id;
    public String barCode;
    public String cena;
    public String dozvoljenPopust;
    public String jedinicaMere;
    public String naziv;
    public String prioritet;
    public String skrNaziv;
    public String slika;
    public String tip;
    public String stampacID;
    
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
    public void makeFromHashMap(HashMap<String, String> artikalFrontMap) 
    {
        this.id = artikalFrontMap.get(PRIMARY_KEY);
        this.barCode = artikalFrontMap.get(BAR_CODE);
        this.cena = artikalFrontMap.get(CENA);
        this.dozvoljenPopust = artikalFrontMap.get(DOZVOLJEN_POPUST);
        this.jedinicaMere = artikalFrontMap.get(JEDINICA_MERE);
        this.naziv = artikalFrontMap.get(NAZIV);
        this.prioritet = artikalFrontMap.get(PRIORITET);
        this.skrNaziv = artikalFrontMap.get(SKRACENI_NAZIV);
        this.slika = artikalFrontMap.get(SLIKA);
        this.tip = artikalFrontMap.get(TIP);
        this.stampacID = artikalFrontMap.get(STAMPAC_ID);
    }
    
    @Override
    public LinkedHashMap<String, String> toHashMap(boolean includeId)
    {
        LinkedHashMap<String, String> artikalFrontMap = new LinkedHashMap();
 
        artikalFrontMap.put(BAR_CODE, this.barCode);
        artikalFrontMap.put(CENA, this.cena);
        artikalFrontMap.put(DOZVOLJEN_POPUST, this.dozvoljenPopust);
        artikalFrontMap.put(JEDINICA_MERE, this.jedinicaMere);
        artikalFrontMap.put(NAZIV, this.naziv);
        artikalFrontMap.put(PRIORITET, this.prioritet);
        artikalFrontMap.put(SKRACENI_NAZIV, this.skrNaziv);
        artikalFrontMap.put(SLIKA, this.slika);
        artikalFrontMap.put(TIP, this.tip);
        artikalFrontMap.put(STAMPAC_ID, this.stampacID);
        
        if (includeId) {
            artikalFrontMap.put(PRIMARY_KEY, this.id);
        }
        
        return artikalFrontMap;
    }
    
    public LinkedHashMap<String, String> makeMapForTableOutput() {
        LinkedHashMap<String, String> artikalFrontMap = new LinkedHashMap();
        
        artikalFrontMap.put(PRIMARY_KEY, this.id);
        artikalFrontMap.put(BAR_CODE, this.barCode);
        artikalFrontMap.put(CENA, this.cena);
        artikalFrontMap.put(DOZVOLJEN_POPUST, this.dozvoljenPopust);
        artikalFrontMap.put(JEDINICA_MERE, this.jedinicaMere);
        artikalFrontMap.put(NAZIV, this.naziv);
        artikalFrontMap.put(PRIORITET, this.prioritet);
        artikalFrontMap.put(SKRACENI_NAZIV, this.skrNaziv);
        artikalFrontMap.put(SLIKA, this.slika);
        artikalFrontMap.put(TIP, this.tip);
        artikalFrontMap.put(STAMPAC_ID, this.stampacID);

        return artikalFrontMap;
    }
    
}
