/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import rmaster.assets.ModelBase;

/**
 *
 * @author Arbor
 */
public class Artikal_Podgrupa extends ModelBase implements Child_Interface {

    public static String TABLE_NAME = "grupaartikalafront";
    public static String PRIMARY_KEY = "id";
    
    public static String NAZIV = "naziv";
    public static String PRIKAZ_NA_EKRAN = "prikazNaEkran";
    public static String PRIORITET = "prioritet";
    public static String SKR_NAZIV = "skrNaziv";
    public static String SLIKA = "slika";
    public static String NADREDJENA_GRUPA_ID = "GRUPA_ID";

    public String id;
    public String naziv;
    public String prikazNaEkran;
    public String prioriter;
    public String skrNaziv;
    public String slika;
    public String nadredjenaGrupaId;
    
    public List<Child_Abstract> artikli = new ArrayList<>();
    
    @Override
    public String getPrimaryKeyName() {
        return PRIMARY_KEY;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public void makeFromHashMap(HashMap<String, String> HashMap) {
        this.id = HashMap.get(PRIMARY_KEY);
        
        this.naziv = HashMap.get(NAZIV);
        
        this.prikazNaEkran = HashMap.get(PRIKAZ_NA_EKRAN);
        
        this.prioriter = HashMap.get(PRIORITET);
        
        this.skrNaziv = HashMap.get(SKR_NAZIV);
        
        this.slika = HashMap.get(SLIKA);
        
        this.nadredjenaGrupaId = HashMap.get(NADREDJENA_GRUPA_ID);
    }

    @Override
    public LinkedHashMap<String, String> toHashMap(boolean includeId) {
        
        LinkedHashMap<String, String> podgrupa = new LinkedHashMap();

        podgrupa.put(naziv, NAZIV);
        podgrupa.put(prikazNaEkran, PRIKAZ_NA_EKRAN);
        podgrupa.put(prioriter, PRIORITET);
        podgrupa.put(skrNaziv, SKR_NAZIV);
        podgrupa.put(slika, SLIKA);
        podgrupa.put(nadredjenaGrupaId, NADREDJENA_GRUPA_ID);
        
        if (includeId) {
            podgrupa.put(PRIMARY_KEY, this.id);
        }
        
        return podgrupa;
    }
    
    public void ucitajSveArtikle() {
        
    }
    
    
    
}
