/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
        
        List<Map<String, String>> listaRezultata = DBBroker.runStoredProcedure(
                "getPorudzbinaTuraStavke", 
                imenaArgumenata, 
                vrednostiArgumenata
        );
        
        for (Map<String, String> noviRed : listaRezultata) {
            StavkaTure novaStavka = new StavkaTure(noviRed);
            if (novaStavka.getGlavnaStavkaID() != 0) {
                if (novaStavka.cena == 0) {
                    novaStavka.naziv = "--> " + novaStavka.naziv;
                    this.getStavkaTureByStavkaID(novaStavka.getGlavnaStavkaID()).dodajKolicinuArtikalOpisni(novaStavka);
                }
                else {
                    novaStavka.naziv = "-> " + novaStavka.naziv;
                    this.getStavkaTureByStavkaID(novaStavka.getGlavnaStavkaID()).dodajKolicinuArtikalDodatni(novaStavka);
                }
            }
            else
                listStavkeTure.add(novaStavka);
        }
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
        return novaTura;
    }

    public Date getVremeTure() {
        return this.datum;
    }

}
