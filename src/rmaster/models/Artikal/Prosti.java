/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models.Artikal;

import java.util.HashMap;
import static rmaster.models.Artikal.ArtikalAbstract.BAR_CODE;
import static rmaster.models.Artikal.ArtikalAbstract.CENA;
import static rmaster.models.Artikal.ArtikalAbstract.DOZVOLJEN_POPUST;
import static rmaster.models.Artikal.ArtikalAbstract.JEDINICA_MERE;
import static rmaster.models.Artikal.ArtikalAbstract.NAZIV;
import static rmaster.models.Artikal.ArtikalAbstract.PRIMARY_KEY;
import static rmaster.models.Artikal.ArtikalAbstract.PRIORITET;
import static rmaster.models.Artikal.ArtikalAbstract.SKRACENI_NAZIV;
import static rmaster.models.Artikal.ArtikalAbstract.SLIKA;
import static rmaster.models.Artikal.ArtikalAbstract.STAMPAC_ID;

/**
 *
 * @author Arbor
 */
public class Prosti extends ArtikalAbstract {
    
    public Prosti(HashMap<String, String> artikalFrontMap) {
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
