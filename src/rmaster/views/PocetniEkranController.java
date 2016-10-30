/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
/**
 *
 * @author Arbor
 */
public class PocetniEkranController extends NumberKeypadController {
    
    @FXML 
    private Label response;
    
    @FXML
    private PasswordField lozinka;
    
    @FXML
    private Label clock;
    
    @FXML
    private Button test;
    
    private final NumberKeypadController keypad = new NumberKeypadController();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        clock.setFont(klavikaBold);
        keypad.sledecaForma = "prikazSala";
        Timeline timeline = this.prikaziCasovnik(clock);
        timeline.play();
        
//        Timeline timeline1 = new Timeline(
//            new KeyFrame(Duration.seconds(0.5), new EventHandler<ActionEvent>() {
//                @Override public void handle(ActionEvent actionEvent) {
//                    test.setStyle("-fx-background-color: black;");
//                }
//            }
//            ),
//            new KeyFrame(Duration.seconds(1),new EventHandler<ActionEvent>() {
//                @Override public void handle(ActionEvent actionEvent) {
//                    test.setStyle("-fx-background-color: grey;");
//                }
//            }
//            )
//          );
//          timeline1.setCycleCount(Animation.INDEFINITE);
//        
//          timeline1.play();
    }
    
   
}
