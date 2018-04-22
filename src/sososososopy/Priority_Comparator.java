
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sososososopy;

import java.util.Comparator;

//ustawianie kolejnosci na podstawie vruntime
public class Priority_Comparator implements Comparator<PCB> {

    @Override
    public int compare(PCB p1, PCB p2) {
        int czyrowne = p1.vruntime - p2.vruntime;
        if (czyrowne == 0) {
            return 1;
            //return (int) (p1.czas_dodania - p2.czas_dodania);//najmniejszy najlepszy
        }

        return (p1.vruntime - p2.vruntime);//najmniejszy najlepszy
    }

}
