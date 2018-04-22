package sososososopy;

public class Proces {

    public PCB block;//Filip nie czepiaj się public, Czarek tego potrzebował
    private Zarzadzanie_pamiecia zarz;

    public Proces(int id, String name, Zarzadzanie_pamiecia zarz) {//proces bezczynności, Filip na starcie robi część roboty automatycznie       
        this.zarz = zarz;
        block = new PCB();
        block.ID = id;
        block.name = name;//on dostanie z ProcesManager name="PB"

        block.which_file = "PB.txt";//zrobiliśmy dla procesu bezczynności plik .txt
        block.how_long = 4;//
        block.table_size = 1;//wiem już teraz, ile będzie zajmował proces bezczynności
        block.base_priority = block.current_priority = 6;//priorytet procesu bezczynności

        zarz.loadToFile("PB.txt", block);//w tej metodzie Filip nadaje odpowiednie wartości zmiennym page_table[] i where_in_file

        block.state = 0;//że gotowy-dopiero w Manager zmieniam na 3=Running

        //reszta wymaganych operacji wykonywana w metodzie ProcesManager, ze wzgledu na inny charakter działań
    }

    public Proces(int id, String name, int how_long, String which_file, Zarzadzanie_pamiecia zarz) {//pozostałe procesy
        this.zarz = zarz;
        block = new PCB();
        block.ID = id;//z pomocą podanych zmiennych inicjalizuję 5 pól w PCB
        block.name = name;//to ma byc logiczne?

        block.which_file = which_file;
        block.how_long = how_long;
        if ((how_long % 16) != 0) {
            block.table_size = how_long / 16 + 1;
        } else {
            block.table_size = how_long / 16;
        }

        if (id % 3 == 0) {//pamiętaj, że proces bezczynności sprawi, że pierwsze dwa będą zwykłe
            block.base_priority = block.current_priority = 2;//że procesy Czasu Rzeczywistego
        } else {
            block.base_priority = block.current_priority = 5;//że zwykłe
        }

        zarz.loadToFile(block.which_file, block);//w tej metodzie Filip nadaje odpowiednie wartości zmiennym page_table[] i where_in_file

        block.state = 0;//GOTOWY

        //reszta operacji wykonywana w metodzie ProcesManager, ze wzgledu na inny charakter działań
    }

    //WAZNE!!!//wszędzie gdzie nie zwracam całego obiektu musi być set (żaden String ani Integer niewiele tu pomoże, tu chodzi chyba o to, że ten ktoś dziala na kopii obiektu, ale na oryginałach jego wnętrza/pól - masz surowego Stringa=kopia i tak)
    public PCB get_PCB() {//kimkolwiek jestes ty używający tej metody przeczytaj powyższy komentarz (jak nie zrozumiesz to napisz do mnie)
        return block;
    }
    //SET niepotrzebny, w tym przypadku GET zadziała jak referencja

    public int get_ID() {
        return block.ID;
    }
    //SET nie ma, id się nie zmienia

    public String get_name() {
        return block.name;
    }
    //SET nie ma, bo imienia się nie zmienia

    public int get_state() {
        return block.state;
    }

    public void set_state(int i) {
        block.state = i;
    }

    public int get_base_priority() {
        return block.base_priority;
    }
    //SET nie ma, bo bazowego priorytetu się nie zmienia 

    public int get_current_priority() {
        return block.current_priority;
    }

    public void set_current_priority(int i) throws Exception {
        if (block.base_priority == 2) {//proces czasu rzeczywistego
            if (i == 0 || i == 1 || i == 2) {
                block.current_priority = i;
            } else {
                throw new Exception("Nie mozna zmienic typu procesu!");
            }
        }
        if (block.base_priority == 5) {//proces zwykły
            if (i == 3 || i == 4 || i == 5) {
                block.current_priority = i;
            } else {
                throw new Exception("Nie mozna zmienic typu procesu!");//Shell musi poinformowac Bartoszka, że podał złą nazwę
            }
        }
        if (block.base_priority == 6) {//proces bezczynności
            if (i != 6) {
                throw new Exception("Nie wolno krzywdzić procesu bezczynnosci!!!");//Shell musi poinformowac Bartoszka, że podał złą nazwę
            }
        }
    }

    //CZAREK
    public int get_how_hungry() {//zlicza wszystkie (ale nie swoje) rozkazy wykonane od narodzin tego procesu
        return block.how_hungry;
    }

    public void set_how_hungry(int i) {
        block.how_hungry = i;
    }

    public void increase_how_hungry() {
        block.how_hungry = block.how_hungry + 1;
    }

    public int get_vruntime() {
        return block.vruntime;
    }

    public void set_vruntime(int i) {
        block.vruntime = i;
    }

    public int get_czas_dodania() {//ten cały czas urodzenia
        return block.czas_dodania;
    }

    public void set_czas_dodania(int i) {
        block.czas_dodania = i;
    }

    public int get_liczba_wyk_rozk() {//ile rozkazów z TEGO procesu zostało wykonanych na razie
        return block.liczba_wyk_rozk;
    }

    public void set_liczba_wyk_rozk(int i) {
        block.liczba_wyk_rozk = i;
    }

    //JĘDRZEJ
    //FILIP
    public Stronica[] get_page_table() {
        return block.page_table;
    }

    //SET niepotrzebny, bo Wiersz to obiekt? No ale z testowania tablic obiektów wynika, że sam get całkowicie wystarczy
    public int get_how_long() {
        return block.how_long;
    }

    //SET niepotrzebny, tego się nie zmienia
    public int get_table_size() {
        return block.table_size;
    }

    //SET niepotrzebny, tego się nie zmienia
    public int get_where_in_file() {
        return block.where_in_file;
    }

    public void set_where_in_file(int i) {
        block.where_in_file = i;
    }

    public String get_which_file() {
        return block.which_file;
    }

    public void set_which_file(String i) {//To, że to string niewiele pomaga. Z naukowych badań doktora Mila wynika, że i tak w GET pracujemy na kopii
        block.which_file = i;
    }

    //SZYMON
    //PIOTREK
    public boolean get_if_lock() {
        return block.if_lock;
    }

    public void set_if_lock(int i) {
        if (i == 0) {
            block.if_lock = false;
        } else {
            block.if_lock = true;
        }
    }

    public int get_is_in_lock() {
        return block.is_in_lock;
    }

    public void set_is_in_lock(int i) {
        block.is_in_lock = i;
    }

    //EWA
    public String show_info() {
        String s = new String();
        s = "Proces " + block.name + " o ID=" + block.ID + " i priorytecie=" + block.base_priority + ", bedacy w stanie ";
        if (block.state == 0) {
            s = s + "''GOTOWY''";
        } else {
            if (block.state == 1) {
                s = s + "''OCZEKUJACY''";
            } else {
                if (block.state == 2) {
                    s = s + "''ZAKONCZONY''";
                } else {
                    if (block.state == 3) {
                        s = s + "!!RUNNING!!";
                    } else {
                        s = s + "-1";
                    }
                }
            }
        }
        s = s + ". Rejestr R1=" + block.R1 + ", Rejestr R2=" + block.R2 + ", Rejestr R3=" + block.R3 + ", Rejestr R4=" + block.R4 + ". Liczba wykonanych rozkazow: " + block.calkowita_lic_wyk_rozk;
        return s;
    }

}
