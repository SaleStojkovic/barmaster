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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import rmaster.assets.FXMLDocumentController;
import rmaster.ScreenController;
import rmaster.assets.ScreenMap;
import rmaster.assets.RM_Button.RM_Button;

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
    
    private Timeline timelineCasovnik;
    
    private List<Button> listaRezervacija = new ArrayList<>();
    
    private Map<String, Tab> saleSkriveniTabovi = new LinkedHashMap<String, Tab>();
    
    public Timer timerOsvezi;
    
    private Timeline timelineRezervacije;
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
//        listaRezervacija.clear();
//        prikaziStolove();
        
        
        imeKonobara.setText(getUlogovaniKonobarIme());
        
        timelineCasovnik = this.prikaziCasovnik(casovnik);
        timelineCasovnik.play();
        
        //osvezava stranicu na svakih 60s
        if (timerOsvezi == null) {
            timerOsvezi = new Timer();
            timerOsvezi.scheduleAtFixedRate(new TimerTask() { 
                @Override
                public void run() {

                    //refreshuje celu scenu
                    listaRezervacija.clear();
                    prikaziStolove();


                    if (timelineRezervacije == null) {
                        timelineRezervacije = prikaziRezervacije();
                        timelineRezervacije.play(); 
                    }
                }
                }, 0, 60000);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prikaziSale();
    }

    public void prikaziSamoSaleOmoguceneKonobaru()
    {
        for (Map.Entry<String, Tab> entry : saleSkriveniTabovi.entrySet()) {
            String key = entry.getKey();
            Tab value = entry.getValue();
            saleTabPane.getTabs().add(Integer.parseInt(key), value);
            saleSkriveniTabovi.remove(key);
        }

        if (!RMaster.firstLogin) {
            return;
        }
        
        int brojac = 0;
        
        for (Tab salaTab : saleTabPane.getTabs()) {
            String salaTabId = salaTab.getId();
            
            for (Map<String, String> salaZabranjena : RMaster.saleZabranjeneKonobaru) {
                String salaZabranjenaId = salaZabranjena.get("grafik_id");

                if (salaZabranjenaId.equals(salaTabId))
                {
                    saleSkriveniTabovi.put("" + brojac, salaTab);
                    break;
                }
            }   

            


//            prekidac = false;
//
//            for (Map<String, String> salaMap : RMaster.saleOmoguceneKonobaru) {
//                String salaMapId = salaMap.get("id");
//
//                if (salaMapId.equals(salaTabId))
//                {
//                    prekidac = true;
//                    break;
//                }
//            }   
//
//            if (!prekidac) {
//                saleSkriveniTabovi.put("" + brojac, salaTab);
//            }
            brojac++;
        }

        for (Map.Entry<String, Tab> entry : saleSkriveniTabovi.entrySet()) {
            String key = entry.getKey();
            Tab tabZaSkrivanje = entry.getValue();
            saleTabPane.getTabs().remove(tabZaSkrivanje);
        }
            
        RMaster.firstLogin = false;
    }
    
    private void prikaziSale() {
        List<Map<String, String>> sale = RMaster.sveSale;
        
        saleTabPane.setSide(Side.BOTTOM);

        for(Map<String, String> salaMap : sale){
            
            new Thread() {
                @Override
                public void start() {
                    prikaziSalu(salaMap);
                }
            }.start();
        
        }            
    }
    
    private void prikaziSalu(Map<String, String> salaMap) {
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
        
        RM_Button noviSto = new RM_Button();
        
        x = Double.parseDouble(stoMap.get("x"));
        x = x * RMaster.sirinaSaleNaEkranu / 1024;

        y = Double.parseDouble(stoMap.get("y"));
        y = y * RMaster.visinaSaleNaEkranu / 768;

        sirina = Double.parseDouble(stoMap.get("sirina"));
        sirina = sirina * RMaster.sirinaSaleNaEkranu / 1024;
        visina = Double.parseDouble(stoMap.get("visina"));
        visina = visina * RMaster.visinaSaleNaEkranu / 768;

        noviSto.setId(stoMap.get("id"));
        noviSto.setPodatak(stoMap.get("broj"));
        
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
    
    private void dodajAkcijuZaSto(RM_Button sto)
    {
        sto.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override public void handle(ActionEvent e) {
                                            RM_Button stoButton = (RM_Button)e.getSource();
                                            HashMap<String, String> stoMap = new HashMap();
                                            stoMap.put("stoId", stoButton.getId());
                                            stoMap.put("stoBroj", stoButton.getPodatak() + "");
                                            stoMap.put("stoNaziv", stoButton.getText());                                            
                                            myController.setScreen(ScreenMap.PORUDZBINA, stoMap);
                                        }
                                    });
    }
      
    
    private List<RM_Button> vratiListuStoButtonBySalaId(String salaId) {
        
         List<RM_Button> listaStolova = new ArrayList<>();
        
        for(Tab sala : saleTabPane.getTabs()) {
            AnchorPane salaAnchorPane = (AnchorPane) sala.getContent();
            
            for (Node node : salaAnchorPane.getChildren()){
                StackPane okvir = (StackPane) node;

                listaStolova.add((RM_Button) okvir.getChildren().get(0));
                
            }
        }
        return listaStolova;
    }
    
    private void setujStoStil(
            RM_Button stoButton, 
            List<Map<String, String>> listaZaPrikaz) 
    {
        for (Map<String, String> stoMap : listaZaPrikaz) {
            
            if (!stoButton.getId().equals(stoMap.get("id"))) {
                continue;
            }
            
            stoButton.getStyleClass().clear();
            stoButton.getStyleClass().add("button");
            
            String konobarID = stoMap.get("KONOBAR_ID") + "";
            
            if (konobarID.equals("null")) {
                
                stoButton.getStyleClass().add("stoSlobodan");
            }
            
            if (konobarID.equals("" + getUlogovaniKonobarID())) {
                stoButton.getStyleClass().add("stoKonobarov");
            } 
            
            if (!konobarID.equals("" + getUlogovaniKonobarID()) && !konobarID.equals("null")) {
                stoButton.getStyleClass().add("stoZauzet");
            } 
  
            if (stoMap.get("RezervacijaDatum") != null) {
                
                dodajRezervaciju(stoButton, stoMap);

            }
  
        }
    }
    
    private void dodajRezervaciju(
            RM_Button stoButton,
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
            List<RM_Button> listStoButton, 
            List<Map<String, String>> listaZaPrikaz) 
    {
        for (RM_Button stoButton : listStoButton) {
            
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

        HashMap<String, String> stoMap = new HashMap();
        stoMap.put("stoId", "0");
        stoMap.put("stoBroj", "0");
        stoMap.put("stoNaziv", "0");  
        
        myController.setScreen(ScreenMap.PORUDZBINA, stoMap);

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

//        myController.setScreen(ScreenMap.RACUNI_ZA_NAPLATU, null);

        RacuniZaNaplatuController racuniPopUp = new RacuniZaNaplatuController();
        
        Optional<String> result = racuniPopUp.showAndWait();
                
        if (result.isPresent()){
            
        }
    }
    
    public void promeniKonobara(ActionEvent event){
        
        myController.setScreen(ScreenMap.PROMENA_KONOBARA, null);
        
        listaRezervacija.clear();

        prikaziStolove();
    }
    
    @Override
    public void odjava(ActionEvent event)
    {            
        myController.setScreen(ScreenMap.POCETNI_EKRAN, null);
    }
}
