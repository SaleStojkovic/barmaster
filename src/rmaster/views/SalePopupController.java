/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import rmaster.RMaster;
import rmaster.assets.FXMLDocumentController;
import static rmaster.views.PrikazSalaController.HALF_HOUR;


public class SalePopupController extends Dialog {
    
    @FXML
    private Label response2;
    
    @FXML
    private TextField unetiTekst;
   
    @FXML
    private Button cancelButton;
    
    @FXML
    AnchorPane salaPrikazPane = new AnchorPane();


    FXMLDocumentController fxmlController;

    private List<Button> listaRezervacija = new ArrayList<>();
    
    private List<Map<String, String>> listSale = null;

    private List<Map<String, String>> listStolovi = null;

    public SalePopupController() {
        
        fxmlController = new FXMLDocumentController();
        
        try {
            this.initStyle(StageStyle.UNDECORATED);

        // Set the button types.
        ButtonType odustaniButtonType = new ButtonType("X", ButtonBar.ButtonData.OK_DONE);
        
        this.getDialogPane().getButtonTypes().addAll(odustaniButtonType);
        
        this.getDialogPane().getStylesheets().
                addAll(this.getClass().getResource("style/style.css").toExternalForm());
        
        this.getDialogPane().getStyleClass().add("myDialog");
        
           
        VBox vBoxGlavni = new VBox();
        
        response2 = new Label();
        
        vBoxGlavni.getChildren().add(response2);
        
        this.setHeaderText("Izaberite sto");
        
        HBox hBoxDugmiciSaSalama = new HBox();
        
            
        Button prikaziDodatneSale = new Button("« »");

        if (RMaster.saleNaziv.size() < 8) {
            prikaziDodatneSale.setDisable(true);
        }
        else {
            prikaziDodatneSale.setDisable(false);
        }

        Iterator it = RMaster.saleNaziv.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Button newButton = new Button();
            newButton.setId(pair.getKey().toString());
            String naziv = pair.getValue().toString();
            newButton.textProperty().set(naziv);
            newButton.setPrefWidth(128);
            newButton.setMinWidth(128);
            newButton.setPrefHeight(hBoxDugmiciSaSalama.getPrefHeight());
            newButton.setMinHeight(hBoxDugmiciSaSalama.getPrefHeight());

            // todo smisliti nacin da se ovo premesti u css.style
            newButton.setStyle(""
                    + "-fx-border-color: white; "
                    + "-fx-border-width: 0.75;"
                    + "-fx-border-radius: 0.2px;"
                    + "-fx-border-width: 1 1 1 1;"
                    + "-fx-background-color: -fx-tamno-crna;"
                    + "-fx-text-fill: white;"
                    + "-fx-font-family: KlavikaBold;"
                    + "-fx-font-size: 16px;"
                    + "");

            newButton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                     prikaziStolove(
                                             vBoxGlavni,
                                             Integer.parseInt(((Button)e.getSource()).getId())
                                        );
                                }
                            });
            hBoxDugmiciSaSalama.getChildren().add(newButton);
        }
            
            hBoxDugmiciSaSalama.getChildren().add(prikaziDodatneSale);
            
            vBoxGlavni.getChildren().add(hBoxDugmiciSaSalama);
                        
            Map.Entry<String,String> entry= RMaster.saleNaziv.entrySet().iterator().next();
            String key = entry.getKey();
            
            prikaziStolove(
                           vBoxGlavni,
                           Integer.parseInt(key)
                       );
            
            cancelButton = (Button)this.getDialogPane().lookupButton(odustaniButtonType);
            cancelButton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    try {
                                        cancelAction(e);
                                    } catch (Exception ex) {
                                    }
                                }
                            });


            this.getDialogPane().setContent(vBoxGlavni);
        }
        catch (Exception e) {
            System.out.println("Greska u prikazu sale! - " + e.toString());
        }
    }
    
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        vratiSaleList();
    } 
    
    public void cancelAction(ActionEvent event) {
        zatvoriOvuFormu();
    }
    
    public void zatvoriOvuFormu(){
        try {
        this.setResult(null);
        this.close();
        } catch (Exception e){
            System.out.println("Neuspelo zatvaranje forme - PrikazSalePopupController" + e);
        }
    }
    
    private void vratiSaleList() {
        try {            
            String[] imenaArgumenata = {"KonobarID"};
            String[] vrednostiArgumenata = {fxmlController.getUlogovaniKonobarID() + ""};
            listSale = fxmlController.runStoredProcedure(
                    "get_SaleOmoguceneKonobaru",
                    imenaArgumenata,
                    vrednostiArgumenata
            );
        } catch (Exception e) {
            System.out.println("Greska u pozivu SP get_SaleOmoguceneKonobaru! - " + e.toString());
        }
    }
    
    public void prikaziStolove(
            VBox vBoxGlavni,
            int salaID
    ) 
    {
        salaPrikazPane.getChildren().clear();
        
        vBoxGlavni.getChildren().remove(salaPrikazPane);
        
        salaPrikazPane.setBackground(new Background(RMaster.saleSlike.get("" + salaID)));
        try {
            String[] imenaArgumenata = {"GRAFIK_ID"};
            String[] vrednostiArgumenata = {salaID + ""};
            listStolovi = fxmlController.runStoredProcedure(
                    "get_StoloviZaPrikaz_BySala", 
                    imenaArgumenata,
                    vrednostiArgumenata 
            );
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
                
                Map<String, String> newData = new HashMap<>();
                
                newData.put("stoID", noviRed.get("id"));
                
                konobarID = noviRed.get("zauzeoKonobarID") + "";
                
                if ((!konobarID.equals("null")) && (!konobarID.equals("" + RMaster.ulogovaniKonobar.konobarID))) {
                    b.getStyleClass().add("stoZauzet");
                    //b.setDisable(true);
                } else {
                    if (konobarID.equals("null"))
                        b.getStyleClass().add("stoSlobodan");
                    else
                        b.getStyleClass().add("stoKonobarov");
                    
                    b.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override public void handle(ActionEvent e) {
                                            Button b = (Button)e.getSource();
                                            String izabraniSto = b.getText();
                                            izaberiSto(izabraniSto);
                                        } 
                                    });
                }
                if ((noviRed.get("RezervacijaDatum") != null)) {
                    String date_s = noviRed.get("RezervacijaDatum"); 
                    SimpleDateFormat dt = new SimpleDateFormat("yyyyy-MM-dd hh:mm:ss"); 
                    Date vremeRezervacije = dt.parse(date_s);

                    Date vremePolaSataPreRezervacije = new Date();
                    vremePolaSataPreRezervacije.setTime(vremeRezervacije.getTime() - HALF_HOUR);

                    Date vreme = new Date();

                    if (vreme.after(vremePolaSataPreRezervacije) && vreme.before(vremeRezervacije)) {
                        //b.getStyleClass().add("stoRezervisan");
                        listaRezervacija.add(b);
                    }
                       
                }
                
                AnchorPane.setLeftAnchor(okvir, x);
                AnchorPane.setTopAnchor(okvir, y);
                AnchorPane.setRightAnchor(okvir, RMaster.sirinaSaleNaEkranu - x - sirina);
                AnchorPane.setBottomAnchor(okvir, RMaster.visinaSaleNaEkranu - y - visina);
                salaPrikazPane.getChildren().add(okvir);
            }
            
            vBoxGlavni.getChildren().add(salaPrikazPane);

        } catch (Exception e) {
            System.out.println("Greska u prikazu stolova! - " + e.toString());
        }
    }
    
    
    public void izaberiSto(String izabraniSto) {
        this.setResult(izabraniSto);
        this.close();
    }
}
