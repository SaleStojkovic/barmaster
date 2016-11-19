/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import rmaster.assets.TastaturaVrsta;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.IntStream;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import rmaster.assets.DBBroker;
import rmaster.assets.FXMLDocumentController;
import rmaster.assets.ScreenMap;
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
    
    @FXML
    public HBox ArtikalGrupe;
    
    @FXML
    public FlowPane ArtikalPodgrupe;
    
    @FXML
    public FlowPane Artikal;
    
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
    
    // Sirina dela u kome se prikazuju grupe, podgrupe i artikli
    public double roditeljSirina = 732.0;
    public final double defaultVisinaDugmeta = 70.0;
        
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

    public boolean prikazFavoriteMENJAJ = false;

    public String selektovanaGlavnaGrupaID = "0";
    public String selektovanaPodgrupaID = "0";
    public String selektovanArtikalID = "0";
    
    public String stilButtonGrupeSelektovana = "buttonGrupeSelektovana";
    public String stilArtikliPrazanButton = "buttonArtikliPrazan";
    public String stilArtikliGrupeNextPrev = "buttonArtikliGrupeNextPrev";

    //Lokalne varijable 
    public String idTrenutnoIzabranogGosta;

    public int popustTrenutnogGosta = 0;
    
    public Map<String,List> racuniStolaPoGostima = new HashMap<>();
    
    //public List<StavkaTure> listNovaTuraGosta = new ArrayList<>();
    
    public TableView<Map<String, String>> tabelaNovaTuraGosta = new TableView<>();

    private List<Porudzbina> porudzbineStola = new ArrayList<Porudzbina>();
  
    int[] sirinaKolonaTabele = {0, 170, 30, 61, 0};
    
    List<Map<String, String>> tureTrenutnoIzabranogGosta = new ArrayList<>();
    
    List<Tura> listTure = new ArrayList<>();
    Tura novaTura = new Tura();
    Porudzbina porudzbinaTrenutna = null;
    
    ArtikalButton selektovani = null;
    StavkaTure selektovana = null;
    
    @Override
    public void initData(Map<String, String> data) {

        try {
            refreshGrupeIliArtikla(this.ArtikalGrupe, GLAVNA_GRUPA);
            refreshGrupeIliArtikla(this.Artikal, ARTIKAL_FAVORITE);

            prikaziPorudzbinu();

            Button dugme = (Button)prikazGostiju.getChildren().get(0);

            IntStream.range(0, 1).forEach(
                i -> dugme.fire()
            );

            
        } catch (Exception e) {
            System.out.println("Greska u pozivu SP get_racuniKonobaraKojiNisuZatvoreni! - " + e.toString());
        }
    }
    
    public void popuniPorudzbineStola() {
        List racuniStola = DBBroker.get_PorudzbineStolaIKonobara();
        for (Object racun : racuniStola) {

            Map<String, String> red = (Map<String, String>) racun;

            String brojNovogGosta = red.get("gost");
            
            racuniStolaPoGostima.put(
                    brojNovogGosta, 
                    DBBroker.getRecordSetIzStoreProcedureZaParametar(
                            "getStavkeRacuna", 
                            "racunID", 
                            red.get("id")
                    )
            ); 
            Button b = new Button(brojNovogGosta);
            b.setId(brojNovogGosta);
            
            b.setPrefSize(50, 50);
            b.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    String gost = ((Button)e.getSource()).getId();
                                    Utils.postaviStil_ObrisiZaOstaleKontroleRoditelja(e, stilButtonGrupeSelektovana);
                                    prikaziTureZaGosta(gost);
                                }
                            });

            this.prikazGostiju.getChildren().add(b);
        }
    }

    public void prikaziPorudzbinu() {
           
            prikazRacunaGosta.setHbarPolicy(ScrollBarPolicy.NEVER);
            prikazRacunaGosta.setVbarPolicy(ScrollBarPolicy.NEVER);
            
            List racuniStola = DBBroker.get_PorudzbineStolaIKonobara();
            
            for (Object racun : racuniStola) {
            
                Map<String, String> red = (Map<String, String>) racun;
                
                String brojNovogGosta = red.get("gost");
                porudzbinaTrenutna = new Porudzbina(new Gost(brojNovogGosta), red.get("id"));
                porudzbineStola.add(porudzbinaTrenutna);

                Button b = new Button(brojNovogGosta);
                b.setId(brojNovogGosta);
                
                b.setPrefSize(50, 50);
                b.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        String gost = ((Button)e.getSource()).getId();
                                        Utils.postaviStil_ObrisiZaOstaleKontroleRoditelja(e, stilButtonGrupeSelektovana);
                                        if (novaTura != null) {
                                            novaTura = null;
                                        }

                                        for (Porudzbina porudzbina : porudzbineStola) {
                                            if (porudzbina.getGost().getGostID() == Long.parseLong(gost)) {
                                                porudzbinaTrenutna = porudzbina;
                                                prikaziPorudzbinu(porudzbinaTrenutna);
                                                break;
                                            }
                                        }
                                    }
                                });

                this.prikazGostiju.getChildren().add(b);
        }       
        prikazGostijuScrollPane.setContent(prikazGostiju);
    
    }
    public void prikaziPorudzbinu(Porudzbina porudzbina) {
        prikazRacunaGosta.setContent(null);  
        prikazRacunaGosta.setFitToWidth(true);
        //listNovaTuraGosta.clear();
                                                
        VBox sveTure = new VBox();
        VBox paneTura = new VBox();
        paneTura.setPadding(new Insets(0, 0, 0, 0));
        
        for (Tura tura : porudzbina.getTure()) {
            TableView<Map<String, String>> tabelaNoveTure = new TableView<>();
            tabelaNoveTure.setSelectionModel(null);
            
            tabelaNoveTure = tableHelper.formatirajTabelu(
                    tabelaNoveTure,
                    tura.dajTuru(),
                    sirinaKolonaTabele
            );
            
            Button ponoviTuru = new Button();
            ponoviTuru.setPrefSize(287, 40);
            ponoviTuru.setText("Ponovi Turu");
            ponoviTuru.setId("" + tura.getTuraID());
            ponoviTuru.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {
                                String turaId = ((Button)e.getSource()).getId();
                                ponoviTuru(turaId);
                            }
                        }); 
            paneTura.getChildren().add(tabelaNoveTure);
            paneTura.getChildren().add(ponoviTuru);
        }
        sveTure.getChildren().add(paneTura);
        
        this.prikaziTotalPopustNaplataTura(porudzbina.getTure());
        
        this.prikazRacunaGosta.setContent(sveTure);
    }

    public void prikaziTureZaGosta(
            String gost
    ) {
        idTrenutnoIzabranogGosta = gost;
        prikazRacunaGosta.setContent(null);  
        prikazRacunaGosta.setFitToWidth(true);
        //listNovaTuraGosta.clear();
                                                
        VBox sveTure = new VBox();
        VBox paneTura = new VBox();
        paneTura.setPadding(new Insets(0, 0, 0, 0));

        List<Map<String, String>> racuniJednogGosta = racuniStolaPoGostima.get(gost);
        
        List<Tura> listTuraZaPrikazNaplate = new ArrayList();
        
        String racunId = racuniJednogGosta.get(0).get("RACUN_ID");

        String[] imenaArgumenata = {"idRacuna"};
        String[] vrednostiArgumenata = {racunId};
        List<Map<String, String>> tureJednogGosta = runStoredProcedure(
                "getPorudzbinaTure", 
                imenaArgumenata, 
                vrednostiArgumenata
        );        
        
        for (Map<String, String> tura : tureJednogGosta) {
                String turaId = tura.get("id");
                Tura turaModel = new Tura(turaId);
                
                listTuraZaPrikazNaplate.add(turaModel);
                
                TableView<Map<String, String>> tabelaNoveTure = new TableView<>();
                tabelaNoveTure.setSelectionModel(null);
                
                tabelaNoveTure = tableHelper.formatirajTabelu(tabelaNoveTure,
                        turaModel.dajTuru(),
                        sirinaKolonaTabele
                );
                
                Button ponoviTuru = new Button();
                ponoviTuru.setPrefSize(287, 40);
                ponoviTuru.setText("Ponovi Turu");
                ponoviTuru.setId(turaId);
                ponoviTuru.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    String turaId = ((Button)e.getSource()).getId();
                                    ponoviTuru(turaId);
                                }
                            }); 
                paneTura.getChildren().add(tabelaNoveTure);
                paneTura.getChildren().add(ponoviTuru);
            }
        
            sveTure.getChildren().add(paneTura);
            
            this.prikaziTotalPopustNaplataTura(listTuraZaPrikazNaplate);
            
            this.prikazRacunaGosta.setContent(sveTure);
    }    
    
    public void ponoviTuru(String izabranaTuraIdString) {
        //OVDE SE SADA PONAVLJA TURA
        long izabranaTuraID = Long.parseLong(izabranaTuraIdString);
        novaTura = null;
        prikazRacunaGosta.setContent(null);
                              
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
            
            TableView<Map<String, String>> novaTabela = new TableView<>();
            tabelaNovaTuraGosta.getItems().clear();
            tabelaNovaTuraGosta = tableHelper.formatirajTabelu(
                        novaTabela,
                        novaTura.dajTuru(),
                        sirinaKolonaTabele
                );
            tabelaNovaTuraGosta.getSelectionModel().select(novaTura.listStavkeTure.size() - 1);

            prikazRacunaGosta.setContent(tabelaNovaTuraGosta);
            this.prikaziTotalPopustNaplataStavke(novaTura.listStavkeTure);
        }
    }

    

    public void prikaziSalu(ActionEvent event) {
        Map<String, String> newData = new HashMap<>();
        prikaziFormu(
                newData,
                ScreenMap.PRIKAZ_SALA,
                true, 
                (Node)event.getSource()
        );
    }
    
    public void prikaziArtikleFavorite(ActionEvent event) {
        prikazFavoriteMENJAJ = false;
        onButtonArtikalIliGrupa_click(event, this.Artikal);
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imeKonobara.setText(ulogovaniKonobar.imeKonobara);
        
        String[] uslovneKolone = {"id"};
        String[] uslovneVrednosti = {rmaster.RMaster.izabraniSto};
        
        List<Map<String, String>> resultList = vratiKoloneIzTabele(
                "stonaziv", 
                uslovneKolone, 
                uslovneVrednosti
        );
        
        String imeStola = rmaster.RMaster.izabraniSto;
        
        if (!resultList.isEmpty()) {
            Map<String, String> red = resultList.get(0);

            imeStola = resultList.get(0).get("naziv");

            if (red.get("naziv").isEmpty()) {
                imeStola = red.get("broj");
            }
        }
        izabraniSto.setText("Sto: " + imeStola);
       
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
            rs = new DBBroker().runStoredProcedure(imeStoreProcedure, imenaArgumenata, vrednostiArgumenata);
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
        prikazArtikalIliGrupa(rs, gdePrikazati, staSePrikazuje);
    }
    
/***************************************************************/
/************** PRIKAZ GRUPA, PODGRUPA I ARTIKALA **************/
/***************************************************************/
    public void prikazArtikalIliGrupa(List rs, Pane gdePrikazati, VrsteGrupaIliArtikal staSePrikazuje) {
        try {
            this.ArtikalPodgrupe.setPrefHeight(prikazPodgrupaBrojRedova * defaultVisinaDugmeta);
            this.Artikal.setPrefHeight(prikazArtikalBrojRedova * defaultVisinaDugmeta);
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
                    prikazRedniBrojPrvog = prikazPodgrupaRedniBrojPrvog;
                    prikazBrojPrikazanihPlus1 = prikazPodgrupaBrojPrikazanihPlus1;
                    prikazBrojRedova = prikazPodgrupaBrojRedova;
                    stilArtikalIliGrupa = stilPodgrupa;
                    break;
                // Za artikle i dodatne artikle
                case ARTIKAL_DODATNI:
                case ARTIKAL_GLAVNI:
                    prikazRedniBrojPrvog = prikazArtikalRedniBrojPrvog;
                    prikazBrojPrikazanihPlus1 = prikazArtikalBrojPrikazanihPlus1;
                    prikazBrojRedova = prikazArtikalBrojRedova;
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
                                cena
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
                prikazFavoriteMENJAJ = true;
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
                    if (prikazFavoriteMENJAJ) {
                        if (selektovani.getDaLiJeArtikalSlozen()) {
                            // TODO : Mora da zapamti koji je da bi njemu dodao opisne i dodatne artikle
                            selektovanArtikalID = selektovani.getId();
                            prikazPodgrupaRedniBrojPrvog = 1;
                            prikazArtikalRedniBrojPrvog = 1;
                            refreshGrupeIliArtikla(this.ArtikalPodgrupe, ARTIKAL_OPISNI);
                            refreshGrupeIliArtikla(this.Artikal, ARTIKAL_DODATNI);                    
                        }
                    }
                } else
                    refreshGrupeIliArtikla(this.Artikal, ARTIKAL_FAVORITE);
                break;
            default:
        }
    }
    
    public void dodajOpisniDodatniArtikalUStavkuTure(StavkaTure poslednjaDodataStavka, ArtikalButton artikalOpisniDodatni) {
        prikazRacunaGosta.setContent(null);
        Map<String, String> novaGlavnaStavka = null;
        StavkaTure nova = null;
        
        Map<String, String> novaStavkaTure = new HashMap<>();
        novaStavkaTure.put("id", artikalOpisniDodatni.getId());
        novaStavkaTure.put("ARTIKAL_ID", artikalOpisniDodatni.getId());
        novaStavkaTure.put("kolicina", "1");
        
        if (artikalOpisniDodatni.getVrstaGrupaIliArtikal() == ARTIKAL_OPISNI) {
            
            novaStavkaTure.put("naziv", "--> " + artikalOpisniDodatni.getText());
            novaStavkaTure.put("cena", "0");
            novaStavkaTure.put("cenaJedinicna", "0");
            
        } else if (artikalOpisniDodatni.getVrstaGrupaIliArtikal() == ARTIKAL_DODATNI) {
            
            novaStavkaTure.put("naziv", "-> " + artikalOpisniDodatni.getText());
            novaStavkaTure.put("cena", Utils.getStringFromDouble(artikalOpisniDodatni.getCenaJedinicna()));
            novaStavkaTure.put("cenaJedinicna", Utils.getStringFromDouble(artikalOpisniDodatni.getCenaJedinicna()));
        }
        
        if (poslednjaDodataStavka.getKolicina()>1) {
            novaGlavnaStavka = new HashMap<>();
            novaGlavnaStavka.put("id", "" + poslednjaDodataStavka.stavkaTureID);
            novaGlavnaStavka.put("ARTIKAL_ID", poslednjaDodataStavka.getArtikalIDString());
            novaGlavnaStavka.put("kolicina", "1");
            novaGlavnaStavka.put("naziv", "" + poslednjaDodataStavka.imeArtikla);
            novaGlavnaStavka.put("cena", "" + poslednjaDodataStavka.cena);
            novaGlavnaStavka.put("cenaJedinicna", "" + poslednjaDodataStavka.cenaJedinicna);
            
            poslednjaDodataStavka.smanjiKolicinu();
            nova = new StavkaTure(novaGlavnaStavka);
            
            novaTura.listStavkeTure.add(nova);
            //listNovaTuraGosta.add(nova);
            poslednjaDodataStavka = nova;
            selektovana = nova;
        }
        
        StavkaTure st = new StavkaTure(novaStavkaTure);
        if (artikalOpisniDodatni.getVrstaGrupaIliArtikal() == ARTIKAL_OPISNI) {
            poslednjaDodataStavka.addArtikalOpisni(st);
        } else if (artikalOpisniDodatni.getVrstaGrupaIliArtikal() == ARTIKAL_DODATNI) {
            poslednjaDodataStavka.addArtikalDodatni(st);
        }

        this.tableRefresh();
        this.prikaziTotalPopustNaplataStavke(novaTura.listStavkeTure);

    }

    public void dodajArtikalUNovuTuru(ArtikalButton artikal) {
        prikazRacunaGosta.setContent(null);
        
        String idArtikla = artikal.getId();
        String nazivArtikla = artikal.getText();
        
        String[] imenaArgumenata = {"id"};
        String[] vrednostiArgumenata = {idArtikla};
                
        String cena = String.format("%1$,.2f", artikal.getCenaJedinicna());
        
        if (artikal.getVrstaGrupaIliArtikal() == ARTIKAL_OPISNI) {
            idArtikla = "-> " + idArtikla;
            cena = "0";
        }

        Map<String, String> novaStavkaTure = new HashMap<>();
        novaStavkaTure.put("id", artikal.getId());
        novaStavkaTure.put("ARTIKAL_ID", idArtikla);
        novaStavkaTure.put("naziv", nazivArtikla);
        novaStavkaTure.put("cena", cena);
        novaStavkaTure.put("cenaJedinicna", cena);
        
        
        this.dodajStavkuUNovuTuru(novaStavkaTure);

        this.tableRefresh();
        this.prikaziTotalPopustNaplataStavke(novaTura.listStavkeTure);

    }
       
    private void dodajStavkuUNovuTuru(Map<String, String> novaStavka) {
        
        if (novaTura == null) {
            novaTura = new Tura();
            
        }
        novaStavka.put("kolicina", "1");
        StavkaTure novaStavkaModel = new StavkaTure(novaStavka);
        
        StavkaTure stavka = novaTura.getStavkaTureByArtikalID(novaStavkaModel.getArtikalID());
        if ((stavka != null) && (stavka.getArtikalID() == novaStavkaModel.getArtikalID())) {
            if (!stavka.getImaDodatneIliOpisneArtikle()) {
                stavka.povecajKolicinu();
                selektovana = stavka;
                return;
            }
        }

        novaTura.listStavkeTure.add(novaStavkaModel);
        selektovana = novaStavkaModel;
    }
    
    
    private void promeniKolicinuStavkeTure(
            Map<String, String> izabranaStavkaTure,
            int novaKolicina
    ) {
        prikazRacunaGosta.setContent(null);
        
        String imeArtikla = izabranaStavkaTure.get("naziv");
        
        for (int i = 0; i < novaTura.listStavkeTure.size(); i++) {
            StavkaTure stavka = novaTura.listStavkeTure.get(i);
            
            if (stavka.imeArtikla.equals(imeArtikla)) {
                novaTura.listStavkeTure.remove(i);
                
                double novaCena = (stavka.cena / stavka.kolicina) * novaKolicina;
                
                stavka.promeniKolicinu(novaKolicina);
                if (stavka.kolicina != 0) {
                    stavka.cena = novaCena;

                    novaTura.listStavkeTure.add(i, stavka);
                }
            }
        }
        
        this.tableRefresh();
        this.prikaziTotalPopustNaplataStavke(novaTura.listStavkeTure);

    }
    
    
    
    
    public void setKolicinaStavkeTure(ActionEvent event) {
             
        if (tabelaNovaTuraGosta.getItems().isEmpty()) {
            return;
        }
        
        Map<String, String> izabraniRed = tabelaNovaTuraGosta.getSelectionModel().getSelectedItem();

        TastaturaController tastatura = new TastaturaController(TastaturaVrsta.UNOS_IZNOSA);
        Optional<String> result = tastatura.showAndWait();
        
        if (result.isPresent()){
            
            Double novaKolicina = Double.parseDouble(result.get());
            
            if (novaKolicina != 0) {
                novaTura.getStavkaTureByStavkaID(Long.parseLong(izabraniRed.get("id")));
                return;
            } 
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Greška!");
            alert.setHeaderText("Neispravna količina!");
            alert.setContentText("Unesite novu količinu.");
            alert.showAndWait();
        }
    }
    
    public void izbrisiStavkuNoveTure(ActionEvent event) {
        if (tabelaNovaTuraGosta.getItems().isEmpty()) {
            return;
        }
        
        novaTura.listStavkeTure
                .remove(
                        novaTura.getStavkaTureByStavkaID(
                                        Long.parseLong(
                                                tabelaNovaTuraGosta.getSelectionModel()
                                                           .getSelectedItem()
                                                           .get("naziv"))));

        this.tableRefresh();
        this.prikaziTotalPopustNaplataStavke(novaTura.listStavkeTure);
    }
    
    private void tableRefresh() {
        
        List<Map<String, String>> listTura = new ArrayList<>();
        
        for (StavkaTure novaStavka : novaTura.listStavkeTure) {
            listTura.add(novaStavka.dajStavkuTure());
            for (StavkaTure stavkaDodatni : novaStavka.getArtikliDodatni()) {
                listTura.add(stavkaDodatni.dajStavkuTure());
            }
            for (StavkaTure stavkaOpisni : novaStavka.getArtikliOpisni()) {
                listTura.add(stavkaOpisni.dajStavkuTure());
            }
        }
                
        TableView<Map<String, String>> novaTabela = new TableView<>();
        
        tabelaNovaTuraGosta.getItems().clear();
        
        if (listTura.isEmpty()) {
            return;
        }
            
        tabelaNovaTuraGosta = tableHelper.formatirajTabelu(novaTabela,
                    listTura,
                    sirinaKolonaTabele
            );

        tabelaNovaTuraGosta.getSelectionModel().select(listTura.size() - 1);

        prikazRacunaGosta.setContent(tabelaNovaTuraGosta);
    }
    
//    //TODO
//    public void unesiNovuTuruGosta() {
//        String idGosta = idTrenutnoIzabranogGosta;
//        String idStola = rmaster.RMaster.izabraniSto;
//        
//        String prirpemljena = "0";
//        String uPripremi = "0";
//        String RACUN_ID = "";
//    }
    
    
    public void prikaziTotalPopustNaplataTura(List<Tura> listTure) {
        
        if (listTure.isEmpty()) {
            this.total.setText("0.00");
            this.popust.setText("0.00%");
            this.naplata.setText("0.00");
            return;
        }

        List<Map<String, String>> totalPopustNaplata = new ArrayList<>();
        
        for (Tura tura : listTure) {
            List<Map<String, String>> listStavkeTure = tura.dajTuru();
            for (Map<String, String> red : listStavkeTure) {
                totalPopustNaplata.add(red);
            }
        }
        this.stampajTotalPopustNaplata(totalPopustNaplata);
    }
    
    
    public void prikaziTotalPopustNaplataStavke(List<StavkaTure> listaStavkiTure) {
        
        if (listaStavkiTure.isEmpty()) {
            this.total.setText("0.00");
            this.popust.setText("0.00%");
            this.naplata.setText("0.00");
            return;
        }

        List<Map<String, String>> totalPopustNaplata = new ArrayList<>();

        for (StavkaTure stavka : listaStavkiTure) {
               totalPopustNaplata.add(stavka.dajStavkuTure());
            }
        this.stampajTotalPopustNaplata(totalPopustNaplata);
    }
    
    public void stampajTotalPopustNaplata(List<Map<String, String>> totalPopustNaplata ) {
        
        double total = 0;

        for(Map<String, String> red : totalPopustNaplata) {
            total += Utils.getDoubleFromString(red.get("cena"));
        }

        double popust = porudzbinaTrenutna.getGost().getProcenatPopusta();
        double naplata = total * (100 - popust) / 100;

        this.total.setText(Utils.getStringFromDouble(total));
        this.popust.setText(Utils.getStringFromDouble(popust) + "%");
        this.naplata.setText(Utils.getStringFromDouble(naplata));
    }
    
    public void dodajNovogGosta(ActionEvent event) {
        
        ObservableList<Node> gostiButtons = prikazGostiju.getChildren();
        Button noviGost = new Button();
        
        int brojNovogGosta = 1;
        
        if (!gostiButtons.isEmpty()) {
            brojNovogGosta = gostiButtons.size() + 1;
        }
        
        //TODO ovde se sada upisuje novi gost u bazu.
        /*Za prvog gosta se upisuje id=autoinc., idRacuna=56789, gost=1, ostaliRacuni=NULL*/
        
        
        noviGost.setId("" + brojNovogGosta);
        noviGost.setText("G" + brojNovogGosta);
        noviGost.setPrefSize(50, 50);
        noviGost.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        String gost = ((Button)e.getSource()).getId();
                                        Utils.postaviStil_ObrisiZaOstaleKontroleRoditelja(e, stilButtonGrupeSelektovana);
                                        if (novaTura != null) {
                                            novaTura = null;
                                        }
                                        prikaziTureZaGosta(gost);
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
    
    
    public void otvoriLojalnost(ActionEvent event) {
        Map<String, String> newData = new HashMap<>();
        prikaziFormu(
                newData,
                ScreenMap.LOJALNOST,
                true, 
                (Node)event.getSource()
        );
    }
}
