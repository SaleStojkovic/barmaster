/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets.RM_TableView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class RM_TableView extends TableView<Map<String, String>> {

    public List<SirinaKolone> SIRINE_KOLONA =  new ArrayList<>();
    
    public List<RavnanjeKolone> RAVNANJA_KOLONA =  new ArrayList<>();

    public void setSirineKolona(SirinaKolone... sirineKolona)
    {
        this.SIRINE_KOLONA.addAll(Arrays.asList(sirineKolona));
    }
    
    public void addRavnjanje(RavnanjeKolone... novoRavnanje) 
    {
        RAVNANJA_KOLONA.addAll(Arrays.asList(novoRavnanje));
    }
    
    /**
     * 
     * @param lista
     */
    public void setPodaci (
            List<Map<String, String>> lista
        ) 
    {        
        if (lista.isEmpty()) {
            return;
        }
        
        popuniTabelu(lista);
                   

            for(SirinaKolone sirina : this.SIRINE_KOLONA) {
                
                this.getColumns().get(sirina.BROJ_KOLONE - 1).setVisible(true);
                this.getColumns().get(sirina.BROJ_KOLONE - 1).setPrefWidth(sirina.SIRINA_KOLONE);

            }
        
        
        if (!this.RAVNANJA_KOLONA.isEmpty()) {
                        
            for (RavnanjeKolone ravnanje : RAVNANJA_KOLONA) {
                                
                this.getColumns().get(ravnanje.BROJ_KOLONE - 1).getStyleClass().add(ravnanje.RAVNANJE);

            }
            
        }

        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
    }
    
    /**
     * 
     * @param listaPodataka
     */
    public void popuniTabelu(
            List<Map<String, String>> listaPodataka
    ) {
        
        this.izbrisiSveIzTabele();
        
        ObservableList<Map<String, String>> tableList = FXCollections.observableArrayList(listaPodataka);            
        
        if (!listaPodataka.isEmpty()) {
            
            Map<String, String> row = listaPodataka.get(0);
            
            Set<String> keys = row.keySet();
            
            String[] kolone = keys.toArray(new String[keys.size()]);
                        
            for (int i = 0; i < keys.size(); i++) {
                 
                TableColumn<Map<String, String>, String> column = new TableColumn<>();
                
                column.setVisible(false);
                
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
                
               this.getColumns().add(column);
               
            }
            
            for (int j = 0; j < listaPodataka.size(); j++) {
                this.getItems().add(tableList.get(j));
            }
            
            this.setFixedCellSize(35);
            int brojRedova = listaPodataka.size();
            this.setPrefHeight(brojRedova * this.getFixedCellSize() + 3);
            this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
    }  
    
    public void izbrisiSveIzTabele() 
    {
        for ( int i = 0; i< this.getItems().size(); i++) {
                this.getItems().clear();
        }
        
    }
    

    
    public void sortTableByColumn(
            String columnName
    ) {
        
        List<Map<String, String>> listaPodataka = new ArrayList<>();
        
            for (Map<String, String> map : this.getItems()) {
                listaPodataka.add(map);
            }        
            
            Comparator<Map<String, String>> mapComparator = new Comparator<Map<String, String>>() {
                public int compare(Map<String, String> m1, Map<String, String> m2) {
                    return m1.get(columnName).compareTo(m2.get(columnName));
                }
            };

            Collections.sort(listaPodataka, mapComparator);
            
            this.izbrisiSveIzTabele();
            
            this.setPodaci( 
                    listaPodataka
                    );
    }
}
