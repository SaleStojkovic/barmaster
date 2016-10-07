/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Arbor
 */
public class StavkaTure {
    
    public String imeArtikla;
    public int kolicina;
    public int cena;
    public String artikalId;
    
    
    public String getArtikalId(){
        return this.artikalId;
    }
    
    public void setCena(int novaCena) {
        this.cena = novaCena;
    }
    
    public StavkaTure(Map<String, String> stavkaTure) {
        String novaKolicina = stavkaTure.get("kolicina");
        if (novaKolicina.contains("x")) {
            novaKolicina.substring(1);
        }
        this.imeArtikla = stavkaTure.get("naziv");
        this.kolicina = Integer.parseInt(novaKolicina);
        this.cena = Integer.parseInt(stavkaTure.get("cena"));
        
        this.artikalId = stavkaTure.get("ARTIKAL_ID");
    }
    
    public Map<String, String> dajStavkuTure() {
        Map<String, String> stavkaTure = new HashMap<>();
//        stavkaTure.put("id", this.stavkaTureId);
        stavkaTure.put("naziv", this.imeArtikla);
        stavkaTure.put("kolicina", "x" + this.kolicina);
        stavkaTure.put("cena", this.cena + "");
//        stavkaTure.put("artikalId", this.artikalId);
        
        return stavkaTure;
    } 
    
    public void promeniKolicinu(int novaKolicina) {
            this.kolicina = novaKolicina;
    }
    
    public void povecajKolicinu() {
            int novaKolicina = this.kolicina;
            novaKolicina++;
            this.kolicina = novaKolicina;
    }
    
    public void smanjiKolicinu() {
            int novaKolicina = this.kolicina;
            novaKolicina--;
            this.kolicina = novaKolicina;
    }
}
