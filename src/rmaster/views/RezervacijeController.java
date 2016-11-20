/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import static javax.management.Query.value;
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
    
    @FXML 
    private DatePicker datumPicker;
    
    @FXML
    private TextField timePicker;
    
    @FXML
    private TextField izabraniSto;
    
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
        
        datumPicker.setPromptText("Izaberite datum");
        ime.setPromptText("Unesite ime");
        
        timePicker.setText("00:00");
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
        String prethodniTekst = "";
        
        if (event.getSource() instanceof TextField) {
            TextField polje = (TextField)event.getSource();
            prethodniTekst = polje.getText();
        }
        
        if (event.getSource() instanceof TextArea) {
            TextArea polje = (TextArea)event.getSource();
            prethodniTekst = polje.getText();
        }
        AlfaNumerickaTastaturaController tastatura = new AlfaNumerickaTastaturaController(prethodniTekst);
        
        Optional<String> result = tastatura.showAndWait();
                
        if (result.isPresent()){
            
            String noviTekst = result.get();
            
            if (event.getSource() instanceof TextField) {
                TextField textPolje = (TextField)event.getSource();
                textPolje.setText("");
                
                if (!noviTekst.isEmpty()) {
                    textPolje.setText(noviTekst);
                } 
            }
            
            if (event.getSource() instanceof TextArea) {
                TextArea textPolje = (TextArea)event.getSource();
                textPolje.setText("");
                
                if (!noviTekst.isEmpty()) {
                    textPolje.setText(noviTekst);
                }
            }
            
        }
    }
    
    public void pozivanjeTimePicker(MouseEvent event) {
        TextField polje = (TextField)event.getSource();
        
        TimePickerController tastatura = new TimePickerController(polje.getText());
        
        Optional<String> result = tastatura.showAndWait();
        
        if (result.isPresent()){
            
            String noviTekst = result.get();
            
            if (!noviTekst.isEmpty()) {
                timePicker.setText(noviTekst);
                return;
            } 
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Greška!");
            alert.setHeaderText("Neispravna vreme!");
            alert.setContentText("Unesite novo vreme.");
            alert.showAndWait();
        }
    
    }
    
    public void pozivanjePrikazSalePopup(ActionEvent event) {
        
        SalePopupController tastatura = new SalePopupController();
        
        Optional<String> result = tastatura.showAndWait();
        
        if (result.isPresent()){
            String noviTekst = result.get();
            
            if (!noviTekst.isEmpty()) {
                izabraniSto.setText(noviTekst);
               
            }
        }
    }
}
