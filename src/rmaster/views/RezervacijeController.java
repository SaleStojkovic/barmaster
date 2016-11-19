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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import rmaster.assets.FXMLDocumentController;
import rmaster.assets.ScreenMap;
import rmaster.assets.TastaturaVrsta;
import rmaster.models.Rezervacija;


public class RezervacijeController extends FXMLDocumentController {

    @FXML
    private Label casovnik;
    
    @FXML
    private Label imeKonobara;
    
    @FXML
    private ScrollPane scrollPaneRezervacije;
    
    @FXML
    private TextField ime;
    
    @FXML 
    private TextField vreme;
//    
//    @FXML 
//    private DatePicker
    
    public TableView<Map<String, String>> tabelaRezervacija = new TableView<>();
    
    public List<Rezervacija> listaRezervacija = new ArrayList();
    
    public List<Map<String, String>> listaZaPrikaz = new ArrayList();
    
    public int[] sirinaKolonaTabele = {140, 100, 100, 100, 100, 100, 242, 0};
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Timeline timeline = this.prikaziCasovnik(casovnik);
        timeline.play();
        
        getRezervacije();
        
        tabelaRezervacija = tableHelper.formatirajTabelu(
                tabelaRezervacija, 
                listaZaPrikaz,
                sirinaKolonaTabele
        );
        
        scrollPaneRezervacije.setContent(tabelaRezervacija);
        imeKonobara.setText(ulogovaniKonobar.imeKonobara);
        
        
//        ime.focusedProperty().addListener(new ChangeListener<Boolean>()
//        {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
//            {
//                if (newPropertyValue)
//                {
//                    pozivanjeAlfaNumerickeTastature();
//                }
//               
//            }
//        });
    }    
    
    public void nazadNaPrikazSale(ActionEvent event) {
            Map<String, String> newData = new HashMap<>();
            
            //sledeca stranica 
            prikaziFormu(
                    newData,
                    ScreenMap.PRIKAZ_SALA, 
                    true, 
                    (Node)event.getSource()
            );
    }
    
    public void pomeriScrollDownRezervacije(){}
    
    public void pomeriScrollUpRezervacije(){}
    
    
    public void getRezervacije() {
        List<HashMap<String,String>> listaRezervacija = DBBroker.vratiSveIzTabele("rezervacija"); 
        
        for(HashMap<String, String> rezervacijaMapa : listaRezervacija) {
            Rezervacija novaRezervacija = new Rezervacija(rezervacijaMapa);
            
            this.listaZaPrikaz.add((Map<String, String>)novaRezervacija.toHashMap());
            this.listaRezervacija.add(novaRezervacija);
        }
        
        
    }
    
    public void pozivanjeAlfaNumerickeTastature(MouseEvent event) {
        
        TextField polje = (TextField)event.getSource();
        
        TastaturaController tastatura = new TastaturaController(
                TastaturaVrsta.ALFA_NUMERICKA_TASTATURA, polje.getText());
        
        Optional<String> result = tastatura.showAndWait();
        
        if (result.isPresent()){
            
            String noviTekst = result.get();
            
            if (!noviTekst.isEmpty()) {
                ime.setText(noviTekst);
                return;
            } 
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Greška!");
            alert.setHeaderText("Neispravna količina!");
            alert.setContentText("Unesite novu količinu.");
            alert.showAndWait();
        }
    }
}
