package sososososopy;

public class File {

    public String name;
    public MutexLocks zamek;

    File(String name, ProcesManager PM) {
        this.name = name;
        MutexLocks zamek = new MutexLocks("File_lock_of_" + name, PM);
        this.zamek = zamek;
    }

    public String getName() {
        return name;
    }

    public MutexLocks getLock() {
        return zamek;
    }

    public void displayZamki() {
        // System.out.println("Zamek >" + zamek.getName() + "< Stan zamku >" + zamek.getState() + "< dotyczy pliku >" + name + "<");
        zamek.displayLockedProcess();
    }

}
