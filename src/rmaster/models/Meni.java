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
public class Meni extends ModelBase{
    public static String TABLE_NAME = "meni";
    public static String PRIMARY_KEY = "id";
    public static String BLOKIRAN = "blokiran";
    public static String NAZIV = "naziv";
    
    public String idMenija;
    public String blokiran;
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
    public void makeFromHashMap(HashMap<String, String> RezervacijaMap) 
    {
        this.idMenija = RezervacijaMap.get(PRIMARY_KEY);
        this.blokiran = RezervacijaMap.get(BLOKIRAN);
        this.naziv = RezervacijaMap.get(NAZIV);
    }
    
    @Override
    public LinkedHashMap<String, String> toHashMap(boolean includeId)
    {
        LinkedHashMap<String, String> mapa = new LinkedHashMap();

        if (includeId) {
            mapa.put(PRIMARY_KEY, this.idMenija);
        }
        mapa.put(NAZIV, this.naziv);
        mapa.put(BLOKIRAN, this.blokiran);

        return mapa;
    }
    
    public LinkedHashMap<String, String> makeMapForTableOutput()
    {
        return toHashMap(true);
    }
    
}
