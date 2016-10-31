
package rmaster.models;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import javafx.util.converter.DateTimeStringConverter;


public final class Rezervacija {
   
    public String brOsoba;
    
    public String brStola;
    
    public String ime;
    
    public String napomena;
    
    public String tel;
    
    public String datum;
    
    public String vreme;
    
    
    public Rezervacija(HashMap<String, String> RezervacijaMap) 
    {
        this.brOsoba = RezervacijaMap.get("brOsoba");
        
        this.brStola = RezervacijaMap.get("brStola");
        
        this.ime = RezervacijaMap.get("ime");
       
        this.napomena = RezervacijaMap.get("napomena");
        
        this.tel = RezervacijaMap.get("tel");
        
        setDatumIVreme(RezervacijaMap.get("vreme"));
    }
    
    
    public void setDatumIVreme(String vremeString) 
    {
        this.datum = vremeString.substring(0, Math.min(vremeString.length(), 10));
        
        this.vreme = vremeString.substring(11, Math.min(vremeString.length(), 16));
    }
}
