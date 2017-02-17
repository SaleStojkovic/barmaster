/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import rmaster.assets.DBBroker;
import static rmaster.assets.DBBroker.runStoredProcedure;
import rmaster.assets.QueryBuilder.QueryBuilder;
import rmaster.assets.QueryBuilder.TableJoin;
import rmaster.assets.QueryBuilder.TableJoinTypes;
import rmaster.assets.RM_TableView.RM_TableView;

/**
 *
 * @author Arbor
 */
public class RacuniZaNaplatuController extends Dialog {
    
    private RM_TableView tabelaSaRacunimaZaNaplatu = new RM_TableView();

    private DBBroker dbBroker = new DBBroker();
    
    private List listRacuni = null;

    
    public RacuniZaNaplatuController()
    {
        this.initStyle(StageStyle.UNDECORATED);
        
        this.getDialogPane().getStyleClass().add("myDialog");

        // Set the button types.        
        ButtonType  odustaniButtonType = new ButtonType("X", ButtonBar.ButtonData.OK_DONE);
                
        this.getDialogPane().getButtonTypes().addAll(odustaniButtonType);
        
        this.getDialogPane().getStylesheets().
                addAll(this.getClass().getResource("style/style.min.css").toExternalForm());
        
        listRacuni = this.ucitajRacuneZaNaplatu();
        
        tabelaSaRacunimaZaNaplatu.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        tabelaSaRacunimaZaNaplatu.setPodaci(listRacuni);
        
        int brojRedova = listRacuni.size();
        
        tabelaSaRacunimaZaNaplatu.setFixedCellSize(30);
                                
        tabelaSaRacunimaZaNaplatu.setPrefHeight(brojRedova * tabelaSaRacunimaZaNaplatu.getFixedCellSize());
                
        tabelaSaRacunimaZaNaplatu.setPrefSize(600, 450);
        
        VBox content = new VBox();
        
        content.setSpacing(10);
        
        Button stampaj = new Button("Štampaj");
        
        stampaj.setOnAction(new EventHandler<ActionEvent>() {               
                                    @Override public void handle(ActionEvent event) {
                                        
                                        stampajRacun();
                                    }
                                });
        
        stampaj.setPrefSize(200, 50);

        content.getChildren().addAll(tabelaSaRacunimaZaNaplatu, stampaj);
        
        this.getDialogPane().setContent(content);
    }
    
    private List<Map<String, String>> ucitajRacuneZaNaplatu()
    {
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        
        query.addTableJoins(
                new TableJoin("racun", "stavkaracuna", "id", "RACUN_ID", TableJoinTypes.LEFT_JOIN)
        );
        
        query.setSelectColumns(
                "racun.id",
                "racun.brojStola AS 'Sto'",
                "SUM(stavkaracuna.cena*((100.0-stavkaracuna.procenatPopusta)/100.0)) AS 'UKUPNO'",
                "TIME(racun.datum)",
                "racun.brojFiskalnogIsecka"
        );
        
        query.addCriteriaColumns("zatvoren", "racun.konobar_id","TIMESTAMPDIFF(HOUR, racun.datum, CURTIME())");
        query.addCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL, QueryBuilder.IS_LESS_THAN);
        query.addOperators(QueryBuilder.LOGIC_AND, QueryBuilder.LOGIC_AND, QueryBuilder.LOGIC_AND);
        query.addCriteriaValues("1", rmaster.RMaster.ulogovaniKonobar.konobarID + "", "1");
        query.GROUP_BY = "racun.id";
        
        DBBroker dbBroker = new DBBroker();
        
        List<Map<String, String>> listaRezultata = dbBroker.runQuery(query);
        
        return listaRezultata;
    }
    
    private void stampajRacun()
    {
         try {
            // TODO: GetSelected Racun (id, brojFiskalnog)
            String racunID = "";
            String brojFiskalnogIsecka = "";
            if (!this.tabelaSaRacunimaZaNaplatu.getSelectionModel().isEmpty()) {
                racunID = this.tabelaSaRacunimaZaNaplatu.getSelectionModel().getSelectedItem().get("id");
                brojFiskalnogIsecka = this.tabelaSaRacunimaZaNaplatu.getSelectionModel().getSelectedItem().get("brojFiskalnogIsecka");
            
                if (brojFiskalnogIsecka == null || brojFiskalnogIsecka.equals("")){
                    NumerickaTastaturaController tastatura = new NumerickaTastaturaController(
                            "Unesite broj fiskalnog isečka",
                            "Broj fiskalnog isečka",
                            false, 
                            null
                    );
                    Optional<String> result = tastatura.showAndWait();

                    if (result.isPresent()){
                        // TODO: Upisati u bazu broj FISKALNOG ISECKA
                        // OVDE DORADITI DA SE POKUPI PORUDZBINA KOJA SE STAMPA
                        // Porudzbina porudzbina = null;
                        // Stampa.getInstance().stampajGotovinskiRacun(porudzbina);
                                try {
                                    String[] imenaArgumenata = {"racunID","brojIsecka"};
                                    String[] vrednostiArgumenata = {racunID,result.get()};

                                    listRacuni = dbBroker.runStoredProcedure("setRacun_BrojFiskalnogIsecka",
                                            imenaArgumenata,
                                            vrednostiArgumenata);
                                } catch (Exception e) {
                                    System.out.println("Greska u pozivu SP get_racuniKonobaraKojiNisuZatvoreni! - " + e.toString());
                                }
                       this.close();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Greška!");
                    alert.setHeaderText("Greška pri štampanju gotovinskog računa");
                    alert.setContentText("Za račun je već odštampan gotovinski račun!");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Greška!");
                alert.setHeaderText("Greška pri štampanju gotovinskog računa");
                alert.setContentText("Račun nije izabran! Izaberite račun za koji želite da odštampate gotovinski račun.");
                alert.showAndWait();
            }
        } catch (Exception e){
            System.out.println("Greska pri otvaranju modalne forme TASTATURA! - " + e.toString());
        }
    }
}
