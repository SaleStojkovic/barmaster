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
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import rmaster.assets.FXMLDocumentController;
import rmaster.assets.QueryBuilder;
import rmaster.assets.ScreenMap;
import rmaster.models.Porudzbina;
import rmaster.models.StalniGost;
import static java.lang.Math.round;
import javafx.geometry.Orientation;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;

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
    
    public int[] sirineKolone = {0, 960, 50};
    
    public String prviRed = "ABCDEFGHIJKLM12345";
    
    public String drugiRed = "NOPQRSTUVWXYZ67890";
    
    private Porudzbina porudzbina;


    @Override
    public void initData(Object data) {
        if (data instanceof Porudzbina) {
                porudzbina = (Porudzbina) data;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    
    
    public List<Map<String, String>> getStalniGosti(String grupaId, int offset) {
        
        QueryBuilder query = new QueryBuilder();
        query.setTableName(StalniGost.TABLE_NAME);
        query.setColumns(StalniGost.PRIMARY_KEY, StalniGost.NAZIV, StalniGost.POPUST);
        query.setCriteriaColumns(StalniGost.SIFRA, StalniGost.BLOKIRAN, StalniGost.GRUPA_ID);
        query.setCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL);
        query.setOperators(QueryBuilder.LOGIC_AND, QueryBuilder.LOGIC_AND);
        query.setCriteriaValues("", QueryBuilder.TRUE, grupaId);
        query.setOrderBy(StalniGost.NAZIV, QueryBuilder.SORT_ASC);
        query.setLimit(19);
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
        QueryBuilder query = new QueryBuilder();
        query.setTableName("stalnigostigrupa");
        query.setCriteriaColumns("naziv", "naziv");
        query.setCriteria(QueryBuilder.IS_NOT_EQUAL, QueryBuilder.IS_NOT_EQUAL);
        query.setOperators(QueryBuilder.LOGIC_AND);
        query.setCriteriaValues("PREDUZECE", "NEPOZELJNI GOST");
        
        List<Map<String, String>> listaRezultata = runQuery(query);
        
        return listaRezultata;
    }
    
    public void popuniLojalnostGostiGrupe() {
        
        List<Map<String, String>> listaGrupaGostiju = getGrupeGostiju();
                
        int sirina = round(1024/listaGrupaGostiju.size());
        
        ToggleGroup group = new ToggleGroup();
        
        for (Map grupaGost : listaGrupaGostiju) {
            
            ToggleButton novoDugme = new ToggleButton(grupaGost.get("naziv") + "");
            novoDugme.setId(grupaGost.get("id") + "");
            novoDugme.setPrefSize(sirina, 74);
            novoDugme.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        prikaziLojalneGosteZavisnoOdGrupe(e);
                                    }
                                });
            novoDugme.setToggleGroup(group);
            
            lojalnostGostiGrupe.getChildren().add(novoDugme);
        }
    } 
    
    private void popuniSlova() {
         HBox prviRedBox = new HBox();
        
        for(char ch : prviRed.toCharArray()) {
            
            Button novoDugme = new Button();
            
            novoDugme.setId(ch + "");
            novoDugme.setPrefSize(56, 50);
            novoDugme.setText(ch + "");
            novoDugme.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        //TO DO
                                    }
                                });
            prviRedBox.getChildren().add(novoDugme);
        }
        
        lojalnostSlova.getChildren().add(prviRedBox);
        
        HBox drugiRedBox = new HBox();

                
        for(char ch : drugiRed.toCharArray()) {
           
            Button novoDugme = new Button();
            
            novoDugme.setId(ch + "");
            novoDugme.setPrefSize(56, 50);
            novoDugme.setText(ch + "");
            novoDugme.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent e) {
                                        //TODO
                                    }
                                });
            drugiRedBox.getChildren().add(novoDugme);
        }
        
        lojalnostSlova.getChildren().add(drugiRedBox);
    }
    
    public void prikaziLojalneGosteZavisnoOdGrupe(ActionEvent event) {
       
        lojalnostFlowPane.getChildren().clear();
        
        ToggleButton pritisnutoDugme = (ToggleButton)event.getSource();
        
        List<Map<String, String>> listaLojalnihGostiju = getStalniGosti(pritisnutoDugme.getId(), 0);
        
        for (Map<String, String> stalniGostMap : listaLojalnihGostiju) {

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
        
        if (listaLojalnihGostiju.size() == 19) {
            //TODO
            Button backButton = new Button("«");
            backButton.setPrefSize(102, 97);
            lojalnostFlowPane.getChildren().add(backButton);
            
            Button forwardButton = new Button("»");
            forwardButton.setPrefSize(102, 97);
            lojalnostFlowPane.getChildren().add(forwardButton);

        }
    }
    
    public void nazadNaNaplatu(ActionEvent event) {
        List<Object> data = new ArrayList<>();
        
        data.add(porudzbina);
        
        prikaziFormu(
                data, 
                ScreenMap.NAPLATA, 
                true, 
                (Node)event.getSource()
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
        
        prikaziFormu(
                data, 
                ScreenMap.NAPLATA, 
                true, 
                (Node)event.getSource()
        );
    }
}
