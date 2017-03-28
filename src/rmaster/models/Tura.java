/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rmaster.assets.DBBroker;
import rmaster.assets.Utils;

/**
 *
 * @author Arbor
 */
public class Tura {
    
    public List<StavkaTure> listStavkeTure = new ArrayList<>();
    public long turaID = 0;
    private long gostID = 0;
    public int redniBrojStavke = 0;
    public Date datum;
    private int brojStolaBroj = 0;
    private long racunID = 0;
    public Tura () {
    }
    public Tura (
            String idTure,
            Date datum
    ) { 
        this.turaID = Integer.parseInt(idTure);
        this.datum = datum;
        
        String[] imenaArgumenata = {"idTure"};
        String[] vrednostiArgumenata = {idTure};
        
        List<Map<String, String>> listaRezultata = DBBroker.runStoredProcedure("getPorudzbinaTuraStavke", 
                imenaArgumenata, 
                vrednostiArgumenata);
        
        int i = 0;
        for (Map<String, String> noviRed : listaRezultata) {
            if (noviRed.get("GLAVNASTAVKA_ID") == null || noviRed.get("GLAVNASTAVKA_ID").equals("0")) {
                StavkaTure novaStavka = new StavkaTure(noviRed);
                novaStavka.setRedniBroj(++i);
                listStavkeTure.add(novaStavka);
            }
        }
        for (Map<String, String> noviRed : listaRezultata) {
            if (noviRed.get("GLAVNASTAVKA_ID") != null && !noviRed.get("GLAVNASTAVKA_ID").equals("0")) {
                StavkaTure novaStavka = new StavkaTure(noviRed);
                StavkaTure glavnaStavka = this.getStavkaTureByStavkaID(novaStavka.getGlavnaStavkaID());
                novaStavka.setRedniBrojGlavneStavke(glavnaStavka.getRedniBroj());
                if (novaStavka.cena == 0) {
                    glavnaStavka.dodajKolicinuArtikalOpisni(novaStavka);
                }
                else {
                    glavnaStavka.dodajKolicinuArtikalDodatni(novaStavka);
                }
            }
        }
    }
    
    public void setBrojStolaBroj(int brojStola) {
        this.brojStolaBroj = brojStola;
    }
    
    public void setPopust(double popust) {
        for (StavkaTure stavkaTure : listStavkeTure) {
            stavkaTure.setProcenatPopusta(popust);
            for (StavkaTure stavkaTure1 : stavkaTure.getArtikliDodatni()) {
                stavkaTure1.setProcenatPopusta(popust);
            }
        }
    }
    
    public double getVrednostTure() {
        double vrednostTure = 0.;
        for (StavkaTure stavkaTure : listStavkeTure) {
            vrednostTure += stavkaTure.getCena();
            for (StavkaTure dodatniArtikli : stavkaTure.getArtikliDodatni()) {
                vrednostTure += dodatniArtikli.getCena();
            }
        }
        return vrednostTure;
    }
    public double getVrednostTureSaObracunatimPopustom() {
        double vrednostTure = 0.;
        for (StavkaTure stavkaTure : listStavkeTure) {
            vrednostTure += stavkaTure.getCenaSaObracunatimPopustom();
            for (StavkaTure dodatniArtikli : stavkaTure.getArtikliDodatni()) {
                vrednostTure += dodatniArtikli.getCenaSaObracunatimPopustom();
            }
        }
        return vrednostTure;
    }
    
    public List<Map<String, String>> dajTuru() {
        List<Map<String, String>> list_StavkeTure = new ArrayList<>();
        
        for (StavkaTure novaStavka : this.listStavkeTure) {
            list_StavkeTure.add(novaStavka.dajStavkuTure());
    
            for (StavkaTure dodatni : novaStavka.getArtikliDodatni()) {
                list_StavkeTure.add(dodatni.dajStavkuTure());
            }
            
            for (StavkaTure opcioni : novaStavka.getArtikliOpisni()) {
                list_StavkeTure.add(opcioni.dajStavkuTure());
            }
        }
        
        return list_StavkeTure;
    }
    
    public long getTuraID() {
        return this.turaID;
    }
    
    public StavkaTure getStavkaTureByRedniBroj(int redniBr) {
        for (StavkaTure stavka : this.listStavkeTure) {
            if (stavka.getRedniBroj() == redniBr)
                return stavka;
        }
        return null;
    }

    public StavkaTure getStavkaTureByArtikalID(long artikalID) {
        for (StavkaTure stavka : this.listStavkeTure) {
            if (stavka.getArtikalID() == artikalID)
                return stavka;
        }
        return null;
    }

    public StavkaTure getStavkaTureByStavkaID(long stavkaTureID) {
        for (StavkaTure stavka : this.listStavkeTure) {
            if (stavka.getStavkaTureId() == stavkaTureID)
                return stavka;
        }
        return null;
    }
    
    public void addStavkaTure(StavkaTure novaStavka) {
        for (StavkaTure stavka : this.listStavkeTure) {
            if (novaStavka.getArtikalID() == stavka.getArtikalID()) {
                // Ukoliko postoji taj artikal u turi, dodaje na njega kolicinu
                // ukoliko taj vec nema dodatne ili opisne artikle
                if (!stavka.getImaDodatneIliOpisneArtikle()) {
                    stavka.povecajKolicinuZa(novaStavka.getKolicina());
                    return;
                }
            }
        }

        novaStavka.setRedniBroj(++this.redniBrojStavke);
        this.listStavkeTure.add(novaStavka);
    }
    
    public int getRedniBrojStavkeSledeci() {
        return ++this.redniBrojStavke;
    }
    public Tura getClone(long turaID) {
        Tura novaTura = new Tura("" + turaID, new Date());
        
        novaTura.turaID = 0;
        novaTura.setBrojStolaBroj(this.brojStolaBroj);
        
        for (StavkaTure stavkaTure : listStavkeTure) {
            stavkaTure.id = 0;
            for (StavkaTure stavkaTure1 : stavkaTure.dodatniArtikli) {
                stavkaTure1.id = 0;
            }
            for (StavkaTure stavkaTure2 : stavkaTure.opisniArtikli) {
                stavkaTure2.id = 0;
            }
        }
        return novaTura;
    }

    public Date getVremeTure() {
        return this.datum;
    }
    
    public void setRacunID (long racunID) {
        this.racunID = racunID;
    }
    
    public void destroy() {
        DBBroker db = new DBBroker();
        if (this.turaID != 0) {
            try {
                db.izbrisi("tura", "id", "" + this.turaID, Boolean.FALSE);
            } catch (Exception e) {
            }
        }
    }
    
    public void snimi() {
        DBBroker db = new DBBroker();
        long result = 0;
        HashMap<String,String> mapaTura = new HashMap();
        
        try {
            mapaTura.put("brojStola", "" + this.brojStolaBroj);
            if (this.datum == null) {
                this.datum = new Date();
            }
            mapaTura.put("datum", Utils.getStringFromDate(this.datum));
            mapaTura.put("pripremljena", "false");
            mapaTura.put("uPripremi", "false");
            mapaTura.put("RACUN_ID", "" + this.racunID);
            if (this.getTuraID() != 0)
                db.izmeni("tura", "id", "" + this.getTuraID(), mapaTura, false);
            else {
                String[] imenaArgumenata = {"settingName"};
                String[] vrednostiArgumenata = {"tura.broj.sledeci"};
                int rez = DBBroker.getValueFromFunction("getSledeciRedniBroj", imenaArgumenata, vrednostiArgumenata);
                mapaTura.put("brojTure", "" + rez);
                result = db.ubaciRed("tura", mapaTura, false);
                this.turaID = result;
            }

            for (StavkaTure stavkaTure : this.listStavkeTure) {
                stavkaTure.setRacunID(this.racunID);
                stavkaTure.setTuraID(this.getTuraID());
                stavkaTure.snimi();
            }
        } catch(Exception e) {

        }

    }

}
