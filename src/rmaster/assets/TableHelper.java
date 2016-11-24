/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import rmaster.models.StavkaTure;


public class TableHelper {
    
    /**
     * 
     * @param novaTabela
     * @param lista
     * @param sirineKolone
     * @return 
     */
    public  TableView<Map<String, String>> formatirajTabelu(
            TableView<Map<String, String>> novaTabela,
            List<Map<String, String>> lista,
            int[] sirineKolone
        ) 
    {
        novaTabela = popuniTabelu(novaTabela, lista);

        List<TableColumn<Map<String,String>, ?>> listaKolona = novaTabela.getColumns();
        
        int brojac = 0;
        
        for(TableColumn<Map<String, String>, ?> novaKolona : listaKolona) {
            
            int sirina = sirineKolone[brojac];
            
            novaKolona.setPrefWidth(sirineKolone[brojac]);

            if (sirina == 0) {
                novaKolona.setVisible(false);
            }
            
            brojac++;
            
        }

        novaTabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        return novaTabela;
    }
    
    /**
     * 
     * @param tabela
     * @param listaPodataka
     * @return 
     */
    public TableView<Map<String,String>> popuniTabelu(
            TableView<Map<String,String>> tabela,
            List<Map<String, String>> listaPodataka
    ) {
        ObservableList<Map<String, String>> tableList = FXCollections.observableArrayList(listaPodataka);            
        
        if (!listaPodataka.isEmpty()) {
            
            Map<String, String> row = listaPodataka.get(0);
            
            Set<String> keys = row.keySet();
            
            String[] kolone = keys.toArray(new String[keys.size()]);
            
            for (int i = 0; i < keys.size(); i++) {
                 
                TableColumn<Map<String, String>, String> column = new TableColumn<>();
                
                column.setResizable(false);
                
                final String colIndex = kolone[i];
                column.setCellValueFactory(data -> {
                    Map<String, String> rowValues = data.getValue();
                    String cellValue;
                    cellValue = rowValues.get(colIndex);
                    
                    if(cellValue == null) {
                        return new ReadOnlyStringWrapper("-/-");
                    }
                    
                    return new ReadOnlyStringWrapper(cellValue);
            });
                
               tabela.getColumns().add(column);
               
            }
            
            for (int j = 0; j < listaPodataka.size(); j++) {
                tabela.getItems().add(tableList.get(j));
            }
            
            tabela.setFixedCellSize(35);
            int brojRedova = listaPodataka.size();
            tabela.setPrefHeight(brojRedova * tabela.getFixedCellSize() + 3);
            tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
        return tabela;
    }  
    
    public void izbrisiSveIzTabele(TableView<Map<String,String>> tabela) 
    {
        for ( int i = 0; i< tabela.getItems().size(); i++) {
                tabela.getItems().clear();
        }
        
    }
    
    public int getRowIndexOfStavka(TableView<Map<String,String>> tabela, StavkaTure stavka) {
        int brojac = 0;
        int redniBrojKolone_RedniBroj = 6;
        int redniBrojKolone_RedniBrojGlavneStavke = 5;
        
        TableColumn kolonaRB = tabela.getColumns().get(redniBrojKolone_RedniBroj);
        TableColumn kolonaGlavniRB = tabela.getColumns().get(redniBrojKolone_RedniBrojGlavneStavke);
        
        for (Object row : tabela.getItems()) {
            if (kolonaRB.getCellObservableValue(row).getValue().equals("" + stavka.getRedniBroj())
                && kolonaGlavniRB.getCellObservableValue(row).getValue().equals("" + stavka.getRedniBrojGlavneStavke())) {
                    tabela.getSelectionModel().select(brojac);
                    return brojac;
            }
            brojac++;
        }
        return -1;
    }

}
