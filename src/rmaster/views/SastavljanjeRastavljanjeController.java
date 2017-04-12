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
import rmaster.assets.RM_Datetime;
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
    
    @FXML private ScrollPane stoA;
    @FXML private ScrollPane stoB;
    
    @FXML private VBox contentA;
    @FXML private VBox contentB;
    
    @FXML private HBox gostiStoA = new HBox();
    @FXML private HBox gostiStoB = new HBox();
    
    
    private ToggleGroup gostiGrupaA = new ToggleGroup();
    private ToggleGroup gostiGrupaB = new ToggleGroup();
    
    private List<Porudzbina> porudzbineStolaA = new ArrayList<>();
    private List<Porudzbina> porudzbineStolaB = new ArrayList<>();
                    
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
        
        barMasterLogo.setImage(RMaster.logo);
    }    
    
    @Override
    public void initData(Object data)
    {
        imeKonobara.setText(RMaster.ulogovaniKonobar.imeKonobara);
        
        RMaster.setClockLabelForUpdate(casovnik);
        
        obrisiCache();
        
    }
    
    public void nazadNaPrikazSale(ActionEvent event) 
    {            
        //sledeca stranica 
        obrisiCache();
        myController.setScreen(ScreenMap.PRIKAZ_SALA, null);
    }
    
    private void obrisiCache() {
        this.modeliZaBrisanje.clear();
        this.modeliZaUPDATE.clear();
        
        obrisiCacheLevo();
        obrisiCacheDesno();
    }

    @FXML
    private void ponistiSve() {
        obrisiCacheDesno();
        osveziLevo();
        refresh();
    }
    private void obrisiCacheDesno() {
        gostiStoB.getChildren().clear();
        izaberiStoB.setText("Izaberi sto");
        contentB.getChildren().clear();
        porudzbinaZaSastavljanje = null;
        prebacenaTura = null;
        blokirajSve(false);
    }

    private void obrisiCacheLevo() {
        gostiStoA.getChildren().clear();
        izaberiStoA.setText("Izaberi sto");
        contentA.getChildren().clear();
        porudzbinaZaRastavljanje = null;
        blokirajSve(false);
    }
    

    private void osveziLevo() {
        porudzbinaZaRastavljanje = new Porudzbina(
                    porudzbinaZaRastavljanje.getGost(), 
                    porudzbinaZaRastavljanje.getID(),
                    "" + porudzbinaZaRastavljanje.getStoID(),
                    "" + porudzbinaZaRastavljanje.getBrojStolaBroj()
            );

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
            noviGostButton.fire();
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
                                    prebacenaTura.setBrojStolaBroj(porudzbinaZaSastavljanje.getBrojStolaBroj());
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

        if (prebacenaTura != null) {
            if (izabranaPorudzbina.getID() != porudzbinaZaRastavljanje.getID()) {
                if (!listaTura.contains(prebacenaTura)) {
                    if ((prebacenaTura != null) && (prebacenaTura.listStavkeTure.size()>0)) {
                        listaTura.add(prebacenaTura);
                    }
                }
            }
        }
        
        for (Tura novaTura : listaTura) {
            
            //popunjava panel porudzbinom za rastavljanje
            if (izabranaPorudzbina.getID() == porudzbinaZaRastavljanje.getID()) {
                
                RM_Button prebaciCeluTuru = new RM_Button();
                
                prebaciCeluTuru.setVrsta(novaTura.listStavkeTure.size());
                
                dodajAkcijuZaPrebaciCeluTuru(prebaciCeluTuru, novaTura);
                
                for(StavkaTure novaStavka : novaTura.listStavkeTure) {
                    
                    HBox stavka = dodajAkcijeNaStavkuTure(prebaciCeluTuru, novaStavka, true);
                    
                    listaDugmica.add(stavka);
                    
                } 
                
                listaDugmica.add(prebaciCeluTuru);
                
            //popunjava panel novom turom koja se sastavlja  
            } else {
                
                RM_Button vratiCeluTuru = new RM_Button();
                
                vratiCeluTuru.setVrsta(novaTura.listStavkeTure.size());
                
                //Saša - mislim da nam ovo ne treba 28.03.2017
                dodajAkcijuZaVratiCeluTuru(vratiCeluTuru, novaTura);
                
                for(StavkaTure novaStavka : novaTura.listStavkeTure) {
                    
                    HBox stavka = dodajAkcijeNaStavkuTure(vratiCeluTuru, novaStavka, false);
                    
                    listaDugmica.add(stavka);
                } 
                
                listaDugmica.add(vratiCeluTuru);
            }
        }

        content.getChildren().addAll(listaDugmica);

    }
   
   private HBox dodajAkcijeNaStavkuTure(
           RM_Button prebaciCeluTuru, 
           StavkaTure novaStavka,
           boolean dodajAkcijuZaPremesti)
   {              
        HBox stavka = new HBox();
        
        RM_Button dugmeStavka = new RM_Button();
        
        dodajAkcijuZaPrebaciStavku(
                dugmeStavka, 
                novaStavka, 
                dodajAkcijuZaPremesti
        );
        
        dugmeStavka.setVrsta(prebaciCeluTuru);
        
        ToggleButton sastavi = new ToggleButton();
        
        sastavi.setPrefWidth(60);

        sastavi.setDisable(true);
        
        RM_Button rastavi = new RM_Button();
        
        rastavi.setPrefWidth(60);

        rastavi.setDisable(true);
        
        sastavi.setMaxHeight(Double.MAX_VALUE);

        rastavi.setMaxHeight(Double.MAX_VALUE);
        
        //dodaje akcije za sastavi i rastavi samo ako nije slozena stavka
        if (!novaStavka.getImaDodatneIliOpisneArtikle() && dodajAkcijuZaPremesti) {
        
            sastavi.setDisable(false);

            stavkeZaSastavljanjeA.add(sastavi);

            sastavi.setText("»«");

            sastavi.setId(novaStavka.id + "");

            rastavi.setDisable(false);

            rastavi.setId(novaStavka.id + "");
            rastavi.setPodatak(novaStavka);

            rastavi.setText("«»");

            rastavi.setDisable(true);

            if (novaStavka.kolicina > 1) {

                rastavi.setDisable(false);

                rastavi.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent event) {

                                RM_Button dugme = (RM_Button)event.getSource();

                                StavkaTure stavka = (StavkaTure)dugme.getPodatak();

                                rastaviIzabranuStavku(stavka);

                                blokirajSve(true);
                            }
                        }); 
            }
            
        }
        
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
                            Tura prebTura = (Tura)dugme.getPodatak();
                            prebTura.setBrojStolaBroj(porudzbinaZaSastavljanje.getBrojStolaBroj());
                            
                            porudzbinaZaRastavljanje.getTure().remove(prebTura);
                            porudzbinaZaSastavljanje.getTure().add(prebTura);
                            
                            refresh();
                            blokirajSve(true);
                        }
                    }); 
    }
   
    private void dodajAkcijuZaVratiCeluTuru(RM_Button vratiCeluTuru, Tura novaTura)
    {
        //TODO
        vratiCeluTuru.setPrefSize(432, 50);

        try {
            String period = Utils.getDateDiff(novaTura.getVremeTure(), new Date(), TimeUnit.MINUTES);
            vratiCeluTuru.setText("PREBAČENA TURA (" + period + ")");
        } catch (Exception e) {
            vratiCeluTuru.setText("NOVA TURA");
        }

        vratiCeluTuru.setPodatak(novaTura);

//        vratiCeluTuru.setOnAction(new EventHandler<ActionEvent>() {
//                        @Override public void handle(ActionEvent e) {
//                            RM_Button dugme = (RM_Button)e.getSource();
//                            prebacenaTura = (Tura)dugme.getPodatak();
//                            prebacenaTura.setBrojStolaBroj(porudzbinaZaRastavljanje.getBrojStolaBroj()); 
//                            porudzbinaZaRastavljanje.getTure().add(prebacenaTura);
//                            porudzbinaZaSastavljanje.getTure().remove(prebacenaTura);
//                            prebacenaTura = new Tura();
//                            
//                            
//                            refresh();
//                           //TODO
//                        }
//                    }); 
    }
   
   private void dodajAkcijuZaPrebaciStavku(
           RM_Button dugmeStavka, 
           StavkaTure novaStavka,
           boolean dodajAkcijuZaPremestanje) 
   {
    
        double cenaStavke = 0.;
        
        
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
            
            Label dodatniCena = new Label();
            dodatniCena.getStyleClass().add("dodatni");
            dodatniCena.setText(dodatnaStavka.cena + "din");
            cena.getChildren().add(dodatniCena);

        }

        for (StavkaTure opisnaStavka : novaStavka.opisniArtikli) {
            
            Label opisni = new Label();
            opisni.wrapTextProperty().setValue(true);
            opisni.getStyleClass().add("opisni");
            opisni.setText("* " + opisnaStavka.naziv);
            naziv.getChildren().add(opisni);
            
            Label opisniKolicina = new Label();
            opisniKolicina.getStyleClass().add("opisni");
            opisniKolicina.setText(opisnaStavka.kolicina + "");
            kolicina.getChildren().add(opisniKolicina);
            
            //ovo samo da bi bilo poravnato
            Label opisniCena = new Label();
            opisniCena.getStyleClass().add("dodatni");
            opisniCena.setText("");
            cena.getChildren().add(opisniCena);
        }
       
        dugmeStavka.setGraphic(dugmeContent);

        dugmeStavka.setPodatak(novaStavka);
        
        dugmeStavka.setPrefWidth(312);
        naziv.setPrefWidth(210);
        kolicina.setPrefWidth(41);
        cena.setPrefWidth(81);
           
        if (dodajAkcijuZaPremestanje) {

            dugmeStavka.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                   RM_Button izabranaStavka = (RM_Button)e.getSource();
                                   premestiStavku((StavkaTure)izabranaStavka.getPodatak());
                                   blokirajSve(true);
                                }
                            });
        }

        dugmeContent.getChildren().addAll(naziv, kolicina, cena);
   }
   
    private void premestiStavku(StavkaTure stavkaZaPrebacivanje) 
    {
        if (prebacenaTura == null) {
            return;
        }
                
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
            if (t.listStavkeTure.isEmpty()) {
                porudzbinaZaRastavljanje.getTure().remove(t);
                modeliZaBrisanje.add(t);
            }
        }
        
        this.refresh();
    }

    private void refresh() {
        if (porudzbinaZaRastavljanje != null) {
            prikaziPorudzbinu(contentA, porudzbinaZaRastavljanje);
        }
        if (porudzbinaZaSastavljanje != null) {
            prikaziPorudzbinu(contentB, porudzbinaZaSastavljanje);
        }

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
        
        
        HashMap<Long, List<StavkaTure>> artikliZaSastavljanje = 
                this.napraviMapuArtikalaKojiSeSastavljaju(
                        izabraneStavke
                    );
        
        
        for (List<StavkaTure> listaStavki : artikliZaSastavljanje.values()) {
            
            // pravi se nova stavka u contentB
            StavkaTure sastavljenaStavka = listaStavki.get(0);
                        
            for (int i = 1; i < listaStavki.size(); i++) {
                
                StavkaTure stavkaZaUklanjanje = listaStavki.get(i);
                
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
                    if (t.listStavkeTure.isEmpty()) {
                        porudzbinaZaRastavljanje.getTure().remove(t);
                        modeliZaBrisanje.add(t);
                    }
                }
 
                
                modeliZaBrisanje.add(stavkaZaUklanjanje);
                         
                sastavljenaStavka.setKolicina(sastavljenaStavka.getKolicina() + stavkaZaUklanjanje.getKolicina());
            }
            
            premestiStavku(sastavljenaStavka);
                  
            blokirajSve(true);
        }
        
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
            List<ToggleButton> izabraneStavke
            ) 
    {
        
        HashMap<Long, List<StavkaTure>> artikliZaSastavljanje = new HashMap();
        
        
        for (int i = 0; i < izabraneStavke.size(); i++) {
            StavkaTure stavka = pronadjiStavkuByID(Long.parseLong(izabraneStavke.get(i).getId()), porudzbinaZaRastavljanje);
            
            for (int j = i+1; j < izabraneStavke.size(); j++) {
                
                StavkaTure sledecaStavka = pronadjiStavkuByID(Long.parseLong(izabraneStavke.get(j).getId()), porudzbinaZaRastavljanje);

                //sastavice se samo barem 2 izabrane stavke
                if ((sledecaStavka != null) && (stavka.ARTIKAL_ID == sledecaStavka.ARTIKAL_ID)) {
                    
                    if (artikliZaSastavljanje.containsKey(stavka.ARTIKAL_ID)) {
                        if (!artikliZaSastavljanje.get(sledecaStavka.ARTIKAL_ID).contains(sledecaStavka)) {
                            artikliZaSastavljanje.get(stavka.ARTIKAL_ID).add(sledecaStavka);
                        }
                        continue;
                    }
                    
                    List<StavkaTure> novaLista = new ArrayList<>();
                
                    novaLista.add(stavka);
                    novaLista.add(sledecaStavka);
                                        
                    artikliZaSastavljanje.put(stavka.ARTIKAL_ID, novaLista);
                    
                }

            }    

        }
        
        return artikliZaSastavljanje;
    } 
    
    private StavkaTure pronadjiStavkuByID(long ID, Porudzbina porudzbina) {
        StavkaTure stavka = null;
        for (Tura turaZaProlaz : porudzbina.getTure()) {
            for (StavkaTure stavkaTure : turaZaProlaz.listStavkeTure) {
                if (ID == stavkaTure.getID()) {
                    stavka = stavkaTure;
                    break;
                }
            }
            if (stavka != null) {
                break;
            }
        }
        return stavka;
    }
    
    
    private void rastaviIzabranuStavku(StavkaTure izabranaStavka)
    {
        //dialog za rastavljanje
        if (prebacenaTura == null) {
            return;
        }
        
        RastaviStavkuPopupController tastatura = new RastaviStavkuPopupController(
                izabranaStavka
        );
        
        Optional<HashMap<String, String>> result = tastatura.showAndWait();
        
        if (!result.isPresent()){ 
            return;
        }
        
        //rezultat je mapa, treba napraviti novu stavku
        HashMap<String, String> rezultatMap = result.get();
        double kolicina = Double.parseDouble(rezultatMap.get("kolicina"));
        
        StavkaTure novaStavka = izabranaStavka.getClone();
        izabranaStavka.setKolicina(izabranaStavka.getKolicina() - kolicina);
        novaStavka.setKolicina(kolicina);
        
        premestiStavku(novaStavka);
        
        // OVDE TREBA VODITI RACUNA - ovo treba da ide sa UPDATE da se promeni u bazi kolicina
        modeliZaUPDATE.add(izabranaStavka);
        
        this.refresh();
    }
    
    private void obrisiModele()
    {
        for (Object object : modeliZaBrisanje) {
            if (object instanceof StavkaTure) {
                StavkaTure stavkaTure = (StavkaTure)object;
                stavkaTure.destroy();
                stavkaTure = null;
            }
        }
        for (Object object : modeliZaBrisanje) {
            if (object instanceof Tura) {
                Tura tura = (Tura)object;
                tura.destroy();
                tura = null;
            }
        }
    }
    
    private void promeniModele()
    {
        for (Object object : modeliZaUPDATE) {
            if (object instanceof Tura) {
                Tura tura = (Tura)object;
                tura.snimi();
                break;
            }
            if (object instanceof StavkaTure) {
                StavkaTure stavkaTure = (StavkaTure)object;
                stavkaTure.snimi();
            }
        }
        
    }
    
    @FXML private void potvrdi() {
        DBBroker db = new DBBroker();
        long result = 0;

        try {
            if (porudzbinaZaSastavljanje.getID() == 0) {
                porudzbinaZaSastavljanje.snimi();
            } else {
                for (Tura tura : porudzbinaZaSastavljanje.getTure()) {
                    if (tura.listStavkeTure.size()>0) {
                        HashMap<String,String> mapaTura = new HashMap();
                        mapaTura.put("brojStola", "" + porudzbinaZaSastavljanje.getBrojStolaBroj());
                        if (tura.datum == null) {
                            
                            RM_Datetime rmDatum = new RM_Datetime();
                            
                            tura.datum = rmDatum.getDate();
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
            }
            
            
        } catch (Exception e) {
        }
        
        promeniModele();
        obrisiModele();
        nazadNaPrikazSale(null);
    }
    
    private void blokirajSve(boolean blokiraj) {
        
        //ako nista nije izabrano ne blokirati
        if (porudzbinaZaSastavljanje == null) {
            return;
        }
        
        izaberiStoA.setDisable(blokiraj);
        izaberiStoB.setDisable(blokiraj);
        
        gostiStoA.setDisable(blokiraj);
        gostiStoB.setDisable(blokiraj);
        
    }
    
    public void pomeriScrollDownA() {
        scrollPaneA.setVvalue((scrollPaneA.getVvalue() + 0.5 ) * 1);
    }
    
    public void pomeriScrollUpA() {
        scrollPaneA.setVvalue((scrollPaneA.getVvalue() - 0.5 ) * 1);
    }
    
    public void pomeriScrollDownB() {
        scrollPaneB.setVvalue((scrollPaneA.getVvalue() + 0.5 ) * 1);
    }
    
    public void pomeriScrollUpB() {
        scrollPaneB.setVvalue((scrollPaneA.getVvalue() - 0.5 ) * 1);
    }
    
    public void toggleGostiA() {
                
        if (stoA.getHvalue() == 1) {
            stoA.setHvalue(0);
            return;
        }
        stoA.setHvalue((stoA.getHvalue() + 0.5 ) * 1);
    }
    
    public void toggleGostiB() {
                
        if (stoB.getHvalue() == 1) {
            stoB.setHvalue(0);
            return;
        }
        stoB.setHvalue((stoB.getHvalue() + 0.5 ) * 1);
    }
}



/*
TODO

10. Ako stignes i mozes napravi mi dijalog za meni, a ja cu da mu odradim logiku
    (pretpostavljam da ulaz treba da mu bude ukupan racun, a mozda i ne treba... )

ODRADJENO:
2. Blokiraj sve ako su odabrana oba stola - Bosko - ovo u stvari kada se prebaci prva stavka ODRADIO
3. Odblokiraj sve u skladu sa blokiraj - Bosko - ODRADIO
4. Tura.snimi() - Bosko - ODRADIO
5. updateModele implementirati za Tura - OVO NIJE POTREBNO ALI JE ODRADJENO - nikada se nece menjati nesto na turi, samo moze da se obrise ako se prebaci desno
6. obrisiModele() - Bosko - ODRADIO
9. Implementirati ODUSTANI dugme - OVO POZVATI I U INIT_DATA - BOSKO - ODRADIO
8. promeniti natpis na Nova tura - ODRADIO
1. Obrisati dugmice za sast/rast za slozene stavke - ODRADIO
7. disable prebacivanje nazad - ODRADIO
*/