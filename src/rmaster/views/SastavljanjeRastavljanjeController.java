/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import rmaster.assets.FXMLDocumentController;
import rmaster.ScreenController;
import rmaster.assets.ScreenMap;
import rmaster.assets.Utils;
import rmaster.models.Gost;
import rmaster.models.Porudzbina;
import rmaster.models.Sto;
import rmaster.models.Tura;

/**
 * FXML Controller class
 *
 * @author Arbor
 */
public class SastavljanjeRastavljanjeController extends FXMLDocumentController {

        ScreenController myController; 
     
    @Override
    public void setScreenParent(ScreenController screenParent){ 
        myController = screenParent; 
    } 
    
    @FXML
    private Label imeKonobara;
    
    @FXML 
    private Label casovnik;
    
    @FXML
    private Button izaberiStoA;
   
    @FXML
    private Button izaberiStoB;
    
    
    @FXML
    private HBox gostiStoA = new HBox();
    
    @FXML
    private HBox gostiStoB = new HBox();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
    @Override
    public void initData(Object data)
    {
        imeKonobara.setText(RMaster.ulogovaniKonobar.imeKonobara);
        
        Timeline timeline = this.prikaziCasovnik(casovnik);
        timeline.play();
    }
    
    public void nazadNaPrikazSale(ActionEvent event) 
    {            
        //sledeca stranica 
        myController.setScreen(ScreenMap.PRIKAZ_SALA, null);
    }
    
    public void pozivanjePrikazSalePopup(ActionEvent event) {

        SalePopupController tastatura = new SalePopupController();
        
        Optional<HashMap<String, String>> result = tastatura.showAndWait();
        
        if (!result.isPresent()){
            return;
        }
        
        if (!(result.get() instanceof HashMap)) {
            return;
        }
        
        HashMap<String, String> izabraniStoMap = result.get();

        Button pritisnutoDugme = (Button)event.getSource();
        pritisnutoDugme.setText(izabraniStoMap.get("stoNaziv"));
        
        //zavisno koje dugme je stisnuto popuniti liste gostiju za taj sto
        if (pritisnutoDugme.getId().equals("izaberiStoA")) {
            
            gostiStoA.getChildren().clear();
            
            popuniGoste(gostiStoA, izabraniStoMap);
            
        }
        
        if (pritisnutoDugme.getId().equals("izaberiStoB")) {
            
            gostiStoB.getChildren().clear();
            
            popuniGoste(gostiStoB, izabraniStoMap);
        }
    }
    
    @Override
    public void odjava(ActionEvent event)
    {            
        myController.setScreen(ScreenMap.POCETNI_EKRAN, null);
    }
    
    public void popuniGoste(HBox gosti, HashMap<String, String> sto) 
    {
        Sto izabraniSto = new Sto(sto);
        
        List racuniStola = izabraniSto.getPorudzbineStola();
                
        if (racuniStola.isEmpty())
        {
            //sta se desava ako nema nijednog gosta
            return;
        }
        
                List<Node> lista = new ArrayList<>();
        
        for (Object racun : racuniStola) {
            
            Map<String, String> red = (Map<String, String>) racun;

            String brojNovogGosta = red.get("gost");

//            Porudzbina porudzbinaTrenutna = new Porudzbina(
//                    new Gost(brojNovogGosta), 
//                    red.get("id"),
//                    sto.get("stoNaziv"),
//                    sto.get("stoBroj")
//            );

            RadioButton noviGostButton = new RadioButton(brojNovogGosta);

            lista.add(prikaziPorudzbinuTaskSetButtonAction(noviGostButton, brojNovogGosta));
        } 
        
        ObservableList<Node> listaDugmica = FXCollections.observableArrayList(lista);            
                
        gosti.getChildren().addAll(listaDugmica);
    }
    
     private RadioButton prikaziPorudzbinuTaskSetButtonAction(
            RadioButton noviGostButton, 
            String brojNovogGosta)
    {
            noviGostButton.getStyleClass().remove("radio-button");
            noviGostButton.getStyleClass().add("toggle-button");

            //ocigledno da ce morati da se stave u toogle grupe
//            noviGostButton.setToggleGroup(gostiButtonGroup);

            noviGostButton.setId(brojNovogGosta);

            noviGostButton.setPrefSize(57, 57);
            
            noviGostButton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    
                                    //TODO
//                                    String gost = ((RadioButton)e.getSource()).getId();
//                                    Utils.postaviStil_ObrisiZaOstaleKontroleRoditelja(e, stilButtonGrupeSelektovana);
//
//                                    for (Porudzbina porudzbina : porudzbineStola) {
//                                        if (porudzbina.getGost().getGostID() == Long.parseLong(gost)) {
//                                            porudzbinaTrenutna = porudzbina;
//                                            prikaziPorudzbinu(porudzbinaTrenutna);
//                                            novaTura = null;
//                                            for (Tura tura : porudzbinaTrenutna.getTure()) {
//                                                if (tura.getTuraID() == 0) {
//                                                    novaTura = tura;
//                                                }
//                                            }
//                                            break;
//                                        }
//                                    }
                                }
                            });
            
            return noviGostButton;
    }
}
