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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BackgroundImage;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import rmaster.assets.DBBroker;
import rmaster.assets.QueryBuilder.QueryBuilder;
import rmaster.assets.QueryBuilder.TableJoin;
import rmaster.assets.QueryBuilder.TableJoinTypes;
import rmaster.assets.ScreenMap;
import rmaster.assets.Settings;
import rmaster.models.Artikal.Podgrupa;
import rmaster.models.Artikal.GrupaThread;
import rmaster.models.Artikal.Grupa;
import rmaster.models.Konobar;
import rmaster.models.SettingsBaza;

/**
 *
 * @author Arbor
 */
public class RMaster extends Application {
    private Executor exec;
    
    public static Image logo;
    
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
    public static List<Map<String, String>> saleZabranjeneKonobaru = new ArrayList<>();
    public static List<Map<String, String>> sveSale = new ArrayList<>();
    public static List<Map<String, String>> sviStolovi = new ArrayList<>();
    
    public static Group root = new Group(); 
    public static ScreenController mainContainer = new ScreenController(); 

    public static List<Grupa> grupeArtikala;
    public static Podgrupa favouriteArtikli;
    
    public final static SettingsBaza settingsBaza = new SettingsBaza();
    
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

        System.setProperty("javax.xml.transform.TransformerFactory",
                "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
        
        settingsBaza.getAllValues();
        
        logo = new Image(
                            Settings.getInstance().getValueString("sale.slike.putanja") + "logo.png",
                            256,
                            51,
                            false,
                            true
                    );        
                        
        mainContainer.loadScreen("pocetniEkran", "views/pocetniEkran.fxml");
                            
        mainContainer.setScreen(ScreenMap.POCETNI_EKRAN, null);
        
        root.getChildren().addAll(mainContainer); 
        
        stage.initStyle(StageStyle.UNDECORATED);
        
        Scene scene = new Scene(root); 
        
        scene.getStylesheets().addAll(this.getClass().getResource("views/style/style.min.css").toExternalForm()); 
        
        stage.setScene(scene); 
        
        stage.show(); 

        new Thread() {
            
            @Override
            public void start()
            {
                try {
                    initializeForms();
                    
                } catch(Exception e) {
                    
                    e.printStackTrace();
                
                }

            }
            
        }.start();
        
        dbBroker.prekiniVezuSaBazom();
    }

    private void initializeForms() throws Exception
    {
        Task<Void> startUpTask;

        ucitajSveSale();
        ucitajSveStolove();

        ScreenMap scrMap = new ScreenMap();
        
        Class cls = scrMap.getClass();
        
        Field[] fields = cls.getDeclaredFields();
        
        exec = Executors.newCachedThreadPool(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t ;
        });
        
        for (Field field : fields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())){
                String imeForme = field.get(null) + "";
                String fxmlPutanja = "views/" + imeForme + ".fxml";
                
                if (imeForme.equals("pocetniEkran")) {
                    continue;
                }
                    
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

    public void ucitajSveSaleZabranjeneKonobaru(){
        QueryBuilder queryZabranjeneSale = new QueryBuilder(QueryBuilder.SELECT);

        queryZabranjeneSale.setTableName("grafik_konobar");
        queryZabranjeneSale.addCriteriaColumns("konobar_id");
        queryZabranjeneSale.addCriteria(QueryBuilder.IS_EQUAL);
        queryZabranjeneSale.addCriteriaValues("" + RMaster.ulogovaniKonobar.konobarID);
        
        saleZabranjeneKonobaru = dbBroker.runQuery(queryZabranjeneSale);        
        
        for (Map<String, String> map : sveSale) {
            saleOmoguceneKonobaru.add(map);
        }
        for (Map<String, String> mapZabrana : saleZabranjeneKonobaru) {
            for (Map<String, String> mapOmogucene : saleOmoguceneKonobaru) {
                if(mapOmogucene.get("id").equals(mapZabrana.get("grafik_id"))) {
                    saleOmoguceneKonobaru.remove(mapOmogucene);
                    break;
                }
            }
        }
    }
    
    public void ucitajSveSale(){
        QueryBuilder querySveSale = new QueryBuilder(QueryBuilder.SELECT);
        
        querySveSale.setTableName("grafiksale");
        
        sveSale = dbBroker.runQuery(querySveSale);
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
                "rezervacija.datum AS RezervacijaDatum",
                "rezervacija.vreme AS RezervacijaVreme",
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
           
            Runnable thread = new GrupaThread(novaGrupa);
            
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
        
        List<HashMap<String, String>> listaArtikala = dbBroker.runQuery(query);
        
        for(HashMap<String, String> artikalMap : listaArtikala) {
            String rezultat = favouriteArtikli.getType(artikalMap.get("id"));
            favouriteArtikli.addChild(rezultat, artikalMap);
        }
    }
   
}
