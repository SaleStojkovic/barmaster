/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import java.util.HashMap;
import java.util.List;
import rmaster.assets.DBBroker;
import rmaster.assets.QueryBuilder.QueryBuilder;


public class SettingsBaza {

    public static String TABLE_NAME = "setting";
    public static String PRIMARY_KEY = "id";
    
    public static String VREDNOST = "actual";
    public static String IME = "name";
    
    public static HashMap<String, String> vrednosti = new HashMap<>();
    
    public void getAllValues() {
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        
        query.setTableName(TABLE_NAME);
        
        DBBroker dbBroker = new DBBroker();
        
        List<HashMap<String, String>> listaRezultata = dbBroker.runQuery(query);
        
        for (HashMap<String, String> novaMapa : listaRezultata) {
            vrednosti.put(novaMapa.get(IME), novaMapa.get(VREDNOST));
        }
    }
    
    public static String getValue(String key) {
        return vrednosti.get(key);
    }
    
}
