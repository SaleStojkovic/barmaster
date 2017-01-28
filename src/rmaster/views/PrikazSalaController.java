/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
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
    
    ScreenController myController; 
     
    @Override
    public void setScreenParent(ScreenController screenParent){ 
        myController = screenParent; 
    } 
    
    @FXML
    private TabPane saleTabPane;
    
    @FXML
    private Label casovnik;
    
    @FXML
    private Label imeKonobara;
    
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
        //pokrece se prilikom logovanja
        prikaziSamoSaleOmoguceneKonobaru();
        
        //pokrece se svaki put prilikom otvaranja stranice
        prikaziStolove();
        
        listaRezervacija.clear();
        
        imeKonobara.setText(getUlogovaniKonobarIme());
        
        timeline2 = this.prikaziRezervacije();
        
        timeline2.play();
        
        Timeline timeline = this.prikaziCasovnik(casovnik);
        timeline.play();
        
        //osvezava stranicu na svakih 60s
        new Timer().scheduleAtFixedRate(new TimerTask() { 
        @Override
        public void run() {
            
            //refreshuje celu scenu
            prikaziStolove();

            listaRezervacija.clear();

            timeline2 = prikaziRezervacije();

            timeline2.play();    
            }
        }, 60000, 60000);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prikaziSale();
    }

    public void prikaziSamoSaleOmoguceneKonobaru()
    {
        if (!RMaster.firstLogin) {
            return;
        }
        
        boolean prekidac; 
        
            for (Tab salaTab : saleTabPane.getTabs()) {
                
                String salaTabId = salaTab.getId();
                
                prekidac = false;
                
                for (Map<String, String> salaMap : RMaster.saleOmoguceneKonobaru) {
                    
                    String salaMapId = salaMap.get("id");
                    
                    if (salaMapId.equals(salaTabId))
                    {
                        prekidac = true;
                    }
                }   
                
                if (!prekidac) {
                    saleTabPane.getTabs().remove(salaTab);
                }
            }
            
        RMaster.firstLogin = false;
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
   
    private void prikaziStoloveSale(AnchorPane sala, String salaId) 
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
                                            //TODO ovo ne bi trebalo ovako vec da se prosledjuje sve u porduzbinu
                                            //predlazem da se napravi sto model
                                            RMaster.izabraniStoID = b.getId();
                                            RMaster.izabraniStoBroj = Integer.parseInt(b.getBrojStola());
                                            RMaster.izabraniStoNaziv = b.getText();
                                            myController.setScreen(ScreenMap.PORUDZBINA, b.getId());
                                        }
                                    });
    }
      
    
    private List<StoButton> vratiListuStoButtonBySalaId(String salaId) {
        
         List<StoButton> listaStolova = new ArrayList<>();
        
        for(Tab sala : saleTabPane.getTabs()) {
            AnchorPane salaAnchorPane = (AnchorPane) sala.getContent();
            
            for (Node node : salaAnchorPane.getChildren()){
                StackPane okvir = (StackPane) node;

                listaStolova.add((StoButton) okvir.getChildren().get(0));
                
            }
        }
        return listaStolova;
    }
    
    private void setujStoStil(
            StoButton stoButton, 
            List<Map<String, String>> listaZaPrikaz) 
    {
        for (Map<String, String> stoMap : listaZaPrikaz) {
            
            if (!stoButton.getId().equals(stoMap.get("id"))) {
                continue;
            }
            
            String konobarID = stoMap.get("KONOBAR_ID") + "";
            
            if (konobarID.equals("null")) {
                stoButton.getStyleClass().add("stoSlobodan");
            }
            
            if (konobarID.equals("" + RMaster.ulogovaniKonobar.konobarID)) {
                stoButton.getStyleClass().add("stoKonobarov");
            } 
            
            if (!konobarID.equals("" + RMaster.ulogovaniKonobar.konobarID)) {
                stoButton.getStyleClass().add("stoZauzet");
            } 
  
            if (stoMap.get("RezervacijaDatum") != null) {
                
                dodajRezervaciju(stoButton, stoMap);

            }
            
  
        }
    }
    
    private void dodajRezervaciju(
            StoButton stoButton,
            Map<String, String> stoMap
    ) {
        try {
                String date_s = stoMap.get("RezervacijaDatum"); 
                SimpleDateFormat dt = new SimpleDateFormat("yyyyy-MM-dd hh:mm:ss"); 
                Date vremeRezervacije = dt.parse(date_s);

                Date vremePolaSataPreRezervacije = new Date();
                vremePolaSataPreRezervacije.setTime(vremeRezervacije.getTime() - HALF_HOUR);

                Date vreme = new Date();

                if (vreme.after(vremePolaSataPreRezervacije) && vreme.before(vremeRezervacije)) {
                    //b.getStyleClass().add("stoRezervisan");
                    listaRezervacija.add(stoButton);
                }
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
    }
    
    private void setujStoloveSale(
            List<StoButton> listStoButton, 
            List<Map<String, String>> listaZaPrikaz) 
    {
        for (StoButton stoButton : listStoButton) {
            
            setujStoStil(
                stoButton,
                listaZaPrikaz
            );
            
        } 
    }
    
    private void prikaziStolove() 
    {        
        for(Tab sala : saleTabPane.getTabs()) {         
            
            String salaId = sala.getId();
            
            setujStoloveSale(
                vratiListuStoButtonBySalaId(salaId),
                RMaster.getStoloveBySalaId(salaId)  
            );
            
        }
    }
    
    public Timeline prikaziRezervacije() 
    {
        List<Button> novaListaRezervacija = this.listaRezervacija;
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
    
    public void prikaziRacuneZaStampu(ActionEvent event){

        myController.setScreen(ScreenMap.RACUNI_ZA_NAPLATU, null);

    }
    
    public void promeniKonobara(ActionEvent event){
        
        myController.setScreen(ScreenMap.PROMENA_KONOBARA_V2, null);

        prikaziStolove();
    }
    
    @Override
    public void odjava(ActionEvent event)
    {            
        myController.setScreen(ScreenMap.POCETNI_EKRAN, null);
    }
}
