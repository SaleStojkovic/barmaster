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
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.StageStyle;
import rmaster.models.StavkaTure;

/**
 *
 * @author Arbor
 */
public class RastaviStavkuPopupController extends Dialog {
    
    int kolicinaGlavni;    
    
    String kolicinaOpisni;
    
    String kolicinaDodatni;
    
    TextField kolicinaGlavniText = new TextField();

    HashMap<String, String> izabranaStavkaMap = new HashMap<>();
   
    public RastaviStavkuPopupController(StavkaTure izabranaStavka) {
        
        this.setHeaderText("Napravite novu stavku");
                
        kolicinaGlavni =  (int)izabranaStavka.kolicina - 1;
        
        izabranaStavkaMap = (HashMap)izabranaStavka.dajStavkuTure();
        
        this.initStyle(StageStyle.UNDECORATED);

        // Set the button types.
        ButtonType odustaniButtonType = new ButtonType("X", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        ButtonType promeniButton = new ButtonType("✓", ButtonBar.ButtonData.OK_DONE);
        
        this.getDialogPane().getButtonTypes().addAll(odustaniButtonType, promeniButton);
        
        this.getDialogPane().getStylesheets().
                addAll(this.getClass().getResource("style/style.min.css").toExternalForm());
        
        this.getDialogPane().getStyleClass().add("myDialog");
        
        
        VBox content = new VBox(5);
        
        HBox glavnaStavka = new HBox(10);
        
        Label glavniArtikal = new Label();
        
        glavniArtikal.setStyle(
                "-fx-border-color: white;"
                + "-fx-padding: 5 5 5 5;"
        );
        
        glavniArtikal.setPrefSize(150, 115);
        
        glavniArtikal.wrapTextProperty().setValue(Boolean.TRUE);
        
        glavniArtikal.textAlignmentProperty().set(TextAlignment.LEFT);
         
        glavniArtikal.setText(izabranaStavka.naziv);
        
        VBox kolicinaGlavniContent = new VBox();
        
        Button povecajKolicinuGlavni = new Button("▲");
        
        povecajKolicinuGlavni.setPrefSize(90, 40);
        
        
        kolicinaGlavniText.setPrefSize(90, 30);
        
        kolicinaGlavniText.setText("x" + kolicinaGlavni);
        
        kolicinaGlavniText.alignmentProperty().setValue(Pos.CENTER);

        Button smanjiKolicinuGlavni = new Button("▼");
         
        smanjiKolicinuGlavni.setPrefSize(90, 40);
        
        kolicinaGlavniContent.getChildren().addAll(
                povecajKolicinuGlavni,
                kolicinaGlavniText,
                smanjiKolicinuGlavni
        );
        
        glavnaStavka.getChildren().addAll(
            glavniArtikal,
            kolicinaGlavniContent    
        );
        
        
        //TODO
        //dodati opisne i dodatne artikle 
        
        content.getChildren().add(glavnaStavka);
        
        
        smanjiKolicinuGlavni.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override public void handle(ActionEvent e) {
                                            try {
                                                incrementOrDecrementKolicinaGlavni("D");
                                            } catch (Exception ex) {
                                            }
                                        }
                                    });
        
        
        povecajKolicinuGlavni.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    try {
                                        incrementOrDecrementKolicinaGlavni("I");
                                    } catch (Exception ex) {
                                    }
                                }
                            });
        
        this.getDialogPane().setContent(content);
        
        this.setResultConverter(dialogButton -> {
            if (dialogButton == promeniButton) {
                return izabranaStavkaMap;
            }
            return null;
        });
    }
    
    public void initialize(URL url, ResourceBundle rb) {

    } 
    
    private void incrementOrDecrementKolicinaGlavni(String akcija) {
        
        String text = kolicinaGlavniText.getText();
        
        int kolicina = Integer.parseInt(text.replaceAll("x", ""));
        
        if (akcija.equals("I") && kolicina < kolicinaGlavni) {
            kolicina++;
            
            kolicinaGlavniText.setText("x" + kolicina);
            
            return;
        }
        
        if (akcija.equals("D") && kolicina > 1) {
            kolicina--;
            kolicinaGlavniText.setText("x" + kolicina); 
        }

        izabranaStavkaMap.put("kolicina", "x" + kolicina);
    }
    
    
}
