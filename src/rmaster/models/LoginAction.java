/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import rmaster.assets.DBBroker;
import rmaster.assets.ModelBase;
import rmaster.assets.QueryBuilder;

/**
 *
 * @author Arbor
 */
public class LoginAction extends ModelBase {
    
    public static String TABLE_NAME = "sifra";
    public static String PRIMARY_KEY = "id";
    
    public String SIFARNIK = "sifarnik";
    public String SIFRA = "sifra";

    public String id;
    public String sifarnik;
    public String sifra;
    
    
    @Override
    public String getPrimaryKeyName() {
        return id;
    }
    
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
    
    @Override
    public void makeFromHashMap(HashMap<String, String> loginActionMap) 
    {
        this.id = loginActionMap.get(PRIMARY_KEY);
        
        this.sifarnik = loginActionMap.get(SIFARNIK);
        
        this.sifra = loginActionMap.get(SIFRA);

    }
    
    @Override
    public LinkedHashMap<String, String> toHashMap(boolean includeId)
    {
        LinkedHashMap<String, String> loginAction = new LinkedHashMap();

        loginAction.put(SIFARNIK, this.sifarnik);
        loginAction.put(SIFRA, this.sifra);
        
        if (includeId) {
            loginAction.put(PRIMARY_KEY, this.id);
        }
        
        return loginAction;
    }
    
    public void takeAction(String lozinkaText) {
        
        QueryBuilder query = new QueryBuilder();
        query.setTableName(TABLE_NAME);
        query.addCriteriaColumns(SIFRA);
        query.addCriteria(QueryBuilder.IS_EQUAL);
        query.setCriteriaValues(lozinkaText);
        
        DBBroker dbBroker = new DBBroker();
        
        List<Map<String, String>> listaRezultata = dbBroker.runQuery(query);
       
        if (listaRezultata.isEmpty()) {
           return;
        }
       
        String action = listaRezultata.get(0).get(SIFARNIK);
        
        //ovde se sada stavljaju akcije
        if (action.equals(LoginAction_Actions.IZLAZAK)) {
            this.exitAction();
        }
        
        
    }
    
    private void exitAction()
    {
        Platform.exit();
        System.exit(0); 
    }
    
}
