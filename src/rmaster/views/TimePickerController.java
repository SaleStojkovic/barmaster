/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;


public class TimePickerController extends Dialog  {
    
    @FXML
    private Label response2;
    
    @FXML
    private TextField sati;
    
    @FXML
    private TextField minuti;
   
    @FXML
    private Button cancelButton;
    
    
    public TimePickerController(
            String prethodnaVrednost
            ) {
        createTimePickerDialog(prethodnaVrednost);
    }
    
    public void createTimePickerDialog(String prethodniTekst) {
        
        String prethodniSati = prethodniTekst.substring(0, Math.min(prethodniTekst.length(), 2));
        
        String prethodniMinuti = prethodniTekst.substring(Math.max(prethodniTekst.length() - 2, 0));

       
        this.initStyle(StageStyle.UNDECORATED);
        
        // Set the icon (must be included in the project).
        //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

        // Set the button types.
        ButtonType potvrdiButtonType = new ButtonType("✓", ButtonBar.ButtonData.OK_DONE);
        ButtonType odustaniButtonType = new ButtonType("X", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(potvrdiButtonType, odustaniButtonType);
        
        this.getDialogPane().getStylesheets().
                addAll(this.getClass().getResource("style/style.css").toExternalForm());
        
        this.getDialogPane().getStyleClass().add("myDialog");
        
            
        // Create the unetiTekst and password labels and fields.
        VBox vBoxTastatura = new VBox();
        
        response2 = new Label();
        vBoxTastatura.getChildren().add(response2);
        this.setHeaderText("Unesite Vreme");

            
        HBox redSaPovecajDugmicima = new HBox();
        
        Button povecajSate = new Button("▲");
        Button povecajMinute = new Button("▲");
        
        povecajSate.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override public void handle(ActionEvent e) {
                                            try {
                                                incrementOrDecrement("I","S");
                                            } catch (Exception ex) {
                                            }
                                        }
                                    });
        
        povecajMinute.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override public void handle(ActionEvent e) {
                                            try {
                                                incrementOrDecrement("I", "M");
                                            } catch (Exception ex) {
                                            }
                                        }
                                    });
        
        povecajSate.setPrefSize(90, 60);
        povecajMinute.setPrefSize(90, 60);

        redSaPovecajDugmicima.getChildren().add(povecajSate);
        redSaPovecajDugmicima.getChildren().add(povecajMinute);

        vBoxTastatura.getChildren().add(redSaPovecajDugmicima);
        
        
        
        HBox redSaTekstom = new HBox();

        sati = new TextField();
        minuti = new TextField();
        
        TextField prazanProstor1 = new TextField();
        TextField prazanProstor2 = new TextField();
        TextField prazanProstor3 = new TextField();

        sati.setPrefSize(50, 40);
        minuti.setPrefSize(50, 40);
            
        prazanProstor1.setPrefSize(25, 40);
        prazanProstor2.setPrefSize(25, 40);
        prazanProstor3.setPrefSize(30, 40);

        sati.setText(prethodniSati);
        minuti.setText(prethodniMinuti);
        prazanProstor3.setText(":");
        
        sati.setFocusTraversable(false);
        minuti.setFocusTraversable(false);

        redSaTekstom.getChildren().add(prazanProstor1);
        redSaTekstom.getChildren().add(sati);
        redSaTekstom.getChildren().add(prazanProstor3);
        redSaTekstom.getChildren().add(minuti);
        redSaTekstom.getChildren().add(prazanProstor2);


        vBoxTastatura.getChildren().add(redSaTekstom);

        // Enable/Disable login button depending on whether a unetiTekst was entered.
        Node loginButton = this.getDialogPane().lookupButton(potvrdiButtonType);
        
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
        sati.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        
        HBox redSaSmanjiDugmicima = new HBox();
        
        Button smanjiSate = new Button("▼");
        Button smanjiMinute = new Button("▼");
        
        smanjiSate.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override public void handle(ActionEvent e) {
                                            try {
                                                incrementOrDecrement("D","S");
                                            } catch (Exception ex) {
                                            }
                                        }
                                    });
        
        smanjiMinute.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override public void handle(ActionEvent e) {
                                            try {
                                                incrementOrDecrement("D","M");
                                            } catch (Exception ex) {
                                            }
                                        }
                                    });
        
        smanjiSate.setPrefSize(90, 60);
        smanjiMinute.setPrefSize(90, 60);

        redSaSmanjiDugmicima.getChildren().add(smanjiSate);
        redSaSmanjiDugmicima.getChildren().add(smanjiMinute);

        vBoxTastatura.getChildren().add(redSaSmanjiDugmicima);
        
        
        
        this.getDialogPane().setContent(vBoxTastatura);

        // Convert the result to a unetiTekst-password-pair when the login button is clicked.
        this.setResultConverter(dialogButton -> {
            if (dialogButton == potvrdiButtonType) {
                return new String(sati.getText() + ":" + minuti.getText());
            }
            return null;
        });
    }
    
    public void cancelAction(ActionEvent event) {
        zatvoriOvuFormu();
    }
    

    public void numberKeyPressed(ActionEvent event) throws Exception {
        Button pritisnutTaster = (Button)event.getSource();
        String lozinkaText = this.sati.getText();
        response2.setText(""); 
        lozinkaText += pritisnutTaster.getText();
        this.sati.setText(lozinkaText);
    }
    
    public void zatvoriOvuFormu(){
        try {
        cancelButton.getScene().getWindow().hide();
        } catch (Exception e){
            System.out.println("Neuspelo zatvaranje forme - TimePickerController");
        }
    }
    
    
    public void incrementOrDecrement(
            String incrementOrDecrement,
            String minutiIliSati
    ) {
        
         //povecaj sate
        if (minutiIliSati.equals("S")) {
            String trenutniSatiString = sati.getText();
            
            if (trenutniSatiString.charAt(0) == '0') {
                trenutniSatiString = trenutniSatiString.substring(0);
            }
            
            int trenutnoVreme = Integer.parseInt(trenutniSatiString);
            
            if (incrementOrDecrement.equals("I")) {
                trenutnoVreme++;
            } else {
                trenutnoVreme--;
            }
            
            if (trenutnoVreme == 24) {
                trenutnoVreme = 0;
            }
            
            if (trenutnoVreme < 0) {
                trenutnoVreme = 23;
            }
            
            String output = trenutnoVreme + "";
            
            if (output.length() == 1) {
                output = "0" + output;
            }
            
            sati.setText(output);
        }
        
        //povecaj minute
        if (minutiIliSati.equals("M")) {
            String trenutniMinutiString = minuti.getText();
            
            if (trenutniMinutiString.charAt(0) == '0') {
                trenutniMinutiString = trenutniMinutiString.substring(0);
            }
            
            int trenutnoVreme = Integer.parseInt(trenutniMinutiString);
            
            if (incrementOrDecrement.equals("I")) {
                trenutnoVreme += 5;
            } else {
                trenutnoVreme -= 5;
            }
            
            if (trenutnoVreme == 60) {
                trenutnoVreme = 0;
            }
            
            if (trenutnoVreme < 0) {
                trenutnoVreme = 55;
            }
            
            String output = trenutnoVreme + "";
            
            if (output.length() == 1) {
                output = "0" + output;
            }
            
            minuti.setText(output);
        }
    }
}
