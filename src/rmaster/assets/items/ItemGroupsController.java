/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets.items;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import rmaster.assets.DBBroker;

/**
 *
 * @author Bosko
 */
public class ItemGroupsController {
    public Connection con = null;
    public Statement stmt = null;
    public ResultSet rs = null;
    
    private final List<Node> itemsGroupsListeners = new ArrayList<>();
    private final Map<Long,ItemGroup> itemsHashMap = new HashMap<>();
    //private List<ItemGroup> itemGroupsCollection = new ArrayList<ItemGroup>();
    private HBox hbox;
    
    public void prikaziPodgrupe(long idNadredjeneGrupe) {
        
    }
    private void refreshData() {
        // Uspostavljanje konekcije
        try {
            con = new DBBroker().poveziSaBazom();
        } catch(Exception e) {
            System.out.println("Database connection error!");
        }
        
        // Dobavljanje podataka iz baze
        try {
            stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT * FROM get_GrupaProizvodaGlavna_ByPrioritet");
        } catch (Exception e) {
            System.out.println("View \"get_GrupaProizvodaGlavna_ByPrioritet\" fetch error!");
        }
        
        //Popunjava HashMap-u sa podacima o glavnim grupama
        try {
            while (rs.next()) {
                ItemGroup ig = new ItemGroup(
                        rs.getString("naziv"),
                        rs.getString("slika"), 
                        rs.getLong("id"), 
                        rs.getInt("prioritet"), 
                        rs.getString("skrNaziv"), 
                        rs.getLong("GRUPA_ID"), 
                        true, false, false);
                itemsHashMap.put(rs.getLong("id"), ig);
            }
        } catch (Exception e) {
            System.out.println("Database connection error!");
        }
        PrikazGlavnihGrupa();
    }

    public void PrikazGlavnihGrupa() {
        // Prikaz glavnih grupa na ekran
        HBox hbox = (HBox)itemsGroupsListeners.get(0);
        hbox.getChildren().clear();
        for(Map.Entry<Long, ItemGroup> entry : itemsHashMap.entrySet()){
            long idGrupe = entry.getKey();
            ItemGroup ig = entry.getValue();
            Button b = new Button();
            b.setId("" + ig.getID());
            b.setText(ig.getName() + " - " + b.getId());
            //Image imagePozadinaGrupe = new Image(getClass().getResourceAsStream("rmaster.views.style.img.Grupe.png"));
            //b.setGraphic(new ImageView(imagePozadinaGrupe));
            b.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    new ItemSubGroupsController((((Button)e.getSource()).getScene().lookup("#ArtikalPodgrupeTable")) ,Long.parseLong(((Button)e.getSource()).getId()));
                                    //new ItemSubGroupsController((((Button)e.getSource()).getScene().lookup("#idPodgrupeArtikala")) ,Long.parseLong(((Button)e.getSource()).getId()));
                                }
                            });

            hbox.getChildren().add(b);
        }
        
    }
    public ItemGroupsController(Node itemGroupListener) {
        itemsGroupsListeners.add(itemGroupListener);
        this.refreshData();
        
    }
}
