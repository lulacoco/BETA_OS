/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sososososopy;

import java.util.Comparator;
//import processManager.PCB;

/**
 *
 * @author Cezary
 */
public class Base_Priority_cmp implements Comparator<PCB> {
//

    @Override
    public int compare(PCB p1, PCB p2) {
        int czyrowne = p1.base_priority - p2.base_priority;
        if (czyrowne == 0) {
            return (int) (p1.czas_dodania - p2.czas_dodania);//najmniejszy najlepszy
        }

        return (p1.base_priority - p2.base_priority);//najmniejszy najlepszy
    }

}
