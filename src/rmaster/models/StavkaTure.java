/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rmaster.assets.Utils;

/**
 *
 * @author Arbor
 */
public class StavkaTure {
    private int redniBroj = -1;
    private int redniBrojGlavneStavke = -1;
     private int redniBrojStavkeOpisni = 0;
    private int redniBrojStavkeDodatni = 0;

    public double cenaJedinicna = 0;
    // Polja iz baze
    public long id = 0;
    private int brojStola;
    public double cena = 0;
    public double kolicina = 0;
    public String naziv;
    public long RACUN_ID;
    public long ARTIKAL_ID;
    public long TURA_ID;
    private long GLAVNASTAVKA_ID = 0;
    public List<StavkaTure> dodatniArtikli = new ArrayList(); 
    public List<StavkaTure> opisniArtikli = new ArrayList();


// Konstruktor
    public StavkaTure(Map<String, String> stavkaTure) {
        String novaKolicina = "";

        if (stavkaTure.get("id")  != null)
            this.id = Long.parseLong(stavkaTure.get("id"));

        if (stavkaTure.get("brojStola")  != null)
            this.brojStola = Integer.parseInt(stavkaTure.get("brojStola"));

        if (stavkaTure.get("cena")  != null)
            this.setCenaJedinicna(Utils.getDoubleFromString(stavkaTure.get("cena"))); 

        if (stavkaTure.get("kolicina")  != null)
            this.kolicina = Utils.getDoubleFromString(
                    (stavkaTure.get("kolicina").contains("x")
                        ? stavkaTure.get("kolicina").substring(1)
                        : stavkaTure.get("kolicina")));

        if (stavkaTure.get("naziv")  != null)
            this.naziv = stavkaTure.get("naziv");

        if (stavkaTure.get("RACUN_ID")  != null)
            this.RACUN_ID = Long.parseLong(stavkaTure.get("RACUN_ID"));
        
        if (stavkaTure.get("ARTIKAL_ID")  != null)
            this.ARTIKAL_ID = Long.parseLong(stavkaTure.get("ARTIKAL_ID"));
        
        if (stavkaTure.get("TURA_ID")  != null)
            this.TURA_ID = Long.parseLong(stavkaTure.get("TURA_ID"));
        
        if (stavkaTure.get("GLAVNASTAVKA_ID") != null)
            this.GLAVNASTAVKA_ID = Long.parseLong(stavkaTure.get("GLAVNASTAVKA_ID"));
    }

// Redni broj koji pomaze pri formiranju tabele za prikaz i brisanju stavki iz prikaza
    public int getRedniBroj() {
        return this.redniBroj;
    }

    public void setRedniBroj(int rb) {
        this.redniBroj = rb;
    }
    
// Redni broj glavne stavke za koji se vezu dodatni i opisni artikli, pomaze pri brisanju stavki iz prikaza
    public int getRedniBrojGlavneStavke() {
        return this.redniBrojGlavneStavke;
    }
    
    public void setRedniBrojGlavneStavke(int rb) {
        this.redniBrojGlavneStavke = rb;
    }
    
// F-je koje vracaju ArtikalID kao long i String
    public long getArtikalID(){
        return this.ARTIKAL_ID;
    }
    public String getArtikalIDString(){
        return "" + this.ARTIKAL_ID;
    }

    public long getStavkaTureId(){
        return this.id;
    }

    public double getKolicina(){
        return this.kolicina;
    }

    public StavkaTure getOpisniArtikalByRedniBrojOpisnog(int redniBrojOpisnogArtikla) {
        for (StavkaTure stavkaTure : this.opisniArtikli) {
            if (stavkaTure.getRedniBroj() == redniBrojOpisnogArtikla)
                return stavkaTure;
        }
        return null;
    }
    
    public StavkaTure getDodatniArtikalByRedniBrojDodatnog(int redniBrojDodatnogArtikla) {
        for (StavkaTure stavkaTure : this.dodatniArtikli) {
            if (stavkaTure.getRedniBroj() == redniBrojDodatnogArtikla)
                return stavkaTure;
        }
        return null;
    }
    
    public Map<String, String> dajStavkuTure() {
        Map<String, String> stavkaTure = new HashMap<>();

        stavkaTure.put("redniBroj", "" + this.getRedniBroj());
        stavkaTure.put("id", "" + this.getStavkaTureId());
        stavkaTure.put("artikalId", this.getArtikalIDString());
        stavkaTure.put("naziv", this.naziv);
        int intKolicina = (int)this.kolicina;
        if (this.kolicina == intKolicina) {
            stavkaTure.put("kolicina", "x" + intKolicina);
        } else {
            stavkaTure.put("kolicina", "x" + Utils.getStringFromDouble(this.kolicina));
        }
        stavkaTure.put("cenaJedinicna", Utils.getStringFromDouble(this.cenaJedinicna));
        stavkaTure.put("cena", Utils.getStringFromDouble(this.cena));
        //stavkaTure.put("GlavnaStavkaID", "" + this.getGlavnaStavkaID());
        stavkaTure.put("redniBrojGlavnaStavka", "" + this.getRedniBrojGlavneStavke());
        
        return stavkaTure;
    } 
    
/************************************************************************************/
/********************* Rad sa opisnim artiklima *************************************/
/************************************************************************************/
    public List<StavkaTure> getArtikliOpisni(){
        return opisniArtikli;
    }
    /*** Dodavanje opisnog artikla za kolicinu koja je prosledjena kroz StavkaTure ***/
    public void dodajKolicinuArtikalOpisni(StavkaTure opisniArtikal){
        for (StavkaTure opArtikal: opisniArtikli) {
            if (opArtikal.ARTIKAL_ID == opisniArtikal.ARTIKAL_ID) {
                opArtikal.povecajKolicinuZa(opisniArtikal.kolicina);
                return;
            }
        }
        opisniArtikal.setRedniBroj(++this.redniBrojStavkeOpisni);
        opisniArtikli.add(opisniArtikal);
    }
    /*** Oduzimanje opisnog artikla 
     *   za kolicinu koja je prosledjena kroz StavkaTure,
     *   brisanje ako je nova kolicina = 0 ***/
    public void smanjiKolicinuArtikalOpisni(StavkaTure opisniArtikal){
        for (StavkaTure opArtikal: opisniArtikli) {
            if (opArtikal.ARTIKAL_ID == opisniArtikal.ARTIKAL_ID) {
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
    public void dodajKolicinuArtikalDodatni(StavkaTure dodatniArtikal){
        for (StavkaTure dodArtikal: dodatniArtikli) {
            if (dodArtikal.ARTIKAL_ID == dodatniArtikal.ARTIKAL_ID) {
                dodArtikal.povecajKolicinuZa(dodatniArtikal.kolicina);
                return;
            }
        }
        dodatniArtikal.setRedniBroj(++this.redniBrojStavkeDodatni);
        dodatniArtikli.add(dodatniArtikal);
    }
    /*** Oduzimanje dodatnog artikla za kolicinu koja je prosledjena kroz StavkaTure, brisanje ako je nova kolicina = 0 ***/
    public void smanjiKolicinuArtikalDodatni(StavkaTure dodatniArtikal){
        for (StavkaTure dodArtikal: dodatniArtikli) {
            if (dodArtikal.ARTIKAL_ID == dodatniArtikal.ARTIKAL_ID) {
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
    
    public boolean getImaDodatneIliOpisneArtikle () {
        return (this.dodatniArtikli.size() + this.opisniArtikli.size() > 0);
    }
    
    public long getGlavnaStavkaID() {
        return this.GLAVNASTAVKA_ID;
    }
}
