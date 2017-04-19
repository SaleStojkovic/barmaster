/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import rmaster.assets.FXMLDocumentController;
import rmaster.assets.QueryBuilder.QueryBuilder;
import rmaster.ScreenController;
import rmaster.assets.RM_Datetime;
import rmaster.assets.ScreenMap;
import rmaster.assets.Stampa;

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

    
    ScreenController myController; 
     
    @Override
    public void setScreenParent(ScreenController screenParent){ 
        myController = screenParent; 
    } 
    
    /**
     * 
     * @param data 
     */
    @Override
    public void initData(Object data) {
        List<Map<String, String>> listaRezultata = null;
        try {
            QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
            query.setTableName("posmeni");
            query.setSelectColumns("fxID");
            query.addCriteriaColumns("korisnikID", "vrstaKorisnika");
            query.addCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL);
            query.addOperators(QueryBuilder.LOGIC_AND);
            query.addCriteriaValues(RMaster.getUlogovaniKonobar().konobarID + "", "1");

            listaRezultata = runQuery(query);
        } catch (Exception e) {
            System.out.println("Greska u pozivu SP getAdministracijaMeni! - " + e.toString());
        }
        
        ArrayList<Node> nodes = new ArrayList<>();
        addAllDescendentsNodes(fxID_Izlaz.getParent().getParent(), nodes);

        for (Node node : nodes) {
            node.setDisable(true);
        }
        fxID_Izlaz.setDisable(false);

        for (Map mapPrivilegija : listaRezultata) {
            try {
                Button button = (Button) fxID_Izlaz.getScene().lookup("#" + mapPrivilegija.get("fxID"));
                button.setDisable(false);
            } catch (Exception e) {
                
            }
            
        }
        
    }
    
    public void stampajDnevniIzvestaj(ActionEvent event) {
        Stampa.getInstance().stampajDnevniIzvestajNaFiskal();
    }

    public void stampajPeriodicniIzvestaj(ActionEvent event) {
        
        PeriodicniIzvestajPopUp popUp = new PeriodicniIzvestajPopUp();
        
        Optional<HashMap<String,String>> result = popUp.showAndWait();

        if (result.isPresent()){
            
            HashMap<String, Date> mapaDatuma = (HashMap)result.get();
            
            Stampa.getInstance().stampajPeriodicni(
                    mapaDatuma.get("datumOd"), 
                    mapaDatuma.get("datumDo")
            );
        }
        
    }

    public void zakljucenjeDana(ActionEvent event) {
        
    }
    
    public void prometKorisnika(ActionEvent event) {
        
    }

    public void presekStanja(ActionEvent event) {
        Stampa.getInstance().stampajPresekNaFiskal();
    }

    public void dnevniIzvestajIPresek(ActionEvent event) {
        Stampa.getInstance().stampajDnevniIzvestajIPresekNaFiskal();
    }

    public void prodatiArtikli(ActionEvent event) {
        
    }

    public void nazadNaPrikazSala(ActionEvent event) {

        myController.setScreen(ScreenMap.PRIKAZ_SALA, null);
    }
      
    @Override
    public void odjava(ActionEvent event)
    {            
        myController.setScreen(ScreenMap.POCETNI_EKRAN, null);
    }
}
