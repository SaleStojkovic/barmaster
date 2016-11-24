/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
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
    private TextField brOsoba;
    
    @FXML 
    private DatePicker datumPicker;
    
    @FXML
    private TextField timePicker;
    
    @FXML
    private TextField izabraniSto;
    
    @FXML
    private TextField idRezervacije;
    
    @FXML
    private TextField telefon;
    
    @FXML
    private TextArea napomena;
    
    public TableView<Map<String, String>> tabelaRezervacija = new TableView<>();
            
    public int[] sirinaKolonaTabele = {140, 100, 100, 100, 100, 100, 242, 0};
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Timeline timeline = this.prikaziCasovnik(casovnik);
        timeline.play();
        
        List<Map<String, String>> listaZaPrikaz = getRezervacije();
        
        tableHelper.izbrisiSveIzTabele(tabelaRezervacija);
        
        tabelaRezervacija = tableHelper.formatirajTabelu(
                tabelaRezervacija, 
                listaZaPrikaz,
                sirinaKolonaTabele
        );
        

        scrollPaneRezervacije.setContent(tabelaRezervacija);
        imeKonobara.setText(ulogovaniKonobar.imeKonobara);
        
        datumPicker.setPromptText("Izaberite datum");
        ime.setText("");
        datumPicker.setValue(null);
        napomena.setText("");
        telefon.setText("");
        brOsoba.setText("");
        izabraniSto.setText("");
        ime.setPromptText("Unesite ime");
        timePicker.setText("00:00");
        
        idRezervacije.setVisible(false);
        
        datumPicker.setConverter(new StringConverter<LocalDate>()
            {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate == null) {
                    return "";
                }
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString == null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString, dateTimeFormatter);
            }
        }); 
        
        tabelaRezervacija.getSelectionModel().select(tabelaRezervacija.getItems().size()-1);        
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
    
    
    public List<Map<String, String>> getRezervacije() {
        List<HashMap<String,String>> listaRezervacija = DBBroker.vratiSveIzTabele("rezervacija"); 
        List<Map<String, String>> listaZaPrikaz = new ArrayList<>(); 
        
        for(HashMap<String, String> rezervacijaMapa : listaRezervacija) {
            Rezervacija novaRezervacija = new Rezervacija();
            novaRezervacija.makeFromHashMap(rezervacijaMapa);
            
            listaZaPrikaz.add(novaRezervacija.makeMapForTableOutput());
        }
        
        return listaZaPrikaz;
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
    
    public void pozivanjePrikazSalePopup(MouseEvent event) {
        
        izabraniSto.setText("");

        SalePopupController tastatura = new SalePopupController();
        
        Optional<String> result = tastatura.showAndWait();
        
        if (result.isPresent()){
            String noviTekst = result.get();
            
            if (!noviTekst.isEmpty()) {
                izabraniSto.setText(noviTekst);
               
            }
        }
    }
    
    public void pozivanjeNumerickeTastature(MouseEvent event) {
        
        TextField polje = (TextField)event.getSource();
        
        String prethodniTekst = polje.getText();
        
        NumerickaTastaturaController tastatura = new NumerickaTastaturaController(
                TastaturaVrsta.UNOS_IZNOSA, prethodniTekst);
        
        Optional<String> result = tastatura.showAndWait();
                
        if (result.isPresent()){
            String noviTekst = result.get();
            
            if (!noviTekst.isEmpty()) {
                polje.setText(noviTekst);
            }        
        }
    }
    
    public void sacuvajRezervaciju(ActionEvent event) {
        
        Rezervacija novaRezervacija = new Rezervacija();
        novaRezervacija.ime = ime.getText();
        novaRezervacija.datum = "" + datumPicker.getValue();
        novaRezervacija.brOsoba = brOsoba.getText();
        novaRezervacija.brStola = izabraniSto.getText();
        novaRezervacija.tel = telefon.getText();
        novaRezervacija.napomena = napomena.getText();
        novaRezervacija.vreme = timePicker.getText();
        
        if (idRezervacije.getText().isEmpty()) {
            novaRezervacija.save(true);
            
            this.initialize(null, null);
            return;
        }
        
        novaRezervacija.idRezervacije = idRezervacije.getText();
        novaRezervacija.saveChanges(true);
        
        tabelaRezervacija.getColumns().clear();
        
        this.initialize(null, null);
    }
    
    public void promeniRezervaciju(ActionEvent event) {
        Map<String, String> odabranaRezervacija =  tabelaRezervacija.getSelectionModel().getSelectedItem();
        
        ime.setText(odabranaRezervacija.get(Rezervacija.IME));
        brOsoba.setText(odabranaRezervacija.get(Rezervacija.BROJ_OSOBA));
        izabraniSto.setText(odabranaRezervacija.get(Rezervacija.BROJ_STOLA));
        napomena.setText(odabranaRezervacija.get(Rezervacija.NAPOMENA));
        telefon.setText(odabranaRezervacija.get(Rezervacija.TELEFON));
        datumPicker.setValue(datumPicker.getConverter().fromString(odabranaRezervacija.get(Rezervacija.DATUM)));
        timePicker.setText(odabranaRezervacija.get(Rezervacija.VREME));
        
        idRezervacije.setText(odabranaRezervacija.get(Rezervacija.PRIMARY_KEY));
    }
    
    public void izbrisiRezervaciju(ActionEvent event) {
        
        ButtonType yes = new ButtonType("Da", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("Ne", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(AlertType.WARNING,
                "Da li ste sigurni da želite da obrišete ovaj zapis? "
                + "Izabrani zapis biće trajno obrisan!",
                yes,
                no);
        
        alert.getDialogPane().getStylesheets().
                addAll(this.getClass().getResource("style/style.css").toExternalForm());
        
        alert.getDialogPane().getStyleClass().add("myDialog");
        alert.initStyle(StageStyle.UNDECORATED);

        alert.setHeaderText("Upozorenje!");
        alert.setTitle("Brisanje zapisa");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == no) {
            return;
        }
        
        Map<String, String> odabranaRezervacija =  tabelaRezervacija.getSelectionModel().getSelectedItem();

        String izabranaRezervacijaId = odabranaRezervacija.get(Rezervacija.PRIMARY_KEY);
        
        Rezervacija izabranaRezervacija = new Rezervacija();
        izabranaRezervacija.idRezervacije = izabranaRezervacijaId;
        
        izabranaRezervacija.delete(true);
        
        this.initialize(null, null);
    }
}
