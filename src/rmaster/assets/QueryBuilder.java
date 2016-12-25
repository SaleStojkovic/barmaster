/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    
    public static String IS_LIKE = " COLLATE UTF8_GENERAL_CI LIKE '";
        
    public static String IS_NOT_IN = " NOT IN ";
    
    public static String IS_IN = " IN ";
    
    public static String LOGIC_AND = "' AND ";

    public static String LOGIC_OR = "' OR ";
    
    public static String SORT_ASC = " ASC";
    
    public static String SORT_DESC = " DESC";

    
    
    public String TABLE_NAME;
    
    public ArrayList<String> SELECT_COLUMNS = new ArrayList<>();
    
    public ArrayList<String> CRITERIA_COLUMNS = new ArrayList<>();
    
    public ArrayList<String> CRITERIA = new ArrayList<>();
    
    public ArrayList<String> OPERATORS = new ArrayList<>();
    
    public ArrayList<String> CRITERIA_VALUES = new ArrayList<>();
    
    public ArrayList<TableJoin> TABLE_JOINS = new ArrayList<>();
    
    public String GROUP_BY;
    
    public String ORDER_BY_COLUMN;
    
    public String ORDER_BY_CRITERIA;
    
    public Integer LIMIT;
    
    public Integer OFFSET;
    
    public void setTableName(String tableName) {
        this.TABLE_NAME = tableName;
    }
    
    public void setColumns(String... kolone) {
        this.SELECT_COLUMNS.addAll(Arrays.asList(kolone));
    }
    
    public void addCriteriaColumns(String... uslovneKolone) {
        this.CRITERIA_COLUMNS.addAll(Arrays.asList(uslovneKolone));
    }
    
    public void addCriteria(String... kriterijumi) {
        this.CRITERIA.addAll(Arrays.asList(kriterijumi));
    }
    
    public void addOperators(String... operatori) {
        this.OPERATORS.addAll(Arrays.asList(operatori));
    }
    
    public void addCriteriaValues(String... uslovneVrednosti) {
        this.CRITERIA_VALUES.addAll(Arrays.asList(uslovneVrednosti));
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
    
    public void addTableJoins(TableJoin... tableJoins)
    {
        this.TABLE_JOINS.addAll(Arrays.asList(tableJoins));
    }
    
    
    public String toQueryString() {
        String queryString = "SELECT *";
        
        if (!SELECT_COLUMNS.isEmpty()) {
        
            queryString = queryString.substring(0, queryString.length() - 1);
            
            for (int j = 0; j < SELECT_COLUMNS.size() - 1; j++) {
                queryString += SELECT_COLUMNS.get(j) + ", ";
            }

            queryString += SELECT_COLUMNS.get(SELECT_COLUMNS.size() - 1);
        }
        
        queryString += " FROM "; 
                 
        if (!this.TABLE_JOINS.isEmpty()) {
            
            queryString += this.TABLE_JOINS.get(0).FIRST_TABLE + " ";
            
            for(TableJoin join : this.TABLE_JOINS) {
                
                queryString += join.joinToString();
            }
            
        } else {
            queryString += TABLE_NAME;
        }
        
        if (!CRITERIA_COLUMNS.isEmpty()) {
        
            if (CRITERIA_COLUMNS.size() > 1) {

                queryString += " WHERE ";

                for (int i = 0; i < CRITERIA_COLUMNS.size() - 1; i++) {

                    queryString += CRITERIA_COLUMNS.get(i)
                            + CRITERIA.get(i)
                            + CRITERIA_VALUES.get(i) 
                            + OPERATORS.get(i); 
                    
                    if (CRITERIA.get(i).equals(IS_NOT_IN)) {
                        queryString = queryString.substring(0, queryString.length() - 1);
                    }
                }

                queryString += CRITERIA_COLUMNS.get(CRITERIA_COLUMNS.size() - 1)
                        + CRITERIA.get(CRITERIA.size() - 1)
                        + CRITERIA_VALUES.get(CRITERIA_COLUMNS.size() - 1);
                
                if (!CRITERIA.get(CRITERIA.size() - 1).equals(IS_NOT_IN) 
                        && !CRITERIA.get(CRITERIA.size() - 1).equals(IS_IN)) {
                        queryString += "'";
                    }
            }

            if (CRITERIA_COLUMNS.size() == 1) {
                queryString += " WHERE ";

                queryString += CRITERIA_COLUMNS.get(0) 
                        + CRITERIA.get(0)
                        + CRITERIA_VALUES.get(0);
                        
                if (!CRITERIA.get(CRITERIA.size() - 1).equals(IS_NOT_IN)
                        && !CRITERIA.get(CRITERIA.size() - 1).equals(IS_IN)) {
                        queryString += "'";
                    }
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
    
    public String makeStringForInCriteriaFromListByParam(List<Map<String, String>> list, String paramName) 
    {
        String result = "(";
        
        for (Map<String, String> map : list) {
            result += map.get(paramName) + ",";
        }
        
        result = result.substring(0, result.length() - 1);

        result += ")";
        
        return result;
    }
}
