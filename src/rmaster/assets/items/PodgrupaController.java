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
public class PodgrupaController extends ArtikalIliGrupaController{
    public PodgrupaController(Node itemGroupListener, String id) {
        super(itemGroupListener, id);
        this.vrstaArtikalIliGrupa = POD_GRUPA;
        prikazRedniBrojPrvog = 1;
        prikazBrojPrikazanihPlus1 = 12;
        prikazBrojRedova = 3;
        stilArtikalIliGrupa = "buttonArtikliPodgrupe";

        this.refresh();  
    }
    
    @Override
    protected void refresh() {
        try {
            imeStoreProcedure = "getArtikliPodgrupeOdGrupe";
            String[] imenaArgumenata = {"nadredjenaGrupa", "brojPrvogZapisa", "brojZapisa"};
            String[] vrednostiArgumenata = super.vratiArgumenteVrednosti();
            rs = new DBBroker().runStoredProcedure(imeStoreProcedure, imenaArgumenata, vrednostiArgumenata);
        } catch (Exception e) {
            System.out.println("View \"" + imeStoreProcedure + "\" fetch error!");
        }
        super.prikazArtikalIliGrupa();
    }

    @Override
    protected void onButtonArtikalIliGrupa_click(ActionEvent izabraniArtikalIliGrupa) {
        //System.out.println("Artikal: " + ((Button)izabraniArtikalIliGrupa.getSource()).getId());
        new ArtikalController(((Button)izabraniArtikalIliGrupa.getSource()).getScene().lookup("#Artikal"), ((Button)izabraniArtikalIliGrupa.getSource()).getId());
    }

    
}