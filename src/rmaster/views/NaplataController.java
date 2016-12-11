/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import rmaster.assets.Settings;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import rmaster.assets.FXMLDocumentController;
import rmaster.assets.Utils;
import rmaster.models.NacinPlacanja;
import rmaster.models.Porudzbina;

/**
 * FXML Controller class
 *
 * @author Arbor
 */
public class NaplataController extends FXMLDocumentController {

    @FXML
    private Label fxID_Total;
    @FXML
    private Label fxID_Popust;
    @FXML
    private Label fxID_ZaUplatu;
    @FXML
    private Label fxID_Uplaceno;
    @FXML
    private Label fxID_Kusur;
    
    @FXML
    private VBox fxID_PopustiZaNaplatu1;
    @FXML
    private VBox fxID_PopustiZaNaplatu2;
    
    @FXML
    private GridPane fxID_NacinPlacanjaGrid;
    @FXML
    private ToggleGroup tgVrstaPlacanja;
    @FXML
    private ToggleButton fxID_Faktura;
    @FXML
    private ToggleButton fxID_Cek;
    @FXML
    private ToggleButton fxID_Kartica;
    @FXML
    private ToggleButton fxID_Gotovina;
    
    @FXML
    private Button fxID_PorudzbinaMedjuzbir;
    @FXML
    private Button fxID_Lojalnost;
    
    private Porudzbina porudzbina;
    private List<NacinPlacanja> placanja = new ArrayList();
    private NacinPlacanja aktivnoPlacanje;
    
    private Map<String, String> mapaPopusta = new HashMap();
    private double popustPorudzbine = 0.;
    
    private String sVrednostFaktura = "";
    private String sVrednostCek = "";
    private String sVrednostKartica = "";
    private String sVrednostGotovina = "";
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.fxID_Faktura.setDisable(!Settings.getInstance().getValueBoolean("faktura"));
        this.fxID_Cek.setDisable(!Settings.getInstance().getValueBoolean("cek"));
        this.fxID_Kartica.setDisable(!Settings.getInstance().getValueBoolean("kartica"));
        this.fxID_Gotovina.setDisable(!Settings.getInstance().getValueBoolean("gotovina"));
        
        placanja.add(new NacinPlacanja(NacinPlacanja.VrstePlacanja.FAKTURA));
        placanja.add(new NacinPlacanja(NacinPlacanja.VrstePlacanja.CEK));
        placanja.add(new NacinPlacanja(NacinPlacanja.VrstePlacanja.KARTICA));
        aktivnoPlacanje = new NacinPlacanja(NacinPlacanja.VrstePlacanja.GOTOVINA);
        placanja.add(aktivnoPlacanje);
        
        tgVrstaPlacanja.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
            {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
                ToggleButton chk;
                if (t1 != null)
                    chk = (ToggleButton)t1.getToggleGroup().getSelectedToggle(); // Cast object to radio button
                else
                    chk = (ToggleButton)t; // Cast object to radio button

                switch (chk.getId()) {
                    case "fxID_Faktura":
                        setAktivnoPlacanje(NacinPlacanja.VrstePlacanja.FAKTURA);
                        break;
                    case "fxID_Cek":
                        setAktivnoPlacanje(NacinPlacanja.VrstePlacanja.CEK);
                        break;
                    case "fxID_Kartica":
                        setAktivnoPlacanje(NacinPlacanja.VrstePlacanja.KARTICA);
                        break;
                    case "fxID_Gotovina":
                        setAktivnoPlacanje(NacinPlacanja.VrstePlacanja.GOTOVINA);
                        break;
                }
                aktivnoPlacanje.setVrednost(aktivnoPlacanje.getVrednost() + 99.);
                osveziPrikaz();
                }
            });
        popuniPopuste();
        popuniHotelGost();
        osveziPrikaz();
    } 
    
    /* BOSKO DODAO */
    public void initData(Object data) {
        List<Object> d = (List<Object>)data;
        for (Object object : d) {
            if (object instanceof Porudzbina)
                porudzbina = (Porudzbina) object;
        }
        this.fxID_Total.setText(Utils.getStringFromDouble(porudzbina.getVrednostPorudzbine()));

        this.data = data;
    }
    
    private void popuniPopuste() {
        List<Map<String, String>> popustiZaNaplatu;
        popustiZaNaplatu = runStoredProcedure("getPopustiZaNaplatu", new String[0], new String[0]);
        int brojac=0;
        for (Map<String, String> popust : popustiZaNaplatu) {
            Button popustButton = new Button(popust.get("naziv"));
            popustButton.setId(popust.get("id"));
            popustButton.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        Button pop = (Button)e.getSource();
                                        popustPorudzbine = Utils.getDoubleFromString(mapaPopusta.get(pop.getId()));
                                        osveziPrikaz();
                                    }
                                });
           mapaPopusta.put(popust.get("id"), popust.get("popust"));
            if (brojac<4)
                this.fxID_PopustiZaNaplatu1.getChildren().add(popustButton);
            else
                this.fxID_PopustiZaNaplatu2.getChildren().add(popustButton);
            brojac++;
        }
    }
    
    private void popuniHotelGost() {
        List<Map<String, String>> hotelGostList;
        hotelGostList = runStoredProcedure("getHotelGost", new String[0], new String[0]);
        int brojac=0;
        for (Map<String, String> hotelGost : hotelGostList) {
            Button hotelGostButton = new Button(hotelGost.get("naziv"));
            hotelGostButton.setId(hotelGost.get("id"));
            hotelGostButton.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        Button hotel = (Button)e.getSource();
                                        //popustPorudzbine = Utils.getDoubleFromString(mapaPopusta.get(pop.getId()));
                                        //osveziPrikaz();
                                    }
                                });
            this.fxID_NacinPlacanjaGrid.add(hotelGostButton, brojac/4, brojac%4);
            brojac++;
        }
        while (brojac<8) {
            Button hotelGostButton = new Button("");
            hotelGostButton.setDisable(true);
            this.fxID_NacinPlacanjaGrid.add(hotelGostButton, brojac/4, brojac%4);
            brojac++;
        }
    }

    private void setAktivnoPlacanje(NacinPlacanja.VrstePlacanja vp) {
        for (NacinPlacanja nacinPlacanja : placanja) {
            if (nacinPlacanja.getNacinPlacanja() == vp) {
                aktivnoPlacanje = nacinPlacanja;
                return;
            }
        }
    }
    
    private void osveziPrikaz() {
        this.fxID_Popust.setText(Utils.getStringFromDouble(Utils.getDoubleFromString(this.fxID_Total.getText()) * (popustPorudzbine/100)));
        this.fxID_ZaUplatu.setText(Utils.getStringFromDouble(Utils.getDoubleFromString(this.fxID_Total.getText()) - Utils.getDoubleFromString(this.fxID_Popust.getText())));
        double uplaceno = 0.;
        for (NacinPlacanja nacinPlacanja : placanja) {
            uplaceno += nacinPlacanja.getVrednost();
        }
        this.fxID_Uplaceno.setText(Utils.getStringFromDouble(uplaceno));
        double kusur = Utils.getDoubleFromString(this.fxID_Uplaceno.getText()) - Utils.getDoubleFromString(this.fxID_ZaUplatu.getText());
        
        this.fxID_Kusur.setText(Utils.getStringFromDouble(kusur>0?kusur:0.));
        
        
        for (NacinPlacanja nacinPlacanja : placanja) {
            String textZaPrikaz = nacinPlacanja.getTextZaButton();
            switch (nacinPlacanja.getNacinPlacanja()) {
                case FAKTURA:
                    this.fxID_Faktura.setText(textZaPrikaz);
                    break;
                case CEK:
                    this.fxID_Cek.setText(textZaPrikaz);
                    break;
                case KARTICA:
                    this.fxID_Kartica.setText(textZaPrikaz);
                    break;
                case GOTOVINA:
                    this.fxID_Gotovina.setText(textZaPrikaz);
                    break;
            }
        }
    }
    
    public void otvoriLojalnost(ActionEvent event) {
    }
    
    public void medjuzbir(ActionEvent event) {
    }
}
