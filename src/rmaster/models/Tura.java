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
            listStavkeTure.add(novaStavka);
        }
    }
    
    
    public List<Map<String, String>> dajTuru() {
        List<Map<String, String>> list_StavkeTure = new ArrayList<>();
        
        for (StavkaTure novaStavka : this.listStavkeTure) {
            list_StavkeTure.add(novaStavka.dajStavkuTure());
        }
        
        return list_StavkeTure;
    }
    
    public long getTuraID() {
        return this.turaID;
    }
    
    public StavkaTure getStavkaTureByID(long artikalID) {
        for (StavkaTure novaStavka : this.listStavkeTure) {
            if (novaStavka.getArtikalId().equals("" + artikalID))
                return novaStavka;
        }
        return null;
    }
    
    public void addStavkaTure(StavkaTure novaStavka) {
        for (StavkaTure stavka : this.listStavkeTure) {
            if (novaStavka.getArtikalId().equals(stavka.getArtikalId())) {
                // Ukoliko postoji taj artikal u turi, dodaje na njega kolicinu
                // ukoliko taj vec nema dodatne ili opisne artikle
                if (!stavka.getImaDodatneIliOpisneArtikle()) {
                    stavka.povecajKolicinuZa(novaStavka.getKolicina());
                    return;
                }
            }
        }
        this.listStavkeTure.add(novaStavka);
    }

}
