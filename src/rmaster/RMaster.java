/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
        
        Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaBold.otf").toExternalForm(), 10);
        Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaBoldItalic.otf").toExternalForm(), 10);
        Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaLight.otf").toExternalForm(), 10);
        Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaLightItalic.otf").toExternalForm(), 10);
        Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaMedium.otf").toExternalForm(), 10);
        Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaMediumItalic.otf").toExternalForm(), 10);
        Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaRegular.otf").toExternalForm(), 10);
        Font.loadFont(RMaster.class.getResource("views/style/fonts/KlavikaRegularItalic.otf").toExternalForm(), 10);

        Parent root = FXMLLoader.load(getClass().getResource(this.getMainScreen()));
        root.getStylesheets().addAll(this.getClass().getResource("views/style/style.css").toExternalForm());

        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
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
    
   
}
