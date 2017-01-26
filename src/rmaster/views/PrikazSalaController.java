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
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import rmaster.assets.FXMLDocumentController;
import rmaster.ScreenController;
import rmaster.assets.ScreenMap;
import rmaster.assets.items.StoButton;

/**
 * FXML Controller class
 *
 * @author Bosko
 */
public class PrikazSalaController extends FXMLDocumentController {
    public static final long HALF_HOUR = 30*60*1000; // in milli-seconds.
    private List<Map<String, String>> listStolovi = null;
    
        ScreenController myController; 
     
    @Override
    public void setScreenParent(ScreenController screenParent){ 
        myController = screenParent; 
    } 
    
    @FXML
    private TabPane saleTabPane;
    
//    @FXML
//    private AnchorPane salaPrikazPane;
    
//    @FXML
//    private HBox hBoxDugmiciSaSalama;
    
    
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
    public void initData(Object data) {
        //todo ovde da se disableuju sale koje nisu dostupne konobaru

        imeKonobara.setText(getUlogovaniKonobarIme());
        timeline2 = this.prikaziRezervacije(this.listaRezervacija);
        timeline2.play();
        Timeline timeline = this.prikaziCasovnik(casovnik);
        timeline.play();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prikaziSale();
    }

    private void prikaziSale() {
        List<Map<String, String>> sale = RMaster.sveSale;
        
        saleTabPane.setSide(Side.BOTTOM);

        for(Map<String, String> salaMap : sale){
            
            Tab newTab = new Tab();
            newTab.setId(salaMap.get("id"));
            
            newTab.setText(salaMap.get("naziv"));
            
            AnchorPane novaSala = new AnchorPane();
            
            prikaziStoloveSale(novaSala, salaMap.get("id"));
            
            novaSala.setBackground(getBackground(salaMap.get("slika")));
                    
            newTab.setContent(novaSala);
            
            saleTabPane.getTabs().add(newTab);
            
            if (RMaster.trenutnaSalaID == Long.parseLong(salaMap.get("id"))) {        
                saleTabPane.getSelectionModel().select(newTab);
            }        
        
        }            
    }
   
    public void prikaziStoloveSale(AnchorPane sala, String salaId) 
    {
        List<Map<String, String>> stoloviZaPrikaz = RMaster.getStoloveBySalaId(salaId);
            
        for (Map<String, String> stoMap : stoloviZaPrikaz)
        {
            StackPane okvir = this.napraviSto(stoMap);
            sala.getChildren().add(okvir);
        }
    }
    
    private StackPane napraviSto(Map<String, String> stoMap)
    {
        StackPane okvir = new StackPane();
        
        int vrstaStola = 0;
        double x, y, sirina, visina;
        String naziv = "";
        
        StoButton noviSto = new StoButton();
        
        x = Double.parseDouble(stoMap.get("x"));
        x = x * RMaster.sirinaSaleNaEkranu / 1024;

        y = Double.parseDouble(stoMap.get("y"));
        y = y * RMaster.visinaSaleNaEkranu / 768;

        sirina = Double.parseDouble(stoMap.get("sirina"));
        sirina = sirina * RMaster.sirinaSaleNaEkranu / 1024;
        visina = Double.parseDouble(stoMap.get("visina"));
        visina = visina * RMaster.visinaSaleNaEkranu / 768;

        noviSto.setId(stoMap.get("id"));
        noviSto.setBrojStola(stoMap.get("broj"));
        
        naziv = stoMap.get("broj");

        if (stoMap.get("naziv") != null) {
            naziv = stoMap.get("naziv");
        }

        noviSto.setText(naziv);
        
        dodajAkcijuZaSto(noviSto);
        
        vrstaStola = Integer.parseInt(stoMap.get("sto_VrstaStolaID"));

        noviSto.setBorder(Border.EMPTY);
        noviSto.setPrefSize(sirina, visina);
        noviSto.setMaxSize(sirina, visina);
        noviSto.setMinSize(sirina, visina);
        okvir.setPrefSize(sirina, visina);
        okvir.getChildren().add(noviSto);

        this.setOblikStola(noviSto, vrstaStola, sirina);

        AnchorPane.setLeftAnchor(okvir, x);
        AnchorPane.setTopAnchor(okvir, y);
        AnchorPane.setRightAnchor(okvir, RMaster.sirinaSaleNaEkranu - x - sirina);
        AnchorPane.setBottomAnchor(okvir, RMaster.visinaSaleNaEkranu - y - visina);
                
        return okvir;   
    }
    
    private void setOblikStola(
            Button sto, 
            int vrstaStola, 
            double sirina)
    {
          if (vrstaStola == 2){
              return;
          }
            sto.setPrefSize(sirina, sirina);
            sto.setShape(new Circle(sirina/2));  
    }    
    
    private void dodajAkcijuZaSto(StoButton sto)
    {
        sto.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override public void handle(ActionEvent e) {
                                            StoButton b = (StoButton)e.getSource();
                                            RMaster.izabraniStoID = b.getId();
                                            RMaster.izabraniStoBroj = Integer.parseInt(b.getBrojStola());
                                            RMaster.izabraniStoNaziv = b.getText();
                                            myController.setScreen(ScreenMap.PORUDZBINA, null);
                                        }
                                    });
    }
        
    private void prikaziStolove() {

//        salaPrikazPane.setBackground(new Background(RMaster.saleSlike.get("" + RMaster.trenutnaSalaID)));
        try {
            String[] imenaArgumenata = {"GRAFIK_ID"};
            String[] vrednostiArgumenata = {RMaster.trenutnaSalaID + ""};
            listStolovi = runStoredProcedure("get_StoloviZaPrikaz_BySala", 
                    imenaArgumenata,
                    vrednostiArgumenata);
        } catch (Exception e) {
            System.out.println("Greska u pozivu SP get_SaleOmoguceneKonobaru! - " + e.toString());
        }
        
//        salaPrikazPane.getChildren().clear();
        
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
                StoButton b = new StoButton();
                
                x = Double.parseDouble(noviRed.get("x"));
                x = x * RMaster.sirinaSaleNaEkranu / sirinaSale;
              
                y = Double.parseDouble(noviRed.get("y"));
                y = y * RMaster.visinaSaleNaEkranu / visinaSale;
               
                
                sirina = Double.parseDouble(noviRed.get("sirina"));
                sirina = sirina * RMaster.sirinaSaleNaEkranu / sirinaSale;
                visina = Double.parseDouble(noviRed.get("visina"));
                visina = visina * RMaster.visinaSaleNaEkranu / visinaSale;

                b.setId(noviRed.get("id"));
                b.setBrojStola(noviRed.get("broj"));
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
                                            StoButton b = (StoButton)e.getSource();
                                            RMaster.izabraniStoID = b.getId();
                                            RMaster.izabraniStoBroj = Integer.parseInt(b.getBrojStola());
                                            RMaster.izabraniStoNaziv = b.getText();
                                            myController.setScreen(ScreenMap.PORUDZBINA, null);
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
//                salaPrikazPane.getChildren().add(okvir);
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

        RMaster.izabraniStoID = "0";
        
        myController.setScreen(ScreenMap.PORUDZBINA, null);

    }
    
    public void otvoriAdministraciju(ActionEvent event) {
        myController.setScreen(ScreenMap.ADMINISTRACIJA, null);
    }
    
    public void otvoriRezervacije(ActionEvent event) {
        myController.setScreen(ScreenMap.REZERVACIJE, null);
    }
    
    public void otvoriRastavljanjeSastavljanje(ActionEvent event) {
        myController.setScreen(ScreenMap.SASTAVLJANJE_RASTAVLJANJE, null);
    }
    
    public void odjava(ActionEvent event)
    {            
        myController.setScreen(ScreenMap.POCETNI_EKRAN, null);
    }
    
    public void prikaziRacuneZaStampu(ActionEvent event){

        myController.setScreen(ScreenMap.RACUNI_ZA_NAPLATU, null);

    }
    
    public void promeniKonobara(ActionEvent event){
        
        myController.setScreen(ScreenMap.PROMENA_KONOBARA_V2, null);

        prikaziStolove();
    }
}
