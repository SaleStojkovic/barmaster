/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import rmaster.assets.FXMLDocumentController;
import rmaster.assets.Stampa;
import rmaster.models.Porudzbina;

/**
 * FXML Controller class
 *
 * @author Bosko
 */
public class RacuniZaNaplatuController extends FXMLDocumentController {

    @FXML
    private TableView<Map<String, String>> tabelaSaRacunimaZaNaplatu;
    
    @FXML
    public AnchorPane tabelaSaRacunima;
    
    private List listRacuni = null;

    @FXML
    private Button cancelButton;
    
    @Override
    public void initData(Object data) {
        try {
            String[] imenaArgumenata = {"konobarID"};
            String[] vrednostiArgumenata = {ulogovaniKonobar.konobarID + ""};
            listRacuni = runStoredProcedure("getZatvoreniRacuniKonobaraTogDanaZaStampu",
                    imenaArgumenata,
                    vrednostiArgumenata);
        } catch (Exception e) {
            System.out.println("Greska u pozivu SP get_racuniKonobaraKojiNisuZatvoreni! - " + e.toString());
        }
        prikaziRacune();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void prikaziRacune() {
        tabelaSaRacunimaZaNaplatu.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabelaSaRacunimaZaNaplatu = tableHelper.popuniTabelu(
                tabelaSaRacunimaZaNaplatu,
                listRacuni
        );
        int brojRedova = listRacuni.size();
        tabelaSaRacunimaZaNaplatu.setFixedCellSize(30);
                                
        tabelaSaRacunimaZaNaplatu.setPrefHeight(brojRedova * tabelaSaRacunimaZaNaplatu.getFixedCellSize());
        
        AnchorPane.setTopAnchor(tabelaSaRacunima, 5.);
        AnchorPane.setBottomAnchor(tabelaSaRacunima, 5.);
        AnchorPane.setLeftAnchor(tabelaSaRacunima, 5.);
        AnchorPane.setRightAnchor(tabelaSaRacunima, 5.);
    }
    public void stampajRacun(){
        try {
            // TODO: GetSelected Racun (id, brojFiskalnog)
            String racunID = "";
            String brojFiskalnogIsecka = "";
            if (!this.tabelaSaRacunimaZaNaplatu.getSelectionModel().isEmpty()) {
                racunID = this.tabelaSaRacunimaZaNaplatu.getSelectionModel().getSelectedItem().get("id");
                brojFiskalnogIsecka = this.tabelaSaRacunimaZaNaplatu.getSelectionModel().getSelectedItem().get("brojFiskalnogIsecka");
            
                if (brojFiskalnogIsecka == null || brojFiskalnogIsecka.equals("")){
                    NumerickaTastaturaController tastatura = new NumerickaTastaturaController(
                            "Unesite broj fiskalnog isečka",
                            "Broj fiskalnog isečka",
                            false, 
                            null
                    );
                    Optional<String> result = tastatura.showAndWait();

                    if (result.isPresent()){
                        // TODO: Upisati u bazu broj FISKALNOG ISECKA
                        // OVDE DORADITI DA SE POKUPI PORUDZBINA KOJA SE STAMPA
                        // Porudzbina porudzbina = null;
                        // Stampa.getInstance().stampajGotovinskiRacun(porudzbina);
                                try {
                                    String[] imenaArgumenata = {"racunID","brojIsecka"};
                                    String[] vrednostiArgumenata = {racunID,result.get()};

                                    listRacuni = runStoredProcedure("setRacun_BrojFiskalnogIsecka",
                                            imenaArgumenata,
                                            vrednostiArgumenata);
                                } catch (Exception e) {
                                    System.out.println("Greska u pozivu SP get_racuniKonobaraKojiNisuZatvoreni! - " + e.toString());
                                }
                        zatvoriOvuFormu();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Greška!");
                    alert.setHeaderText("Greška pri štampanju gotovinskog računa");
                    alert.setContentText("Za račun je već odštampan gotovinski račun!");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Greška!");
                alert.setHeaderText("Greška pri štampanju gotovinskog računa");
                alert.setContentText("Račun nije izabran! Izaberite račun za koji želite da odštampate gotovinski račun.");
                alert.showAndWait();
            }
        } catch (Exception e){
            System.out.println("Greska pri otvaranju modalne forme TASTATURA! - " + e.toString());
        }
        
    }
    
    
    public void cancelAction(ActionEvent event) {
        zatvoriOvuFormu();
    }

    public void zatvoriOvuFormu(){
        try {
        cancelButton.getScene().getWindow().hide();
        } catch (Exception e){
            System.out.println("Neuspelo zatvaranje forme - ConfirmFormController");
        }
    }

}
