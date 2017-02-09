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
public class Podgrupa_Thread implements Runnable {
    
    public Podgrupa_Thread(Grupa grupa, Podgrupa podgrupa) {
       this.podgrupa = podgrupa;
       this.grupa = grupa;
    }
   
   private Podgrupa podgrupa;
   private Grupa grupa;

   @Override
   public void run() {
         this.podgrupa.setAllChildren();
         this.grupa.podgrupe.add(podgrupa);
   }
   
}
