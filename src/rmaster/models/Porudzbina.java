/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import java.util.ArrayList;
import java.util.List;
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
        List listStavkePorudzbine = DBBroker.getRecordSetIzStoreProcedureZaParametar(
                                "getStavkeRacuna", 
                                "racunID", 
                                "" + this.racunID
                        );
        
        // TODO: Pokupi stavke racuna, treba voditi racuna u kojoj je turi, za svaku novu kreirati novu Tura, i dodati StavkaTure u Tura
    }
    
    public void setGost(Gost gost) {
        this.gost = gost;
    }
}
