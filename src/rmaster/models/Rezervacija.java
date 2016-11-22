
package rmaster.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import rmaster.assets.ModelBase;


public final class Rezervacija extends ModelBase {
    
    public static String TABLE_NAME = "rezervacija";
    public static String PRIMARY_KEY = "id";
    public static String BROJ_OSOBA = "brOsoba";
    public static String DATUM = "datum";
    public static String IME = "ime";
    public static String NAPOMENA = "napomena";
    public static String TELEFON = "tel";
    public static String VREME = "vreme";
    public static String BROJ_STOLA = "brStola";
    
    public String idRezervacije;

    public String brOsoba;
    
    public String brStola;
    
    public String ime;
    
    public  String napomena;
    
    public  String tel;
    
    public  String datum;
    
    public  String vreme;
    
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
        this.ime = RezervacijaMap.get(IME);

        setDatumIVreme(RezervacijaMap.get(VREME));

        this.brStola = RezervacijaMap.get(BROJ_STOLA);

        this.brOsoba = RezervacijaMap.get(BROJ_OSOBA);
               
        this.tel = RezervacijaMap.get(TELEFON);
        
        this.napomena = RezervacijaMap.get(NAPOMENA);

        this.idRezervacije = RezervacijaMap.get(PRIMARY_KEY);

    }
    
    
    public void setDatumIVreme(String vremeString) 
    {
        String datumString = vremeString.substring(0, Math.min(vremeString.length(), 10));
        
        SimpleDateFormat  myFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");

        try {
            this.datum = myFormat.format(fromUser.parse(datumString));
        } catch (Exception e) {
            System.err.println(e);
        }
        
        this.vreme = vremeString.substring(11, Math.min(vremeString.length(), 16));
    }
    
    
    @Override
    public LinkedHashMap<String, String> toHashMap(boolean includeId)
    {
        LinkedHashMap<String, String> rezervacijaMap = new LinkedHashMap();
        rezervacijaMap.put(IME, this.ime);
        rezervacijaMap.put(DATUM, this.datum + " " + this.vreme + ":00");
        rezervacijaMap.put(VREME, this.datum + " " + this.vreme + ":00");
        rezervacijaMap.put(BROJ_STOLA, this.brStola);
        rezervacijaMap.put(BROJ_OSOBA, this.brOsoba);
        rezervacijaMap.put(TELEFON, this.tel);
        rezervacijaMap.put(NAPOMENA, this.napomena);
        if (includeId) {
            rezervacijaMap.put(PRIMARY_KEY, this.idRezervacije);
        }

        return rezervacijaMap;
    }
    
    public LinkedHashMap<String, String> makeMapForTableOutput() {
        LinkedHashMap<String, String> rezervacijaMap = new LinkedHashMap();
        rezervacijaMap.put(IME, this.ime);
        rezervacijaMap.put(DATUM, this.datum);
        rezervacijaMap.put(VREME, this.vreme);
        rezervacijaMap.put(BROJ_STOLA, this.brStola);
        rezervacijaMap.put(BROJ_OSOBA, this.brOsoba);
        rezervacijaMap.put(TELEFON, this.tel);
        rezervacijaMap.put(NAPOMENA, this.napomena);
        rezervacijaMap.put(PRIMARY_KEY, this.idRezervacije);

        return rezervacijaMap;
    }
}
