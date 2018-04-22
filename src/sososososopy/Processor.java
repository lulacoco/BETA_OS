package sososososopy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cezary
 */
//1 running
//0 ready
//0, 1, 2,3
//gotowy, oczekujÄ…cy, zakoĹ„czony wykonujacy
public class Processor {
//trzeba przerobic na arraylist

    int target_latency = 20;
//int minimum_granularity = 2; 
    static int czas_bezwzgledny = 0;
    public LinkedList<PCB> Runnable_proces_Queue = new LinkedList();  // Tablica kolejek dla zadaĹ„ aktywnych
    private ProcesManager manager;
    private Zarzadzanie_pamiecia zarz;

    public void setManager(ProcesManager manager, Zarzadzanie_pamiecia zarz) {
        this.manager = manager;
        this.zarz = zarz;

    }

    void boringmode(PCB ref) {
        try {
            ref.state = 3;
        } catch (Exception ex) {
            Logger.getLogger(Processor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void Add_to_Runnable_proces_Queue(PCB ref) throws Exception {

        //priorytety od 0 do 5
        ref.czas_dodania = czas_bezwzgledny;
        czas_bezwzgledny++;
        if (Runnable_proces_Queue.isEmpty()) {
            //  ref.przydzial=1;
            ref.vruntime = 20;//
            ref.state = 3;
            Runnable_proces_Queue.add(ref);

        } else {
            //Collections.sort(Runnable_proces_Queue,new Priority_Comparator());
            ref.vruntime = Runnable_proces_Queue.get(0).vruntime;
            Runnable_proces_Queue.add(ref);
            //tutaj moglbys sprawdzac czy jak zaden nie wykonal rozkazu to wykonuje i wtedy flagi przydzialu zeruje
            int flagprzydz = 0;
            for (int i = 0; i < Runnable_proces_Queue.size(); ++i) {
                if (Runnable_proces_Queue.get(i).calkowita_lic_wyk_rozk != 0) {
                    flagprzydz = 1;
                    break;
                }
            }

            if (flagprzydz == 0) {
                for (int i = 0; i < Runnable_proces_Queue.size(); ++i) {
                    Runnable_proces_Queue.get(i).przydzial = 0;
                }

                scheduler();
            }

        }
        Collections.sort(Runnable_proces_Queue, new Priority_Comparator());

        // System.out.println("cala kolejka procesow gotowych po dodaniu nowego PCB");
//                for(Iterator<PCB> it=Runnable_proces_Queue.iterator();it.hasNext();)
//                 {
//                     PCB elem=it.next();
//                     System.out.println(elem.name+" vruntime:"+elem.vruntime+" stan:"+elem.state+" calkowita_lic_wyk_rozk"+elem.calkowita_lic_wyk_rozk);
//                 }
    }

    void scheduler() throws Exception { // jesli przekroczy czas 2  

        //System.out.println("wywolanie schedulera");//zmiana vruntime dla calej kolejki gotowych, ew wywlaszczenie
        if (!Runnable_proces_Queue.isEmpty()) {//try catch i uwzglednij max granulality

            int prior = 0;//tzn w przykladzie (6-3)+(6-2)+(6-5)=8      
            PCB zm = Collections.max(Runnable_proces_Queue, new Base_Priority_cmp());
            int a = 1 + zm.base_priority;//Runnable_proces_Queue.get(Runnable_proces_Queue.size()-1).base_priority;
            for (int i = 0; i < Runnable_proces_Queue.size(); ++i) {

                int b = Runnable_proces_Queue.get(i).base_priority;
                prior += a - b;

            }  //System.out.println("Prior:"+prior);

            //wlasciwe liczenie vruntime
            Collections.sort(Runnable_proces_Queue, new Priority_Comparator());

            //obliczanie vruntime
            for (int i = 0; i < Runnable_proces_Queue.size(); ++i) {
                //(najwiekszy priorytet+1-najmniejszy)*(20/prior) zaokroglone do inta
                int b = Runnable_proces_Queue.get(i).base_priority;
                // System.out.println("a:"+a+"  b:"+b);
                //   Runnable_proces_Queue.get(i).vruntime=(int)Math.round((a-b)*(20.0/prior));//Czy tak moĹĽna?
                if ((int) ((a - b) * (20.0 / prior)) < 2) {
                    Runnable_proces_Queue.get(i).vruntime = 2;//minimalna granulacja 
                } else {
                    Runnable_proces_Queue.get(i).vruntime = (int) ((a - b) * (20.0 / prior));
                }
                //  System.out.println(Runnable_proces_Queue.get(i).name+"vruntime bez castow"+((a-b)*(20.0/prior)));
                //  System.out.println(Runnable_proces_Queue.get(i).name+"vruntime z castem"+Math.round((a-b)*(20.0/prior)));

                Runnable_proces_Queue.get(i).state = 0;

            }
            Collections.sort(Runnable_proces_Queue, new Priority_Comparator());

            //zmiana statusu na running       
            ///poczatek
            //zmiana statusu na running   
            int flag = 0;
            for (int i = 0; i < Runnable_proces_Queue.size(); ++i) {
                if (Runnable_proces_Queue.get(i).przydzial == 0) {
                    //  System.out.println("PIERWSZY IF");
                    Runnable_proces_Queue.get(i).przydzial = 1;
                    Runnable_proces_Queue.get(i).state = 3;
                    Running.running = Runnable_proces_Queue.get(i); //wybrany_przez_procesor do zmiennej globa lnej
                    flag = 1;
                    break;
                }

            }
            if (flag == 0) {
                for (int i = 0; i < Runnable_proces_Queue.size(); ++i) {
                    Runnable_proces_Queue.get(i).przydzial = 0;
                }

                Runnable_proces_Queue.get(0).przydzial = 1;
                Runnable_proces_Queue.get(0).state = 3;
                flag = 1;
                Running.running = Runnable_proces_Queue.get(0); //wybrany_przez_procesor do zmiennej globa lnej
            }
            //

            Collections.sort(Runnable_proces_Queue, new Priority_Comparator());

            //System.out.println("cala kolejka procesow gotowych po wywoĹ‚aniu SCHEDULERA ");
//    for(Iterator<PCB> it=Runnable_proces_Queue.iterator();it.hasNext();)
//                 {
//                      PCB elem=it.next();
//                     System.out.println(elem.name+" vruntime:"+elem.vruntime+" stan:"+elem.state+" calkowita_lic_wyk_rozk"+elem.calkowita_lic_wyk_rozk+" base priority"+elem.base_priority);
//                 }
        } else {
            Running.running = manager.get_proces_bezczynnosci().get_PCB();
        }
    }

    void Delete_from_Runnable_proces_Queue(PCB ref) throws Exception {
        ref.przydzial = 0;
        ref.liczba_wyk_rozk = 0;
        //System.out.println("ZDJECIE Z KOLEJKI PROCESOW GOTOWYCH procesu"+ref.name+" stan"+ref.state+" calkowita_lic_wyk_rozk"+ref.calkowita_lic_wyk_rozk);
        Runnable_proces_Queue.remove(ref);
        scheduler();

    }

    void czywywlaszczyc() throws Exception {
        PCB var = Collections.min(Runnable_proces_Queue, new Base_Priority_cmp());
        if (!Running.running.name.equals(var.name)) {
            if (Running.running.base_priority > var.base_priority) {
                Collections.sort(Runnable_proces_Queue, new Priority_Comparator());
                scheduler();
            }
        }

        Collections.sort(Runnable_proces_Queue, new Priority_Comparator());

    }

    void czywypisac() {
        System.out.println("Wypisanie calej listy PCB z shella");
        for (Iterator<PCB> it = Runnable_proces_Queue.iterator(); it.hasNext();) {
            PCB elem = it.next();
            System.out.println(elem.name + " vruntime:" + elem.vruntime + " stan:" + elem.state + " calkowita_lic_wyk_rozk" + elem.calkowita_lic_wyk_rozk + " base priority" + elem.base_priority);
        }
    }

}
