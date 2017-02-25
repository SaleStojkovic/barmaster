/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import rmaster.assets.FXMLDocumentController;
import rmaster.ScreenController;
import rmaster.assets.RM_Button.RM_Button;
import rmaster.assets.ScreenMap;
import rmaster.assets.Utils;
import rmaster.models.Gost;
import rmaster.models.Porudzbina;
import rmaster.models.StavkaTure;
import rmaster.models.Sto;
import rmaster.models.Tura;

/**
 * FXML Controller class
 *
 * @author Arbor
 */
public class SastavljanjeRastavljanjeController extends FXMLDocumentController {

        ScreenController myController; 
     
    @Override
    public void setScreenParent(ScreenController screenParent){ 
        myController = screenParent; 
    } 
    
    @FXML
    private Label imeKonobara;
    
    @FXML 
    private Label casovnik;
    
    @FXML
    private Button izaberiStoA;
   
    @FXML
    private Button izaberiStoB;
    
    @FXML
    private ScrollPane scrollPaneA;
    
    @FXML
    private ScrollPane scrollPaneB;
    
    @FXML
    private VBox contentA;
    
    @FXML
    private VBox contentB;
    
    @FXML
    private HBox gostiStoA = new HBox();
    
    @FXML
    private HBox gostiStoB = new HBox();
    
    private ToggleGroup gostiGrupaA = new ToggleGroup();
    
    private ToggleGroup gostiGrupaB = new ToggleGroup();
    
    private List<Porudzbina> porudzbineStolaA = new ArrayList<>();
    
    private List<Porudzbina> porudzbineStolaB = new ArrayList<>();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scrollPaneA.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneB.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneA.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneB.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    }    
    
    @Override
    public void initData(Object data)
    {
        imeKonobara.setText(RMaster.ulogovaniKonobar.imeKonobara);
        
        Timeline timeline = this.prikaziCasovnik(casovnik);
        timeline.play();
    }
    
    public void nazadNaPrikazSale(ActionEvent event) 
    {            
        //sledeca stranica 
        myController.setScreen(ScreenMap.PRIKAZ_SALA, null);
    }
    
    public void pozivanjePrikazSalePopup(ActionEvent event) {

        SalePopupController tastatura = new SalePopupController();
        
        Optional<HashMap<String, String>> result = tastatura.showAndWait();
        
        if (!result.isPresent()){
            return;
        }
        
        if (!(result.get() instanceof HashMap)) {
            return;
        }
        
        HashMap<String, String> izabraniStoMap = result.get();

        Button pritisnutoDugme = (Button)event.getSource();
        pritisnutoDugme.setText(izabraniStoMap.get("stoNaziv"));
        
        //zavisno koje dugme je stisnuto popuniti liste gostiju za taj sto
        if (pritisnutoDugme.getId().equals("izaberiStoA")) {
            
            gostiStoA.getChildren().clear();
            
            popuniGosteA(izabraniStoMap);
            
        }
        
        if (pritisnutoDugme.getId().equals("izaberiStoB")) {
            
            gostiStoB.getChildren().clear();
            
            popuniGosteB(izabraniStoMap);
        }
    }
    
    @Override
    public void odjava(ActionEvent event)
    {            
        myController.setScreen(ScreenMap.POCETNI_EKRAN, null);
    }
    
    public void popuniGosteA(HashMap<String, String> sto) 
    {
        Sto izabraniSto = new Sto(sto);
        
        List racuniStola = izabraniSto.getPorudzbineStola();
                
        if (racuniStola.isEmpty())
        {
            //sta se desava ako nema nijednog gosta
            return;
        }
        
        List<Node> lista = new ArrayList<>();
        
        for (Object racun : racuniStola) {
            
            Map<String, String> red = (Map<String, String>) racun;

            String brojNovogGosta = red.get("gost");

            Porudzbina porudzbinaGosta = new Porudzbina(
                    new Gost(brojNovogGosta), 
                    red.get("id"),
                    izabraniSto.stoId,
                    izabraniSto.stoBroj
            );
            
            porudzbineStolaA.add(porudzbinaGosta);

            RadioButton noviGostButton = new RadioButton(brojNovogGosta);

            noviGostButton.setToggleGroup(gostiGrupaA);
            
            lista.add(prikaziPorudzbinuTaskSetButtonActionA(noviGostButton, brojNovogGosta));
        } 
        
        ObservableList<Node> listaDugmica = FXCollections.observableArrayList(lista);            
                
        gostiStoA.getChildren().addAll(listaDugmica);
    }
    
    public void popuniGosteB(HashMap<String, String> sto) 
    {
        Sto izabraniSto = new Sto(sto);
        
        List racuniStola = izabraniSto.getPorudzbineStola();
                
        if (racuniStola.isEmpty())
        {
            //sta se desava ako nema nijednog gosta
            return;
        }
        
        List<Node> lista = new ArrayList<>();
        
        for (Object racun : racuniStola) {
            
            Map<String, String> red = (Map<String, String>) racun;

            String brojNovogGosta = red.get("gost");

            Porudzbina porudzbinaTrenutna = new Porudzbina(
                    new Gost(brojNovogGosta), 
                    red.get("id"),
                    izabraniSto.stoId,
                    izabraniSto.stoBroj
            );

            porudzbineStolaB.add(porudzbinaTrenutna);
            
            RadioButton noviGostButton = new RadioButton(brojNovogGosta);

            noviGostButton.setToggleGroup(gostiGrupaB);
            
            lista.add(prikaziPorudzbinuTaskSetButtonActionB(noviGostButton, brojNovogGosta));
        } 
        
        ObservableList<Node> listaDugmica = FXCollections.observableArrayList(lista);            
                
        gostiStoB.getChildren().addAll(listaDugmica);
    }
    
     private RadioButton prikaziPorudzbinuTaskSetButtonActionA(
            RadioButton noviGostButton, 
            String brojNovogGosta
     )
    {
        noviGostButton.getStyleClass().remove("radio-button");
        noviGostButton.getStyleClass().add("toggle-button");

        noviGostButton.setId(brojNovogGosta);

        noviGostButton.setPrefSize(57, 57);

        noviGostButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {
                                String brojGosta = ((RadioButton)e.getSource()).getId();

                                for (Porudzbina porudzbina : porudzbineStolaA) {
                                    String gostId = porudzbina.getGost().getGostID() + "";
                                    if (gostId.equals(brojGosta)) {
                                        prikaziPorudzbinu(contentA, porudzbina);
                                        break;
                                    }
                                }

                            }      
                        });

        return noviGostButton;
    }
     
    private RadioButton prikaziPorudzbinuTaskSetButtonActionB(
            RadioButton noviGostButton, 
            String brojNovogGosta
    )
    {        
        noviGostButton.getStyleClass().remove("radio-button");
        noviGostButton.getStyleClass().add("toggle-button");

        noviGostButton.setId(brojNovogGosta);

        noviGostButton.setPrefSize(57, 57);

        noviGostButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {

                                String brojGosta = ((RadioButton)e.getSource()).getId();

                                for (Porudzbina porudzbina : porudzbineStolaB) {
                                    if (porudzbina.getGost().getGostID() == Long.parseLong(brojGosta)) {
                                        prikaziPorudzbinu(contentB, porudzbina);
                                        break;
                                    }
                                }
                            }
                        });

        return noviGostButton;
    }
    
   private void prikaziPorudzbinu(VBox content, Porudzbina izabranaPorudzbina)
   {
       content.getChildren().clear();
       
       List<Tura> listaTura = izabranaPorudzbina.getTure();
              
       List<RM_Button> listaDugmica = new ArrayList<>();
       
       for (Tura novaTura : listaTura) {
                       
            RM_Button prebaciCeluTuru = new RM_Button();
            
            dodajAkcijuZaPrebaciCeluTuru(prebaciCeluTuru, novaTura);
            
            for(StavkaTure novaStavka : novaTura.listStavkeTure)
            {
                RM_Button stavka = new RM_Button();
                
                dodajAkcijuZaPrebaciStavku(stavka, novaStavka);
                
                listaDugmica.add(stavka);
            } 
            
            listaDugmica.add(prebaciCeluTuru);
       }
              
       content.getChildren().addAll(listaDugmica);
   }
   
   private void dodajAkcijuZaPrebaciCeluTuru(RM_Button prebaciCeluTuru, Tura novaTura)
   {
       //TODO
       prebaciCeluTuru.setPrefSize(432, 50);
            try {
                String period = Utils.getDateDiff(novaTura.getVremeTure(), new Date(), TimeUnit.MINUTES);
                prebaciCeluTuru.setText("Prebaci celu turu (" + period + ")");
            } catch (Exception e) {
                prebaciCeluTuru.setText("Prebaci celu turu");
            }
            prebaciCeluTuru.setPodatak("" + novaTura.getTuraID());
            
            prebaciCeluTuru.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {
                                String turaId = ((RM_Button)e.getSource()).getPodatak() + "";
                               //TODO
                            }
                        }); 
            
   }
   
   
   private void dodajAkcijuZaPrebaciStavku(RM_Button dugmeStavka, StavkaTure novaStavka) 
   {
      
        dugmeStavka.setPrefWidth(432);
        
        dugmeStavka.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    
        HBox dugmeContent = new HBox();
        dugmeContent.setMinHeight(30);

        VBox naziv = new VBox();
        naziv.setAlignment(Pos.CENTER_LEFT);
        
        
        VBox kolicina = new VBox();
        kolicina.setAlignment(Pos.CENTER);
        
        kolicina.setStyle(
                "-fx-border-style: solid inside;\n" 
                + "-fx-border-width: 0 0.4 0 0.4;\n" 
                + "-fx-border-color: white;"
        );

        VBox cena = new VBox();
        cena.setAlignment(Pos.CENTER_RIGHT);
        
        naziv.setPrefWidth(310);
        kolicina.setPrefWidth(41);
        cena.setPrefWidth(81);
        
        Label dugmeText = new Label();
        dugmeText.wrapTextProperty().setValue(true);
        dugmeText.getStyleClass().add("artikal");
        dugmeText.setText(novaStavka.naziv);
        naziv.getChildren().add(dugmeText);
        
        Label kolicinaArtikal = new Label();
        kolicinaArtikal.getStyleClass().add("artikal"); 
        kolicinaArtikal.setText(novaStavka.kolicina + "");
        kolicina.getChildren().add(kolicinaArtikal);
        
        Label cenaArtikal = new Label();
        cenaArtikal.getStyleClass().add("artikal"); 
        cenaArtikal.setText(novaStavka.getCena() + "din");
        cena.getChildren().add(cenaArtikal);
        
        for (StavkaTure dodatnaStavka : novaStavka.dodatniArtikli) {
            Label dodatni = new Label();
            dodatni.wrapTextProperty().setValue(true);
            dodatni.getStyleClass().add("dodatni");
            dodatni.setText("-> " + dodatnaStavka.naziv);
            naziv.getChildren().add(dodatni);
            
            Label dodatniKolicina = new Label();
            dodatniKolicina.getStyleClass().add("dodatni");
            dodatniKolicina.setText(dodatnaStavka.kolicina + "");
            kolicina.getChildren().add(dodatniKolicina);
            
        }

        for (StavkaTure opisnaStavka : novaStavka.opisniArtikli) {
            Label opisni = new Label();
            opisni.wrapTextProperty().setValue(true);
            opisni.getStyleClass().add("opisni");
            opisni.setText("* " + opisnaStavka.naziv);
            naziv.getChildren().add(opisni);
            
            Label opisniKolicina = new Label();
            opisniKolicina.getStyleClass().add("dodatni");
            opisniKolicina.setText(opisnaStavka.kolicina + "");
            kolicina.getChildren().add(opisniKolicina);
            
        }

        dugmeContent.getChildren().addAll(naziv, kolicina, cena);
        
        dugmeStavka.setGraphic(dugmeContent);

        dugmeStavka.setPodatak(novaStavka);
        
        dugmeStavka.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {
                               RM_Button izabranaStavka = (RM_Button)e.getSource();
                               izabranaStavka.getParent();
                               premestiStavku(
                                       (StavkaTure)izabranaStavka.getPodatak(),
                                       (VBox)izabranaStavka.getParent()
                               );
                            }
                        });
   }
   
    private void premestiStavku(StavkaTure izabranaStavka, VBox content) 
    {
        if (content.getId().equals(contentA.getId())) {
            System.out.println(content.getId() + " " + izabranaStavka.naziv);

        }
        
        if (content.getId().equals(contentB.getId())) {
            System.out.println(content.getId() + " " + izabranaStavka.naziv);

        }
        
        
    }
}
