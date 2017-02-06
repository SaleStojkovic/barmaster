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
public class Artikal_Slozeni extends Child_Abstract implements Child_Interface {

    public List<Artikal_Opisni> opisniArtikli = new ArrayList<>();
    public List<Artikal_Dodatni> dodatniArtikli = new ArrayList<>();
    
    public Artikal_Slozeni(HashMap<String, String> artikalFrontMap) {
        
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
        
        getAllChildren();    
    }
    

    private void getAllChildren()
    {
        DBBroker dbBroker = new DBBroker();
        
        QueryBuilder query = new QueryBuilder(QueryBuilder.SELECT);
        
        query.addTableJoins(
                new TableJoin("artikal", "artikal_slozeni", "id", "opisniDodatniArtikalID", TableJoinTypes.INNER_JOIN),
                new TableJoin("artikal", "artikal_stampac", "id", "artikalID", TableJoinTypes.INNER_JOIN)
        );
        
        query.addCriteriaColumns("artikal.artikalVrstaID", "artikal_slozeni.artikalID");
        query.addCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL);
        query.addOperators(QueryBuilder.LOGIC_AND);
        query.addCriteriaValues("2", this.id);
        
        List<HashMap<String, String>> listaOpisnih = dbBroker.runQuery(query);
        
        for (HashMap mapaOpisni : listaOpisnih) {
            Artikal_Opisni noviOpisni = new Artikal_Opisni();
            noviOpisni.makeFromHashMap(mapaOpisni);
            
            this.opisniArtikli.add(noviOpisni);
        }
        
        QueryBuilder query2 = new QueryBuilder(QueryBuilder.SELECT);
        
        query2.addTableJoins(
                new TableJoin("artikal", "artikal_slozeni", "id", "opisniDodatniArtikalID", TableJoinTypes.INNER_JOIN),
                new TableJoin("artikal", "artikal_stampac", "id", "artikalID", TableJoinTypes.INNER_JOIN)
        );
        
        query2.addCriteriaColumns("artikal.artikalVrstaID", "artikal_slozeni.artikalID");
        query2.addCriteria(QueryBuilder.IS_EQUAL, QueryBuilder.IS_EQUAL);
        query2.addOperators(QueryBuilder.LOGIC_AND);
        query2.addCriteriaValues("3", this.id);
        
        List<HashMap<String, String>> listaDodatnih  = dbBroker.runQuery(query2);

        for (HashMap mapaDodatni : listaDodatnih) {
            Artikal_Dodatni noviDodatni = new Artikal_Dodatni();
            noviDodatni.makeFromHashMap(mapaDodatni);
            
            this.dodatniArtikli.add(noviDodatni);
        }
    }
    
}
