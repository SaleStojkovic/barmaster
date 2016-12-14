/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import rmaster.assets.FXMLDocumentController;
import rmaster.assets.QueryBuilder;
import rmaster.assets.ScreenMap;
import rmaster.assets.Stampac;
import rmaster.models.StalniGost;

/**
 * FXML Controller class
 *
 * @author Bosko
 */
public class AdministracijaController extends FXMLDocumentController{
    
    @FXML
    private Button fxID_DnevniIzvestaj;
    @FXML
    private Button fxID_PeriodicniIzvestaj;
    @FXML
    private Button fxID_ZakljucenjeDana;
    @FXML
    private Button fxID_PrometKorisnika;
    @FXML
    private Button fxID_PresekStanja;
    @FXML
    private Button fxID_ProdatiArtikli;
    @FXML
    private Button fxID_Izlaz;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    

    /**
     * 
     * @param data 
     */
    @Override
    public void initData(Object data) {
        List<Map<String, String>> listaRezultata = null;
        try {
            QueryBuilder query = new QueryBuilder();
            query.setTableName("posmeni");
            query.setColumns("fxID");
            query.setCriteriaColumns("korisnikID", "vrstaKorisnika");
            query.setCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL);
            query.setOperators(QueryBuilder.LOGIC_AND);
            query.setCriteriaValues(ulogovaniKonobar.konobarID + "", "1");

            listaRezultata = runQuery(query);
        } catch (Exception e) {
            System.out.println("Greska u pozivu SP getAdministracijaMeni! - " + e.toString());
        }
        for (Map mapPrivilegija : listaRezultata) {
            try {
                Button button = (Button) fxID_Izlaz.getScene().lookup("#" + mapPrivilegija.get("fxID"));
                button.setDisable(false);
            } catch (Exception e) {
                
            }
            
        }
        
    }
    
    public void stampajDnevniIzvestaj(ActionEvent event) {
        Stampac.getInstance().stampajDnevniIzvestajNaFiskal();
    }

    public void stampajPeriodicniIzvestaj(ActionEvent event) {
        Date doDatuma = new Date();
        Stampac.getInstance().stampajPeriodicni(doDatuma, doDatuma);
    }

    public void zakljucenjeDana(ActionEvent event) {
        
    }
    
    public void prometKorisnika(ActionEvent event) {
        
    }

    public void presekStanja(ActionEvent event) {
        Stampac.getInstance().stampajPresekNaFiskal();
    }

    public void dnevniIzvestajIPresek(ActionEvent event) {
        Stampac.getInstance().stampajDnevniIzvestajIPresekNaFiskal();
    }

    public void prodatiArtikli(ActionEvent event) {
        
    }

    public void nazadNaPrikazSala(ActionEvent event) {

        prikaziFormu(
            new Object(),
            ScreenMap.PRIKAZ_SALA, 
            true, 
            (Node)event.getSource());
    }
    
}
