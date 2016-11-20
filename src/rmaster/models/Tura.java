/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import rmaster.assets.DBBroker;

/**
 *
 * @author Arbor
 */
public class Tura {
    
    public List<StavkaTure> listStavkeTure = new ArrayList<>();
    public long turaID = 0;
    private long gostID = 0;
    public int redniBrojStavke = 0;
    
    public Tura () {
    }
    public Tura (
            String idTure
    ) { 
        this.turaID = Integer.parseInt(idTure);
        
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
                    novaStavka.imeArtikla = "--> " + novaStavka.imeArtikla;
                    this.getStavkaTureByStavkaID(novaStavka.getGlavnaStavkaID()).dodajKolicinuArtikalOpisni(novaStavka);
                }
                else {
                    novaStavka.imeArtikla = "-> " + novaStavka.imeArtikla;
                    this.getStavkaTureByStavkaID(novaStavka.getGlavnaStavkaID()).dodajKolicinuArtikalDodatni(novaStavka);
                }
            }
            else
                listStavkeTure.add(novaStavka);
        }
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
        Tura novaTura = new Tura("" + turaID);
        novaTura.turaID = 0;
        return novaTura;
    }

}
