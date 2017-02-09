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
public class Grupa_Thread implements Runnable {

   public Grupa_Thread(Grupa grupa) {
       this.grupa = grupa;
   }
   
   private Grupa grupa;
   

   @Override
   public void run() {
         this.grupa.setAllChildren();
        rmaster.RMaster.grupeArtikala.add(grupa);

   }
}