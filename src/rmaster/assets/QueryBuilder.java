/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets;

/**
 *
 * @author Arbor
 */
public class QueryBuilder {
    
    public static String TRUE = "true";
    
    public static String FALSE = "false";
    
    public static String IS_EQUAL = " = '";
    
    public static String IS_NOT_EQUAL = " <> '";

    public static String IS_GREATER_THAN = " > '";
    
    public static String IS_LESS_THAN = " < '";

    public static String IS_GREATER_OR_EQUAL_THAN = " >= '";

    public static String IS_LESS_OR_EQUAL_THAN = " <= '";
    
    public static String LOGIC_AND = "' AND ";

    public static String LOGIC_OR = "' OR ";
    
    
    public String TABLE_NAME;
    
    public String[] SELECT_COLUMNS;
    
    public String[] CRITERIA_COLUMNS;
    
    public String[] CRITERIA;
    
    public String[] OPERATORS;
    
    public String[] CRITERIA_VALUES;
    
    
    public void setTableName(String tableName) {
        this.TABLE_NAME = tableName;
    }
    
    public void setColumns(String... kolone) {
        this.SELECT_COLUMNS = kolone;
    }
    
    public void setCriteriaColumns(String... uslovneKolone) {
        this.CRITERIA_COLUMNS = uslovneKolone;
    }
    
    public void setCriteria(String... kriterijumi) {
        this.CRITERIA = kriterijumi;
    }
    
    public void setOperators(String... operatori) {
        this.OPERATORS = operatori;
    }
    
    public void setCriteriaValues(String... uslovneVrednosti) {
        this.CRITERIA_VALUES = uslovneVrednosti;
    }
    
    
}
