package sososososopy;

import java.util.List;
import java.util.LinkedList;

public class PCB {

    public static List<PCB> all_PCB = new LinkedList<>();//ta Filipowa lista specjalnej troski, ĹĽeby w kaĹĽdym bloku byĹ‚y wszystkie inne bloki

    public PCB() {//konstruktor
        all_PCB.add(this);
    }

    public int ID = -1;
    public String name = "-1";
    public int state = -1;//0, 1, 2, 3
    //gotowy, oczekujÄ…cy, zakoĹ„czony, RUNNING
    //0->2, 0->3, 3->0, 3->1, 3->2, 1->0, 1->2
    public int base_priority = -1;//LINUX!!! 0,1,2=CR, 3,4,5=N, 6-BezczynnoĹ›ci (NIZSZY=WAZNIEJSZY)
    public int current_priority = -1;
    public int how_hungry = 0; //Ja tego uĹĽywam i zwiekszam priorytety dziÄ™ki temu. Zlicza wszystkie rozkazy (ale nie swoje!) wykonane od narodzin tego konkretnego procesu. Ja tego uĹĽywam! W "increase_counters" sprawdzam, czy to pole podzielne przez np. 3. 

    //CZAREK
    public int skonczony = 0;
    public int przydzial = 0;
    public int vruntime = 2;//przyznany czas wykonania
    public int czas_dodania = 0;//Czarek tego potrzebuje
    public int calkowita_lic_wyk_rozk = 0;
    public int liczba_wyk_rozk = 0;////ile rozkazĂłw zostaĹ‚o wykonanych, gdy Czarek zauwaĹĽy, ĹĽe przekroczy vruntime to wywĹ‚aszzany
    //Czarek to wyĹĽej JÄ™drzej ma zerowaÄ‡ itp.? Znaczy z ciekawoĹ›ci chciaĹ‚bym jak to dziaĹ‚a :D ZrobiĹ‚em sety i gety jak coĹ› (sÄ… w Proces)

    //JÄ�DRZEJ
    public int licznik2 = 0;//JÄ™drzej to jest ta zmienna, ktĂłra ma okreĹ›laÄ‡ gdzie skoĹ„czyĹ‚eĹ› wykonywaÄ‡ program tego procesu
    public int R1 = 0;
    public int R2 = 0;
    public int R3 = 0;
    public int R4 = 0;
    public int licznik_aktualny = 0;
    public int flaga = 0;

    public void setR1(int R1) {
        this.R1 = R1;
    }

    public void setR2(int R2) {
        this.R2 = R2;
    }

    public void setR3(int R3) {
        this.R3 = R3;
    }

    public void setR4(int R4) {
        this.R4 = R4;
    }

    //FILIP
    public Stronica page_table[];//ref do tablicy stronic
    public int how_long = -1; //dĹ‚ugoĹ›Ä‡ w znaczkach/bajtach, dostajÄ™ od Ewy
    public int table_size = -1; //how_long/16 zaokrÄ…glajÄ…c w gĂłrÄ™
    public int where_in_file = -1;//numer bajtu, od ktĂłrego zaczyna siÄ™ kod tego procesu w pliku wymiany
    public String which_file = "-1";//nazwa pliku .txt(to .txt bÄ™dzie juĹĽ uwzglÄ™dnione w nazwie), ktĂłry zawiera kod tego procesu - dostaje od Ewy
    public Boolean blad = false;//Filip nie wiem po co ci to ale chyba tego chciaĹ‚es tak?
    //Filipowa lista statyczna na gĂłrze (na poczÄ…tku tej klasy)

    //SZYMON
    //PIOTREK
    public boolean if_lock = false;//ĹĽe niezablokowany
    public int is_in_lock = 0;//Proces nie jest w zamku

}
