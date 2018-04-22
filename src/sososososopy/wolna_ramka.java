/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sososososopy;

/**
 *
 * @author oem
 */
public class wolna_ramka {

    boolean isFree;
    int id;

    public wolna_ramka(boolean isFree, int id) {
        this.isFree = isFree;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIsFree(boolean isFree) {
        this.isFree = isFree;
    }

    public int getId() {
        return id;
    }

    public boolean getisFree() {
        return isFree;

    }
}
