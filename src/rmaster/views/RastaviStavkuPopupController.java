/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.StageStyle;
import rmaster.models.StavkaTure;

/**
 *
 * @author Arbor
 */
public class RastaviStavkuPopupController extends Dialog {
    
    double kolicinaGlavni;    
    
    String kolicinaOpisni;
    
    String kolicinaDodatni;
    
    TextField kolicinaGlavniText = new TextField();
    
    HashMap<String, TextField> dodatniArtikliTextField = new HashMap<>();

    HashMap<String, String> izabranaStavkaMap = new HashMap<>();
    
    HashMap<String, HashMap<String, String>> dodatniArtikli = new HashMap<>();
    
    
    
    StavkaTure izabranaStavka;
   
    public RastaviStavkuPopupController(StavkaTure izabranaStavka) {
        
        this.izabranaStavka = izabranaStavka;
        
        this.setHeaderText("Napravite novu stavku");
                
        kolicinaGlavni =  (double)izabranaStavka.kolicina - 1;
        
        izabranaStavkaMap = (HashMap)izabranaStavka.dajStavkuTure();
        
        this.initStyle(StageStyle.UNDECORATED);

        // Set the button types.
        ButtonType odustaniButtonType = new ButtonType("X", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        ButtonType promeniButton = new ButtonType("✓", ButtonBar.ButtonData.OK_DONE);
        
        this.getDialogPane().getButtonTypes().addAll(odustaniButtonType, promeniButton);
        
        this.getDialogPane().getStylesheets().
                addAll(this.getClass().getResource("style/style.min.css").toExternalForm());
        
        this.getDialogPane().getStyleClass().add("myDialog");
        
        
        VBox content = new VBox(5);

        
        content.getChildren().addAll(
                this.glavnaStavkaHbox(izabranaStavka)//,
               // this.dodatniArtikliHBox(izabranaStavka)
        );

        this.getDialogPane().setContent(content);
        
        this.setResultConverter(dialogButton -> {
            if (dialogButton == promeniButton) {
                
                HashMap<String, String> rezultati = new HashMap<>();
                
                String kolicina = this.kolicinaGlavniText.getText().replaceAll("x", "");
                rezultati.put("kolicina", kolicina);
                //rezultati.put("glavniArtikal", izabranaStavkaMap);
                
                //rezultati.put("listaDodatnih", dodatniArtikli);
                
                return rezultati;
            }
            return null;
        });
    }
    
    public void initialize(URL url, ResourceBundle rb) {

    } 
    
    private HBox glavnaStavkaHbox(StavkaTure izabranaStavka) 
    {
        HBox glavnaStavka = new HBox(10);
        
        Label glavniArtikal = new Label();
        
        glavniArtikal.setStyle(
                "-fx-border-color: white;"
                + "-fx-padding: 5 5 5 5;"
        );
        
        glavniArtikal.setPrefSize(150, 115);
        
        glavniArtikal.wrapTextProperty().setValue(Boolean.TRUE);
        
        glavniArtikal.textAlignmentProperty().set(TextAlignment.LEFT);
         
        glavniArtikal.setText(izabranaStavka.naziv);
        
        VBox kolicinaGlavniContent = new VBox();
        
        Button povecajKolicinuGlavni = new Button("▲");
        
        povecajKolicinuGlavni.setPrefSize(90, 40);
        
        
        kolicinaGlavniText.setPrefSize(90, 30);
        
        kolicinaGlavniText.setText("x" + kolicinaGlavni);
        
        kolicinaGlavniText.alignmentProperty().setValue(Pos.CENTER);

        Button smanjiKolicinuGlavni = new Button("▼");
         
        smanjiKolicinuGlavni.setPrefSize(90, 40);
        
        kolicinaGlavniContent.getChildren().addAll(
                povecajKolicinuGlavni,
                kolicinaGlavniText,
                smanjiKolicinuGlavni
        );
        
        glavnaStavka.getChildren().addAll(
            glavniArtikal,
            kolicinaGlavniContent    
        );
        
        smanjiKolicinuGlavni.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override public void handle(ActionEvent e) {
                                            incrementOrDecrementKolicinaGlavni("D");
                                        }
                                    });
        
        
        povecajKolicinuGlavni.setOnAction(new EventHandler<ActionEvent>() {
                                @Override public void handle(ActionEvent e) {
                                    incrementOrDecrementKolicinaGlavni("I");
                                    
                                }
                            });
        
        return glavnaStavka;
    }
    
    private VBox dodatniArtikliHBox(StavkaTure izabranaStavka) 
    {
         VBox dodatniArtikalBox = new VBox(5);
    
        for(StavkaTure dodatniArtikal : izabranaStavka.dodatniArtikli) {
        
            HBox noviSadrzaj = new HBox(10);
            
            Label dodatniArtikalLabel = new Label();

            dodatniArtikalLabel.setStyle(
                    "-fx-border-color: white;"
                    + "-fx-padding: 5 5 5 5;"
            );

            dodatniArtikalLabel.setPrefSize(150, 115);

            dodatniArtikalLabel.wrapTextProperty().setValue(Boolean.TRUE);

            dodatniArtikalLabel.textAlignmentProperty().set(TextAlignment.LEFT);

            dodatniArtikalLabel.setText(dodatniArtikal.naziv);

            
            VBox kolicinaDodatniContent = new VBox();

            Button povecajKolicinuDodatni = new Button("▲");

            povecajKolicinuDodatni.setPrefSize(90, 40);

            povecajKolicinuDodatni.setId(dodatniArtikal.id + "");
            
            TextField dodatniArtikalField = new TextField();
            
            dodatniArtikalField.setPrefSize(90, 30);

            dodatniArtikalField.setText("x" + (int)dodatniArtikal.kolicina);
            
            dodatniArtikalField.alignmentProperty().setValue(Pos.CENTER);
            
            
            dodatniArtikliTextField.put(dodatniArtikal.id + "", dodatniArtikalField);

            
            Button smanjiKolicinuDodatni = new Button("▼");
            
            smanjiKolicinuDodatni.setId(dodatniArtikal.id + "");

            smanjiKolicinuDodatni.setPrefSize(90, 40);

            kolicinaDodatniContent.getChildren().addAll(
                    povecajKolicinuDodatni,
                    dodatniArtikalField,
                    smanjiKolicinuDodatni
            );

            noviSadrzaj.getChildren().addAll(
                dodatniArtikalLabel,
                kolicinaDodatniContent    
            );

            smanjiKolicinuDodatni.setOnAction(new EventHandler<ActionEvent>() {
                                            @Override public void handle(ActionEvent event) {
                                                
                                                Button dugme = (Button)event.getSource();
                                                
                                                incrementOrDecrementKolicinaDodatni("D", dugme.getId());
                                               
                                            }
                                        });


            povecajKolicinuDodatni.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent event) {
                                        
                                        Button dugme = (Button)event.getSource();

                                        incrementOrDecrementKolicinaDodatni("I", dugme.getId());
                                    }
                                });
        
            
            dodatniArtikalBox.getChildren().add(noviSadrzaj);
        }
        
        return dodatniArtikalBox;

    }
    
    private void incrementOrDecrementKolicinaDodatni(String akcija, String dodatniId)
    {
        StavkaTure stavkaKojaSeMenja = this.getDodatnaStavkaKojaSeMenja(dodatniId);
        
        TextField izabranoPolje = dodatniArtikliTextField.get(dodatniId);
        
        String text = izabranoPolje.getText();
        
        int kolicina = Integer.parseInt(text.replaceAll("x", ""));
        
        if (akcija.equals("I") && kolicina < (int)stavkaKojaSeMenja.kolicina - 1) {
            
            kolicina++;
            
            izabranoPolje.setText("x" + kolicina);
            
            HashMap<String, String> stavkaKojaSeMenjaMap = (HashMap)stavkaKojaSeMenja.dajStavkuTure();
        
            stavkaKojaSeMenjaMap.put("kolicina", "x" + kolicina);

            dodatniArtikli.put(dodatniId, stavkaKojaSeMenjaMap);
        }
        
        if (akcija.equals("D") && kolicina > 1) {
            
            kolicina--;
            
            izabranoPolje.setText("x" + kolicina); 
            
            HashMap<String, String> stavkaKojaSeMenjaMap = (HashMap)stavkaKojaSeMenja.dajStavkuTure();
        
            stavkaKojaSeMenjaMap.put("kolicina", "x" + kolicina);

            dodatniArtikli.put(dodatniId, stavkaKojaSeMenjaMap);
        }
        
       
        
    }
    
    private StavkaTure getDodatnaStavkaKojaSeMenja(String dodatniId)
    {
        StavkaTure stavkaKojaSeMenja = null;
        
        for(StavkaTure dodatnaStavka : this.izabranaStavka.dodatniArtikli) {
            
            if ((dodatnaStavka.id + "").equals(dodatniId)) {
                
                stavkaKojaSeMenja = dodatnaStavka;
                break;
            }
        }
        
        return stavkaKojaSeMenja;
    }
    
    
    private void incrementOrDecrementKolicinaGlavni(String akcija) {
        
        String text = kolicinaGlavniText.getText();
        
        double kolicina = Double.parseDouble(text.replaceAll("x", ""));
        
        if (akcija.equals("I") && kolicina < kolicinaGlavni) {
            kolicina++;
            
            kolicinaGlavniText.setText("x" + kolicina);
            
            return;
        }
        
        if (akcija.equals("D") && kolicina > 1) {
            kolicina--;
            kolicinaGlavniText.setText("x" + kolicina); 
        }

        //izabranaStavkaMap.put("kolicina", "x" + kolicina);
    }
    
    
}
