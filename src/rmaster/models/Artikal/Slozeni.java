/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models.Artikal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import rmaster.assets.DBBroker;
import rmaster.assets.QueryBuilder.QueryBuilder;
import rmaster.assets.QueryBuilder.TableJoin;
import rmaster.assets.QueryBuilder.TableJoinTypes;

/**
 *
 * @author Arbor
 */
public class Slozeni extends ArtikalAbstract implements ArtikalInterface {

    public List<Opisni> opisniArtikli = new ArrayList<>();
    public List<Dodatni> dodatniArtikli = new ArrayList<>();
    
    public Slozeni(HashMap<String, String> artikalFrontMap) {
        
        this.id = artikalFrontMap.get(PRIMARY_KEY);
        this.barCode = artikalFrontMap.get(BAR_CODE);
        this.cena = artikalFrontMap.get(CENA);
        this.dozvoljenPopust = artikalFrontMap.get(DOZVOLJEN_POPUST);
        this.jedinicaMere = artikalFrontMap.get(JEDINICA_MERE);
        this.naziv = artikalFrontMap.get(NAZIV);
        this.prioritet = artikalFrontMap.get(PRIORITET);
        this.skrNaziv = artikalFrontMap.get(SKRACENI_NAZIV);
        this.slika = artikalFrontMap.get(SLIKA);
        this.stampacID = artikalFrontMap.get(STAMPAC_ID);
        
        setAllChildren();    
    }
    
    @Override
    public void setAllChildren()
    {
        DBBroker dbBroker = new DBBroker();
        
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        
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

        query.addTableJoins(
                new TableJoin("artikal", "artikal_slozeni", "id", "opisniDodatniArtikalID", TableJoinTypes.INNER_JOIN),
                new TableJoin("artikal", "artikal_stampac", "id", "artikalID", TableJoinTypes.LEFT_JOIN)
        );
        
        query.addCriteriaColumns("artikal.artikalVrstaID", "artikal_slozeni.artikalID");
        query.addCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL);
        query.addOperators(QueryBuilder.LOGIC_AND);
        query.addCriteriaValues("2", this.id);
        
        List<HashMap<String, String>> listaOpisnih = dbBroker.runQuery(query);
        
        for (HashMap mapaOpisni : listaOpisnih) {
            Opisni noviOpisni = new Opisni();
            noviOpisni.makeFromHashMap(mapaOpisni);
            
            this.opisniArtikli.add(noviOpisni);
        }
        
        query.CRITERIA_VALUES.clear();
        query.addCriteriaValues("3", this.id);
        
        List<HashMap<String, String>> listaDodatnih  = dbBroker.runQuery(query);

        for (HashMap mapaDodatni : listaDodatnih) {
            Dodatni noviDodatni = new Dodatni();
            noviDodatni.makeFromHashMap(mapaDodatni);
            
            this.dodatniArtikli.add(noviDodatni);
        }
    }
    
}
