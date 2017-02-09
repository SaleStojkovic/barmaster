/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models.Artikal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import rmaster.assets.DBBroker;
import rmaster.assets.ModelBase;
import rmaster.assets.QueryBuilder.QueryBuilder;
import rmaster.assets.QueryBuilder.TableJoin;
import rmaster.assets.QueryBuilder.TableJoinTypes;

/**
 *
 * @author Arbor
 */
public class Podgrupa extends ModelBase implements Child_Interface {

    public static String TABLE_NAME = "grupaartikalafront";
    public static String PRIMARY_KEY = "id";
    
    public static String NAZIV = "naziv";
    public static String PRIKAZ_NA_EKRAN = "prikazNaEkran";
    public static String PRIORITET = "prioritet";
    public static String SKR_NAZIV = "skrNaziv";
    public static String SLIKA = "slika";
    public static String NADREDJENA_GRUPA_ID = "GRUPA_ID";

    public String id;
    public String naziv;
    public String prikazNaEkran;
    public String prioriter;
    public String skrNaziv;
    public String slika;
    public String nadredjenaGrupaId;
    
    public List<Child_Abstract> artikli = new ArrayList<>();
    
    @Override
    public String getPrimaryKeyName() {
        return PRIMARY_KEY;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public void makeFromHashMap(HashMap<String, String> HashMap) {
        this.id = HashMap.get(PRIMARY_KEY);
        
        this.naziv = HashMap.get(NAZIV);
        
        this.prikazNaEkran = HashMap.get(PRIKAZ_NA_EKRAN);
        
        this.prioriter = HashMap.get(PRIORITET);
        
        this.skrNaziv = HashMap.get(SKR_NAZIV);
        
        this.slika = HashMap.get(SLIKA);
        
        this.nadredjenaGrupaId = HashMap.get(NADREDJENA_GRUPA_ID);
    }

    @Override
    public LinkedHashMap<String, String> toHashMap(boolean includeId) {
        
        LinkedHashMap<String, String> podgrupa = new LinkedHashMap();

        podgrupa.put(naziv, NAZIV);
        podgrupa.put(prikazNaEkran, PRIKAZ_NA_EKRAN);
        podgrupa.put(prioriter, PRIORITET);
        podgrupa.put(skrNaziv, SKR_NAZIV);
        podgrupa.put(slika, SLIKA);
        podgrupa.put(nadredjenaGrupaId, NADREDJENA_GRUPA_ID);
        
        if (includeId) {
            podgrupa.put(PRIMARY_KEY, this.id);
        }
        
        return podgrupa;
    }
    
    @Override
    public void setAllChildren() {
        
        DBBroker dbBroker = new DBBroker();
        
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        
        query.addTableJoins(
                new TableJoin("artikal", "artikal_grupa", "id", "artikalID", TableJoinTypes.INNER_JOIN),
                new TableJoin("artikal", "artikal_stampac", "id", "artikalID", TableJoinTypes.INNER_JOIN)
        );
        
        query.setSelectColumns(
                "artikal.id",
                "artikal.barCode",
                "artikal.cena",
                "artikal.dozvoljenPopust",
                "artikal.jedinicaMere",
                "artikal.name",
                "artikal.prioritet",
                "artikal.skrNaziv",
                "artikal.slika",
                "artikal_stampac.stampacID"
        );
        
        query.addCriteriaColumns("artikal.blokiran", "artikal_grupa.grupaID", "artikal.artikalVrstaID");
        query.addCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL);
        query.addOperators(QueryBuilder.LOGIC_AND, QueryBuilder.LOGIC_AND);
        query.addCriteriaValues(QueryBuilder.BIT_0, this.id, "1");
        query.addOrderByColumns("artikal.prioritet", "artikal.name");
        query.addOrderByCriterias(QueryBuilder.SORT_ASC, QueryBuilder.SORT_ASC);
        
        List<HashMap<String, String>> listaArtikala = dbBroker.runQuery(query);
        
        //dodavanje artikala
        for(HashMap<String, String> artikalMap : listaArtikala) {
            
            String rezultat = this.getType(artikalMap.get("id"));
            
            this.addChild(rezultat, artikalMap);   
        }
    }
    
    public String getType(String artikalId) {
        
        DBBroker dbBroker = new DBBroker();
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        
        query.setTableName("artikal_slozeni");
        query.setSelectColumns("COUNT(id)");
        
        query.addCriteriaColumns("artikalID");
        query.addCriteria(QueryBuilder.IS_EQUAL);
        query.addCriteriaValues(artikalId);
        
        List<Map<String, String>> listaRezultata = dbBroker.runQuery(query);
        
        int prviKriterijum = Integer.parseInt(listaRezultata.get(0).get("COUNT(id)"));
                
        String tipArtikla = "PROST";

        if (prviKriterijum > 0) {
            tipArtikla = "SLOZEN";
        }
        
        return tipArtikla;
    }
    
    public void addChild(String rezultat, HashMap<String, String> artikalMap) {
        
        if (rezultat.equals("PROST")) {
            Artikal_Prosti noviProst = new Artikal_Prosti(artikalMap);
            this.artikli.add(noviProst);
            return;
        }
        
        Artikal_Slozeni noviSlozeni = new Artikal_Slozeni(artikalMap);
        this.artikli.add(noviSlozeni);

    }
}
