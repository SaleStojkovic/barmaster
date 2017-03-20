/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;

/**
 *
 * @author Bosko
 */
public class ImageBroker {
    private static ImageBroker instance = null;
    
    public static Map<String,Image> images;
    
    protected ImageBroker(){
        images = new HashMap();
    }
    
    public static ImageBroker getInstance() {
        if (instance == null) {
            instance = new ImageBroker();
        }
        return instance;
    }
    
    public Image getImage(String imagePath) {
        Image image;
        
        image = images.get(imagePath);
        
        if (image == null) {
            image = new Image(
                            imagePath,
                            1024,
                            608,
                            false,
                            true
                    );
            images.put(imagePath, image);
        }
        
        return image;
    }
    
    
}
