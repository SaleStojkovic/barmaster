/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rmaster.assets.DBBroker;
import rmaster.assets.QueryBuilder.QueryBuilder;
import rmaster.assets.RM_Datetime;
import rmaster.assets.Utils;

/**
 *
 * @author Bosko
 */
public class Porudzbina {
    private long racunID = 0;
    private List<Tura> turePorudzbine = new ArrayList(); 
    private Gost gost;
    //private Tura novaTuraPorudzbine;
    private boolean blokirana = false;

    private String brojFakture;
    private int brojRacuna;
    private long brojFiskalnogIsecka;
    private int brojStolaID;
    private int brojStolaBroj;
    private String crnoPlacanje;
    private Date datum;
    private boolean fiskalniOdstampan;
    private String oznakaSobe;
    private double popust;
    private boolean storniran;
    private boolean zatvoren;
    private long KASA_ID;
    private long KONOBAR_ID;
    //private long STALNIGOST_ID;
    private int gostInt;
    private Date vremeIzdavanjaRacuna;

    private StalniGost stalniGost;
    
    public Porudzbina(
            Gost gost, 
            String izabraniStoId, 
            String izabraniStoBroj
    )
    {
        this.setGost(gost);
        this.brojStolaID = Integer.parseInt(izabraniStoId);
        this.brojStolaBroj = Integer.parseInt(izabraniStoBroj);
    }

    public void setPopust(double popust) {
        this.popust = popust;
        for (Tura tura : turePorudzbine) {
            tura.setPopust(popust);
        }
    }
    
    public Date getVremeIzdavanjaRacuna() {
        return vremeIzdavanjaRacuna;
    }
    public void setVremeIzdavanjaRacuna(Date vreme) {
        this.vremeIzdavanjaRacuna = vreme;
    }
    
    public String getGodinaIzdavanjaRacuna() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.vremeIzdavanjaRacuna);
        return "" + cal.get(Calendar.YEAR);
    }
    
    public double getVrednostPorudzbine() {
        double vrednostPorudzbine = 0.;
        for (Tura tura : turePorudzbine) {
            vrednostPorudzbine += tura.getVrednostTure();
        }
        return vrednostPorudzbine;
    }
    public double getVrednostPorudzbineSaObracunatimPopustom() {
        double vrednostPorudzbine = 0.;
        for (Tura tura : turePorudzbine) {
            vrednostPorudzbine += tura.getVrednostTureSaObracunatimPopustom();
        }
        return vrednostPorudzbine;
    }
    
    public Porudzbina(
            Gost gost, 
            long racunID, 
            String izabraniStoId,
            String izabraniStoBroj
    ) {
        this.setGost(gost);
        this.racunID = racunID;
        this.brojStolaID = Integer.parseInt(izabraniStoId);
        this.brojStolaBroj = Integer.parseInt(izabraniStoBroj);
        popuniPorudzbinuIzBaze();
    }

    public Porudzbina(
            Gost gost, 
            String racunIDstring,
            String izabraniStoId,
            String izabraniStoBroj
    ) {
        this.setGost(gost);
        this.racunID = Integer.parseInt(racunIDstring);
        this.brojStolaID = Integer.parseInt(izabraniStoId);
        this.brojStolaBroj = Integer.parseInt(izabraniStoBroj);
        popuniPorudzbinuIzBaze();
    }
    
    public int getBrojStolaBroj() {
        return this.brojStolaBroj;
    }
    
    private void popuniTurePorudzbineIzBaze() {
        String[] imenaArgumenata = {"idRacuna"};
        String[] vrednostiArgumenata = {"" + this.racunID};
        
        List<Map<String, String>> tureJednogGosta = DBBroker.runStoredProcedure("getPorudzbinaTure", 
                imenaArgumenata, 
                vrednostiArgumenata);
        
        for (Map<String, String> tura : tureJednogGosta) {
                String turaId = tura.get("id");
                String datum = tura.get("datum");
 
                RM_Datetime datumD = new RM_Datetime(datum);

                Tura turaModel = new Tura(turaId, datumD.getDate());
                turaModel.setBrojStolaBroj(this.getBrojStolaBroj());
        
                this.turePorudzbine.add(turaModel);
        }

        // TODO: Pokupi stavke racuna, treba voditi racuna u kojoj je turi, za svaku novu kreirati novu Tura, i dodati StavkaTure u Tura
    }
    
    private void popuniPorudzbinuIzBaze() 
    {
        
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        query.setTableName("racun");
        query.addCriteriaColumns("id");
        query.addCriteria(QueryBuilder.IS_EQUAL);
        query.addCriteriaValues(this.racunID + "");
        
        List podaci = new DBBroker().runQuery(query);
            if (!podaci.isEmpty()) {
                
                Map<String,String>  porudzbina = (Map<String, String>)podaci.get(0);
                
                if (porudzbina != null) {
                    
                    if (porudzbina.get("id") != null) {
                        
                        this.racunID = Long.parseLong(porudzbina.get("id"));
                    }
                    if (porudzbina.get("brojFakture") != null) {
                        
                        this.brojFakture = porudzbina.get("brojFakture");
                    }
                    if (porudzbina.get("brojRacuna") != null) {
                        
                        this.brojRacuna = Integer.parseInt(porudzbina.get("brojRacuna"));
                    }
                    
                    if (porudzbina.get("brojFiskalnogIsecka") != null && porudzbina.get("brojFiskalnogIsecka").equals(0)) {
                        
                        this.brojFiskalnogIsecka = Long.parseLong(porudzbina.get("brojFiskalnogIsecka"));
                    }
                    
                    if (porudzbina.get("brojStola") != null) {
                        
                        this.brojStolaBroj = Integer.parseInt(porudzbina.get("brojStola"));
                    }
                    
                    if (porudzbina.get("crnoPlacanje") != null) { 
                        
                        this.crnoPlacanje = porudzbina.get("crnoPlacanje");
                    }
                    
                    if (porudzbina.get("datum") != null) {
                        
                        RM_Datetime date = new RM_Datetime(porudzbina.get("datum"));
                       
                        this.datum = date.getDate();
                    }
                        
                    if (porudzbina.get("fiskalniOdstampan") != null) {
                        
                        this.fiskalniOdstampan = Boolean.parseBoolean(porudzbina.get("fiskalniOdstampan"));
                    }
                    
                    if (porudzbina.get("oznakaSobe") != null) {
                        
                        this.oznakaSobe = porudzbina.get("oznakaSobe");
                    }
                    if (porudzbina.get("popust") != null) {
                        
                        this.popust = Double.parseDouble(porudzbina.get("popust"));
                    }
                    
                    if (porudzbina.get("storniran") != null) {
                        this.storniran = Boolean.parseBoolean(porudzbina.get("storniran"));
                    }
                   
                    if (porudzbina.get("zatvoren") != null) { 
                       
                        this.zatvoren = Boolean.parseBoolean(porudzbina.get("zatvoren"));
                    }
                    if (porudzbina.get("KASA_ID") != null) { 
                     
                        this.KASA_ID = Long.parseLong(porudzbina.get("KASA_ID"));
                    }
                    
                    if (porudzbina.get("KONOBAR_ID") != null) {
                        
                        this.KONOBAR_ID = Long.parseLong(porudzbina.get("KONOBAR_ID"));
                    }
                    
                    if (porudzbina.get("STALNIGOST_ID") != null && !porudzbina.get("STALNIGOST_ID").equals("0")) {
                        
                        this.stalniGost = new StalniGost();
                        this.stalniGost.getInstance(porudzbina.get("STALNIGOST_ID"));
                    }
                    
                    if (porudzbina.get("gost") != null) {
                        this.gostInt = Integer.parseInt(porudzbina.get("gost"));
                    }
                    
                    if (porudzbina.get("vremeIzdavanjaRacuna") != null) {
                        
                        RM_Datetime date = new RM_Datetime(porudzbina.get("vremeIzdavanjaRacuna"));
                       
                        this.vremeIzdavanjaRacuna = date.getDate();
                       
                    }
                       
                }
                
            } else {
                System.out.println("Greska pri formiranju Porudzbine!");
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
        DBBroker db = new DBBroker();
        snimi();
        db.zatvoriRacunIOslobodiSto(this);
        this.zatvoren = true;
    }
    
    public void snimi() {
        DBBroker db = new DBBroker();
        try {
            //java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long result = 0;
            HashMap<String,String> mapa = new HashMap();
            if ((this.brojFakture != null) && (!this.brojFakture.equals("")))
                mapa.put("brojFakture", this.brojFakture);
            mapa.put("brojRacuna", "" + this.brojRacuna);
            if (this.brojFiskalnogIsecka!=0)
                mapa.put("brojFiskalnogIsecka", "" + this.brojFiskalnogIsecka);
            mapa.put("popust", "" + this.popust);
            mapa.put("brojStola", "" + this.brojStolaBroj);
            if (this.crnoPlacanje!=null && !(this.crnoPlacanje.equals("") || this.crnoPlacanje.equals("0") || this.crnoPlacanje.equals("null")))
                mapa.put("crnoPlacanje", "" + this.crnoPlacanje);
            
            if (this.datum == null) {
                
                RM_Datetime rmDatum = new RM_Datetime();
              
                this.datum = rmDatum.getDate();
            }
            
            mapa.put("datum", Utils.getStringFromDate(this.datum));
            mapa.put("fiskalniOdstampan", "" + this.fiskalniOdstampan);
    //        mapa.put("fiskalniOdstampan", "" + (this.fiskalniOdstampan ? "1" : "0"));
            if (this.oznakaSobe!=null && !(this.oznakaSobe.equals("") || this.oznakaSobe.equals("0") || this.oznakaSobe.equals("null")))
                mapa.put("oznakaSobe", "" + this.oznakaSobe);
            mapa.put("popust", "" + this.popust);
            mapa.put("storniran", "" + this.storniran);
    //        mapa.put("storniran", "" + (this.storniran ? "b'1'" : "b'0'"));
            mapa.put("zatvoren", "" + this.zatvoren);
    //        mapa.put("zatvoren", "" + (this.zatvoren ? "1" : "0"));
            mapa.put("KASA_ID", "" + this.KASA_ID);
            mapa.put("KONOBAR_ID", "" + rmaster.RMaster.ulogovaniKonobar.konobarID);
            if (this.stalniGost != null)
                mapa.put("STALNIGOST_ID", "" + this.stalniGost.id);
            mapa.put("gost", "" + this.gost.getGostID());
            if (this.vremeIzdavanjaRacuna != null)
                mapa.put("vremeIzdavanjaRacuna", Utils.getStringFromDate(this.vremeIzdavanjaRacuna));

            if (this.racunID != 0)
                db.izmeni("racun", "id", "" + this.racunID, mapa, blokirana);
            else {
                result = db.ubaciRed("racun", mapa, blokirana);
                this.racunID = result;
            }

            if (!this.zatvoren) {
                // Upisi u bazu u tabelu sto da je sto zauzet
                HashMap<String,String> mapaSto = new HashMap();

                mapaSto.put("blokiran", "false");
                mapaSto.put("broj", "" + this.brojStolaBroj);
                mapaSto.put("KONOBAR_ID", "" + rmaster.RMaster.ulogovaniKonobar.konobarID);
                try {
                    result = db.ubaciRed("sto", mapaSto, blokirana);
                } catch (Exception e) {
                }
            }

            for (Tura tura : turePorudzbine) {
                tura.setBrojStolaBroj(this.brojStolaBroj);
                tura.setRacunID(this.racunID);
                tura.snimi();
            }
        } catch(Exception e) {

        }
    }
    
    public long getID() {
        return this.racunID;
    }
    public void setID(long id) {
        this.racunID = id;
    }

    public int getStoID() {
        return this.brojStolaID;
    }
    public double getPopustDouble() {
        if (this.stalniGost != null)
            return Utils.getDoubleFromString(this.stalniGost.popust);
        else
            return 0.;
    }
    public String getPopustString() {
        if (this.stalniGost != null)
            return this.stalniGost.popust;
        else
            return Utils.getStringFromDouble(0.);
    }
    
    // Stalni gost
    public StalniGost getStalniGost() {
        return this.stalniGost;
    }
    public void setStalniGost(StalniGost stalniGost) {
        this.stalniGost = stalniGost;
    }

    // Broj fakture
    public String getBrojFakture() {
        return this.brojFakture;
    }
    public void setBrojFakture(String brojFakt) {
        this.brojFakture = brojFakt;
    }

    // Broj racuna
    public String getBrojRacuna() {
        return "" + this.brojRacuna;
    }
    public void setBrojRacunaBroj(int brojRacunaBroj) {
        this.brojRacuna = brojRacunaBroj;
    }

}
 