/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import rmaster.assets.FXMLDocumentController;
import rmaster.assets.QueryBuilder;
import rmaster.assets.ScreenMap;
import rmaster.models.LoginAction;
/**
 *
 * @author Arbor
 */
public class PocetniEkranController extends FXMLDocumentController {
   
    @FXML 
    private Label response;
        
    @FXML
    private PasswordField lozinka;
    
    @FXML
    private Label clock;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lozinka.requestFocus();
        Timeline timeline = this.prikaziCasovnik(clock);
        timeline.play();
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
        
        lozinka.requestFocus();
        
    }  
    
    public void passwordCheck() {
        response.setText("");
        String lozinkaText = this.lozinka.getText();
        
        LoginAction akcija = new LoginAction();
        
        akcija.takeAction(lozinkaText);
        
        QueryBuilder query = new QueryBuilder();
        query.setTableName("konobar");
        query.addCriteriaColumns("pin");
        query.addCriteria(QueryBuilder.IS_EQUAL);
        query.setCriteriaValues(lozinkaText);
        
        List<Map<String, String>> rezultat = runQuery(query);
        
        if (!rezultat.isEmpty()) {
            
            Map<String,String>  konobar = rezultat.get(0);
            
            //zapamti KonobarID
            setUlogovaniKonobar(konobar);
            
            List<Object> newData = new ArrayList<>();
                            
            //sledeca stranica 
            prikaziFormu(newData,
                    ScreenMap.PRIKAZ_SALA, 
                    true, 
                    response, false
            );
            return;
        }
        
        response.setText("Neuspelo logovanje!");
        lozinka.setText("");
        lozinka.requestFocus();
    }
    
    public void onEnter(KeyEvent key){
        if (key.getCode().equals(KeyCode.ENTER))
        {
            passwordCheck();
        }
    }
}
