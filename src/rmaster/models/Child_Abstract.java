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
import rmaster.assets.ModelBase;
import rmaster.assets.QueryBuilder.QueryBuilder;

/**
 *
 * @author Arbor
 */
public abstract class Child_Abstract extends ModelBase {
    
    public static String TABLE_NAME = "artikal";
 
    public static String PRIMARY_KEY = "id";

    public static String BAR_CODE = "barCode";
    public static String CENA = "cena";
    public static String DOZVOLJEN_POPUST = "dozvoljenPopust";
    public static String JEDINICA_MERE = "jedinicaMere";
    public static String NAZIV = "name";
    public static String PRIORITET = "prioritet";
    public static String SKRACENI_NAZIV = "skrNaziv";
    public static String SLIKA = "slika";
    public static String STAMPAC_ID = "stampacID";
    public static String TIP_ARTIKLA = "tip";
    
    
    public String id;
    public String barCode;
    public String cena;
    public String dozvoljenPopust;
    public String jedinicaMere;
    public String naziv;
    public String prioritet;
    public String skrNaziv;
    public String slika;
    public String stampacID;
    public String tip;
    
    public List<Child_Abstract> childList = new ArrayList<>();
    
    protected void setType(String artikalId) {
        DBBroker dbBroker = new DBBroker();
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        
        query.setTableName("artikal_dodaci");
        query.setSelectColumns("COUNT(id)");
        
        query.addCriteriaColumns("ArtikalIDGlavni");
        query.addCriteria(QueryBuilder.IS_EQUAL);
        query.addCriteriaValues(artikalId);
        
        List<Map<String, String>> listaRezultata = dbBroker.runQuery(query);
        
        int prviKriterijum = Integer.parseInt(listaRezultata.get(0).get("COUNT(id)"));
                
        QueryBuilder query2 = new QueryBuilder(QueryBuilder.SELECT);
        
        query2.setTableName("artikal_atribut");
        query2.setSelectColumns("COUNT(id)");
        
        query2.addCriteriaColumns("artikalID");
        query2.addCriteria(QueryBuilder.IS_EQUAL);
        query2.addCriteriaValues(artikalId);
        
        List<Map<String, String>> listaRezultata2 = dbBroker.runQuery(query2);
        
        int drugiKriterijum = Integer.parseInt(listaRezultata2.get(0).get("COUNT(id)"));
        
        tip = "PROS";

        if (prviKriterijum + drugiKriterijum > 0) {
            tip = "SLOZ";
        }

    }
    
}
