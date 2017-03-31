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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import rmaster.ScreenController;
import rmaster.assets.ScreenMap;
import rmaster.assets.RM_Button.RM_Button;
import rmaster.models.SettingsBaza;

/**
 * FXML Controller class
 *
 * @author Bosko
 */
public class PrikazSalaController extends PrikazSalaParentController {
    public static long VREME_PRIKAZIVANJA_PRE_REZERVACIJE; // in milli-seconds.
    
    //ScreenController myController; 
     
    @Override
    public void setScreenParent(ScreenController screenParent){ 
        myController = screenParent; 
    } 
    
    
    @FXML
    private Label casovnik;
    
    @FXML
    private Label imeKonobara;
    
    private final List<Button> listaRezervacija = new ArrayList<>();
    
    public Timer timerOsvezi;
    
    private Timeline timelineRezervacije;
    
    @FXML
    private ImageView barMasterLogo;
    
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
        listaRezervacija.clear();
        prikaziStolove();
                        
        imeKonobara.setText(getUlogovaniKonobarIme());
        
        RMaster.setClockLabelForUpdate(casovnik);
        
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
        
        barMasterLogo.setImage(RMaster.logo);
    }
    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        VREME_PRIKAZIVANJA_PRE_REZERVACIJE = Long.parseLong(SettingsBaza.getValue("trajanje.rezervacije")) * 60 * 1000;
    }

    @Override
    protected void setOblikStola(
            ButtonBase sto, 
            int vrstaStola, 
            double sirina)
    {
          if (vrstaStola == 2){
              return;
          }
            sto.setPrefSize(sirina, sirina);
            sto.setShape(new Circle(sirina/2));  
    }    
    
    @Override
    protected void dodajAkcijuZaSto(RM_Button sto)
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
            if (sala.getId().equals(salaId)) {
                AnchorPane salaAnchorPane = (AnchorPane) sala.getContent();

                for (Node node : salaAnchorPane.getChildren()){
                    StackPane okvir = (StackPane) node;

                    listaStolova.add((RM_Button) okvir.getChildren().get(0));

                }
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
                dodajAkcijuZaSto(stoButton);
            }
            
            if (konobarID.equals("" + getUlogovaniKonobarID())) {
                stoButton.getStyleClass().add("stoKonobarov");
                dodajAkcijuZaSto(stoButton);
            } 
            
            if (!konobarID.equals("" + getUlogovaniKonobarID()) && !konobarID.equals("null")) {
                stoButton.getStyleClass().add("stoZauzet");
                ukloniAkcijuZaSto(stoButton);
            } 
  
            if (stoMap.get("RezervacijaDatum") != null) {
                dodajRezervaciju(stoButton, stoMap);
            }
            
            break;
        }
    }
    
    private void dodajRezervaciju(
            RM_Button stoButton,
            Map<String, String> stoMap
    ) {
        try {
                String date_s = stoMap.get("RezervacijaDatum");
                String time_s = stoMap.get("RezervacijaVreme");
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
                Date vremeRezervacije = dt.parse(date_s + " " + time_s);
                
                Date vremePolaSataPreRezervacije = new Date();
                vremePolaSataPreRezervacije.setTime(vremeRezervacije.getTime() - VREME_PRIKAZIVANJA_PRE_REZERVACIJE);

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

//        System.out.println("Ucitavanja - prikaziStolove - kraj: " + System.nanoTime());

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
