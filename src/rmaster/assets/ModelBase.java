/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 *
 * @author Arbor
 */
public abstract class ModelBase extends Object {
    
    LinkedHashMap<String, String> columnValues = new LinkedHashMap<>();

    DBBroker dbBroker = new DBBroker();
    
    public Class<?> clazz = this.getClass();

    String[] columnNames; 
    
    
    public abstract LinkedHashMap<String, String> toHashMap(boolean includeId);
    
    public abstract void makeFromHashMap(HashMap<String, String> HashMap);

    
    //Vraca ime tabele
    public abstract String getTableName();
    
    //Vraca ime PK
    public abstract String getPrimaryKeyName();
    
    
    public void getColumnNames(boolean includeId) {
        
        columnValues = this.toHashMap(includeId);
        
        Set<String> keys = columnValues.keySet();
        
        columnNames = keys.toArray(new String[keys.size()]);
    }
    
    
    public void save(boolean closeConnection) {
        try {
                dbBroker.ubaci(
                        this.getTableName(), 
                        this.toHashMap(false),
                        closeConnection
                );
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    public void saveChanges(boolean closeConnection) 
    {
        try {
            dbBroker.izmeni(
                 this.getTableName(), 
                 this.getPrimaryKeyName(), 
                 this.toHashMap(true).get(this.getPrimaryKeyName()), 
                 this.toHashMap(false),
                 closeConnection
         );
            } catch (Exception e) {
                System.err.println(e);
        }
    }
    
    public void delete(boolean closeConnection)
    {
        try {
            dbBroker.izbrisi(
                    this.getTableName(), 
                    this.getPrimaryKeyName(), 
                    this.toHashMap(true).get(this.getPrimaryKeyName()), 
                    closeConnection    
                    );
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    
}
