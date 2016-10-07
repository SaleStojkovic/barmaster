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
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import rmaster.assets.FXMLDocumentController;

/**
 * FXML Controller class
 *
 * @author Arbor
 */
public class RezervacijeController extends FXMLDocumentController {

    @FXML
    private Label casovnik;
    
    @FXML
    private Label imeKonobara;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Timeline timeline = this.prikaziCasovnik(casovnik);
        timeline.play();
        
        imeKonobara.setText(ulogovaniKonobar.imeKonobara);
    }    
    
    public void nazadNaPrikazSale(ActionEvent event) {
            Map<String, String> newData = new HashMap<>();
            
            //sledeca stranica 
            prikaziFormu(
                    newData,
                    "prikazSala", 
                    true, 
                    (Node)event.getSource()
            );
    }
    
    public void pomeriScrollDownRezervacije(){}
    
    public void pomeriScrollUpRezervacije(){}
}
