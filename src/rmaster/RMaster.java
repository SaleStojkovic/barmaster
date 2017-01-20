/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import rmaster.assets.DBBroker;
import rmaster.assets.QueryBuilder.QueryBuilder;
import rmaster.assets.ScreenMap;
import rmaster.assets.items.Artikal;
import rmaster.assets.items.GrupaArtikalaFront;
import rmaster.models.Konobar;

/**
 *
 * @author Arbor
 */
public class RMaster extends Application {
    
    public String resource;
    public String mainScreen = "views/pocetniEkran.fxml";
    public static Konobar ulogovaniKonobar;
    public static long trenutnaSalaID;
    public static String trenutnaSalaSlika;
    public static String izabraniStoID;
    public static int izabraniStoBroj;
    public static String izabraniStoNaziv;
    public static Map<String,BackgroundImage> saleSlike = new HashMap();
    public static Map<String,String> saleNaziv = new HashMap();
    public static double visinaSaleNaEkranu = 768 - 150;
    public static double sirinaSaleNaEkranu = 1024;
    
    public static List<Map<String, String>> listaGrupaArtikalaFront;
    public static List<Map<String, String>> listaArtikalaFavorite;
               
    DBBroker dbBroker = new DBBroker();
    
    public Konobar getUlogovaniKonobar() {
        return ulogovaniKonobar;
    }
    
    public void promeniKonobara(Map<String, String> noviKonobar) {
        ulogovaniKonobar = new Konobar(noviKonobar);
    }
    
    public void promeniSalu(long novaSala) {
        trenutnaSalaID = novaSala;
    }
    
    

    @Override
    public void start(Stage stage) throws Exception {
        
        getGrupaArtikalaFront();
        
        getListaArtikalaFavorite();
        
        Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaBold.otf").toExternalForm(), 10);
        Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaBoldItalic.otf").toExternalForm(), 10);
        Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaLight.otf").toExternalForm(), 10);
        Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaLightItalic.otf").toExternalForm(), 10);
        Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaMedium.otf").toExternalForm(), 10);
        Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaMediumItalic.otf").toExternalForm(), 10);
        Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaRegular.otf").toExternalForm(), 10);
        Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaRegularItalic.otf").toExternalForm(), 10);
        

       ScreenController mainContainer = new ScreenController(); 

        ScreenMap scrMap = new ScreenMap();
        
        Class cls = scrMap.getClass();
        
        Field[] fields = cls.getDeclaredFields();
            

        for (Field field : fields) {

            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())){

                String imeForme = field.get(null) + "";
                String fxmlPutanja = "views/" + imeForme + ".fxml";
                mainContainer.loadScreen(imeForme, fxmlPutanja);
                
            }
        }
        
        mainContainer.setScreen(ScreenMap.POCETNI_EKRAN, null);
        
        Group root = new Group(); 
       root.getChildren().addAll(mainContainer); 
        stage.initStyle(StageStyle.UNDECORATED);
       Scene scene = new Scene(root); 
        scene.getStylesheets().addAll(this.getClass().getResource("views/style/style.css").toExternalForm()); 
       stage.setScene(scene); 
       stage.show(); 
            
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
    
    public String getMainScreen() {
        return this.mainScreen;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
      public void getGrupaArtikalaFront() {
        
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        query.setTableName(GrupaArtikalaFront.TABLE_NAME);
        //query.setSelectColumns(GrupaArtikalaFront.PRIMARY_KEY, GrupaArtikalaFront.NAZIV, GrupaArtikalaFront.PRIORITET);
        
        //query.addCriteriaColumns(StalniGost.SIFRA, StalniGost.BLOKIRAN, StalniGost.GRUPA_ID);
        //query.addCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL);
        //query.addOperators(QueryBuilder.LOGIC_AND, QueryBuilder.LOGIC_AND);
        //query.addCriteriaValues("", QueryBuilder.TRUE, grupaId);
                
        //if (text != null) {
        //    query.addCriteriaColumns(StalniGost.NAZIV);
        //    query.addCriteria(QueryBuilder.IS_LIKE);
        //    query.addOperators(QueryBuilder.LOGIC_AND);
        //    query.addCriteriaValues(text + "%");
        //}
        
        query.setOrderBy(GrupaArtikalaFront.PRIORITET, QueryBuilder.SORT_ASC);
        //query.setLimit(20);
        //query.setOffset(offset);
        
        List<Map<String, String>> listaRezultata = dbBroker.runQuery(query);
          
        List<Map<String, String>> listaGrupaArtikalaFront = new ArrayList<>();
        
        for (Map mapGrupaArtikalaFront : listaRezultata) {
            GrupaArtikalaFront noviGrupaArtikalaFront = new GrupaArtikalaFront();
            noviGrupaArtikalaFront.makeFromHashMap((HashMap)mapGrupaArtikalaFront);
            
            listaGrupaArtikalaFront.add(noviGrupaArtikalaFront.makeMapForTableOutput());
        }
              
        this.listaGrupaArtikalaFront = listaGrupaArtikalaFront;
    }

    public void getListaArtikalaFavorite() {
        String imeStoreProcedure = "getArtikliFavorite";
        String[] imenaArgumenata = new String[]{"brojPrvogZapisa", "brojZapisa"};
        String[] vrednostiArgumenata = new String[]{"" + (1 - 1),"" + 32};
        List<Map<String,String>> listaRezultata;
        listaRezultata = dbBroker.runStoredProcedure(imeStoreProcedure, imenaArgumenata, vrednostiArgumenata);
            
//        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
//        query.setTableName(Artikal.TABLE_NAME);
//        
//        query.setOrderBy(Artikal.PRIORITET, QueryBuilder.SORT_ASC);
//        
//        List<Map<String, String>> listaRezultata = runQuery(query);
//          
        List<Map<String, String>> listaArtikalFront = new ArrayList<>();
        
        for (Map mapArtikalFront : listaRezultata) {
            Artikal noviArtikalFront = new Artikal();
            noviArtikalFront.makeFromHashMap((HashMap)mapArtikalFront);
            
            listaArtikalFront.add(noviArtikalFront.makeMapForTableOutput());
        }
              
        this.listaArtikalaFavorite = listaArtikalFront;
    }
}
