/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.views;

import java.util.List;
import java.util.Map;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.stage.StageStyle;
import rmaster.assets.DBBroker;
import rmaster.assets.QueryBuilder.QueryBuilder;
import rmaster.assets.QueryBuilder.TableJoin;
import rmaster.assets.QueryBuilder.TableJoinTypes;
import rmaster.assets.RM_TableView.RM_TableView;

/**
 *
 * @author Arbor
 */
public class RacuniZaNaplatuController_V2 extends Dialog {
    
    private RM_TableView tabelaSaRacunimaZaNaplatu = new RM_TableView();

    
    public RacuniZaNaplatuController_V2()
    {
        this.initStyle(StageStyle.UNDECORATED);

        // Set the button types.
        ButtonType potvrdiButtonType = new ButtonType("✓", ButtonBar.ButtonData.OK_DONE);
        ButtonType odustaniButtonType = new ButtonType("X", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(potvrdiButtonType, odustaniButtonType);
        
        this.getDialogPane().getStylesheets().
                addAll(this.getClass().getResource("style/style.min.css").toExternalForm());
        
        List<Map<String, String>> racuniZaNaplatu = this.ucitajRacuneZaNaplatu();
        
        tabelaSaRacunimaZaNaplatu.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        tabelaSaRacunimaZaNaplatu.setPodaci(racuniZaNaplatu);
        
        int brojRedova = racuniZaNaplatu.size();
        tabelaSaRacunimaZaNaplatu.setFixedCellSize(30);
                                
        tabelaSaRacunimaZaNaplatu.setPrefHeight(brojRedova * tabelaSaRacunimaZaNaplatu.getFixedCellSize());
                
        tabelaSaRacunimaZaNaplatu.setPrefSize(600, 450);
        
        this.getDialogPane().setContent(tabelaSaRacunimaZaNaplatu);
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
}
