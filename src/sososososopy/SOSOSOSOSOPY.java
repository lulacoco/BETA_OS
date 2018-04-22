/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sososososopy;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.openmbean.OpenDataException;

public class SOSOSOSOSOPY {

    public static void main(String[] args) {
        Shell shell = null;

        try {
            shell = new Shell();
        } catch (InterruptedException ex) {
            Logger.getLogger(SOSOSOSOSOPY.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SOSOSOSOSOPY.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OpenDataException ex) {
            Logger.getLogger(SOSOSOSOSOPY.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
