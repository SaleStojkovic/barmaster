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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 *
 * @author Arbor
 */
public class MeniContent extends Pane{
    
    @FXML
    private TextField kol;
    
    @FXML
    private TextField cena;
    
    @FXML
    private TextField ukupno;
    
    
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
        kol.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    textFieldFocus = kol.getId();
                }
            }
        });
        
        cena.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    textFieldFocus = cena.getId();
                }
            }
        });
                
        ukupno.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    textFieldFocus = ukupno.getId();
                }
            }
        });
        
        Platform.runLater(() -> kol.requestFocus());

    }
   
    
    
    public void numberKeyPressed(ActionEvent event) throws Exception {
        
        Button pritisnutTaster = (Button)event.getSource();
                
        if (textFieldFocus.isEmpty()) {
            return;
        }
        
        if (textFieldFocus.equals(kol.getId())) {
            
            String tekst = kol.getText();
            tekst += pritisnutTaster.getText();
            kol.setText(tekst);
            return;
        }
        
        if (textFieldFocus.equals(cena.getId())) {
            
            String tekst = cena.getText();
            tekst += pritisnutTaster.getText();
            cena.setText(tekst);        
            return;

        }
        
        if (textFieldFocus.equals(ukupno.getId())) {
            
            String tekst = ukupno.getText();
            tekst += pritisnutTaster.getText();
            ukupno.setText(tekst); 
        }


    }
    
}
