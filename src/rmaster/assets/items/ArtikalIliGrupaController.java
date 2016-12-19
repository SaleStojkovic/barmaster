/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
//import rmaster.assets.DBBroker;
import rmaster.assets.Utils;

/**
 *
 * @author Bosko
 */
public class ArtikalIliGrupaController {
    public final int GLAVNA_GRUPA = 1;
    public final int POD_GRUPA = 2;
    public final int ARTIKAL_GLAVNI = 3;
    public final int ARTIKAL_OPISNI = 4;
    public final int ARTIKAL_DODATNI = 5;
    public final int ARTIKAL_FAVORITE = 6;
    public List rs = null;
    
    public final String stilButtonGrupeSelektovana = "buttonGrupeSelektovana";
    public String stilArtikalIliGrupa = "buttonArtikli";
    public String stilArtikliPrazanButton = "buttonArtikliPrazan";
    public String stilArtikliGrupeNextPrev = "buttonArtikliGrupeNextPrev";
    //public String stilArtikliNextPrev = "buttonArtikliNextPrev";

    public final List<Node> artikalIliGrupaListeners = new ArrayList<>();
    
    public int vrstaArtikalIliGrupa;
    public String imeStoreProcedure;
    public Pane gp = null;
    public String idNadredjenogArtikalIliGrupa = "";
    
    public double roditeljSirina = 764.0;
    public final double defaultVisinaDugmeta = 64.0;

    public int prikazRedniBrojPrvog;
    public int prikazBrojPrikazanihPlus1;
    public int prikazBrojRedova;
    

    
    /* Konstruktor
    */
    public ArtikalIliGrupaController(Node itemGroupListener, String id){
        this.idNadredjenogArtikalIliGrupa = id;
        artikalIliGrupaListeners.add(itemGroupListener);
    }

    protected void refresh() {
    }
    

/* Prikazuje grupe, podgrupe, artikale, dodatne artikle ili opcione artikle
    u prosledjeni Pane
    */
    public void prikazArtikalIliGrupa() {
        try {
            gp = (Pane)artikalIliGrupaListeners.get(0);
            gp.getChildren().clear();
            
            // Odredjivanje sirine dugmica za podgrupe artikala
            //double roditeljSirina = gp.getParent().getBoundsInParent().getWidth();
            //Ovo sam uveo da bi radilo... vraca mi gore nula
            
            int brojPrikazanihUJednomRedu = (this.prikazBrojPrikazanihPlus1)/this.prikazBrojRedova;
            double artikalPodgrupaSirina = (roditeljSirina/brojPrikazanihUJednomRedu);
            int brojac=0;
            double sirina = 0.0;
            
            for(Object r : rs) {
                brojac++;
                Map<String, String> red = (Map<String, String>) r;
                //long id = Long.parseLong(red.get("id"));
                //String naziv = red.get("naziv");
                ArtikalButton buttonArtikalIliGrupa = 
                        new ArtikalButton(
                                red.get("naziv"),
                                red.get("slika"),
                                red.get("id"),
                                red.get("prioritet"),
                                red.get("skrNaziv"),
                                red.get("GRUPA_ID"),
                                red.get("tip"),
                                VrsteGrupaIliArtikal.GLAVNA_GRUPA,
                                Utils.getDoubleFromString(red.get("cena")),
                                red.get("dozvoljenPopust"),
                                red.get("stampacID")
                        );
                if ((brojac%brojPrikazanihUJednomRedu)==0) {
                    sirina = roditeljSirina - (brojPrikazanihUJednomRedu-1)*artikalPodgrupaSirina;
                    buttonArtikalIliGrupa.setPrefSize(sirina, defaultVisinaDugmeta);
                }
                else
                    buttonArtikalIliGrupa.setPrefSize(artikalPodgrupaSirina, defaultVisinaDugmeta);
                buttonArtikalIliGrupa.getStyleClass().add(stilArtikalIliGrupa);
                buttonArtikalIliGrupa.setOnAction(new EventHandler<ActionEvent>() {               
                                    @Override public void handle(ActionEvent e) {
                                        obradiIzabraniArtikalIliGrupu(e);
                                    }
                                });
                gp.getChildren().add((Node)buttonArtikalIliGrupa);
                if (brojac == this.prikazBrojPrikazanihPlus1-1)
                    break;
            }
            
            int ostaloPraznihDugmica = this.prikazBrojPrikazanihPlus1 - 1 - rs.size();
            while (ostaloPraznihDugmica>0) {
                Button bPrazan = new Button();
                bPrazan.getStyleClass().add(stilArtikliPrazanButton);
                bPrazan.setDisable(true);
                bPrazan.setPrefSize(artikalPodgrupaSirina, defaultVisinaDugmeta);

                gp.getChildren().add(bPrazan);
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
                                    prikazRedniBrojPrvog = prikazRedniBrojPrvog - prikazBrojPrikazanihPlus1 + 1;
                                    refresh();
                                }
                            });
            gp.getChildren().add(bPrethodniArtikliIliGrupe);

            sirina = roditeljSirina - (brojPrikazanihUJednomRedu-1)*artikalPodgrupaSirina - artikalPodgrupaSirina/2;
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
                                    prikazRedniBrojPrvog = prikazRedniBrojPrvog + prikazBrojPrikazanihPlus1 - 1;
                                    refresh();
                                        //obradiIzabraniArtikalIliGrupu(e);
                                }
                            });
            gp.getChildren().add(bSledeciArtikliIliGrupe);
        } catch (Exception e) {
            System.out.println("Greska u prikazu artikala!");
        } 
    }
    
    protected void obradiIzabraniArtikalIliGrupu(ActionEvent izabraniArtikalIliGrupa) {
        Utils.postaviStil_ObrisiZaOstaleKontroleRoditelja(izabraniArtikalIliGrupa, stilButtonGrupeSelektovana);
        //((Node)izabraniArtikalIliGrupa.getSource()).getScene().lookup("artikliFavorite").getStyleClass().removeAll("buttonGrupeSelektovana");
        onButtonArtikalIliGrupa_click(izabraniArtikalIliGrupa);
    }
    
    protected void onButtonArtikalIliGrupa_click(ActionEvent izabraniArtikalIliGrupa) {
//        new GrupaController(((Button)izabraniArtikalIliGrupa.getSource()).getScene().lookup("#ArtikalPodgrupe"), ((Button)izabraniArtikalIliGrupa.getSource()).getId(), POD_GRUPA, 192, 3);
    }
    
    protected String[] vratiArgumenteVrednosti() {
        String[] vrednostiArgumenata = {
                idNadredjenogArtikalIliGrupa,
                "" + (this.prikazRedniBrojPrvog - 1),
                "" + (this.prikazBrojPrikazanihPlus1)
            };
        return vrednostiArgumenata;
    }
}
