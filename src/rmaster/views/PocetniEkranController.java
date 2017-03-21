/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import rmaster.assets.FXMLDocumentController;
import rmaster.assets.QueryBuilder.QueryBuilder;
import rmaster.ScreenController;
import rmaster.assets.ScreenMap;
import rmaster.assets.Settings;
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
    
    @FXML
    private ImageView barMasterLogo;
    
    @FXML
    private AnchorPane pocetniEkran;
    
    ScreenController myController; 

    @Override
    public void setScreenParent(ScreenController screenParent){ 
        myController = screenParent; 
     } 
    
    @Override
    public void initData(Object data)
    {
        lozinka.setText("");
        timelineSat = this.prikaziCasovnik(clock);
        timelineSat.play();
        
        RMaster.firstLogin = true;
        RMaster.saleOmoguceneKonobaru.clear();
        RMaster.saleZabranjeneKonobaru.clear();
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        String pozadina = Settings.getInstance().getValueString("sale.slike.putanja") + "start_screen.jpg" ;  

        pocetniEkran.setStyle("-fx-background-image: url('" 
                + pozadina + "'); " +
           "-fx-background-repeat: stretch;" +
           "-fx-background-size: 1024 768;");
        
        barMasterLogo.setImage(RMaster.logo);
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
        
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        query.setTableName("konobar");
        query.addCriteriaColumns("pin");
        query.addCriteria(QueryBuilder.IS_EQUAL);
        query.addCriteriaValues(lozinkaText);
        
        List<Map<String, String>> rezultat = runQuery(query);
        
        if (!rezultat.isEmpty()) {
            
            Map<String, String>  konobar = rezultat.get(0);
            
            //zapamti KonobarID
            setUlogovaniKonobar(konobar);
                    
            kesirajSaleOmoguceneKonobaru();

            new Thread(){
                @Override
                public void start(){
                   RMaster.ucitajSveArtikle();  
                }
            }.start();
            
            
            myController.setScreen(ScreenMap.PRIKAZ_SALA, null);
            timelineSat.stop();

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
    
    private void kesirajSaleOmoguceneKonobaru()
    {
        RMaster.ucitajSveSaleZabranjeneKonobaru();

        //setSaleOmoguceneKonobaru(getUlogovaniKonobar().saleOmoguceneKonobaru());
    }
    
    @Override
    public void odjava(ActionEvent event)
    {            
        myController.setScreen(ScreenMap.POCETNI_EKRAN, null);
        RMaster.firstLogin = true;
        RMaster.saleZabranjeneKonobaru.clear();
        RMaster.saleOmoguceneKonobaru.clear();
    }
}
