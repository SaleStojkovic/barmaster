/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import rmaster.assets.FXMLDocumentController;

/**
 * FXML Controller class
 *
 * @author Arbor
 */
public class LojalnostController extends FXMLDocumentController {

    
    @FXML
    private Label casovnik;
    
    @FXML
    private Label imeKonobara;
    
    @FXML
    private HBox  lojalnostSlova = new HBox();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Timeline timeline = this.prikaziCasovnik(casovnik);
        timeline.play();
        this.imeKonobara.setText(ulogovaniKonobar.imeKonobara);
        //dodaje dugmice jer me mrzi da pravim 26 dugmeta... :P
        for(char alphabet = 'A'; alphabet <= 'Z';alphabet++) {
           
            Button novoDugme = new Button();
            
            novoDugme.setId(alphabet + "");
            novoDugme.setPrefSize(39.3, 50);
            novoDugme.setText(alphabet + "");
            novoDugme.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        //TODO
                                    }
                                });
            lojalnostSlova.getChildren().add(novoDugme);
        }
    } 
    
//    @Override
    public void initData(Map<String, String> data)
    {
        
    }
    
}
