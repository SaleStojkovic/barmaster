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
    private VrstePlacanja nacinPlacanja;
    private String text = "";
    private double vrednost = 0.;
    
    
    public NacinPlacanja() {
    }

    public NacinPlacanja(VrstePlacanja np) {
        this.nacinPlacanja = np;
        switch (np) {
            case FAKTURA:
                this.text = "Faktura";
                break;
            case CEK:
                this.text = "Ček";
                break;
            case KARTICA:
                this.text = "Kartica";
                break;
            case GOTOVINA:
                this.text = "Gotovina";
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
        return Utils.getStringFromDouble(this.vrednost);
    }
    public String getText() {
        String rez = this.text;
        if (this.vrednost!=0)
            rez = rez + "\n" + this.getVrednostString();
        return rez;
    }
    
    public void setVrednost (double vred) {
        this.vrednost = vred;
    }
}
