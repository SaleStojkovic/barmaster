/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster;

import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import rmaster.assets.ControlledScreen;
import rmaster.assets.FXMLDocumentController;

/**
 *
 * @author Arbor
 */
public class ScreenController extends StackPane {
    
    private HashMap<String, Node> screens = new HashMap<>(); 
    
    private HashMap<String, FXMLLoader> controllers = new HashMap<>();

    public void addScreen(String name, Node screen) { 
       screens.put(name, screen); 
    } 
    
    private void addController(String name, FXMLLoader loader) {
        controllers.put(name, loader);
    }
     
    public boolean loadScreen(String name, String resource) { 
    try { 
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
       
       Pane loadScreen = (Pane) loader.load(); 
       
       ControlledScreen myScreenControler = 
              ((ControlledScreen) loader.getController()); 
       
       addController(name, loader);
               
       myScreenControler.setScreenParent(this); 
       
       addScreen(name, loadScreen); 
       
       return true; 
     } catch(Exception e) { 
       e.printStackTrace(); 
       return false; 
        } 
   } 
    
     
   public boolean setScreen(final String name, Object data) { 

     if(screens.get(name) != null) { //screen loaded 

       //Is there is more than one screen 
       if(!getChildren().isEmpty()){ 

            //remove displayed screen 
            getChildren().remove(0); 
            //add new screen 
            getChildren().add(0, screens.get(name)); 
                    
       } else { 
         //no one else been displayed, then just show 
         getChildren().add(screens.get(name)); 
         
       }     
       
       FXMLDocumentController controller = controllers.get(name).getController();
       
       controller.initData(data);
       
       return true; 
        } else { 
         System.out.println("screen hasn't been loaded!\n"); 
         return false; 
        }
   }

   

   public boolean unloadScreen(String name) { 
     if(screens.remove(name) == null) { 
       System.out.println("Screen didn't exist"); 
       return false; 
     } else { 
       return true; 
     } 
   } 
   
   
}
