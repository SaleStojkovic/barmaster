/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] uslovKolone = new String[1];
        uslovKolone[0] = "id";
        String[] uslovVrednosti = new String[1];
        uslovVrednosti[0] = "" + this.racunID;
        try {
            List podaci = new DBBroker().vratiKoloneIzTabele("racun", uslovKolone, uslovVrednosti);
            if (!podaci.isEmpty()) {
                Map<String,String>  porudzbina = (Map<String, String>)podaci.get(0);
                if (porudzbina != null) {
                    if (porudzbina.get("id") != null)
                        this.racunID = Long.parseLong(porudzbina.get("id"));
                    if (porudzbina.get("brojFakture") != null)
                        this.brojFakture = Integer.parseInt(porudzbina.get("brojFakture"));
                    if (porudzbina.get("brojRacuna") != null)
                        this.brojRacuna = Integer.parseInt(porudzbina.get("brojRacuna"));
                    if (porudzbina.get("brojFiskalnogIsecka") != null)
                        this.brojFiskalnogIsecka = Long.parseLong(porudzbina.get("brojFiskalnogIsecka"));
                    if (porudzbina.get("brojStola") != null)
                        this.brojStola = Integer.parseInt(porudzbina.get("brojStola"));
                    if (porudzbina.get("crnoPlacanje") != null)
                        this.crnoPlacanje = porudzbina.get("crnoPlacanje");
                    if (porudzbina.get("datum") != null)
                        this.datum = dateFormat.parse(porudzbina.get("datum"));
                    if (porudzbina.get("fiskalniOdstampan") != null)
                        this.fiskalniOdstampan = Boolean.parseBoolean(porudzbina.get("fiskalniOdstampan"));
                    if (porudzbina.get("oznakaSobe") != null)
                        this.oznakaSobe = porudzbina.get("oznakaSobe");
                    if (porudzbina.get("popust") != null)
                        this.popust = Double.parseDouble(porudzbina.get("popust"));
                    if (porudzbina.get("storniran") != null)
                        this.storniran = Boolean.parseBoolean(porudzbina.get("storniran"));
                    if (porudzbina.get("zatvoren") != null)
                        this.zatvoren = Boolean.parseBoolean(porudzbina.get("zatvoren"));
                    if (porudzbina.get("KASA_ID") != null)
                        this.KASA_ID = Long.parseLong(porudzbina.get("KASA_ID"));
                    if (porudzbina.get("KONOBAR_ID") != null)
                        this.KONOBAR_ID = Long.parseLong(porudzbina.get("KONOBAR_ID"));
                    if (porudzbina.get("STALNIGOST_ID") != null)
                        this.STALNIGOST_ID = Long.parseLong(porudzbina.get("STALNIGOST_ID"));
                    if (porudzbina.get("gost") != null)
                        this.gostInt = Integer.parseInt(porudzbina.get("gost"));
                    if (porudzbina.get("vremeIzdavanjaRacuna") != null)
                        this.vremeIzdavanjaRacuna = dateFormat.parse(porudzbina.get("vremeIzdavanjaRacuna"));
                }
                } else {
                    System.out.println("Greska pri formiranju Porudzbine!");
            }
        } catch(Exception e) {
        }
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
    
    public void zatvoriRacun() {
        this.zatvoren = true;
    }
    
    public void snimi() {
        DBBroker db = new DBBroker();
    try {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long result = 0;
        HashMap<String,String> mapa = new HashMap();
        mapa.put("brojFakture", "" + this.brojFakture);
        mapa.put("brojRacuna", "" + this.brojRacuna);
        mapa.put("brojFiskalnogIsecka", "" + this.brojFiskalnogIsecka);
        mapa.put("brojStola", "" + this.brojStola);
        mapa.put("crnoPlacanje", "" + this.crnoPlacanje);
        if (this.datum == null)
            this.datum = new Date();
        mapa.put("datum", dateFormat.format(this.datum));
        mapa.put("fiskalniOdstampan", "" + this.fiskalniOdstampan);
//        mapa.put("fiskalniOdstampan", "" + (this.fiskalniOdstampan ? "1" : "0"));
        mapa.put("oznakaSobe", "" + this.oznakaSobe);
        mapa.put("popust", "" + this.popust);
        mapa.put("storniran", "" + this.storniran);
//        mapa.put("storniran", "" + (this.storniran ? "b'1'" : "b'0'"));
        mapa.put("zatvoren", "" + this.zatvoren);
//        mapa.put("zatvoren", "" + (this.zatvoren ? "1" : "0"));
        mapa.put("KASA_ID", "" + this.KASA_ID);
        mapa.put("KONOBAR_ID", "" + this.KONOBAR_ID);
        mapa.put("STALNIGOST_ID", "" + this.STALNIGOST_ID);
        mapa.put("gost", "" + this.gost.getGostID());
        if (this.vremeIzdavanjaRacuna != null)
            mapa.put("vremeIzdavanjaRacuna", dateFormat.format(this.vremeIzdavanjaRacuna));
        
        if (this.racunID != 0)
            db.izmeni("racun", "id", "" + this.racunID, mapa, blokirana);
        else {
            result = db.ubaciRed("racun", mapa, blokirana);
            this.racunID = result;
        }
    } catch(Exception e) {
        
    }
    }
}
 