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
public class ArtikalOpisniController extends ArtikalIliGrupaController{
    public ArtikalOpisniController(Node itemGroupListener, String id) {
        super(itemGroupListener, id);
        this.vrstaArtikalIliGrupa = ARTIKAL_OPISNI;
        prikazRedniBrojPrvog = 1;
        prikazBrojPrikazanihPlus1 = 12;
        prikazBrojRedova = 3;
        stilArtikalIliGrupa = "buttonArtikliPodgrupe";

        this.refresh();  
    }
    
    @Override
    protected void refresh() {
        try {
            imeStoreProcedure = "getArtikalAtributi";
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
        System.out.println("Artikal - opisni: " + ((Button)izabraniArtikalIliGrupa.getSource()).getId());
        //new GrupaController(((Button)izabraniArtikalIliGrupa.getSource()).getScene().lookup("#ArtikalPodgrupe"), ((Button)izabraniArtikalIliGrupa.getSource()).getId(), POD_GRUPA, 192, 3);
    }
}
