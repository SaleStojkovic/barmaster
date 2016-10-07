/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets.items;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import rmaster.assets.DBBroker;

/**
 *
 * @author Bosko
 */
public class ItemSubGroupsController {
    
    private long itemParentID = 0;
    public Connection con = null;
    public Statement stmt = null;
    public ResultSet rs = null;
    
    private List<Node> itemSubGroupsListeners = new ArrayList<Node>();
    private List<ItemGroup> itemSubGroupsCollection = new ArrayList<ItemGroup>();
    //private HBox hbox;
    private FlowPane gp;
    
    private void refresh() {
        try {
            //HBox hbox = (HBox)itemSubGroupsListeners.get(0);
            gp = (FlowPane)itemSubGroupsListeners.get(0);
            gp.getChildren().clear();
            int index=0;
            while (rs.next()) {
                long id = rs.getLong("id");
                String naziv = rs.getString("naziv");
                Button b = new Button();
//              b.setId("itemSubroup-" + id);
                b.setId("" + id);
                b.textProperty().set(naziv + " - " + b.getId());
                b.setPrefSize(192, 64);
                b.setMaxSize(250, 64);
                b.setMinSize(150, 64);
                b.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        System.out.println("Item Subgroup: " + ((Button)e.getSource()).getId());
                                    }
                                });
                gp.getChildren().add((Node)b);//, (index % 4), index/4);
                //gp.getChildren().add(index++, b);
                index++;
                //hbox.getChildren().add(b);
                if (index>14)
                    break;
            }
            Region r = new Region();
            gp.getChildren().add(r);
            
            Button bDalje = new Button(">");
            bDalje.setPrefSize(192, 64);
            gp.getChildren().add(bDalje);//, 4,3);
        } catch (Exception e) {
            System.out.println("Database connection error!");
        }
    }
   
    private void runQuery() {
        try {
//            String spGetItemSubGroupByID = "{ call get_GrupaProizvodaPodgrupa_ByPrioritet(?) }";
//            // Step-3: prepare the callable statement
//            CallableStatement cs = con.prepareCall(spGetItemSubGroupByID);
//            // Step-4: set input parameters ...
//            // first input argument
//            cs.setLong("nadredjenaGrupa", itemParentID);
//            // Step-6: execute the stored procedures: proc3
//            cs.execute();
    
//            stmt = con.createStatement();
//
//            rs = stmt.executeQuery("SELECT * FROM podgrupaartikala Where GRUPA_ID = " + itemParentID + " Order By prioritet");
            
            CallableStatement cStmt = con.prepareCall("{CALL get_GrupaProizvodaPodgrupa_ByPrioritet(?)}");
            cStmt.setLong("nadredjenaGrupa", itemParentID);
            cStmt.execute();
            rs = cStmt.getResultSet();
//            ResultSet rs1 = cStmt.getResultSet();
//            while (rs1.next()) {
//            System.out.println(rs1.getString("department_name") + " "
//               + rs1.getString("location"));
//            }
//            rs1.close();

        } catch (Exception e) {
            System.out.println("Table \"podgrupaartikala\" fetch error!");
        }
        this.refresh();
    }
    
    public ItemSubGroupsController(Node itemGroupListener, long itemParentID) {

        DBBroker db = new DBBroker();
        con = db.poveziSaBazom();

        if (con != null)
            //login();
            System.out.println("Connected to database " + con.toString());
        else
            System.out.println("Database connection error!");
        
        this.itemParentID = itemParentID;
        itemSubGroupsListeners.add(itemGroupListener);
        runQuery();
    }
}
