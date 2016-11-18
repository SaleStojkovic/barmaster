
package rmaster.models;

import java.util.HashMap;
import java.util.LinkedHashMap;


public final class Rezervacija {
    
    public String idRezervacije;
    
    public String brOsoba;
    
    public String brStola;
    
    public String ime;
    
    public String napomena;
    
    public String tel;
    
    public String datum;
    
    public String vreme;
    
    
    public Rezervacija(HashMap<String, String> RezervacijaMap) 
    {
        this.ime = RezervacijaMap.get("ime");

        setDatumIVreme(RezervacijaMap.get("vreme"));

        this.brStola = RezervacijaMap.get("brStola");

        this.brOsoba = RezervacijaMap.get("brOsoba");
               
        this.tel = RezervacijaMap.get("tel");
        
        this.napomena = RezervacijaMap.get("napomena");

        this.idRezervacije = RezervacijaMap.get("id");

    }
    
    
    public void setDatumIVreme(String vremeString) 
    {
        this.datum = vremeString.substring(0, Math.min(vremeString.length(), 10));
        
        this.vreme = vremeString.substring(11, Math.min(vremeString.length(), 16));
    }
    
    public LinkedHashMap<String, String> toHashMap()
    {
        LinkedHashMap<String, String> rezervacijaMap = new LinkedHashMap();
        rezervacijaMap.put("ime", this.ime);
        rezervacijaMap.put("datum", this.datum);
        rezervacijaMap.put("vreme", this.vreme);
        rezervacijaMap.put("brStola", this.brStola);
        rezervacijaMap.put("brOsoba", this.brOsoba);
        rezervacijaMap.put("tel", this.tel);
        rezervacijaMap.put("napomena", this.napomena);
        rezervacijaMap.put("id", this.idRezervacije);

        return rezervacijaMap;
    }
}
