/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;


import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import rmaster.assets.DBBroker;
import rmaster.assets.QueryBuilder.QueryBuilder;
import rmaster.assets.Utils;
import rmaster.models.Porudzbina;

/**
 *
 * @author Arbor
 */
public class MeniContent extends Pane{
    
    DBBroker dbBroker = new DBBroker();

    @FXML private ListView<Map<String, String>> fxID_MeniList;
    
    @FXML private TextField fxID_Kolicina;
    private String kolicina;
    
    @FXML private TextField fxID_Cena;
    private String cena;
    
    @FXML private TextField fxID_Ukupno;
    private String ukupno;
    
    private Porudzbina porudzbina;
    
    private String textFieldFocus = "";
    
    @FXML public HBox fxID_footer;
    
    private String id = "";

    private HashMap<String,String> meniPromet = null;
    
    public MeniContent(Porudzbina porudzbina, HashMap<String,String> meniPromet) {
        
        super();
        this.porudzbina = porudzbina;
        this.meniPromet = meniPromet;
        
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
        fxID_footer.setSpacing(20);
        
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
        
        fxID_MeniList.setCellFactory(lv -> new ListCell<Map<String, String>>() {
            @Override
            public void updateItem(Map<String, String> novaMapa, boolean empty) {
                super.updateItem(novaMapa, empty);
                setText(empty ? null : novaMapa.get("naziv"));
            }
        }); 

        kolicina = "";
        cena = "";
        ukupno = "";

        if (meniPromet != null) {
            kolicina = meniPromet.get("kolicina");
            cena = meniPromet.get("cena");
            ukupno = Utils.getStringFromDouble(Utils.getDoubleFromString(kolicina) * Utils.getDoubleFromString(cena));
            id = meniPromet.get("id");
         }
        
        popuniMenije();
        
        osveziPolja();
        
        Platform.runLater(() -> fxID_Kolicina.requestFocus());

    }
   
    public void dodajTekst(String akcija) {
        
        if (textFieldFocus.isEmpty()) {
            return;
        }
        
        if (textFieldFocus.equals(fxID_Kolicina.getId())) {
            // Obrada ako se unosi u polje Kolicina
            if (akcija.equals("back")) {
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
                    kolicina += akcija;
                } else if (kolicina.length()-indeksTacke <= 2) {
                    kolicina += akcija;
                }
            }
        }
        
        if (textFieldFocus.equals(fxID_Cena.getId())) {
            // Obrada ako se unosi u polje Cena
            if (akcija.equals("back")) {
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
                    cena += akcija;
                } else if (cena.length()-indeksTacke <= 2) {
                    cena += akcija;
                }
            }
        }
        
        if (textFieldFocus.equals(fxID_Ukupno.getId())) {
            // Obrada ako se unosi u polje Ukupno
            if (akcija.equals("back")) {
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
                    ukupno += akcija;
                } else if (ukupno.length()-indeksTacke <= 2) {
                    ukupno += akcija;
                }
            }
        }

        proracunajPolja();
        osveziPolja();
    }
    
    
    public HashMap<String,String> getMeniPromet() {
        return meniPromet;
    }
    
    public void numberKeyPressed(ActionEvent event) throws Exception {
        
        Button pritisnutTaster = (Button)event.getSource();
           
        String akcija = pritisnutTaster.getText();
        
        if (pritisnutTaster.getId().equals("fxID_Back")) {
            
            akcija = "back";
        
        }
        
        dodajTekst(akcija);
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
    
    public void popuniMenije() {
        
        fxID_MeniList.getItems().clear();
        
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        query.setTableName("meni");
        query.addCriteriaColumns("blokiran");
        query.addCriteria(QueryBuilder.IS_EQUAL);
        query.addCriteriaValues(QueryBuilder.BIT_0);
        query.addOrderByColumns("naziv");
        query.addOrderByCriterias(QueryBuilder.SORT_ASC);
        
        List<HashMap<String, String>> listaRezultata = dbBroker.runQuery(query);
        
        for(HashMap<String, String> novaMapa : listaRezultata) {
            fxID_MeniList.getItems().add(novaMapa);
            if (novaMapa.get("id").equals(id)) {
                fxID_MeniList.getSelectionModel().select(novaMapa);
            }
        }
    }
    
    
    public void napraviNoviMeni(ActionEvent event) {
        
        ButtonType sacuvaj = new ButtonType("Sačuvaj", ButtonBar.ButtonData.OK_DONE);
            
        Dialog dialog = new Dialog();      
        
        dialog.getDialogPane().getButtonTypes().add(sacuvaj);

        HBox box = new HBox();
        
        Label label = new Label("Naziv menija: ");
        
        TextField text = new TextField();

        box.getChildren().addAll(label, text);

        dialog.getDialogPane().setContent(box);

        dialog.getDialogPane().getStylesheets().
                addAll(this.getClass().getResource("style/style.css").toExternalForm());

        dialog.getDialogPane().getStyleClass().add("myDialog");
        dialog.initStyle(StageStyle.UNDECORATED);

        dialog.setHeaderText("Unos novog menija.");
        dialog.setTitle("");
            
        dialog.setResultConverter(new Callback<ButtonType, String>() {
            @Override
            public String call(ButtonType b) {
                if (b == sacuvaj) {
                    return text.getText();
                }
                return null;
            }
        });
                
        Optional<String> result = dialog.showAndWait();
        
        if (result.isPresent()) {

            sacuvajMeni(result.get());
        }

    }
    
    public void sacuvajMeni(String naziv) {
         
        HashMap<String, String> noviMeni = new HashMap<>();

        noviMeni.put("naziv", naziv);
        noviMeni.put("blokiran", "BIT_0");
        
        try {
            dbBroker.ubaci(
                            "meni", 
                            noviMeni,
                            true
                    );
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        popuniMenije();
        
    }
    
    public boolean snimiMenipromet() {
       
        if (fxID_MeniList.getSelectionModel().getSelectedItem() ==  null) {
            
            prikaziUpozorenje();
            
            return false;
        } 
        
//        RM_Datetime rmDate = new RM_Datetime();
//        
//        Date datum = rmDate.getDate();
        
        Map<String, String> izabraniMeni = fxID_MeniList.getSelectionModel().getSelectedItem();
       
        if (meniPromet == null) {
            meniPromet = new HashMap<>();
        } else {
            meniPromet.clear();
        }
        meniPromet.put("cena", cena);
        //noviMenipromet.put("datum", Utils.getStringFromDate(datum));
        meniPromet.put("kolicina", kolicina);
        meniPromet.put("naziv", izabraniMeni.get("naziv"));
        meniPromet.put("RACUN_ID", porudzbina.getID() + "");
        meniPromet.put("MENI_ID", izabraniMeni.get("id"));
        
        try {
            if (id.equals("")) {
                dbBroker.ubaci(
                                "menipromet", 
                                meniPromet,
                                true
                        );
            } else {
                dbBroker.izmeni("menipromet", "id", id, meniPromet, Boolean.TRUE);
            }
            
            //dialog koji prikazuje snimljeni meni
            prikaziSnimljeniMenipromet(meniPromet);
            
            return true;
            
        } catch(Exception e) {
            
            e.printStackTrace();
            return false;
        
        }
        

    }
    
    
    public void prikaziUpozorenje() {
        
        ButtonType ok = new ButtonType("U redu", ButtonBar.ButtonData.CANCEL_CLOSE);
            
        Alert alert = new Alert(
                Alert.AlertType.WARNING,
                "Morate izabrati meni!", 
                ok
        );        
                
        alert.getDialogPane().getStylesheets().
                addAll(this.getClass().getResource("style/style.css").toExternalForm());

        alert.getDialogPane().getStyleClass().add("myDialog");
        alert.initStyle(StageStyle.UNDECORATED);

        alert.setTitle("Upozorenje!");
            
        alert.showAndWait();
    }
    
    public void prikaziSnimljeniMenipromet(HashMap<String, String> meniPromet) {
        
        ButtonType ok = new ButtonType("U redu", ButtonBar.ButtonData.CANCEL_CLOSE);
            
        String text = "Podaci o meniju: \n";
        text += "\tNaziv: " +  meniPromet.get("naziv") + "\n";
        text += "\tKoličina: " + meniPromet.get("kolicina") + "\n";
        text += "\tCena: " + meniPromet.get("cena") + "\n";
        text += "\tUkupno: " + Utils.getStringFromDouble(Utils.getDoubleFromString(meniPromet.get("kolicina")) * Utils.getDoubleFromString(meniPromet.get("cena"))) + "\n";
        
        Alert alert = new Alert(
                Alert.AlertType.INFORMATION,
                text, 
                ok
        );        
                
        alert.getDialogPane().getStylesheets().
                addAll(this.getClass().getResource("style/style.css").toExternalForm());

        alert.getDialogPane().getStyleClass().add("myDialog");
        alert.initStyle(StageStyle.UNDECORATED);

        alert.setTitle("Info");
            
        alert.showAndWait();
    }
    
    public void keyboardPressed(KeyEvent ke) {
        
        if (ke.getCode() == KeyCode.BACK_SPACE) {
            dodajTekst("back");
            return;
        }
        
        char digit = ke.getText().charAt(0);
        
        if (!Character.isDigit(digit)) {
            return;
        }
       
        textFieldFocus = ((TextField) ke.getSource()).getId();
        
        dodajTekst(digit + "");
        
    }
    
    
}
