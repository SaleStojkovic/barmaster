/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author Arbor
 */
public class RM_Datetime {
    
    private Date date = new Date();
    
    public RM_Datetime() {
        
        try {
            
            String dateString = getServerTime();
            date =  Utils.getDateFromString(dateString);
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public RM_Datetime(String dateString) {
        
        try {
            
            date =  Utils.getDateFromString(dateString);
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Date getDate() {
        return this.date;
    }
    
    private String getServerTime() throws Exception {

        ResultSet setRezultata = null;
        String dateString = "";
        
        String query = "SELECT current_timestamp;";
        
        Connection dbConnection = DBBroker.poveziSaBazom();
        Statement selectStatement = dbConnection.createStatement();
        setRezultata = selectStatement.executeQuery(query);
        setRezultata.next();
        dateString = setRezultata.getString(1);

        setRezultata.close();
        selectStatement = DBBroker.zatvoriStatement(selectStatement);
        dbConnection = DBBroker.zatvoriVezuSaBazom(dbConnection);
        
        return dateString;
    }
}
