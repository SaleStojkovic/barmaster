/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.search.QueryWrapperFilter;
import rmaster.assets.DBBroker;
import rmaster.assets.QueryBuilder.QueryBuilder;
import rmaster.assets.QueryBuilder.TableJoin;
import rmaster.assets.QueryBuilder.TableJoinTypes;

/**
 *
 * @author Arbor
 */
public class Sto {


    public String stoNaziv;
    public String stoBroj;
    public String stoId;
    
    public Sto(HashMap<String, String> stoMapa)
    {
        this.stoNaziv = stoMapa.get("stoNaziv");

        this.stoBroj = stoMapa.get("stoBroj");
               
        this.stoId = stoMapa.get("stoId");
      
    }
    
    public List<Map<String, String>> getPorudzbineStola()
    {
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
     
        query.addTableJoins(
                new TableJoin("racun", "stoprikaz", "brojStola", "broj", TableJoinTypes.LEFT_JOIN)
        );
        
        query.setSelectColumns("racun.*");
        
        query.addCriteriaColumns("stoprikaz.id", "racun.zatvoren", "racun.storniran");
        query.addCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL);
        query.addOperators(QueryBuilder.LOGIC_AND, QueryBuilder.LOGIC_AND);
        query.addCriteriaValues(this.stoId, "0", "0");
        
        query.addOrderByColumns("gost");
        query.addOrderByCriterias(QueryBuilder.SORT_ASC);
        
        DBBroker dbBroker = new DBBroker();
        
        List<Map<String, String>> listaRezultata = dbBroker.runQuery(query);
        
        return listaRezultata;
    }
    
}
