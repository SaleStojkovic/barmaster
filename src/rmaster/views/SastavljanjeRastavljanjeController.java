/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import rmaster.assets.FXMLDocumentController;
import rmaster.assets.ScreenMap;

/**
 * FXML Controller class
 *
 * @author Arbor
 */
public class SastavljanjeRastavljanjeController extends FXMLDocumentController {

    
    @FXML
    private Label imeKonobara;
    
    @FXML 
    private Label casovnik;
    
    @FXML
    private Button izaberiStoA;
   
    @FXML
    private Button izaberiStoB;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imeKonobara.setText(ulogovaniKonobar.imeKonobara);
        
        Timeline timeline = this.prikaziCasovnik(casovnik);
        timeline.play();
    }    
    
    
    public void nazadNaPrikazSale(ActionEvent event) 
    {
                    Map<String, String> newData = new HashMap<>();
            
            //sledeca stranica 
            prikaziFormu(
                    newData,
                    ScreenMap.PRIKAZ_SALA, 
                    true, 
                    (Node)event.getSource()
            );
    }
    
    public void pozivanjePrikazSalePopup(ActionEvent event) {

        SalePopupController tastatura = new SalePopupController();
        
        Optional<String> result = tastatura.showAndWait();
        
        if (result.isPresent()){
            String noviTekst = result.get();
            
            if (!noviTekst.isEmpty()) {
                Button pritisnutoDugme = (Button)event.getSource();
                pritisnutoDugme.setText(noviTekst);
               
            }
        }
    }
}
