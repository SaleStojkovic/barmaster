/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import rmaster.assets.DBBroker;
import rmaster.assets.ModelBase;
import rmaster.assets.QueryBuilder.QueryBuilder;

/**
 *
 * @author Arbor
 */
public class Grupa extends ModelBase {

    public static String TABLE_NAME = "grupaartikalafront";
    public static String PRIMARY_KEY = "id";
    
    public static String NAZIV = "naziv";
    public static String PRIKAZ_NA_EKRAN = "prikazNaEkran";
    public static String PRIORITET = "prioritet";
    public static String SKR_NAZIV = "skrNaziv";
    public static String SLIKA = "slika";
    public static String GRUPA_ID = "GRUPA_ID";

    public String id;
    public String naziv;
    public String prikazNaEkran;
    public String prioriter;
    public String skrNaziv;
    public String slika;
    
    
    public List<Artikal_Podgrupa> podgrupe = new ArrayList<>();
    public List<Child_Abstract> artikli = new ArrayList<>();

    
    @Override
    public String getPrimaryKeyName() {
        return PRIMARY_KEY;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public void makeFromHashMap(HashMap<String, String> HashMap) {
        this.id = HashMap.get(PRIMARY_KEY);
        
        this.naziv = HashMap.get(NAZIV);
        
        this.prikazNaEkran = HashMap.get(PRIKAZ_NA_EKRAN);
        
        this.prioriter = HashMap.get(PRIORITET);
        
        this.skrNaziv = HashMap.get(SKR_NAZIV);
        
        this.slika = HashMap.get(SLIKA);
    }

    @Override
    public LinkedHashMap<String, String> toHashMap(boolean includeId) {
        LinkedHashMap<String, String> grupa = new LinkedHashMap();

        grupa.put(naziv, NAZIV);
        grupa.put(prikazNaEkran, PRIKAZ_NA_EKRAN);
        grupa.put(prioriter, PRIORITET);
        grupa.put(skrNaziv, SKR_NAZIV);
        grupa.put(slika, SLIKA);

        
        if (includeId) {
            grupa.put(PRIMARY_KEY, this.id);
        }
        
        return grupa;
    }
    
    
    public void setAllChildren() {
        uzmiSvePodgrupe();
        uzmiSveArtikle();
    }
    
    private void uzmiSvePodgrupe() {
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        DBBroker dbBroker = new DBBroker();
        
        query.setTableName(Artikal_Podgrupa.TABLE_NAME);
        
        query.addCriteriaColumns(Artikal_Podgrupa.PRIKAZ_NA_EKRAN, Artikal_Podgrupa.NADREDJENA_GRUPA_ID);
        query.addCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL);
        query.addOperators(QueryBuilder.LOGIC_AND);
        query.addCriteriaValues("1", this.id);
        query.setOrderBy(Artikal_Podgrupa.PRIORITET, QueryBuilder.SORT_ASC);
        
        List<Map<String, String>> podrgupaList = dbBroker.runQuery(query);
        
        for(Map<String, String> podgrupaMap : podrgupaList) {
            Artikal_Podgrupa novaPodgrupa = new Artikal_Podgrupa();
            
            novaPodgrupa.makeFromHashMap((HashMap)podgrupaMap);
            
            novaPodgrupa.ucitajSveArtikle();
            
            this.podgrupe.add(novaPodgrupa);
        }
    }
    
    private void uzmiSveArtikle()
    {
        //TODO ucitati sve artikle
    }
}
