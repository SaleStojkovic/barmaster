/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.assets.RM_TableView;

/**
 *
 * @author Arbor
 */
public class RavnanjeKolone {
     
    
    public static String ALIGN_LEFT = "poravnaj-levo";
    
    public static String ALIGN_RIGHT = "poravnaj-desno";
    
    public static String ALIGN_CENTER = "poravnaj-centar";
    
    public int BROJ_KOLONE;
    
    public String RAVNANJE;
    
    public RavnanjeKolone (Integer brojKolone, String ravnanje)
    {
        this.BROJ_KOLONE = brojKolone;
        this.RAVNANJE = ravnanje;
    }
}
