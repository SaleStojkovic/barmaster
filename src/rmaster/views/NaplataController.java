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
import java.util.Optional;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import rmaster.assets.DBBroker;
import rmaster.assets.FXMLDocumentController;
import rmaster.assets.QueryBuilder.QueryBuilder;
import rmaster.ScreenController;
import rmaster.assets.ScreenMap;
import rmaster.assets.Stampa;
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

    ScreenController myController; 
     
    @FXML private ImageView barMasterLogo;
    
    @FXML private Label fxID_Total;
    @FXML private Label fxID_Popust;
    @FXML private Label fxID_ZaUplatu;
    @FXML private Label fxID_Uplaceno;
    @FXML private Label fxID_Kusur;
    @FXML private Label casovnik;
    @FXML private Label imeKonobara;
    
    @FXML private TextField skenerKartice;
    
    @FXML private VBox fxID_PopustiZaNaplatu1;
    @FXML private VBox fxID_PopustiZaNaplatu2;
    
    @FXML private GridPane fxID_NacinPlacanjaGrid;
    @FXML private ToggleGroup tgVrstaPlacanja;
    @FXML private ToggleButton fxID_Faktura;
    @FXML private ToggleButton fxID_Cek;
    @FXML private ToggleButton fxID_Kartica;
    @FXML private ToggleButton fxID_Gotovina;
    
    @FXML private Button fxID_Odustani;
    @FXML private ToggleButton fxID_GotovinskiRacun;
    @FXML private Button fxID_PorudzbinaMedjuzbir;
    @FXML private Button fxID_Lojalnost;
    @FXML private Button fxID_Naplata;
    
    public enum VrstaRacunaZaStampu {FISKALNI, FAKTURA, GOTOVINSKI, MEDJUZBIR};
    
    private Porudzbina porudzbina;
    private List<NacinPlacanja> placanja = new ArrayList();
    private NacinPlacanja aktivnoPlacanje;
    
    private Map<String, String> mapaPopusta = new HashMap();
    private double total = 0.;
//    private double popustPorudzbineProcenat = 0.;
    private double popustPorudzbineIznos = 0.;
    private double zaUplatu = 0.;
    private double kusur = 0.;
    
    private String sVrednostFaktura = "";
    private String sVrednostCek = "";
    private String sVrednostKartica = "";
    private String sVrednostGotovina = "";
    private Node roditelj = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        popuniPopuste();
        popuniHotelGost();

        barMasterLogo.setImage(RMaster.logo);
    } 
    
    @Override
    public void initData(Object data) {
        
        RMaster.setClockLabelForUpdate(casovnik);
        
        
        //this.fxID_Uplaceno.setText("0.00");
        
        this.imeKonobara.setText(RMaster.getUlogovaniKonobar().imeKonobara);

        this.fxID_Faktura.setDisable(!Settings.getInstance().getValueBoolean("faktura"));
        this.fxID_Cek.setDisable(!Settings.getInstance().getValueBoolean("cek"));
        this.fxID_Kartica.setDisable(!Settings.getInstance().getValueBoolean("kartica"));
        this.fxID_Gotovina.setDisable(!Settings.getInstance().getValueBoolean("gotovina"));
        
        if (placanja.size()==0) {
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

                                List<Object> newData = new ArrayList<>();
                                newData.add(porudzbina);
                                newData.add("Faktura");

                                myController.setScreen(ScreenMap.LOJALNOST, newData);
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

            this.tgVrstaPlacanja.selectToggle(fxID_Gotovina);
            //this.fxID_Gotovina.fire();
        }
        
        for (NacinPlacanja nacinPlacanja : placanja) {
            nacinPlacanja.setVrednostString("");
        }
        

        List<Object> d = (List<Object>)data;
        StalniGost stalniGost = null;

        for (Object object : d) {
            if (object instanceof Porudzbina) {
                porudzbina = (Porudzbina) object;
                stalniGost = porudzbina.getStalniGost();
            }

            if (object instanceof Node) {
               roditelj = (Node)object;
            }
        }
        this.total = porudzbina.getVrednostPorudzbine();
        this.fxID_Total.setText(Utils.getStringFromDouble(this.total));
        
        this.data = data;

        this.skenerKartice.setText("");
        
        osveziPrikaz();
    }
    
    @Override
    public void setScreenParent(ScreenController screenParent){ 
        myController = screenParent; 
    } 
    
    private void popuniPopuste() {
        List<Map<String, String>> popustiZaNaplatu;
        popustiZaNaplatu = runStoredProcedure("getPopustiZaNaplatu", new String[0], new String[0]);
        int brojac=0;
        for (Map<String, String> popust : popustiZaNaplatu) {
            Button popustButton = new Button(popust.get("naziv"));
            popustButton.setPrefSize(173, 79);
            popustButton.setId(popust.get("id"));
            popustButton.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        Button pop = (Button)e.getSource();
                                        StalniGost sg = new StalniGost();
                                        sg.getInstance(pop.getId());
                                        porudzbina.setStalniGost(sg);
                                        porudzbina.setPopust(Utils.getDoubleFromString(sg.popust));
                                        //popustPorudzbineProcenat = Utils.getDoubleFromString(mapaPopusta.get(pop.getId()));
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
            hotelGostButton.setPrefSize(90, 80);
            hotelGostButton.setId(hotelGost.get("id"));
            hotelGostButton.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        Button hotel = (Button)e.getSource();
                                        // TODO - odraditi crno placanje
                                        Alert alert = new Alert(Alert.AlertType.WARNING);
                                        alert.setTitle("NaplataController!");
                                        alert.setHeaderText("Obrada crnog placanja!");
                                        alert.setContentText("TODO - odraditi crno placanje");
                                        alert.showAndWait();
                                      
                                    }
                                });
            this.fxID_NacinPlacanjaGrid.add(hotelGostButton, brojac/4, brojac%4);
            brojac++;
        }
        while (brojac<8) {
            Button hotelGostButton = new Button("");
            hotelGostButton.setPrefSize(90, 80);
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
    
    private double getUplacenoBezFakture() {
        double uplaceno = 0.;
        for (NacinPlacanja nacinPlacanja : placanja) {
            if (nacinPlacanja.getNacinPlacanja() != NacinPlacanja.VrstePlacanja.FAKTURA)
                uplaceno += nacinPlacanja.getVrednost();
        }
        return uplaceno;
    }

    private double getKusur() {
        this.kusur = this.getUplaceno() - this.zaUplatu;
        return (this.kusur>0 ? this.kusur : 0);
    }
    
    private void osveziPrikaz() {
        this.zaUplatu = this.porudzbina.getVrednostPorudzbineSaObracunatimPopustom();
        this.fxID_ZaUplatu.setText(Utils.getStringFromDouble(this.zaUplatu));
        this.fxID_Popust.setText(Utils.getStringFromDouble(Utils.getDoubleFromString(this.fxID_Total.getText()) - this.zaUplatu));
        this.fxID_Uplaceno.setText(Utils.getStringFromDouble(this.getUplaceno()));
        this.fxID_Kusur.setText(Utils.getStringFromDouble(this.getKusur()));
        
        
        for (NacinPlacanja nacinPlacanja : placanja) {
            String textZaPrikaz = nacinPlacanja.getTextZaButton();
            switch (nacinPlacanja.getNacinPlacanja()) {
                case FAKTURA:
                    obradiFakturu(true);
                    this.fxID_Faktura.setText(textZaPrikaz);
                    break;
                case CEK:
                    obradiFakturu(false);
                    this.fxID_Cek.setText(textZaPrikaz);
                    break;
                case KARTICA:
                    obradiFakturu(false);
                    this.fxID_Kartica.setText(textZaPrikaz);
                    break;
                case GOTOVINA:
                    obradiFakturu(false);
                    this.fxID_Gotovina.setText(textZaPrikaz);
                    break;
            }
        }
    }
    
    public void obradiFakturu(boolean jeFaktura) {
        if (!jeFaktura && (this.getUplacenoBezFakture()!= 0)) {
            
            this.fxID_Faktura.setDisable(true);
            
            for (NacinPlacanja nacinPlacanja : placanja) {
                
                if (nacinPlacanja.getNacinPlacanja() == NacinPlacanja.VrstePlacanja.FAKTURA) {
                    
                    nacinPlacanja.setVrednost(0);
                }
            }
            return;
        }
        this.fxID_Faktura.setDisable(false);
        
        
        // TODO
        // Otvoriti formu ili izvestaj za fakturu

    }


    
    public void otvoriLojalnost(ActionEvent event) {
        List<Object> newData = new ArrayList<>();
        newData.add(porudzbina);
        newData.add("Gosti");
        
        myController.setScreen(ScreenMap.LOJALNOST, newData);
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
    
    private void stampajSnimiZatvoriFormu(VrstaRacunaZaStampu vrstaRacuna, ActionEvent event) {
        // TODO
        // Ovde treba odraditi zatvaranje forme i odlazak na stolove ili LOG-OF
        this.snimi(vrstaRacuna);
        if (vrstaRacuna == VrstaRacunaZaStampu.FISKALNI) {
            Stampa.getInstance().stampajFiskalniRacun(porudzbina, placanja);
        } else if (vrstaRacuna == VrstaRacunaZaStampu.MEDJUZBIR) {
            Stampa.getInstance().stampajMedjuzbir(porudzbina);
        } else if ((vrstaRacuna == VrstaRacunaZaStampu.FAKTURA) || (vrstaRacuna == VrstaRacunaZaStampu.GOTOVINSKI)) {
            Stampa.getInstance().stampajFiskalniRacun(porudzbina, placanja);
            Stampa.getInstance().stampajFakturu(vrstaRacuna, porudzbina);
        }
        this.placanja.clear();
        myController.setScreen(ScreenMap.PRIKAZ_SALA, null);

    }
    
    @FXML
    public void naplata(ActionEvent event) {
        NacinPlacanja nacinPlacanjaGotovina = getNacinPlacanja(NacinPlacanja.VrstePlacanja.GOTOVINA);
        if (this.getUplaceno() == 0) {
            // Nista nije uneto, obracunava kao da je tacan iznos gotovine
            // - ako nista nije kucano knjizi kao da je uplacen tacan iznos u gotovini
            nacinPlacanjaGotovina.setVrednost(this.zaUplatu);
            //osveziPrikaz();
            if (this.fxID_GotovinskiRacun.isSelected()) {
                stampajSnimiZatvoriFormu(VrstaRacunaZaStampu.GOTOVINSKI, event);
            } else {
                stampajSnimiZatvoriFormu(VrstaRacunaZaStampu.FISKALNI, event);
            }
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
            
            if (porudzbina.getBrojFakture()==null || porudzbina.getBrojFakture().equals("")) {
                String[] imenaArgumenata = {"settingName"};
                String[] vrednostiArgumenata = {"faktura.broj.sledeci"};
                int rez = DBBroker.getValueFromFunction("getSledeciRedniBroj", imenaArgumenata, vrednostiArgumenata);
                this.porudzbina.setBrojFakture("" + rez);
                HashMap<String,String> h = new HashMap<>();
                h.put("brojFakture", porudzbina.getBrojFakture());
                try {
                    DBBroker.izmeni("racun", "id", "" + porudzbina.getID(), h, Boolean.TRUE);
                } catch (Exception e) {
                    
                }
                    
            }

            stampajSnimiZatvoriFormu(VrstaRacunaZaStampu.FAKTURA, event);
        }
        
        if (this.kusur > nacinPlacanjaGotovina.getVrednost()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Naplata!");
                alert.setHeaderText("Neregularno plaćanje!");
                alert.setContentText("Kusur ne sme biti veći od iznosa plaćenog gotovinom.");
                alert.showAndWait();
                return;
        }
        
        if (this.kusur>0)
            nacinPlacanjaGotovina.setVrednost(nacinPlacanjaGotovina.getVrednost() - this.kusur);
        
        if (this.fxID_GotovinskiRacun.isSelected()) {
            stampajSnimiZatvoriFormu(VrstaRacunaZaStampu.GOTOVINSKI, event);
        } else {
            stampajSnimiZatvoriFormu(VrstaRacunaZaStampu.FISKALNI, event);
        }
    }

    public void snimi(VrstaRacunaZaStampu vrstaRacuna) {
        // TODO
        // ovde treba odraditi snimanje placanja, moguce da treba vise razlicitih snimanja
        DBBroker db = new DBBroker();
        try {
            long result = 0;
            this.porudzbina.zatvoriRacun();
            for (NacinPlacanja nacinPlacanja : placanja) {
                if (nacinPlacanja.getVrednost() > 0) {
                    HashMap<String,String> mapa = new HashMap();
                    mapa.put("iznos", "" + nacinPlacanja.getVrednost());
                    mapa.put("nacin", "" + nacinPlacanja.getNacinPlacanjaString());
                    mapa.put("vreme", "" + this.porudzbina.getVremeIzdavanjaRacuna());
                    mapa.put("RACUN_ID", "" + this.porudzbina.getID());

                    result = db.ubaciRed("placanje",mapa, Boolean.FALSE);
                }
            }
        } catch(Exception e) {

        }
    }
    
    @FXML
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
    
    @FXML
    public void prikaziRacun(ActionEvent event) {
        myController.setScreen(ScreenMap.PRIKAZ_SALA, null);
    }

    @FXML
    public void odustani(ActionEvent event) {
        this.placanja.clear();
        myController.setScreen(ScreenMap.PORUDZBINA, null);
    }
    
    public void keyListener(KeyEvent keyEvent) 
    {   
        if (keyEvent.getCharacter().isEmpty()) {
            return;
        }
        String taster = keyEvent.getCharacter();
        String tekst = skenerKartice.getText() + taster;
        
        skenerKartice.setText(tekst);
        proveriPin(keyEvent);
    }
    
    public void proveriPin(KeyEvent keyEvent)
    {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
        {
            String hotelGost = skenerKartice.getText();

            if (hotelGost.isEmpty()) {
                return;
            }
            
            QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);

            query.setTableName("hotelgost");
            query.addCriteriaColumns("sifra");
            query.addCriteria(QueryBuilder.IS_EQUAL);
            query.addCriteriaValues(hotelGost);

            List<Map<String, String>> rezultat = this.runQuery(query);

            if (rezultat.isEmpty()) {
                skenerKartice.setText("");
                return;
            }
            
            System.out.print(hotelGost + " Ima ga");
            //ovde se sad radi sta vec ako postoji
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Naplata!");
            alert.setHeaderText("CRNO PLACANJE PREPOZNATO!");
            alert.setContentText("Funkcionalnost jos uvek nije zavrsena."
                    + "Vrsta crnog placanja: "
                    + rezultat.get(0).get("naziv"));
            alert.showAndWait();

        }
    }
    
    @Override
    public void odjava(ActionEvent event)
    {            
        myController.setScreen(ScreenMap.POCETNI_EKRAN, null);
    }
    
    public void otvoriMeniPopUp(ActionEvent event)
    {
        MeniPopUpController meniPopUp = new MeniPopUpController();
        
        Optional<String> result = meniPopUp.showAndWait();

        if (result.isPresent()){
            //TODO
        }
        
        
    }
}
