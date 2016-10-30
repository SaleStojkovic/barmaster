/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import rmaster.assets.FXMLDocumentController;
import rmaster.models.Konobar;

/**
 * FXML Controller class
 *
 * @author Bosko
 */
public class PromenaKonobaraController  extends FXMLDocumentController {

    @FXML
    public VBox stoloviZaPredaju;

    @FXML
    public VBox stoloviPredati;
    
    @FXML
    public Button promeniKonobara;
    
    private List listStoloviKojeJeKonobarZauzeo = null;

    @FXML
    private Button cancelButton;
    
    @FXML
    public ScrollPane zaPredajuScrollPane;
    
    @FXML
    public ScrollPane predatiScrollPane;
    
    /**
     * Initializes the controller class.
     */
    //@Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
    //@Override
    public void initData(Map<String, String> data) {
        
        try {
            String[] imenaArgumenata = {"KonobarID"};
            String[] vrednostiArgumenata = {ulogovaniKonobar.konobarID + ""};
            listStoloviKojeJeKonobarZauzeo =  runStoredProcedure(
                    "getStoloviKojeJeKonobarZauzeo", 
                    imenaArgumenata,
                    vrednostiArgumenata
            );
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
            
            Map<String, String> newData = new HashMap<>();
            
            try {
                TastaturaController tastatura = new TastaturaController(TastaturaVrsta.LOGOVANJE);
                Optional<String> result = tastatura.showAndWait();

                if (result.isPresent()){
                    Konobar noviKonobar = DBBroker.passwordCheck(result.get());
                    if (noviKonobar != null) {
                        if (ulogovaniKonobar.konobarID != noviKonobar.konobarID) {
                            DBBroker.promeniKonobaraZaStolove(
                                noviKonobar.konobarID,
                                stolovi);
                            zatvoriOvuFormu();
                        } else
                        {
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("Greška!");
                            alert.setHeaderText("Greška pri predstavljanju");
                            alert.setContentText("Konobar je već ulogovan! Unesite lozinku konobara na koga se prebacuju stolovi.");
                            alert.showAndWait();

                            System.out.println("Konobar je već ulogovan! - Forma PromenaKonobaraController - " + result.get());
                        }
                    } else {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Greška!");
                        alert.setHeaderText("Greška pri predstavljanju");
                        alert.setContentText("Pogrešna lozinka! Unesite lozinku konobara na koga se prebacuju stolovi.");
                        alert.showAndWait();

                        System.out.println("Nepostojeci konobar! - Forma PromenaKonobaraController - " + result.get());

                    }
                }
                
            } catch (Exception e){
                System.out.println("Greska pri otvaranju modalne forme TASTATURA! - " + e.toString());
            }

        } catch (Exception e) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Greška!");
            alert.setHeaderText("Niste izabrali nijedan sto za prebacivanje.");
            alert.setContentText("Odaberite stolove prvo ili se vratite na prikaz sala.");

            alert.showAndWait();
        }
    }
    
    private void prikaziStoloveKojeJeKonobarZauzeo() {
        try {
            
            for (int i = 0; i < listStoloviKojeJeKonobarZauzeo.size(); i++) {
                
                Map<String, String> row = (Map<String, String>)listStoloviKojeJeKonobarZauzeo.get(i);
                String id = row.get("id");
                String broj = row.get("broj");
                //String blokiran = row.get("blokiran");
                String naziv;

                Button b = new Button();
                b.setId("" + id);
                naziv = row.get("naziv");
                
                if (naziv == null) {
                    b.textProperty().set(broj);
                }
                else {
                    b.textProperty().set(naziv);
                }

                b.setPrefSize(
                        stoloviZaPredaju.getPrefWidth(), 
                        40
                );
                

                
                b.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        Button b = (Button)e.getSource();
                                        try {
                                            if (b.getParent().getId().equals("stoloviZaPredaju")) {
                                                stoloviPredati.getChildren().add(b);
                                            }
                                            else {
                                                stoloviZaPredaju.getChildren().add(b);
                                            }
                                        } catch(Exception x){
                                        }
                                    }
                                });
                stoloviZaPredaju.getChildren().add(b);
                
            }
            
            
        } catch (Exception e) {
            System.out.println("Greska u prikazu zauzetih stolova! - " + e.toString());
        }
    }

    public void cancelAction(ActionEvent event) {
        zatvoriOvuFormu();
    }
    
    public void zatvoriOvuFormu(){
        try {
        cancelButton.getScene().getWindow().hide();
        } catch (Exception e){
            System.out.println("Neuspelo zatvaranje forme - PromenaKonobaraController");
        }
    }

    public void pomeriScrollDownZaPredaju() {
        zaPredajuScrollPane.setVvalue((zaPredajuScrollPane.getVvalue() + 0.3 ) * 1);
    }
    
    public void pomeriScrollUpZaPredaju() {
        zaPredajuScrollPane.setVvalue((zaPredajuScrollPane.getVvalue() - 0.3 ) * 1);

    } 

    public void pomeriScrollDownPredati() {
        predatiScrollPane.setVvalue((predatiScrollPane.getVvalue() + 0.3 ) * 1);

    }
    
    public void pomeriScrollUpPredati() {
        predatiScrollPane.setVvalue((predatiScrollPane.getVvalue() - 0.3 ) * 1);
    }    
}
