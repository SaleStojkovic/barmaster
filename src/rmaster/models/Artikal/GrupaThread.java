/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models.Artikal;

/**
 *
 * @author Arbor
 */
public class GrupaThread implements Runnable {

   private Grupa grupa;
   
   public GrupaThread(Grupa grupa) {
       this.grupa = grupa;
   }
   
   @Override
   public void run() {
        this.grupa.setAllChildren();
        rmaster.RMaster.grupeArtikala.add(grupa);

   }
}