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
public class GrupaArtikalaFront extends ModelBase {
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
    public static String TABLE_NAME = "grupaartikalafront";
    public static String PRIMARY_KEY = "id";
    
    public static String NAZIV = "naziv";

    public static String PRIORITET = "prioritet";
    public static String SKRNAZIV = "skrNaziv";
    public static String SLIKA = "slika";
    public static String GRUPA_ID = "GRUPA_ID";
    public static String PRIKAZ_NA_EKRAN = "prikazNaEkran";
    
    public String id;
    public String naziv;
    
    public String prioritet;
    public String skrNaziv;
    public String slika;
    public String GrupaID;
    public String PrikazNaEkran;
    
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
    public void makeFromHashMap(HashMap<String, String> grupaArtikalaFrontMap) 
    {
        this.id = grupaArtikalaFrontMap.get(PRIMARY_KEY);
        this.naziv = grupaArtikalaFrontMap.get(NAZIV);
        
        this.prioritet = grupaArtikalaFrontMap.get(PRIORITET);
        this.skrNaziv = grupaArtikalaFrontMap.get(SKRNAZIV);
        this.slika = grupaArtikalaFrontMap.get(SLIKA);
        if (grupaArtikalaFrontMap.get(GRUPA_ID) != null)
            this.GrupaID = grupaArtikalaFrontMap.get(GRUPA_ID);
        else
            this.GrupaID = "";
        this.PrikazNaEkran = grupaArtikalaFrontMap.get(PRIKAZ_NA_EKRAN);
    }
    
    @Override
    public LinkedHashMap<String, String> toHashMap(boolean includeId)
    {
        LinkedHashMap<String, String> grupaArtikalaFrontMap = new LinkedHashMap();

        grupaArtikalaFrontMap.put(NAZIV, this.naziv);
        grupaArtikalaFrontMap.put(PRIORITET, this.prioritet);
        grupaArtikalaFrontMap.put(SKRNAZIV, this.skrNaziv);
        grupaArtikalaFrontMap.put(SLIKA, this.slika);
        grupaArtikalaFrontMap.put(GRUPA_ID, this.GrupaID);
        grupaArtikalaFrontMap.put(PRIKAZ_NA_EKRAN, this.PrikazNaEkran);
        
        if (includeId) {
            grupaArtikalaFrontMap.put(PRIMARY_KEY, this.id);
        }
        
        return grupaArtikalaFrontMap;
    }
    
    public LinkedHashMap<String, String> makeMapForTableOutput() {
        LinkedHashMap<String, String> grupaArtikalaFrontMap = new LinkedHashMap();
        
        grupaArtikalaFrontMap.put(PRIMARY_KEY, this.id);
        grupaArtikalaFrontMap.put(NAZIV, this.naziv);
        grupaArtikalaFrontMap.put(PRIORITET, this.prioritet);
        grupaArtikalaFrontMap.put(SKRNAZIV, this.skrNaziv);
        grupaArtikalaFrontMap.put(SLIKA, this.slika);
        grupaArtikalaFrontMap.put(GRUPA_ID, this.GrupaID);
        grupaArtikalaFrontMap.put(PRIKAZ_NA_EKRAN, this.PrikazNaEkran);

        return grupaArtikalaFrontMap;
    }
    
}
