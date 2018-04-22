package sososososopy;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author oem
 */
public class Stronica {

    private boolean v_or_i;
    private boolean sc;
    private int FrameNumber;

    public Stronica() {

    }

    public void setSc(boolean sc) {
        this.sc = sc;
    }

    public void setV_or_i(boolean v_or_i) {
        this.v_or_i = v_or_i;
    }

    public void setFrameNumber(int FrameNumber) {
        this.FrameNumber = FrameNumber;
    }

    boolean getV_or_i() {
        return v_or_i;
    }

    public int getFrameNumber() {
        return FrameNumber;
    }

}
