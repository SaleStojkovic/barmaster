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
    
    public List<StavkaTure> listTura = new ArrayList<>();
    
    
    public Tura (
            String idTure
    ) {        
        DBBroker dbBroker = new DBBroker();
                
        String[] imenaArgumenata = {"idTure"};
        
        String[] vrednostiArgumenata = {idTure};
        
        List<Map<String, String>> listaRezultata = dbBroker.runStoredProcedure(
                "getPorudzbinaTuraStavke", 
                imenaArgumenata, 
                vrednostiArgumenata
        );
        
        for (Map<String, String> noviRed : listaRezultata) {
            StavkaTure novaStavka = new StavkaTure(noviRed);
            listTura.add(novaStavka);
        }
    }
    
    
    public List<Map<String, String>> dajTuru() {
        List<Map<String, String>> listaTura = new ArrayList<>();
        
        for (StavkaTure novaStavka : this.listTura) {
            listaTura.add(novaStavka.dajStavkuTure());
        }
        
        return listaTura;
    }
    
}
