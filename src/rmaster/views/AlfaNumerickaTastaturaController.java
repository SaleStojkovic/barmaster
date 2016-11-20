/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

/**
 * Controller class
 *
 * @author Bosko
 */
public class AlfaNumerickaTastaturaController extends Dialog {

    
    public char[] specijalniKarakteri = {'š','ć','č','đ','ž'};
    
    @FXML
    private Label response2;
    
    @FXML
    private TextField unetiTekst;
   
    @FXML
    private Button cancelButton;
    
    
    /**
     * Initializes the controller class.
     */
    public AlfaNumerickaTastaturaController(String prethodniTekst) {
        createDialogTastatura(prethodniTekst);
    }
    
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    
    
    public void createDialogTastatura(String prethodniTekst) {
        this.initStyle(StageStyle.UNDECORATED);

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

        this.setHeaderText("Unesite tekst");
        unetiTekst = new TextField();
        unetiTekst.setPromptText("Unesite tekst");
        redSaTekstom.getChildren().add(unetiTekst);
            
        vBoxTastatura = this.vratiAlfanumerickuTastaturu(
                    vBoxTastatura, 
                    prethodniTekst, 
                    false
            );
        
        cancelButton = (Button)this.getDialogPane().lookupButton(odustaniButtonType);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {
                                try {
                                    cancelAction(e);
                                } catch (Exception ex) {
                                }
                            }
                        });


        this.getDialogPane().setContent(vBoxTastatura);


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
    
    
    public void spaceButtonPressed()
    {
        String lozinkaText = this.unetiTekst.getText();
        response2.setText(""); 
        lozinkaText += " ";
        this.unetiTekst.setText(lozinkaText);
    }
    
    public void capsLockPressed(List<Button> listaSlova, ActionEvent event) 
    {
        Button capsLock = (Button)event.getSource();
        String textCapsLock = "";
        
        for(Button dugme : listaSlova) {
            char karakter =  dugme.getText().charAt(0);
           
            if (capsLock.getText().equals("▲")) {
                dugme.setText(Character.toUpperCase(karakter) + "");
                textCapsLock = "▼";
            } else {
                dugme.setText(Character.toLowerCase(karakter) + "");
                textCapsLock = "▲";
            }      
        } 
        
        capsLock.setText(textCapsLock);
    }
    
    public VBox vratiAlfanumerickuTastaturu(
            VBox vBoxTastatura,
            String prethodniTekst,
            boolean capsLockValue
    ){ 
        
            HBox redSaTekstom = new HBox();

            this.setHeaderText("Unesite tekst");
            unetiTekst = new TextField();
            unetiTekst.setPromptText("Unesite tekst");
            redSaTekstom.getChildren().add(unetiTekst);
            
            unetiTekst.setPrefSize(910, 60);
            
            unetiTekst.setText(prethodniTekst);
            
            unetiTekst.setFocusTraversable(false);
            
            vBoxTastatura.getChildren().add(redSaTekstom);
            
            char ch = 'a';
            
            List<Button> svaSlova = new ArrayList<>();
            
            while (ch <= 'z') {
                               
                HBox redSaSlovima = new HBox();

                for(int i=1; i<14; i++) {
                
                    Button bSlovo = new Button(ch + "");
                    bSlovo.setPrefSize(70,70);
                    bSlovo.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override public void handle(ActionEvent e) {
                                            try {
                                                numberKeyPressed(e);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    });

                    redSaSlovima.getChildren().add(bSlovo);
                    ch++;
                    
                    svaSlova.add(bSlovo);
                }
                
                vBoxTastatura.getChildren().add(redSaSlovima);

            }

            HBox redSaSpecijalnimKarakterima = new HBox();

            for (char karakter : specijalniKarakteri) {
                                
                Button bSlovo = new Button(karakter + "");
                    bSlovo.setPrefSize(70,70);
                    bSlovo.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override public void handle(ActionEvent e) {
                                            try {
                                                numberKeyPressed(e);
                                            } catch (Exception ex) {
                                            }
                                        }
                                    });

                    redSaSpecijalnimKarakterima.getChildren().add(bSlovo);
                    
                    svaSlova.add(bSlovo);
            }
            
            Button bSpace = new Button("space");
            bSpace.setPrefSize(350,70);
            bSpace.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    try {
                                        spaceButtonPressed();
                                    } catch (Exception ex) {
                                    }
                                }
                            });
            
            redSaSpecijalnimKarakterima.getChildren().add(bSpace);

            Button bBack = new Button("«");

            bBack.setPrefSize(210,70);

            bBack.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    try {
                                        backButton(e);
                                    } catch (Exception ex) {
                                    }
                                }
                            });
            
            redSaSpecijalnimKarakterima.getChildren().add(bBack);
            

            vBoxTastatura.getChildren().add(redSaSpecijalnimKarakterima);
            
            
            HBox redSaBrojevima = new HBox();

                for (int j=0; j<10; j++) {
                
                        
                    Button bBroj = new Button("" + j);
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
              
            Button capsLock = new Button("▲");
            capsLock.setPrefSize(210,70);
            capsLock.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    try {
                                        capsLockPressed(svaSlova, e);
                                    } catch (Exception ex) {
                                    }
                                }
                            });  
            
            redSaBrojevima.getChildren().add(capsLock);
   
            vBoxTastatura.getChildren().add(redSaBrojevima);

            return vBoxTastatura;
    }
}
