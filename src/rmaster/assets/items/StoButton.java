/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets.items;

import javafx.scene.control.Button;

/**
 *
 * @author Bosko
 */
public class StoButton extends Button{
    private String brojStola = "";
    
    public void setBrojStola(String brojStola) {
        this.brojStola = brojStola;
    }
    
    public String getBrojStola() {
        return this.brojStola;
    }
    
}
