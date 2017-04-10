package rmaster.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.stage.StageStyle;
import rmaster.models.Porudzbina;




public class MeniPopUpController extends Dialog {
    
    public MeniPopUpController(Porudzbina porudzbina) {
        
        Button potvrdi = new Button("Potvrdi");
        
        potvrdi.setPrefSize(140, 50);
        
        Button otkazi = new Button("Otkaži");
        
        otkazi.setPrefSize(140, 50);
        
        otkazi.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {
                                    zatvoriDialog();
                            }
                        });
        
        this.initStyle(StageStyle.UNDECORATED);
                
        this.getDialogPane().getStylesheets().
                addAll(this.getClass().getResource("style/style.css").toExternalForm());
        
        this.getDialogPane().getStyleClass().add("myDialog");
        
        MeniContent content = new MeniContent(porudzbina);
        
        content.fxID_footer.getChildren().addAll(otkazi, potvrdi);
        
        this.getDialogPane().setContent(content);
        
        potvrdi.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {
                               
                            if (content.snimiMenipromet()) {
                                   vratiRezultat();
                               }
                            }
                        });
        
        otkazi.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {
                               
                            zatvoriDialog();
                            
                        }
        });
    }
    
    private void zatvoriDialog() {
        this.close();
    }
    
    private void vratiRezultat() {
        
        this.setResultConverter(button -> {
            //sta ovde treba da vrati??
            return null;
        });
        
    }
}
