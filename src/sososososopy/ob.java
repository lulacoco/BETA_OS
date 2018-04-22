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
public class ob {

    int pagenr;
    boolean SC;
    int id;

    public int getId() {
        return id;
    }

    public ob(int pagenr, boolean SC, int id) {
        this.pagenr = pagenr;
        this.SC = SC;
        this.id = id;
    }

    public ob(int pagenr, int id) {
        this.pagenr = pagenr;

        this.id = id;
    }

    public int getPagenr() {
        return pagenr;
    }

    public boolean getSC() {
        return SC;
    }

    public void setPagenr(int pagenr) {
        this.pagenr = pagenr;
    }

    public void setSC(boolean SC) {
        this.SC = SC;
    }

}
