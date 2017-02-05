/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets.RM_Button;

import javafx.scene.control.Button;

/**
 *
 * @author Bosko
 */
public class RM_Button extends Button{
    private Object podatak = "";
    
    private Object vrsta = "";
    
    public void setPodatak(Object podatak) {
        this.podatak = podatak;
    }
    
    public Object getPodatak() {
        return this.podatak;
    }
    
    public void setVrsta(Object vrsta) {
        this.vrsta = vrsta;
    }
    
    public Object getVrsta() {
        return this.vrsta;
    }
}
