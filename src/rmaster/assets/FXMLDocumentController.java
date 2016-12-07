/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets;
 
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import rmaster.RMaster;
import rmaster.models.Konobar;

/**
 *
 * @author Arbor
 */
public class FXMLDocumentController implements Initializable {
    
    public static String UPOZORENJE_NEISPRAVAN_UNOS_TITLE = "Neispravan unos!";
    public static String UPOZORENJE_BRISANJE_ZAPISA_TITLE = "Brisanje zapisa";
    public static String UPOZORENJE_BRISANJE_ZAPISA_CONTENT = "Da li ste sigurni da želite da obrišete ovaj zapis? " + 
            "Izabrani zapis biće trajno obrisan!";
    
    public DBBroker DBBroker;
    public RMaster RMaster;
    public TableHelper tableHelper;
    
    public Konobar ulogovaniKonobar;
    public Map<String, String> data = new HashMap<>();
     
    public FXMLDocumentController() {
        
        try {
            DBBroker = new DBBroker();
            tableHelper = new TableHelper();
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
   
        RMaster = new RMaster();
        ulogovaniKonobar = RMaster.getUlogovaniKonobar();
        


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
     */
    public void sacuvaj(
            String imeTabele,
            HashMap<String, String> elementi,
            Boolean zatvoriKonekciju
    )  {
        try {
        DBBroker.ubaci(
                imeTabele,
                elementi,
                zatvoriKonekciju
            );
        } catch (Exception e) {
            System.out.println(e);
        }
    }
     
    /**
     * 
     * @param imeTabele
     * @param uslovnaKolona
     * @param uslovnaVrednost
     * @param elementi
     * @param zatvoriKonekciju
     */
    public void izmeni(
            String imeTabele,
            String uslovnaKolona,
            String uslovnaVrednost,
            HashMap<String, String> elementi,
            Boolean zatvoriKonekciju
    )  {
        try {
        DBBroker.izmeni(
                imeTabele,
                uslovnaKolona,
                uslovnaVrednost,
                elementi,
                zatvoriKonekciju  
            );
        } catch (Exception e) {
            System.out.println(e);
        } 
    }
     
    /**
     * 
     * @param imeTabele
     * @param uslovnaKolona
     * @param uslovnaVrednost
     * @param zatvoriKonekciju
     */
    public void izbrisi(
            String imeTabele,
            String uslovnaKolona,
            String uslovnaVrednost,
            Boolean zatvoriKonekciju
    )  {
        try {
        DBBroker.izbrisi(
                imeTabele,
                uslovnaKolona,
                uslovnaVrednost,
                zatvoriKonekciju
            );
        } catch (Exception e) {
            System.out.println(e);
        }
    }
     
    
    public List vratiKoloneIzTabele(
            String imeTabele,
            String[] kolone,
            String[] uslovneKolone,
            String[] uslovneVrednosti 
    )   
    {
         List<Map<String, String>> listaRezultata = null;
         
         try{
            listaRezultata = DBBroker.vratiKoloneIzTabele(
                    imeTabele, 
                    kolone, 
                    uslovneKolone, 
                    uslovneVrednosti
            );
        
        } catch (Exception e) {
            System.out.println(e);
        }
         
        return listaRezultata;
    }        
    
    /**
     * 
     * @param imeTabele
     * @param uslovneKolone
     * @param uslovneVrednosti
     * @return
     */
    public List vratiSveIzTabeleUzUslov(
            String imeTabele,
            String[] uslovneKolone,
            String[] uslovneVrednosti
    ) {
        List<Map<String, String>> listaRezultata = null;
        try{
        listaRezultata = DBBroker.vratiSveIzTabeleUzUslov(
                imeTabele, 
                uslovneKolone, 
                uslovneVrednosti
        );
        } catch (Exception e) {
            System.out.println(e);
        }
         
        return listaRezultata;
    }
     
    /**
     * 
     * @param imeTabele
     * @return
     */
     public List vratiSveIzTabele(
            String imeTabele
    ) {
        List listaRezultata = DBBroker.vratiSveIzTabele(
                imeTabele
        );
         
        return listaRezultata;
    }
     
    /* BOSKO DODAO */
     public void initData(Map<String, String> data) {
         this.data = data;
     }
     
    /**
     * 
     * @param imeProcedure
     * @param imenaArgumenata
     * @param vrednostiArgumenata
     * @return 
     */
    public List runStoredProcedure(
            String imeProcedure,
            String[] imenaArgumenata,
            String[] vrednostiArgumenata
    ) {
        List<Map<String, String>> listaRezultata = null;
        try {
            listaRezultata = DBBroker.runStoredProcedure(
                    imeProcedure, 
                    imenaArgumenata, 
                    vrednostiArgumenata
            );
        } catch (Exception e) {
            System.out.println(e);
        }
        return listaRezultata;
    }
     /**
      * 
      * @param imeForme
      * @param ugasiPrethodnuFormu
      * @param prethodnaForma
      */
     public void prikaziFormu(
             Map<String, String> data,
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
            if (!data.isEmpty()) {
                controller.initData(data);
            } else {
                Map<String, String> newData = new HashMap<>();
                controller.initData(newData);
            }

            stage.show();
            
        } catch (Exception e){
            System.out.println("Greska pri otvaranju forme " + imeNoveForme + "! - " + e.toString());
        } finally {
            
            if (ugasiPrethodnuFormu) {
                prethodnaForma.getScene().getWindow().hide();
        }
     }
        
    }
     
     

    public void prikaziFormuModalno(
            Map<String, String> data,
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
            //Map<String, String> newData = new HashMap<>();
            controller.initData(data);

            stage.showAndWait();
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
    
    
   public Timeline prikaziCasovnik(
           Label casovnik
   ) {
       Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0),
              new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent actionEvent) {
                  Calendar time = Calendar.getInstance();
                  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                  casovnik.setText(simpleDateFormat.format(time.getTime()));
                }
              }
            ),
            new KeyFrame(Duration.seconds(1))
          );
          timeline.setCycleCount(Animation.INDEFINITE);
          return timeline;
   }
   
    public void odjava(ActionEvent event)
    {
            //TODO da li treba upisati kada se konobar izlogovao
        
            Map<String, String> newData = new HashMap<>();
            
            //sledeca stranica 
            prikaziFormu(
                    newData,
                    ScreenMap.POCETNI_EKRAN, 
                    true, 
                    (Node)event.getSource()
            );
    }
    
    /**
     * 
     * @param titleText
     * @param headerText
     * @param contentText
     * @param buttons
     * @return 
     */
    public Alert showWarning(
            String headerText,
            String contentText,
            ButtonType... buttons
    ) {
        Alert alert = new Alert(
                Alert.AlertType.WARNING,
                contentText, 
                buttons
        );        
                
        alert.getDialogPane().getStylesheets().
                addAll(this.getClass().getResource("style/style.css").toExternalForm());

        alert.getDialogPane().getStyleClass().add("myDialog");
        alert.initStyle(StageStyle.UNDECORATED);

        alert.setHeaderText(headerText);
        alert.setTitle("Upozorenje!");
        
        return alert;
    }
}