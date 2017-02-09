/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models.Artikal;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Arbor
 */
public class Slozeni_Thread implements Runnable {
    public Slozeni_Thread(Podgrupa podgrupa, Map<String, String> slozeniArtikal) {
       this.podgrupa = podgrupa;
       this.slozeniArtikal = slozeniArtikal;
    }
   
   private Podgrupa podgrupa;
   private Map<String, String> slozeniArtikal;

   @Override
   public void run() {
        Artikal_Slozeni noviSlozeni = new Artikal_Slozeni((HashMap)slozeniArtikal);
        this.podgrupa.artikli.add(noviSlozeni);
   }
}
