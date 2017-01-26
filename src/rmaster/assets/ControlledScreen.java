/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets;

import javafx.event.ActionEvent;
import rmaster.ScreenController;

/**
 *
 * @author Arbor
 */
public interface ControlledScreen {
    
     //This method will allow the injection of the Parent ScreenPane 
     public void setScreenParent(ScreenController screenPage); 
     
     public void initData(Object data);
  
     public void odjava(ActionEvent e);
}
