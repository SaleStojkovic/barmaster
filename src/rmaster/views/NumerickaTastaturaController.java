/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import rmaster.assets.TastaturaVrsta;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

/**
 * Controller class
 *
 * @author Bosko
 */
public class NumerickaTastaturaController extends Dialog {

       
    @FXML
    private Label response2;
    
    @FXML
    private TextField unetiTekst;
   
    @FXML
    private Button cancelButton;
    
    
    /**
     * Initializes the controller class.
     */
    public NumerickaTastaturaController(
            TastaturaVrsta vrstaTastature,
            String prethodniTekst
            ) {
        createDialogTastatura(vrstaTastature, prethodniTekst);
    }
    
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    
    
    public void createDialogTastatura(
            TastaturaVrsta vrstaTastature,
            String prethodniTekst
    ) {
        this.initStyle(StageStyle.UNDECORATED);
        
        // Set the icon (must be included in the project).
        //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

        // Set the button types.
        ButtonType potvrdiButtonType = new ButtonType("✓", ButtonData.OK_DONE);
        ButtonType odustaniButtonType = new ButtonType("X", ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(potvrdiButtonType, odustaniButtonType);
        
        this.getDialogPane().getStylesheets().
                addAll(this.getClass().getResource("style/style.css").toExternalForm());
        
        this.getDialogPane().getStyleClass().add("myDialog");
        
            
        // Create the unetiTekst and password labels and fields.
        VBox vBoxTastatura = new VBox();
        
        response2 = new Label();
        vBoxTastatura.getChildren().add(response2);
        
        HBox redSaTekstom = new HBox();
        
        switch (vrstaTastature) {
            case LOGOVANJE:
            this.setHeaderText("Provera identiteta");
            unetiTekst = new PasswordField();
            unetiTekst.setPromptText("Unesite lozinku");
            redSaTekstom.getChildren().add(unetiTekst);
                break;
            case UNOS_IZNOSA:
            this.setHeaderText("Unesite iznos");
            unetiTekst = new TextField();
            unetiTekst.setPromptText("Unesite broj");
            redSaTekstom.getChildren().add(unetiTekst);
                break;
            case BROJ_FISKALNOG_ISECKA:
                this.setHeaderText("Unesite broj fiskalnog isečka");
                unetiTekst = new TextField();
                unetiTekst.setPromptText("Broj fiskalnog isečka");
                redSaTekstom.getChildren().add(unetiTekst);
                break;
            default:
        }

            unetiTekst.setPrefSize(140, 60);
            
            unetiTekst.setText(prethodniTekst);

            Button bBack = new Button("«");

            bBack.setPrefSize(70,60);

            bBack.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    try {
                                        backButton(e);
                                    } catch (Exception ex) {
                                    }
                                }
                            });
            redSaTekstom.getChildren().add(bBack);     
            
            vBoxTastatura.getChildren().add(redSaTekstom);

            for(int i=1; i<4; i++) {
                HBox redSaBrojevima = new HBox();

                for (int j=1; j<4; j++) {
                    Button bBroj = new Button("" + ((i-1)*3 + j));
                    bBroj.setPrefSize(70,70);
                    bBroj.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override public void handle(ActionEvent e) {
                                            try {
                                                numberKeyPressed(e);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    });

                    redSaBrojevima.getChildren().add(bBroj);
                }
                vBoxTastatura.getChildren().add(redSaBrojevima);
            
        }
        
        Button nula = new Button("" + 0);
        nula.setPrefSize(70,70);
        nula.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        try {
                                            numberKeyPressed(e);
                                        } catch (Exception ex) {
                                        }
                                    }
                                });
        vBoxTastatura.getChildren().add(nula);

        
        // Enable/Disable login button depending on whether a unetiTekst was entered.
        Node loginButton = this.getDialogPane().lookupButton(potvrdiButtonType);
        loginButton.setDisable(true);
        
        cancelButton = (Button)this.getDialogPane().lookupButton(odustaniButtonType);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {
                                try {
                                    cancelAction(e);
                                } catch (Exception ex) {
                                }
                            }
                        });

        // Do some validation (using the Java 8 lambda syntax).
        unetiTekst.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        this.getDialogPane().setContent(vBoxTastatura);

        // Request focus on the unetiTekst field by default.
        Platform.runLater(() -> unetiTekst.requestFocus());

        // Convert the result to a unetiTekst-password-pair when the login button is clicked.
        this.setResultConverter(dialogButton -> {
            if (dialogButton == potvrdiButtonType) {
                return new String(unetiTekst.getText());
            }
            return null;
        });
    }
    
    public void cancelAction(ActionEvent event) {
        zatvoriOvuFormu();
    }
    
    
    public void backButton(ActionEvent event) {
        response2.setText("");
        String lozinkaText = this.unetiTekst.getText();
        
        if (lozinkaText.length() != 0) {
            lozinkaText = lozinkaText.substring(0, lozinkaText.length()-1);
            this.unetiTekst.setText(lozinkaText);
        }
    }

    public void numberKeyPressed(ActionEvent event) throws Exception {
        Button pritisnutTaster = (Button)event.getSource();
        String lozinkaText = this.unetiTekst.getText();
        response2.setText(""); 
        lozinkaText += pritisnutTaster.getText();
        this.unetiTekst.setText(lozinkaText);
    }
    
    public void zatvoriOvuFormu(){
        try {
        cancelButton.getScene().getWindow().hide();
        } catch (Exception e){
            System.out.println("Neuspelo zatvaranje forme - TastaturaController");
        }
    }
    
}
    
    
