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
import rmaster.assets.QueryBuilder.TableJoin;
import rmaster.assets.QueryBuilder.TableJoinTypes;
import rmaster.assets.ScreenMap;
import rmaster.models.Artikal.Artikal_Podgrupa;
import rmaster.models.Artikal.Grupa;
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
    
    public static Map<String,BackgroundImage> saleSlike = new HashMap();
    public static Map<String,String> saleNaziv = new HashMap();
    public static double visinaSaleNaEkranu = 768 - 150;
    public static double sirinaSaleNaEkranu = 1024;
    
    public static List<Map<String, String>> saleOmoguceneKonobaru = new ArrayList<>();
    public static List<Map<String, String>> sveSale = new ArrayList<>();
    public static List<Map<String, String>> sviStolovi = new ArrayList<>();
    
    public static List<Grupa> grupeArtikala = new ArrayList<>();
    public static Artikal_Podgrupa favouriteArtikli = new Artikal_Podgrupa();
    
    
    public static boolean firstLogin = true;
               
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
           
        ucitajSveSale();
        
        ucitajSveStolove();
        
        ucitajSveGrupe(); 
        
        ucitajFavourite();
        
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
        scene.getStylesheets().addAll(this.getClass().getResource("views/style/style.min.css").toExternalForm()); 
        stage.setScene(scene); 
        stage.show(); 

        dbBroker.prekiniVezuSaBazom(dbBroker.poveziSaBazom());
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
 
    public void ucitajSveSale(){
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);

        query.setTableName("grafik_konobar");
        
        List<Map<String, String>> sale = dbBroker.runQuery(query);
        
        QueryBuilder query2 = new QueryBuilder(QueryBuilder.SELECT);
        
        query2.setTableName("grafiksale");
        
        if (!sale.isEmpty()) {
            String saleString = query2.makeStringForInCriteriaFromListByParam(sale, "grafik_id");
            query2.addCriteriaColumns("id");
            query2.addCriteria(QueryBuilder.IS_NOT_IN);
            query2.addCriteriaValues(saleString);
        }

        sveSale = dbBroker.runQuery(query2);
    }
    
    public void ucitajSveStolove()
    {
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
                
        query.addTableJoins(
                new TableJoin(
                        "stoprikaz",
                        "stonaziv",
                        "broj",
                        "broj",
                        TableJoinTypes.LEFT_JOIN),
                new TableJoin(
                        "stoprikaz",
                        "sto",
                        "broj",
                        "broj",
                        TableJoinTypes.LEFT_JOIN),
                new TableJoin(
                        "stoprikaz",
                        "rezervacija",
                        "broj",
                        "brStola",
                        TableJoinTypes.LEFT_JOIN) 
        );
        
        query.setSelectColumns(
                "stoprikaz.*", 
                "stonaziv.naziv",
                "sto.KONOBAR_ID",
                "sto.blokiran",
                "rezervacija.datum",
                "rezervacija.vreme",
                "rezervacija.brOsoba"
        );
        
        sviStolovi = dbBroker.runQuery(query);
    }

    public List<Map<String, String>> getStoloveBySalaId(String salaId)
    {
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
                
        query.addTableJoins(
                new TableJoin(
                        "stoprikaz",
                        "stonaziv",
                        "broj",
                        "broj",
                        TableJoinTypes.LEFT_JOIN),
                new TableJoin(
                        "stoprikaz",
                        "sto",
                        "broj",
                        "broj",
                        TableJoinTypes.LEFT_JOIN),
                new TableJoin(
                        "stoprikaz",
                        "rezervacija",
                        "broj",
                        "brStola",
                        TableJoinTypes.LEFT_JOIN) 
        );
        
        query.setSelectColumns(
                "stoprikaz.*", 
                "stonaziv.naziv",
                "sto.KONOBAR_ID",
                "sto.blokiran",
                "rezervacija.datum",
                "rezervacija.vreme",
                "rezervacija.brOsoba"
        );

        query.addCriteriaColumns("stoprikaz.GRAFIK_ID");
        query.addCriteria(QueryBuilder.IS_EQUAL);
        query.addCriteriaValues(salaId);
        
        List<Map<String, String>> listStolovi = dbBroker.runQuery(query);

        
        return listStolovi;
    }


    private void ucitajSveGrupe() 
    {
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        query.setTableName(Grupa.TABLE_NAME);
        
        query.addCriteriaColumns(Grupa.PRIKAZ_NA_EKRAN, Grupa.GRUPA_ID);
        query.addCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL);
        query.addOperators(QueryBuilder.LOGIC_AND);
        query.addCriteriaValues("1", "0");
        query.addOrderByColumns(Grupa.PRIORITET);
        query.addOrderByCriterias(QueryBuilder.SORT_ASC);
        //query.setOrderBy(Grupa.PRIORITET, QueryBuilder.SORT_ASC);
        
        List<Map<String, String>> podrgupaList = dbBroker.runQuery(query);
       
        if (podrgupaList.isEmpty()){
            return;
        }
        
        for(Map<String, String> grupa : podrgupaList) {
            Grupa novaGrupa = new Grupa();
            
            novaGrupa.makeFromHashMap((HashMap)grupa);
            
            novaGrupa.setAllChildren();
            
            grupeArtikala.add(novaGrupa);
        }
    }
    
    
    private void ucitajFavourite()
    {
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        
        query.addTableJoins(
                new TableJoin(
                        "artikal", 
                        "artikal_stampac", 
                        "id", 
                        "artikalID", 
                        TableJoinTypes.INNER_JOIN
                )
                
        );
        
        query.setSelectColumns(
                "artikal.id",
                "artikal.barCode",
                "artikal.cena",
                "artikal.dozvoljenPopust",
                "artikal.jedinicaMere",
                "artikal.name",
                "artikal.prioritet",
                "artikal.skrNaziv",
                "artikal.slika",
                "artikal_stampac.stampacID"
        );
        
        query.addCriteriaColumns("artikal.blokiran", "artikal.favorite");
        query.addCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL);
        query.addOperators(QueryBuilder.LOGIC_AND);
        query.addCriteriaValues(QueryBuilder.BIT_0, "1");
        query.addOrderByColumns("artikal.prioritet", "artikal.name");
        query.addOrderByCriterias(QueryBuilder.SORT_ASC, QueryBuilder.SORT_ASC);
        //query.setOrderBy("artikal.prioritet, artikal.name", QueryBuilder.SORT_ASC);
        
        List<HashMap<String, String>> listaArtikala = dbBroker.runQuery(query);
        
        for(HashMap<String, String> artikalMap : listaArtikala) {
            String rezultat = favouriteArtikli.getType(artikalMap.get("id"));
            favouriteArtikli.addChild(rezultat, artikalMap);
        }
    }
    
}
