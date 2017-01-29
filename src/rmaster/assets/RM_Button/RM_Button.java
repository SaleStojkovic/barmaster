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
    private String podatak = "";
    
    public void setPodatak(String brojStola) {
        this.podatak = brojStola;
    }
    
    public String getPodatak() {
        return this.podatak;
    }
    
}
