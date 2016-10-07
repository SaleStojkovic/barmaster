/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets.items;
import javafx.scene.image.ImageView;

/**
 *
 * @author Bosko
 */
public class ItemGroup {
    private long ID;
    private String Name;
    private String ImagePutanja;
    private ImageView Image;
    private int prioritet;
    private String skrNaziv;
    private long NadredjenaGrupaID;
    private boolean GlavnaGrupa;
    private boolean Podgrupa;
    private boolean Artikal;
    
    public ItemGroup(String name, 
            String imagePutanja, 
            long id, 
            int prioritet, 
            String skrNaziv, 
            long NadredjenaGrupaID, 
            boolean GlavnaGrupa, 
            boolean Podgrupa, 
            boolean Artikal) {
        this.ID = id;
        this.Name = name;
        this.ImagePutanja = imagePutanja;
        this.prioritet = prioritet;
        this.skrNaziv = skrNaziv;
        this.NadredjenaGrupaID = NadredjenaGrupaID;
        this.GlavnaGrupa = GlavnaGrupa;
        this.Podgrupa = Podgrupa;
        this.Artikal = Artikal;
        // Nafilovati sliku iz putanje
    }
    
    public String getName() {
        return Name;
    }
    public ImageView getImage() {
        return Image;
    }
    
    public long getID() {
        return ID;
    }

    public boolean getDaLiJeGlavnaGrupa() {
        return GlavnaGrupa;
    }

    public boolean getDaLiJePodgrupa() {
        return Podgrupa;
    }

    public boolean getDaLiJeArtikal() {
        return Artikal;
    }
}
