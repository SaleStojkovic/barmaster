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
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import rmaster.assets.DBBroker;
import rmaster.assets.FXMLDocumentController;

/**
 * FXML Controller class
 *
 * @author Bosko
 */
public class RacuniZaNaplatuController extends FXMLDocumentController {

    @FXML
    private TableView<Map<String, String>> tabela;
    
    @FXML
    public AnchorPane tabelaSaRacunima;
    
    private List rsRacuni = null;
    
    public void initData() {
        try {
            rsRacuni = new DBBroker().getRecordSetIzStoreProcedureZaKonobara("get_racuniKonobaraKojiNisuZatvoreni","konobarID");
            //rsRacuni = new DBBroker().get_SaleOmoguceneKonobaru(RMaster.ulogovaniKonobarID);
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
        tabela = this.popuniTabelu(
                tabela,
                rsRacuni
        );
        AnchorPane.setTopAnchor(tabelaSaRacunima, 5.);
        AnchorPane.setBottomAnchor(tabelaSaRacunima, 5.);
        AnchorPane.setLeftAnchor(tabelaSaRacunima, 5.);
        AnchorPane.setRightAnchor(tabelaSaRacunima, 5.);

        //tabelaSaRacunima.setEditable(false);
        //tabelaSaRacunima.setItems(t.getDataZaTableView());
    }
    public void stampajRacun(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Štampanje fiskalnog računa");
        dialog.setHeaderText("Look, a Text Input Dialog");
        dialog.setContentText("Unesite broj fiskalnog isečka za račun ID =" + tabelaSaRacunima);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && result.get().length()!=0){
            // TODO - Upisati ovo uz racun u tabelu
            System.out.println("Upisati ovo u polje u tabeli RACUN: " + result.get());
        }

    }
    
}
