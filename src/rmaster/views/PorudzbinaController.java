/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.lang.reflect.Field;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.collections.ObservableList;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import rmaster.assets.FXMLDocumentController;
import rmaster.assets.RM_TableView.RM_TableView;
import rmaster.assets.RM_TableView.RavnanjeKolone;
import rmaster.assets.RM_TableView.SirinaKolone;
import rmaster.assets.ScreenMap;
import rmaster.assets.Stampa;
import rmaster.assets.Utils;
import rmaster.assets.items.ArtikalButton;
import rmaster.assets.items.VrsteGrupaIliArtikal;
import static rmaster.assets.items.VrsteGrupaIliArtikal.*;
import rmaster.models.Gost;
import rmaster.models.Porudzbina;
import rmaster.models.StavkaTure;
import rmaster.models.Tura;

/**
 * FXML Controller class
 *
 * @author Bosko
 */
public class PorudzbinaController extends FXMLDocumentController {

    private long startTime;
    private long ms;
    
    @FXML
    public VBox prikazRacunaGostaSadrzaj;
    
    @FXML
    public HBox ArtikalGrupe;
    
    @FXML
    public FlowPane ArtikalPodgrupe;
    
    @FXML
    public FlowPane Artikal;
    
    @FXML
    public FlowPane ArtikalFavorite;
    
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
        long startTime = System.nanoTime();
        prikazRacunaGosta.setHbarPolicy(ScrollBarPolicy.NEVER);

        listaTabela = prikazRacunaGostaSadrzaj.getChildren();
        
        tabelaNovaTuraGosta.addRavnjanje(
            new RavnanjeKolone(7, RavnanjeKolone.ALIGN_RIGHT),
            new RavnanjeKolone(5, RavnanjeKolone.ALIGN_LEFT)
        );
        
        tabelaNovaTuraGosta.setSirineKolona(
                new SirinaKolone(3, sirinaKolonaTabele[2]),
                new SirinaKolone(5, sirinaKolonaTabele[4]),
                new SirinaKolone(7, sirinaKolonaTabele[6])
            );
        
        imeKonobara.setText(ulogovaniKonobar.imeKonobara);
        
        izabraniSto.setText("Sto: " + RMaster.izabraniStoNaziv);
        long ms;
        ms = System.nanoTime() - startTime;
        System.out.format("Porudzbina(initialize): %,10dms%n", ms);
    }    
    

    @Override
    public void initData(Object data) {

        try {
            //refreshGrupeIliArtikla(this.ArtikalGrupe, GLAVNA_GRUPA);
            long startTimeUK = System.nanoTime();
            long startUK = System.nanoTime();
            if (this.ArtikalGrupe.getChildren().isEmpty()) {
                long startTime = System.nanoTime();
                initArtikalPrikaz(GLAVNA_GRUPA);
                long ms;
                ms = System.nanoTime() - startTime;
                System.out.format("INIT: ----> initArtikalPrikaz(GLAVNA_GRUPA): %,10dms%n", ms);

                startTime = System.nanoTime();
                initArtikalPrikaz(ARTIKAL_FAVORITE);
                ms = System.nanoTime() - startTime;
                System.out.format("INIT: ----> initArtikalPrikaz(ARTIKAL_FAVORITE): %,10dms%n", ms);

                startTime = System.nanoTime();
                initArtikalPrikaz(ARTIKAL_DODATNI);
                ms = System.nanoTime() - startTime;
                System.out.format("INIT: ----> initArtikalPrikaz(ARTIKAL_DODATNI): %,10dms%n", ms);

                startTime = System.nanoTime();
                initArtikalPrikaz(ARTIKAL_OPISNI);
                ms = System.nanoTime() - startTime;
                System.out.format("INIT: ----> initArtikalPrikaz(ARTIKAL_OPISNI): %,10dms%n", ms);

                //startTime = System.nanoTime();
                startTime = System.nanoTime();
                this.ArtikalFavorite.setVisible(true);
                this.Artikal_DvaDela.setVisible(false);
                ms = System.nanoTime() - startTime;
                System.out.format("INIT: ----> this.ArtikalFavorite.setVisible(true) + DvaDela.setVisible(false): %,10dms%n", ms);
            }
            ms = System.nanoTime() - startTimeUK;
            System.out.format("INIT: --> initArtikalPrikaz(UKUPNO): %,10dms%n", ms);
            
            startTimeUK = System.nanoTime();
            refreshGrupeIliArtikla_v2(this.ArtikalGrupe, GLAVNA_GRUPA);
            ms = System.nanoTime() - startTimeUK;
            System.out.format("INIT: --> refreshGrupeIliArtikla_v2(this.ArtikalGrupe, GLAVNA_GRUPA): %,10dms%n", ms);
            
            startTimeUK = System.nanoTime();
            refreshGrupeIliArtikla_v2(this.ArtikalFavorite, ARTIKAL_FAVORITE);
            ms = System.nanoTime() - startTimeUK;
            System.out.format("INIT: --> refreshGrupeIliArtikla_v2(this.ArtikalFavorite, ARTIKAL_FAVORITE): %,10dms%n", ms);

            //ms = System.nanoTime() - startTime;
            //System.out.format("refreshGrupeIliArtikla(this.Artikal, ARTIKAL_FAVORITE): %,10dms%n", ms);

            startTime = System.nanoTime();
            prikaziPorudzbinu();
            ms = System.nanoTime() - startTime;
            System.out.format("INIT: --> prikaziPorudzbinu(): %,10dms%n", ms);

            startTime = System.nanoTime();
            if (this.prikazGostiju.getChildren().isEmpty()) {
                 //TODO: Dodaj prvog gosta i napravi porudzbinu za njega
                dodajNovogGosta(new ActionEvent());
            }
            ms = System.nanoTime() - startTime;
            System.out.format("INIT: --> dodajNovogGosta(new ActionEvent()): %,10dms%n", ms);

            startTime = System.nanoTime();
            RadioButton dugme = (RadioButton)prikazGostiju.getChildren().get(0);
            dugme.fire();
            ms = System.nanoTime() - startTime;
            System.out.format("INIT: --> dugme.fire(): %,10dms%n", ms);
            ms = System.nanoTime() - startUK;
            System.out.format("INIT: UKUPNO: %,10dms%n", ms);

        } catch (Exception e) {
            System.out.println("Greska u pozivu SP get_racuniKonobaraKojiNisuZatvoreni! - " + e.toString());
        }
    }
    

/***************************************************************/
/**** KREIRAJ PRIKAZ ZA GRUPA, PODGRUPA I ARTIKAL, FAVORITE ****/
/**
     * @param staSePrikazuje*************************************************************/
    public void initArtikalPrikaz(VrsteGrupaIliArtikal staSePrikazuje) {
        Pane gdePrikazati = null;
        switch(staSePrikazuje) {
            case GLAVNA_GRUPA:
                prikazRedniBrojPrvog = prikazGrupaRedniBrojPrvog;
                prikazBrojPrikazanihPlus1 = prikazGrupaBrojPrikazanihPlus1;
                prikazBrojRedova = prikazGrupaBrojRedova;
                stilArtikalIliGrupa = stilGrupa;
                gdePrikazati = this.ArtikalGrupe;
                break;
            // Za podgrupu i opisne artikle
            case POD_GRUPA:
            case ARTIKAL_OPISNI:
                prikazBrojPrikazanihPlus1 = prikazPodgrupaBrojPrikazanihPlus1;
                prikazBrojRedova = prikazPodgrupaBrojRedova;

                prikazRedniBrojPrvog = prikazPodgrupaRedniBrojPrvog;
                stilArtikalIliGrupa = stilPodgrupa;
                gdePrikazati = this.Artikal;
                break;
            // Za artikle i dodatne artikle
            case ARTIKAL_DODATNI:
            case ARTIKAL_GLAVNI:
                prikazBrojRedova = prikazArtikalBrojRedova;
                prikazBrojPrikazanihPlus1 = prikazArtikalBrojPrikazanihPlus1;

                prikazRedniBrojPrvog = prikazArtikalRedniBrojPrvog;
                stilArtikalIliGrupa = stilArtikal;
                gdePrikazati = this.ArtikalPodgrupe;
                break;
            case ARTIKAL_FAVORITE:
                prikazRedniBrojPrvog = prikazFavoriteRedniBrojPrvog;
                prikazBrojPrikazanihPlus1 = prikazFavoriteBrojPrikazanihPlus1;
                prikazBrojRedova = prikazFavoriteBrojRedova;
                stilArtikalIliGrupa = stilArtikal;
                gdePrikazati = this.ArtikalFavorite;
                break;
            default:
        }
        gdePrikazati.getChildren().clear();
        gdePrikazati.setPrefHeight(prikazBrojRedova*defaultVisinaDugmeta);

        // Odredjivanje sirine dugmica za podgrupe artikala
        //double roditeljSirina = gdePrikazati.getParent().getBoundsInParent().getWidth();
        //Ovo sam uveo da bi radilo... vraca mi gore nula


        double artikalPodgrupaSirina = (roditeljSirina/prikazBrojArtikalaUJednomRedu);
        int brojac=0;
        double sirina = 0.0;
            
           
        try {

                int ostaloPraznihDugmica = this.prikazBrojPrikazanihPlus1 - 1;
                while (ostaloPraznihDugmica>0) {
                    ArtikalButton bPrazan = new ArtikalButton();
                    //bPrazan.getStyleClass().add(stilArtikliPrazanButton);
                    bPrazan.getStyleClass().add(stilArtikalIliGrupa);
                    bPrazan.setVisible(true);
                    bPrazan.setDisable(false);
                    bPrazan.setPrefSize(artikalPodgrupaSirina, defaultVisinaDugmeta);
                    bPrazan.setOnAction(new EventHandler<ActionEvent>() {               
                                    @Override public void handle(ActionEvent e) {
                                        //obradiIzabraniArtikalIliGrupu(e, gdePrikazati);
                                    }
                                });

                    gdePrikazati.getChildren().add(bPrazan);
                    ostaloPraznihDugmica--;
                }
            
                Button bPrethodniArtikliIliGrupe = new Button("«");
                bPrethodniArtikliIliGrupe.setPrefSize(artikalPodgrupaSirina/2, defaultVisinaDugmeta);
                bPrethodniArtikliIliGrupe.getStyleClass().add(stilArtikalIliGrupa);
                bPrethodniArtikliIliGrupe.setDisable(true);
                bPrethodniArtikliIliGrupe.getStyleClass().add(stilArtikliGrupeNextPrev);
                bPrethodniArtikliIliGrupe.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        switch(staSePrikazuje) {
                                            case GLAVNA_GRUPA:
                                                prikazGrupaRedniBrojPrvog = prikazGrupaRedniBrojPrvog - prikazGrupaBrojPrikazanihPlus1 + 1;
                                                break;
                                            // Za podgrupu i opisne artikle
                                            case POD_GRUPA:
                                            case ARTIKAL_OPISNI:
                                                prikazPodgrupaRedniBrojPrvog = prikazPodgrupaRedniBrojPrvog - prikazPodgrupaBrojPrikazanihPlus1 + 1;
                                                break;
                                            // Za artikle i dodatne artikle
                                            case ARTIKAL_GLAVNI:
                                            case ARTIKAL_DODATNI:
                                                prikazArtikalRedniBrojPrvog = prikazArtikalRedniBrojPrvog - prikazArtikalBrojPrikazanihPlus1 + 1;
                                                break;
                                            case ARTIKAL_FAVORITE:
                                                prikazFavoriteRedniBrojPrvog = prikazFavoriteRedniBrojPrvog - prikazFavoriteBrojPrikazanihPlus1 + 1;
                                                break;
                                            default:
                                        }
                                        //refreshGrupeIliArtikla_v2(gdePrikazati, staSePrikazuje);
                                    }
                                });
                gdePrikazati.getChildren().add(bPrethodniArtikliIliGrupe);

                sirina = roditeljSirina - (prikazBrojArtikalaUJednomRedu-1)*artikalPodgrupaSirina - artikalPodgrupaSirina/2;
                Button bSledeciArtikliIliGrupe = new Button("»");
                bSledeciArtikliIliGrupe.setPrefSize(sirina, defaultVisinaDugmeta);
                bSledeciArtikliIliGrupe.getStyleClass().add(stilArtikalIliGrupa);
                bSledeciArtikliIliGrupe.setDisable(true);
                bSledeciArtikliIliGrupe.getStyleClass().add(stilArtikliGrupeNextPrev);
                bSledeciArtikliIliGrupe.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        switch(staSePrikazuje) {
                                            case GLAVNA_GRUPA:
                                                prikazGrupaRedniBrojPrvog = prikazGrupaRedniBrojPrvog + prikazGrupaBrojPrikazanihPlus1 - 1;
                                                break;
                                            // Za podgrupu i opisne artikle
                                            case POD_GRUPA:
                                            case ARTIKAL_OPISNI:
                                                prikazPodgrupaRedniBrojPrvog = prikazPodgrupaRedniBrojPrvog + prikazPodgrupaBrojPrikazanihPlus1 - 1;
                                                break;
                                            // Za artikle i dodatne artikle
                                            case ARTIKAL_GLAVNI:
                                            case ARTIKAL_DODATNI:
                                                prikazArtikalRedniBrojPrvog = prikazArtikalRedniBrojPrvog + prikazArtikalBrojPrikazanihPlus1 - 1;
                                                break;
                                            case ARTIKAL_FAVORITE:
                                                prikazFavoriteRedniBrojPrvog = prikazFavoriteRedniBrojPrvog + prikazFavoriteBrojPrikazanihPlus1 - 1;
                                                break;
                                            default:
                                        }
                                        //refreshGrupeIliArtikla(gdePrikazati, staSePrikazuje);
                                    }
                                });
                gdePrikazati.getChildren().add(bSledeciArtikliIliGrupe);

        } catch (Exception e) {
            System.out.println("Greska u prikazu artikala!");
        } 
    }
/***************************************************************/
/************************ KRAJ *********************************/
/**** KREIRAJ PRIKAZ ZA GRUPA, PODGRUPA I ARTIKAL, FAVORITE ****/
/************************ KRAJ *********************************/
/***************************************************************/


    
    
    public void prikaziPorudzbinu() {
           
            long startTime = System.nanoTime();
            List racuniStola = DBBroker.get_PorudzbineStola();
            long ms = System.nanoTime() - startTime;
            System.out.format("prikaziPorudzbinu() - DBBroker.get_PorudzbineStolaIKonobara(): %,10dms%n", ms);
            for (Object racun : racuniStola) {
            
                startTime = System.nanoTime();
                Map<String, String> red = (Map<String, String>) racun;
                
                String brojNovogGosta = red.get("gost");
                porudzbinaTrenutna = new Porudzbina(new Gost(brojNovogGosta), red.get("id"));
                porudzbineStola.add(porudzbinaTrenutna);

                RadioButton b = new RadioButton(brojNovogGosta);
                
                b.getStyleClass().remove("radio-button");
                b.getStyleClass().add("toggle-button");
                
                b.setToggleGroup(gostiButtonGroup);
                
                b.setId(brojNovogGosta);
                
                b.setPrefSize(50, 50);
                b.setOnAction(new EventHandler<ActionEvent>() {
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
                                                    if (tura.getTuraID() == 0)
                                                        novaTura = tura;
                                                }
                                                break;
                                            }
                                        }
                                    }
                                });

                this.prikazGostiju.getChildren().add(b);
                ms = System.nanoTime() - startTime;
                System.out.format("prikaziPorudzbinu() - dinamicko kreiranje porudzbine: %,10dms%n", ms);
            
        }       
        prikazGostijuScrollPane.setContent(prikazGostiju);
    
    }
    public void prikaziPorudzbinu(Porudzbina porudzbina) {
        
        sakrijSveTabele();
 
        List<Node> listaSadrzaja = prikazRacunaGostaSadrzaj.getChildren();

        prikazRacunaGosta.setFitToWidth(true);
                                                
        prikazRacunaGostaSadrzaj.setPadding(new Insets(0, 0, 0, 0));
        
        int brojac = 0;
        
        for (Tura tura : porudzbina.getTure()) {
            
            RM_TableView novaTabela = new RM_TableView();
            Button ponoviTuru = new Button();
            
            brojac++;
            
            if (brojac < 12) {
                novaTabela = (RM_TableView) findNodeById(listaSadrzaja, "tura" + brojac);
                prikaziKomponentu(novaTabela);
                ponoviTuru = (Button) findNodeById(listaSadrzaja, "ponoviTura" + brojac);
                prikaziKomponentu(ponoviTuru);
            } 
            
            novaTabela.setSelectionModel(null);
            
            if (tura.getTuraID() == 0) {
                novaTura = tura;
            }
            
            novaTabela.addRavnjanje(
                    new RavnanjeKolone(7, RavnanjeKolone.ALIGN_RIGHT),
                    new RavnanjeKolone(5, RavnanjeKolone.ALIGN_LEFT)            
            );
            
            novaTabela.setSirineKolona(
                new SirinaKolone(3, sirinaKolonaTabele[2]),
                new SirinaKolone(5, sirinaKolonaTabele[4]),
                new SirinaKolone(7, sirinaKolonaTabele[6])
            );
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
            ponoviTuru.setId("" + tura.getTuraID());
            ponoviTuru.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {
                                String turaId = ((Button)e.getSource()).getId();
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
        prikaziFormu(new ArrayList<>(),
                ScreenMap.PRIKAZ_SALA,
                true, 
                (Node)event.getSource(), false
        );
    }
    
    public void prikaziArtikleFavorite(ActionEvent event) {
        onButtonArtikalIliGrupa_click(event, this.Artikal);
    }

    protected void refreshGrupeIliArtikla(Pane gdePrikazati, VrsteGrupaIliArtikal staSePrikazuje) {
        String imeStoreProcedure = "";
        String[] imenaArgumenata = {"","",""};
        String[] vrednostiArgumenata = {"","",""};
        List rs = null;
        try {
           switch(staSePrikazuje) {
                // Za grupu i podgrupu
                case GLAVNA_GRUPA:
                    imeStoreProcedure = "getArtikliPodgrupeOdGrupe";
                    imenaArgumenata = new String[]{"nadredjenaGrupa", "brojPrvogZapisa", "brojZapisa"};
                    vrednostiArgumenata = new String[]{"0","" + (this.prikazGrupaRedniBrojPrvog - 1),"" + (this.prikazGrupaBrojPrikazanihPlus1)};
                    break;
                case POD_GRUPA:
                    imeStoreProcedure = "getArtikliPodgrupeOdGrupe";
                    imenaArgumenata = new String[]{"nadredjenaGrupa", "brojPrvogZapisa", "brojZapisa"};
                    vrednostiArgumenata = new String[]{"" + this.selektovanaGlavnaGrupaID,"" + (this.prikazPodgrupaRedniBrojPrvog - 1),"" + (this.prikazPodgrupaBrojPrikazanihPlus1)};
                    break;
                case ARTIKAL_GLAVNI:
                    imeStoreProcedure = "getArtikliGrupe";
                    imenaArgumenata = new String[]{"podgrupaID", "brojPrvogZapisa", "brojZapisa"};
                    vrednostiArgumenata = new String[]{"" + this.selektovanaPodgrupaID,"" + (this.prikazArtikalRedniBrojPrvog - 1),"" + (this.prikazArtikalBrojPrikazanihPlus1)};
                    break;
                case ARTIKAL_OPISNI:
                    imeStoreProcedure = "getArtikalAtributi";
                    imenaArgumenata = new String[]{"ArtikalID", "brojPrvogZapisa", "brojZapisa"};
                    // Opisni artikli idu u polje gde i podgrupe
                    vrednostiArgumenata = new String[]{"" + this.selektovanArtikalID,"" + (this.prikazPodgrupaRedniBrojPrvog - 1),"" + (this.prikazPodgrupaBrojPrikazanihPlus1)};
                    break;
                case ARTIKAL_DODATNI:
                    imeStoreProcedure = "getArtikalDodaci";
                    imenaArgumenata = new String[]{"ArtikalID", "brojPrvogZapisa", "brojZapisa"};
                    // Dodatni artikli idu u polje gde i artikli
                    vrednostiArgumenata = new String[]{"" + this.selektovanArtikalID,"" + (this.prikazArtikalRedniBrojPrvog - 1),"" + (this.prikazArtikalBrojPrikazanihPlus1)};
                    break;
                case ARTIKAL_FAVORITE:
                    imeStoreProcedure = "getArtikliFavorite";
                    imenaArgumenata = new String[]{"brojPrvogZapisa", "brojZapisa"};
                    vrednostiArgumenata = new String[]{"" + (this.prikazFavoriteRedniBrojPrvog - 1),"" + (this.prikazFavoriteBrojPrikazanihPlus1)};
                    break;
                default:
            };
            startTime = System.nanoTime();
            rs = runStoredProcedure(imeStoreProcedure, imenaArgumenata, vrednostiArgumenata);
            ms = System.nanoTime() - startTime;
            System.out.format("runStoredProcedure(" + imeStoreProcedure + ", " + imenaArgumenata.toString() + ", " + vrednostiArgumenata.toString() + ");: %,10dms%n", ms);

            switch(staSePrikazuje) {
                case GLAVNA_GRUPA:
                    rsGrupe = rs;
                    break;
                // Za podgrupu i opisne artikle
                case POD_GRUPA:
                case ARTIKAL_OPISNI:
                    rsPodgrupe = rs;
                    break;
                // Za artikle i dodatne artikle
                case ARTIKAL_GLAVNI:
                case ARTIKAL_DODATNI:
                case ARTIKAL_FAVORITE:
                    rsArtikli = rs;
                    break;
                default:
            }
        } catch (Exception e) {
            System.out.println("View \"" + imeStoreProcedure + "\" fetch error!");
        }
        startTime = System.nanoTime();
        prikazArtikalIliGrupa(rs, gdePrikazati, staSePrikazuje);
        ms = System.nanoTime() - startTime;
        System.out.format("prikazArtikalIliGrupa(" + gdePrikazati + ", " + staSePrikazuje + ");: %,10dms%n", ms);
    }
    
    protected void refreshGrupeIliArtikla_v2(Pane gdePrikazati, VrsteGrupaIliArtikal staSePrikazuje) {
//        String imeStoreProcedure = "";
//        String[] imenaArgumenata = {"","",""};
//        String[] vrednostiArgumenata = {"","",""};

        List<Map<String,String>> rs = new ArrayList<>();
        
        try {
           //switch(staSePrikazuje) {
                // Za grupu i podgrupu
          //      case GLAVNA_GRUPA:
//                    imeStoreProcedure = "getArtikliPodgrupeOdGrupe";
//                    imenaArgumenata = new String[]{"nadredjenaGrupa", "brojPrvogZapisa", "brojZapisa"};
//                    vrednostiArgumenata = new String[]{"0","" + (this.prikazGrupaRedniBrojPrvog - 1),"" + (this.prikazGrupaBrojPrikazanihPlus1)};
//                    break;
//                case POD_GRUPA:
//                    imeStoreProcedure = "getArtikliPodgrupeOdGrupe";
//                    imenaArgumenata = new String[]{"nadredjenaGrupa", "brojPrvogZapisa", "brojZapisa"};
//                    vrednostiArgumenata = new String[]{"" + this.selektovanaGlavnaGrupaID,"" + (this.prikazPodgrupaRedniBrojPrvog - 1),"" + (this.prikazPodgrupaBrojPrikazanihPlus1)};
//                    break;
//                case ARTIKAL_GLAVNI:
//                    imeStoreProcedure = "getArtikliGrupe";
//                    imenaArgumenata = new String[]{"podgrupaID", "brojPrvogZapisa", "brojZapisa"};
//                    vrednostiArgumenata = new String[]{"" + this.selektovanaPodgrupaID,"" + (this.prikazArtikalRedniBrojPrvog - 1),"" + (this.prikazArtikalBrojPrikazanihPlus1)};
//                    break;
//                case ARTIKAL_OPISNI:
//                    imeStoreProcedure = "getArtikalAtributi";
//                    imenaArgumenata = new String[]{"ArtikalID", "brojPrvogZapisa", "brojZapisa"};
//                    // Opisni artikli idu u polje gde i podgrupe
//                    vrednostiArgumenata = new String[]{"" + this.selektovanArtikalID,"" + (this.prikazPodgrupaRedniBrojPrvog - 1),"" + (this.prikazPodgrupaBrojPrikazanihPlus1)};
//                    break;
//                case ARTIKAL_DODATNI:
//                    imeStoreProcedure = "getArtikalDodaci";
//                    imenaArgumenata = new String[]{"ArtikalID", "brojPrvogZapisa", "brojZapisa"};
//                    // Dodatni artikli idu u polje gde i artikli
//                    vrednostiArgumenata = new String[]{"" + this.selektovanArtikalID,"" + (this.prikazArtikalRedniBrojPrvog - 1),"" + (this.prikazArtikalBrojPrikazanihPlus1)};
//                    break;
//                case ARTIKAL_FAVORITE:
//                    imeStoreProcedure = "getArtikliFavorite";
//                    imenaArgumenata = new String[]{"brojPrvogZapisa", "brojZapisa"};
//                    vrednostiArgumenata = new String[]{"" + (this.prikazFavoriteRedniBrojPrvog - 1),"" + (this.prikazFavoriteBrojPrikazanihPlus1)};
//                    break;
          //      default:
          //  };
            startTime = System.nanoTime();
            //rs = runStoredProcedure(imeStoreProcedure, imenaArgumenata, vrednostiArgumenata);
            //ms = System.nanoTime() - startTime;
            //System.out.format("runStoredProcedure(" + imeStoreProcedure + ", " + imenaArgumenata.toString() + ", " + vrednostiArgumenata.toString() + ");: %,10dms%n", ms);

            switch(staSePrikazuje) {
                case GLAVNA_GRUPA:
                    popuniListuGrupa(rs, "", this.prikazGrupaRedniBrojPrvog - 1, this.prikazGrupaBrojPrikazanihPlus1);
                    rsGrupe = rs;
                    break;
                // Za podgrupu i opisne artikle
                case POD_GRUPA:
                case ARTIKAL_OPISNI:
                    rsPodgrupe = rs;
                    break;
                // Za artikle i dodatne artikle
                case ARTIKAL_GLAVNI:
                case ARTIKAL_DODATNI:
                case ARTIKAL_FAVORITE:
                    popuniListuArtikala(rs, (this.prikazFavoriteRedniBrojPrvog - 1), this.prikazFavoriteBrojPrikazanihPlus1);
                    rsArtikli = rs;
                    break;
                default:
            }
        } catch (Exception e) {
            System.out.println("View \"NEKA GRESKA" + "\" fetch error!");
        }
        //startTime = System.nanoTime();
        prikazArtikalIliGrupa_v2(rs, gdePrikazati, staSePrikazuje);
        ms = System.nanoTime() - startTime;
        System.out.format("prikazArtikalIliGrupa(" + gdePrikazati + ", " + staSePrikazuje + ");: %,10dms%n", ms);
    }
    
    public void popuniListuGrupa(List<Map<String,String>> rs, String uslov, int pocetak, int brojElemenata) {
        List<Map<String,String>> listaGrupa = RMaster.listaGrupaArtikalaFront;
        int brojac = 0;
        int kraj = pocetak + brojElemenata;
        for (Map<String,String> grupa : listaGrupa) {
            if ((grupa.get("GRUPA_ID") != null) && grupa.get("GRUPA_ID").equals(uslov)) {
                brojac++;
                if (brojac>=pocetak && brojac<kraj)
                    rs.add(grupa);
            }
        }
    }

    public void popuniListuArtikala(List<Map<String,String>> rs, int pocetak, int brojElemenata) {
        List<Map<String,String>> listaArtikalaFav = RMaster.listaArtikalaFavorite;
        int brojac = 0;
        int kraj = pocetak + brojElemenata;
        for (Map<String,String> fav : listaArtikalaFav) {
            brojac++;
            if (brojac>=pocetak && brojac<kraj)
                rs.add(fav);
        }
    }
/***************************************************************/
/************** PRIKAZ GRUPA, PODGRUPA I ARTIKALA **************/
/***************************************************************/
    public void prikazArtikalIliGrupa(List rs, Pane gdePrikazati, VrsteGrupaIliArtikal staSePrikazuje) {
        try {
//                    if (staSePrikazuje == VrsteGrupaIliArtikal.POD_GRUPA && rs.isEmpty()) {
//                        this.ArtikalPodgrupe.getChildren().clear();
//                        this.ArtikalPodgrupe.setPrefHeight(0);
//                        this.Artikal.setPrefHeight((prikazPodgrupaBrojRedova+prikazArtikalBrojRedova)*defaultVisinaDugmeta);
//                    } else {
//                        this.ArtikalPodgrupe.getChildren().clear();
//                        this.ArtikalPodgrupe.setPrefHeight(prikazBrojRedova*defaultVisinaDugmeta);
//                        this.Artikal.setPrefHeight(prikazArtikalBrojRedova*defaultVisinaDugmeta);
//                    }
            
            //this.ArtikalPodgrupe.setPrefHeight(prikazPodgrupaBrojRedova * defaultVisinaDugmeta);
            //this.Artikal.setPrefHeight(prikazArtikalBrojRedova * defaultVisinaDugmeta);
            switch(staSePrikazuje) {
                case GLAVNA_GRUPA:
                    prikazRedniBrojPrvog = prikazGrupaRedniBrojPrvog;
                    prikazBrojPrikazanihPlus1 = prikazGrupaBrojPrikazanihPlus1;
                    prikazBrojRedova = prikazGrupaBrojRedova;
                    stilArtikalIliGrupa = stilGrupa;
                    break;
                // Za podgrupu i opisne artikle
                case POD_GRUPA:
                case ARTIKAL_OPISNI:
                    if (rs.isEmpty()) {
                        prikazBrojPrikazanihPlus1 = 0;
                        prikazBrojRedova = 0;
                    } else {
                        prikazBrojPrikazanihPlus1 = prikazPodgrupaBrojPrikazanihPlus1;
                        prikazBrojRedova = prikazPodgrupaBrojRedova;
                    }
                    
                    prikazRedniBrojPrvog = prikazPodgrupaRedniBrojPrvog;
                    stilArtikalIliGrupa = stilPodgrupa;
                    break;
                // Za artikle i dodatne artikle
                case ARTIKAL_DODATNI:
                case ARTIKAL_GLAVNI:
                    if (this.ArtikalPodgrupe.getPrefHeight() == 0) {
                        prikazBrojRedova = prikazFavoriteBrojRedova;
                        prikazBrojPrikazanihPlus1 = prikazFavoriteBrojPrikazanihPlus1;
                    } else {
                        prikazBrojRedova = prikazArtikalBrojRedova;
                        prikazBrojPrikazanihPlus1 = prikazArtikalBrojPrikazanihPlus1;
                    }
                    prikazRedniBrojPrvog = prikazArtikalRedniBrojPrvog;
                    //prikazBrojRedova = prikazArtikalBrojRedova;
                    stilArtikalIliGrupa = stilArtikal;
                    break;
                case ARTIKAL_FAVORITE:
                    prikazRedniBrojPrvog = prikazFavoriteRedniBrojPrvog;
                    prikazBrojPrikazanihPlus1 = prikazFavoriteBrojPrikazanihPlus1;
                    prikazBrojRedova = prikazFavoriteBrojRedova;
                    stilArtikalIliGrupa = stilArtikal;
                    this.ArtikalPodgrupe.getChildren().clear();
                    this.ArtikalPodgrupe.setPrefHeight(0.0);
                    break;
                default:
            }
            gdePrikazati.getChildren().clear();
            gdePrikazati.setPrefHeight(prikazBrojRedova*defaultVisinaDugmeta);
            
            // Odredjivanje sirine dugmica za podgrupe artikala
            //double roditeljSirina = gdePrikazati.getParent().getBoundsInParent().getWidth();
            //Ovo sam uveo da bi radilo... vraca mi gore nula
            
            
            double artikalPodgrupaSirina = (roditeljSirina/prikazBrojArtikalaUJednomRedu);
            int brojac=0;
            double sirina = 0.0;
            
            for(Object r : rs) {
                brojac++;
                Map<String, String> red = (Map<String, String>) r;
                //long id = Long.parseLong(red.get("id"));
                //String naziv = red.get("naziv");
                double cena = 0;
                try {
                    cena = Double.parseDouble(red.get("cena"));
                }catch (Exception e) {
                }
                
                String dozvoljenPopust = red.get("dozvoljenPopust");
                if(dozvoljenPopust == null) {
                    dozvoljenPopust = "";
                }
                
                String stampacID = red.get("stampacID");
                
                ArtikalButton buttonArtikalIliGrupa = 
                        new ArtikalButton(
                                red.get("naziv"),
                                red.get("slika"),
                                red.get("id"),
                                red.get("prioritet"),
                                red.get("skrNaziv"),
                                red.get("GRUPA_ID"),
                                red.get("tip"),
                                staSePrikazuje,
                                cena,
                                dozvoljenPopust,
                                stampacID
                        );
                
                if ((brojac%prikazBrojArtikalaUJednomRedu)==0) {
                    sirina = roditeljSirina - (prikazBrojArtikalaUJednomRedu-1)*artikalPodgrupaSirina;
                    buttonArtikalIliGrupa.setPrefSize(sirina, defaultVisinaDugmeta);
                }
                else
                    buttonArtikalIliGrupa.setPrefSize(artikalPodgrupaSirina, defaultVisinaDugmeta);
                
                buttonArtikalIliGrupa.getStyleClass().add(stilArtikalIliGrupa);
                buttonArtikalIliGrupa.setOnAction(new EventHandler<ActionEvent>() {               
                                    @Override public void handle(ActionEvent e) {
                                        obradiIzabraniArtikalIliGrupu(e, gdePrikazati);
                                    }
                                });
                gdePrikazati.getChildren().add((Node)buttonArtikalIliGrupa);
                if (brojac == this.prikazBrojPrikazanihPlus1-1)
                    break;
            }
            
            if (!rs.isEmpty()) {
                int ostaloPraznihDugmica = this.prikazBrojPrikazanihPlus1 - 1 - rs.size();
                while (ostaloPraznihDugmica>0) {
                    Button bPrazan = new Button();
                    bPrazan.getStyleClass().add(stilArtikliPrazanButton);
                    bPrazan.setDisable(true);
                    bPrazan.setPrefSize(artikalPodgrupaSirina, defaultVisinaDugmeta);

                    gdePrikazati.getChildren().add(bPrazan);
                    ostaloPraznihDugmica--;
                }
            
                Button bPrethodniArtikliIliGrupe = new Button("«");
                bPrethodniArtikliIliGrupe.setPrefSize(artikalPodgrupaSirina/2, defaultVisinaDugmeta);
                bPrethodniArtikliIliGrupe.getStyleClass().add(stilArtikalIliGrupa);
                if (this.prikazRedniBrojPrvog==1)
                    bPrethodniArtikliIliGrupe.setDisable(true);
                else {
                    bPrethodniArtikliIliGrupe.setDisable(false);
                    bPrethodniArtikliIliGrupe.getStyleClass().add(stilArtikliGrupeNextPrev);
                        }
                bPrethodniArtikliIliGrupe.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        switch(staSePrikazuje) {
                                            case GLAVNA_GRUPA:
                                                prikazGrupaRedniBrojPrvog = prikazGrupaRedniBrojPrvog - prikazGrupaBrojPrikazanihPlus1 + 1;
                                                break;
                                            // Za podgrupu i opisne artikle
                                            case POD_GRUPA:
                                            case ARTIKAL_OPISNI:
                                                prikazPodgrupaRedniBrojPrvog = prikazPodgrupaRedniBrojPrvog - prikazPodgrupaBrojPrikazanihPlus1 + 1;
                                                break;
                                            // Za artikle i dodatne artikle
                                            case ARTIKAL_GLAVNI:
                                            case ARTIKAL_DODATNI:
                                                prikazArtikalRedniBrojPrvog = prikazArtikalRedniBrojPrvog - prikazArtikalBrojPrikazanihPlus1 + 1;
                                                break;
                                            case ARTIKAL_FAVORITE:
                                                prikazFavoriteRedniBrojPrvog = prikazFavoriteRedniBrojPrvog - prikazFavoriteBrojPrikazanihPlus1 + 1;
                                                break;
                                            default:
                                        }
                                        refreshGrupeIliArtikla(gdePrikazati, staSePrikazuje);
                                    }
                                });
                gdePrikazati.getChildren().add(bPrethodniArtikliIliGrupe);

                sirina = roditeljSirina - (prikazBrojArtikalaUJednomRedu-1)*artikalPodgrupaSirina - artikalPodgrupaSirina/2;
                Button bSledeciArtikliIliGrupe = new Button("»");
                bSledeciArtikliIliGrupe.setPrefSize(sirina, defaultVisinaDugmeta);
                bSledeciArtikliIliGrupe.getStyleClass().add(stilArtikalIliGrupa);
                if (this.prikazBrojPrikazanihPlus1 == rs.size()) {
                    bSledeciArtikliIliGrupe.setDisable(false);
                    bSledeciArtikliIliGrupe.getStyleClass().add(stilArtikliGrupeNextPrev);
                }
                else
                    bSledeciArtikliIliGrupe.setDisable(true);
                bSledeciArtikliIliGrupe.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        switch(staSePrikazuje) {
                                            case GLAVNA_GRUPA:
                                                prikazGrupaRedniBrojPrvog = prikazGrupaRedniBrojPrvog + prikazGrupaBrojPrikazanihPlus1 - 1;
                                                break;
                                            // Za podgrupu i opisne artikle
                                            case POD_GRUPA:
                                            case ARTIKAL_OPISNI:
                                                prikazPodgrupaRedniBrojPrvog = prikazPodgrupaRedniBrojPrvog + prikazPodgrupaBrojPrikazanihPlus1 - 1;
                                                break;
                                            // Za artikle i dodatne artikle
                                            case ARTIKAL_GLAVNI:
                                            case ARTIKAL_DODATNI:
                                                prikazArtikalRedniBrojPrvog = prikazArtikalRedniBrojPrvog + prikazArtikalBrojPrikazanihPlus1 - 1;
                                                break;
                                            case ARTIKAL_FAVORITE:
                                                prikazFavoriteRedniBrojPrvog = prikazFavoriteRedniBrojPrvog + prikazFavoriteBrojPrikazanihPlus1 - 1;
                                                break;
                                            default:
                                        }
                                        refreshGrupeIliArtikla(gdePrikazati, staSePrikazuje);
                                    }
                                });
                gdePrikazati.getChildren().add(bSledeciArtikliIliGrupe);
            }
        } catch (Exception e) {
            System.out.println("Greska u prikazu artikala!");
        } 
    }
/***************************************************************/
/********** KRAJ - PRIKAZ GRUPA, PODGRUPA I ARTIKALA ***********/
/***************************************************************/

/***************************************************************/
/************** PRIKAZ GRUPA, PODGRUPA I ARTIKALA V2 ***********/
/***************************************************************/
    public void prikazArtikalIliGrupa_v2(List rs, Pane gdePrikazati, VrsteGrupaIliArtikal staSePrikazuje) {
        try {
            switch(staSePrikazuje) {
                case GLAVNA_GRUPA:
                    prikazRedniBrojPrvog = prikazGrupaRedniBrojPrvog;
                    prikazBrojPrikazanihPlus1 = prikazGrupaBrojPrikazanihPlus1;
                    prikazBrojRedova = prikazGrupaBrojRedova;
                    stilArtikalIliGrupa = stilGrupa;
                    break;
                // Za podgrupu i opisne artikle
                case POD_GRUPA:
                case ARTIKAL_OPISNI:
                    this.Artikal_DvaDela.setVisible(true);
                    this.ArtikalFavorite.setVisible(false);
                    if (rs.isEmpty()) {
                        prikazBrojPrikazanihPlus1 = 0;
                        prikazBrojRedova = 0;
                    } else {
                        prikazBrojPrikazanihPlus1 = prikazPodgrupaBrojPrikazanihPlus1;
                        prikazBrojRedova = prikazPodgrupaBrojRedova;
                    }
                    
                    prikazRedniBrojPrvog = prikazPodgrupaRedniBrojPrvog;
                    stilArtikalIliGrupa = stilPodgrupa;
                    break;
                // Za artikle i dodatne artikle
                case ARTIKAL_DODATNI:
                case ARTIKAL_GLAVNI:
                    this.Artikal_DvaDela.setVisible(true);
                    this.ArtikalFavorite.setVisible(false);
                    if (this.ArtikalPodgrupe.getPrefHeight() == 0) {
                        prikazBrojRedova = prikazFavoriteBrojRedova;
                        prikazBrojPrikazanihPlus1 = prikazFavoriteBrojPrikazanihPlus1;
                    } else {
                        prikazBrojRedova = prikazArtikalBrojRedova;
                        prikazBrojPrikazanihPlus1 = prikazArtikalBrojPrikazanihPlus1;
                    }
                    prikazRedniBrojPrvog = prikazArtikalRedniBrojPrvog;
                    //prikazBrojRedova = prikazArtikalBrojRedova;
                    stilArtikalIliGrupa = stilArtikal;
                    break;
                case ARTIKAL_FAVORITE:
                    prikazRedniBrojPrvog = prikazFavoriteRedniBrojPrvog;
                    prikazBrojPrikazanihPlus1 = prikazFavoriteBrojPrikazanihPlus1;
                    prikazBrojRedova = prikazFavoriteBrojRedova;
                    stilArtikalIliGrupa = stilArtikal;
                    //this.ArtikalPodgrupe.getChildren().clear();
                    //this.ArtikalPodgrupe.setPrefHeight(0.0);
                    this.ArtikalFavorite.setVisible(true);
                    this.Artikal_DvaDela.setVisible(false);
                    
                    break;
                default:
            }
            
            int brojac=0;
            
            for(Object r : rs) {
                Map<String, String> red = (Map<String, String>) r;

                ArtikalButton buttonArtikalIliGrupa = (ArtikalButton)gdePrikazati.getChildren().get(brojac);

                brojac++;
                double cena = 0;
                try {
                    cena = Double.parseDouble(red.get("cena"));
                }catch (Exception e) {
                }
                
                String dozvoljenPopust = red.get("dozvoljenPopust");
                if(dozvoljenPopust == null) {
                    dozvoljenPopust = "";
                }
                
                String stampacID = red.get("stampacID");
                buttonArtikalIliGrupa.setId(red.get("id"));
                buttonArtikalIliGrupa.setText(red.get("naziv"));
                buttonArtikalIliGrupa.ImagePutanja = red.get("slika");
                try {
                    if (buttonArtikalIliGrupa.ImagePutanja != null && !buttonArtikalIliGrupa.ImagePutanja.equals("")) {
                        Image image = new Image(new FileInputStream(new File("images/" + buttonArtikalIliGrupa.ImagePutanja)),50,50,true,true);
                        buttonArtikalIliGrupa.setGraphic(new ImageView(image));
                    }
                } catch (Exception e) {
                    System.out.println("Greska u otvaranju slike " + "../images/" + buttonArtikalIliGrupa.ImagePutanja + "!");
                }
                buttonArtikalIliGrupa.prioritet = red.get("prioritet");
                buttonArtikalIliGrupa.skrNaziv = red.get("skrNaziv");
                buttonArtikalIliGrupa.NadredjenaGrupaID = red.get("GRUPA_ID");
                buttonArtikalIliGrupa.tip = red.get("tip");
                buttonArtikalIliGrupa.vrstaZaPrikaz = staSePrikazuje;
                buttonArtikalIliGrupa.cenaJedinicna = cena;
                if (dozvoljenPopust.equals("1"))
                    buttonArtikalIliGrupa.dozvoljenPopust = true;
                buttonArtikalIliGrupa.stampacID = stampacID;

                buttonArtikalIliGrupa.getStyleClass().add(stilArtikalIliGrupa);
                buttonArtikalIliGrupa.setOnAction(new EventHandler<ActionEvent>() {               
                                    @Override public void handle(ActionEvent e) {
                                        obradiIzabraniArtikalIliGrupu(e, gdePrikazati);
                                    }
                                });
                //gdePrikazati.getChildren().add((Node)buttonArtikalIliGrupa);
                if (brojac == this.prikazBrojPrikazanihPlus1-1)
                    break;
            }
            
            if (!rs.isEmpty()) {
                int ostaloPraznihDugmica = this.prikazBrojPrikazanihPlus1 - 1 - rs.size();
                while (ostaloPraznihDugmica>0) {
                    Button bPrazan = (ArtikalButton)gdePrikazati.getChildren().get(brojac);
                    bPrazan.getStyleClass().add(stilArtikliPrazanButton);
                    bPrazan.setDisable(true);
                    
                    ostaloPraznihDugmica--;
                    brojac++;
                }
            
                Button bPrethodniArtikliIliGrupe = (Button)gdePrikazati.getChildren().get(brojac);
                bPrethodniArtikliIliGrupe.getStyleClass().add(stilArtikalIliGrupa);
                if (this.prikazRedniBrojPrvog==1)
                    bPrethodniArtikliIliGrupe.setDisable(true);
                else {
                    bPrethodniArtikliIliGrupe.setDisable(false);
                    bPrethodniArtikliIliGrupe.getStyleClass().add(stilArtikliGrupeNextPrev);
                        }

                brojac++;
                Button bSledeciArtikliIliGrupe = (Button)gdePrikazati.getChildren().get(brojac);
                bSledeciArtikliIliGrupe.getStyleClass().add(stilArtikalIliGrupa);
                if (this.prikazBrojPrikazanihPlus1 == rs.size()) {
                    bSledeciArtikliIliGrupe.setDisable(false);
                    bSledeciArtikliIliGrupe.getStyleClass().add(stilArtikliGrupeNextPrev);
                }
                else
                    bSledeciArtikliIliGrupe.setDisable(true);
            }
        } catch (Exception e) {
            System.out.println("Greska u prikazu artikala!");
        } 
    }
/***************************************************************/
/********** KRAJ - PRIKAZ GRUPA, PODGRUPA I ARTIKALA ***********/
/***************************************************************/

    protected void obradiIzabraniArtikalIliGrupu(ActionEvent izabraniArtikalIliGrupa, Pane gdePrikazati) {
        Utils.postaviStil_ObrisiZaOstaleKontroleRoditelja(izabraniArtikalIliGrupa, stilButtonGrupeSelektovana);
        //((Node)izabraniArtikalIliGrupa.getSource()).getScene().lookup("artikliFavorite").getStyleClass().removeAll("buttonGrupeSelektovana");
        onButtonArtikalIliGrupa_click(izabraniArtikalIliGrupa, gdePrikazati);
    }
    
    protected void onButtonArtikalIliGrupa_click(ActionEvent izabraniArtikalIliGrupa, Pane gdePrikazati) {
        if (porudzbinaTrenutna.getBlokiranaPorudzbina()) {
            return;
        }
        
        VrsteGrupaIliArtikal vrstaZaPrikaz;
        ArtikalButton noviArtikal = null;
        
        try {
            noviArtikal = (ArtikalButton)izabraniArtikalIliGrupa.getSource();
            vrstaZaPrikaz = noviArtikal.getVrstaGrupaIliArtikal();
            if (!(vrstaZaPrikaz == ARTIKAL_DODATNI || vrstaZaPrikaz == ARTIKAL_OPISNI))
                selektovani = noviArtikal;
        } catch (Exception e) {
            //selektovaniFavorite = (Button)izabraniArtikalIliGrupa.getSource();
            vrstaZaPrikaz = ARTIKAL_FAVORITE;
            selektovani = null;
            selektovana = null;
        }

        //VrsteGrupaIliArtikal staSePrikazuje = ARTIKAL_GLAVNI;
        /**********************************************
         * Ovde obraditi promenu prikaza nakon klika za svakog pojedinacno
        * 
        * 
        */
        switch(vrstaZaPrikaz) {
            case GLAVNA_GRUPA:
//                prikazFavoriteMENJAJ = true;
                selektovanaPodgrupaID = selektovanaGlavnaGrupaID = selektovani.getId();
                prikazPodgrupaRedniBrojPrvog = 1;
                prikazArtikalRedniBrojPrvog = 1;
                refreshGrupeIliArtikla(this.ArtikalPodgrupe, POD_GRUPA);
                refreshGrupeIliArtikla(this.Artikal, ARTIKAL_GLAVNI);
                break;
            case POD_GRUPA:
                selektovanaPodgrupaID = selektovani.getId();
                prikazArtikalRedniBrojPrvog = 1;
                refreshGrupeIliArtikla(this.Artikal, ARTIKAL_GLAVNI);
                break;
            case ARTIKAL_GLAVNI:
                dodajArtikalUNovuTuru(selektovani);
                if (selektovani.getDaLiJeArtikalSlozen()) {
                    // TODO : Mora da zapamti koji je da bi njemu dodao opisne i dodatne artikle
                    selektovanArtikalID = selektovani.getId();
                    prikazPodgrupaRedniBrojPrvog = 1;
                    prikazArtikalRedniBrojPrvog = 1;
                    refreshGrupeIliArtikla(this.ArtikalPodgrupe, ARTIKAL_OPISNI);
                    refreshGrupeIliArtikla(this.Artikal, ARTIKAL_DODATNI);                    
                }
                break;
            case ARTIKAL_OPISNI:
            case ARTIKAL_DODATNI:
                // TODO : Mora da pronadje koji je artikal zadnji dodat da bi njemu dodao opisne i dodatne artikle
                //dodajArtikalUNovuTuru(selektovani);
//                dodajOpisniDodatniArtikalUNovuTuru(selektovani, noviArtikal);
                dodajOpisniDodatniArtikalUStavkuTure(selektovana, noviArtikal);
                break;
            case ARTIKAL_FAVORITE:
                if (selektovani != null) {
                    dodajArtikalUNovuTuru(selektovani);
                    //if (prikazFavoriteMENJAJ) {
                        if (selektovani.getDaLiJeArtikalSlozen()) {
                            // TODO : Mora da zapamti koji je da bi njemu dodao opisne i dodatne artikle
                            selektovanArtikalID = selektovani.getId();
                            prikazPodgrupaRedniBrojPrvog = 1;
                            prikazArtikalRedniBrojPrvog = 1;
                            refreshGrupeIliArtikla(this.ArtikalPodgrupe, ARTIKAL_OPISNI);
                            refreshGrupeIliArtikla(this.Artikal, ARTIKAL_DODATNI);                    
                        }
                    //}
                } else
                    refreshGrupeIliArtikla(this.Artikal, ARTIKAL_FAVORITE);
                break;
            default:
        }
    }
    
    public void dodajOpisniDodatniArtikalUStavkuTure(StavkaTure poslednjaDodataStavka, ArtikalButton artikalOpisniDodatni) {
        if (porudzbinaTrenutna.getBlokiranaPorudzbina()) {
            return;
        }
//        prikazRacunaGosta.setContent(null);
        
        sakrijSveTabele();
        
        Map<String, String> novaGlavnaStavka = null;
        StavkaTure nova = null;

        Map<String, String> novaStavkaTure = new HashMap<>();
        novaStavkaTure.put("id", "0");//artikalOpisniDodatni.getId());
        novaStavkaTure.put("ARTIKAL_ID", artikalOpisniDodatni.getId());
        novaStavkaTure.put("kolicina", "1");
        novaStavkaTure.put("brojStola", "" + poslednjaDodataStavka.getBrojStola());
        novaStavkaTure.put("dozvoljenPopust", "" + artikalOpisniDodatni.getDozvoljenPopust());
        novaStavkaTure.put("stampacID", "" + poslednjaDodataStavka.stampacID);
        
        if (artikalOpisniDodatni.getVrstaGrupaIliArtikal() == ARTIKAL_OPISNI) {
            novaStavkaTure.put("naziv", artikalOpisniDodatni.getText());
            novaStavkaTure.put("cena", "0");
            novaStavkaTure.put("cenaJedinicna", "0");
        } else if (artikalOpisniDodatni.getVrstaGrupaIliArtikal() == ARTIKAL_DODATNI) {
            novaStavkaTure.put("naziv", artikalOpisniDodatni.getText());
            novaStavkaTure.put("cena", Utils.getStringFromDouble(artikalOpisniDodatni.getCenaJedinicna()));
            novaStavkaTure.put("cenaJedinicna", Utils.getStringFromDouble(artikalOpisniDodatni.getCenaJedinicna()));
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
        if (artikalOpisniDodatni.getVrstaGrupaIliArtikal() == ARTIKAL_OPISNI) {
            poslednjaDodataStavka.dodajKolicinuArtikalOpisni(st);
        } else if (artikalOpisniDodatni.getVrstaGrupaIliArtikal() == ARTIKAL_DODATNI) {
            poslednjaDodataStavka.dodajKolicinuArtikalDodatni(st);
        }

        this.tableRefresh();
        this.prikaziTotalPopustNaplataTura(novaTura);
//        this.prikaziTotalPopustNaplataStavke(novaTura.listStavkeTure);
        this.porudzbinaNaplati.setText("Potvrdi ✓");
    }

    public void dodajArtikalUNovuTuru(ArtikalButton artikal) {
        if (porudzbinaTrenutna.getBlokiranaPorudzbina()) {
            return;
        }
        
        //TODO isto i ovde treba da se samo setuju sve tabele na empty i hide
//        prikazRacunaGosta.setContent(null);

        sakrijSveTabele();

        String idArtikla = artikal.getId();
        String nazivArtikla = artikal.getText();
        String cena = String.format("%1$,.2f", artikal.getCenaJedinicna());

        String[] imenaArgumenata = {"id"};
        String[] vrednostiArgumenata = {idArtikla};

        Map<String, String> novaStavkaTure = new HashMap<>();
        novaStavkaTure.put("id", "0");//artikal.getId());
        novaStavkaTure.put("ARTIKAL_ID", idArtikla);
        novaStavkaTure.put("naziv", nazivArtikla);
        novaStavkaTure.put("cena", cena);
        novaStavkaTure.put("cenaJedinicna", cena);
        novaStavkaTure.put("brojStola", "" + rmaster.RMaster.izabraniStoBroj);
        novaStavkaTure.put("dozvoljenPopust", "" + artikal.getDozvoljenPopust());
        novaStavkaTure.put("procenatPopusta", this.porudzbinaTrenutna.getPopustString());
        novaStavkaTure.put("stampacID", artikal.getStampacID());
        
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
                najveciBrojGosta = brojStola;
        }
        najveciBrojGosta = najveciBrojGosta + 1;

        Gost gost = new Gost(najveciBrojGosta);
        idTrenutnoIzabranogGosta = "" + najveciBrojGosta;
        this.porudzbinaTrenutna = new Porudzbina(gost);
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
        if (novaTura != null) {
            // TODO: Ovo treba preraditi da snimi nove ture svih porudzbina i da ih odstampa po stampacima
            
            // Ovo je valjda resilo snimanje
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
                    Stampa.getInstance().stampajTuru(novaTura);
                }
            }
            // Ovde resiti stampu po artiklima
            List<Object> newData = new ArrayList<>();
            prikaziFormu(newData,
                    ScreenMap.PRIKAZ_SALA,
                    true, 
                    (Node)event.getSource(), false
            );    
            return;
        }
        // TODO: Otvoriti formu za naplatu
        List<Object> newData = new ArrayList<>();
        newData.add(this.porudzbinaTrenutna);
        newData.add((Node)event.getSource());
        prikaziFormu(newData,
                ScreenMap.NAPLATA,
                false, 
                (Node)event.getSource(), 
                true
        );    
    }

    public void sakrijSveTabele() {
        for(Node node : listaTabela) {
            node.setVisible(false);
            node.setManaged(false);
            if (node instanceof Button) {
                ((Button) node).setPrefSize(0, 0); 
            }
            if (node instanceof RM_TableView) {
                ((RM_TableView) node).setPrefSize(0, 0);
            }
        }
    }
    
    public void prikaziKomponentu(Node komponenta)
    {
        komponenta.setVisible(true);
        komponenta.setManaged(true);
    }
}
