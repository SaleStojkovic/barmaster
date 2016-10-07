/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

import java.util.Map;

/**
 *
 * @author Arbor
 */
public class Konobar {
    
    public  long konobarID;
    public  String konobarPin;
    public  String skrImeKonobara;
    public  String imeKonobara;
    
    public Konobar (
            Map<String, String> konobarMap
    ) {
        this.konobarID = Long.parseLong(konobarMap.get("id"));
        this.konobarPin = konobarMap.get("pin");
        this.skrImeKonobara = konobarMap.get("skrIme");
        this.imeKonobara = konobarMap.get("punoIme");
    }
    
    
}
