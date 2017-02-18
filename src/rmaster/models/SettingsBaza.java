/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import rmaster.assets.DBBroker;
import rmaster.assets.ModelBase;
import rmaster.assets.QueryBuilder.QueryBuilder;

/**
 *
 * @author Arbor
 */
public class SettingsBaza extends ModelBase {

    public static String TABLE_NAME = "setting";
    public static String PRIMARY_KEY = "id";
    
    public static String VREDNOST = "actual";
    public static String IME = "name";
    
    public HashMap<String, String> vrednosti = new HashMap<>();
    
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
        //Nema potrebe za ovim
    }

    @Override
    public LinkedHashMap<String, String> toHashMap(boolean includeId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void getAllValues() {
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        
        query.setTableName(TABLE_NAME);
        
        DBBroker dbBroker = new DBBroker();
        
        List<HashMap<String, String>> listaRezultata = dbBroker.runQuery(query);
        
        for (HashMap<String, String> novaMapa : listaRezultata) {
            vrednosti.put(novaMapa.get(IME), novaMapa.get(VREDNOST));
        }
    }
    
}
