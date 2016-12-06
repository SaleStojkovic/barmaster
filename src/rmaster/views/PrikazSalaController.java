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
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import javafx.util.Duration;
import rmaster.assets.FXMLDocumentController;
import rmaster.assets.ScreenMap;

/**
 * FXML Controller class
 *
 * @author Bosko
 */
public class PrikazSalaController extends FXMLDocumentController {
    public static final long HALF_HOUR = 30*60*1000; // in milli-seconds.
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
    
    @FXML
    private Label casovnik;
    
    @FXML
    private Label imeKonobara;
    
    @FXML
    private Button odjava;
    
    private List<Button> listaRezervacija = new ArrayList<>();
    
    
    private Timeline timeline2 = new Timeline();
    /**
     * Initializes the controller class.
     */
    
    /**
     * 
     * @param data 
     */
    @Override
    public void initData(Map<String, String> data) {
        try {
            String[] imenaArgumenata = {"KonobarID"};
            String[] vrednostiArgumenata = {ulogovaniKonobar.konobarID + ""};
            listSale = runStoredProcedure(
                    "get_SaleOmoguceneKonobaru",
                    imenaArgumenata,
                    vrednostiArgumenata
            );
        } catch (Exception e) {
            System.out.println("Greska u pozivu SP get_SaleOmoguceneKonobaru! - " + e.toString());
        }
        imeKonobara.setText(ulogovaniKonobar.imeKonobara);
        prikaziSale();
        timeline2 = this.prikaziRezervacije(this.listaRezervacija);
        timeline2.play();
        Timeline timeline = this.prikaziCasovnik(casovnik);
        timeline.play();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {


    }

    private void prikaziSale() {
        try {
            boolean prvi = true;
            if (RMaster.saleNaziv.isEmpty()) {
                for (int i = 0; i < listSale.size(); i++)
                {
                    Map<String, String> novaSala = listSale.get(i);
                    
                    RMaster.saleNaziv.put("" + novaSala.get("id"), novaSala.get("naziv"));
                    
                    Image image = new Image(
                            getClass().getResourceAsStream("style/img/" + novaSala.get("slika")),
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
                        + "-fx-border-width: 0 1 0 0;"
                        + "-fx-background-color: -fx-tamno-crna;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-family: KlavikaBold;"
                        + "-fx-font-size: 16px;"
                        + "");
                
                newButton.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        RMaster.trenutnaSalaID = Integer.parseInt(((Button)e.getSource()).getId());
                                        prikaziStolove();
                                    }
                                });
                hBoxDugmiciSaSalama.getChildren().add(newButton);
            }
            prikaziStolove();
            
            prikaziDodatneSale.setVisible(false);

            if (listSale.size() > 7) {
                prikaziDodatneSale.setVisible(true);
            }
            
        } catch (Exception e) {
            System.out.println("Greska u prikazu sale! - " + e.toString());
        }
    }
    
    public void odjava(ActionEvent event)
    {
            Map<String, String> newData = new HashMap<>();
            
            //sledeca stranica 
            prikaziFormu(
                    newData,
                    ScreenMap.POCETNI_EKRAN, 
                    true, 
                    (Node)event.getSource()
            );
    }
    
    public void prikaziRacuneZaStampu(ActionEvent event){
        Map<String, String> newData = new HashMap<>();
        prikaziFormuModalno(
                newData,
                ScreenMap.RACUNI_ZA_NAPLATU, 
                (Node)(event.getSource())
        );
    }
    
    public void promeniKonobara(ActionEvent event){
        Map<String, String> newData = new HashMap<>();

        prikaziFormuModalno(
                newData,
                ScreenMap.PROMENA_KONOBARA,
                (Node)(event.getSource())
        );
        prikaziStolove();
    }
    
    private void prikaziStolove() {

        salaPrikazPane.setBackground(new Background(RMaster.saleSlike.get("" + RMaster.trenutnaSalaID)));
        try {
            String[] imenaArgumenata = {"GRAFIK_ID"};
            String[] vrednostiArgumenata = {RMaster.trenutnaSalaID + ""};
            listStolovi = runStoredProcedure(
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
                    //b.setDisable(false);
                    b.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override public void handle(ActionEvent e) {
                                            Button b = (Button)e.getSource();
                                            RMaster.izabraniSto = b.getId();
                                              prikaziFormu(
                                                      newData,
                                                      "porudzbina", 
                                                      true, 
                                                      (Node)e.getSource());
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

        } catch (Exception e) {
            System.out.println("Greska u prikazu stolova! - " + e.toString());
        }
        
    }
    
    public Timeline prikaziRezervacije(List<Button> novaListaRezervacija) 
    {
                
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0.5), new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent actionEvent) {
                    for (int i = 0; i < novaListaRezervacija.size(); i++) {
                        Button novoDugme = novaListaRezervacija.get(i);
                        novoDugme.setStyle("-fx-background-color: -fx-boja-rezervacije1;");
                    }
                }
            }
            ),
            new KeyFrame(Duration.seconds(1),new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent actionEvent) {
                    for (int i = 0; i < novaListaRezervacija.size(); i++) {
                        Button novoDugme = novaListaRezervacija.get(i);
                        novoDugme.setStyle("-fx-background-color: -fx-boja-rezervacije2;");
                    }
                }
            }
            )
          );
          timeline.setCycleCount(Animation.INDEFINITE);
          
        return timeline;
    }
    
    public void brzaNaplata(ActionEvent event){
        Map<String, String> newData = new HashMap<>();
        RMaster.izabraniSto = "0";
        prikaziFormu(
                newData,
                ScreenMap.PORUDZBINA, 
                true, 
                (Node)event.getSource());

    }
    
    public void otvoriAdministraciju(ActionEvent event) {
        Map<String, String> newData = new HashMap<>();

        prikaziFormu(
            newData,
            ScreenMap.ADMINISTRACIJA, 
            true, 
            (Node)event.getSource()); 
    }
    
     
    public void otvoriRezervacije(ActionEvent event) {
        Map<String, String> newData = new HashMap<>();

        prikaziFormu(
            newData,
            ScreenMap.REZERVACIJE, 
            true, 
            (Node)event.getSource()); 
    }
    
    public void otvoriRastavljanjeSastavljanje(ActionEvent event) {
        Map<String, String> newData = new HashMap<>();

        prikaziFormu(
            newData,
            ScreenMap.SASTAVLJANJE_RASTAVLJANJE, 
            true, 
            (Node)event.getSource()); 
    }
}
