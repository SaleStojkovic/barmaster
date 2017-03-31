/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import rmaster.ScreenController;
import rmaster.assets.FXMLDocumentController;
import rmaster.assets.RM_Button.RM_Button;

/**
 *
 * @author Bosko
 */
public abstract class PrikazSalaParentController extends FXMLDocumentController {

    ScreenController myController; 
    

    @Override
    public void setScreenParent(ScreenController screenParent){ 
        myController = screenParent; 
    } 
    
    
    @FXML
    protected TabPane saleTabPane;
    
    protected Map<String, Tab> saleSkriveniTabovi = new LinkedHashMap<String, Tab>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prikaziSale();
    }

    @Override
    public void initData(Object data) {
    }

    @Override
    public void odjava(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    protected void prikaziSale() {
        List<Map<String, String>> sale = new ArrayList<>();
        for (Map<String, String> map : RMaster.sveSale) {
            sale.add(map);
        }
        
        saleTabPane.setSide(Side.BOTTOM);

        Task<List> prikaziSaluTask = new Task<List>() {
            @Override
            public List call() throws Exception {
               prikaziSalu(sale.get(0));
               sale.remove(0);
               return null;
            }
        };
            
        prikaziSaluTask.setOnSucceeded(e -> {
                for(Map<String, String> salaMap : sale){

                    new Thread() {
                        @Override
                        public void start() {
                            prikaziSalu(salaMap);
                        }
                    }.start();
                }    
            }
        );
        
        new Thread(prikaziSaluTask).start();
    }


    public void prikaziSamoSaleOmoguceneKonobaru()
    {
        for (Map.Entry<String, Tab> entry : saleSkriveniTabovi.entrySet()) {
            String key = entry.getKey();
            Tab value = entry.getValue();
            saleTabPane.getTabs().add(Integer.parseInt(key), value);
            saleSkriveniTabovi.remove(key);
        }

        int brojac = 0;
        
        for (Tab salaTab : saleTabPane.getTabs()) {
            String salaTabId = salaTab.getId();
            
            for (Map<String, String> salaZabranjena : RMaster.saleZabranjeneKonobaru) {
                String salaZabranjenaId = salaZabranjena.get("grafik_id");

                if (salaZabranjenaId.equals(salaTabId))
                {
                    saleSkriveniTabovi.put("" + brojac, salaTab);
                    break;
                }
            }   
            brojac++;
        }

        for (Map.Entry<String, Tab> entry : saleSkriveniTabovi.entrySet()) {
            String key = entry.getKey();
            Tab tabZaSkrivanje = entry.getValue();
            saleTabPane.getTabs().remove(tabZaSkrivanje);
        }
            
        RMaster.firstLogin = false;
    }

    private void prikaziSalu(Map<String, String> salaMap) {
        Tab newTab = new Tab();
        newTab.setId(salaMap.get("id"));

        newTab.setText(salaMap.get("naziv"));

        AnchorPane novaSala = new AnchorPane();

        prikaziStoloveSale(novaSala, salaMap.get("id"));

        novaSala.setBackground(getBackground(salaMap.get("slika")));

        newTab.setContent(novaSala);

        saleTabPane.getTabs().add(newTab);

        if (RMaster.trenutnaSalaID == Long.parseLong(salaMap.get("id"))) {        
            saleTabPane.getSelectionModel().select(newTab);
        }
    }
   
    private void prikaziStoloveSale(AnchorPane sala, String salaId) 
    {
        List<Map<String, String>> stoloviZaPrikaz = RMaster.getStoloveBySalaId(salaId);
            
        for (Map<String, String> stoMap : stoloviZaPrikaz)
        {
            StackPane okvir = napraviSto(stoMap);
            sala.getChildren().add(okvir);
        }
    }
    
    protected StackPane napraviSto(Map<String, String> stoMap)
    {
        StackPane okvir = new StackPane();
        
        int vrstaStola = 0;
        double x, y, sirina, visina;
        String naziv = "";
        
        RM_Button noviSto = new RM_Button();
        
        x = Double.parseDouble(stoMap.get("x"));
        x = x * RMaster.sirinaSaleNaEkranu / 1024;

        y = Double.parseDouble(stoMap.get("y"));
        y = y * RMaster.visinaSaleNaEkranu / 768;

        sirina = Double.parseDouble(stoMap.get("sirina"));
        sirina = sirina * RMaster.sirinaSaleNaEkranu / 1024;
        visina = Double.parseDouble(stoMap.get("visina"));
        visina = visina * RMaster.visinaSaleNaEkranu / 768;

        noviSto.setId(stoMap.get("id"));
        noviSto.setPodatak(stoMap.get("broj"));
        
        naziv = stoMap.get("broj");

        if (stoMap.get("naziv") != null) {
            naziv = stoMap.get("naziv");
        }

        noviSto.setText(naziv);
        
        vrstaStola = Integer.parseInt(stoMap.get("sto_VrstaStolaID"));

        noviSto.setBorder(Border.EMPTY);
        noviSto.setPrefSize(sirina, visina);
        noviSto.setMaxSize(sirina, visina);
        noviSto.setMinSize(sirina, visina);
        okvir.setPrefSize(sirina, visina);
        okvir.getChildren().add(noviSto);

        setOblikStola(noviSto, vrstaStola, sirina);

        AnchorPane.setLeftAnchor(okvir, x);
        AnchorPane.setTopAnchor(okvir, y);
        AnchorPane.setRightAnchor(okvir, RMaster.sirinaSaleNaEkranu - x - sirina);
        AnchorPane.setBottomAnchor(okvir, RMaster.visinaSaleNaEkranu - y - visina);
                
        return okvir;   
    }

    
    protected void dodajAkcijuZaSto(RM_Button sto)
    {
    }
    
    protected void ukloniAkcijuZaSto(RM_Button sto)
    {
        sto.setOnAction(null);
    }

    protected void setOblikStola(
            ButtonBase sto, 
            int vrstaStola, 
            double sirina)
    {
    }    

}
