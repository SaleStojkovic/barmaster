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
    
    public static String SORT_ASC = " ASC";
    
    public static String SORT_DESC = " DESC";
    
    
    public String TABLE_NAME;
    
    public String[] SELECT_COLUMNS;
    
    public String[] CRITERIA_COLUMNS;
    
    public String[] CRITERIA;
    
    public String[] OPERATORS;
    
    public String[] CRITERIA_VALUES;
    
    public String GROUP_BY;
    
    public String ORDER_BY_COLUMN;
    
    public String ORDER_BY_CRITERIA;
    
    public Integer LIMIT;
    
    public Integer OFFSET;
    
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
    
    public void setOrderBy(String orderByColumn, String orderCriteria) {
        this.ORDER_BY_COLUMN = orderByColumn;
        this.ORDER_BY_CRITERIA = orderCriteria;
    }
    
    public void setLimit(int limit) {
        this.LIMIT = limit;
    }
    
    public void setOffset(int offset) {
        this.OFFSET = offset;
    }
    
    public String toQueryString() {
        String queryString = "SELECT *";
        
        if (SELECT_COLUMNS != null) {
        
            queryString = queryString.substring(0, queryString.length() - 1);
            
            for (int j = 0; j < SELECT_COLUMNS.length - 1; j++) {
                queryString += SELECT_COLUMNS[j] + ", ";
            }

            queryString += SELECT_COLUMNS[SELECT_COLUMNS.length - 1];
        }
        
        queryString += " FROM " + TABLE_NAME; 
                 
        
        if (CRITERIA_COLUMNS != null ) {
        
            if (CRITERIA_COLUMNS.length > 1) {

                queryString += " WHERE ";

                for (int i = 0; i < CRITERIA_COLUMNS.length - 1; i++) {

                    queryString += CRITERIA_COLUMNS[i] 
                            + CRITERIA[i] 
                            + CRITERIA_VALUES[i] 
                            + OPERATORS[i]; 
                }

                queryString += CRITERIA_COLUMNS[CRITERIA_COLUMNS.length - 1] 
                        + CRITERIA[CRITERIA.length - 1] 
                        + CRITERIA_VALUES[CRITERIA_COLUMNS.length - 1] + "'";
            }

            if (CRITERIA_COLUMNS.length == 1) {
                queryString += " WHERE ";

                queryString += CRITERIA_COLUMNS[0] 
                        + CRITERIA[0] 
                        + CRITERIA_VALUES[0] 
                        + "' "; 
            }
        }
        
        if (GROUP_BY != null && !GROUP_BY.isEmpty()) {
            queryString += "GROUP BY '" + GROUP_BY + "'";
        }
        
        if (ORDER_BY_COLUMN != null && !ORDER_BY_COLUMN.isEmpty()) {
            queryString += " ORDER BY '" + ORDER_BY_COLUMN + "'" + ORDER_BY_CRITERIA;
        }
        
        if (LIMIT != null) {
            queryString += " LIMIT " + LIMIT;
        }
        
        if (OFFSET != null) {
            queryString += " OFFSET " + OFFSET;
        }
        
        queryString += ";";
        return queryString;
    }
}
