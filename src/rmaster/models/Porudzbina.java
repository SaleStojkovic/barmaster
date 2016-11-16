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
 * @author Bosko
 */
public class Porudzbina {
    private long racunID = 0;
    private List<Tura> turePorudzbine = new ArrayList(); 
    private Gost gost;
    private Tura novaTuraPorudzbine;
    
    public Porudzbina(Gost gost, long racunID) {
        this.setGost(gost);
        this.racunID = racunID;
        popuniPorudzbinuIzBaze();
    }

    public Porudzbina(Gost gost, String racunIDstring) {
        this.setGost(gost);
        this.racunID = Integer.parseInt(racunIDstring);
        popuniPorudzbinuIzBaze();
    }
    
    private void popuniPorudzbinuIzBaze() {
        String[] imenaArgumenata = {"idRacuna"};
        String[] vrednostiArgumenata = {"" + this.racunID};
        List<Map<String, String>> tureJednogGosta = DBBroker.runStoredProcedure(
                "getPorudzbinaTure", 
                imenaArgumenata, 
                vrednostiArgumenata
        );
        for (Map<String, String> tura : tureJednogGosta) {
                String turaId = tura.get("id");
                Tura turaModel = new Tura(turaId);
                this.turePorudzbine.add(turaModel);
        }

        // TODO: Pokupi stavke racuna, treba voditi racuna u kojoj je turi, za svaku novu kreirati novu Tura, i dodati StavkaTure u Tura
    }
    
    public List<Tura> getTure() {
        return this.turePorudzbine;
    }
    
    public Gost getGost() {
        return this.gost;
    }

    public void setGost(Gost gost) {
        this.gost = gost;
    }
}
