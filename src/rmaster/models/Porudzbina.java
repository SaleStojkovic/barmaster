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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import rmaster.assets.DBBroker;
import rmaster.assets.Utils;

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
        this.brojStola = Integer.parseInt(rmaster.RMaster.izabraniSto);
    }

    public Porudzbina(Gost gost, long racunID) {
        this.setGost(gost);
        this.racunID = racunID;
        this.brojStola = Integer.parseInt(rmaster.RMaster.izabraniSto);
        popuniPorudzbinuIzBaze();
    }

    public Porudzbina(Gost gost, String racunIDstring) {
        this.setGost(gost);
        this.racunID = Integer.parseInt(racunIDstring);
        this.brojStola = Integer.parseInt(rmaster.RMaster.izabraniSto);
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
                String datum = tura.get("datum");
                Date datumD = new Date();
                try {
                    datumD = Utils.getDateFromString(datum);
                } catch (ParseException e) {
                    System.out.println("Neuspela konverzija stringa u datum za vreme ture!");
                }
                Tura turaModel = new Tura(turaId, datumD);
                this.turePorudzbine.add(turaModel);
        }

        // TODO: Pokupi stavke racuna, treba voditi racuna u kojoj je turi, za svaku novu kreirati novu Tura, i dodati StavkaTure u Tura
    }
    
    private void popuniPorudzbinuIzBaze() {
        //java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                        try {
                            this.datum = Utils.getDateFromString(porudzbina.get("datum"));
                        } catch (ParseException e) {
                            System.out.println("Neuspelo pretvaranje stringa u datum za kreiranje porudzbine - datum!");
                        }
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
                        try {
                            this.vremeIzdavanjaRacuna = Utils.getDateFromString(porudzbina.get("vremeIzdavanjaRacuna"));
                        } catch (ParseException e) {
                            System.out.println("Neuspelo pretvaranje stringa u datum za kreiranje porudzbine - vremeIzdavanjaRacuna!");
                        }
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
    
    public void setNovaTuraPorudzbine(Tura tura) {
        this.novaTuraPorudzbine = tura;
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
        mapa.put("KONOBAR_ID", "2");// + rmaster.RMaster.ulogovaniKonobar);
        mapa.put("STALNIGOST_ID", "" + this.STALNIGOST_ID);
        mapa.put("gost", "" + this.gost.getGostID());
        if (this.vremeIzdavanjaRacuna != null)
            mapa.put("vremeIzdavanjaRacuna", dateFormat.format(this.vremeIzdavanjaRacuna));
        
        if (this.racunID != 0)
            db.izmeni("racun", "id", "" + this.racunID, mapa, blokirana);
        else {
            result = db.ubaciRed("racun", mapa, blokirana);
            this.racunID = result;
            
            for (Tura tura : turePorudzbine) {
                if (tura.getTuraID() == 0) {
                    // Tura nije upisana, upisi je u bazu
                    HashMap<String,String> mapaTura = new HashMap();
                    
                    
                    mapaTura.put("brojStola", "" + this.brojStola);
                    mapaTura.put("brojTure", "223344");
                    mapaTura.put("datum", dateFormat.format(new Date()));
                    mapaTura.put("pripremljena", "false");
                    mapaTura.put("uPripremi", "false");
                    //mapaTura.put("fiskalniOdstampan", "" + (this.fiskalniOdstampan ? "1" : "0"));
                    mapaTura.put("RACUN_ID", "" + this.racunID);
                    result = db.ubaciRed("tura", mapaTura, blokirana);
                    tura.turaID = result;
                    
                    for (StavkaTure stavkaTure : tura.listStavkeTure) {
                        stavkaTure.setRacunID(this.racunID);
                        stavkaTure.setTuraID(tura.getTuraID());
                        stavkaTure.snimi();
                    }
                }
            }
        }
    } catch(Exception e) {
        
    }
    }
}
 