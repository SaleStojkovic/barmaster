/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.io.File;
import rmaster.assets.DBBroker;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import rmaster.assets.FXMLDocumentController;
import rmaster.RMaster;

/**
 * FXML Controller class
 *
 * @author Bosko
 */
public class SalaController extends FXMLDocumentController {
    private List<Map<String, String>> listStolovi = null;
    private List<Map<String, String>> listSale = null;
    
    @FXML
    private StackPane imageViewSala;
    
    @FXML
    private AnchorPane salaPrikazPane;
    
    @FXML
    private HBox hBoxDugmiciSaSalama;
    
    @FXML
    private Button prikaziDodatneSale;
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initData() {
        try {
            listSale = new DBBroker().get_SaleOmoguceneKonobaru(RMaster.ulogovaniKonobar.konobarID);
        } catch (Exception e) {
            System.out.println("Greska u pozivu SP get_SaleOmoguceneKonobaru! - " + e.toString());
        }
        prikaziSale();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // PROMENITI id Sale
    }

    // TODO promeniti da while ide po List a ne ResultSet
    private void prikaziSale() {
        try {
            boolean prvi = true;
            if (RMaster.saleNaziv.isEmpty()) {
                for (int i = 0; i < listSale.size(); i++)
                {
                    Map<String, String> novaSala = listSale.get(i);
                    
                    RMaster.saleNaziv.put("" + novaSala.get("id"), novaSala.get("naziv"));
                    
                    File file = new File("style/img/" + novaSala.get("slika"));
                    
                    Image image = new Image(
                            file.toURI().toString(),
                            RMaster.sirinaSaleNaEkranu,
                            RMaster.visinaSaleNaEkranu,
                            false,
                            true
                    );
                    
                    BackgroundImage myBI= new BackgroundImage(
                            image,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.DEFAULT,
                            BackgroundSize.DEFAULT
                    );
                    
                    RMaster.saleSlike.put("" + novaSala.get("id"), myBI);
                    
                    if (prvi && RMaster.trenutnaSalaID == 0) {
                        
                        RMaster.trenutnaSalaID = Long.parseLong(novaSala.get("id"));
                        prvi = false;
                    }
                }
            }
            if (RMaster.saleNaziv.size()<8) {
                prikaziDodatneSale.setDisable(true);
            }
            else {
                prikaziDodatneSale.setDisable(false);
            }
            
            Iterator it = RMaster.saleNaziv.entrySet().iterator();
            
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                Button b = new Button();
                b.setId(pair.getKey().toString());
                String naziv = pair.getValue().toString();
                b.textProperty().set(naziv);
                b.setPrefWidth(128);
                b.setMinWidth(128);
                b.setPrefHeight(hBoxDugmiciSaSalama.getPrefHeight());
                b.setMinHeight(hBoxDugmiciSaSalama.getPrefHeight());
                
                b.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        RMaster.trenutnaSalaID = Integer.parseInt(((Button)e.getSource()).getId());
                                        System.out.println("Sala: " + RMaster.trenutnaSalaID);
                                        prikaziStolove();
                                    }
                                });
                hBoxDugmiciSaSalama.getChildren().add(b);
                //RMaster.trenutnaSalaSlika = RMaster.saleSlike.get(pair.getKey().toString()); //rsSale.getString("slika");
            }
            prikaziStolove();
        } catch (Exception e) {
            System.out.println("Greska u prikazu sale! - " + e.toString());
        }
    }
    
    public void odjava(){
        
    }
    
    public void prikaziRacuneZaStampu(ActionEvent event){
        prikaziFormu("racuniZaNaplatu", false, (Node)(event.getSource()));
    }
    
    public void promeniKonobara(ActionEvent event){
        prikaziFormuModalno("promenaKonobara", (Node)(event.getSource()));
    }
    
    private void prikaziStolove() {

        salaPrikazPane.setBackground(new Background(RMaster.saleSlike.get("" + RMaster.trenutnaSalaID)));
        try {
            listStolovi = DBBroker.get_StoloviZaPrikaz_BySala();
        } catch (Exception e) {
            System.out.println("Greska u pozivu SP get_SaleOmoguceneKonobaru! - " + e.toString());
        }
        
        salaPrikazPane.getChildren().clear();
        
        try {
            
            int vrstaStola = 0;
            double x, y, sirina, visina;
            String konobarID;
            String naziv = "";
            double sirinaSale = 1024; 
            double visinaSale = 768; 
            
            for(int i = 0; i < listStolovi.size(); i++) {
                
                Map<String, String> noviRed = listStolovi.get(i);
                
                StackPane okvir = new StackPane();
                Button b = new Button();
                
                x = Double.parseDouble(noviRed.get("x"));
                x = x * RMaster.sirinaSaleNaEkranu / sirinaSale;
              
                y = Double.parseDouble(noviRed.get("y"));
                y = y * RMaster.visinaSaleNaEkranu / visinaSale;
               
                
                sirina = Double.parseDouble(noviRed.get("sirina"));
                sirina = sirina * RMaster.sirinaSaleNaEkranu / sirinaSale;
                visina = Double.parseDouble(noviRed.get("visina"));
                visina = visina * RMaster.visinaSaleNaEkranu / visinaSale;

                b.setId(noviRed.get("id"));
                
                naziv = noviRed.get("broj");
                
                if (noviRed.get("naziv") != null) {
                    naziv = noviRed.get("naziv");
                }
                
                b.setText(naziv);

                vrstaStola = Integer.parseInt(noviRed.get("sto_VrstaStolaID"));

                b.setBorder(Border.EMPTY);
                b.setPrefSize(sirina, visina);
                okvir.setPrefSize(sirina, visina);
                okvir.getChildren().add(b);
                
                switch (vrstaStola){
                    case 1:
                        // Sto u obliku kruga
                    case 3:
                        // Sto u obliku elipse
                        b.setPrefSize(sirina, sirina);
                        b.setShape(new Circle(sirina/2));
                        break;
                    case 2:
                        break;
                    default:
                }
                
                
                b.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        
                                          prikaziFormu("porudzbina", true, (Node)e.getSource());
                                    }
                                });
                konobarID = noviRed.get("KONOBAR_ID") + "";
                
                if (!konobarID.equals("" + RMaster.ulogovaniKonobar.konobarID)) {
                    b.getStyleClass().add("stoZauzet");
                } else {
                    b.getStyleClass().add("stoSlobodan");

                }
                
                AnchorPane.setLeftAnchor(okvir, x);
                AnchorPane.setTopAnchor(okvir, y);
                AnchorPane.setRightAnchor(okvir, RMaster.sirinaSaleNaEkranu - x - sirina);
                AnchorPane.setBottomAnchor(okvir, RMaster.visinaSaleNaEkranu - y - visina);
                salaPrikazPane.getChildren().add(okvir);
            }

        } catch (Exception e) {
            System.out.println("Greska u prikazu stolova! - " + e.toString());
        }
        
    }
}
