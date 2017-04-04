/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;


import java.io.IOException;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import rmaster.assets.Utils;

/**
 *
 * @author Arbor
 */
public class MeniContent extends Pane{
    
    @FXML private ListView fxID_MeniList;
    
    @FXML private TextField fxID_Kolicina;
    private String kolicina;
    
    @FXML private TextField fxID_Cena;
    private String cena;
    
    @FXML private TextField fxID_Ukupno;
    private String ukupno;
    
    
    private String textFieldFocus = "";
    

    public MeniContent() {
        super();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("meniContent.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            initialilize();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    private void initialilize()
    {
        fxID_Kolicina.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    textFieldFocus = fxID_Kolicina.getId();
                }
            }
        });
        
        fxID_Cena.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    textFieldFocus = fxID_Cena.getId();
                }
            }
        });
                
        fxID_Ukupno.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    textFieldFocus = fxID_Ukupno.getId();
                }
            }
        });
        
        kolicina = "";
        cena = "";
        ukupno = "";
        
        osveziPolja();
        
        Platform.runLater(() -> fxID_Kolicina.requestFocus());

    }
   
    
    
    public void numberKeyPressed(ActionEvent event) throws Exception {
        
        Button pritisnutTaster = (Button)event.getSource();
                
        if (textFieldFocus.isEmpty()) {
            return;
        }
        
        if (textFieldFocus.equals(fxID_Kolicina.getId())) {
            // Obrada ako se unosi u polje Kolicina
            if (pritisnutTaster.getId().equals("fxID_Back")) {
                if (kolicina.length()>0) {
                    int indeksTacke = kolicina.indexOf(".");
                    if (indeksTacke == kolicina.length()-1) {
                        kolicina = kolicina.substring(0, kolicina.length()-1);
                    }
                    if (kolicina.length()>0) {
                        kolicina = kolicina.substring(0, kolicina.length()-1);
                    }
                }
            } else {
                int indeksTacke = kolicina.indexOf(".");
                if (indeksTacke == -1) { 
                    kolicina += pritisnutTaster.getText();
                } else if (kolicina.length()-indeksTacke <= 2) {
                    kolicina += pritisnutTaster.getText();
                }
            }
        }
        
        if (textFieldFocus.equals(fxID_Cena.getId())) {
            // Obrada ako se unosi u polje Cena
            if (pritisnutTaster.getId().equals("fxID_Back")) {
                if (cena.length()>0) {
                    int indeksTacke = cena.indexOf(".");
                    if (indeksTacke == cena.length()-1) {
                        cena = cena.substring(0, cena.length()-1);
                    }
                    if (cena.length()>0) {
                        cena = cena.substring(0, cena.length()-1);
                    }
                }
            } else {
                int indeksTacke = cena.indexOf(".");
                if (indeksTacke == -1) { 
                    cena += pritisnutTaster.getText();
                } else if (cena.length()-indeksTacke <= 2) {
                    cena += pritisnutTaster.getText();
                }
            }
        }
        
        if (textFieldFocus.equals(fxID_Ukupno.getId())) {
            // Obrada ako se unosi u polje Ukupno
            if (pritisnutTaster.getId().equals("fxID_Back")) {
                if (ukupno.length()>0) {
                    int indeksTacke = ukupno.indexOf(".");
                    if (indeksTacke == ukupno.length()-1) {
                        ukupno = ukupno.substring(0, ukupno.length()-1);
                    }
                    if (ukupno.length()>0) {
                        ukupno = ukupno.substring(0, ukupno.length()-1);
                    }
                }
            } else {
                int indeksTacke = ukupno.indexOf(".");
                if (indeksTacke == -1) { 
                    ukupno += pritisnutTaster.getText();
                } else if (ukupno.length()-indeksTacke <= 2) {
                    ukupno += pritisnutTaster.getText();
                }
            }
        }

        proracunajPolja();
        osveziPolja();
    }
    
    private void proracunajPolja() {

        // Proracun ako se unosi u polje CENA
        if (textFieldFocus.equals(fxID_Cena.getId())) {
            if ((!cena.equals("")) && (!cena.equals(Utils.getStringFromDouble(0)))) {
                if ((!kolicina.equals("")) && (!kolicina.equals(Utils.getStringFromDouble(0)))) {
                    ukupno = "" + Utils.getDoubleFromString(cena) * Utils.getDoubleFromString(kolicina);
                } else if ((!ukupno.equals("")) && (!ukupno.equals(Utils.getStringFromDouble(0)))) {
                    kolicina = "" + Utils.getDoubleFromString(ukupno) / Utils.getDoubleFromString(cena);
                } 
            } else {
                ukupno = "";
            }
        }
        
        // Proracun ako se unosi u polje KOLICINA
        if (textFieldFocus.equals(fxID_Kolicina.getId())) {
            if ((!kolicina.equals("")) && (!kolicina.equals(Utils.getStringFromDouble(0)))) {
                if ((!cena.equals("")) && (!cena.equals(Utils.getStringFromDouble(0)))) {
                    ukupno = "" + Utils.getDoubleFromString(cena) * Utils.getDoubleFromString(kolicina);
                } else if ((!ukupno.equals("")) && (!ukupno.equals(Utils.getStringFromDouble(0)))) {
                    cena = "" + Utils.getDoubleFromString(ukupno) / Utils.getDoubleFromString(kolicina);
                } 
            } else {
                ukupno = "";
            }
        }
        
        // Proracun ako se unosi u polje UKUPNO
        if (textFieldFocus.equals(fxID_Ukupno.getId())) {
            if ((!ukupno.equals("")) && (!ukupno.equals(Utils.getStringFromDouble(0)))) {
                if ((!kolicina.equals("")) && (!kolicina.equals(Utils.getStringFromDouble(0)))) {
                    cena = "" + Utils.getDoubleFromString(ukupno) / Utils.getDoubleFromString(kolicina);
                } else if ((!cena.equals("")) && (!cena.equals(Utils.getStringFromDouble(0)))) {
                    kolicina = "" + Utils.getDoubleFromString(ukupno) / Utils.getDoubleFromString(cena);
                } 
            } else {
                ukupno = "";
                cena = "";
            }
        }
        
    }

    private void osveziPolja() {
        fxID_Cena.setText(Utils.getStringFromDouble(Utils.getDoubleFromString(cena.equals("")?"0":cena)));
        fxID_Kolicina.setText(Utils.getStringFromDouble(Utils.getDoubleFromString(kolicina.equals("")?"0":kolicina)));
        fxID_Ukupno.setText(Utils.getStringFromDouble(Utils.getDoubleFromString(ukupno.equals("")?"0":ukupno)));
    }
    
}
