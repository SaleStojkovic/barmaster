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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rmaster.assets.DBBroker;
import rmaster.assets.FXMLDocumentController;

/**
 * FXML Controller class
 *
 * @author Bosko
 */
public class PromenaKonobaraController extends FXMLDocumentController {

    @FXML
    public VBox stoloviZaPredaju;

    @FXML
    public VBox stoloviPredati;
    
    @FXML
    public Button promeniKonobara;
    
    private List rsStoloviKojeJeKonobarZauzeo = null;

    @Override
    public void initData() {
        try {
            rsStoloviKojeJeKonobarZauzeo = new DBBroker().getRecordSetIzStoreProcedureZaKonobara("getStoloviKojeJeKonobarZauzeo", "KonobarID");
        } catch (Exception e) {
            System.out.println("Greska u pozivu SP get_SaleOmoguceneKonobaru! - " + e.toString());
        }
        prikaziStoloveKojeJeKonobarZauzeo();
    }

    public void btnPrebaciSve_Action(ActionEvent event) {
        try {
            List<Button> buttons= getKontroleOdredjenogTipa(stoloviZaPredaju, Button.class);
            for (Button button : buttons) {
                stoloviPredati.getChildren().add(button);
            }
        } catch (Exception e) {
            System.out.println("Greska u prebacivanju stolova na drugog konobara! - " + e.toString());
        }
    }
    
    public void btnVratiSve_Action(ActionEvent event) {
        try {
            List<Button> buttons= getKontroleOdredjenogTipa(stoloviPredati, Button.class);
            for (Button button : buttons) {
                stoloviZaPredaju.getChildren().add(button);
            }
        } catch (Exception e) {
            System.out.println("Greska u prebacivanju stolova na drugog konobara! - " + e.toString());
        }
    }

    @FXML
    public void btnPromeniKonobara_Action(ActionEvent event) {
        try {
            String stolovi = "";
            List<Button> buttons= getKontroleOdredjenogTipa(stoloviPredati, Button.class);
            for (Button button : buttons) {
                stolovi += button.getId() + ",";
            }
            stolovi = stolovi.substring(0, stolovi.length()-1);
            
//            PROMENITI STOLOVE ZA KONOBARA
//            LogIn forma da se potvrdi novi konobar
//            Pozvati PROCEDURE `promeniKonobaraZaStolove`(
//                                          in StariKonobarID long, 
//                                          in NoviKonobarID long, 
//                                          in stolovi VARCHAR(1000), 
//                                          out brojPromenjenihStolova int)
            int brojPromStolova = new DBBroker().promeniKonobaraZaStolove(3, stolovi);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Broj prebačenih stolova: " + brojPromStolova, ButtonType.OK);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                
            }
            
            Stage stage = (Stage) promeniKonobara.getScene().getWindow();
    // do what you have to do
            stage.close();
        } catch (Exception e) {
            System.out.println("Greska u prebacivanju stolova na drugog konobara! - " + e.toString());
        }
    }

    private void prikaziStoloveKojeJeKonobarZauzeo() {
        try {
            int i = 0;
            while (!rsStoloviKojeJeKonobarZauzeo.isEmpty()) {
                Map<String, String> row = (Map<String, String>)rsStoloviKojeJeKonobarZauzeo.get(i);
                String id = row.get("id");
                String broj = row.get("broj");
                //String blokiran = row.get("blokiran");
                String naziv;

                Button b = new Button();
                b.setId("" + id);
                naziv = row.get("naziv");
                if (naziv == null)
                    b.textProperty().set(broj);
                else
                    b.textProperty().set(naziv);

                b.setPrefWidth(stoloviZaPredaju.getPrefWidth());
                b.setMinWidth(stoloviZaPredaju.getPrefWidth());
                b.setPrefHeight(40);
                b.setMinHeight(40);
                
                b.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        Button b = (Button)e.getSource();
                                        try {
                                            if (b.getParent().getId().equals("stoloviZaPredaju"))
                                                stoloviPredati.getChildren().add(b);
                                            else
                                                stoloviZaPredaju.getChildren().add(b);
                                        } catch(Exception x){
                                        }
                                    }
                                });
                stoloviZaPredaju.getChildren().add(b);
                i++;
            }
        } catch (Exception e) {
            System.out.println("Greska u prikazu zauzetih stolova! - " + e.toString());
        }
    }

/**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
