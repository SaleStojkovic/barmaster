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
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private VBox  lojalnostSlova;
    
    @FXML
    private ScrollPane lojalnostScrollPane;
    
    @FXML
    private TableView<Map<String, String>> tabelaLojalnost = new TableView();
    
    public int[] sirineKolone = {50, 50, 50, 50, 50, 50, 50};
    
    public String prviRed = "ABCDEFGHIJKLM12345";
    
    public String drugiRed = "NOPQRSTUVWXYZ67890";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Timeline timeline = this.prikaziCasovnik(casovnik);
        timeline.play();
        this.imeKonobara.setText(ulogovaniKonobar.imeKonobara);

        HBox prviRedBox = new HBox();
        
        for(char ch : prviRed.toCharArray()) {
            
            Button novoDugme = new Button();
            
            novoDugme.setId(ch + "");
            novoDugme.setPrefSize(56, 50);
            novoDugme.setText(ch + "");
            novoDugme.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        //TODO
                                    }
                                });
            prviRedBox.getChildren().add(novoDugme);
        }
        
        lojalnostSlova.getChildren().add(prviRedBox);
        
        HBox drugiRedBox = new HBox();

                
        for(char ch : drugiRed.toCharArray()) {
           
            Button novoDugme = new Button();
            
            novoDugme.setId(ch + "");
            novoDugme.setPrefSize(56, 50);
            novoDugme.setText(ch + "");
            novoDugme.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        //TODO
                                    }
                                });
            drugiRedBox.getChildren().add(novoDugme);
        }
        
        lojalnostSlova.getChildren().add(drugiRedBox);

        
        tabelaLojalnost = tableHelper.formatirajTabelu(
                tabelaLojalnost, 
                getStalniGosti(), 
                sirineKolone
        );
        
        lojalnostScrollPane.setContent(tabelaLojalnost);
    } 
    
    @Override
    public void initData(Map<String, String> data)
    {
        
    }
    
    public List<Map<String, String>> getStalniGosti() {
        
        List<Map<String, String>> listaStalnihGostiju = this.vratiSveIzTabele("stalnigost");
              
        return listaStalnihGostiju;
    }
    
}
