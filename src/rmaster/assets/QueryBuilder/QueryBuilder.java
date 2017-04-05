/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets.QueryBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Arbor
 */
public class QueryBuilder {
    
    public static String SELECT = "SELECT ";
    
    public static String UPDATE = "UPDATE ";
    
    public static String DELETE = "DELETE ";
    
    public static String TRUE = "true";
    
    public static String FALSE = "false";
    
    public static String BIT_0 = "b'0";
    
    public static String BIT_1 = "b'1";
    
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

    
    public String QUERY_TYPE;
    
    public String TABLE_NAME;
    
    public ArrayList<String> UPDATE_COLUMNS = new ArrayList<>();

    public ArrayList<String> UPDATE_COLUMNS_VALUES = new ArrayList<>();
    
    public ArrayList<String> SELECT_COLUMNS = new ArrayList<>();
    
    public ArrayList<String> CRITERIA_COLUMNS = new ArrayList<>();
    
    public ArrayList<String> CRITERIA = new ArrayList<>();
    
    public ArrayList<String> OPERATORS = new ArrayList<>();
    
    public ArrayList<String> CRITERIA_VALUES = new ArrayList<>();
    
    public ArrayList<TableJoin> TABLE_JOINS = new ArrayList<>();
    
    public String GROUP_BY;
    
    public ArrayList<String> ORDER_BY_COLUMNS = new ArrayList<>();
    
    public ArrayList<String> ORDER_BY_CRITERIAS = new ArrayList<>();
    
    public Integer LIMIT;
    
    public Integer OFFSET;

    public String queryString; 
    
    public QueryBuilder(String queryType) 
    {
        this.QUERY_TYPE = queryType;
    }
    
    public void setTableName(String tableName) {
        this.TABLE_NAME = tableName;
    }
    
    public void setSelectColumns(String... kolone) {
        this.SELECT_COLUMNS.addAll(Arrays.asList(kolone));
    }
    
    public void setUpdateColumns(String... kolone) {
        this.UPDATE_COLUMNS.addAll(Arrays.asList(kolone));
    }
    
    public void setUpdateColumnValues(String... noveVrednosti) {
        this.UPDATE_COLUMNS_VALUES.addAll(Arrays.asList(noveVrednosti));
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
    
    public void addOrderByCriterias(String... orderByCriterias) {
        this.ORDER_BY_CRITERIAS.addAll(Arrays.asList(orderByCriterias));
    }
    public void addOrderByColumns(String... orderByColumns) {
        this.ORDER_BY_COLUMNS.addAll(Arrays.asList(orderByColumns));
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

        if (this.QUERY_TYPE.equals(SELECT)) {
            this.makeSelectQuery();
        }

        if (this.QUERY_TYPE.equals(UPDATE)) {
            this.makeUpdateQuery();
        }
        
        queryString += ";";
        
        return queryString;
    }
    
    public String makeStringForInCriteriaFromListByParam(List<Map<String, String>> list, String paramName) 
    {
        String result = "(";
        
        if (list.size() > 1) {
            
            for (Map<String, String> map : list) {
                result += map.get(paramName) + ",";
            }

            result = result.substring(0, result.length() - 1);
        }
        
        if (list.size() == 1) {
            result += list.get(0).get(paramName);
        }
        
        result += ")";
        
        return result;
    }
    
    private void makeSelectQuery()
    {
        queryString = "SELECT *";
        
        if (!SELECT_COLUMNS.isEmpty()) {

            queryString = queryString.substring(0, queryString.length() - 1);


            for (int j = 0; j < SELECT_COLUMNS.size() - 1; j++) {
                queryString += SELECT_COLUMNS.get(j) + ", ";
            }

            queryString += SELECT_COLUMNS.get(SELECT_COLUMNS.size() - 1);

        }

        queryString += " FROM "; 


        if (!TABLE_JOINS.isEmpty()) {

            queryString += TABLE_JOINS.get(0).FIRST_TABLE + " ";

            for(TableJoin join : TABLE_JOINS) {

                queryString += join.joinToString();
            }

        } else {
            queryString += TABLE_NAME;
        }

        if (!CRITERIA_COLUMNS.isEmpty()) {

            this.addWhereClauses();
        }

        if (GROUP_BY != null && !GROUP_BY.isEmpty()) {
            queryString += " GROUP BY " + GROUP_BY;
        }

        if (!ORDER_BY_COLUMNS.isEmpty()) {
            this.addOrderByClauses();
        }

        if (LIMIT != null) {
            queryString += " LIMIT " + LIMIT;
        }

        if (OFFSET != null) {
            queryString += " OFFSET " + OFFSET;
        }
            
    }
    
    private void makeUpdateQuery() {
        
        queryString = "UPDATE ";
        
        queryString += TABLE_NAME;
        
        queryString += " SET ";
        
        queryString += UPDATE_COLUMNS.get(0) + " = " + UPDATE_COLUMNS_VALUES.get(0);
        
        if (UPDATE_COLUMNS.size() > 1) {
            
            queryString += ",";

            for (int i = 1; i < UPDATE_COLUMNS.size() - 1; i++) {

                queryString += UPDATE_COLUMNS.get(i) + " = " + UPDATE_COLUMNS_VALUES.get(i);
                queryString += ",";
            } 
            
            queryString = queryString.substring(0, queryString.length() - 1);
            
            queryString += 
                    UPDATE_COLUMNS.get(UPDATE_COLUMNS.size() - 1) 
                    + " = " 
                    + UPDATE_COLUMNS_VALUES.get(UPDATE_COLUMNS_VALUES.size() - 1);
        }

        if (!CRITERIA_COLUMNS.isEmpty()) {

            this.addWhereClauses();
        }
        
    }
    
    private String deleteLastOccurance(String target, String whatToDelete) {
        int index = target.lastIndexOf(whatToDelete);
        return target.substring(0,index-1) + target.substring(index+1, target.length());
    }
    
    private void addWhereClauses() {
        
        if (CRITERIA_COLUMNS.size() > 1) {

            queryString += " WHERE ";

            for (int i = 0; i < CRITERIA_COLUMNS.size() - 1; i++) {
                if (CRITERIA_VALUES.get(i).equals(BIT_0) || CRITERIA_VALUES.get(i).equals(BIT_1)) {
                    queryString += CRITERIA_COLUMNS.get(i)
                            + CRITERIA.get(i);
                    queryString = deleteLastOccurance(queryString, "\'");
                    queryString += CRITERIA_VALUES.get(i) 
                            + OPERATORS.get(i); 
                }
                if (!(CRITERIA_VALUES.get(i).equals(BIT_0) || CRITERIA_VALUES.get(i).equals(BIT_1))) {
                    queryString += CRITERIA_COLUMNS.get(i)
                            + CRITERIA.get(i)
                            + CRITERIA_VALUES.get(i) 
                            + OPERATORS.get(i); 
                }

                if (CRITERIA.get(i).equals(IS_NOT_IN) && CRITERIA.get(i).equals(IS_IN)) {
                    queryString = queryString.substring(0, queryString.length() - 1);
                }
            }

            int lastCriteriaIndex = CRITERIA_COLUMNS.size() - 1;
            if (CRITERIA_VALUES.get(lastCriteriaIndex).equals(BIT_0) || CRITERIA_VALUES.get(lastCriteriaIndex).equals(BIT_1)) {
                queryString += CRITERIA_COLUMNS.get(lastCriteriaIndex)
                            + CRITERIA.get(lastCriteriaIndex);
                    queryString = deleteLastOccurance(queryString, "\'");
                    queryString += CRITERIA_VALUES.get(lastCriteriaIndex); 

            }            
            
            if (!(CRITERIA_VALUES.get(lastCriteriaIndex).equals(BIT_0) || CRITERIA_VALUES.get(lastCriteriaIndex).equals(BIT_1))) {
                queryString += CRITERIA_COLUMNS.get(lastCriteriaIndex)
                        + CRITERIA.get(lastCriteriaIndex)
                        + CRITERIA_VALUES.get(lastCriteriaIndex); 
            }

            if (!CRITERIA.get(CRITERIA.size() - 1).equals(IS_NOT_IN) 
                    && !CRITERIA.get(CRITERIA.size() - 1).equals(IS_IN)) {
                    queryString += "'";
            }
        }

        if (CRITERIA_COLUMNS.size() == 1) {
            queryString += " WHERE ";

            queryString += CRITERIA_COLUMNS.get(0) 
                    + CRITERIA.get(0);
            
            if (CRITERIA_VALUES.get(0).equals(BIT_0) || CRITERIA_VALUES.get(0).equals(BIT_1)) {
                queryString = deleteLastOccurance(queryString, "\'");
            }
            
            queryString += CRITERIA_VALUES.get(0);

            if (!CRITERIA.get(CRITERIA.size() - 1).equals(IS_NOT_IN)
                    && !CRITERIA.get(CRITERIA.size() - 1).equals(IS_IN)) {
                    queryString += "'";
                }
        }
    }
    
    private void addOrderByClauses() {
        
        queryString += " ORDER BY";

        for (int i = 0; i < ORDER_BY_COLUMNS.size(); i++) {
            queryString += " " + ORDER_BY_COLUMNS.get(i)
                         + ORDER_BY_CRITERIAS.get(i)
                         + ",";
        }
        
        queryString = queryString.substring(0, queryString.length()-1);
    }

}
