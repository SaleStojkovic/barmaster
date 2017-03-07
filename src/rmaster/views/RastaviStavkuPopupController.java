/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.StageStyle;
import rmaster.models.StavkaTure;

/**
 *
 * @author Arbor
 */
public class RastaviStavkuPopupController extends Dialog {
    
    String kolicina;    
    
    
    
    public RastaviStavkuPopupController(StavkaTure izabranaStavka) {
        
        Map<String, String> mapa = izabranaStavka.dajStavkuTure();
        
        kolicina = mapa.get("kolicina");
        
        //TODO 
        
        this.initStyle(StageStyle.UNDECORATED);

        // Set the button types.
        ButtonType odustaniButtonType = new ButtonType("X", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        ButtonType promeniButton = new ButtonType("√", ButtonBar.ButtonData.OK_DONE);
        
        this.getDialogPane().getButtonTypes().addAll(odustaniButtonType, promeniButton);
        
        this.getDialogPane().getStylesheets().
                addAll(this.getClass().getResource("style/style.min.css").toExternalForm());
        
        this.getDialogPane().getStyleClass().add("myDialog");
        
    }
    
    public void initialize(URL url, ResourceBundle rb) {

    } 
    
    public void cancelAction(ActionEvent event) {
        zatvoriOvuFormu();
    }
    
    public void zatvoriOvuFormu(){
        try {
        this.setResult(null);
        this.close();
        } catch (Exception e){
            System.out.println("Neuspelo zatvaranje forme - PrikazSalePopupController" + e);
        }
    }
}
