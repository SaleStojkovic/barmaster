/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import rmaster.assets.FXMLDocumentController;

/**
 * FXML Controller class
 *
 * @author Arbor
 */
public class ConfirmFormController extends FXMLDocumentController {
    
    @FXML
    private Label response2;
    
    @FXML
    private PasswordField lozinka2;
   
    @FXML
    private Button cancelButton;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    

    public void passwordCheck(ActionEvent event) throws Exception {
        response2.setText("");
        String lozinkaText = this.lozinka2.getText();
        
        String[] uslovneKolone = {"pin"};
        String[] uslovneVrednosti = {lozinkaText};
        
        List rezultat = vratiKoloneIzTabele(
                "konobar", 
                uslovneKolone, 
                uslovneVrednosti
        );
        
        if (!rezultat.isEmpty()) {
            Map<String,String>  konobar = (Map<String, String>)rezultat.get(0);
            
            proveraPina(konobar);
            return;
        }
        
        response2.setText("Neuspešna promena!");
        lozinka2.setText("");
    }
    
    /**
     * 
     * @param konobar 
     */
    public void proveraPina(Map<String, String> konobar) {
        String unetKonobarId = konobar.get("id") + "";
        String trenutniKonobar = ulogovaniKonobar.konobarID + "";
        
        if (trenutniKonobar.equals(unetKonobarId))
        {
            response2.setText("Greška! Već ste ulogovani!");
            lozinka2.setText("");
            return;
        }
        
        DBBroker.promeniKonobaraZaStolove(
                Long.parseLong(unetKonobarId),
                data.get("promenjeniStolovi")
        );
        zatvoriOvuFormu();
    }
    
    public void cancelAction(ActionEvent event) {
        zatvoriOvuFormu();
    }
    
    
    public void backButton(ActionEvent event) {
        response2.setText("");
        String lozinkaText = this.lozinka2.getText();
        
        if (lozinkaText.length() != 0) {
            lozinkaText = lozinkaText.substring(0, lozinkaText.length()-1);
            this.lozinka2.setText(lozinkaText);
        }
    }
   
    public void numberKeyPressed(ActionEvent event) throws Exception {
        
        Button pritisnutTaster = (Button)event.getSource();
        
        String lozinkaText = this.lozinka2.getText();
        
        if (lozinkaText.length() < 4) {
           response2.setText(""); 
           lozinkaText += pritisnutTaster.getText();
           this.lozinka2.setText(lozinkaText);
        } 
        
    }  
    
    public void zatvoriOvuFormu(){
        try {
        cancelButton.getScene().getWindow().hide();
        } catch (Exception e){
            System.out.println("Neuspelo zatvaranje forme - ConfirmFormController");
        }
    }

}
