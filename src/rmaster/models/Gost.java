/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmaster.models;

/**
 *
 * @author Bosko
 */
public class Gost {
    private long gostID = 0;
    private double procenatPopusta = 0;
    
    public Gost() {
    }

    public Gost(long ID) {
        gostID = ID;
    }
    
    public Gost(String ID) {
        gostID = Long.parseLong(ID);
    }

    public void setGostID(long gostID) {
        this.gostID = gostID;
    }

    public long getGostID() {
        return this.gostID;
    }

    public void setProcenatPopusta(double procenatPopusta) {
        this.procenatPopusta = procenatPopusta;
    }

    public double getProcenatPopusta() {
        return this.procenatPopusta;
    }
}
