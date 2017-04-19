/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import rmaster.assets.RM_Datetime;

/**
 *
 * @author Arbor
 */
public class PeriodicniIzvestajPopUp extends Dialog {
    
    DatePicker datumOd = new DatePicker();

    DatePicker datumDo = new DatePicker();

    
    public PeriodicniIzvestajPopUp()
    {
        inicijalizuj();
        
        this.initStyle(StageStyle.UNDECORATED);
        this.getDialogPane().getStylesheets().
                addAll(this.getClass().getResource("style/style.css").toExternalForm());
        this.getDialogPane().getStyleClass().add("myDialog");
        
        Button potvrdi = new Button("Potvrdi");
        Button otkazi = new Button("Otkaži");
        
        potvrdi.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {
                                
                                vratiDatumeMap();
                               
                            }
        });
        
        otkazi.setOnAction(new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent e) {
                                
                                Button otkazi = (Button) e.getSource();
                                otkazi.getScene().getWindow().hide();
                            }
        });
        
        potvrdi.setPrefSize(140, 50);
        
        otkazi.setPrefSize(140, 50);

        Label dateOd = new Label("Datum od");
        
        Label dateDo = new Label("Datum do");
        
        HBox date1 = new HBox(20);
        
        date1.getChildren().addAll(dateOd, datumOd);
        
        HBox date2 = new HBox(20);
        
        date2.getChildren().addAll(dateDo, datumDo);
        
        VBox content = new VBox(20);
        
        VBox datumiBox = new VBox(20);
        
        HBox dugmiciBox = new HBox(20);
        
        datumiBox.getChildren().addAll(date1, date2);
        
        dugmiciBox.getChildren().addAll(otkazi, potvrdi);
        
        content.getChildren().addAll(datumiBox, dugmiciBox);
        
        this.getDialogPane().setContent(content);
    }
    
    private void inicijalizuj()
    {
        datumOd.setConverter(
            new StringConverter<LocalDate>() {
                private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                @Override
                public String toString(LocalDate localDate) {
                    if(localDate == null) {
                        return "";
                    }
                    return dateTimeFormatter.format(localDate);
                }

                @Override
                public LocalDate fromString(String dateString) {
                    if(dateString == null || dateString.trim().isEmpty()) {
                        return null;
                    }
                    return LocalDate.parse(dateString, dateTimeFormatter);
                }
            }
        );
        
        datumDo.setConverter(
            new StringConverter<LocalDate>() {
                private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                @Override
                public String toString(LocalDate localDate) {
                    if(localDate == null) {
                        return "";
                    }
                    return dateTimeFormatter.format(localDate);
                }

                @Override
                public LocalDate fromString(String dateString) {
                    if(dateString == null || dateString.trim().isEmpty()) {
                        return null;
                    }
                    return LocalDate.parse(dateString, dateTimeFormatter);
                }
            }
        );
        
        datumOd.setPromptText("Datum od");
        datumDo.setPromptText("Datum do");

        RM_Datetime rmDate = new RM_Datetime();
        
        Date danasnjidatum = rmDate.getDate();
        
        Calendar kalendar = Calendar.getInstance();

        kalendar.setTime(danasnjidatum);
        kalendar.add(Calendar.DAY_OF_MONTH, -1);
        
        Date jucerasnjiDatum = kalendar.getTime();
        
        LocalDate datumOdDeafultValue = jucerasnjiDatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate datumDoDeafultValue = danasnjidatum.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        
        datumOd.setValue(
                datumOdDeafultValue
        );
        
        datumDo.setValue(
                datumDoDeafultValue
        );
    }
    
    private void vratiDatumeMap()
    {
        if(datumOd.getValue() == null || datumDo.getValue() == null) {
            
            ButtonType no = new ButtonType("U redu", ButtonBar.ButtonData.CANCEL_CLOSE);
            
            Alert alert = new Alert(
                Alert.AlertType.WARNING,
                "Morate izabrati oba datuma.", 
                no
            );        
                
            alert.getDialogPane().getStylesheets().
                    addAll(this.getClass().getResource("style/style.css").toExternalForm());

            alert.getDialogPane().getStyleClass().add("myDialog");
            alert.initStyle(StageStyle.UNDECORATED);

            alert.setHeaderText("Neispravan unos!");
            alert.setTitle("Upozorenje!");

            alert.showAndWait();

            return;
        }
        
        HashMap<String, Date> datumiMap = new HashMap();

        Date dateOd = Date.from(datumOd.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        Date dateDo = Date.from(datumDo.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        datumiMap.put("datumOd", dateOd);
        datumiMap.put("datumDo", dateDo);

        this.setResult(datumiMap);
        this.close();

    }
    
}
