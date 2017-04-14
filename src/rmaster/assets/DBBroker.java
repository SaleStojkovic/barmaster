package rmaster.assets;
 
import rmaster.assets.QueryBuilder.QueryBuilder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Alert;
import rmaster.models.Konobar;
import rmaster.models.Porudzbina;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.PooledDataSource;
import org.olap4j.PreparedOlapStatement;
/**
 *
 * @author Sasa Stojkovic       
 */
public final class DBBroker {
    /**
     * This class would perform basic CRUD 
     */
    private static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_NAME = "barmaster";
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/" + DB_NAME + "?dontTrackOpenResources=true";
    private static final String USERNAME = "root";
    
//    private static final String PASSWORD = "burek";
//    private static final String PASSWORD = "928374";
    private static final String PASSWORD = "";
    
    private static ComboPooledDataSource dataSource = null;
//    private static PooledDataSource pds = null;
//    private static int brojKonekcija = 0;
    
    public DBBroker() {
        if (dataSource == null) {
            try {
                dataSource = new ComboPooledDataSource(); 

                dataSource.setDriverClass(DATABASE_DRIVER); 
                dataSource.setJdbcUrl(URL); 
                dataSource.setUser(USERNAME); 
                dataSource.setPassword(PASSWORD); 

                
                dataSource.setMinPoolSize(3); 
                dataSource.setAcquireIncrement(3); 
                dataSource.setMaxPoolSize(12);
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    /**
     * 
     * @return Connection
     */
    public static Connection poveziSaBazom() {
        Connection con = null;
        try {
            con = dataSource.getConnection();
       } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return con;
    }

    /**
     * 
     * @param connection
     * @throws Exception 
     */
    public static Connection zatvoriVezuSaBazom(Connection connection) {
        try {
            connection.close();
        } catch (SQLException ignore) {
        } finally {
            connection = null;
        }
        
        return connection;
        
    }
    
    public static Statement zatvoriStatement(Statement statement) {
        try {
            statement.close();
        } catch (SQLException ignore) {
        } finally {
            statement = null;
        }
        
        return statement;
    }
    
    public static CallableStatement zatvoriCallableStatement(CallableStatement statement) {
        return (CallableStatement) zatvoriStatement(statement);
    }

    public static PreparedStatement zatvoriPreparedStatement(PreparedStatement statement) {
        return (PreparedStatement) zatvoriStatement(statement);
    }
    /**
     * 
     * @param imeTabele
     * @param elementi
     * @param zatvoriKonekciju
     * @throws Exception 
     */
    public void ubaci(
            String imeTabele,
            HashMap<String, String> elementi,
            Boolean zatvoriKonekciju
    ) throws Exception {
        Connection dbConnection = null;
      
        PreparedStatement insertStatement = null;
         
        String insertValues = " VALUES (";
        String insertTableSQL = "INSERT INTO " + imeTabele + "(";
        
        
        for (HashMap.Entry<String, String> element : elementi.entrySet()) {
            if (element.getValue().equals("BIT_0")) {
                insertValues += QueryBuilder.BIT_0 + "',";
            } else if (element.getValue().equals("BIT_1")) {
                insertValues += QueryBuilder.BIT_1 + "',";
            } else {
                insertValues += "'" + element.getValue() + "',";
            }
            insertTableSQL += element.getKey() + ",";
        }
        insertTableSQL = insertTableSQL.substring(0, insertTableSQL.length()-1);
        insertValues = insertValues.substring(0, insertValues.length()-1);
 
        insertTableSQL += ")";
        insertValues += ");";
         
        insertTableSQL += insertValues;
                  
        try {
                dbConnection = poveziSaBazom();
                
                insertStatement = dbConnection.prepareStatement(
                        insertTableSQL,
                        Statement.RETURN_GENERATED_KEYS
                );
                 
                insertStatement.executeUpdate();

 
        } catch (SQLException e) {
 
                System.out.println(e.getMessage());
                dbConnection.rollback();
                
 
        } finally {
            insertStatement = zatvoriPreparedStatement(insertStatement);
            
            dbConnection = zatvoriVezuSaBazom(dbConnection);
        }
    }

     
    /**
     * 
     * @param imeTabele
     * @param elementi
     * @param zatvoriKonekciju
     * @return insertedID
     * @throws Exception 
     */
    public long ubaciRed(
            String imeTabele,
            HashMap<String, String> elementi,
            Boolean zatvoriKonekciju
    ) throws Exception {
        Connection dbConnection = null;
        PreparedStatement insertStatement = null;
        long result = 0;
         
        String insertValues = " VALUES (";
        String insertTableSQL = "INSERT INTO " + imeTabele + "(";
        
        for (HashMap.Entry<String, String> element : elementi.entrySet()) {
            switch (element.getValue()) {
                case "true":
                    insertValues += "b'1',";
                    break;
                case "false":
                    insertValues += "b'0',";
                    break;
                default:
                    insertValues += "'" + element.getValue() + "',";
                    break;
            }
            insertTableSQL += element.getKey() + ",";
        }
        insertTableSQL = insertTableSQL.substring(0, insertTableSQL.length()-1);
        insertValues = insertValues.substring(0, insertValues.length()-1);
 
        insertTableSQL += ")";
        insertValues += ");";
         
        insertTableSQL += insertValues;
                  
        try {
            dbConnection = poveziSaBazom();

            insertStatement = dbConnection.prepareStatement(
                    insertTableSQL,
                    Statement.RETURN_GENERATED_KEYS
            );
            insertStatement.executeUpdate();

            try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    result = generatedKeys.getLong(1);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            dbConnection.rollback();
        } finally {
            insertStatement = zatvoriPreparedStatement(insertStatement);
            dbConnection = zatvoriVezuSaBazom(dbConnection);
        }
        return result;
    }

    /**
     * 
     * @param imeTabele
     * @param uslovnaKolona
     * @param uslovnaVrednost
     * @param elementi
     * @param zatvoriKonekciju
     * @throws Exception 
     */
    public void izmeni(
            String imeTabele,
            String uslovnaKolona,
            String uslovnaVrednost,
            HashMap<String, String> elementi,
            Boolean zatvoriKonekciju
    ) throws Exception {
        Connection dbConnection = null;
        PreparedStatement updateStatement = null;
         
        String updateTableSQL = "UPDATE " + DB_NAME + ".`" + imeTabele + "` SET ";
         
        for (HashMap.Entry<String, String> element : elementi.entrySet()) {
            updateTableSQL +=  "`" + element.getKey() + "` = ?,";
        }
 
        updateTableSQL = updateTableSQL.substring(0, updateTableSQL.length()-1);
        updateTableSQL += " WHERE `" + uslovnaKolona + "` = ?;";
         
             
        try {
            dbConnection = poveziSaBazom();

            updateStatement = dbConnection.prepareStatement(updateTableSQL);
            int brojac = 1;

            for (HashMap.Entry<String, String> element : elementi.entrySet()) {
                if (element.getValue().equals("true"))
                    updateStatement.setBoolean(brojac, true);
                if (element.getValue().equals("false"))
                    updateStatement.setBoolean(brojac, false);
                else
                    updateStatement.setString(brojac, element.getValue());
                brojac++;
            } 

            updateStatement.setString(brojac, uslovnaVrednost);
            updateStatement.executeUpdate();

        } catch (SQLException e) {
                System.out.println(e.getMessage());
                dbConnection.rollback();
        } finally {
            updateStatement = zatvoriPreparedStatement(updateStatement);
            dbConnection = zatvoriVezuSaBazom(dbConnection);
        }
    }
     
    /**
     * 
     * @param imeTabele
     * @param uslovnaKolona
     * @param uslovnaVrednost
     * @param zatvoriKonekciju
     * @throws Exception 
     */
    public void izbrisi(
            String imeTabele,
            String uslovnaKolona,
            String uslovnaVrednost,
            Boolean zatvoriKonekciju
    ) throws Exception {
        Connection dbConnection = null; 
        PreparedStatement deleteStatement = null;
         
        String deleteQuery = "DELETE FROM " + imeTabele + 
                " WHERE " + uslovnaKolona + " = " + uslovnaVrednost + ";";
                 
        try {
                dbConnection = poveziSaBazom();
                
                deleteStatement = dbConnection.prepareStatement(deleteQuery);
                 
                deleteStatement.executeUpdate();
 
        } catch (SQLException e) {
 
                System.out.println(e.getMessage());
                dbConnection.rollback();
 
        } finally {
            deleteStatement = zatvoriPreparedStatement(deleteStatement);
            dbConnection = zatvoriVezuSaBazom(dbConnection);
        }
    }
     
    
    /**
     * 
     * @param query
     * @return 
     */
    public List runQuery(QueryBuilder query) 
    {
        Connection dbConnection = null;
        Statement selectStatement = null;
        PreparedStatement updateStatement = null;
        ResultSet setRezultata = null;
        List listaRezultata = null;
         
        String queryString = query.toQueryString();
        
        try {
            dbConnection = poveziSaBazom();

            // select query
            if (query.QUERY_TYPE.equals(QueryBuilder.SELECT)) {
                
                selectStatement = dbConnection.createStatement();
                
                setRezultata = selectStatement.executeQuery(queryString);

                listaRezultata = prebaciUListu(setRezultata);
            }
            
            //update query
            if (query.QUERY_TYPE.equals(QueryBuilder.UPDATE)) 
            {
                updateStatement = dbConnection.prepareStatement(queryString);
                
                updateStatement.executeUpdate();   
            }
            
        
        } catch (SQLException e) {
 
            System.out.println(e.getMessage());
 
        } finally {
            
            if (setRezultata != null) {
                try { 
                    setRezultata.close();
                    setRezultata = null;
                } catch (SQLException ignore) {}
            }
            
            if (selectStatement != null) {
                selectStatement = zatvoriStatement(selectStatement);
            }
            
            if (updateStatement != null) {
                updateStatement = zatvoriPreparedStatement(updateStatement);
            }
            
            queryString = null;
            
            dbConnection = zatvoriVezuSaBazom(dbConnection);
        }
 
        return listaRezultata; 
    }
    
    /**
     * 
     * @param query
     * @param ostaviOtvorenuKonekciju
     * @return 
     */
    public List runQuery(
            QueryBuilder query,
            Boolean ostaviOtvorenuKonekciju
    ) 
    {
        Connection dbConnection = null;
        Statement selectStatement = null;
        ResultSet setRezultata = null;
        List listaRezultata = null;
         
        String queryString = query.toQueryString();
        
        try {
            dbConnection = poveziSaBazom();
            
            // select query
            if (query.QUERY_TYPE.equals(QueryBuilder.SELECT)) {
                
                selectStatement = dbConnection.createStatement();
                
                setRezultata = selectStatement.executeQuery(queryString);

                listaRezultata = prebaciUListu(setRezultata);
            }
            
            //update query
            if (query.QUERY_TYPE.equals(QueryBuilder.UPDATE)) 
            {
                PreparedStatement updateStatement = dbConnection.prepareStatement(queryString);
                
                updateStatement.executeUpdate();   
            }
            
        
        } catch (SQLException e) {
 
            System.out.println(e.getMessage());
 
        } finally {
            
            if (setRezultata != null) {
                try { setRezultata.close(); } catch (SQLException ignore) {}
            }
            
            selectStatement = zatvoriStatement(selectStatement);
            
            dbConnection = zatvoriVezuSaBazom(dbConnection);
        }
 
        return listaRezultata; 
    }
    
    
    /**
     * 
     * @param resultSet
     * @return
     * @throws Exception 
     */
    private static List prebaciUListu(
            ResultSet resultSet
    ) 
    {    
        ArrayList listaRezultata = new ArrayList();       
        try {
            ResultSetMetaData md = resultSet.getMetaData();
            int columns = md.getColumnCount();
        
            while (resultSet.next()){
                HashMap row = new HashMap(columns);
                for(int i = 1; i<= columns; ++i){           
                    row.put(md.getColumnLabel(i),resultSet.getString(i));
                }
                listaRezultata.add(row);
            }
        
        } catch (Exception e) {
            System.out.println(e);
        }

        return listaRezultata;
    }
    
    /**
     * 
     * @param imeProcedure
     * @param imenaArgumenata
     * @param vrednostiArgumenata 
     * @return the java.util.List 
     */
    public static List runStoredProcedure(
            String imeProcedure, 
            String[] imenaArgumenata, 
            String[] vrednostiArgumenata)
    {
        Connection dbConnection = null;
        ResultSet rs = null;
        List listaRezultata = null;
        CallableStatement cStmt = null;
        
        try {
            dbConnection = poveziSaBazom();
            
            String procedureCall = "{CALL " + imeProcedure + "(";
            
            int brojArgumenata = imenaArgumenata.length;
            
            for (int i = 0; i < brojArgumenata; i++) {
                procedureCall += "?,";
            }
            
            if (brojArgumenata>0)
                procedureCall = procedureCall.substring(0, procedureCall.length()-1);
            procedureCall += ")}";
            
            cStmt = dbConnection.prepareCall(procedureCall);
            
            for (int i = 0; i < brojArgumenata; i++) {
                 cStmt.setString(imenaArgumenata[i], vrednostiArgumenata[i]);
            }
           
            cStmt.execute();
            rs = cStmt.getResultSet();
            
            listaRezultata = prebaciUListu(rs);
            
        } catch (Exception e) {
            
            System.out.println("Store procedure \"" + imeProcedure + "\" exec error! - " + e.toString());
        
        } finally {
            
            if (rs != null) {
                try { rs.close(); } catch (SQLException ignore) {}
            }
            
            cStmt = zatvoriCallableStatement(cStmt);
                        
            dbConnection = zatvoriVezuSaBazom(dbConnection);
        } 
        
        return listaRezultata;   
    }
    
    /**
     * 
     * @param imeFunkcije
     * @param imenaArgumenata
     * @param vrednostiArgumenata
     * @return 
     */
    public static int getValueFromFunction(
            String imeFunkcije,
            String[] imenaArgumenata,
            String[] vrednostiArgumenata
    )
    {
        Connection dbConnection = null;
        ResultSet rs = null;
        List listaRezultata = null;
        CallableStatement cStmt = null;
        int rez = 0;
        try {
            dbConnection = poveziSaBazom();
            
            String procedureCall = "{? = call " + imeFunkcije + "(";
            
            int brojArgumenata = imenaArgumenata.length;
            
            for (int i = 0; i < brojArgumenata; i++) {
                procedureCall += "?,";
            }
            
            if (brojArgumenata>0)
                procedureCall = procedureCall.substring(0, procedureCall.length()-1);
            procedureCall += ")}";
            
            cStmt = dbConnection.prepareCall(procedureCall);
            cStmt.registerOutParameter (1, Types.INTEGER);
    
            for (int i = 0; i < brojArgumenata; i++) {
                 cStmt.setString(imenaArgumenata[i], vrednostiArgumenata[i]);
            }
           
            cStmt.execute();
            rez = cStmt.getInt(1);
            
        } catch (Exception e) {
            
            System.out.println("Store procedure \"" + imeFunkcije + "\" exec error! - " + e.toString());
        
        } finally {
            
            if (rs != null) {
                try { rs.close(); } catch (SQLException ignore) {}
            }
            
            cStmt = zatvoriCallableStatement(cStmt);
            
            dbConnection = zatvoriVezuSaBazom(dbConnection);
        } 
        
        return rez;   
    }
    
    
        /**
     * 
     * @param imeStoreProcedure
     * @param imeArgumentaSP
     * @param vrednostArgumentaSP
     * @return 
     */
    public List getRecordSetIzStoreProcedureZaParametar(
            String imeStoreProcedure, 
            String imeArgumentaSP,
            String vrednostArgumentaSP) 
    {
        
        Connection dbConnection = null;
        ResultSet rs = null;
        List listaRezultata = null;
        CallableStatement cStmt = null;
        
        try {
            dbConnection = poveziSaBazom();
            cStmt = dbConnection.prepareCall("{CALL " + imeStoreProcedure + "(?)}");
            cStmt.setString(imeArgumentaSP, vrednostArgumentaSP);
            cStmt.execute();
            rs = cStmt.getResultSet();
            
            listaRezultata = prebaciUListu(rs);
            
        } catch (Exception e) {
            
            System.out.println("Store procedure \"" + imeStoreProcedure + "\" exec error! - " + e.toString());
        
        } finally {
            
            if (rs != null) {
                try { rs.close(); } catch (SQLException ignore) {}
            }
            
            cStmt = zatvoriCallableStatement(cStmt);
            
            dbConnection = zatvoriVezuSaBazom(dbConnection);
        } 
        
        return listaRezultata;
    }
    
    
    public List get_PorudzbineStola(String izabraniStoId)
    {    
        Connection dbConnection = null;
        ResultSet rs = null;
        List listaRezultata = null;
        CallableStatement cStmt = null;
        
        try {
            dbConnection = poveziSaBazom();
            cStmt = dbConnection.prepareCall("{CALL getPorudzbineStola(?)}");
            cStmt.setString("stoID", izabraniStoId);
            cStmt.execute();
            rs = cStmt.getResultSet();
            
            listaRezultata = prebaciUListu(rs);
            
        } catch (Exception e) {
            System.out.println("Store procedure \"get_PorudzbineStola\" exec error! - " + e.toString());
        } finally {
           
            if (rs != null) {
                try { rs.close(); } catch (SQLException ignore) {}
            }
            
            cStmt = zatvoriCallableStatement(cStmt);
            
            dbConnection = zatvoriVezuSaBazom(dbConnection);
            
        }
            
        return listaRezultata;
    }
    
    
    public boolean passwordCheckZaMenadzera(String lozinkaText) throws Exception {
        boolean jesteMenadzer = false;
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        query.setTableName("login");
        query.addCriteriaColumns("pass");
        query.addCriteria(QueryBuilder.IS_EQUAL);
        query.addCriteriaValues(lozinkaText);
        
        List rezultat = this.runQuery(query);
        
        if (!rezultat.isEmpty()) {
            Map<String,String>  menadzer = (Map<String, String>)rezultat.get(0);
            if (menadzer != null) {
                if (menadzer.get("admin").equals("1")) {
                    jesteMenadzer = true;
                } else
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Greška!");
                    alert.setHeaderText("Greška pri predstavljanju");
                    alert.setContentText("Menadžer nema privilegije! Unesite lozinku menadžera koje je ovlašćen da odobri ponovnu štampu međuzbira.");
                    alert.showAndWait();

                    System.out.println("Menadžer nema privilegije! - Forma PoridzbinaController" + lozinkaText);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Greška!");
                alert.setHeaderText("Greška pri predstavljanju");
                alert.setContentText("Pogrešna lozinka menadžera! Unesite lozinku menadžera koje je ovlašćen da odobri ponovnu štampu međuzbira.");
                alert.showAndWait();

                System.out.println("Pogrešna lozinka menadžera! Nepostojeci menadžer! - Forma PoridzbinaController - " + lozinkaText);
            }
        }
        return jesteMenadzer;
    }

    public Konobar passwordCheck(String lozinkaText) throws Exception {
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        query.setTableName("konobar");
        query.addCriteriaColumns("pin");
        query.addCriteria(QueryBuilder.IS_EQUAL);
        query.addCriteriaValues(lozinkaText);
                
        List rezultat = this.runQuery(query);
        
        if (!rezultat.isEmpty()) {
            Map<String,String>  konobar = (Map<String, String>)rezultat.get(0);
            //proveraPina(konobar);
            return new Konobar(konobar);
        }
        
        return null;
    }

    /**
     * 
     * @param RacunID
     * @param vreme
     * @param stoBroj
     */
    public void zatvoriRacunIOslobodiSto(
            Porudzbina porudzbina) 
    {
        Connection dbConnection = null;
        CallableStatement cStmt = null;

        try {
            dbConnection = poveziSaBazom();
            
            cStmt = dbConnection.prepareCall("{CALL zatvoriRacunIOslobodiSto(?, ?, ?)}");
            cStmt.setLong("racunID", porudzbina.getID());
            cStmt.registerOutParameter("vreme", java.sql.Types.TIMESTAMP);
            cStmt.registerOutParameter("brojNovogRacuna", java.sql.Types.INTEGER);
            cStmt.execute();

            porudzbina.setVremeIzdavanjaRacuna(new Date(cStmt.getTimestamp("vreme").getTime()));
            porudzbina.setBrojRacunaBroj(cStmt.getInt("brojNovogRacuna"));
       } catch (Exception e) {
            System.out.println("Store procedure \"zatvoriRacunIOslobodiSto\" exec error! - " + e.toString());
        } finally {
            cStmt = zatvoriCallableStatement(cStmt);
            dbConnection = zatvoriVezuSaBazom(dbConnection);
        }
    }
    
    
}