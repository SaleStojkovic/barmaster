/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import rmaster.assets.FXMLDocumentController;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.PasswordField;

/**
 *
 * @author Arbor
 */
public class PocetniEkranController extends FXMLDocumentController {
    
    @FXML 
    private Label response;
    
    @FXML
    private PasswordField lozinka;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void backButton(ActionEvent event) {
        response.setText("");
        String lozinkaText = this.lozinka.getText();
        
        if (lozinkaText.length() != 0) {
            lozinkaText = lozinkaText.substring(0, lozinkaText.length()-1);
            this.lozinka.setText(lozinkaText);
        }
    }
   
    public void numberKeyPressed(ActionEvent event) throws Exception {
        
        Button pritisnutTaster = (Button)event.getSource();
        
        String lozinkaText = this.lozinka.getText();
        
        if (lozinkaText.length() < 4) {
           response.setText(""); 
           lozinkaText += pritisnutTaster.getText();
           this.lozinka.setText(lozinkaText);
        } 
        
    }  
    
    public void passwordCheck(ActionEvent event) throws Exception {
        response.setText("");
        String lozinkaText = this.lozinka.getText();
        
        String[] uslovneKolone = {"pin"};
        String[] uslovneVrednosti = {lozinkaText};
        
        List rezultat = vratiKoloneIzTabele(
                "konobar", 
                uslovneKolone, 
                uslovneVrednosti
        );
        
        if (!rezultat.isEmpty()) {
            Map<String,String>  konobar = (Map<String, String>)rezultat.get(0);
            
            //zapamti KonobarID
            setUlogovaniKonobar(konobar);
            
            //sledeca stranica 
            prikaziFormu(
                    "prikazSala", 
                    true, 
                    response
            );
        }
        
        response.setText("Neuspelo logovanje!");
        lozinka.setText("");
    }
   
}
