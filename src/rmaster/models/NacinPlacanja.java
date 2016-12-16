/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import rmaster.assets.Utils;

/**
 *
 * @author Bosko
 */
public class NacinPlacanja {
    public enum VrstePlacanja {FAKTURA, CEK, KARTICA, GOTOVINA};
    private String vrstaPlacanjaString = "";
    private VrstePlacanja nacinPlacanja;
    private String text = "";
    private double vrednost = 0.;
    private String vrednostString = "";
    
    public NacinPlacanja() {
    }
    
    public String getNacinPlacanjaString() {
        return this.vrstaPlacanjaString;
    }

    public NacinPlacanja(VrstePlacanja np) {
        this.nacinPlacanja = np;
        switch (np) {
            case FAKTURA:
                this.text = "Faktura";
                this.vrstaPlacanjaString = "FAKTURA";
                break;
            case CEK:
                this.text = "Ček";
                this.vrstaPlacanjaString = "CEK";
                break;
            case KARTICA:
                this.text = "Kartica";
                this.vrstaPlacanjaString = "KARTICA";
                break;
            case GOTOVINA:
                this.text = "Gotovina";
                this.vrstaPlacanjaString = "GOTOVINA";
                break;
        }
    }
    
    public VrstePlacanja getNacinPlacanja() {
        return this.nacinPlacanja;
    }
    
    public double getVrednost() {
        return this.vrednost;
    }
    public String getVrednostString() {
        return this.vrednostString;
    }
    public String getTextZaButton() {
        String rez = this.text;
        if (this.vrednost!=0) 
            rez = rez + "\n" + Utils.getStringFromDouble(this.vrednost);
        return rez;
    }
    
    public void setVrednost (double vred) {
        this.vrednost = vred;
        this.vrednostString = Utils.getStringFromDouble(this.vrednost);
    }
    public void setVrednostString(String vred) {
        this.vrednostString = vred;
        this.vrednost = Utils.getDoubleFromString(this.vrednostString);
    }

}
