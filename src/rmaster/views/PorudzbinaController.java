/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import rmaster.assets.FXMLDocumentController;
import rmaster.assets.RM_TableView.RM_TableView;
import rmaster.assets.RM_TableView.RavnanjeKolone;
import rmaster.assets.RM_TableView.SirinaKolone;
import rmaster.ScreenController;
import rmaster.assets.ScreenMap;
import rmaster.assets.Stampa;
import rmaster.assets.Utils;
import rmaster.assets.items.ArtikalButton;
import rmaster.assets.RM_Button.RM_Button;
import static rmaster.assets.items.VrsteGrupaIliArtikal.*;
import rmaster.models.Artikal.Artikal_Dodatni;
import rmaster.models.Artikal.Artikal_Opisni;
import rmaster.models.Artikal.Artikal_Prosti;
import rmaster.models.Artikal.Podgrupa;
import rmaster.models.Artikal.Artikal_Slozeni;
import rmaster.models.Artikal.Child_Abstract;
import rmaster.models.Gost;
import rmaster.models.Artikal.Grupa;
import rmaster.models.Porudzbina;
import rmaster.models.StavkaTure;
import rmaster.models.Tura;

/**
 * FXML Controller class
 *
 * @author Bosko
 */
public class PorudzbinaController extends FXMLDocumentController {

    ScreenController myController; 
     
    @Override
    public void setScreenParent(ScreenController screenParent){ 
        myController = screenParent; 
    } 
    
    private Executor exec;
    
    @FXML
    public VBox prikazRacunaGostaSadrzaj;
    
    @FXML
    public HBox ArtikalGrupe;
    
    @FXML
    public FlowPane ArtikalPodgrupe;
    
    @FXML
    public FlowPane Artikal;
    
    @FXML
    public VBox Artikal_DvaDela;
    
    @FXML
    public HBox prikazGostiju = new HBox();

    @FXML
    public ScrollPane prikazRacunaGosta;

    @FXML
    public ToggleButton buttonKolicina;
    
    @FXML
    private Label imeKonobara;
    
    @FXML
    private Label izabraniSto;
    
    @FXML
    public Button favorites;
    
    @FXML
    public Label total;
    
    @FXML
    public Label popust;
    
    @FXML
    public Label naplata;
    
    @FXML
    public Pane sekcijaDodavanjeGostiju;
    
    @FXML
    public ScrollPane prikazGostijuScrollPane;
    
    @FXML
    public Button porudzbinaNaplati;

    @FXML
    public Button porudzbinaMedjuzbir;
    
    @FXML
    public RM_TableView tabelaNovaTuraGosta;
    
    public String izabraniStoId;
    
    public String izabraniStoBroj;
    
    public String izabraniStoNaziv;

    @FXML
    private RM_Button grupaPrevious;
    
    @FXML
    private RM_Button grupaNext;
    
    @FXML
    private RM_Button podgrupaPrevious;
    
    @FXML
    private RM_Button podgrupaNext;

    @FXML
    private RM_Button artikalPrevious;
    
    @FXML
    private RM_Button artikalNext;
    
    // Sirina dela u kome se prikazuju grupe, podgrupe i artikli
    public double roditeljSirina = 592.0;
    public final double defaultVisinaDugmeta = 68.0;
        
    // Definise broj artikala, grupa i podgrupa koje se prikazuju u jednom redu
    public int prikazBrojArtikalaUJednomRedu = 4;
    // Definise opseg grupa koje se prikazuju
    public int prikazGrupaBrojRedova = 1;
    public int prikazGrupaRedniBrojPrvog = 1;
    public int prikazGrupaBrojPrikazanihPlus1 = prikazGrupaBrojRedova * prikazBrojArtikalaUJednomRedu;
    public String stilGrupa = "buttonArtikliGrupe";
    List rsGrupe = null;

    // Definise opseg podgrupa koje se prikazuju
    public int prikazPodgrupaBrojRedova = 3;
    public int prikazPodgrupaRedniBrojPrvog = 1;
    public int prikazPodgrupaBrojPrikazanihPlus1 = prikazPodgrupaBrojRedova * prikazBrojArtikalaUJednomRedu;
    public String stilPodgrupa = "buttonArtikliPodgrupe";
    List rsPodgrupe = null;
    
    // Definise opseg artikala koje se prikazuju
    public int prikazArtikalBrojRedova = 5;
    public int prikazArtikalRedniBrojPrvog = 1;
    public int prikazArtikalBrojPrikazanihPlus1 = prikazArtikalBrojRedova * prikazBrojArtikalaUJednomRedu;
    public String stilArtikal = "buttonArtikli";
    List rsArtikli = null;

    // Definise opseg artikala koje se prikazuju
    public int prikazFavoriteBrojRedova = 8;
    public int prikazFavoriteRedniBrojPrvog = 1;
    public int prikazFavoriteBrojPrikazanihPlus1 = prikazFavoriteBrojRedova * prikazBrojArtikalaUJednomRedu;
    
    // Koristi se za prikaz grupe, podgrupe i artikli
    // Podesava se na pocetku f-je prikazArtikalIliGrupa
    // u zavisnosti sta se prikazuje
    public int prikazBrojRedova = 1;
    public int prikazRedniBrojPrvog = 1;
    public int prikazBrojPrikazanihPlus1 = prikazBrojRedova * prikazBrojArtikalaUJednomRedu;
    public String stilArtikalIliGrupa = "";

    public String selektovanaGlavnaGrupaID = "0";
    public String selektovanaPodgrupaID = "0";
    public String selektovanArtikalID = "0";
    
    public String stilButtonGrupeSelektovana = "buttonGrupeSelektovana";
    public String stilArtikliPrazanButton = "buttonArtikliPrazan";
    public String stilArtikliGrupeNextPrev = "buttonArtikliGrupeNextPrev";

    //Lokalne varijable 
    public String idTrenutnoIzabranogGosta;
    
    ToggleGroup gostiButtonGroup = new ToggleGroup();

    public int popustTrenutnogGosta = 0;
    
    public Map<String,List> racuniStolaPoGostima = new HashMap<>();
    
    public List<Node> listaTabela = new VirtualFlow.ArrayLinkedList<>();
            
    private List<Porudzbina> porudzbineStola = new ArrayList<Porudzbina>();
    
    Integer[] sirinaKolonaTabele = {0, 0, 305, 0, 40, 0, 58, 0, 0, 0};
    
    List<Map<String, String>> tureTrenutnoIzabranogGosta = new ArrayList<>();
    
    List<Tura> listTure = new ArrayList<>();
    Tura novaTura;
    Porudzbina porudzbinaTrenutna = null;
    
    ArtikalButton selektovani = null;
    StavkaTure selektovana = null;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        prikazRacunaGosta.setHbarPolicy(ScrollBarPolicy.NEVER);

        listaTabela = prikazRacunaGostaSadrzaj.getChildren();
       
         for (Node novaTabela : prikazRacunaGostaSadrzaj.getChildren()) {
             
             if (!(novaTabela instanceof RM_TableView)) {
                 continue;
             }
             
             
             ((RM_TableView) novaTabela).addRavnjanje(
                    new RavnanjeKolone(7, RavnanjeKolone.ALIGN_RIGHT),
                    new RavnanjeKolone(5, RavnanjeKolone.ALIGN_LEFT)            
            );
            
            ((RM_TableView) novaTabela).setSirineKolona(
                new SirinaKolone(3, sirinaKolonaTabele[2]),
                new SirinaKolone(5, sirinaKolonaTabele[4]),
                new SirinaKolone(7, sirinaKolonaTabele[6])
            );
         }

    } 
    
        
    @Override
    public void initData(Object data) {
        if (data != null) {
            HashMap<String, String> stoMap = (HashMap) data;

            izabraniStoId = stoMap.get("stoId");

            izabraniStoBroj = stoMap.get("stoBroj");

            izabraniStoNaziv = stoMap.get("stoNaziv");

            imeKonobara.setText(getUlogovaniKonobarIme());

            izabraniSto.setText("Sto: " + izabraniStoNaziv);

            this.porudzbineStola.clear();
            this.porudzbinaTrenutna = null;
            this.novaTura = null;

            this.prikazGostiju.getChildren().clear();

            this.total.setText("0.00");

            exec = Executors.newCachedThreadPool(runnable -> {
                Thread t = new Thread(runnable);
                t.setDaemon(true);
                return t ;
            });

            new Thread() {
                @Override
                public void start() {
                    prikaziFavorite(0);
                }
            }.start();

            new Thread() {
                @Override
                public void start() {
                    prikaziPorudzbinu();
                }
            }.start();

            new Thread() {
                @Override
                public void start() {
                    prikaziGrupe(0);
                }
            }.start();
        }
    }
    
    private void prikaziFavorite(int offset) {
       
        RM_Button novoDugme = (RM_Button)findNodeById(
                   ArtikalPodgrupe.getChildren(), 
                    "podgrupa_12"
            );

        novoDugme.setVisible(true);
        novoDugme.setManaged(true);
        
        podgrupaNext.setVisible(false);
        podgrupaNext.setManaged(false);
        podgrupaPrevious.setVisible(false);
        podgrupaPrevious.setManaged(false);
        
        for (int i = 0; i < 12; i++) {
            
            RM_Button dugmeArtikal = (RM_Button)findNodeById(
                   ArtikalPodgrupe.getChildren(), 
                    "podgrupa_" + (i + 1)
            );
            
            if (offset + i >= rmaster.RMaster.favouriteArtikli.artikli.size()) {
                dugmeArtikal.setVisible(false);
                continue;
            }
            
            dugmeArtikal.setPodatak( rmaster.RMaster.favouriteArtikli.artikli.get(offset + i));
            
            dugmeArtikal.setText(rmaster.RMaster.favouriteArtikli.artikli.get(offset + i).naziv);
            
            //dodaje akciju artiklu
            dodajAkcijuArtikla(dugmeArtikal, rmaster.RMaster.favouriteArtikli.artikli.get(offset + i));

            if (!dugmeArtikal.getStyleClass().contains("ArtikalStil")) {

                setujStill(dugmeArtikal, "ArtikalStil");

            }
            
            
            dugmeArtikal.setVisible(true);
        }
        
        for (int i = 12; i < 31; i++) {
             RM_Button dugmeArtikal = (RM_Button)findNodeById(
                   Artikal.getChildren(), 
                    "artikal_" + (i - 11)
            );

            if (offset + i >= rmaster.RMaster.favouriteArtikli.artikli.size()) {
                dugmeArtikal.setVisible(false);
                continue;
            }

            dugmeArtikal.setPodatak( rmaster.RMaster.favouriteArtikli.artikli.get(offset + i));
            
            dugmeArtikal.setText(rmaster.RMaster.favouriteArtikli.artikli.get(offset + i).naziv);
            
            dodajAkcijuArtikla(dugmeArtikal, rmaster.RMaster.favouriteArtikli.artikli.get(offset + i));

            if (!dugmeArtikal.getStyleClass().contains("ArtikalStil")) {

                setujStill(dugmeArtikal, "ArtikalStil");

            }
            dugmeArtikal.setVisible(true);
        }

        artikalNext.setPodatak("");
        artikalPrevious.setPodatak("");
        
        if (rmaster.RMaster.favouriteArtikli.artikli.size() > offset + 31) {
            artikalNext.setPodatak(offset + 31);
            artikalNext.setVrsta("FAV");
        }
            
        if (offset - 31 >= 0) {
            artikalPrevious.setPodatak(offset - 31);
            artikalPrevious.setVrsta("FAV");
        }
        
        
        //todo set podgrupa Next i podgrupa previous 
        artikalPrevious.setDisable(offset == 0);
        artikalNext.setDisable(rmaster.RMaster.favouriteArtikli.artikli.size() <= offset + 31);

    }
    
    @FXML
    public void prikaziFavouriteAction(ActionEvent event) {
        setujSveGrupeStill("");
        prikaziFavorite(0);
    }
    
    private void prikaziGrupe(int offset) {
           
        int i = 0;
        for(; i < 3; i++) {
            
            RM_Button dugmeGrupe = (RM_Button)findNodeById(
                    ArtikalGrupe.getChildren(), 
                    "grupa_" + (i+1)
            );

            if (offset + i >= rmaster.RMaster.grupeArtikala.size()) {
                dugmeGrupe.setVisible(false);
                continue;
            }
            
            dugmeGrupe.setVisible(true);

            dugmeGrupe.setPodatak(rmaster.RMaster.grupeArtikala.get(i + offset));
            
            dugmeGrupe.setText(rmaster.RMaster.grupeArtikala.get(i + offset).naziv);

            if (!dugmeGrupe.getStyleClass().contains("GrupaPodgrupaStill")) {

                setujStill(dugmeGrupe, "GrupaPodgrupaStill");

            }  
            
            setGroupButtonAction(dugmeGrupe);
            
        }
        
        for (; i < 3; i++) {
            RM_Button dugmeGrupe = (RM_Button)findNodeById(
                    ArtikalGrupe.getChildren(), 
                    "grupa_" + (i+1)
            );
            dugmeGrupe.setDisable(true);
        }
            
        
        grupaNext.setPodatak("");
        grupaPrevious.setPodatak("");
        
        if (rmaster.RMaster.grupeArtikala.size() > offset + 3) {
            grupaNext.setPodatak(offset + 3);
        }
            
        if (offset - 3 >= 0) {
            grupaPrevious.setPodatak(offset - 3);
        }
        
        grupaPrevious.setDisable(offset == 0);
        grupaNext.setDisable(rmaster.RMaster.grupeArtikala.size() <= offset + 3);
    }
    
    private void setGroupButtonAction(RM_Button dugmeGrupe) {
        
        dugmeGrupe.setOnAction(new EventHandler<ActionEvent>() {               
                                    @Override public void handle(ActionEvent event) {
                                        RM_Button pressedButton = (RM_Button)event.getSource();
                                        Grupa izabranaGrupa = (Grupa)pressedButton.getPodatak();
                                        
                                        pressedButton.getStyleClass().add(
                                                "GrupaPodgrupaSelected"
                                        );
                                        
                                        setujSveGrupeStill(pressedButton.getId());
                                        
                                        setujSvePodgrupeStill("");
                                        
                                        if (izabranaGrupa.podgrupe.isEmpty()) {
                                            prikaziArtikleGrupeKojaNemaPodgrupe(izabranaGrupa, 0);
                                            return;
                                        }
                                        
                                        prikaziPodgrupe(izabranaGrupa, 0);
                                        prikaziArtikleGrupe(izabranaGrupa, 0);
                                    }
                                });
    }
    
    @FXML
    public void grupaPrevious(ActionEvent event) {
        String podatak = grupaPrevious.getPodatak() + "";
        
        if(podatak.isEmpty()) {
            return;
        }
        
        int offset = Integer.parseInt(grupaPrevious.getPodatak() + "");
                
        setujSveGrupeStill("");
        
        prikaziGrupe(offset);
    }
    
    @FXML
    public void grupaNext(ActionEvent event) {
        String podatak = grupaNext.getPodatak() + "";
        
        if(podatak.isEmpty()) {
            return;
        }
        
        int offset = Integer.parseInt(grupaNext.getPodatak() + "");
        
        setujSveGrupeStill("");
                
        prikaziGrupe(offset);
    }
    
    @FXML
    public void podgrupaPrevious(ActionEvent event) {
        String podatak = podgrupaPrevious.getPodatak() + "";
        
        if(podatak.isEmpty()) {
            return;
        }
        
        int offset = Integer.parseInt(podgrupaPrevious.getPodatak() + "");

        Object model = podgrupaPrevious.getVrsta();

        if (model instanceof Grupa) {
                    
            Grupa izabranaGrupa = (Grupa)model;

            prikaziPodgrupe(izabranaGrupa, offset);
            
            setujSvePodgrupeStill("");
            
            return;
        }
        
        if (model instanceof Artikal_Slozeni) {
            //prikaz slozenog artikla
            Artikal_Slozeni artikal = (Artikal_Slozeni)model;
            
            prikaziSlozeniArtikal(artikal, offset);
        }
        
    }
    
    @FXML
    public void podgrupaNext(ActionEvent event) {
        String podatak = podgrupaNext.getPodatak() + "";
        
        if(podatak.isEmpty()) {
            return;
        }
        
        int offset = Integer.parseInt(podgrupaNext.getPodatak() + "");

        Object model = podgrupaNext.getVrsta();

        if (model instanceof Grupa) {

            Grupa izabranaGrupa = (Grupa)model;

            prikaziPodgrupe(izabranaGrupa, offset);
            
            setujSvePodgrupeStill("");
            
            return;
        }
        
        if (model instanceof Artikal_Slozeni) {
            //todo
            Artikal_Slozeni artikal = (Artikal_Slozeni)model;
            
            prikaziSlozeniArtikal(artikal, offset);
        }
        
    }
    
    @FXML
    public void artikalPrevious(ActionEvent event) {
        String podatak = artikalPrevious.getPodatak() + "";
        Object model = artikalPrevious.getVrsta();
        
        if(podatak.isEmpty()) {
            return;
        }
        
        int offset = Integer.parseInt(artikalPrevious.getPodatak() + "");
           
        if(model instanceof String) 
        {
            //favourites
            prikaziFavorite(offset);
            return;
        }
        
        if(model instanceof Grupa) {
            //prikaz grupe
            
            if (((Grupa) model).podgrupe.isEmpty()) {
                prikaziArtikleGrupeKojaNemaPodgrupe((Grupa)model, offset);
                return;
            }
            
            prikaziArtikleGrupe((Grupa) model, offset);
            
            return;
        }

        if(model instanceof Podgrupa) {
            //prikaz artikala podgrupe
            prikaziArtiklePodgrupe((Podgrupa)model, offset);
            return;
        }
        
        if(model instanceof Artikal_Slozeni) {
            //prikaz slozenog artikala
            prikaziSlozeniArtikal((Artikal_Slozeni)model, offset);
        }  
    }
    
    @FXML
    public void artikalNext(ActionEvent event) {
        String podatak = artikalNext.getPodatak() + "";
        Object model = artikalNext.getVrsta();

        if(podatak.isEmpty()) {
            return;
        }
        
        int offset = Integer.parseInt(artikalNext.getPodatak() + "");
        
        if(model instanceof String) 
        {
            //favourites
            prikaziFavorite(offset);
            return;
        }
        
        if(model instanceof Grupa) {
            //prikaz grupe
             if (((Grupa) model).podgrupe.isEmpty()) {
                prikaziArtikleGrupeKojaNemaPodgrupe((Grupa)model, offset);
                return;
            }
            
            prikaziArtikleGrupe((Grupa) model, offset);
            return;
        }

        if(model instanceof Podgrupa) {
            //prikaz artikala podgrupe
            prikaziArtiklePodgrupe((Podgrupa)model, offset);
            return;
        }
        
        if(model instanceof Artikal_Slozeni) {
            //prikaz slozenog artikala
            prikaziSlozeniArtikal((Artikal_Slozeni)model, offset);
        } 
    }
    
    private void prikaziPodgrupe(Grupa izabranaGrupa, int offset) {
        
        if (izabranaGrupa.podgrupe.isEmpty()) {
            return;
        }
        
        for (int i = 0; i < 12; i++) {
            
            RM_Button dugmePodgrupa = (RM_Button)findNodeById(
                   ArtikalPodgrupe.getChildren(), 
                    "podgrupa_" + (i + 1)
            );
            
            if (offset + i >= izabranaGrupa.podgrupe.size()) {
                dugmePodgrupa.setVisible(false);
                continue;
            }
            
            dugmePodgrupa.setVisible(true);

            dugmePodgrupa.setPodatak(izabranaGrupa.podgrupe.get(offset + i));
            
            if (!dugmePodgrupa.getStyleClass().contains("GrupaPodgrupaStill")) {

                setujStill(dugmePodgrupa, "GrupaPodgrupaStill");

            }  
                        
            dugmePodgrupa.setText(izabranaGrupa.podgrupe.get(offset + i).naziv);
            
            setPodgrupaButtonAction(dugmePodgrupa);
        }

        setPodgrupaNextAndPrevious(izabranaGrupa, offset);
    }
    
    private void setPodgrupaButtonAction(RM_Button podgrupaDugme) {
         podgrupaDugme.setOnAction(new EventHandler<ActionEvent>() {               
                                    @Override public void handle(ActionEvent event) {
                                        RM_Button pressedButton = (RM_Button)event.getSource();
                                        Podgrupa izabranaPodgrupa = (Podgrupa)pressedButton.getPodatak();
                                        
                                        pressedButton.getStyleClass().add("GrupaPodgrupaSelected");
                                        
                                        setujSvePodgrupeStill(pressedButton.getId());
                                        
                                        prikaziArtiklePodgrupe(izabranaPodgrupa, 0);
                                    }
                                });
    }
    
    private void setujStill(RM_Button dugme, String stil) 
    {
        
        dugme.getStyleClass().clear();
        
        dugme.getStyleClass().add("button");
        
        dugme.getStyleClass().add(stil);
    }
    
    private void setujSveGrupeStill(String skipStil) 
    {
        for(int i = 0; i < 3; i++) {
            
            RM_Button dugmeGrupa = (RM_Button)findNodeById(
                   ArtikalGrupe.getChildren(), 
                    "grupa_" + (i + 1)
            );
            
            if (dugmeGrupa.getId().equals(skipStil)) {
                continue;
            }

            dugmeGrupa.getStyleClass().clear();

            dugmeGrupa.getStyleClass().add("button");    
            
            dugmeGrupa.getStyleClass().add("GrupaPodgrupaStill");
        }
    }
    
    private void setujSvePodgrupeStill(String skipStil)
    {
         for(int i = 0; i < 12; i++) {
            
            RM_Button podgrupa = (RM_Button)findNodeById(
                   ArtikalPodgrupe.getChildren(), 
                    "podgrupa_" + (i + 1)
            );
            
            if (podgrupa.getId().equals(skipStil)) {
                continue;
            }

            podgrupa.getStyleClass().clear();

            podgrupa.getStyleClass().add("button");    
            
            podgrupa.getStyleClass().add("GrupaPodgrupaStill");
        }
    }
    
    private void prikaziArtiklePodgrupe(Podgrupa izabranaPodgrupa, int offset) 
    {
        for (int i = 0; i < 19; i++) {
             RM_Button dugmeArtikal = (RM_Button)findNodeById(
                   Artikal.getChildren(), 
                    "artikal_" + (i + 1)
            );

            if (offset + i >= izabranaPodgrupa.artikli.size()) {
                dugmeArtikal.setVisible(false);
                continue;
            }

            dugmeArtikal.setPodatak(izabranaPodgrupa.artikli.get(offset + i));
            
            dugmeArtikal.setText(izabranaPodgrupa.artikli.get(offset + i).naziv);
            
             //dodaje akciju svakom artiklu
            dodajAkcijuArtikla(dugmeArtikal, izabranaPodgrupa.artikli.get(offset + i));
            
            if (!dugmeArtikal.getStyleClass().contains("ArtikalStil")) {

                setujStill(dugmeArtikal, "ArtikalStil");

            } 
            
            dugmeArtikal.setVisible(true);
        }

        artikalNext.setPodatak("");
        artikalPrevious.setPodatak("");
        
        if (izabranaPodgrupa.artikli.size() > offset + 19) {
            artikalNext.setPodatak(offset + 19);
            artikalNext.setVrsta(izabranaPodgrupa);
        }
            
        if (offset - 19 >= 0) {
            artikalPrevious.setPodatak(offset - 19);
            artikalPrevious.setVrsta(izabranaPodgrupa);
        }
        
        artikalPrevious.setDisable(offset == 0);
        artikalNext.setDisable(izabranaPodgrupa.artikli.size() <= offset + 19);
    }
    
    
    
    private void prikaziArtikleGrupe(Grupa izabranaGrupa, int offset) 
    {
        for (int i = 0; i < 19; i++) {
             RM_Button dugmeArtikal = (RM_Button)findNodeById(
                   Artikal.getChildren(), 
                    "artikal_" + (i + 1)
            );

            if (offset + i >= izabranaGrupa.artikli.size()) {
                dugmeArtikal.setVisible(false);
                continue;
            }

            dugmeArtikal.setPodatak(izabranaGrupa.artikli.get(offset + i));
            
            //dodaje akciju artikla
            dodajAkcijuArtikla(dugmeArtikal, izabranaGrupa.artikli.get(offset + i));
            
            dugmeArtikal.setText(izabranaGrupa.artikli.get(offset + i).naziv);

            if (!dugmeArtikal.getStyleClass().contains("ArtikalStil")) {

                setujStill(dugmeArtikal, "ArtikalStil");

            } 

            dugmeArtikal.setVisible(true);
        }

        artikalNext.setPodatak("");
        artikalPrevious.setPodatak("");
        
        if (izabranaGrupa.artikli.size() > offset + 19) {
            artikalNext.setPodatak(offset + 19);
            artikalNext.setVrsta("FAV");
        }
            
        if (offset - 19 >= 0) {
            artikalPrevious.setPodatak(offset - 19);
            artikalPrevious.setVrsta("FAV");
        }
        
        
        //todo set podgrupa Next i podgrupa previous 
        artikalPrevious.setDisable(offset == 0);
        artikalNext.setDisable(izabranaGrupa.artikli.size() <= offset + 19);
    }
    
    public void prikaziSlozeniArtikal(Artikal_Slozeni slozeniArtikal, int offset)
    {   
        for (int i = 0; i < 12; i++) {
            
            RM_Button dugmeArtikal = (RM_Button)findNodeById(
                   ArtikalPodgrupe.getChildren(), 
                    "podgrupa_" + (i + 1)
            );
            
            if (offset + i >= slozeniArtikal.opisniArtikli.size()) {
                dugmeArtikal.setVisible(false);
                continue;
            }
            
            dugmeArtikal.setVisible(true);

            dugmeArtikal.setPodatak(slozeniArtikal.opisniArtikli.get(offset + i));
            dugmeArtikal.setText(slozeniArtikal.opisniArtikli.get(offset + i).naziv);
            
            //dodaje akciju opisnom artiklu    
            dodajAkcijuArtikla(dugmeArtikal, slozeniArtikal.opisniArtikli.get(offset + i));

            if (!dugmeArtikal.getStyleClass().contains("ArtikalStil")) {

                setujStill(dugmeArtikal, "ArtikalStil");

            } 
        }
        
        podgrupaNext.setPodatak("");
        podgrupaNext.setVrsta(slozeniArtikal);
        
        podgrupaPrevious.setPodatak("");
        podgrupaPrevious.setVrsta(slozeniArtikal);
        
        if (slozeniArtikal.opisniArtikli.size() > offset + 11) {
            podgrupaNext.setPodatak(offset + 11);
        }
            
        if (offset - 11 >= 0) {
            podgrupaPrevious.setPodatak(offset - 11);
        }

        podgrupaPrevious.setDisable(offset == 0);
        podgrupaNext.setDisable(slozeniArtikal.opisniArtikli.size() <= offset + 11);

 
        for (int i = 0; i < 19; i++) {
             RM_Button dugmeArtikal = (RM_Button)findNodeById(
                   Artikal.getChildren(), 
                    "artikal_" + (i+1)
            );

            if (offset + i >= slozeniArtikal.dodatniArtikli.size()) {
                dugmeArtikal.setVisible(false);
                continue;
            }
            
            dugmeArtikal.setVisible(true);

            dugmeArtikal.setPodatak(slozeniArtikal.dodatniArtikli.get(offset + i));
            
            dugmeArtikal.setText(slozeniArtikal.dodatniArtikli.get(offset + i).naziv);
            
            if (!dugmeArtikal.getStyleClass().contains("ArtikalStil")) {

                setujStill(dugmeArtikal, "ArtikalStil");

            } 
            //dodaje akciju dodatnom artiklu    
            dodajAkcijuArtikla(dugmeArtikal, slozeniArtikal.dodatniArtikli.get(offset + i));
        }
        
        artikalNext.setPodatak("");
        artikalPrevious.setPodatak("");
        
        if (slozeniArtikal.dodatniArtikli.size() > offset + 19) {
            artikalNext.setPodatak(offset + 19);
            artikalNext.setVrsta(slozeniArtikal);
        }
            
        if (offset - 19 >= 0) {
            artikalPrevious.setPodatak(offset - 19);
            artikalPrevious.setVrsta(slozeniArtikal);
        }
        
        artikalPrevious.setDisable(offset == 0);
        artikalNext.setDisable(slozeniArtikal.dodatniArtikli.size() <= offset + 19);
        
        //todo dodati opisni next i dodatni next
    }
    
    public void dodajAkcijuArtikla(RM_Button dugmeArtikal, Child_Abstract artikal) 
    {
        dugmeArtikal.setPodatak(artikal);

        if (artikal instanceof Artikal_Slozeni) {
            
            akcijaZaSlozeniArtikal(dugmeArtikal);
        }
        
        if (artikal instanceof Artikal_Prosti) {
            
            akcijaZaProstiArtikal(dugmeArtikal);
        }
        
        if (artikal instanceof Artikal_Dodatni) {
            
            akcijaZaDodatniArtikal(dugmeArtikal);
        }
        
        if (artikal instanceof Artikal_Opisni) {
            
            akcijaZaOpisniArtikal(dugmeArtikal);
        }
    }
    
    private void akcijaZaSlozeniArtikal(RM_Button artikalDugme) 
    {
        artikalDugme.setOnAction(new EventHandler<ActionEvent>() {               
                                    @Override public void handle(ActionEvent event) {
                                        
                                        RM_Button pressedButton = (RM_Button)event.getSource();
                                        
                                        Artikal_Slozeni slozeniArtikal = 
                                                (Artikal_Slozeni)pressedButton.getPodatak();
                                        
                                        dodajArtikalUNovuTuru(pressedButton);
                                        
                                        prikaziSlozeniArtikal(slozeniArtikal, 0);
                                        
                                    }
                                });
        
    }
    
    private void akcijaZaProstiArtikal(RM_Button artikalDugme) 
    {
        artikalDugme.setOnAction(new EventHandler<ActionEvent>() {               
                                    @Override public void handle(ActionEvent event) {
                                        
                                        RM_Button pressedButton = (RM_Button)event.getSource();
                                        
                                        dodajArtikalUNovuTuru(pressedButton);
                                        
                                    }
                                });
    }
    
    private void akcijaZaDodatniArtikal(RM_Button artikalDugme)
    {
        artikalDugme.setOnAction(new EventHandler<ActionEvent>() {               
                                    @Override public void handle(ActionEvent event) {
                                        
                                        RM_Button pressedButton = (RM_Button)event.getSource();
                                        
                                       dodajOpisniDodatniArtikalUStavkuTure(selektovana, pressedButton);
                                    }
                                });
    }
    
    private void akcijaZaOpisniArtikal(RM_Button artikalDugme)
    {
        artikalDugme.setOnAction(new EventHandler<ActionEvent>() {               
                                    @Override public void handle(ActionEvent event) {
                                        
                                        RM_Button pressedButton = (RM_Button)event.getSource();
                                        dodajOpisniDodatniArtikalUStavkuTure(selektovana, pressedButton);

                                    }
                                });
    }
    
    private void setPodgrupaNextAndPrevious(Grupa izabranaGrupa, int offset) {
        
        RM_Button novoDugme = (RM_Button)findNodeById(
                   ArtikalPodgrupe.getChildren(), 
                    "podgrupa_12"
            );

        novoDugme.setVisible(false);
        novoDugme.setManaged(false);
        
        podgrupaPrevious.setManaged(true);
        podgrupaPrevious.setVisible(true);
        podgrupaNext.setManaged(true);
        podgrupaNext.setVisible(true);
            
        podgrupaNext.setPodatak("");
        podgrupaNext.setVrsta(izabranaGrupa);
        
        podgrupaPrevious.setPodatak("");
        podgrupaPrevious.setVrsta(izabranaGrupa);
        
        if (izabranaGrupa.podgrupe.size() > offset + 11) {
            podgrupaNext.setPodatak(offset + 11);
        }
            
        if (offset - 11 >= 0) {
            podgrupaPrevious.setPodatak(offset - 11);
        }

        podgrupaPrevious.setDisable(offset == 0);
        podgrupaNext.setDisable(izabranaGrupa.podgrupe.size() <= offset + 11);
    }
    
    private void prikaziArtikleGrupeKojaNemaPodgrupe(Grupa izabranaGrupa, int offset) {
         
        RM_Button novoDugme = (RM_Button)findNodeById(
                   ArtikalPodgrupe.getChildren(), 
                    "podgrupa_12"
            );

        novoDugme.setVisible(true);
        novoDugme.setManaged(true);
        
        podgrupaNext.setVisible(false);
        podgrupaNext.setManaged(false);
        podgrupaPrevious.setVisible(false);
        podgrupaPrevious.setManaged(false);
        
        for (int i = 0; i < 12; i++) {
            
            RM_Button dugmeArtikal = (RM_Button)findNodeById(
                   ArtikalPodgrupe.getChildren(), 
                    "podgrupa_" + (i + 1)
            );
            
            if (offset + i >= izabranaGrupa.artikli.size()) {
                dugmeArtikal.setVisible(false);
                continue;
            }
            
            dugmeArtikal.setVisible(true);
            
            dugmeArtikal.setPodatak(izabranaGrupa.artikli.get(offset + i));

            dugmeArtikal.setText(izabranaGrupa.artikli.get(offset + i).naziv);

            dodajAkcijuArtikla(dugmeArtikal, izabranaGrupa.artikli.get(offset + i));
            
            if (!dugmeArtikal.getStyleClass().contains("ArtikalStil")) {

                setujStill(dugmeArtikal, "ArtikalStil");

            } 
                        
        }
        
        for (int i = 12; i < 31; i++) {
             RM_Button dugmeArtikal = (RM_Button)findNodeById(
                   Artikal.getChildren(), 
                    "artikal_" + (i - 11)
            );

            if (offset + i >= izabranaGrupa.artikli.size()) {
                dugmeArtikal.setVisible(false);
                continue;
            }

            dugmeArtikal.setPodatak(izabranaGrupa.artikli.get(offset + i));
            
            dugmeArtikal.setText(izabranaGrupa.artikli.get(offset + i).naziv);
            
            if (!dugmeArtikal.getStyleClass().contains("ArtikalStil")) {

                setujStill(dugmeArtikal, "ArtikalStil");

            } 
            dodajAkcijuArtikla(dugmeArtikal, izabranaGrupa.artikli.get(offset + i));
            
            dugmeArtikal.setVisible(true);
        }

        artikalNext.setPodatak("");
        artikalPrevious.setPodatak("");
        
        if (izabranaGrupa.artikli.size() > offset + 31) {
            artikalNext.setPodatak(offset + 31);
            artikalNext.setVrsta("FAV");
        }
            
        if (offset - 31 >= 0) {
            artikalPrevious.setPodatak(offset - 31);
            artikalPrevious.setVrsta("FAV");
        }
        
        
        //todo set podgrupa Next i podgrupa previous 
        artikalPrevious.setDisable(offset == 0);
        artikalNext.setDisable(izabranaGrupa.artikli.size() <= offset + 31);
        
    }
    
    public void prikaziPorudzbinu() {

        Task<List> porudzbinaTask = new Task<List>() {
            @Override
            public List call() throws Exception {
               List racuniStola = DBBroker.get_PorudzbineStola(izabraniStoId); 
               return racuniStola;
            }
        };
            
        porudzbinaTask.setOnSucceeded(e -> 
            prikaziPorudzbinuTask(porudzbinaTask.getValue()));

        exec.execute(porudzbinaTask);
    }
    
    
    private RadioButton prikaziPorudzbinuTaskSetButtonAction(
            RadioButton noviGostButton, 
            String brojNovogGosta)
    {
            noviGostButton.getStyleClass().remove("radio-button");
            noviGostButton.getStyleClass().add("toggle-button");

            noviGostButton.setToggleGroup(gostiButtonGroup);

            noviGostButton.setId(brojNovogGosta);

            noviGostButton.setPrefSize(50, 50);
            
            noviGostButton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    String gost = ((RadioButton)e.getSource()).getId();
                                    Utils.postaviStil_ObrisiZaOstaleKontroleRoditelja(e, stilButtonGrupeSelektovana);

                                    for (Porudzbina porudzbina : porudzbineStola) {
                                        if (porudzbina.getGost().getGostID() == Long.parseLong(gost)) {
                                            porudzbinaTrenutna = porudzbina;
                                            prikaziPorudzbinu(porudzbinaTrenutna);
                                            novaTura = null;
                                            for (Tura tura : porudzbinaTrenutna.getTure()) {
                                                if (tura.getTuraID() == 0) {
                                                    novaTura = tura;
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            });
            
            return noviGostButton;
    }
    
    public void prikaziPorudzbinuTask(List racuniStola)
    {        
        if (racuniStola.isEmpty())
        {
            //TODO: Dodaj prvog gosta i napravi porudzbinu za njega
            dodajNovogGosta(new ActionEvent());
            return;
        }
        
        
        List<Node> lista = new ArrayList<>();
        
        for (Object racun : racuniStola) {
            
            Map<String, String> red = (Map<String, String>) racun;

            String brojNovogGosta = red.get("gost");

            porudzbinaTrenutna = new Porudzbina(
                    new Gost(brojNovogGosta), 
                    red.get("id"),
                    izabraniStoId,
                    izabraniStoBroj
            );

            porudzbineStola.add(porudzbinaTrenutna);

            RadioButton noviGostButton = new RadioButton(brojNovogGosta);

            lista.add(prikaziPorudzbinuTaskSetButtonAction(noviGostButton, brojNovogGosta));
        } 
        
        ObservableList<Node> listaDugmica = FXCollections.observableArrayList(lista);            
                
        this.prikazGostiju.getChildren().addAll(listaDugmica);
            
        prikazGostijuScrollPane.setContent(prikazGostiju);
           
        RadioButton dugme = (RadioButton)prikazGostiju.getChildren().get(0);
        dugme.fire();
    }
    
    public void prikaziPorudzbinu(Porudzbina porudzbina) 
    {
        
        sakrijSveTabele();
 
        List<Node> listaSadrzaja = prikazRacunaGostaSadrzaj.getChildren();

        prikazRacunaGosta.setFitToWidth(true);
                                                
        prikazRacunaGostaSadrzaj.setPadding(new Insets(0, 0, 0, 0));
        
        int brojac = 0;
        
        for (Tura tura : porudzbina.getTure()) {
            
            RM_TableView novaTabela = new RM_TableView();
            RM_Button ponoviTuru = new RM_Button();
            
            brojac++;
            
            if (brojac < 12) {
                novaTabela = (RM_TableView) findNodeById(listaSadrzaja, "tura" + brojac);
                prikaziKomponentu(novaTabela);
                ponoviTuru = (RM_Button) findNodeById(listaSadrzaja, "ponoviTura" + brojac);
                prikaziKomponentu(ponoviTuru);
            } 
            
            novaTabela.setSelectionModel(null);
            
            if (tura.getTuraID() == 0) {
                novaTura = tura;
            }
            

            novaTabela.setPodaci(
                    tura.dajTuru()
            );
            

            ponoviTuru.setPrefSize(403, 40);
            try {
                String period = Utils.getDateDiff(tura.getVremeTure(), new Date(), TimeUnit.MINUTES);
                ponoviTuru.setText("Ponovi Turu (" + period + ")");
            } catch (Exception e) {
                ponoviTuru.setText("Ponovi Turu");
            }
            ponoviTuru.setPodatak("" + tura.getTuraID());
            ponoviTuru.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {
                                String turaId = ((RM_Button)e.getSource()).getPodatak() + "";
                                ponoviTuru(turaId);
                            }
                        }); 
            
            if (brojac > 12) {
                prikazRacunaGostaSadrzaj.getChildren().add(novaTabela);
                prikazRacunaGostaSadrzaj.getChildren().add(ponoviTuru);
            }
        }
        
        this.prikaziTotalPopustNaplataPorudzbina(porudzbina);
    }

    public void ponoviTuru(String izabranaTuraIdString) {
        if (porudzbinaTrenutna.getBlokiranaPorudzbina()) {
            return;
        }
        //OVDE SE SADA PONAVLJA TURA
        long izabranaTuraID = Long.parseLong(izabranaTuraIdString);
        novaTura = null;
        
        sakrijSveTabele();

        for (Tura tura : porudzbinaTrenutna.getTure()) {
            if (tura.turaID == izabranaTuraID) {
                novaTura = tura.getClone(izabranaTuraID);
                break;
            }
        }
        if (novaTura != null) {
            porudzbinaTrenutna.getTure().add(novaTura);
        }

        if (novaTura != null) {
            
            tabelaNovaTuraGosta.setPodaci(
                        novaTura.dajTuru()
                );
            tabelaNovaTuraGosta.getSelectionModel().select(novaTura.listStavkeTure.size() - 1);
            
            prikaziKomponentu(tabelaNovaTuraGosta);
            this.prikaziTotalPopustNaplataTura(novaTura);
        }
    }

    public void prikaziSalu(ActionEvent event) {
        novaTura = null;
        sakrijSveTabele();
        myController.setScreen(ScreenMap.PRIKAZ_SALA, null);
    }
    
    public void dodajOpisniDodatniArtikalUStavkuTure(StavkaTure poslednjaDodataStavka, RM_Button artikalOpisniDodatni) {
        if (porudzbinaTrenutna.getBlokiranaPorudzbina()) {
            return;
        }
//        prikazRacunaGosta.setContent(null);
        
        sakrijSveTabele();
        
        Map<String, String> novaGlavnaStavka = null;
        StavkaTure nova = null;

        Child_Abstract artikal = (Child_Abstract)artikalOpisniDodatni.getPodatak();
        Map<String, String> novaStavkaTure = artikal.toHashMap(true);
//        novaStavkaTure.put("id", "0");//artikalOpisniDodatni.getId());
//        novaStavkaTure.put("ARTIKAL_ID", artikalOpisniDodatni.getId());
        novaStavkaTure.put("kolicina", "1");
//        novaStavkaTure.put("brojStola", "" + poslednjaDodataStavka.getBrojStola());
//        novaStavkaTure.put("dozvoljenPopust", "" + artikalOpisniDodatni.getDozvoljenPopust());
//        novaStavkaTure.put("stampacID", "" + poslednjaDodataStavka.stampacID);
        
        if (artikal instanceof Artikal_Opisni) { //artikalOpisniDodatni.getVrstaGrupaIliArtikal() == ARTIKAL_OPISNI) {
            novaStavkaTure.put("naziv", artikal.naziv); // artikalOpisniDodatni.getText());
            novaStavkaTure.put("cena", "0");
            novaStavkaTure.put("cenaJedinicna", "0");
        } else if (artikal instanceof Artikal_Dodatni) { //artikalOpisniDodatni.getVrstaGrupaIliArtikal() == ARTIKAL_DODATNI) {
            novaStavkaTure.put("naziv", artikal.naziv); // artikalOpisniDodatni.getText());
            novaStavkaTure.put("cena", artikal.cena);// Utils.getStringFromDouble(artikalOpisniDodatni.getCenaJedinicna()));
            novaStavkaTure.put("cenaJedinicna", artikal.cena);// Utils.getStringFromDouble(artikalOpisniDodatni.getCenaJedinicna()));
        }

        if (poslednjaDodataStavka.getKolicina()>1) {
            novaGlavnaStavka = new HashMap<>();
            novaGlavnaStavka.put("id", "" + poslednjaDodataStavka.id);
            novaGlavnaStavka.put("ARTIKAL_ID", poslednjaDodataStavka.getArtikalIDString());
            novaGlavnaStavka.put("brojStola", "" + poslednjaDodataStavka.getBrojStola());
            novaGlavnaStavka.put("kolicina", "1");
            novaGlavnaStavka.put("naziv", "" + poslednjaDodataStavka.naziv);
            novaGlavnaStavka.put("cena", "" + poslednjaDodataStavka.cenaJedinicna);
            novaGlavnaStavka.put("cenaJedinicna", "" + poslednjaDodataStavka.cenaJedinicna);
            novaGlavnaStavka.put("dozvoljenPopust", "" + poslednjaDodataStavka.getDozvoljenPopust());
            novaGlavnaStavka.put("procenatPopusta", "" + poslednjaDodataStavka.getProcenatPopusta());
            novaGlavnaStavka.put("stampacID", "" + poslednjaDodataStavka.stampacID);
            poslednjaDodataStavka.smanjiKolicinu();

            nova = new StavkaTure(novaGlavnaStavka);
            nova.setRedniBroj(novaTura.getRedniBrojStavkeSledeci());

            novaTura.listStavkeTure.add(nova);

            poslednjaDodataStavka = nova;
            selektovana = nova;
        }

        StavkaTure st = new StavkaTure(novaStavkaTure);
        st.setRedniBrojGlavneStavke(poslednjaDodataStavka.getRedniBroj());
        if (artikal instanceof Artikal_Opisni) { // artikalOpisniDodatni.getVrstaGrupaIliArtikal() == ARTIKAL_OPISNI) {
            poslednjaDodataStavka.dodajKolicinuArtikalOpisni(st);
        } else if (artikal instanceof Artikal_Dodatni) { //artikalOpisniDodatni.getVrstaGrupaIliArtikal() == ARTIKAL_DODATNI) {
            poslednjaDodataStavka.dodajKolicinuArtikalDodatni(st);
        }

        this.tableRefresh();
        this.prikaziTotalPopustNaplataTura(novaTura);
//        this.prikaziTotalPopustNaplataStavke(novaTura.listStavkeTure);
        this.porudzbinaNaplati.setText("Potvrdi ✓");
    }

    public void dodajArtikalUNovuTuru(RM_Button dugme) {
        if (porudzbinaTrenutna.getBlokiranaPorudzbina()) {
            return;
        }
        
        //TODO isto i ovde treba da se samo setuju sve tabele na empty i hide
//        prikazRacunaGosta.setContent(null);

        sakrijSveTabele();

        Child_Abstract artikal = (Child_Abstract)dugme.getPodatak();
        
        Map<String, String> novaStavkaTure = artikal.toHashMap(true);
        
        
        
//        String idArtikla = dugme.getId();
//        String nazivArtikla = artikal.getText();
//        String cena = String.format("%1$,.2f", artikal.getPodatak().getCenaJedinicna());
//
//        String[] imenaArgumenata = {"id"};
//        String[] vrednostiArgumenata = {idArtikla};

//        Map<String, String> novaStavkaTure = new HashMap<>();
//        novaStavkaTure.put("id", "0");//artikal.getId());
//        novaStavkaTure.put("ARTIKAL_ID", idArtikla);
//        novaStavkaTure.put("naziv", nazivArtikla);
//        novaStavkaTure.put("cena", cena);
//        novaStavkaTure.put("cenaJedinicna", cena);
//        novaStavkaTure.put("brojStola", "" + izabraniStoBroj);
//        novaStavkaTure.put("dozvoljenPopust", "" + artikal.getDozvoljenPopust());
//        novaStavkaTure.put("procenatPopusta", this.porudzbinaTrenutna.getPopustString());
//        novaStavkaTure.put("stampacID", artikal.getStampacID());
        
        this.dodajStavkuUNovuTuru(novaStavkaTure);
        
        this.tableRefresh();
        this.prikaziTotalPopustNaplataTura(novaTura);
//        this.prikaziTotalPopustNaplataStavke(novaTura.listStavkeTure);
        this.porudzbinaNaplati.setText("Potvrdi ✓");
    }
       
    private void dodajStavkuUNovuTuru(Map<String, String> novaStavka) {
        if (novaTura == null) {
            novaTura = new Tura();
            porudzbinaTrenutna.getTure().add(novaTura);
            //porudzbinaTrenutna.setNovaTuraPorudzbine(novaTura);
            
        }
        novaStavka.put("kolicina", "1");
        StavkaTure novaStavkaModel = new StavkaTure(novaStavka);

        for (StavkaTure stavka : novaTura.listStavkeTure) {
            if (novaStavkaModel.getArtikalID() == stavka.getArtikalID()) {
                // Ukoliko postoji taj artikal u turi, dodaje na njega kolicinu
                // ukoliko taj vec nema dodatne ili opisne artikle
                if (!stavka.getImaDodatneIliOpisneArtikle()) {
                    stavka.povecajKolicinuZa(novaStavkaModel.getKolicina());
                    selektovana = stavka;
                    return;
                }
            }
        }

        novaStavkaModel.setRedniBroj(++novaTura.redniBrojStavke);
        novaTura.listStavkeTure.add(novaStavkaModel);
        selektovana = novaStavkaModel;
        //novaTura.addStavkaTure(novaStavkaModel);
        
    }
    
    
    public void setKolicinaStavkeTure(ActionEvent event) {
             
        if (tabelaNovaTuraGosta.getItems().isEmpty() || porudzbinaTrenutna.getBlokiranaPorudzbina()) {
            return;
        }
        
        Map<String, String> izabraniRed = tabelaNovaTuraGosta.getSelectionModel().getSelectedItem();

        String kolicina = izabraniRed.get("kolicina");
        if (kolicina.startsWith("x"))
            kolicina = kolicina.substring(1);
        NumerickaTastaturaController tastatura = new NumerickaTastaturaController(
                "Unesite količinu", 
                "Unesite količinu", 
                false, 
                kolicina
        );
        Optional<String> result = tastatura.showAndWait();
        
        if (result.isPresent()){
            int redniBroj = Integer.parseInt(izabraniRed.get("redniBroj"));
            Double novaKolicina = Double.parseDouble(result.get());
            int redniBrojGlavnaStavka = Integer.parseInt(izabraniRed.get("redniBrojGlavnaStavka"));
            long artikalID = Long.parseLong(izabraniRed.get("artikalId"));

            if (redniBrojGlavnaStavka != -1) {
                // Menjanje kolicine ili brisanje ako je odabran dodatni ili opisni artikal
                StavkaTure stavkaTure = null;
                StavkaTure glavnaStavka = novaTura.getStavkaTureByRedniBroj(redniBrojGlavnaStavka);
                if (glavnaStavka != null) {
                    // Menjanje kolicine ili brisanje ako je odabran dodatni artikal
                    stavkaTure = glavnaStavka.getDodatniArtikalByRedniBrojDodatnog(redniBroj);
                    if ((stavkaTure != null) && (artikalID == stavkaTure.getArtikalID())) {
                        if (novaKolicina == 0)
                            glavnaStavka.getArtikliDodatni().remove(stavkaTure);
                        else
                            stavkaTure.setKolicina(novaKolicina);
                    } else {
                        // Menjanje kolicine ili brisanje ako je odabran opisni artikal
                        stavkaTure = glavnaStavka.getOpisniArtikalByRedniBrojOpisnog(redniBroj);
                        if ((stavkaTure != null) && (artikalID == stavkaTure.getArtikalID())) {
                            if (novaKolicina == 0)
                                glavnaStavka.getArtikliOpisni().remove(stavkaTure);
                            else
                                stavkaTure.setKolicina(novaKolicina);
                        }
                    }
                }
            } else {
                StavkaTure stavkaTure = novaTura.getStavkaTureByRedniBroj(redniBroj);
                if (stavkaTure != null) {
                    if (novaKolicina == 0) {
                        novaTura.listStavkeTure.remove(stavkaTure);
                    }
                    else {
                        stavkaTure.setKolicina(novaKolicina);
                    }
                }
            }

            this.tableRefresh();
            this.prikaziTotalPopustNaplataTura(novaTura);
//            this.prikaziTotalPopustNaplataStavke(novaTura.listStavkeTure);
        }
    }
    
    public void izbrisiStavkuNoveTure(ActionEvent event) {
        if (tabelaNovaTuraGosta.getItems().isEmpty() || porudzbinaTrenutna.getBlokiranaPorudzbina()) {
            return;
        }
        
        Map<String, String> izabraniRed = tabelaNovaTuraGosta.getSelectionModel().getSelectedItem();
        
        if (izabraniRed != null) {
            int redniBroj = Integer.parseInt(izabraniRed.get("redniBroj"));
            int redniBrojGlavnaStavka = Integer.parseInt(izabraniRed.get("redniBrojGlavnaStavka"));
            long artikalID = Long.parseLong(izabraniRed.get("artikalId"));

            if (redniBrojGlavnaStavka != -1) {
                // Menjanje kolicine ili brisanje ako je odabran dodatni ili opisni artikal
                StavkaTure stavkaTure = null;
                StavkaTure glavnaStavka = novaTura.getStavkaTureByRedniBroj(redniBrojGlavnaStavka);
                if (glavnaStavka != null) {
                    // Menjanje kolicine ili brisanje ako je odabran dodatni artikal
                    stavkaTure = glavnaStavka.getDodatniArtikalByRedniBrojDodatnog(redniBroj);
                    if ((stavkaTure != null) && (artikalID == stavkaTure.getArtikalID())) {
                        glavnaStavka.getArtikliDodatni().remove(stavkaTure);
                    } else {
                        // Menjanje kolicine ili brisanje ako je odabran opisni artikal
                        stavkaTure = glavnaStavka.getOpisniArtikalByRedniBrojOpisnog(redniBroj);
                        if ((stavkaTure != null) && (artikalID == stavkaTure.getArtikalID())) {
                            glavnaStavka.getArtikliOpisni().remove(stavkaTure);
                        }
                    }
                }
            } else {
                StavkaTure stavkaTure = novaTura.getStavkaTureByRedniBroj(redniBroj);
                if (stavkaTure != null) {
                    novaTura.listStavkeTure.remove(stavkaTure);
                }
            }
        } else {
            novaTura.listStavkeTure.clear();
        }

        this.tableRefresh();
        tabelaNovaTuraGosta.getSelectionModel().clearSelection();
        this.prikaziTotalPopustNaplataTura(novaTura);
//        this.prikaziTotalPopustNaplataStavke(novaTura.listStavkeTure);
    }
    
    private void tableRefresh() {
        
        List<Map<String, String>> listTura = new ArrayList<>();
        //int selektovano = tabelaNovaTuraGosta.getSelectionModel().getSelectedIndex();
        
        for (StavkaTure novaStavka : novaTura.listStavkeTure) {
            listTura.add(novaStavka.dajStavkuTure());
            for (StavkaTure stavkaDodatni : novaStavka.getArtikliDodatni()) {
                listTura.add(stavkaDodatni.dajStavkuTure());
            }
            for (StavkaTure stavkaOpisni : novaStavka.getArtikliOpisni()) {
                listTura.add(stavkaOpisni.dajStavkuTure());
            }
        }
                               
        if (listTura.isEmpty()) {
            tabelaNovaTuraGosta.izbrisiSveIzTabele();
            tabelaNovaTuraGosta.setManaged(false);
            tabelaNovaTuraGosta.setVisible(false);
            return;
        } 
        
        tabelaNovaTuraGosta.setPodaci(
                    listTura
        );
        
        tabelaNovaTuraGosta.getSelectionModel().select(
                getRowIndexOfStavka(tabelaNovaTuraGosta, selektovana)
        );
 
        prikaziKomponentu(tabelaNovaTuraGosta);
        //tabelaNovaTuraGosta.getSelectionModel().select(selektovano);
    }
    
    
    public void prikaziTotalPopustNaplataPorudzbina(Porudzbina porudzbina) {
        this.total.setText(Utils.getStringFromDouble(porudzbina.getVrednostPorudzbine()));
    }
    public void prikaziTotalPopustNaplataTura(Tura tura) {
        this.total.setText(Utils.getStringFromDouble(tura.getVrednostTure()));
    }
    
    public void dodajNovogGosta(ActionEvent event) 
    {
        
        ObservableList<Node> gostiButtons = prikazGostiju.getChildren();
        RadioButton noviGost = new RadioButton();
        noviGost.getStyleClass().remove("radio-button");
        noviGost.getStyleClass().add("toggle-button");

        noviGost.setToggleGroup(gostiButtonGroup);
        //int brojNovogGosta = 1;
        
        //if (!gostiButtons.isEmpty()) {
        int najveciBrojGosta = 0;
        for (Node gostiButton : gostiButtons) {
            int brojStola = Integer.parseInt(((RadioButton)gostiButton).getText());
            if (brojStola > najveciBrojGosta)
            { 
                najveciBrojGosta = brojStola;
            }
        }
        
        najveciBrojGosta++;

        Gost gost = new Gost(najveciBrojGosta);
        idTrenutnoIzabranogGosta = "" + najveciBrojGosta;
        
        this.porudzbinaTrenutna = new Porudzbina(
                gost, 
                izabraniStoId,
                izabraniStoBroj
        );
        
        this.porudzbineStola.add(porudzbinaTrenutna);
        this.racuniStolaPoGostima.put(
            "" + najveciBrojGosta, 
            DBBroker.getRecordSetIzStoreProcedureZaParametar(
                    "getStavkeRacuna", 
                    "racunID", 
                    "0"
            )
        );
        
        //TODO ovde se sada upisuje novi gost u bazu.
        /*Za prvog gosta se upisuje id=autoinc., idRacuna=56789, gost=1, ostaliRacuni=NULL*/
        
        
        noviGost.setId("" + najveciBrojGosta);
        noviGost.setText("" + najveciBrojGosta);
        noviGost.setPrefSize(50, 50);
        noviGost.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        String gost = ((RadioButton)e.getSource()).getId();
                                        Utils.postaviStil_ObrisiZaOstaleKontroleRoditelja(e, stilButtonGrupeSelektovana);
//                                        if (novaTura != null) {
//                                            novaTura = null;
//                                        }

                                        for (Porudzbina porudzbina : porudzbineStola) {
                                            if (porudzbina.getGost().getGostID() == Long.parseLong(gost)) {
                                                porudzbinaTrenutna = porudzbina;
                                                prikaziPorudzbinu(porudzbinaTrenutna);
                                                novaTura = null;
                                                for (Tura tura : porudzbinaTrenutna.getTure()) {
                                                    if (tura.getTuraID() == 0) {
                                                        novaTura = tura;
                                                        break;
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                    }
                                });
        
        this.prikazGostiju.getChildren().add(noviGost);
        prikazGostijuScrollPane.setContent(prikazGostiju);
    }
    
    
    public void pomeriScrollDown() {
        prikazRacunaGosta.setVvalue((prikazRacunaGosta.getVvalue() + 0.5 ) * 1);
    }
    
    public void pomeriScrollUp() {
        prikazRacunaGosta.setVvalue((prikazRacunaGosta.getVvalue() - 0.5 ) * 1);
    }
    
    public void toggleDodavanjeGostiju() {
                
        if (prikazGostijuScrollPane.getHvalue() == 1) {
            prikazGostijuScrollPane.setHvalue(0);
            return;
        }
        prikazGostijuScrollPane.setHvalue((prikazGostijuScrollPane.getHvalue() + 0.5 ) * 1);
    }
    
    /**
     * Smanjenje kolicine za 1 ili brisanje
     * @param event 
     */
    public void smanjiKolicinuZaJedan(ActionEvent event) {
        if (tabelaNovaTuraGosta.getItems().isEmpty() || porudzbinaTrenutna.getBlokiranaPorudzbina()) {
            return;
        }
        
        Map<String, String> izabraniRed = tabelaNovaTuraGosta.getSelectionModel().getSelectedItem();
        int izabraniRedIndex = tabelaNovaTuraGosta.getSelectionModel().getSelectedIndex();
        
        if (izabraniRed != null) {
            int redniBroj = Integer.parseInt(izabraniRed.get("redniBroj"));
            int redniBrojGlavnaStavka = Integer.parseInt(izabraniRed.get("redniBrojGlavnaStavka"));
            long artikalID = Long.parseLong(izabraniRed.get("artikalId"));

            if (redniBrojGlavnaStavka != -1) {
                // Smanjenje kolicine za 1 ili brisanje ako je odabran dodatni ili opisni artikal
                StavkaTure stavkaTure = null;
                StavkaTure glavnaStavka = novaTura.getStavkaTureByRedniBroj(redniBrojGlavnaStavka);
                if (glavnaStavka != null) {
                    // // Smanjenje kolicine za 1 ili brisanje ako je odabran dodatni artikal
                    stavkaTure = glavnaStavka.getDodatniArtikalByRedniBrojDodatnog(redniBroj);
                    if ((stavkaTure != null) && (artikalID == stavkaTure.getArtikalID())) {
                        stavkaTure.smanjiKolicinu();
                        if (stavkaTure.getKolicina() == 0)
                            glavnaStavka.getArtikliDodatni().remove(stavkaTure);
                    } else {
                        // // Smanjenje kolicine za 1 ili brisanje ako je odabran opisni artikal
                        stavkaTure = glavnaStavka.getOpisniArtikalByRedniBrojOpisnog(redniBroj);
                        if (stavkaTure != null) {
                            stavkaTure.smanjiKolicinu();
                            if (stavkaTure.getKolicina() == 0)
                                glavnaStavka.getArtikliOpisni().remove(stavkaTure);
                        }
                    }
                }
            } else {
                // Smanjenje kolicine za 1 ili brisanje glavne stavke
                StavkaTure stavkaTure = novaTura.getStavkaTureByRedniBroj(redniBroj);
                if (stavkaTure != null) {
                    stavkaTure.smanjiKolicinu();
                    if (stavkaTure.getKolicina() == 0)
                        novaTura.listStavkeTure.remove(stavkaTure);
                }
            }
        }

        this.tableRefresh();
        if (izabraniRedIndex+1 > this.tabelaNovaTuraGosta.getItems().size())
            izabraniRedIndex--;
        this.tabelaNovaTuraGosta.getSelectionModel().select(izabraniRedIndex);
        this.prikaziTotalPopustNaplataTura(novaTura);
//        this.prikaziTotalPopustNaplataStavke(novaTura.listStavkeTure);
        
    }

    
    public void naplataIliStampaPorudzbine(ActionEvent event) {
        if (novaTura != null && novaTura.listStavkeTure.size()>0) {
            if (this.izabraniStoBroj.equals("0"))
                // SNIMANJE BRZE NAPLATE
                porudzbinaTrenutna.snimi();
            else {
                for (Porudzbina porudzbina : porudzbineStola) {
                    novaTura = null;
                    for (Tura tura : porudzbina.getTure()) {
                        if (tura.getTuraID() == 0) {
                            novaTura = tura;
                            break;
                        }
                    }
                    if (novaTura != null) {
                        porudzbina.snimi();
                        Stampa.getInstance().stampajTuru(novaTura, izabraniStoId);
                    }
                }
    // Ovde resiti stampu po artiklima po stampacima
    // Ovde resiti stampu po artiklima po stampacima
    // Ovde resiti stampu po artiklima po stampacima

                List<Object> newData = new ArrayList<>();

                myController.setScreen(ScreenMap.PRIKAZ_SALA, newData);

                return;
            }
        }
        // TODO: Otvoriti formu za naplatu
        List<Object> newData = new ArrayList<>();
        newData.add(this.porudzbinaTrenutna);
        newData.add((Node)event.getSource());
        
        myController.setScreen(ScreenMap.NAPLATA, newData);
 
    }

    private void izbrisiSveIzTabela() {
         for(Node node : listaTabela) {
            if (!(node instanceof RM_TableView))  {
                continue;
            }
            node.setManaged(true);
            ((RM_TableView) node).izbrisiSveIzTabele();
         }
    }
    
    public void sakrijSveTabele() {
        izbrisiSveIzTabela();
        for(Node node : listaTabela) {
            node.setVisible(false);
            node.setManaged(false);
            if (node instanceof RM_Button) {
                ((RM_Button) node).setPrefSize(0, 0); 
            }
            if (node instanceof RM_TableView) {
                ((RM_TableView) node).setPrefSize(0, 0);
            }
        }
    }
    
    public void prikaziKomponentu(Node komponenta)
    {
        komponenta.setManaged(true);
        komponenta.setVisible(true);
    }
    
    @Override
    public void odjava(ActionEvent event)
    {            
        myController.setScreen(ScreenMap.POCETNI_EKRAN, null);
        RMaster.firstLogin = true;
        RMaster.saleOmoguceneKonobaru.clear();
    }
}
