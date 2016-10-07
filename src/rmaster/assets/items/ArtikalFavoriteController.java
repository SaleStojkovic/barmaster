/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets.items;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import rmaster.assets.DBBroker;

/**
 *
 * @author Bosko
 */
public class ArtikalFavoriteController extends ArtikalIliGrupaController{
    public ArtikalFavoriteController(Node itemGroupListener, String id) {
        super(itemGroupListener, id);
        this.vrstaArtikalIliGrupa = ARTIKAL_FAVORITE;
        prikazRedniBrojPrvog = 1;
        prikazBrojPrikazanihPlus1 = 20;
        prikazBrojRedova = 5;
        stilArtikalIliGrupa = "buttonArtikli";

        this.refresh();  
    }
    
    @Override
    protected void refresh() {
        try {
            imeStoreProcedure = "getArtikliFavorite";
            String[] imenaArgumenata = {"brojPrvogZapisa", "brojZapisa"};
            String[] vrednostiArgumenata = {"" + (this.prikazRedniBrojPrvog - 1),"" + (this.prikazBrojPrikazanihPlus1)};
            rs = new DBBroker().runStoredProcedure(imeStoreProcedure, imenaArgumenata, vrednostiArgumenata);
        } catch (Exception e) {
            System.out.println("View \"" + imeStoreProcedure + "\" fetch error!");
        }
        super.prikazArtikalIliGrupa();
    }

    @Override
    protected void onButtonArtikalIliGrupa_click(ActionEvent izabraniArtikalIliGrupa) {
        System.out.println("Artikal - dodatni: " + ((Button)izabraniArtikalIliGrupa.getSource()).getId());
    }
}