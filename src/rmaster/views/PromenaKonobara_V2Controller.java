/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import rmaster.assets.FXMLDocumentController;
import rmaster.ScreenController;
import rmaster.assets.ScreenMap;
import rmaster.models.Konobar;

/**
 * FXML Controller class
 *
 * @author Arbor
 */
public class PromenaKonobara_V2Controller extends FXMLDocumentController {

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
    
    List<ToggleButton> stoloviZaPromenu = new ArrayList<>();
        
    private Konobar konobar;
    
    double sirinaSale = 1024; 
    double visinaSale = 768; 
    
    @Override
    public void initData(Object data)
    {
        
        //TODO samo njegove stolove prikazati
        
        konobar = RMaster.getUlogovaniKonobar();
        Timeline timeline = this.prikaziCasovnik(casovnik);
        timeline.play();
        this.imeKonobara.setText(getUlogovaniKonobarIme());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        
        ToggleButton noviSto = new ToggleButton();
        
        x = Double.parseDouble(stoMap.get("x"));
        x = x * RMaster.sirinaSaleNaEkranu / sirinaSale;

        y = Double.parseDouble(stoMap.get("y"));
        y = y * RMaster.visinaSaleNaEkranu / visinaSale;

        sirina = Double.parseDouble(stoMap.get("sirina"));
        sirina = sirina * RMaster.sirinaSaleNaEkranu / sirinaSale;
        visina = Double.parseDouble(stoMap.get("visina"));
        visina = visina * RMaster.visinaSaleNaEkranu / visinaSale;

        noviSto.setId(stoMap.get("broj"));

        naziv = stoMap.get("broj");

        if (stoMap.get("naziv") != null) {
            naziv = stoMap.get("naziv");
        }

        noviSto.setText(naziv);
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
        
        stoloviZaPromenu.add(noviSto);
        
        return okvir;   
    }
    
    private void setOblikStola(
            ToggleButton sto, 
            int vrstaStola, 
            double sirina)
    {
          if (vrstaStola == 2){
              return;
          }
            sto.setPrefSize(sirina, sirina);
            sto.setShape(new Circle(sirina/2));  
    }
    
    public void promeniKonobora(ActionEvent event)
    {
        
        List<String> brojeviStolova = new ArrayList<>();
        
        for (ToggleButton sto : stoloviZaPromenu) {
            if (sto.isSelected()) {
                brojeviStolova.add(sto.getId());
            }
        }
      
        if (brojeviStolova.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Greška!");
            alert.setHeaderText("Greška pri promeni");
            alert.setContentText("Niste izabrali nijedan sto za promenu.");
            alert.showAndWait(); 
            return;
        }
        
        try {
                NumerickaTastaturaController tastatura = new NumerickaTastaturaController(
                        "Unesite lozinku",
                        "Unesite lozinku",
                        true,
                        ""
                );
                    Optional<String> result = tastatura.showAndWait();

                    if (!result.isPresent()){
                        return;
                    }

                    Konobar noviKonobar = DBBroker.passwordCheck(result.get());
                    
                    if (noviKonobar == null) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Greška!");
                        alert.setHeaderText("Greška pri predstavljanju");
                        alert.setContentText("Pogrešna lozinka! Unesite lozinku konobara na koga se prebacuju stolovi.");
                        alert.showAndWait();
                        
                        return;
                    }
                    
                    if (ulogovaniKonobar.konobarID == noviKonobar.konobarID) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Greška!");
                        alert.setHeaderText("Greška pri predstavljanju");
                        alert.setContentText("Konobar je već ulogovan! Unesite lozinku konobara na koga se prebacuju stolovi.");
                        alert.showAndWait(); 
                        
                        return;
                    }
                            
                    //OVDE SE MENJAJU STOLOVI
                    promeniStolove(noviKonobar, brojeviStolova);
                
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void promeniStolove(Konobar noviKonobar, List<String> brojeviStolova)
    {   
        noviKonobar.promenaStolova(ulogovaniKonobar.konobarID + "", brojeviStolova);
        
        odjava(new ActionEvent());
    }
    
    @Override
    public void odjava(ActionEvent event)
    {            
        myController.setScreen(ScreenMap.POCETNI_EKRAN, null);
    }
    
    public void nazadNaPrikazSala(ActionEvent event)
    {            
        myController.setScreen(ScreenMap.PRIKAZ_SALA, null);
    }
}
