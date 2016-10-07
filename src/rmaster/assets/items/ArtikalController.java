/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets.items;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import rmaster.assets.DBBroker;

/**
 *
 * @author Bosko
 */
public class ArtikalController extends ArtikalIliGrupaController{
    public ArtikalController(Node itemGroupListener, String id) {
        super(itemGroupListener, id);
        this.vrstaArtikalIliGrupa = ARTIKAL_GLAVNI;
        prikazRedniBrojPrvog = 1;
        prikazBrojPrikazanihPlus1 = 20;
        prikazBrojRedova = 5;
        stilArtikalIliGrupa = "buttonArtikli";
        
        this.refresh();  
    }
    
    @Override
    protected void refresh() {
        try {
            imeStoreProcedure = "getArtikliGrupe";
            String[] imenaArgumenata = {"podgrupaID", "brojPrvogZapisa", "brojZapisa"};
            String[] vrednostiArgumenata = super.vratiArgumenteVrednosti();
            rs = new DBBroker().runStoredProcedure(imeStoreProcedure, imenaArgumenata, vrednostiArgumenata);
        } catch (Exception e) {
            System.out.println("View \"" + imeStoreProcedure + "\" fetch error!");
        }
        super.prikazArtikalIliGrupa();
    }

    @Override
    protected void onButtonArtikalIliGrupa_click(ActionEvent izabraniArtikalIliGrupa) {
        System.out.println("Artikal: " + ((Button)izabraniArtikalIliGrupa.getSource()).getId());
        if (((ArtikalButton)izabraniArtikalIliGrupa.getSource()).getDaLiJeArtikalSlozen()) {
            new ArtikalOpisniController(((Button)izabraniArtikalIliGrupa.getSource()).getScene().lookup("#ArtikalPodgrupe"), ((Button)izabraniArtikalIliGrupa.getSource()).getId());
            new ArtikalDodatniController(((Button)izabraniArtikalIliGrupa.getSource()).getScene().lookup("#Artikal"), ((Button)izabraniArtikalIliGrupa.getSource()).getId());
        }
    }

    
}