/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import rmaster.assets.ModelBase;
import static rmaster.models.Rezervacija.BROJ_OSOBA;
import static rmaster.models.Rezervacija.BROJ_STOLA;
import static rmaster.models.Rezervacija.DATUM;
import static rmaster.models.Rezervacija.IME;
import static rmaster.models.Rezervacija.NAPOMENA;
import static rmaster.models.Rezervacija.PRIMARY_KEY;
import static rmaster.models.Rezervacija.TABLE_NAME;
import static rmaster.models.Rezervacija.TELEFON;
import static rmaster.models.Rezervacija.VREME;

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
