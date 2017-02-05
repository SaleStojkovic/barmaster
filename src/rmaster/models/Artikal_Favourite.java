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
public class Artikal_Favourite extends Child_Abstract implements Child_Interface {
    

    
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
        this.stampacID = artikalFrontMap.get(STAMPAC_ID);
        
        this.setType(this.id);
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
        artikalFrontMap.put(STAMPAC_ID, this.stampacID);

        return artikalFrontMap;
    }
    
}
