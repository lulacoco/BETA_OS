package sososososopy;

import java.util.Iterator;
import java.util.LinkedList;

public class Communication {

    public static LinkedList<Queue> queues;
    private ProcesManager PM;
    private int reader_flag;
    private String msgToSend;

    public Communication(ProcesManager PM) {
        this.PM = PM;
        queues = new LinkedList<Queue>();
        reader_flag = 0;
        this.msgToSend = new String();
    }

    public void pokazZamki() {
        if (queues.isEmpty()) {
            //System.out.println("Brak zamkow zwiazanych z potokami!");
        } else {
            for (Iterator<Queue> iter = queues.iterator(); iter.hasNext();) {
                Queue data = iter.next();
                data.lock.displayLockedProcess();
                //System.out.print(data.pagenr+"&"+data.SC+"&id"+data.id+" ");
            }
        }
    }

    public String getMsgToSend() {
        return msgToSend;
    }

    public int get_reader_flag() {
        return reader_flag;
    }

    public Boolean isQueue(String name) {
        for (Queue x : queues) {
            if (x.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Queue issQueue(String name) {
        Queue q1 = null;
        for (Queue x : queues) {
            if (x.getName().equals(name)) {
                return x;
            }
        }
        return q1;
    }

    public Queue createQueue(String name) {
        if (isQueue(name)) {
            System.out.println("Potok o danej nazwie juz istnieje!");
            return issQueue(name);
        }
        Queue que = new Queue(name, new MutexLocks("Pipe_lock_of_" + name, PM));
        queues.push(que);

        System.out.println("Stworzono potok o danej nazwie " + name + "!");
        return que;
    }

    public void deleteQueue(String name) {
        for (Queue x : queues) {
            if (x.getName().equals(name)) {
                System.out.println("Usunieto potok o danej nazwie " + x.getName() + "!");
                queues.remove(x);
                return;
            }
        }
        System.out.println("Potok o danej nazwie nie istnieje!");
    }

    public void write(String potok, String msg) throws Exception {

        Queue q1 = issQueue(potok);
        if (q1 == null) {
            System.out.println("Nie stworzono takiego potoku!");
            return;
        }
        q1.lock.lock(PM.get_proces(Running.running.name));
        System.out.println("Dopisano wiadomosc '" + msg + "' z procesu " + Running.running.name + " do kolejki o nazwie " + potok);
        q1.getFifo().add(msg);
        q1.lock.unlockPipe(PM.get_proces(Running.running.name));
    }

    public void read(String potok, int ilosc_znakow, int adres_w_pamieci) throws Exception {
        Queue q1 = issQueue(potok);

        ///////////////////////////
        if (q1 == null) {
            System.out.println("Najpierw stworz potok o takiej nazwie!");
            Running.running.blad = true;
            return;
        }

        /////////////////////////////////
        if (q1.getFifo().isEmpty()) {

            //Running.running.flaga = 1;
            System.out.println("Nie mozna czytac z pustego potoku!");
            q1.lock.putInWaitingQueue(PM.get_proces(Running.running.name));
            reader_flag = 1;
            //msgToSend = "NULL";
            msgToSend = "";

        } else {

            q1.lock.lock(PM.get_proces(Running.running.name));
            String buffor = new String(); //Nowa wiadomosc
            int counter = 0; //Zlicza ilosc znakow ktore chcemy uzyskac

            int countWordsInQueue = 0; //Zlicza slowa ktore nalezy usunac w kolejce
            int sumaWszystkichCharow = 0; //Ilosc charow w calej kolejce
            int poczatekNowegoStringa = 0;

            for (int i = 0; i < q1.getFifo().size(); i++) {
                sumaWszystkichCharow += q1.getFifo().get(i).length();
            }

            for (int i = 0; i < q1.getFifo().size(); i++) {
                poczatekNowegoStringa = 0;
                countWordsInQueue++;

                for (int j = 0; j < q1.getFifo().get(i).length(); j++) {
                    poczatekNowegoStringa++;
                    buffor += q1.getFifo().get(i).charAt(j);
                    counter++;
                    if (counter == ilosc_znakow) {
                        break;
                    }
                }
                if (counter == ilosc_znakow) {
                    break;
                }
            }

            if (ilosc_znakow > sumaWszystkichCharow) {
                System.out.println("Wiadomosc ma tylko " + sumaWszystkichCharow + " znakow, oto one: " + buffor);

            } else {
                System.out.println("Proces " + Running.running.name + " odczytal wiadomosc: " + buffor);
            }

            String stringPierwszy = new String();

            for (int i = poczatekNowegoStringa; i < q1.getFifo().get(countWordsInQueue - 1).length(); i++) {
                stringPierwszy += q1.getFifo().get(countWordsInQueue - 1).charAt(i);
            }

            for (int i = 0; i < countWordsInQueue; i++) {
                q1.getFifo().pop();
            }

            if (!stringPierwszy.isEmpty()) {
                q1.getFifo().push(stringPierwszy);
            }

            if (q1.getFifo().isEmpty()) {
                Iterator<Queue> iterr = queues.iterator();
                while (iterr.hasNext()) {
                    Queue p = iterr.next();
                    if (p.equals(q1)) {
                        iterr.remove();
                    }
                }
            }

            msgToSend = buffor;
            reader_flag = 0;
            q1.lock.unlockPipe(PM.get_proces(Running.running.name));
        }

    }

    public void show_all() {
        if (queues.isEmpty()) {
            System.out.println("Nie ma zadnych kolejek!");
            return;
        }
        int x = 1;
        System.out.println("To sa wszystkie istniejace kolejki:");
        for (Queue q : queues) {
            System.out.println(x + ". Kolejka o nazwie " + q.getName() + " ma nastepujace wiadomosci: ");
            for (int i = 0; i < q.getFifo().size(); i++) {
                System.out.println(q.getFifo().get(i));
            }
            System.out.println();
            x++;
        }
    }
}
