/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import rmaster.assets.FXMLDocumentController;
import rmaster.assets.ScreenMap;

/**
 * FXML Controller class
 *
 * @author Bosko
 */
public class AdministracijaController extends FXMLDocumentController{

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void nazadNaPrikazSala(ActionEvent event) {
        Map<String, String> newData = new HashMap<>();

        prikaziFormu(
            newData,
            ScreenMap.PRIKAZ_SALA, 
            true, 
            (Node)event.getSource());
    }
    
}
