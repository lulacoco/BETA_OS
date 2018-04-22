/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sososososopy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author oem
 */
public class SecondChance {

    Queue<ob> fifo;
    List<ob> list;

    public SecondChance() {
        fifo = new LinkedList<>();
        list = new LinkedList<>();
    }

    public void clearFifo(int id) {
        for (Iterator<ob> iter = list.iterator(); iter.hasNext();) {
            ob data = iter.next();
            if (data.getId() == id) {
                iter.remove();

            }
        }
        for (Iterator<ob> iter = fifo.iterator(); iter.hasNext();) {
            ob data = iter.next();
            if (data.getId() == id) {
                iter.remove();

            }
        }
    }

    public Queue<ob> getQueue() {
        return fifo;
    }

    private void remove(int nr, int id) {
        for (Iterator<ob> iter = list.iterator(); iter.hasNext();) {
            ob data = iter.next();
            if (data.getPagenr() == nr && data.getId() == id) {
                iter.remove();
                break;
            }
        }
    }

    private void show() {
        for (Iterator<ob> iter = list.iterator(); iter.hasNext();) {
            ob data = iter.next();
            //System.out.print(data.pagenr+"&"+data.SC+"&id"+data.id+" ");
        }
    }

    private boolean contains(int nr, int id) {
        for (Iterator<ob> iter = list.iterator(); iter.hasNext();) {
            ob data = iter.next();
            if (data.getPagenr() == nr && data.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private ob getOb(int nr, int id) {
        for (Iterator<ob> iter = fifo.iterator(); iter.hasNext();) { //zmienione na fifo

            ob data = iter.next();
            if (data.getPagenr() == nr && data.getId() == id) {
                return data;
            }
        }
        return null;
    }

    public ob make_space(Integer nr, int id) {

        ob toRet;
        if (!contains(nr, id)) {
            // System.out.println("Szukamy miejsca dla ramki nr "+nr + " o id "+id);
            for (int i = 0; i < 8; i++) {
                ob current = fifo.peek();

                if (getOb(current.getPagenr(), current.getId()).getSC()) {
                    // System.out.println("Ustawiam stronice "+ current.getPagenr()+" o id "+ current.getId()+ " na false");
                    fifo.remove().setSC(false);
                    current.setSC(false);
                    fifo.add(current);
                    list.get(i).setSC(false);

                } else if (getOb(current.getPagenr(), current.getId()).getSC() == false) {
                    fifo.remove();
                    fifo.add(new ob(nr, true, id));
                    remove(current.getPagenr(), current.getId());
                    list.add(new ob(nr, true, id));

                    // System.out.println("Wywalamy stronice nr "+ current.getPagenr() + " o id "+ current.getId());
                    show();
                    return current;
                }
            }

            ob last = fifo.remove();
            fifo.add(new ob(nr, true, id));
            remove(last.getPagenr(), last.getId());
            list.add(new ob(nr, true, id));
            // System.out.println("Wywalamy stronice nr "+ last.getPagenr()+" o id "+last.getId());
            return last;

        } else {
            // System.out.println("null");
            getOb(nr, id).setSC(true);
            return null;
        }
    }

    public void dodaj_do_kolejki(Integer nr, int id) {
        // System.out.println("do kolejki dodano ramke" + nr);
        fifo.add(new ob(nr, true, id));
        list.add(new ob(nr, true, id));

    }
}
