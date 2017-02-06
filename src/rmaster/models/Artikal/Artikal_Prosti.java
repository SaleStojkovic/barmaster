/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models.Artikal;

import java.util.HashMap;
import static rmaster.models.Artikal.Child_Abstract.BAR_CODE;
import static rmaster.models.Artikal.Child_Abstract.CENA;
import static rmaster.models.Artikal.Child_Abstract.DOZVOLJEN_POPUST;
import static rmaster.models.Artikal.Child_Abstract.JEDINICA_MERE;
import static rmaster.models.Artikal.Child_Abstract.NAZIV;
import static rmaster.models.Artikal.Child_Abstract.PRIMARY_KEY;
import static rmaster.models.Artikal.Child_Abstract.PRIORITET;
import static rmaster.models.Artikal.Child_Abstract.SKRACENI_NAZIV;
import static rmaster.models.Artikal.Child_Abstract.SLIKA;
import static rmaster.models.Artikal.Child_Abstract.STAMPAC_ID;

/**
 *
 * @author Arbor
 */
public class Artikal_Prosti extends Child_Abstract implements Child_Interface {
    
    public Artikal_Prosti(HashMap<String, String> artikalFrontMap) {
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
    }
    
}
