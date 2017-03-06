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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
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
import javafx.stage.StageStyle;
import rmaster.RMaster;
import rmaster.assets.RM_Button.RM_Button;
import static rmaster.views.PrikazSalaController.HALF_HOUR;


public class SalePopupController extends Dialog {

    @FXML
    private TabPane saleTabPane = new TabPane();

    private List<Button> listaRezervacija = new ArrayList<>();
    
    public SalePopupController() {
        
        this.initStyle(StageStyle.UNDECORATED);

        // Set the button types.
        ButtonType odustaniButtonType = new ButtonType("X", ButtonBar.ButtonData.OK_DONE);
        
        this.getDialogPane().getButtonTypes().addAll(odustaniButtonType);
        
        this.getDialogPane().getStylesheets().
                addAll(this.getClass().getResource("style/style.min.css").toExternalForm());
        
        this.getDialogPane().getStyleClass().add("myDialog");
        
        List<Map<String, String>> sale = RMaster.sveSale;
        
        saleTabPane.setSide(Side.TOP);

        saleTabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        
        for(Map<String, String> salaMap : sale){
            
             new Thread() {
                 @Override
                 public void start()
                 {
                     prikaziSalu(salaMap);
                 }
             }.start();
        }
        
        this.getDialogPane().setContent(saleTabPane);
    }
    
    public void initialize(URL url, ResourceBundle rb) {

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

    
    private void prikaziSalu(Map<String, String> salaMap)
    {
        Tab newTab = new Tab();
                
        newTab.setId(salaMap.get("id"));

        newTab.setText(salaMap.get("naziv"));

        AnchorPane novaSala = new AnchorPane();

        new Thread() {
            
            @Override
            public void start()
            {
                prikaziStoloveSale(novaSala, salaMap.get("id"));
            }       
            
        }.start();
        
        novaSala.setBackground(getBackground(salaMap.get("slika")));

        newTab.setContent(novaSala);

        saleTabPane.getTabs().add(newTab);

        if (RMaster.trenutnaSalaID == Long.parseLong(salaMap.get("id"))) {        
            saleTabPane.getSelectionModel().select(newTab);
        }       
    }
    
     public void prikaziStoloveSale(AnchorPane sala, String salaId) 
    {
        List<Map<String, String>> stoloviZaPrikaz = getStoloviBySalaId(salaId);
            
        for (Map<String, String> stoMap : stoloviZaPrikaz)
        {
            StackPane okvir = this.napraviSto(stoMap);
            sala.getChildren().add(okvir);
        }
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
     
    private StackPane napraviSto(Map<String, String> stoMap)
    {
        StackPane okvir = new StackPane();
        int vrstaStola = 0;
        double x, y, sirina, visina;
        String naziv = "";
        double sirinaSale = 1024; 
        double visinaSale = 768; 
            
        RM_Button noviSto = new RM_Button();
        
        x = Double.parseDouble(stoMap.get("x"));
        x = x * RMaster.sirinaSaleNaEkranu / sirinaSale;

        y = Double.parseDouble(stoMap.get("y"));
        y = y * RMaster.visinaSaleNaEkranu / visinaSale;

        sirina = Double.parseDouble(stoMap.get("sirina"));
        sirina = sirina * RMaster.sirinaSaleNaEkranu / sirinaSale;
        visina = Double.parseDouble(stoMap.get("visina"));
        visina = visina * RMaster.visinaSaleNaEkranu / visinaSale;

        naziv = stoMap.get("broj");

        noviSto.setId(stoMap.get("id"));
        noviSto.setPodatak(stoMap.get("broj"));
        
        if (stoMap.get("naziv") != null) {
            naziv = stoMap.get("naziv");
        }

        String konobarID = stoMap.get("KONOBAR_ID") + "";

        noviSto.setText(naziv);
        vrstaStola = Integer.parseInt(stoMap.get("sto_VrstaStolaID"));

                if ((!konobarID.equals("null")) && (!konobarID.equals("" + RMaster.ulogovaniKonobar.konobarID))) {
                    noviSto.getStyleClass().add("stoZauzet");
                    
                    noviSto.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override public void handle(ActionEvent e) {
                                            RM_Button stoButton = (RM_Button)e.getSource();
                                            HashMap<String, String> stoMap = new HashMap();
                                            stoMap.put("stoId", stoButton.getId());
                                            stoMap.put("stoBroj", stoButton.getPodatak() + "");
                                            stoMap.put("stoNaziv", stoButton.getText());

                                            izaberiSto(stoMap);
                                        } 
                                    });
                    
                } else {
                    if (konobarID.equals("null")) {
                        noviSto.getStyleClass().add("stoSlobodan");
                        
                    }
                    else {
                        noviSto.getStyleClass().add("stoKonobarov");
                    }
                    
                    noviSto.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override public void handle(ActionEvent e) {
                                             RM_Button stoButton = (RM_Button)e.getSource();
                                            HashMap<String, String> stoMap = new HashMap();
                                            stoMap.put("stoId", stoButton.getId());
                                            stoMap.put("stoBroj", stoButton.getPodatak() + "");
                                            stoMap.put("stoNaziv", stoButton.getText());

                                            izaberiSto(stoMap);
                                        } 
                                    });
                    
                }
                if ((stoMap.get("RezervacijaDatum") != null)) {
                    
                    String date_s = stoMap.get("RezervacijaDatum"); 
                    SimpleDateFormat dt = new SimpleDateFormat("yyyyy-MM-dd hh:mm:ss"); 
                    try {
                        Date vremeRezervacije = dt.parse(date_s);
                        Date vremePolaSataPreRezervacije = new Date();
                        vremePolaSataPreRezervacije.setTime(vremeRezervacije.getTime() - HALF_HOUR);

                        Date vreme = new Date();

                    if (vreme.after(vremePolaSataPreRezervacije) && vreme.before(vremeRezervacije)) {
                        //b.getStyleClass().add("stoRezervisan");
                        listaRezervacija.add(noviSto);
                    }
                    
                    } catch (Exception e) {}
   
                }
        
        
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
    
    public void izaberiSto(HashMap<String, String> izabraniSto) {
        this.setResult(izabraniSto);
        this.close();
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
    
    private List<Map<String, String>> getStoloviBySalaId(String salaID)
    {
        List<Map<String, String>> listStolovi = new ArrayList<>();
        for (Map<String, String> noviSto : RMaster.sviStolovi) {
            if (noviSto.get("GRAFIK_ID").equals(salaID)) {
                listStolovi.add(noviSto);
            }
                
        }
        return listStolovi;
    }
}
