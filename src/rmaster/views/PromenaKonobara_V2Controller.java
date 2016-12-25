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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import rmaster.assets.FXMLDocumentController;

/**
 * FXML Controller class
 *
 * @author Arbor
 */
public class PromenaKonobara_V2Controller extends FXMLDocumentController {

    @FXML
    private TabPane saleTabPane;
    
    List<Map<String, String>> stoloviKonobara = new ArrayList<>();
        
    double sirinaSale = 1024; 
    double visinaSale = 768; 
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        List<Map<String, String>> sale = this.ulogovaniKonobar.saleOmoguceneKonobaru();

        for(Map<String, String> salaMap : sale){
            
            Tab newTab = new Tab();
            newTab.setId(salaMap.get("id"));
            
            newTab.setText(salaMap.get("naziv"));
            
            AnchorPane novaSala = new AnchorPane();
            
            prikaziStoloveSale(novaSala, salaMap.get("id"));
            
            novaSala.setBackground(getBackground(salaMap.get("slika")));
                    
            newTab.setContent(novaSala);
            
            saleTabPane.getTabs().add(newTab);
        }
        
    } 

    public Background getBackground(String slikaURL)
    {
        Image image = new Image(
                            getClass().getResourceAsStream("style/img/" + slikaURL),
                            1024,
                            608,
                            false,
                            true
                    );
                    
        BackgroundImage newBackgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );
             
        return new Background(newBackgroundImage);
    }
    
    public void prikaziStoloveSale(AnchorPane sala, String salaId) 
    {
        List<Map<String, String>> stoloviZaPrikaz = 
                ulogovaniKonobar.stoloviZaPrikazPromeneKonobara(salaId);
            
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

        noviSto.setId(stoMap.get("id"));

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
}
