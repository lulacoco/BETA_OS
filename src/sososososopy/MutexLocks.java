package sososososopy;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MutexLocks {

    public String name;
    private boolean state;
    private Proces lockedProcess;
    private Queue<Proces> waitingProcesses;
    private ProcesManager manager;

    public MutexLocks(String name, ProcesManager manager) {
        this.name = name;
        this.manager = manager;
        this.setState(false);
        waitingProcesses = new LinkedList<Proces>();
    }

    public String getLockedProcessName() {
        return lockedProcess.get_name();
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public boolean isQueueEmpty() {
        return waitingProcesses.isEmpty();
    }

    public void putInWaitingQueue(Proces process) {
        System.out.println("Potok jest pusty, prcoes >" + process.get_name() + "< przechodzi w stan oczekiwania!");
        process.set_if_lock(1);
        try {
            Running.running.flaga = 1;
            manager.set_state(process.get_ID(), 1);

        } catch (Exception ex) {
            // Logger.getLogger(MutexLocks.class.getName()).log(Level.SEVERE, null, ex);
        }

        waitingProcesses.offer(process);
    }

    public void releaseProcesses() {
        try {
            //  manager.set_state(waitingProcesses.peek().get_ID(), 0); //to jest metoda od prcesow i tu zmieniamy procesowi na szczycie kolejki procesow czekajacych stan na ready
            for (Iterator<Proces> iter = waitingProcesses.iterator(); iter.hasNext();) {
                Proces data = iter.next();
                data.set_if_lock(0);
                manager.set_state(data.get_ID(), 0);

            }
        } catch (Exception ex) {
            //Logger.getLogger(MutexLocks.class.getName()).log(Level.SEVERE, null, ex);
        }
        int size = waitingProcesses.size();
        for (int i = 0; i < size; i++) {
            waitingProcesses.remove();
        }
    }

    public void displayLockedProcess() {
        if (lockedProcess == null) {
            System.out.println("Nazwa zamka >" + name + "< Brak procesow w zamku. Stan zamka: >" + getState() + "<");
        } else {
            System.out.println("Nazwa zamka >" + name + "< Proces w zamku >" + lockedProcess.get_name() + "< Stan zamka >" + getState() + "<");
        }
    }

    public void lock(Proces processToLock) {
        if (!getState()) {
            //zamek jest otwarty, proces zamyka zamek i rusza dalej
            setState(true);
            this.lockedProcess = processToLock;
            processToLock.set_is_in_lock(1);
            System.out.println("Metoda lock() -> Lock >" + name + "< " + getState() + " with process >" + lockedProcess.get_name() + "<");
        } else {

            //zamek jest zamkniety wiec proces wedruje do kolejki i ustawiany jest jego bit blocked
            System.out.println("Lock >" + name + "< is already taken by process >" + lockedProcess.get_name() + "<. Process >" + processToLock.get_name() + "< state is set to waiting!");
            waitingProcesses.offer(processToLock);
            processToLock.set_if_lock(1);  // to ustawiamy na true zeby wiadomo bylo ze proces jest zablokowany

            try {
                Running.running.flaga = 1;
                manager.set_state(processToLock.get_ID(), 1); // to jest metoda od prcesow i nie wiem jeszcze jak oznaczyc proces czekajacy
            } catch (Exception ex) {
                // Logger.getLogger(MutexLocks.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void unlock(Proces processToUnlock) {
        //odblokowuje zamek i jezli kolejka nie jest pusta to zeruje bit blocked pierwszego oczekujacego procesu.
        if (processToUnlock.get_ID() == this.lockedProcess.get_ID()) {
            this.setState(false);
            System.out.println("Metoda unlock() -> Lock >" + name + "< " + getState() + " with process >" + lockedProcess.get_name() + "<");
            processToUnlock.set_is_in_lock(0);
            this.lockedProcess = null;
            if (!waitingProcesses.isEmpty()) {
                try {
                    manager.set_state(waitingProcesses.peek().get_ID(), 0); //to jest metoda od prcesow i tu zmieniamy procesowi na szczycie kolejki procesow czekajacych stan na ready
                } catch (Exception ex) {
                    //Logger.getLogger(MutexLocks.class.getName()).log(Level.SEVERE, null, ex);
                }
                waitingProcesses.poll().set_if_lock(0); // to ustawiamy na false zeby wiadomo bylo ze proces juz nie jest zablokowany (funkcja poll() zwraca gore kolejki procesow czekajacych i usuwa go z niej)
            }
        }
    }

    public void unlockPipe(Proces processToUnlock) {
        //odblokowuje zamek i jezli kolejka nie jest pusta to zeruje bit blocked pierwszego oczekujacego procesu.
        if (processToUnlock.get_ID() == this.lockedProcess.get_ID()) {
            this.setState(false);
            System.out.println("Metoda unlock() -> Lock >" + name + "< " + getState() + " with process >" + lockedProcess.get_name() + "<");
            processToUnlock.set_is_in_lock(0);
            this.lockedProcess = null;
            if (!waitingProcesses.isEmpty()) {
                try {
                    //  manager.set_state(waitingProcesses.peek().get_ID(), 0); //to jest metoda od prcesow i tu zmieniamy procesowi na szczycie kolejki procesow czekajacych stan na ready
                    for (Iterator<Proces> iter = waitingProcesses.iterator(); iter.hasNext();) {
                        Proces data = iter.next();
                        data.set_if_lock(0);
                        manager.set_state(data.get_ID(), 0);

                    }
                } catch (Exception ex) {
                    //Logger.getLogger(MutexLocks.class.getName()).log(Level.SEVERE, null, ex);
                }
                int size = waitingProcesses.size();
                for (int i = 0; i < size; i++) {
                    waitingProcesses.remove();
                }
            }
        }
    }

}
