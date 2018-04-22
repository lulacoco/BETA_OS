package sososososopy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.util.Scanner;
import java.util.HashMap;
import javax.management.openmbean.OpenDataException;

public class Shell {

    private void Separate(String line) { ///////////Rozdzielanie
        if (line.length() > 2) {

            Command = line.substring(0, 2);
            Command = Command.toUpperCase();
            Parameter = line.substring(3, line.length());
            Parameters = Parameter.split(" ");
        } else {
            if (line.length() < 3) {
                Command = line;
                Command = Command.toUpperCase();
                Parameters = null;
            }
        }
    } /////end rozdzielania

    private boolean isAllowed() { ///////Czy dane sa podane prawidlowo
        if (!Commands.containsKey(Command)) {
            return false;
        }
        if (Commands.get(Command) == 0 && Parameters == null) {
            return true;
        }
        if (Commands.get(Command) == 0 && Parameters != null) {
            return false;
        }
        if (Commands.get(Command) != 0 && Parameters == null) {
            return false;
        }
        if (Parameters.length != Commands.get(Command)) {
            return false;
        }
        return true;
    }

    private void draw() throws FileNotFoundException {

        fileLogo = new File("logo.txt");

        Scanner inL = new Scanner(fileLogo);
        while (inL.hasNextLine()) {  ////Dopoki plik nie jest pusty

            String logoLine = inL.nextLine();   ////////Wczytujemy polecenie

            System.out.println(logoLine);
        }
        System.out.println();
    } //////end logo

    private String Command;
    private String Parameter;
    private String[] Parameters;  ///maksymalnie 3 parametry
    private String PowtCom;
    private HashMap<String, Integer> Commands;

    private FileSystem disc;
    private ProcesManager ProcesManagerr;
    private Zarzadzanie_pamiecia Memory;
    private File fileScript;
    private File fileLogo;
    private Communication Communication;
    private MutexLocks Mutex;
    private Processor Processor;
    private Interpreter interpreter;

    public Shell() throws InterruptedException, FileNotFoundException, IOException, OpenDataException {
        int liczGO = 0;
        boolean cokolwiek = false;
        Memory = new Zarzadzanie_pamiecia();
        Memory.clearFile();

        Processor = new Processor();
        ProcesManagerr = new ProcesManager(Processor, Memory);
        disc = new FileSystem(32, 32, ProcesManagerr).create();

        Processor.setManager(ProcesManagerr, Memory);
        Communication = new Communication(ProcesManagerr);
        interpreter = new Interpreter(Communication, Memory, ProcesManagerr, disc);
        //////////

        Commands = new HashMap<>(); ///tworzymy liste zawierajaca komendy oraz liczbe parametrow
        Commands.put("CP", 2); ///Utworz proces
        ///Commands.put("PW", 1); ///Utworz proces
        Commands.put("DP", 1); ///Usuń proces
        Commands.put("BC", 1); ///Pokaż blok procesu
        Commands.put("PA", 0); ///Pokaż wszystkie procesy
        Commands.put("TS", 1); ///Pokaż tablice stronnic po nazwie 

        Commands.put("GO", 0); ///Wykonaj rozkaz
        Commands.put("MC", 2); ///Pokaz pamiec
        Commands.put("SS", 0); ///Pokaz kolejke drugiej szansy

        Commands.put("MF", 1); ///Utworz plik
        Commands.put("SF", 1); ///Otworz plik w trybie odczytu
        Commands.put("SE", 1); ///Otworz plik w trybie edycji
        Commands.put("DF", 1); ///Usuń plik
        Commands.put("EF", 2); ///Edytuj plik
        Commands.put("AF", 2); ///Dopisz do pliku
        Commands.put("MV", 2); ///Zmień nazwę pliku
        Commands.put("CF", 1); ///Zmien nazwe pliku
        Commands.put("SC", 0); ///Wyswietl tablice Fat
        Commands.put("SD", 0); ///Wyswietl plik z informacja o dlugosci
        Commands.put("PP", 1); ///Wyswietl plik
        Commands.put("SB", 2); ///Wyswietl konkretna liczbe bitow
        Commands.put("DB", 1); ///Wyswietlanie bloku dyskowego

        Commands.put("SP", 0); ///Pokaż wszystkie szymonowe rzeczy
        Commands.put("PI", 0); ///Pokaż wszystkie szymonowe rzeczy
        Commands.put("CW", 0); /////////Wyswietlic cos tam procesora
        Commands.put("HP", 0); /////////Wyswietlic helpa
        Commands.put("EX", 0); ///Zamknij system

        ////////////////////Ekran powitalny
        try {
            draw();
        } catch (FileNotFoundException e) {
            System.out.println("Wystapil błąd z wczytaniem loga.");
        }

        Scanner console = new Scanner(System.in);
        do {
            System.out.println("Prosze podac plik ze skryptem.");
            String inputScript = console.next();
            fileScript = new File(inputScript);
        } while (!fileScript.exists() || fileScript.isDirectory());
        Scanner in = new Scanner(fileScript);  ///////////Zawartosc pliku ze skryptami

        String line = in.nextLine();   ////////Wczytujemy polecenie 1
        String NewCom = "N";

        while (NewCom.charAt(0) == 'N' || NewCom.charAt(0) == 'B' || NewCom.charAt(0) == 'V' || NewCom.charAt(0) == 'H') {  ////POCZATEK WYKONYWANIA SIE

            Separate(line);

            while (isAllowed() == false) {
                System.out.println("Komenda >> " + line + " << jest nieprawidłowa.");
                System.out.println("Naciśnij E, jeśli chcesz wyjść z programu.");
                System.out.println("Naciśnij N, jesli chcesz ją pominąć i przejść do dalszej części skryptu.");
                System.out.println("Naciśnij B, jeśli chcesz podać nową kolejną komendę.");
                System.out.println("Naciśnij H, jeśli chcesz wyświetlić listę komend.");

                PowtCom = console.next();
                PowtCom = PowtCom.toUpperCase();

                if (PowtCom.charAt(0) == 'E') {

                    System.exit(0);
                } else if (PowtCom.charAt(0) == 'B') {

                    System.out.println("Proszę podać kolejną komendę oraz jej parametry.");
                    Scanner odczyt = new Scanner(System.in);

                    line = odczyt.nextLine();
                    /// line = line.toUpperCase();
                    Separate(line);
                } else if (PowtCom.charAt(0) == 'H') {

                    line = "HP";
                    Separate(line);
                } else {
                    line = in.nextLine();
                    Separate(line);
                }
            }

            switch (Command) {             ///osługa komendy
                case "CP": { ///Utworz proces

                    String inputScript2 = Parameters[1];
                    fileScript = new File(inputScript2);
                    if (!fileScript.exists() || fileScript.isDirectory()) {
                        inputScript2 = inputScript2 + ".txt";
                        fileScript = new File(inputScript2);
                        if (!fileScript.exists() || fileScript.isDirectory()) {
                            System.out.println("Plik o podanej nazwie nie istnieje. Nie można utworzyć procesu"
                                    + "Nastąpi teraz przejście do nastepnej komendy");
                            break;
                        }
                        Parameters[2] = inputScript2;
                    }
                    String PowT = "T";
                    while (PowT.charAt(0) == 'T') {
                        try {
                            ///a = Integer.parseInt(Parameters[1]);

                            ProcesManagerr.new_proces(Parameters[0], 1, Parameters[1]);
                            System.out.println("Pomyślnie utworzono proces o nazwie " + Parameters[0] + "!");
                            PowT = "G";

                        }///catch(NumberFormatException e){
                        ///System.out.println("Podana wielkość jest nieprawidłowa. Parametr drugi musi być liczbą. Nastąpi teraz przejście do następnej komendy."); break;}
                        catch (Exception e) {
                            System.out.println("Proces o takiej nazwie już istnieje. Czy chcesz uwtorzyć proces o nazwie " + (Parameters[0] + "1") + "?");
                            System.out.println("Wciśnij T jeśli tak.");

                            PowT = console.next();
                            PowT = PowT.toUpperCase();
                            Parameters[0] = (Parameters[0] + "1");
                        }
                    }
                    break;
                }
//                case "PW" :{
//                    
//                try {
//                    ProcesManagerr.set_state(ProcesManagerr.get_proces(Parameters[0]).get_ID(), 1);
//                } catch (Exception ex) {
//                    System.out.println(ex.getMessage());
//                }

                /// break;}
                case "DP": {
                    try {
                        ///Usuń proces
                        ProcesManagerr.delete_proces(Parameters[0]);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                }
                case "BC": { ///Pokaż blok procesu
                    System.out.println(ProcesManagerr.show_info(Parameters[0]));
                    break;
                }
                case "TS": {
                    int li = 0;
                    for (Stronica m : ProcesManagerr.get_page_table(Parameters[0])) {
                        System.out.println("Stronica " + li + " ramka " + m.getFrameNumber() + " bit v_or_i " + m.getV_or_i());
                        li++;
                    }

                    break;
                }
                case "GO": {
                    ///int b = 0;
                    ///try {
                    ///  b = Integer.parseInt(Parameters[0]);
                    ///} catch (NumberFormatException e) {
                    /// System.out.println("Podana wielkość jest nieprawidłowa. Parametr drugi musi być liczbą. Nastąpi teraz przejście do następnej komendy.");
                    ///break;
                    ///}

                    ///for(int i=0;i<b;i++){
                    try {
                        // if(2==Running.running.liczba_wyk_rozk %(Running.running.vruntime+1) )
                        //Processor.czywywlaszczyc();
                        //Processor.scheduler();
                        ///Wykonaj rozkaz
                        if (Running.running.vruntime == (Running.running.liczba_wyk_rozk) % (Running.running.vruntime + 1)) {
                            Running.running.skonczony = 1;//wykonal swoj kwant
                            //System.out.println("RANNINGGGGG/wykonal swoj kwant"+"liczba  wykonanych rozkazowe"+Running.running.calkowita_lic_wyk_rozk);
                            Running.running.liczba_wyk_rozk -= Running.running.vruntime;
                            Processor.scheduler();

                        }

                        interpreter.GO();
                        ++Running.running.liczba_wyk_rozk;
                        ++Running.running.calkowita_lic_wyk_rozk;
                        liczGO++;
                        System.out.println("wykonal sie rozkaz o numerze " + Running.running.liczba_wyk_rozk + "  name running " + Running.running.name + " Stan:  " + Running.running.state);
                        /// System.in.read();
                        // Memory.showFifo();

                        //for (int i = 0; i < Memory.getRAM(0, 128).length; i++) {
                        // System.out.print(Memory.getRAM(0, 128)[i]);
                        //}
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    ///}

                    break;
                }
                case "MC": { ///Pokaz pamiec
                    try {
                        int adr = Integer.parseInt(Parameters[0]);
                        int size = Integer.parseInt(Parameters[1]);

                        System.out.println();
                        for (int i = 0; i < Memory.getRAM(adr, size).length; i++) {
                            System.out.print(Memory.getRAM(adr, size)[i]);
                        }
                        System.out.println();
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Podana wielkość jest nieprawidłowa. Parametry muszą być liczbami. Nastąpi teraz przejście do następnej komendy.");
                        break;
                    } catch (Exception e) {
                        System.out.println("Podane dane są nieprawidłowe. Wychodzą poza zakres.");
                    }
                }

                case "SS": {

                    Memory.showFifo();

                    break;
                }

                case "MF": { ///Utworz plik
                    try {
                        disc.saveOnDisc(Parameters[0], "");
                        System.out.println("Plik został pomyślnie utworzony!");
                        break;
                    } catch (IOException e) {
                        System.out.println("Nie udało się utworzyć pliku: " + e.getMessage());
                    }
                }
                case "SF": { ///otworz plik w trybie odczytu
                    try {
                        System.out.println(disc.readFile(Parameters[0]));
                    } catch (NoSuchFileException e) {
                        System.out.println("Plik o nazwie \"" + Parameters[0] + " \" nie istnieje.");
                    } catch (OpenDataException e) {
                        System.out.println("Plik już jest otwarty!");

                    }
                    break;
                }
                case "SE": { ///Otworz plik w trybie edycji
                    try {
                        System.out.println(disc.readFile(Parameters[0], FileSystem.FILE_EDIT));
                    } catch (NoSuchFileException e) {
                        System.out.println("Plik o nazwie \"" + Parameters[0] + " \" nie istnieje.");
                    } catch (OpenDataException e) {
                        System.out.println("Plik już jest otwarty!");

                    }
                    break;
                }
                case "DF": { ///Usuń plik
                    if (disc.removeFile(Parameters[0])) {
                        System.out.println("Usunięto plik " + Parameters[0] + "!");
                    } else {
                        System.out.println("Usunięcie pliku nie powiodło się, ponieważ już nie istnieje. ");
                    }
                    break;
                }
                case "EF": { ///Edytuj zawartość pliku
                    try {
                        disc.saveOnDisc(Parameters[0], Parameters[1]);
                        System.out.println("Pomyślnie zapełniono plik!");
                    } catch (FileAlreadyExistsException e) {
                        System.out.println("Plik o nazwie \"" + Parameters[0] + " \" istnieje. I nie jest otwarty w trybie edycji!");
                    } catch (NoSuchFileException e) {
                        System.out.println("Nie można wyświetlić. Plik o nazwie \"" + Parameters[0] + " \" nie istnieje.");
                    } catch (IOException e) {
                        System.out.println("Wystąpił problem podczas edycji pliku: " + e.getMessage());
                    }
                    break;
                }
                case "AF": { ///Dopisz treść do pliku
                    try {
                        disc.appendFile(Parameters[0], Parameters[1]);
                        System.out.println("Dane " + Parameters[1] + " zostaly pomyslnie dopisane");
                    } catch (OpenDataException e) {
                        System.out.println(e.getMessage());
                    } catch (NoSuchFileException e) {
                        System.out.println("Plik o nazwie \"" + Parameters[0] + " \" nie istnieje.");
                    } catch (IOException e) {
                        System.out.println("Wystąpił problem podczas edycji pliku: " + e.getMessage());
                    }
                    break;
                }
                case "MV": { ///Edytuj nazwę pliku
                    try {
                        disc.moveFile(Parameters[0], Parameters[1]);
                    } catch (OpenDataException e) {
                        System.out.println(e.getMessage());
                    } catch (NoSuchFileException e) {
                        System.out.println("Plik o nazwie \"" + Parameters[0] + " \" nie istnieje.");
                    } catch (IOException e) {
                        System.out.println("Wystąpił problem podczas edycji nazwy pliku: " + e.getMessage());
                    }
                    break;
                }

                case "CF": { ///Zamknij plik
                    if (disc.closeFile(Parameters[0])) {
                        System.out.println("Plik " + Parameters[0] + " zostal‚ pomyslnie zamkniety.");
                    } else {
                        System.out.println("Plik u" + Parameters[0] + " nie udalo sie zamknac!");
                    }
                    break;
                }

                case "SC": { ///Wyswietl tablice Fat
                    disc.showClusters();
                    break;
                }

                case "SD": { ///Wyswietl tablice plików
                    try {
                        disc.showDirectories();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }

                case "PP": { ///Wyswietl plik
                    try {
                        disc.showFile(Parameters[0]);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }

                case "PA": { ///Wyswietl wszystkie procesy
                    ProcesManagerr.show_all();

                    break;
                }

                case "SB": { ///Wyswietl konkretna liczbe bitow
                    int a;
                    try {
                        a = Integer.parseInt(Parameters[1]);
                        disc.showFileWithSpecifiedLength(Parameters[0], a);
                    } catch (NumberFormatException e) {
                        System.out.println("Podana wielkość jest nieprawidłowa. Parametry muszą być liczbami. Nastąpi teraz przejście do następnej komendy.");
                        break;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }

                case "DB": { ///Wyswietlanie bloku dyskowego
                    int a;
                    try {
                        a = Integer.parseInt(Parameters[0]);
                        disc.showDiscBlock(a);
                    } catch (NoSuchFileException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }

                case "SP": { ///Wyswietl coś tam szymona
                    Communication.show_all();
                    break;
                }
                case "PI": {
                    System.out.println("MUTEX LOCKS:");
                    disc.pokazZamki();
                    Communication.pokazZamki();
                    break;
                }

                case "CW": {
                    Processor.czywypisac();
                    break;
                }

                case "HP": {

                    System.out.println("OKNO POMOCY:");
                    System.out.println(" ");
                    System.out.println("CP nazwa program.txt  -  Utworz proces");
                    System.out.println("DP nazwa              -  Usun proces");
                    System.out.println("BC nazwa              -  Pokaz blok procesu");
                    System.out.println("PA                    -  Pokaz wszystkie procesy");
                    System.out.println("TS nazwa              -  Pokaz tablice stronic danego procesu");
                    System.out.println("GO                    -  Wykonaj rozkaz");
                    System.out.println("MC 0 128              -  Pokaz pamiec w przediale od 0 do 128");
                    System.out.println("SS                    -  Pokaz kolejke drugiej szansy");
                    System.out.println("MF nazwa              -  Utworz plik");
                    System.out.println("SE nazwa              -  Otworz plik w trybie edycji");
                    System.out.println("DF nazwa              -  Usun plik");
                    System.out.println("AF nazwa zawartosc    -  Dopisz do pliku");
                    System.out.println("MV nazwaS nazwaN      -  Zmien nazwe pliku");
                    System.out.println("CF nazwa              -  Zamknij plik");
                    System.out.println("SC                    -  Wyswietl tablice FAT");
                    System.out.println("SD                    -  Wyswietl pliki z informcja o dlugosci");
                    System.out.println("PP nazwa              -  Wyswietl plik");
                    System.out.println("SB nazwa liczba       -  Wyswietl liczbe bitow pliku");
                    System.out.println("SP                    -  Wyswietl potoki");
                    System.out.println("PI                    -  Wyswietl zamki");
                    System.out.println("CW                    -  Wyswietl liste PCB procesora");
                    System.out.println("EX                    -  Wyjdz z systemu");
                    System.out.println("--------------------------------------------------------------");

                    break;
                }
                case "EX": { ///Zamknij system
                    System.out.println("Dziekujemy za skorzystanie z systemu!");
                    System.out.println("Do widzenia!");
                    System.in.read();
                    Memory.clearFile();
                    System.exit(0);
                }
            }
            System.out.println("");
            System.out.println("");
            /////////////////////////////////Koniec komendy
            cokolwiek = false;
            while (cokolwiek == false) {
                System.out.println("Co chcesz teraz zrobic? ");
                System.out.println("Wciśnij E, jeśli chcesz wyjść z programu.");
                System.out.println("Wciśnij H, jeśli chcesz zobaczyć listę komend.");
                System.out.println("Wciśnij N, jeśli chcesz przejść do następnej komendy ze skryptu.");
                System.out.println("Wciśnij B, jeśli chcesz podać nową kolejną komendę.");
                System.out.println("Wciśnij V, jeśli chcesz powtórzyć ostatnią komendę: " + Command);

                NewCom = console.next();
                NewCom = NewCom.toUpperCase();
                if (NewCom.length() == 1) {

                    switch (NewCom.charAt(0)) {
                        case 'B':
                            cokolwiek = true;
                            System.out.println("Proszę podać kolejną komendę oraz jej parametry.");
                            Scanner odczyt = new Scanner(System.in);
                            line = odczyt.nextLine();
                            ///line = line.toUpperCase();
                            break;
                        case 'N':
                            cokolwiek = true;
                            if (in.hasNextLine()) {
                                line = in.nextLine();
                            } else {
                                System.out.println("Nie ma więcej komend do wykonania! Wyświetlimy okno pomocy!");
                                line = "HP";
                            }
                            break;
                        case 'E':
                            cokolwiek = true;
                            System.exit(0);
                        case 'H':
                            cokolwiek = true;
                            System.out.println("TU MA BYC HELP");
                            line = "HP";
                            break;
                        case 'V':
                            cokolwiek = true;
                            line = line;
                            break;
                        default:
                            System.out.println("Niepoprawna komenda!!!!!!!!");
                            cokolwiek = false;
                            break;
                    }
                } else {

                    System.out.println("Niepoprawna komenda!");
                    cokolwiek = false;
                }

                System.out.println("");
                System.out.println("");
            }
        }

        System.out.println("Nieoczekiwany błąd! System teraz się wyłączy!");
//            Memory.clearFile();
        /// System.in.read();
    }

}
