package sososososopy;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ProcesManager {

    private int new_ID = 0;
    private List<Proces> waiting_normal_processes = new LinkedList<>();
    private List<Proces> ready_normal_processes = new LinkedList<>();
    private List<Proces> waiting_real_processes = new LinkedList<>();
    private List<Proces> ready_real_processes = new LinkedList<>();
    private List<Proces> all_processes = new LinkedList<>();//tylko ta lista zawiera proces bezczynności
    private Processor procesor;
    private Zarzadzanie_pamiecia zarz;

    public ProcesManager(Processor procesor, Zarzadzanie_pamiecia zarz) {
        this.procesor = procesor;
        this.zarz = zarz;

        //PROCES BEZCZYNNOŚCI TYLKO W ALL_PROCESSESready_normal_processes.add(new Proces(new_ID, "PB", zarz));
        //PROCES BEZCZYNNOŚCI TYLKO W ALL_PROCESSESall_processes.add(ready_normal_processes.get(0));
        all_processes.add(new Proces(new_ID, "PB", zarz));
        new_ID++;
        //CZAREK TEGO NIE CHCE!!!procesor.Add_to_Runnable_proces_Queue(all_processes.get(0).get_PCB());//ref zadziała jak trzeba
        all_processes.get(0).set_state(3);//bo w następnej linijce go wstawiam do running
        Running.running = all_processes.get(0).get_PCB();//to bedzie ten nasz proces bezczynności

    }

    public void new_proces(String name, int how_long, String which_file) throws Exception {
        int w = 0;
        for (Proces p : all_processes) {
            if (p.get_name().equals(name)) {
                w = 1;
            }
        }

        if (w == 0) {//nazwa się nie powtarza
            Proces p1 = new Proces(new_ID, name, how_long, which_file, zarz);
            new_ID++;

            if (p1.get_base_priority() == 0 || p1.get_base_priority() == 1 || p1.get_base_priority() == 2) {//2 wystarczy ale wolałem to uodpornic troche na błędy
                ready_real_processes.add(p1);
                all_processes.add(p1);

                procesor.Add_to_Runnable_proces_Queue(p1.get_PCB());//dodaj proces do mojej listy
            }
            if (p1.get_base_priority() == 3 || p1.get_base_priority() == 4 || p1.get_base_priority() == 5) {//jak wyżej, 5 wystarczy
                ready_normal_processes.add(p1);
                all_processes.add(p1);

                procesor.Add_to_Runnable_proces_Queue(p1.get_PCB());//dodaj proces do mojej listy
            }
        } else {//że nazwa się powtarza
            throw new Exception("Prosze podac inna nazwe, ta jest juz zajeta!");
        }
    }

    public void delete_proces(String name) throws Exception {//NO I TUTAJ MILA COŚ JEST ŻLE//jeśli to czytacie znaczy już jest ok

        int w = 0;//flaga "czy znaleziono" taki proces

        Iterator<Proces> iterr = all_processes.iterator();
        while (iterr.hasNext()) {
            Proces p = iterr.next();
            if (p.get_name().equals(name)) {
                iterr.remove();
            }

            if (p.get_name().equals(name)) {
                int czy_czarek_usuwa = 0;
                if (p.get_state() == 0 || p.get_state() == 3 || p.get_state() == 2) {
                    czy_czarek_usuwa = 1;
                }
                w = 1;//ze znaleziono taki proces

                p.set_state(2);//zakonczony, bardzo ważny stan!!!
                if (p.get_if_lock() == true) {
                    throw new Exception("Nie wolno krzywdzic procesu oczekujacego!");
                } else {
                    if (p.get_name().equals("PB")) {//jeśli użytkownik chce usunąć proces bezczynności, to nakrzycz na niego i nic nie rób
                        //if(p.get_name().equals(Running.running.name)) zarz.freeMem();
                        // System.out.println("LISTA MILI:LISTA MILI:LISTA MILI:LISTA MILI:LISTA MILI:LISTA MILI:LISTA MILI:LISTA MILI:LISTA MILI:LISTA MILI:LISTA MILI:LISTA MILI:");

                        throw new Exception("Nie wolno krzywdzic procesu bezczynnosci!");
                    } else {//jeśli to nie jest proces bezczynności, to pozwól usunąć
                        //to poniżej bardzo potrzebne! Robię coś takiego w paru metodach ale one NIGDY nie działają ze sobą po kolei przyjrzyj się uważnie!!!
                        zarz.freeMem();
                        if (czy_czarek_usuwa == 1) {
                            procesor.Delete_from_Runnable_proces_Queue(p.get_PCB());//usunie od Czarka tylko jeśli to był proces gotowy -> czyli Czarek go miał
                        }

                        Iterator<Proces> iter = ready_normal_processes.iterator();
                        while (iter.hasNext()) {
                            Proces str = iter.next();
                            if (str.get_name().equals(name)) {
                                // tutaj trzeba znalesc tego durnego locka w ktorym siedzi ten durny proces bo bamboszkowi zachcialo sie zajebywac procesy!
                                if (str.get_is_in_lock() == 1) {
                                    for (int i = 0; i < FileSystem.listaZamkow.size(); i++) {
                                        if (FileSystem.listaZamkow.get(i).zamek.getLockedProcessName().equals(str.get_name())) {
                                            FileSystem.listaZamkow.get(i).zamek.unlockPipe(str);
                                        }
                                    }
                                }

                                for (Iterator<Queue> it = Communication.queues.iterator(); it.hasNext();) {
                                    Queue data = it.next();
                                    if (data.lock.getState() == false && !data.lock.isQueueEmpty()) {
                                        data.lock.releaseProcesses();
                                    }
                                }
                                iter.remove();
                            }
                        }
                        Iterator<Proces> iter2 = waiting_normal_processes.iterator();
                        while (iter2.hasNext()) {
                            Proces str = iter2.next();
                            if (str.get_name().equals(name)) {
                                // tutaj trzeba znalesc tego durnego locka w ktorym siedzi ten durny proces bo bamboszkowi zachcialo sie zajebywac procesy!
                                if (str.get_is_in_lock() == 1) {
                                    for (int i = 0; i < FileSystem.listaZamkow.size(); i++) {
                                        if (FileSystem.listaZamkow.get(i).zamek.getLockedProcessName().equals(str.get_name())) {
                                            FileSystem.listaZamkow.get(i).zamek.unlockPipe(str);
                                        }
                                    }
                                }

                                for (Iterator<Queue> it = Communication.queues.iterator(); it.hasNext();) {
                                    Queue data = it.next();
                                    if (data.lock.getState() == false && !data.lock.isQueueEmpty()) {
                                        data.lock.releaseProcesses();
                                    }
                                }
                                iter2.remove();
                            }
                        }
                        Iterator<Proces> iter3 = ready_real_processes.iterator();
                        while (iter3.hasNext()) {
                            Proces str = iter3.next();
                            if (str.get_name().equals(name)) {
                                // tutaj trzeba znalesc tego durnego locka w ktorym siedzi ten durny proces bo bamboszkowi zachcialo sie zajebywac procesy!
                                if (str.get_is_in_lock() == 1) {
                                    for (int i = 0; i < FileSystem.listaZamkow.size(); i++) {
                                        if (FileSystem.listaZamkow.get(i).zamek.getLockedProcessName().equals(str.get_name())) {
                                            FileSystem.listaZamkow.get(i).zamek.unlockPipe(str);
                                        }
                                    }
                                }

                                for (Iterator<Queue> it = Communication.queues.iterator(); it.hasNext();) {
                                    Queue data = it.next();
                                    if (data.lock.getState() == false && !data.lock.isQueueEmpty()) {
                                        data.lock.releaseProcesses();
                                    }
                                }
                                iter3.remove();
                            }
                        }
                        Iterator<Proces> iter4 = waiting_real_processes.iterator();
                        while (iter4.hasNext()) {
                            Proces str = iter4.next();
                            if (str.get_name().equals(name)) {
                                // tutaj trzeba znalesc tego durnego locka w ktorym siedzi ten durny proces bo bamboszkowi zachcialo sie zajebywac procesy!
                                if (str.get_is_in_lock() == 1) {
                                    for (int i = 0; i < FileSystem.listaZamkow.size(); i++) {
                                        if (FileSystem.listaZamkow.get(i).zamek.getLockedProcessName().equals(str.get_name())) {
                                            FileSystem.listaZamkow.get(i).zamek.unlockPipe(str);
                                        }
                                    }
                                }

                                for (Iterator<Queue> it = Communication.queues.iterator(); it.hasNext();) {
                                    Queue data = it.next();
                                    if (data.lock.getState() == false && !data.lock.isQueueEmpty()) {
                                        data.lock.releaseProcesses();
                                    }
                                }
                                iter4.remove();
                            }
                        }
                        Iterator<Proces> iter5 = all_processes.iterator();
                        while (iter5.hasNext()) {
                            Proces str = iter5.next();
                            if (str.get_name().equals(name)) {

                                // tutaj trzeba znalesc tego durnego locka w ktorym siedzi ten durny proces bo bamboszkowi zachcialo sie zajebywac procesy!
                                if (str.get_is_in_lock() == 1) {
                                    for (int i = 0; i < FileSystem.listaZamkow.size(); i++) {
                                        if (FileSystem.listaZamkow.get(i).zamek.getLockedProcessName().equals(str.get_name())) {
                                            FileSystem.listaZamkow.get(i).zamek.unlockPipe(str);
                                        }
                                    }
                                }

                                for (Iterator<Queue> it = Communication.queues.iterator(); it.hasNext();) {
                                    Queue data = it.next();
                                    if (data.lock.getState() == false && !data.lock.isQueueEmpty()) {
                                        data.lock.releaseProcesses();
                                    }
                                }

                                iter5.remove();
                            }
                        }
                    }
                }
            }
        }
        if (w == 0) {//że taki proces w ogóle nie istnieje
            throw new Exception("Taki proces nie istnieje");
        }
        //System.out.println("LISTA MILI:LISTA MILI:LISTA MILI:LISTA MILI:LISTA MILI:LISTA MILI:LISTA MILI:LISTA MILI:LISTA MILI:LISTA MILI:LISTA MILI:LISTA MILI:");
        for (Proces p : all_processes) {
            System.out.println(p.get_name());
        }
    }//CZAREK JUŻ BĘDZIE O WSZYSTKIM WIEDZIAŁ

    public int get_state(int ID) throws Exception {
        int w = 0;
        for (Proces p : all_processes) {
            if (p.get_ID() == ID) {
                w = 1;
                return p.get_state();//metoda o tej samej nazwie ale PROCESU a nie MENEDŻERA
            }
        }
        if (w == 0) {//że jednak nie znalazło
            throw new Exception("Nie ma takiego procesu!");//Shell musi powiadomić użytkownika
        }
        return -1;//to i tak nigdy się nie wykona ale bez tego program się czepia
    }

    public void set_state(int id, int new_state) throws Exception {

        if (new_state != 0 && new_state != 1 && new_state != 2 && new_state != 3) {//jeśli nowy stan poza zakresem
            throw new Exception("Podano zly nowy stan (podaj 0, 1, 2 lub 3!)");//Shell musi powiadomić użytkownika
        } else {//jeśli nowy stan to 0, 1, 2 albo 3(czyli jakis istniejący)
            ////0->2, 0->3, 3->0, 3->1, 3->2, 1->0, 1->2  

            for (Proces p : ready_normal_processes) {//nie wiem, czy tyle tego musi być, no ale dla pewności niech będzie - zaszkodzic nie może

                if (p.get_ID() == id) {
                    p.set_state(new_state);

                    if (new_state == 0) {
                        break;//bez tego czasem (w poniższych przypadkach) pętla "out of range"
                    }
                    if (new_state == 1) {
                        waiting_normal_processes.add(p);
                        ready_normal_processes.remove(p);
                        procesor.Delete_from_Runnable_proces_Queue(p.get_PCB());
                        break;
                    }
                    if (new_state == 2) {

                        delete_proces(p.get_name());//wywoła też kogo trzeba
                        break;
                    }
                    if (new_state == 3) {//jak ma być Running, to ciągle będzie w "Gotowych", dla mnie to to samo
                        break;
                    }
                }

            }
            for (Proces p : waiting_normal_processes) {
                if (p.get_ID() == id) {
                    p.set_state(new_state);

                    if (new_state == 0) {
                        ready_normal_processes.add(p);
                        waiting_normal_processes.remove(p);
                        procesor.Add_to_Runnable_proces_Queue(p.get_PCB());
                        break;
                    }
                    if (new_state == 1) {
                        break;
                    }
                    if (new_state == 2) {
                        delete_proces(p.get_name());//wywoła też kogo trzeba
                        break;
                    }
                    if (new_state == 3) {
                        ready_normal_processes.add(p);
                        waiting_normal_processes.remove(p);
                        procesor.Add_to_Runnable_proces_Queue(p.get_PCB());
                        break;
                    }
                }
            }
            for (Proces p : ready_real_processes) {
                if (p.get_ID() == id) {
                    p.set_state(new_state);

                    if (new_state == 0) {
                        break;
                    }
                    if (new_state == 1) {
                        waiting_real_processes.add(p);
                        ready_real_processes.remove(p);
                        procesor.Delete_from_Runnable_proces_Queue(p.get_PCB());
                        break;
                    }
                    if (new_state == 2) {
                        delete_proces(p.get_name());//wywoła też kogo trzeba
                        break;
                    }
                    if (new_state == 3) {
                        break;
                    }
                }
            }
            for (Proces p : waiting_real_processes) {
                if (p.get_ID() == id) {
                    p.set_state(new_state);

                    if (new_state == 0) {
                        ready_real_processes.add(p);
                        waiting_real_processes.remove(p);
                        procesor.Add_to_Runnable_proces_Queue(p.get_PCB());
                        break;
                    }
                    if (new_state == 1) {
                    }
                    if (new_state == 2) {
                        delete_proces(p.get_name());//wywoła też kogo trzeba
                        break;
                    }
                    if (new_state == 3) {
                        ready_real_processes.add(p);
                        waiting_real_processes.remove(p);
                        procesor.Add_to_Runnable_proces_Queue(p.get_PCB());
                        break;
                    }
                }
            }
            if (id == 0) {//proces bezczynności jest tylko w liście "ALL_PROCESSES", i mimo zmian stanu nigdy z niej i tak nie wyjdzie. Mało tego zawsze 0 lub 3
                all_processes.get(0).set_state(new_state);//zmiana stanu, która nigdy nie powoduje żadnego przejścia
                //if(new_state==2) delete_proces("PB");
            }
        }
    }

    public PCB get_PCB(int id) throws Exception {
        for (Proces p : all_processes) {
            if (p.get_ID() == id) {
                return p.get_PCB();//taka sama nazwa metody, ale to juz jest metoda PROCESU a nie MENEDŻERA :D
            }
        }
        throw new Exception("Proces o podanym ID nie istnieje!");
    }

    public String show_info(String name) {
        for (Proces p : all_processes) {
            if (p.get_name().equals(name)) {
                return p.show_info();//jak wyżej, ta sama nzwa ale inne argumenty=metoda PROCESU a nie MENEDŻERA
            }
        }
        return "Proces o podanej nazwie nie zostal znaleziony";
    }

    public void increase_counters() {//Jędrzej wywołuje za każdym razem, gdy wykona jakiś rozkaz

        for (Proces p : all_processes) {//wszystkie procesy poza running - on nie jest głodzony!!!
            if (!p.get_name().equals(Running.running.name)) {
                p.increase_how_hungry();
                if (p.get_how_hungry() >= 5) {
                    p.set_how_hungry(p.get_how_hungry() - 5);
                    if (p.get_current_priority() == 1 || p.get_current_priority() == 2 || p.get_current_priority() == 4 || p.get_current_priority() == 5) {//jeśli można prawidłowo zmniejszyć priorytet
                        try {
                            p.set_current_priority(p.get_current_priority() - 1);//zmiana poza przedział nic nie zrobi
                        } catch (Exception e) {
                        }//Po prostu się nie wykona, ja nie mogę nic wyswietlić a poza tym to i tak byłby mój błąd w kodzie, a nie błąd użytkownika
                    }
                }
            }
        }
    }

    public Proces get_proces(String name) throws Exception {
        for (Proces p : all_processes) {
            if (p.get_name().equals(name)) {
                return p;
            }
        }
        throw new Exception("Nie ma takiego procesu!");
    }

    public Proces get_proces_bezczynnosci() {
        //zarz.loadToFile("PB.txt", all_processes.get(0).get_PCB());
        //System.out.println("LOAD TO FILELOAD TO FILELOAD TO FILELOAD TO FILELOAD TO FILELOAD TO FILE");
        return all_processes.get(0);
    }

    public int czy_istnieje(String nazwa) {
        for (Proces p : all_processes) {
            if (p.get_name().equals(nazwa)) {
                return 1;
            }
        }
        return 0;
    }

    public Stronica[] get_page_table(String name) {
        for (Proces p : all_processes) {
            if (p.get_name().equals(name)) {
                return p.get_page_table();
            }
        }
        return null;
    }

    public void show_all() {
        for (Proces p : all_processes) {
            if (p.get_state() == 3 && p.get_ID() != 0) {
                System.out.println(p.show_info());
            }
        }
        for (Proces p : all_processes) {
            if (p.get_state() == 0 && p.get_ID() != 0) {
                System.out.println(p.show_info());
            }
        }
        for (Proces p : all_processes) {
            if (p.get_state() == 1 && p.get_ID() != 0) {
                System.out.println(p.show_info());
            }
        }
    }

}
