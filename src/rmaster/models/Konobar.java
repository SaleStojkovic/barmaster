/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import java.util.List;
import java.util.Map;
import rmaster.assets.DBBroker;
import rmaster.assets.QueryBuilder;
import rmaster.assets.TableJoin;
import rmaster.assets.TableJoinTypes;

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
    
    public List<Map<String, String>> saleOmoguceneKonobaru()
    {
        List<Map<String, String>> sale = this.dajNizGrafikSale();
        
        QueryBuilder query = new QueryBuilder();
        String saleString = query.makeStringForInCriteriaFromListByParam(sale, "grafik_id");
        
        query.setTableName("grafiksale");
        query.addCriteriaColumns("id");
        query.addCriteria(QueryBuilder.IS_NOT_IN);
        query.addCriteriaValues(saleString);
        
        return dbBroker.runQuery(query);
    }
    
    private List<Map<String, String>> dajNizGrafikSale()
    {
        QueryBuilder query = new QueryBuilder();

        query.setTableName("grafik_konobar");
        query.setColumns("grafik_id");
        query.addCriteriaColumns("konobar_id");
        query.addCriteria(QueryBuilder.IS_EQUAL);
        query.addCriteriaValues(this.konobarID + "");
        
        return dbBroker.runQuery(query);
    }
    
    public List<Map<String, String>> stoloviKojeJeKonobarZauzeo()
    {
        QueryBuilder query = new QueryBuilder();
        TableJoin tableJoin = new TableJoin(
                "sto", 
                "stonaziv", 
                "broj", 
                "broj", 
                TableJoinTypes.INNER_JOIN
        );
        
        query.addTableJoins(tableJoin);
        query.setColumns("sto.id","sto.broj","sto.blokiran","stonaziv.naziv");
        query.addCriteriaColumns("KONOBAR_ID");
        query.addCriteria(QueryBuilder.IS_EQUAL);
        query.addCriteriaValues(this.konobarID + "");
        
        return dbBroker.runQuery(query);
    }
                
    public List<Map<String, String>> stoloviZaPrikazPromeneKonobara(String salaId)
    {
        QueryBuilder query = new QueryBuilder();
        
        List<Map<String, String>> stoloviKojeJeKonobarZauzeo = this.stoloviKojeJeKonobarZauzeo();
        
        query.addTableJoins(
                new TableJoin(
                        "stoprikaz",
                        "stonaziv",
                        "broj",
                        "broj",
                        TableJoinTypes.LEFT_JOIN
                ),
                new TableJoin(
                        "stoprikaz",
                        "sto",
                        "broj",
                        "broj",
                        TableJoinTypes.LEFT_JOIN
                ),
                new TableJoin(
                        "stoprikaz",
                        "rezervacija",
                        "broj",
                        "brStola",
                        TableJoinTypes.LEFT_JOIN
                )
        );
        
        query.addCriteriaColumns("stoprikaz.GRAFIK_ID", "sto.id");
        query.addCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_IN);
        query.addOperators(QueryBuilder.LOGIC_AND);
        query.addCriteriaValues(
                salaId, 
                query.makeStringForInCriteriaFromListByParam(stoloviKojeJeKonobarZauzeo, "id"));
        
        return dbBroker.runQuery(query);
    }            
}
