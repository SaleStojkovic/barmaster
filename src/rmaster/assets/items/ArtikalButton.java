/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets.items;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Bosko
 */
public class ArtikalButton extends Button{
    private String ImagePutanja;
    private Image image;
    private String prioritet;
    private String skrNaziv;
    private String NadredjenaGrupaID;
    private String tip;
    private VrsteGrupaIliArtikal vrstaZaPrikaz;
    
    public ArtikalButton(
            String name, 
            String imagePutanja, 
            String id, 
            String prioritet, 
            String skrNaziv, 
            String NadredjenaGrupaID,
            String tip,
            VrsteGrupaIliArtikal vrstaZaPrikaz
    )
    {
        this.idProperty().set(id);
        this.setText(name);
        this.ImagePutanja = imagePutanja;
        try {
            if (this.ImagePutanja != null && !this.ImagePutanja.equals("")) {
                image = new Image(getClass().getResourceAsStream("style/img/" + imagePutanja));
                this.setGraphic(new ImageView(image));
            }
        } catch (Exception e) {
            System.out.println("Greska u otvaranju slike " + "style/img/" + imagePutanja + "!");
        }
        this.prioritet = prioritet;
        this.skrNaziv = skrNaziv;
        this.NadredjenaGrupaID = NadredjenaGrupaID;
        this.tip = tip;
        this.vrstaZaPrikaz = vrstaZaPrikaz;
    }
    
    //public String getVrstaArtikalSlozenIliProd() {
    //    return tip;
    //}

    public VrsteGrupaIliArtikal getVrstaGrupaIliArtikal() {
        return vrstaZaPrikaz;
    }
    
    public boolean getDaLiJeArtikalSlozen() {
        return this.tip.equals("SLOZ");
    } 
}