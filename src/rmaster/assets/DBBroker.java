package rmaster.assets;
 
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Alert;
import rmaster.models.Konobar;
 
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
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/barmaster";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
        
    public DBBroker() {
    }
    /**
     * 
     * @return Connection
     */
    public static Connection poveziSaBazom() {
        
        Connection dbConnection = null;
 
        try {
 
            Class.forName(DATABASE_DRIVER);
 
        } catch (ClassNotFoundException e) {
 
            System.out.println(e.getMessage());
 
        }
 
        try {
 
            dbConnection = DriverManager.getConnection(
                    URL, 
                    USERNAME,
                    PASSWORD
            );
            

        } catch (SQLException e) {
 
                System.out.println(e.getMessage());
 
        }
 
        return dbConnection;
    }
     
    /**
     * 
     * @param connection
     * @throws Exception 
     */
    public void prekiniVezuSaBazom(Connection connection) throws Exception {
        try {
            connection.close();
        }
        catch (Exception e) {
            System.out.println("Prekid veze.");
        }
 
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
            insertValues += "'" + element.getValue() + "',";
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
            if (insertStatement != null) {
                try { insertStatement.close(); } catch (SQLException ignore) {}
            }
            if (dbConnection != null && zatvoriKonekciju) {
                try { dbConnection.close(); } catch (SQLException ignore) {}
            }
            
           
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
                if (insertStatement != null) {
                    try { insertStatement.close(); } catch (SQLException ignore) {}
                }
                if (dbConnection != null && zatvoriKonekciju) {
                    try { dbConnection.close(); } catch (SQLException ignore) {}
                }
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
            
        
                if (updateStatement != null) {
                    try { updateStatement.close(); } catch (SQLException ignore) {}
                }
                if (dbConnection != null && zatvoriKonekciju) {
                    try { dbConnection.close(); } catch (SQLException ignore) {}
                }
 
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
            
        
                if (deleteStatement != null) {
                    try { deleteStatement.close(); } catch (SQLException ignore) {}
                }
                if (dbConnection != null && zatvoriKonekciju) {
                    try { dbConnection.close(); } catch (SQLException ignore) {}
                }
        }
    }
     
    
    /**
     * 
     * @param imeTabele
     * @param uslovneKolone
     * @param uslovneVrednosti
     * @return
     * @throws Exception 
     */
    public List vratiSveIzTabeleUzUslov(
            String imeTabele,
            String[] uslovneKolone,
            String[] uslovneVrednosti
    ) throws Exception {
        Connection dbConnection = null;
        Statement selectStatement = null;
        ResultSet setRezultata = null;
        List listaRezultata = null;
         
        int brojKolona = uslovneKolone.length;
        
        String selectQuery = "SELECT * FROM " + imeTabele + " WHERE ";
              
        for (int i = 0; i < brojKolona-1; i++) {
            selectQuery += uslovneKolone[i] + " = '" + uslovneVrednosti[i] + "' AND "; 
        }
        
        selectQuery += uslovneKolone[brojKolona-1] + " = '" + uslovneVrednosti[brojKolona-1] + "';";
        
        try {
            dbConnection = poveziSaBazom();
            
            selectStatement = dbConnection.createStatement();
 
            setRezultata = selectStatement.executeQuery(selectQuery);
 
            listaRezultata = this.prebaciUListu(setRezultata);
            
        } catch (SQLException e) {
 
            System.out.println(e.getMessage());
 
        } finally {
            
            if (setRezultata != null) {
                try { setRezultata.close(); } catch (SQLException ignore) {}
            }
            
            if (selectStatement != null) {
                try { selectStatement.close(); } catch (SQLException ignore) {}
            }
            
            if (dbConnection != null) {
                try { dbConnection.close(); } catch (SQLException ignore) {}
            }
            
            
        } 
 
         
        return listaRezultata; 
    }
    
    /**
     * 
     * @param imeTabele
     * @param naziviKolona
     * @param uslovneKolone
     * @param kriterijumi
     * @param operatori
     * @param uslovneVrednosti
     * @return
     * @throws Exception 
     */
    public List vratiKoloneIzTabele(
            String imeTabele,
            String[] naziviKolona,
            String[] uslovneKolone,
            String[] kriterijumi,
            String[] operatori,
            String[] uslovneVrednosti
    ) throws Exception {
        Connection dbConnection = null;
        Statement selectStatement = null;
        ResultSet setRezultata = null;
        List listaRezultata = null;
         
        
        String selectQuery = "SELECT  ";
              
        for (int j = 0; j < naziviKolona.length - 1; j++) {
            selectQuery += naziviKolona[j] + ", ";
        }
        
        selectQuery = selectQuery.substring(0, selectQuery.length() - 1);
        
        selectQuery += naziviKolona[naziviKolona.length - 1] 
                + " FROM " 
                + imeTabele; 
                 
        if (uslovneKolone.length > 1) {
            
            selectQuery += " WHERE ";
            
            for (int i = 0; i < uslovneKolone.length - 1; i++) {
                
                selectQuery += uslovneKolone[i] 
                        + kriterijumi[i] 
                        + uslovneVrednosti[i] 
                        + operatori[i]; 
            }

            selectQuery += uslovneKolone[uslovneKolone.length - 1] 
                    + kriterijumi[kriterijumi.length - 1] 
                    + uslovneVrednosti[uslovneKolone.length - 1] + "'";
        }
        
        if (uslovneKolone.length == 1) {
            selectQuery += " WHERE ";
            
            selectQuery += uslovneKolone[0] 
                    + kriterijumi[0] 
                    + uslovneVrednosti[0] 
                    + "' "; 
        }
        
        
        selectQuery += ";";
        
        try {
            dbConnection = poveziSaBazom();
            
            selectStatement = dbConnection.createStatement();
 
            setRezultata = selectStatement.executeQuery(selectQuery);
 
            listaRezultata = this.prebaciUListu(setRezultata);
            
        } catch (SQLException e) {
 
            System.out.println(e.getMessage());
 
        } finally {
            
            if (setRezultata != null) {
                try { setRezultata.close(); } catch (SQLException ignore) {
                    System.err.println(ignore);
                }
            }
            
            if (selectStatement != null) {
                try { selectStatement.close(); } catch (SQLException ignore) {
                    System.err.println(ignore);
                }
            }
            
            if (dbConnection != null) {
                try { dbConnection.close(); } catch (SQLException ignore) {   
                    System.err.println(ignore);
                }
            }
            
            
        } 
 
         
        return listaRezultata; 
    }
     
    /**
     * 
     * @param imeTabele
     * @return
     */
    public List vratiSveIzTabele(
            String imeTabele
    ) {
        Connection dbConnection = null;
        Statement selectStatement = null;
        ResultSet setRezultata = null;
        List listaRezultata = null;
         
        String selectTableSQL = "SELECT * FROM " + imeTabele;
         
        try {
            dbConnection = poveziSaBazom();
            
            selectStatement = dbConnection.createStatement();
 
            setRezultata = selectStatement.executeQuery(selectTableSQL);
 
            listaRezultata = this.prebaciUListu(setRezultata);
            
        } catch (SQLException e) {
 
            System.out.println(e.getMessage());
 
        } finally {
            
            if (setRezultata != null) {
                try { setRezultata.close(); } catch (SQLException ignore) {}
            }
            
            if (selectStatement != null) {
                try { selectStatement.close(); } catch (SQLException ignore) {}
            }
            
            if (dbConnection != null) {
                try { dbConnection.close(); } catch (SQLException ignore) {}
            }
            
           
        }
 
        return listaRezultata; 
    }
    
    
    public List runQuery(QueryBuilder query) 
    {
        Connection dbConnection = null;
        Statement selectStatement = null;
        ResultSet setRezultata = null;
        List listaRezultata = null;
         
        
        String selectQuery = "SELECT *";
        
        if (query.SELECT_COLUMNS != null) {
        
            selectQuery = selectQuery.substring(0, selectQuery.length() - 1);
            
            for (int j = 0; j < query.SELECT_COLUMNS.length - 1; j++) {
                selectQuery += query.SELECT_COLUMNS[j] + ", ";
            }

            selectQuery += query.SELECT_COLUMNS[query.SELECT_COLUMNS.length - 1];
        }
        
        selectQuery += " FROM " + query.TABLE_NAME; 
                 
        
        if (query.CRITERIA_COLUMNS != null ) {
        
            if (query.CRITERIA_COLUMNS.length > 1) {

                selectQuery += " WHERE ";

                for (int i = 0; i < query.CRITERIA_COLUMNS.length - 1; i++) {

                    selectQuery += query.CRITERIA_COLUMNS[i] 
                            + query.CRITERIA[i] 
                            + query.CRITERIA_VALUES[i] 
                            + query.OPERATORS[i]; 
                }

                selectQuery += query.CRITERIA_COLUMNS[query.CRITERIA_COLUMNS.length - 1] 
                        + query.CRITERIA[query.CRITERIA.length - 1] 
                        + query.CRITERIA_VALUES[query.CRITERIA_COLUMNS.length - 1] + "'";
            }

            if (query.CRITERIA_COLUMNS.length == 1) {
                selectQuery += " WHERE ";

                selectQuery += query.CRITERIA_COLUMNS[0] 
                        + query.CRITERIA[0] 
                        + query.CRITERIA_VALUES[0] 
                        + "' "; 
            }
        }
        
        selectQuery += ";";
        
        try {
            dbConnection = poveziSaBazom();
            
            selectStatement = dbConnection.createStatement();
 
            setRezultata = selectStatement.executeQuery(selectQuery);
 
            listaRezultata = prebaciUListu(setRezultata);
        
        } catch (SQLException e) {
 
            System.out.println(e.getMessage());
 
        } finally {
            
            if (setRezultata != null) {
                try { setRezultata.close(); } catch (SQLException ignore) {}
            }
            
            if (selectStatement != null) {
                try { selectStatement.close(); } catch (SQLException ignore) {}
            }
            
            if (dbConnection != null) {
                try { dbConnection.close(); } catch (SQLException ignore) {}
            }
            
           
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
     * @return 
     */
    public static List runStoredProcedure(
            String imeProcedure,
            String[] imenaArgumenata,
            String[] vrednostiArgumenata
    )
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
            
            if (cStmt != null) {
                try { cStmt.close(); } catch (SQLException ignore) {}
            }
            
            if (dbConnection != null) {
                try { dbConnection.close(); } catch (SQLException ignore) {}
            }
        } 
        
        return listaRezultata;   
    }
    
    /**
     * 
     * @param imeStoreProcedure
     * @param imeArgumentaSP
     * @return 
     */
    public List getRecordSetIzStoreProcedureZaKonobara(
            String imeStoreProcedure, 
            String imeArgumentaSP) 
    {
        
        Connection dbConnection = null;
        ResultSet rs = null;
        List listaRezultata = null;
        CallableStatement cStmt = null;
        
        try {
            dbConnection = poveziSaBazom();
            cStmt = dbConnection.prepareCall("{CALL " + imeStoreProcedure + "(?)}");
            cStmt.setLong(imeArgumentaSP, rmaster.RMaster.ulogovaniKonobar.konobarID);
            cStmt.execute();
            rs = cStmt.getResultSet();
            
            listaRezultata = prebaciUListu(rs);
            
        } catch (Exception e) {
            
            System.out.println("Store procedure \"" + imeStoreProcedure + "\" exec error! - " + e.toString());
        
        } finally {
            
            if (rs != null) {
                try { rs.close(); } catch (SQLException ignore) {}
            }
            
            if (cStmt != null) {
                try { cStmt.close(); } catch (SQLException ignore) {}
            }
            
            if (dbConnection != null) {
                try { dbConnection.close(); } catch (SQLException ignore) {}
            }
        } 
        
        return listaRezultata;
    }
    
    
        /**
     * 
     * @param imeStoreProcedure
     * @param imeArgumentaSP
     * @param vrednostArgumentaSP
     * @return 
     */
    public static List getRecordSetIzStoreProcedureZaParametar(
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
            
            if (cStmt != null) {
                try { cStmt.close(); } catch (SQLException ignore) {}
            }
            
            if (dbConnection != null) {
                try { dbConnection.close(); } catch (SQLException ignore) {}
            }
        } 
        
        return listaRezultata;
    }
    
    
    public List get_PorudzbineStolaIKonobara()
    {    
        Connection dbConnection = null;
        ResultSet rs = null;
        List listaRezultata = null;
        CallableStatement cStmt = null;
        
        try {
            dbConnection = poveziSaBazom();
            cStmt = dbConnection.prepareCall("{CALL getPorudzbineStolaIKonobara(?,?)}");
            cStmt.setLong("konobarID", rmaster.RMaster.ulogovaniKonobar.konobarID);
            cStmt.setString("stoID", rmaster.RMaster.izabraniStoID);
            cStmt.execute();
            rs = cStmt.getResultSet();
            
            listaRezultata = prebaciUListu(rs);
            
        } catch (Exception e) {
            System.out.println("Store procedure \"getPorudzbineStolaIKonobara\" exec error! - " + e.toString());
        } finally {
            
            if (rs != null) {
                try { rs.close(); } catch (SQLException ignore) {}
            }
            
            if (cStmt != null) {
                try { cStmt.close(); } catch (SQLException ignore) {}
            }
            
            if (dbConnection != null) {
                try { dbConnection.close(); } catch (SQLException ignore) {}
            }
        }
            
        return listaRezultata;
    }

    /**
     * 
     * @param noviKonobarID
     * @param stolovi
     * @return 
     */
    public void promeniKonobaraZaStolove(
            long noviKonobarID, 
            String stolovi) 
    {
        Connection dbConnection;
        CallableStatement cStmt;
        
        try {
            dbConnection = poveziSaBazom();
            cStmt = dbConnection.prepareCall("{CALL promeniKonobaraZaStolove(?,?,?)}");
            cStmt.setLong("StariKonobarID", rmaster.RMaster.ulogovaniKonobar.konobarID);
            cStmt.setLong("NoviKonobarID", noviKonobarID);
            cStmt.setString("stolovi", stolovi);
            cStmt.execute();
            prekiniVezuSaBazom(dbConnection);
        } catch (Exception e) {
            System.out.println("Store procedure \"promeniKonobaraZaStolove\" exec error! - " + e.toString());
        }
    }
    
        
    public List getStavkePorudzbinaGosta(String brojGosta){
        Connection dbConnection = null;
        ResultSet rs = null;
        List listaRezultata = null;
        CallableStatement cStmt = null;
        
        try {
            dbConnection = poveziSaBazom();
            cStmt = dbConnection.prepareCall("{CALL getPorudzbinaGosta(?,?,?)}");
            cStmt.setLong("konobarID", rmaster.RMaster.ulogovaniKonobar.konobarID);
            cStmt.setString("stoID", rmaster.RMaster.izabraniStoID);
            cStmt.setString("brojGosta", brojGosta);
            cStmt.execute();
            rs = cStmt.getResultSet();
            
            listaRezultata = prebaciUListu(rs);
            
        } catch (Exception e) {
            System.out.println("Store procedure \"getPorudzbinaGosta\" exec error! - " + e.toString());
        } finally {
            
            if (rs != null) {
                try { rs.close(); } catch (SQLException ignore) {}
            }
            
            if (cStmt != null) {
                try { cStmt.close(); } catch (SQLException ignore) {}
            }
            
            if (dbConnection != null) {
                try { dbConnection.close(); } catch (SQLException ignore) {}
            }
        }
            
        return listaRezultata;
    }
    
    public boolean passwordCheckZaMenadzera(String lozinkaText) throws Exception {
        boolean jesteMenadzer = false;
        String[] uslovneKolone = {"pass"};
        String[] uslovneVrednosti = {lozinkaText};
        
        List rezultat = this.vratiSveIzTabeleUzUslov(
                "login", 
                uslovneKolone, 
                uslovneVrednosti
        );
        
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
        String[] uslovneKolone = {"pin"};
        String[] uslovneVrednosti = {lozinkaText};
        
        List rezultat = this.vratiSveIzTabeleUzUslov(
                "konobar", 
                uslovneKolone, 
                uslovneVrednosti
        );
        
        if (!rezultat.isEmpty()) {
            Map<String,String>  konobar = (Map<String, String>)rezultat.get(0);
            //proveraPina(konobar);
            return new Konobar(konobar);
        }
        
        return null;
    }

    /**
     * 
     * @param noviKonobarID
     * @param stolovi
     * @return 
     */
    public void oslobodiSto(
            long RacunID, 
            int stoBroj) 
    {
        Connection dbConnection;
        CallableStatement cStmt;
        
        try {
            dbConnection = poveziSaBazom();
            cStmt = dbConnection.prepareCall("{CALL zatvoriRacun(?,?)}");
            cStmt.setLong("RacunID", RacunID);
            cStmt.setInt("stoBroj", stoBroj);
            cStmt.execute();
            prekiniVezuSaBazom(dbConnection);
        } catch (Exception e) {
            System.out.println("Store procedure \"promeniKonobaraZaStolove\" exec error! - " + e.toString());
        }
    }
    
    
}