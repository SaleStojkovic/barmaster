/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 *
 * @author Arbor
 */
public abstract class ModelBase extends Object {
    
    public abstract LinkedHashMap<String, String> toHashMap(boolean addId);
    
    LinkedHashMap<String, String> columnValues = new LinkedHashMap<>();

    DBBroker dbBroker = new DBBroker();
    
    public Class<?> clazz = this.getClass();

    public String tableName;
    
    String[] columnNames; 
    
    public void construct(boolean includeId) {
        
        columnValues = this.toHashMap(includeId);
        
        Set<String> keys = columnValues.keySet();
        
        columnNames = keys.toArray(new String[keys.size()]);
        
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals("TableName")) {
                try {
                    Object fieldValue = field.get(clazz);
                    tableName = fieldValue + "";    
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        }
    }
    
    public void save(boolean closeConnection) {
        construct(false);
        try {
                dbBroker.ubaci(
                        tableName, 
                        columnValues,
                        closeConnection
                );
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
