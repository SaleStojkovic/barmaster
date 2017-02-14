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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.concurrent.Task;
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
import rmaster.models.Artikal.Podgrupa;
import rmaster.models.Artikal.Grupa_Thread;
import rmaster.models.Artikal.Grupa;
import rmaster.models.Konobar;

/**
 *
 * @author Arbor
 */
public class RMaster extends Application {
    private Executor exec;
    
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
    
    public static List<Grupa> grupeArtikala;
    public static Podgrupa favouriteArtikli;
    
    
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

        exec = Executors.newCachedThreadPool(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t ;
        });
           
        Task<Void> startUpTask;
        startUpTask = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                
                long startTimeT = System.nanoTime();
                System.out.println("ucitajSveSale() - pocetak: " + startTimeT);
                
                ucitajSveSale();
                
                long estimatedTimeT = System.nanoTime() - startTimeT;
                System.out.println("ucitajSveSale() - kraj: " + System.nanoTime());
                System.out.println("ucitajSveSale(): " + estimatedTimeT);
                
                return null;
            }
        };
        exec.execute(startUpTask);

        startUpTask = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                
                long startTimeT = System.nanoTime();
                System.out.println("ucitajSveStolove() - pocetak: " + startTimeT);
                
                ucitajSveStolove();
                
                long estimatedTimeT = System.nanoTime() - startTimeT;
                System.out.println("ucitajSveStolove() - kraj: " + System.nanoTime());
                System.out.println("ucitajSveStolove(): " + estimatedTimeT);
                
                return null;
            }
        };
        exec.execute(startUpTask);

//        startUpTask = new Task<Void>() {
//            @Override
//            public Void call() throws Exception {
//                
                long startTimeF = System.nanoTime();
                System.out.println("FONTOVI - pocetak: " + startTimeF);
                
                Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaBold.otf").toExternalForm(), 10);
                Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaBoldItalic.otf").toExternalForm(), 10);
                Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaLight.otf").toExternalForm(), 10);
                Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaLightItalic.otf").toExternalForm(), 10);
                Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaMedium.otf").toExternalForm(), 10);
                Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaMediumItalic.otf").toExternalForm(), 10);
                Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaRegular.otf").toExternalForm(), 10);
                Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaRegularItalic.otf").toExternalForm(), 10);

                long estimatedTimeF = System.nanoTime() - startTimeF;
                System.out.println("FONTOVI - kraj: " + System.nanoTime());
                System.out.println("FONTOVI: " + estimatedTimeF);
                
//                return null;
//            }
//        };
//        exec.execute(startUpTask);
        

        ScreenController mainContainer = new ScreenController(); 

        ScreenMap scrMap = new ScreenMap();
        
        Class cls = scrMap.getClass();
        
        Field[] fields = cls.getDeclaredFields();
  

long startTime = System.nanoTime();
System.out.println("Ucitavanja - pocetak: " + startTime);        
        for (Field field : fields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())){
                String imeForme = field.get(null) + "";
                String fxmlPutanja = "views/" + imeForme + ".fxml";
                
                if (imeForme.equals("pocetniEkran")) {
        
                    long startTimeT = System.nanoTime();
                    System.out.println("Ucitavanja - " + imeForme + " - pocetak: " + startTimeT);
                    
                    mainContainer.loadScreen(imeForme, fxmlPutanja);
                    
                    long estimatedTimeT = System.nanoTime() - startTimeT;
                    System.out.println("Ucitavanja - " + imeForme + " - kraj: " + System.nanoTime());
                    System.out.println("Ucitavanja - " + imeForme + ": " + estimatedTimeT);
                }
                else {
                    startUpTask = new Task<Void>() {
                        @Override
                        public Void call() throws Exception {
                            
                            long startTimeT = System.nanoTime();
                            System.out.println("Ucitavanja - " + imeForme + " - pocetak: " + startTimeT);
                            
                            mainContainer.loadScreen(imeForme, fxmlPutanja);
                            
                            long estimatedTimeT = System.nanoTime() - startTimeT;
                            System.out.println("Ucitavanja - " + imeForme + " - kraj: " + System.nanoTime());
                            System.out.println("Ucitavanja - " + imeForme + ": " + estimatedTimeT);
                            
                            return null;
                        }
                    };

                    exec.execute(startUpTask);
                }

            }
        }

        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("Ucitavanja - kraj: " + System.nanoTime());
        System.out.println("Ucitavanja: " + estimatedTime);

        mainContainer.setScreen(ScreenMap.POCETNI_EKRAN, null);
        
        estimatedTime = System.nanoTime() - startTime - estimatedTime;
        System.out.println("Otvaranje - kraj - POCETNI_EKRAN: " + System.nanoTime());
        System.out.println("Otvaranje POCETNI_EKRAN: " + estimatedTime);
        


        long startTimeP = System.nanoTime();
        System.out.println("Prikaz - POCETNI_EKRAN - pocetak: " + startTimeP);

        Group root = new Group(); 
        root.getChildren().addAll(mainContainer); 
        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root); 
        scene.getStylesheets().addAll(this.getClass().getResource("views/style/style.min.css").toExternalForm()); 
        stage.setScene(scene); 
        stage.show(); 
        System.out.println("Prikaz - POCETNI_EKRAN - kraj: " + System.nanoTime());
        System.out.println("Prikaz - POCETNI_EKRAN: " + (System.nanoTime() - startTimeP));

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


    public void ucitajSveArtikle() 
    {
        grupeArtikala = new ArrayList<>();
        
        favouriteArtikli = new Podgrupa();
        
        Thread noviThread = new Thread() {
            @Override
            public void start() {
                 ucitajFavourite();
            }
        };
        
        noviThread.start();
        
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        query.setTableName(Grupa.TABLE_NAME);
        
        query.addCriteriaColumns(Grupa.PRIKAZ_NA_EKRAN, Grupa.GRUPA_ID);
        query.addCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL);
        query.addOperators(QueryBuilder.LOGIC_AND);
        query.addCriteriaValues("1", "0");
        query.addOrderByColumns(Grupa.PRIORITET);
        query.addOrderByCriterias(QueryBuilder.SORT_ASC);
        
        List<Map<String, String>> podrgupaList = dbBroker.runQuery(query);
       
        if (podrgupaList.isEmpty()){
            return;
        }
        
        for(Map<String, String> grupa : podrgupaList) {
            
            Grupa novaGrupa = new Grupa();
            
            novaGrupa.makeFromHashMap((HashMap)grupa);
           
            Runnable thread = new Grupa_Thread(novaGrupa);
            
            thread.run();                             
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
