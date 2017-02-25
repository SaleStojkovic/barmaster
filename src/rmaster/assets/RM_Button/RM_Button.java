/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets.RM_Button;

import javafx.scene.control.Button;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Bosko
 */
public class RM_Button extends Button{
    
    public RM_Button(){
        this.wrapTextProperty().setValue(true);
        this.textAlignmentProperty().set(TextAlignment.CENTER);
        this.setCenterShape(true);
    }
    
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
