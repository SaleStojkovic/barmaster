/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Arbor
 */
public class StavkaTure {
    
    public String artikalId;
    public String imeArtikla;
    public double kolicina = 0;
    public double cenaJedinicna = 0;
    public double cena = 0;
    public List<StavkaTure> dodatniArtikli = new ArrayList(); 
    public List<StavkaTure> opisniArtikli = new ArrayList(); 
    

    public String getArtikalId(){
        return this.artikalId;
    }

    public StavkaTure(Map<String, String> stavkaTure) {
        String novaKolicina = stavkaTure.get("kolicina");
        if (novaKolicina.contains("x")) {
            novaKolicina.substring(1);
        }
        this.artikalId = stavkaTure.get("ARTIKAL_ID");
        this.imeArtikla = stavkaTure.get("naziv");
        this.kolicina = Double.parseDouble(novaKolicina);
        //this.cena = Double.parseDouble(stavkaTure.get("cena"));
        setCenaJedinicna(Double.parseDouble(stavkaTure.get("cenaJedinicna")));
    }
    
    public Map<String, String> dajStavkuTure() {
        Map<String, String> stavkaTure = new HashMap<>();

        stavkaTure.put("artikalId", this.artikalId);
        stavkaTure.put("naziv", this.imeArtikla);
        //stavkaTure.put("kolicina", "x" + String.format("%1$,.2f", this.kolicina));
        stavkaTure.put("kolicina", "x" + String.format("%1$,.0f", this.kolicina));
        stavkaTure.put("cenaJedinicna", String.format("%1$,.2f", this.cenaJedinicna));
        stavkaTure.put("cena", String.format("%1$,.2f", this.cena));
        
        return stavkaTure;
    } 
    
/************************************************************************************/
/********************* Rad sa opisnim artiklima *************************************/
/************************************************************************************/
    public List<StavkaTure> getArtikliOpisni(){
        return opisniArtikli;
    }
    /*** Dodavanje opisnog artikla za kolicinu koja je prosledjena kroz StavkaTure ***/
    public void addArtikalOpisni(StavkaTure opisniArtikal){
        for (StavkaTure opArtikal: opisniArtikli) {
            if (opArtikal.artikalId == opisniArtikal.artikalId) {
                opArtikal.povecajKolicinuZa(opisniArtikal.kolicina);
                return;
            }
        }
        opisniArtikli.add(opisniArtikal);
    }
    /*** Oduzimanje opisnog artikla 
     *   za kolicinu koja je prosledjena kroz StavkaTure,
     *   brisanje ako je nova kolicina = 0 ***/
    public void removeArtikalOpisni(StavkaTure opisniArtikal){
        for (StavkaTure opArtikal: opisniArtikli) {
            if (opArtikal.artikalId == opisniArtikal.artikalId) {
                opArtikal.smanjiKolicinuZa(opisniArtikal.kolicina);
                if (opArtikal.kolicina <= 0)
                    opisniArtikli.remove(opArtikal);
            }
        }
    }
/************************************************************************************/
/********************* Rad sa opisnim artiklima - KRAJ ******************************/
/************************************************************************************/

    
/************************************************************************************/
/********************* Rad sa dodatnim artiklima ************************************/
/************************************************************************************/
    public List<StavkaTure> getArtikliDodatni(){
        return dodatniArtikli;
    }
    /*** Dodavanje dodatnog artikla za kolicinu koja je prosledjena kroz StavkaTure ***/
    public void addArtikalDodatni(StavkaTure dodatniArtikal){
        for (StavkaTure dodArtikal: dodatniArtikli) {
            if (dodArtikal.artikalId == dodatniArtikal.artikalId) {
                dodArtikal.povecajKolicinuZa(dodatniArtikal.kolicina);
                return;
            }
        }
        dodatniArtikli.add(dodatniArtikal);
    }
    /*** Oduzimanje dodatnog artikla za kolicinu koja je prosledjena kroz StavkaTure, brisanje ako je nova kolicina = 0 ***/
    public void removeArtikalDodatni(StavkaTure dodatniArtikal){
        for (StavkaTure dodArtikal: dodatniArtikli) {
            if (dodArtikal.artikalId == dodatniArtikal.artikalId) {
                dodArtikal.smanjiKolicinuZa(dodatniArtikal.kolicina);
                if (dodArtikal.kolicina <= 0)
                    dodatniArtikli.remove(dodArtikal);
            }
        }
    }
/************************************************************************************/
/********************* KRAJ - Rad sa dodatnim artiklima *****************************/
/************************************************************************************/

    
    
/************************************************************************************/
/************************* Rad sa cenama ********************************************/
/************************************************************************************/
    public void setCenaJedinicna(double cenaJedinicna) {
        this.cenaJedinicna = cenaJedinicna;
        cenaObracunaj();
    }

    public void cenaObracunaj() {
        this.cena = this.cenaJedinicna * this.kolicina;
    }
/************************************************************************************/
/************************* Rad sa cenama - KRAJ *************************************/
/************************************************************************************/
    
    
/************************************************************************************/
/************************* Rad sa kolicinama ****************************************/
/************************************************************************************/
    /*** Promena kolicine ***/
    public void promeniKolicinu(double novaKolicina) {
            this.kolicina = novaKolicina;
            this.cenaObracunaj();
    }
    
    /*** Povecavanje kolicine ***/
    public void povecajKolicinu() {
            this.kolicina = this.kolicina + 1;
            this.cenaObracunaj();
    }
    public void povecajKolicinuZa(double promena) {
            this.kolicina = this.kolicina + promena;
            if (this.kolicina<0)
                this.kolicina=0;
            this.cenaObracunaj();
    }

    /*** Smanjivanje kolicine ***/
    public void smanjiKolicinu() {
            this.kolicina = this.kolicina - 1;
            if (this.kolicina<0)
                this.kolicina=0;
            this.cenaObracunaj();
    }
    public void smanjiKolicinuZa(double smanjiZa) {
            this.kolicina = this.kolicina - smanjiZa;
            if (this.kolicina<0)
                this.kolicina=0;
            this.cenaObracunaj();
    }
/************************************************************************************/
/************************* Rad sa kolicinama - KRAJ *********************************/
/************************************************************************************/
}
