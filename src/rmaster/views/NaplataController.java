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
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import rmaster.assets.FXMLDocumentController;
import rmaster.assets.ScreenMap;
import rmaster.assets.Stampac;
import rmaster.assets.Utils;
import rmaster.models.NacinPlacanja;
import rmaster.models.Porudzbina;
import rmaster.models.StalniGost;

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
    @FXML
    private Button fxID_Naplata;
    
    private Porudzbina porudzbina;
    private List<NacinPlacanja> placanja = new ArrayList();
    private NacinPlacanja aktivnoPlacanje;
    
    private Map<String, String> mapaPopusta = new HashMap();
    private double total = 0.;
    private double popustPorudzbineProcenat = 0.;
    private double popustPorudzbineIznos = 0.;
    private double zaUplatu = 0.;
    private double kusur = 0.;
    
    private String sVrednostFaktura = "";
    private String sVrednostCek = "";
    private String sVrednostKartica = "";
    private String sVrednostGotovina = "";
    
    private StalniGost stalniGost;
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
                        chk = (ToggleButton)t1.getToggleGroup().getSelectedToggle();
                    else
                        chk = (ToggleButton)t;

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
            if (object instanceof Porudzbina) {
                porudzbina = (Porudzbina) object;
            }
            if (object instanceof StalniGost) {
               stalniGost = (StalniGost)object;
               popustPorudzbineProcenat = Double.parseDouble(stalniGost.popust);
            }
        }
        this.total = porudzbina.getVrednostPorudzbine();
        this.fxID_Total.setText(Utils.getStringFromDouble(this.total));

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
                                        popustPorudzbineProcenat = Utils.getDoubleFromString(mapaPopusta.get(pop.getId()));
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

    private double getUplaceno() {
        double uplaceno = 0.;
        for (NacinPlacanja nacinPlacanja : placanja) {
            uplaceno += nacinPlacanja.getVrednost();
        }
        return uplaceno;
    }
    
    private double getKusur() {
        this.kusur = this.getUplaceno() - this.zaUplatu;
        return (this.kusur>0 ? this.kusur : 0);
    }
    
    private void osveziPrikaz() {
        this.fxID_Popust.setText(Utils.getStringFromDouble(Utils.getDoubleFromString(this.fxID_Total.getText()) * (popustPorudzbineProcenat/100)));
        this.zaUplatu = Utils.getDoubleFromString(this.fxID_Total.getText()) - Utils.getDoubleFromString(this.fxID_Popust.getText());
        this.fxID_ZaUplatu.setText(Utils.getStringFromDouble(this.zaUplatu));
        this.fxID_Uplaceno.setText(Utils.getStringFromDouble(this.getUplaceno()));
        this.fxID_Kusur.setText(Utils.getStringFromDouble(this.getKusur()));
        
        
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
        prikaziFormu(
                porudzbina, 
                ScreenMap.LOJALNOST, 
                true, 
                (Node)event.getSource()
        );
    }
    
    public void medjuzbir(ActionEvent event) {
    }

    private NacinPlacanja getNacinPlacanja(NacinPlacanja.VrstePlacanja vrstaPlacanja) {
        // Vraca nacin placanja, ako ne postoji kreira ga, ubacuje u listu placanja i vraca
        NacinPlacanja placanje = null;
        for (NacinPlacanja nacinPlacanja : placanja) {
            if (nacinPlacanja.getNacinPlacanja() == vrstaPlacanja) {
                placanje = nacinPlacanja;
                break;
            }
        }
        if (placanje == null) {
            placanje = new NacinPlacanja(vrstaPlacanja);
            placanja.add(placanje);
        }
        return placanje;
    }
    
    public void naplata(ActionEvent event) {
        if (this.getUplaceno() == 0) {
            // Nista nije uneto, obracunava kao da je tacan iznos gotovine
            // - ako nista nije kucano knjizi kao da je uplacen tacan iznos u gotovini
            NacinPlacanja nacinPlacanjaGotovina = getNacinPlacanja(NacinPlacanja.VrstePlacanja.GOTOVINA);
            nacinPlacanjaGotovina.setVrednost(porudzbina.getVrednostPorudzbine());
            osveziPrikaz();
            this.snimi();
            Stampac.getInstance().stampajGotovinskiRacun();
            return;
        }

        if (this.getUplaceno() < this.zaUplatu) {
            // Nedovoljno uplaceno
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Naplata!");
            alert.setHeaderText("Nedovoljan iznos!");
            alert.setContentText("Morate uneti dovoljan iznos za plaćanje.");
            alert.showAndWait();
            return;
        }

        NacinPlacanja nacinPlacanjaFaktura = getNacinPlacanja(NacinPlacanja.VrstePlacanja.FAKTURA);
        if (nacinPlacanjaFaktura.getVrednost() != 0) {
            // Placanje fakturom
            if (nacinPlacanjaFaktura.getVrednost() != this.zaUplatu) {
                // Provera da li je tacan iznos i da li ima jos nekog placanja osim fakture, ne sme da bude
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Naplata!");
                alert.setHeaderText("Netačan iznos fakture!");
                alert.setContentText("Morate uneti tačan iznos računa za plaćanje fakturom.");
                alert.showAndWait();
                return;
            }
            osveziPrikaz();
            this.snimi();
            Stampac.getInstance().stampajFakturu();
        }
        
        if (this.kusur > getNacinPlacanja(NacinPlacanja.VrstePlacanja.GOTOVINA).getVrednost()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Naplata!");
                alert.setHeaderText("Neregularno plaćanje!");
                alert.setContentText("Kusur ne sme biti veći od iznosa plaćenog gotovinom.");
                alert.showAndWait();
                return;
        }
        /*
        for (NacinPlacanja nacinPlacanja : placanja) {
            if (nacinPlacanja.getNacinPlacanja() == NacinPlacanja.VrstePlacanja.GOTOVINA)
                nacinPlacanja.setVrednost(porudzbina.getVrednostPorudzbine());
        }
                                - ako je ista menjano proverava sledece
                                        kombinovano ili ne?
                                        kombinovano - ne sme faktura, mora biti tacan iznos ili da gotovina prelazi preko a u bazu se upisuje tacno po racunu bez kusura
                                        ne - ok, ima kusura"
        */
    }

    public void snimi() {
        // ovde treba odraditi snimanje placanja, moguce da treba vise razlicitih snimanja
    }
    public void backButton(ActionEvent event) {
        String text = this.aktivnoPlacanje.getVrednostString();
        
        if (text.length() != 0) {
            text = text.substring(0, text.length()-1);
            if (text.endsWith("."))
                text = text.substring(0, text.length()-1);
            this.aktivnoPlacanje.setVrednostString(text);
        }
        this.osveziPrikaz();        
    }

    public void numberKeyPressed(ActionEvent event) throws Exception {
        Button pritisnutTaster = (Button)event.getSource();
        String text = this.aktivnoPlacanje.getVrednostString();
        text += pritisnutTaster.getText();
        this.aktivnoPlacanje.setVrednostString(text);
        long iznos = 0;
        if (!this.aktivnoPlacanje.getVrednostString().equals(""))
            iznos = (long)(this.aktivnoPlacanje.getVrednost());
        if (iznos > 9999999999L) {
            // Unet preveliki iznos
            text = text.substring(0, text.length()-1);
            if (text.endsWith("."))
                text = text.substring(0, text.length()-1);
            this.aktivnoPlacanje.setVrednostString(text);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Naplata!");
            alert.setHeaderText("Preveliki iznos!");
            alert.setContentText("Ne možete uneti toliki iznos za plaćanje.");
            alert.showAndWait();
            return;
        }
        this.osveziPrikaz();
    }
    
}
