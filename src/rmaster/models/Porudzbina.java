/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import static java.sql.JDBCType.NULL;
import java.util.ArrayList;
import java.util.Date;
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
    private boolean blokirana = false;

    private int brojFakture;
    private int brojRacuna;
    private long brojFiskalnogIsecka;
    private int brojStola;
    private String crnoPlacanje;
    private Date datum;
    private boolean fiskalniOdstampan;
    private String oznakaSobe;
    private double popust;
    private boolean storniran;
    private boolean zatvoren;
    private long KASA_ID;
    private long KONOBAR_ID;
    private long STALNIGOST_ID;
    private int gostInt;
    private Date vremeIzdavanjaRacuna;

    
    public Porudzbina(Gost gost) {
        this.setGost(gost);
    }

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
    
    private void popuniTurePorudzbineIzBaze() {
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
    
    private void popuniPorudzbinuIzBaze() {
//        String[] imenaArgumenata = {"idRacuna"};
//        String[] vrednostiArgumenata = {"" + this.racunID};
//        List<Map<String, String>> tureJednogGosta = DBBroker.runStoredProcedure(
//                "getPorudzbinaTure", 
//                imenaArgumenata, 
//                vrednostiArgumenata
//        );
//        for (Map<String, String> tura : tureJednogGosta) {
//                String turaId = tura.get("id");
//                Tura turaModel = new Tura(turaId);
//                this.turePorudzbine.add(turaModel);
//        }

        // TODO: Pokupi stavke racuna, treba voditi racuna u kojoj je turi, za svaku novu kreirati novu Tura, i dodati StavkaTure u Tura
        popuniTurePorudzbineIzBaze();
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
    
    public boolean getBlokiranaPorudzbina() {
        return this.blokirana;
    }
    
    public void setBlokiranaPorudzbina(boolean blokiranaPoruzbina) {
        this.blokirana = blokiranaPoruzbina;
    }
    
    public void snimi() {
        DBBroker db = new DBBroker();
    try {
        long result = 0;
        result = db.ubaciRed("racun", null, blokirana);
        this.racunID = result;
    } catch(Exception e) {
        
    }
    }
}
 