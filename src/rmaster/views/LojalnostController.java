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
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import rmaster.assets.FXMLDocumentController;
import rmaster.assets.QueryBuilder;
import rmaster.assets.ScreenMap;
import rmaster.models.Porudzbina;
import rmaster.models.StalniGost;
import javafx.geometry.Orientation;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import static java.lang.Math.round;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;
import static java.lang.Math.round;

/**
 * FXML Controller class
 *
 * @author Arbor
 */
public class LojalnostController extends FXMLDocumentController {

    
    @FXML
    private Label casovnik;
    
    @FXML
    private Label imeKonobara;
    
    @FXML
    private VBox  lojalnostSlova;
    
    @FXML
    private FlowPane lojalnostFlowPane;
    
    @FXML
    private HBox lojalnostGostiGrupe;
    
    @FXML
    private TextField skenerKartice;
    
    public int[] sirineKolone = {0, 960, 50};
    
    public String prviRed = "ABCDEFGHIJKLM12345";
    
    public String drugiRed = "NOPQRSTUVWXYZ67890";
    
    private Porudzbina porudzbina;
    
    ToggleGroup headerButtonGroup = new ToggleGroup();
    
    ToggleGroup slova = new ToggleGroup();
    
    RadioButton prikaziSve = new RadioButton();


    public int brojacOffset = 0;
    
    @Override
    public void initData(Object data) {
        if (data instanceof Porudzbina) {
                porudzbina = (Porudzbina) data;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        skenerKartice.setVisible(false);
        lojalnostFlowPane.setOrientation(Orientation.VERTICAL);
        Timeline timeline = this.prikaziCasovnik(casovnik);
        timeline.play();
        this.imeKonobara.setText(ulogovaniKonobar.imeKonobara);

        popuniSlova();

        popuniLojalnostGostiGrupe();
        
        ToggleButton dugme = (ToggleButton)lojalnostGostiGrupe.getChildren().get(0);
                
        dugme.fire();
        
        dugme.setSelected(true);

    } 
    
    
    public List<Map<String, String>> getStalniGosti(String grupaId, String text, int offset) {
        
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        query.setTableName(StalniGost.TABLE_NAME);
        query.setSelectColumns(StalniGost.PRIMARY_KEY, StalniGost.NAZIV, StalniGost.POPUST);
        
        query.addCriteriaColumns(StalniGost.SIFRA, StalniGost.BLOKIRAN, StalniGost.GRUPA_ID);
        query.addCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL);
        query.addOperators(QueryBuilder.LOGIC_AND, QueryBuilder.LOGIC_AND);
        query.addCriteriaValues("", QueryBuilder.TRUE, grupaId);
                
        if (text != null) {
            query.addCriteriaColumns(StalniGost.NAZIV);
            query.addCriteria(QueryBuilder.IS_LIKE);
            query.addOperators(QueryBuilder.LOGIC_AND);
            query.addCriteriaValues(text + "%");
        }
        
        query.setOrderBy(StalniGost.NAZIV, QueryBuilder.SORT_ASC);
        query.setLimit(20);
        query.setOffset(offset);
        
        List<Map<String, String>> listaRezultata = runQuery(query);
          
        List<Map<String, String>> listaStalnihGostiju = new ArrayList<>();
        
        for (Map mapStalniGost : listaRezultata) {
            StalniGost noviStalniGost = new StalniGost();
            noviStalniGost.makeFromHashMap((HashMap)mapStalniGost);
            
            listaStalnihGostiju.add(noviStalniGost.makeMapForTableOutput());
        }
              
        return listaStalnihGostiju;
    }
    
    
    public List<Map<String, String>> getGrupeGostiju()
    {
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        query.setTableName("stalnigostigrupa");
        query.addCriteriaColumns("naziv", "naziv");
        query.addCriteria(QueryBuilder.IS_NOT_EQUAL, QueryBuilder.IS_NOT_EQUAL);
        query.addOperators(QueryBuilder.LOGIC_AND);
        query.addCriteriaValues("PREDUZECE", "NEPOZELJNI GOST");
        
        List<Map<String, String>> listaRezultata = runQuery(query);
        
        return listaRezultata;
    }
    
    public void popuniLojalnostGostiGrupe() {
        
        List<Map<String, String>> listaGrupaGostiju = getGrupeGostiju();
                
        int sirina = round(1024/listaGrupaGostiju.size());
        
        for (Map grupaGost : listaGrupaGostiju) {
            
            RadioButton novoDugme = new RadioButton(grupaGost.get("naziv") + "");

            novoDugme.getStyleClass().remove("radio-button");
            novoDugme.getStyleClass().add("toggle-button");
            
            novoDugme.setId(grupaGost.get("id") + "");
            novoDugme.setPrefSize(sirina, 74);
            novoDugme.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        prikaziLojalneGosteZavisnoOdGrupe(e);
                                        
                                    }
                                });
            novoDugme.setToggleGroup(headerButtonGroup);
            
            lojalnostGostiGrupe.getChildren().add(novoDugme);
        }
    } 
    
    private void popuniSlova() {

        HBox prviRedBox = new HBox();
        
        for(char ch : prviRed.toCharArray()) {
            
            RadioButton novoDugme = new RadioButton();
            
            novoDugme.setId(ch + "");
            novoDugme.setPrefSize(56, 50);
            novoDugme.setText(ch + "");
            novoDugme.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        sortStalneGoste(e);
                                    }
                                });
                        
            novoDugme.getStyleClass().remove("radio-button");
            novoDugme.getStyleClass().add("toggle-button");
            
            novoDugme.setToggleGroup(slova);

            prviRedBox.getChildren().add(novoDugme);
        }
        
        lojalnostSlova.getChildren().add(prviRedBox);
        
        HBox drugiRedBox = new HBox();

                
        for(char ch : drugiRed.toCharArray()) {
           
            RadioButton novoDugme = new RadioButton();

            
            novoDugme.setId(ch + "");
            novoDugme.setPrefSize(56, 50);
            novoDugme.setText(ch + "");
            novoDugme.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        sortStalneGoste(e);
                                    }
                                });
                        
            novoDugme.getStyleClass().remove("radio-button");
            novoDugme.getStyleClass().add("toggle-button");
            
            novoDugme.setToggleGroup(slova);

            drugiRedBox.getChildren().add(novoDugme);
        }
        
        lojalnostSlova.getChildren().add(drugiRedBox);
        
        prikaziSve.setText("Prikaži sve");
        prikaziSve.setId("sve");
        prikaziSve.setPrefSize(200, 50);
        
        prikaziSve.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        sortStalneGoste(e);
                                    }
                                });
                        
            prikaziSve.getStyleClass().remove("radio-button");
            prikaziSve.getStyleClass().add("toggle-button");
            
            prikaziSve.setToggleGroup(slova);
            
            
        lojalnostSlova.getChildren().add(prikaziSve);
  
    }
    
    public void prikaziLojalneGosteZavisnoOdGrupe(ActionEvent event) {
               
        brojacOffset = 0;
        
        ToggleButton pritisnutoDugme = (ToggleButton)event.getSource();
        
        List<Map<String, String>> listaLojalnihGostiju = getStalniGosti(pritisnutoDugme.getId(), null, 0);
        
        prikaziSve.setSelected(true);
        
        popuniFlowPane(listaLojalnihGostiju);
    }
    
    public void nazadNaNaplatu(ActionEvent event) {
        List<Object> data = new ArrayList<>();
        
        data.add(porudzbina);
        
        prikaziFormu(data, 
                ScreenMap.NAPLATA, 
                true, 
                (Node)event.getSource(), false
        );
    }
    
    public void potvrdi(ActionEvent event) {
        
        Button pritisnutoDugme = (Button)event.getSource();
    
        String stalniGostId = pritisnutoDugme.getId();
        
        StalniGost izabraniGost = new StalniGost();
        
        izabraniGost.getInstance(stalniGostId);
        
        List<Object> data = new ArrayList<>();
        
        data.add(porudzbina);
        data.add(izabraniGost);
        
        prikaziFormu(data, 
                ScreenMap.NAPLATA, 
                true, 
                (Node)event.getSource(), false
        );
    }
    
    public void sortStalneGoste(ActionEvent event)
    {
        RadioButton pritisnutTaster = (RadioButton)event.getSource();
        String sortTekst = pritisnutTaster.getId();
        
        if (sortTekst.equals("sve")) {
            sortTekst = null;
        }
        
        RadioButton izabranaGrupa = (RadioButton)headerButtonGroup.getSelectedToggle();
        String grupaId = izabranaGrupa.getId();

        List<Map<String, String>> listaRezultata = getStalniGosti(grupaId, sortTekst, 0);
        
        brojacOffset = 0;
        
        popuniFlowPane(listaRezultata);
    }
    
    
    public void popuniFlowPane(List<Map<String, String>> listaRezultata) {
        
        lojalnostFlowPane.getChildren().clear();

        for (Map<String, String> stalniGostMap : listaRezultata) {

            Button stalniGostButton = new Button(stalniGostMap.get(StalniGost.NAZIV));
            
            stalniGostButton.setId(stalniGostMap.get(StalniGost.PRIMARY_KEY));

            stalniGostButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override public void handle(ActionEvent e) {
                            potvrdi(e);
                        }
                    });
            
            stalniGostButton.setPrefSize(204, 97);

            lojalnostFlowPane.getChildren().add((Node)stalniGostButton);

        }
    }
    
    public void prikaziSledecuIliPrethodnuStranuFlowPane(ActionEvent event) 
    {
        
        RadioButton izabranaGrupa = (RadioButton)headerButtonGroup.getSelectedToggle();
        String grupaId = izabranaGrupa.getId();
        
        Button button = (Button)event.getSource();
        String stisnutiTaster = button.getText();
                
        if (stisnutiTaster.equals("»") && lojalnostFlowPane.getChildren().size() > 19) {
            brojacOffset++;
            popuniFlowPane(getStalniGosti(grupaId, null, brojacOffset * 19));
        }
        
        if (stisnutiTaster.equals("«") && brojacOffset > 0 ) {
            brojacOffset--;
            popuniFlowPane(getStalniGosti(grupaId, null, brojacOffset * 19));
        }
    }
    
    public void keyListener(KeyEvent keyEvent) 
    {   
        if (keyEvent.getCharacter().isEmpty()) {
            return;
        }
        String taster = keyEvent.getCharacter();
        String tekst = skenerKartice.getText() + taster;
        
        skenerKartice.setText(tekst);
    }
    
    public void proveriPin(KeyEvent keyEvent)
    {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
        {
            String lozinka = skenerKartice.getText();

            if (lozinka.isEmpty()) {
                return;
            }
            
            QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);

            query.setTableName(StalniGost.TABLE_NAME);
            query.addCriteriaColumns(StalniGost.SIFRA);
            query.addCriteria(QueryBuilder.IS_EQUAL);
            query.addCriteriaValues(lozinka);

            List<Map<String, String>> rezultat = this.runQuery(query);

            if (rezultat.isEmpty()) {
                skenerKartice.setText("");
                return;
            }
            
            StalniGost izabraniGost = new StalniGost();
            izabraniGost.makeFromHashMap((HashMap)rezultat.get(0));
            
            List<Object> data = new ArrayList<>();
        
            data.add(porudzbina);
            data.add(izabraniGost);

            prikaziFormu(data, 
                ScreenMap.NAPLATA, 
                true, 
                (Node)keyEvent.getSource(), false
            );
        }
    }
}
