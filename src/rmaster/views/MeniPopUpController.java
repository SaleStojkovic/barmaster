package rmaster.views;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.StageStyle;




public class MeniPopUpController extends Dialog {
    
    public MeniPopUpController() {
        
        this.initStyle(StageStyle.UNDECORATED);
        
        ButtonType potvrdiButtonType = new ButtonType("✓", ButtonBar.ButtonData.OK_DONE);
        ButtonType odustaniButtonType = new ButtonType("X", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(potvrdiButtonType, odustaniButtonType);
        
        this.getDialogPane().getStylesheets().
                addAll(this.getClass().getResource("style/style.css").toExternalForm());
        
        this.getDialogPane().getStyleClass().add("myDialog");
        
        MeniContent content = new MeniContent();
        
        this.getDialogPane().setContent(content);
    }
    
    
    
    
}
