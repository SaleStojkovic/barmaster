/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import java.util.List;
import java.util.Map;
import rmaster.assets.DBBroker;
import rmaster.assets.QueryBuilder.*;

/**
 *
 * @author Arbor
 */
public class Konobar {
    
    public  long konobarID;
    public  String konobarPin;
    public  String skrImeKonobara;
    public  String imeKonobara;
    DBBroker dbBroker = new DBBroker();

    public Konobar (
            Map<String, String> konobarMap
    ) {
        this.konobarID = Long.parseLong(konobarMap.get("id"));
        this.konobarPin = konobarMap.get("pin");
        this.skrImeKonobara = konobarMap.get("skrIme");
        this.imeKonobara = konobarMap.get("punoIme");
    }
    
    public List<Map<String, String>> saleOmoguceneKonobaru(List<Map<String, String>> nizGrafikSale)
    {
        List<Map<String, String>> sale = nizGrafikSale;
        
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        
        query.setTableName("grafiksale");
        
        if (!sale.isEmpty()) {
            String saleString = query.makeStringForInCriteriaFromListByParam(sale, "grafik_id");
            query.addCriteriaColumns("id");
            query.addCriteria(QueryBuilder.IS_NOT_IN);
            query.addCriteriaValues(saleString);
        }

        return dbBroker.runQuery(query);
    }
    
    public List<Map<String, String>> saleOmoguceneKonobaru()
    {
        List<Map<String, String>> sale = this.dajNizGrafikSale();
        
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        
        query.setTableName("grafiksale");
        
        if (!sale.isEmpty()) {
            String saleString = query.makeStringForInCriteriaFromListByParam(sale, "grafik_id");
            query.addCriteriaColumns("id");
            query.addCriteria(QueryBuilder.IS_NOT_IN);
            query.addCriteriaValues(saleString);
        }

        return dbBroker.runQuery(query);
    }
    
    private List<Map<String, String>> dajNizGrafikSale()
    {
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);

        query.setTableName("grafik_konobar");
        query.setSelectColumns("grafik_id");
        query.addCriteriaColumns("konobar_id");
        query.addCriteria(QueryBuilder.IS_EQUAL);
        query.addCriteriaValues(this.konobarID + "");
        
        return dbBroker.runQuery(query);
    }
    
    public List<Map<String, String>> stoloviKojeJeKonobarZauzeo()
    {
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        TableJoin tableJoin = new TableJoin(
                "sto", 
                "stonaziv", 
                "broj", 
                "broj", 
                TableJoinTypes.INNER_JOIN
        );
        
        query.addTableJoins(tableJoin);
        query.setSelectColumns("sto.id","sto.broj","sto.blokiran","stonaziv.naziv");
        query.addCriteriaColumns("KONOBAR_ID");
        query.addCriteria(QueryBuilder.IS_EQUAL);
        query.addCriteriaValues(this.konobarID + "");
        
        return dbBroker.runQuery(query);
    }
        
    public List<Map<String, String>> stoloviZaPrikazPromeneKonobara(String salaId)
    {
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
                
        query.addTableJoins(
                new TableJoin(
                        "stoprikaz",
                        "sto",
                        "broj",
                        "broj",
                        TableJoinTypes.INNER_JOIN
                ),
                new TableJoin(
                        "stoprikaz",
                        "stonaziv",
                        "broj",
                        "broj",
                        TableJoinTypes.LEFT_JOIN
                ) 
        );
        
        query.setSelectColumns("stoprikaz.*", "stonaziv.naziv");
        query.addCriteriaColumns("sto.KONOBAR_ID", "stoprikaz.GRAFIK_ID");
        query.addCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL);
        query.addOperators(QueryBuilder.LOGIC_AND);
        query.addCriteriaValues(
                this.konobarID + "", 
                salaId
        );
        
        return dbBroker.runQuery(query);
    }  
    

    public void promenaStolova(String stariKonobarId, List<String> brojeviStolova)
    {
        // Priprema argumenta za promenu stolova -> sto IN (11,22,33)
        
        String stolovi = "";
        for (String stoBroj : brojeviStolova) {
            stolovi += stoBroj + ",";
        }
        stolovi = stolovi.substring(0, stolovi.length() - 1);
        stolovi = "(" + stolovi + ")";
        
        // Promena konobara za stolove
        QueryBuilder query = new QueryBuilder(QueryBuilder.UPDATE);
        query.setTableName("sto");
        query.setUpdateColumns("KONOBAR_ID");
        query.setUpdateColumnValues(this.konobarID + "");
        query.addCriteriaColumns("KONOBAR_ID", "broj");
        query.addCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_IN);
        query.addOperators(QueryBuilder.LOGIC_AND);
        query.addCriteriaValues(stariKonobarId, stolovi);

        dbBroker.runQuery(query);


        // Promena konobara za racune
        query = new QueryBuilder(QueryBuilder.UPDATE);
        query.setTableName("racun");
        query.setUpdateColumns("KONOBAR_ID");
        query.setUpdateColumnValues(this.konobarID + "");
        query.addCriteriaColumns("KONOBAR_ID", "zatvoren", "brojStola");
        query.addCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL, QueryBuilder.IS_IN);
        query.addOperators(QueryBuilder.LOGIC_AND, QueryBuilder.LOGIC_AND);
        query.addCriteriaValues(stariKonobarId, QueryBuilder.BIT_0, stolovi);


        dbBroker.runQuery(query);

    }
    
}
