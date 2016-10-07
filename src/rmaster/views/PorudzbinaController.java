/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import rmaster.assets.items.ItemGroupsController;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import rmaster.assets.FXMLDocumentController;

/**
 * FXML Controller class
 *
 * @author Bosko
 */
public class PorudzbinaController extends FXMLDocumentController {
    
    @FXML
    public HBox idGrupeArtikala;
    @FXML
    public HBox idPodgrupeArtikala;
    @FXML
    public FlowPane ArtikalPodgrupeTable;
    
    private static int counter = 0;
    private List<Button> c = new ArrayList<>();
    
    public void btnExit_Action(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Exit?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Platform.exit();
            System.exit(0);
        }
    }

    private void hboxGrupeArtikalaRefresh(HBox hbox) {
        hbox.getChildren().clear();
        for (Button next : c) {
            hbox.getChildren().add(next);
        }
    }
    
    public void btnAddButton_Action(ActionEvent event) {
        ItemGroupsController igdb = new ItemGroupsController(idGrupeArtikala);
    }
    

    public void prikaziSalu(ActionEvent event) {
        prikaziFormu(
                "prikazSala",
                true, 
                (Node)event.getSource()
        );
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
