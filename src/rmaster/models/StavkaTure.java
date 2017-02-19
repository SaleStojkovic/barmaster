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
import rmaster.assets.DBBroker;
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
    public double procenatPopusta = 0;
    public double kolicina = 0;
    public String naziv;
    public long RACUN_ID;
    public long ARTIKAL_ID;
    public long TURA_ID;
    public long GLAVNASTAVKA_ID = 0;
    public List<StavkaTure> dodatniArtikli = new ArrayList(); 
    public List<StavkaTure> opisniArtikli = new ArrayList();
    public boolean jeOpisni = false;
    public boolean jeDodatni = false;
    public boolean dozvoljenPopust;
    public String stampacID;


// Konstruktor
    public StavkaTure(Map<String, String> stavkaTure) {
        //String novaKolicina = "";

        if (stavkaTure.get("id")  != null)
            this.id = Long.parseLong(stavkaTure.get("id"));

        if (stavkaTure.get("brojStola")  != null)
            this.brojStola = Integer.parseInt(stavkaTure.get("brojStola"));

        if (stavkaTure.get("procenatPopusta")  != null)
            this.setProcenatPopusta(Utils.getDoubleFromString(stavkaTure.get("procenatPopusta"))); 

        if (stavkaTure.get("kolicina")  != null)
            this.setKolicina(Utils.getDoubleFromString(
                    (stavkaTure.get("kolicina").contains("x")
                        ? stavkaTure.get("kolicina").substring(1)
                        : stavkaTure.get("kolicina"))));

        if (stavkaTure.get("cena")  != null)
            this.setCenaJedinicna(Utils.getDoubleFromString(stavkaTure.get("cena"))/this.kolicina); 

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

        if (stavkaTure.get("dozvoljenPopust") != null && (stavkaTure.get("dozvoljenPopust").equals("true") || stavkaTure.get("dozvoljenPopust").equals("1")))
            this.dozvoljenPopust = true;

        if (stavkaTure.get("stampacID") != null)
            this.stampacID = stavkaTure.get("stampacID");
    }

// Redni broj koji pomaze pri formiranju tabele za prikaz i brisanju stavki iz prikaza
    public int getRedniBroj() {
        return this.redniBroj;
    }

    public void setRedniBroj(int rb) {
        this.redniBroj = rb;
    }
    
    public void setProcenatPopusta(double procenat) {
        if (this.dozvoljenPopust)
            this.procenatPopusta = procenat;
    }

    public String getStampacID() {
        return this.stampacID;
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
        stavkaTure.put("naziv", this.getNaziv());
        int intKolicina = (int)this.kolicina;
        if (this.kolicina == intKolicina) {
            stavkaTure.put("kolicina", "x" + intKolicina);
        } else {
            stavkaTure.put("kolicina", "x" + Utils.getStringFromDouble(this.kolicina));
        }
        stavkaTure.put("procenatPopusta", Utils.getStringFromDouble(this.procenatPopusta));
        stavkaTure.put("cenaJedinicna", Utils.getStringFromDouble(this.cenaJedinicna));        stavkaTure.put("cenaJedinicna", Utils.getStringFromDouble(this.cenaJedinicna));        stavkaTure.put("cenaJedinicna", Utils.getStringFromDouble(this.cenaJedinicna));
        stavkaTure.put("cena", Utils.getStringFromDouble(this.cena));
        if (this.getRedniBrojGlavneStavke()!=0)
            stavkaTure.put("redniBrojGlavnaStavka", "" + this.getRedniBrojGlavneStavke());
        stavkaTure.put("dozvoljenPopust", "" + this.dozvoljenPopust);
        
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
                if (opArtikal.getKolicina() < 1) {
                    opArtikal.povecajKolicinuZa(opisniArtikal.kolicina);
                }
                return;
            }
        }
        opisniArtikal.setRedniBroj(++this.redniBrojStavkeOpisni);
        opisniArtikal.jeOpisni = true;
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
        dodatniArtikal.jeDodatni = true;
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

    public double getCena() {
        return cena;
    }

    public boolean getDozvoljenPopust() {
        return this.dozvoljenPopust;
    }

    public double getCenaSaObracunatimPopustom() {
        if (this.dozvoljenPopust)
            return cena * (100 - procenatPopusta) / 100;
        else
            return cena;
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
    public void setKolicina(double novaKolicina) {
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
    
    public int getBrojStola() {
        return this.brojStola;
    }

    public String getNaziv() {
        if (this.jeDodatni)
            return "-> " + this.naziv;
        if (this.jeOpisni)
            return "--> " + this.naziv;
        return this.naziv;
    }

    public long getRacunID() {
        return this.RACUN_ID;
    }
    
    public void setRacunID(long racunID) {
        this.RACUN_ID = racunID;
    }
    
    public long getTuraID() {
        return this.TURA_ID;
    }
    
    public void setTuraID(long turaID) {
        this.TURA_ID = turaID;
    }
    
    public long getID() {
        return this.id;
    }
    
    public void setID(long idStavke) {
        this.id = idStavke;
    }
    
    public long getGlavnaStavkaID() {
        return this.GLAVNASTAVKA_ID;
    }

    public void setGlavnaStavkaID(long glavnaStavkaID) {
        this.GLAVNASTAVKA_ID = glavnaStavkaID;
    }
    
    public double getProcenatPopusta() {
        return this.procenatPopusta;
    }
    
    public StavkaTure getClone() {
        Map<String, String> novaGlavnaStavka = new HashMap<>();
        novaGlavnaStavka.put("id", "" + 0);
        novaGlavnaStavka.put("ARTIKAL_ID", this.getArtikalIDString());
        novaGlavnaStavka.put("brojStola", "" + this.getBrojStola());
        novaGlavnaStavka.put("kolicina", "" + this.kolicina);
        novaGlavnaStavka.put("naziv", "" + this.naziv);
        novaGlavnaStavka.put("cena", "" + this.cena);
        novaGlavnaStavka.put("cenaJedinicna", "" + this.cenaJedinicna);
        novaGlavnaStavka.put("dozvoljenPopust", "" + this.getDozvoljenPopust());
        novaGlavnaStavka.put("procenatPopusta", "" + this.getProcenatPopusta());
        novaGlavnaStavka.put("stampacID", "" + this.stampacID);
        
        StavkaTure stavka = new StavkaTure(novaGlavnaStavka);

        stavka.redniBroj = this.redniBroj;
        stavka.redniBrojGlavneStavke = this.redniBrojGlavneStavke;
 
        stavka.redniBrojStavkeOpisni = this.redniBrojStavkeOpisni;
        stavka.redniBrojStavkeDodatni = this.redniBrojStavkeDodatni;
        stavka.jeOpisni = this.jeOpisni;
        stavka.jeDodatni = this.jeDodatni;
        stavka.GLAVNASTAVKA_ID = this.GLAVNASTAVKA_ID;
        
        for (StavkaTure stavkaTure : dodatniArtikli) {
            stavka.dodajKolicinuArtikalDodatni(stavkaTure.getClone());
        }
        for (StavkaTure stavkaTure : opisniArtikli) {
            stavka.dodajKolicinuArtikalOpisni(stavkaTure.getClone());
        }
        return stavka;
    }
    
    public void snimi() {
        try {
            DBBroker db = new DBBroker();
            long result = 0;
            HashMap<String,String> mapaStavka = new HashMap();
            mapaStavka.put("brojStola", "" + this.getBrojStola());
            mapaStavka.put("cena", "" + this.getCena());
            mapaStavka.put("procenatPopusta", "" + this.getProcenatPopusta());
            mapaStavka.put("kolicina", "" + this.getKolicina());
            mapaStavka.put("naziv", this.naziv);
            mapaStavka.put("RACUN_ID", "" + this.getRacunID());
            mapaStavka.put("ARTIKAL_ID", "" + this.getArtikalID());
            mapaStavka.put("TURA_ID", "" + this.getTuraID());
            if (this.getGlavnaStavkaID() != 0)
                mapaStavka.put("GLAVNASTAVKA_ID", "" + this.getGlavnaStavkaID());
            if (this.getID() != 0)
                db.izmeni("stavkaracuna", "id", "" + this.getID(), mapaStavka, false);
            else {
                result = db.ubaciRed("stavkaracuna", mapaStavka, false);
                this.setID(result);
            }

            for (StavkaTure stavkaTureDodatni : this.getArtikliDodatni()) {
                stavkaTureDodatni.setGlavnaStavkaID(this.getID());
                stavkaTureDodatni.setRacunID(this.getRacunID());
                stavkaTureDodatni.setTuraID(this.getTuraID());
                stavkaTureDodatni.snimi();
            }
            for (StavkaTure stavkaTureOpisni : this.getArtikliOpisni()) {
                stavkaTureOpisni.setGlavnaStavkaID(this.getID());
                stavkaTureOpisni.setRacunID(this.getRacunID());
                stavkaTureOpisni.setTuraID(this.getTuraID());
                stavkaTureOpisni.snimi();
            }
        } catch(Exception e) {

        }
    }
}
