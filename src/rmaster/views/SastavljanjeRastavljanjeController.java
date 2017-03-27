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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import rmaster.assets.FXMLDocumentController;
import rmaster.ScreenController;
import rmaster.assets.DBBroker;
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
    
    @FXML private ImageView barMasterLogo;
    
    @FXML private Label imeKonobara;
    
    @FXML private Label casovnik;
    
    @FXML private Button izaberiStoA;
    @FXML private Button izaberiStoB;
    
    @FXML private ScrollPane scrollPaneA;
    @FXML private ScrollPane scrollPaneB;
    
    @FXML private VBox contentA;
    @FXML private VBox contentB;
    
    @FXML private HBox gostiStoA = new HBox();
    @FXML private HBox gostiStoB = new HBox();
    
    //@FXML private VBox novaTuraA = new VBox();
    //@FXML private VBox novaTuraB = new VBox();
    
    private ToggleGroup gostiGrupaA = new ToggleGroup();
    private ToggleGroup gostiGrupaB = new ToggleGroup();
    
    private List<Porudzbina> porudzbineStolaA = new ArrayList<>();
    private List<Porudzbina> porudzbineStolaB = new ArrayList<>();
                
    private RM_Button labelB = new RM_Button();
    
    private List<Object> modeliZaBrisanje = new ArrayList();
    private List<Object> modeliZaUPDATE = new ArrayList();
       
    List<ToggleButton> stavkeZaSastavljanjeA = new ArrayList<>();
    List<ToggleButton> stavkeZaSastavljanjeB = new ArrayList<>();
    
    HashMap<String, RM_Button> izabranaPorudzbinaMap = new HashMap();
    
    Porudzbina porudzbinaZaRastavljanje;
    Porudzbina porudzbinaZaSastavljanje;
    Tura prebacenaTura;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scrollPaneA.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneB.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneA.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneB.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        //labelB.setId("novaTuraLabel");
        //labelB.setText("NOVA TURA");
        //labelB.textAlignmentProperty().set(TextAlignment.CENTER);
        //labelB.setPrefSize(432, 20);
        
        //novaTuraB.setId("novaTuraB");
        
        barMasterLogo.setImage(RMaster.logo);
    }    
    
    @Override
    public void initData(Object data)
    {
        imeKonobara.setText(RMaster.ulogovaniKonobar.imeKonobara);
        
        RMaster.setClockLabelForUpdate(casovnik);
        
        gostiStoA.getChildren().clear();
        gostiStoB.getChildren().clear();
        
        izaberiStoA.setText("Izaberi sto");
        izaberiStoB.setText("Izaberi sto");
        
        contentA.getChildren().clear();
        contentB.getChildren().clear();
        
        //novaTuraA.getChildren().clear();
        //novaTuraB.getChildren().clear();
        porudzbinaZaSastavljanje = null;
        prebacenaTura = null;
    }
    
    public void nazadNaPrikazSale(ActionEvent event) 
    {            
        //sledeca stranica 
        myController.setScreen(ScreenMap.PRIKAZ_SALA, null);
    }
    
    public void pozivanjePrikazSalePopup(ActionEvent event) {

        SalePopupController tastatura = new SalePopupController();
        tastatura.setWidth(1024.);
        tastatura.setHeight(768.);
        tastatura.getDialogPane().setMaxSize(1024., 768.);
        tastatura.setResizable(false);
        
        
        
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
            contentA.getChildren().clear();
            porudzbineStolaA.clear();
            //novaTuraA.getChildren().clear();
            
            new Thread() {
            
                @Override
                public void start()
                {
                    popuniGosteA(izabraniStoMap);
                }

            }.start();
            
        }
        
        if (pritisnutoDugme.getId().equals("izaberiStoB")) {
            
            gostiStoB.getChildren().clear();
            contentB.getChildren().clear();
            porudzbineStolaB.clear();
            //novaTuraB.getChildren().clear();

            new Thread() {
            
                @Override
                public void start()
                {
                    popuniGosteB(izabraniStoMap);
                }

            }.start();
            
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
        
        boolean prvaPorudzbina = true;
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
            if (prvaPorudzbina) {
                noviGostButton.fire();
                prvaPorudzbina = false;
            }
        } 
        
        ObservableList<Node> listaDugmica = FXCollections.observableArrayList(lista);            
                
        gostiStoA.getChildren().addAll(listaDugmica);
    }
    
    public void popuniGosteB(HashMap<String, String> sto) 
    {
        Porudzbina porudzbinaTrenutna;
        List<Node> lista = new ArrayList<>();

        Sto izabraniSto = new Sto(sto);
        List racuniStola = izabraniSto.getPorudzbineStola();

        if (racuniStola.isEmpty())
        {
            porudzbinaTrenutna = new Porudzbina(
                    new Gost(1), 
                    izabraniSto.stoId,
                    izabraniSto.stoBroj
            );
            
            porudzbineStolaB.add(porudzbinaTrenutna);
            RadioButton noviGostButton = new RadioButton("1");
            noviGostButton.setToggleGroup(gostiGrupaB);
            porudzbinaZaSastavljanje = porudzbinaTrenutna;
            
            lista.add(prikaziPorudzbinuTaskSetButtonActionB(noviGostButton, "1"));
            //sta se desava ako nema nijednog gosta
            //TODO dodati novog gosta
            //return;
        }
        
        boolean prvaPorudzbina = true;
        for (Object racun : racuniStola) {
            
            Map<String, String> red = (Map<String, String>) racun;

            String brojNovogGosta = red.get("gost");

            porudzbinaTrenutna = new Porudzbina(
                    new Gost(brojNovogGosta), 
                    //red.get("id"),
                    izabraniSto.stoId,
                    izabraniSto.stoBroj
            );

            porudzbinaTrenutna.setID(Long.parseLong(red.get("id")));
            porudzbineStolaB.add(porudzbinaTrenutna);
            
            RadioButton noviGostButton = new RadioButton(brojNovogGosta);

            noviGostButton.setToggleGroup(gostiGrupaB);
            
            lista.add(prikaziPorudzbinuTaskSetButtonActionB(noviGostButton, brojNovogGosta));
            if (prvaPorudzbina) {
                noviGostButton.fire();
                porudzbinaZaSastavljanje = porudzbinaTrenutna;
                prvaPorudzbina = false;
            }
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
                                        porudzbinaZaRastavljanje = porudzbina;
                                        prikaziPorudzbinu(contentA, porudzbinaZaRastavljanje);
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
                                
                                //if (contentB.getChildren().contains(labelB)) {
                                //    return;
                                //}
                                
                                //contentB.getChildren().add(labelB);
                                //contentB.getChildren().add(novaTuraB);
                                porudzbinaZaSastavljanje = null;
                                prebacenaTura = null;
                                
                                for (Porudzbina porudzbina : porudzbineStolaB) {
                                    if (("" + porudzbina.getGost().getGostID()).equals(((RadioButton)e.getSource()).getId())) {
                                        porudzbinaZaSastavljanje = porudzbina;
                                        break;
                                    }
                                }
                                if (porudzbinaZaSastavljanje != null) {
                                    prikaziPorudzbinu(contentB, porudzbinaZaSastavljanje);
                                    prebacenaTura = new Tura();
                                }
                            }      
                        });    


        return noviGostButton;
    }
    
    private void prikaziPorudzbinu(VBox content, Porudzbina izabranaPorudzbina)
    {
        content.getChildren().clear();

        List<Tura> listaTura = izabranaPorudzbina.getTure();

        List<Node> listaDugmica = new ArrayList<>();

        if (listaTura.isEmpty()) {
            if (izabranaPorudzbina.getID() != porudzbinaZaRastavljanje.getID()) {
                if ((prebacenaTura != null) && (prebacenaTura.listStavkeTure.size()>0)) {
                    listaTura.add(prebacenaTura);
                }
            }
        }
        for (Tura novaTura : listaTura) {
            if (izabranaPorudzbina.getID() == porudzbinaZaRastavljanje.getID()) {
                RM_Button prebaciCeluTuru = new RM_Button();
                prebaciCeluTuru.setVrsta(novaTura.listStavkeTure.size());
                dodajAkcijuZaPrebaciCeluTuru(prebaciCeluTuru, novaTura);
                for(StavkaTure novaStavka : novaTura.listStavkeTure) {
                    HBox stavka = dodajAkcijeNaStavkuTure(prebaciCeluTuru, novaStavka);
                    listaDugmica.add(stavka);
                } 
                listaDugmica.add(prebaciCeluTuru);
            } else {
                RM_Button vratiCeluTuru = new RM_Button();
                vratiCeluTuru.setVrsta(novaTura.listStavkeTure.size());
                dodajAkcijuZaVratiCeluTuru(vratiCeluTuru, novaTura);
                for(StavkaTure novaStavka : novaTura.listStavkeTure) {
                    HBox stavka = dodajAkcijeNaStavkuTure(vratiCeluTuru, novaStavka);
                    listaDugmica.add(stavka);
                } 
                listaDugmica.add(vratiCeluTuru);
            }
        }

        content.getChildren().addAll(listaDugmica);

    }
   
   private HBox dodajAkcijeNaStavkuTure(RM_Button prebaciCeluTuru, StavkaTure novaStavka)
   {
        HBox stavka = new HBox();
        
        HBox akcije = new HBox();

        ToggleButton sastavi = new ToggleButton();
        
        stavkeZaSastavljanjeA.add(sastavi);
        
        sastavi.setText("»«");
        
        sastavi.setId(novaStavka.id + "");
        
        RM_Button rastavi = new RM_Button();
        
        rastavi.setId(novaStavka.id + "");

        rastavi.setText("«»");

        akcije.getChildren().addAll(sastavi, rastavi);

        akcije.setMinHeight(30);

        rastavi.setDisable(true);

        sastavi.setPrefWidth(60);
        sastavi.setMaxHeight(Double.MAX_VALUE);

        rastavi.setPrefWidth(60);
        rastavi.setMaxHeight(Double.MAX_VALUE);


        if (novaStavka.kolicina > 1) {

            rastavi.setDisable(false);
            
            rastavi.setOnAction(new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent event) {
                            RM_Button dugme = (RM_Button)event.getSource();
                            String id = dugme.getId();
                            //rastaviIzabranuStavku(izabranaPorudzbinaMap.get(id));
                            rastaviIzabranuStavku(dugme);
                        }
                    }); 
        }

        RM_Button dugmeStavka = new RM_Button();

        izabranaPorudzbinaMap.put(novaStavka.id + "", dugmeStavka);
       
        dugmeStavka.setVrsta(prebaciCeluTuru);

        dodajAkcijuZaPrebaciStavku(dugmeStavka, novaStavka);

        stavka.getChildren().addAll(dugmeStavka, sastavi, rastavi);
                
        return stavka;
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

        prebaciCeluTuru.setPodatak(novaTura);

        prebaciCeluTuru.setOnAction(new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent e) {
                            if (prebacenaTura == null) {
                                return;
                            }
                            
                            RM_Button dugme = (RM_Button)e.getSource();
                            prebacenaTura = (Tura)dugme.getPodatak();
                             
                            porudzbinaZaRastavljanje.getTure().remove(prebacenaTura);
                            porudzbinaZaSastavljanje.getTure().add(prebacenaTura);
                            
                            //prebacenaTura = new Tura();
                            
                            refresh();
                            

                        }
                    }); 
    }
   
    private void dodajAkcijuZaVratiCeluTuru(RM_Button vratiCeluTuru, Tura novaTura)
    {
        //TODO
        vratiCeluTuru.setPrefSize(432, 50);

        try {
            String period = Utils.getDateDiff(novaTura.getVremeTure(), new Date(), TimeUnit.MINUTES);
            vratiCeluTuru.setText("Vrati celu turu (" + period + ")");
        } catch (Exception e) {
            vratiCeluTuru.setText("Vrati celu turu");
        }

        vratiCeluTuru.setPodatak(novaTura);

        vratiCeluTuru.setOnAction(new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent e) {
                            RM_Button dugme = (RM_Button)e.getSource();
                            prebacenaTura = (Tura)dugme.getPodatak();
                             
                            porudzbinaZaRastavljanje.getTure().add(prebacenaTura);
                            porudzbinaZaSastavljanje.getTure().remove(prebacenaTura);
                            prebacenaTura = new Tura();
                            
                            refresh();
                           //TODO
                        }
                    }); 
    }
   
   private void dodajAkcijuZaPrebaciStavku(RM_Button dugmeStavka, StavkaTure novaStavka) 
   {
    
        double cenaStavke = 0.;
        
        dugmeStavka.setPrefWidth(312);
        
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
        
        naziv.setPrefWidth(210);
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
        
        cenaStavke = novaStavka.getCena();
        Label cenaArtikal = new Label();
        cenaArtikal.getStyleClass().add("artikal"); 
        //cenaArtikal.setText(novaStavka.getCena() + "din");
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
            
            cenaStavke = cenaStavke + dodatnaStavka.getCena();
            
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
       
        cenaArtikal.setText(cenaStavke + "din");
        
        dugmeStavka.setGraphic(dugmeContent);

        dugmeStavka.setPodatak(novaStavka);
        
        dugmeStavka.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {
                               RM_Button izabranaStavka = (RM_Button)e.getSource();
                               premestiStavku(izabranaStavka);
                            }
                        });

        dugmeContent.getChildren().addAll(naziv, kolicina, cena);
   }
   
    private void premestiStavku(RM_Button dugme) 
    {
        if (dugme.getParent().getParent().getId().equals(contentB.getId())) {
            return;
        }
        if (prebacenaTura == null) {
            return;
        }
                
        StavkaTure stavkaZaPrebacivanje = (StavkaTure)dugme.getPodatak();
        
        prebacenaTura.listStavkeTure.add(stavkaZaPrebacivanje);
        
        Tura t = null;
        for (Tura tura : porudzbinaZaRastavljanje.getTure()) {
            for (StavkaTure stavkaTure : tura.listStavkeTure) {
                if (stavkaTure.getID() == stavkaZaPrebacivanje.getID()) {
                    t = tura;
                    break;
                }
            }
            if (t != null) {
                break;
            }
        }
        
        if (t != null) {
            t.listStavkeTure.remove(stavkaZaPrebacivanje);
            if (t.listStavkeTure.size() == 0) {
                porudzbinaZaRastavljanje.getTure().remove(t);
            }
        }
        
        this.refresh();
    }

    private void refresh() {
        prikaziPorudzbinu(contentA, porudzbinaZaRastavljanje);
        prikaziPorudzbinu(contentB, porudzbinaZaSastavljanje);

    }
    
    
    public void sastaviIzabraneStavkeA(ActionEvent event)
    {
        //ako nije izabran gost na kome se prebacuje
        //if (contentB.getChildren().isEmpty()) {
        if (porudzbinaZaSastavljanje == null) {
            return;
        }
        
        List<ToggleButton> izabraneStavke = 
                this.vratiSamoIzabraneStavkeZaSastavljanje(stavkeZaSastavljanjeA);
            
        
        //ako je izabrana samo jedna ili nijedna stavka
        if (izabraneStavke.size() <= 1) {
            return;
        }
        
        
        HashMap<Long, List<RM_Button>> artikliZaSastavljanje = 
                this.napraviMapuArtikalaKojiSeSastavljaju(
                        izabraneStavke,
                        izabranaPorudzbinaMap
                    );
        
        
        for (List<RM_Button> listaStavki : artikliZaSastavljanje.values()) {
            
            // pravi se nova stavka u contentB
            RM_Button sastavljenaStavkaDugme = listaStavki.get(0);
            
            StavkaTure sastavljenaStavka = (StavkaTure)sastavljenaStavkaDugme.getPodatak();
                        
            for (int i = 1; i < listaStavki.size(); i++) {
                
                RM_Button stavkaZaUklanjanjeDugme = listaStavki.get(i);
                StavkaTure stavkaZaUklanjanje = (StavkaTure)stavkaZaUklanjanjeDugme.getPodatak();
                
                Tura t = null;
                for (Tura tura : porudzbinaZaRastavljanje.getTure()) {
                    for (StavkaTure stavkaTure : tura.listStavkeTure) {
                        if (stavkaTure == stavkaZaUklanjanje) {
                            t = tura;
                            break;
                        }
                    }
                    if (t != null) {
                        break;
                    }
                }
                if (t != null) {
                    t.listStavkeTure.remove(stavkaZaUklanjanje);
                    if (t.listStavkeTure.size() == 0) {
                        porudzbinaZaRastavljanje.getTure().remove(t);
                        modeliZaBrisanje.add(t);
                    }
                }
 
                stavkaZaUklanjanje.setKolicina(0.);
                modeliZaBrisanje.add(stavkaZaUklanjanje);
                         
                sastavljenaStavka.setKolicina(sastavljenaStavka.getKolicina() + stavkaZaUklanjanje.getKolicina());
            }
            
            dodajAkcijuZaPrebaciStavku(sastavljenaStavkaDugme, sastavljenaStavka);

            HBox celaStavka = (HBox)sastavljenaStavkaDugme.getParent();
            
            izbrisiAkcijeIzStavke(celaStavka);
            
            premestiStavku(sastavljenaStavkaDugme);
                        
        }
        
    }
    
    private void izbrisiAkcijeIzStavke(HBox celaStavka)
    {        
        List<Node> removeList = new ArrayList<>();
        
        for(Node node : celaStavka.getChildren()) {
                
            if (node instanceof ToggleButton) {
                ToggleButton dugme = (ToggleButton)node;

                stavkeZaSastavljanjeA.remove(dugme);

                removeList.add(dugme);
            }

            if (node instanceof RM_Button) {

                RM_Button dugme = (RM_Button) node;

                if (dugme.getText().equals("«»")) {

                    removeList.add(dugme);
                } 

                else {

                    dugme.setPrefWidth(432);

                }

            }
                
        }
        
        celaStavka.getChildren().removeAll(removeList);
    }
    
    private List vratiSamoIzabraneStavkeZaSastavljanje(List<ToggleButton> listaStavki)
    {
        List<ToggleButton> izabraneStavke = new ArrayList();
        
        for (ToggleButton dugme : listaStavki) {
            
            if (!dugme.isSelected()) {
                continue;
            }
            
            dugme.setSelected(false);

            izabraneStavke.add(dugme);
                    
        } 
        
        return izabraneStavke;
    }
    
    
    //todo rastaviti ovo na prostije metode
    private HashMap napraviMapuArtikalaKojiSeSastavljaju(
            List<ToggleButton> izabraneStavke,
            HashMap<String, RM_Button> mapaDugmica) 
    {
        
        HashMap<Long, List<RM_Button>> artikliZaSastavljanje = new HashMap();
        
        
        for (int i = 0; i < izabraneStavke.size(); i++) {

            RM_Button stavkaDugme = mapaDugmica.get(
                    izabraneStavke.get(i).getId()
            );

            StavkaTure stavka = (StavkaTure)stavkaDugme.getPodatak();
            
            for (int j = 0; j < izabraneStavke.size(); j++) {
                
                //sprecava uporedjivanje istih indeksa
                if (j == i) {
                    continue;
                }
                                
                RM_Button sledecaStavkaDugme = mapaDugmica.get(
                        izabraneStavke.get(j).getId()
                );
                
                StavkaTure sledecaStavka = (StavkaTure)sledecaStavkaDugme.getPodatak();

                //sastavice se samo barem 2 izabrane stavke
                if (stavka.ARTIKAL_ID == sledecaStavka.ARTIKAL_ID) {
                    
                    if (artikliZaSastavljanje.containsKey(stavka.ARTIKAL_ID)) {
                        
                        artikliZaSastavljanje.get(stavka.ARTIKAL_ID).add(stavkaDugme);
                        continue;
                    }
                    
                    List<RM_Button> novaLista = new ArrayList<>();
                
                    novaLista.add(stavkaDugme);
                                        
                    artikliZaSastavljanje.put(stavka.ARTIKAL_ID, novaLista);
                    
                }

            }    

        }
        
        return artikliZaSastavljanje;
    } 
    
    private void rastaviIzabranuStavku(RM_Button izabranaStavka)
    {
        //dialog za rastavljanje
        if (prebacenaTura == null) {
            return;
        }
        
        StavkaTure stavkaZaRastavljanje = (StavkaTure)izabranaStavka.getPodatak();
        RastaviStavkuPopupController tastatura = new RastaviStavkuPopupController(
                stavkaZaRastavljanje
        );
        
        Optional<HashMap<String, String>> result = tastatura.showAndWait();
        
        if (!result.isPresent()){ 
            return;
        }
        
        //rezultat je mapa, treba napraviti novu stavku
        HashMap<String, String> rezultatMap = result.get();
        double kolicina = Double.parseDouble(rezultatMap.get("kolicina"));
        
        StavkaTure novaStavka = stavkaZaRastavljanje.getClone();
        stavkaZaRastavljanje.setKolicina(stavkaZaRastavljanje.getKolicina() - kolicina);
        novaStavka.setKolicina(kolicina);
        
        prebacenaTura.addStavkaTure(novaStavka);
        
        // OVDE TREBA VODITI RACUNA - ovo treba da ide sa UPDATE da se promeni u bazi kolicina
        modeliZaUPDATE.add(stavkaZaRastavljanje);
        
        this.refresh();
        
        //Glavna Stavka
        //HashMap<String, String> glavnaStavkaZaPromenu = (HashMap)rezultatMap.get("glavniArtikal");
        
        //Lista svih dodatnih Artikala
        //HashMap<String, HashMap<String, String>> MapaDodatnihStavki = (HashMap)rezultatMap.get("listaDodatnih");
        

        //Treba promeniti kolicinu izabranoj stavci i napraviti novo dugme u ContentB
        //Dodati promenjenu stavku u listu
        //novu stavku u listu za pravljenje novih modela
    }
    
    private void obrisiModele()
    {
        
    }
    
    private void promeniModele()
    {
        
    }
    
    @FXML private void potvrdi() {
        DBBroker db = new DBBroker();
        long result = 0;

        try {
            for (Tura tura : porudzbinaZaSastavljanje.getTure()) {
                if (tura.listStavkeTure.size()>0) {
                    HashMap<String,String> mapaTura = new HashMap();
                    mapaTura.put("brojStola", "" + porudzbinaZaSastavljanje.getBrojStolaBroj());
                    if (tura.datum == null) {
                        tura.datum = new Date();
                    }
                    mapaTura.put("datum", Utils.getStringFromDate(tura.datum));
                    mapaTura.put("pripremljena", "false");
                    mapaTura.put("uPripremi", "false");
                    mapaTura.put("RACUN_ID", "" + porudzbinaZaSastavljanje.getID());
                    if (tura.getTuraID() != 0)
                        db.izmeni("tura", "id", "" + tura.getTuraID(), mapaTura, porudzbinaZaSastavljanje.getBlokiranaPorudzbina());
                    else {
                        String[] imenaArgumenata = {"settingName"};
                        String[] vrednostiArgumenata = {"tura.broj.sledeci"};
                        int rez = DBBroker.getValueFromFunction("getSledeciRedniBroj", imenaArgumenata, vrednostiArgumenata);
                        mapaTura.put("brojTure", "" + rez);
                        result = db.ubaciRed("tura", mapaTura, porudzbinaZaSastavljanje.getBlokiranaPorudzbina());
                        tura.turaID = result;
                    }

                    for (StavkaTure stavkaTure : tura.listStavkeTure) {
                        boolean pronadjenaStavka = false;
                        for (Tura turaZaRastavljanje : porudzbinaZaRastavljanje.getTure()) {
                            for (StavkaTure stavkaTureZaRastavljanje : turaZaRastavljanje.listStavkeTure) {
                                if (stavkaTureZaRastavljanje.getID() == stavkaTure.getID()) {
                                    stavkaTureZaRastavljanje.snimi();
                                    stavkaTure.setID(0);
                                    pronadjenaStavka = true;
                                    break;
                                }
                            }
                            if (pronadjenaStavka) {
                                break;
                            }
                        }
                        stavkaTure.setRacunID(porudzbinaZaSastavljanje.getID());
                        stavkaTure.setTuraID(tura.getTuraID());
                        stavkaTure.snimi();
                    }
                }
            }
            nazadNaPrikazSale(null);
            
            
            // Odraditi brisanje iz modeliZaBrisanje ako je kolicina==0 ili brojStavkiTure==0, a ako nije onda UPDATE modeliZaUPDATE
        } catch (Exception e) {
        }
    }
    
}
