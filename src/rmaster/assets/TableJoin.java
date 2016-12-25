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
public class TableJoin {
    
    public String FIRST_TABLE;
    
    private final String SECOND_TABLE;
    
    private final String FIRST_TABLE_KEY;
    
    private final String SECOND_TABLE_KEY;
    
    private final String JOIN_TYPE;
    
    /**
     * 
     * @param firstTableName
     * @param secondTableName
     * @param firstKey
     * @param secondKey
     * @param joinType 
     */
    public TableJoin (
            String firstTableName,
            String secondTableName,
            String firstKey,
            String secondKey,
            String joinType
    )
    {
        this.FIRST_TABLE = firstTableName;
        this.SECOND_TABLE = secondTableName;
        this.FIRST_TABLE_KEY = firstKey;
        this.SECOND_TABLE_KEY = secondKey;
        this.JOIN_TYPE = joinType;
    }
    
    public String joinToString() 
    {
        String joinString;
        
        joinString = this.JOIN_TYPE + " "
                + this.SECOND_TABLE 
                + " ON "
                + this.FIRST_TABLE + "."
                + this.FIRST_TABLE_KEY 
                + " = "
                + this.SECOND_TABLE + "."
                + this.SECOND_TABLE_KEY + " ";
        
        return joinString;
    }
    
}
