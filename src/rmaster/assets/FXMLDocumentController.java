/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets;
 
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import rmaster.RMaster;
import rmaster.models.Konobar;
 
 
/**
 *
 * @author Arbor
 */
public class FXMLDocumentController implements Initializable {
     
    public DBBroker DBBroker;
    public RMaster RMaster;
     
    public FXMLDocumentController() {
        
        try {
            DBBroker = new DBBroker();
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
   
        RMaster = new RMaster();
    } 
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
     
    /**
     * 
     * @param konobar 
     */ 
    public void setUlogovaniKonobar(Map<String, String> konobar) {
        RMaster.ulogovaniKonobar = new Konobar(konobar);
    }
    
    public long getUlogovaniKonobarID() {
        return RMaster.ulogovaniKonobar.konobarID;
    }
    
    public void setUlogovaniKonobarID(long konobarID) {
        RMaster.ulogovaniKonobar.konobarID = konobarID;
    }
    
    public String getUlogovaniKonobarIme() {
        return RMaster.ulogovaniKonobar.imeKonobara;
    }
    
    public void setUlogovaniKonobarIme(String imeKonobara) {
        RMaster.ulogovaniKonobar.imeKonobara = imeKonobara;
    }
    
    public String getUlogovaniKonobarSkrIme() {
        return RMaster.ulogovaniKonobar.skrImeKonobara;
    }
    
        
    public void setUlogovaniKonobarSkrIme(String skrImeKonobara) {
        RMaster.ulogovaniKonobar.skrImeKonobara = skrImeKonobara;
    }
    
    public String getUlogovaniKonobarPin() {
        return RMaster.ulogovaniKonobar.konobarPin;
    }
    
    public void setUlogovaniKonobarPin(String konobarPin) {
        RMaster.ulogovaniKonobar.konobarPin = konobarPin;
    }
    
    /**
     * 
     * @param imeTabele
     * @param elementi
     * @param zatvoriKonekciju
     * @throws Exception 
     */
    public void sacuvaj(
            String imeTabele,
            HashMap<String, String> elementi,
            Boolean zatvoriKonekciju
    ) throws Exception {
        DBBroker.ubaci(
                imeTabele,
                elementi,
                zatvoriKonekciju
            );
    }
     
    /**
     * 
     * @param imeTabele
     * @param uslovnaKolona
     * @param uslovnaVrednost
     * @param elementi
     * @param zatvoriKonekciju
     * @throws Exception 
     */
    public void izmeni(
            String imeTabele,
            String uslovnaKolona,
            String uslovnaVrednost,
            HashMap<String, String> elementi,
            Boolean zatvoriKonekciju
    ) throws Exception {
        DBBroker.izmeni(
                imeTabele,
                uslovnaKolona,
                uslovnaVrednost,
                elementi,
                zatvoriKonekciju  
            );
    }
     
    /**
     * 
     * @param imeTabele
     * @param uslovnaKolona
     * @param uslovnaVrednost
     * @param zatvoriKonekciju
     * @throws Exception 
     */
    public void izbrisi(
            String imeTabele,
            String uslovnaKolona,
            String uslovnaVrednost,
            Boolean zatvoriKonekciju
    ) throws Exception {
        DBBroker.izbrisi(
                imeTabele,
                uslovnaKolona,
                uslovnaVrednost,
                zatvoriKonekciju
            );
    }
     
    /**
     * 
     * @param imeTabele
     * @param uslovneKolone
     * @param uslovneVrednosti
     * @return
     * @throws Exception 
     */
    public List vratiKoloneIzTabele(
            String imeTabele,
            String[] uslovneKolone,
            String[] uslovneVrednosti
    ) throws Exception {
        List listaRezultata = DBBroker.vratiKoloneIzTabele(
                imeTabele, 
                uslovneKolone, 
                uslovneVrednosti
        );
         
        return listaRezultata;
    }
     
    /**
     * 
     * @param imeTabele
     * @return
     * @throws Exception 
     */
     public List vratiSveIzTabele(
            String imeTabele
    ) throws Exception {
        List listaRezultata = DBBroker.vratiSveIzTabele(
                imeTabele
        );
         
        return listaRezultata;
    }
     
    /* BOSKO DODAO */
     public void initData() {
     }
     
     /**
      * 
      * @param imeForme
      * @param ugasiPrethodnuFormu
      * @param prethodnaForma
      */
     public void prikaziFormu(
             String imeForme, 
             boolean ugasiPrethodnuFormu, 
             Node prethodnaForma) 
     {
        Stage stage;
        String imeNoveForme = imeForme + ".fxml";
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(imeNoveForme));
        
            stage = new Stage(StageStyle.UNDECORATED);

            Pane pane = loader.load();
            Scene novaScena = new Scene(pane);
            novaScena.getStylesheets().addAll(this.getClass().getResource("style/style.css").toExternalForm());

            stage.setScene(novaScena);

            FXMLDocumentController controller = loader.getController();
            controller.initData();

            stage.show();
            if (ugasiPrethodnuFormu) {
                prethodnaForma.getScene().getWindow().hide();
            }
            
        } catch (Exception e){
            System.out.println("Greska pri otvaranju forme " + imeNoveForme + "! - " + e.toString());
        }
    }
     
     
    /**
     * @param imeForme
     * @param prethodnaForma 
     */ 
    public void prikaziFormuModalno(
            String imeForme, 
            Node prethodnaForma
    ) {
        Stage stage;
        String imeNoveForme = imeForme + ".fxml";
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(imeNoveForme));

            stage = new Stage(StageStyle.UNDECORATED);
            Pane pane = loader.load();
            stage.setScene(new Scene(pane));

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(prethodnaForma.getScene().getWindow());

            FXMLDocumentController controller = loader.getController();
            controller.initData();

            stage.show();
        } catch (Exception e){
            System.out.println("Greska pri otvaranju modalne forme " + imeNoveForme + "! - " + e.toString());
        }
    } 
    
    /**
     * 
     * @param <T>
     * @param parent
     * @param type
     * @return 
     */
    public <T>List<T> getKontroleOdredjenogTipa(Pane parent, Class<T> type) {
        List<T> elements = new ArrayList<>();
        for (Node node : parent.getChildren()) {
            
            if (type.isAssignableFrom(node.getClass())) {
                elements.add((T) node);
            }
        }
        return Collections.unmodifiableList(elements);
    }
    
    
     
    public TableView<Map<String,String>> popuniTabelu(
            TableView<Map<String,String>> tabela,
            List listaPodataka
    ) {
        ObservableList<Map<String, String>> tableList = FXCollections.observableArrayList(listaPodataka);            
        
        if (!listaPodataka.isEmpty()) {
            
            HashMap row = (HashMap)listaPodataka.get(0);
            
            Set<String> keys = row.keySet();
            
            String[] kolone = keys.toArray(new String[keys.size()]);
            
            for (int i = 0; i < keys.size(); i++) {
                 
                TableColumn<Map<String, String>, String> column = new TableColumn<>(kolone[i]);
                final String colIndex = kolone[i];
                column.setCellValueFactory(data -> {
                    Map<String, String> rowValues = data.getValue();
                    String cellValue;
                    cellValue = rowValues.get(colIndex);
                    return new ReadOnlyStringWrapper(cellValue);
            });
                
               tabela.getColumns().add(column);
            }
            
            for (int j = 0; j < listaPodataka.size(); j++) {
                tabela.getItems().add(tableList.get(j));
            }
            
        }
        return tabela;
    }  

   
}