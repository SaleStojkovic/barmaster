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
public class ArtikalDodatniController extends ArtikalIliGrupaController{
    public ArtikalDodatniController(Node itemGroupListener, String id) {
        super(itemGroupListener, id);
        this.vrstaArtikalIliGrupa = ARTIKAL_DODATNI;
        prikazRedniBrojPrvog = 1;
        prikazBrojPrikazanihPlus1 = 20;
        prikazBrojRedova = 5;
        stilArtikalIliGrupa = "buttonArtikli";

        this.refresh();  
    }
    
    @Override
    protected void refresh() {
        try {
            imeStoreProcedure = "getArtikalDodaci";
            String[] imenaArgumenata = {"ArtikalID", "brojPrvogZapisa", "brojZapisa"};
            String[] vrednostiArgumenata = super.vratiArgumenteVrednosti();
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