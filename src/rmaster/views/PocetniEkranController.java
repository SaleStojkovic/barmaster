/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
/**
 *
 * @author Arbor
 */
public class PocetniEkranController extends NumberKeypadController {
    
    @FXML 
    private Label response;
    
    @FXML
    private PasswordField lozinka;
    
    @FXML
    private Label clock;
    
    @FXML
    private Button test;
    
    private final NumberKeypadController keypad = new NumberKeypadController();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        keypad.sledecaForma = "prikazSala";
        Timeline timeline = this.prikaziCasovnik(clock);
        timeline.play();

    }
    
   
}
