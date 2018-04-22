/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sososososopy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Zarzadzanie_pamiecia {

    Running running;
    char[] RAM = new char[128];
    Stronica Tablica_str[];
    wolna_ramka[] freeFrames = new wolna_ramka[8];
    int file_adr = 0;
    int adr_of_previous = 0;
    SecondChance SecCh;

    public Zarzadzanie_pamiecia() {
        running = new Running();
//        this.Tablica_str = running.running.page_table;
        for (int i = 0; i < 8; i++) {
            freeFrames[i] = new wolna_ramka(true, 0);
        }
        SecCh = new SecondChance();
    }

    public void showFifo() {
        System.out.println("Kolejka drugiej szansy");
        for (Iterator<ob> iter = SecCh.getQueue().iterator(); iter.hasNext();) {
            ob data = iter.next();
            System.out.println("id " + data.id + " numer stronicy " + data.getPagenr() + " druga szansa " + data.getSC());
            //System.out.print(data.pagenr+"&"+data.SC+"&id"+data.id+" ");
        }
    }

    public void clearFile() {
        String FileName = "plik_wymiany.txt";
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(FileName, false);
        } catch (IOException ex) {
        }
        PrintWriter bufferedWriter
                = new PrintWriter(fileWriter, false);
        bufferedWriter.flush();
        bufferedWriter.close();
        try {
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(Zarzadzanie_pamiecia.class.getName()).log(Level.SEVERE, null, ex);
        }

        bufferedWriter.close();

    }

    public void saveFrameToDisc(char data[], int adr) throws IOException {

        RandomAccessFile f = new RandomAccessFile(new File("plik_wymiany.txt"), "rw");
        long aPositionWhereIWantToGo = adr;
        f.seek(aPositionWhereIWantToGo);
        String toWr = new String(data);
        f.write(toWr.getBytes());
        f.close();
    }

    public void freeMem() {
        this.Tablica_str = running.running.page_table;
        for (wolna_ramka a : freeFrames) {
            if (a.getId() == running.running.ID) {
                a.setIsFree(true);
            }
        }
        SecCh.clearFifo(running.running.ID);
        char tabl[] = new char[16];
        for (int i = 0; i < 16; i++) {
            tabl[i] = ' ';
        }
        int licznik = 0;
        for (Stronica a : running.running.page_table) {
            try {
                saveFrameToDisc(tabl, running.running.where_in_file + licznik);
            } catch (IOException ex) {
                Logger.getLogger(Zarzadzanie_pamiecia.class.getName()).log(Level.SEVERE, null, ex);
            }
            licznik += 16;
        }
    }

    public void loadToFile(String FileName, PCB b) {
        // System.out.print("loadtofile");
        save_to_disk(read_from_file(FileName, b));

    }

    public int getwhere_in_file() {
        return file_adr;
    }

    public char readMemory(int Adres_Logiczny) {
        int FreeFrame = 9;
        // System.out.println("id "+Running.running.ID+" adres " + Adres_Logiczny);
        this.Tablica_str = running.running.page_table;
        //this.Tablica_str = running.running.page_table;
        if (Adres_Logiczny >= running.running.how_long) {
            running.running.blad = true;
            return '$';
        } else {
            int nrStr = Adres_Logiczny / 16;
            Stronica[] aha = running.running.page_table;
            boolean isInRam = aha[nrStr].getV_or_i();
            if (isInRam == true) {
                int FrameNr = running.running.page_table[nrStr].getFrameNumber();
                int adrFiz = FrameNr * 16 + (Adres_Logiczny % 16);
                //  if (RamIsFull()) { //chyba to
                SecCh.make_space(nrStr, running.running.ID);
                //   }
                return RAM[adrFiz];
            } else {
                if (RamIsFull()) {
                    int adr_to_delete = 0;
                    ob victim = SecCh.make_space(nrStr, running.running.ID);
                    List<PCB> lista = running.running.all_PCB; //MILA NAPRAW TO KURWA
                    int lll = 0;
                    for (PCB m : lista) {
                        if (m.ID == victim.getId()) {
                            for (Stronica str : m.page_table) {
                                if (lll == victim.getPagenr()) {
                                    FreeFrame = m.page_table[lll].getFrameNumber();
                                    adr_to_delete = m.where_in_file + lll * 16;
                                    str.setV_or_i(false);
                                    break;
                                }
                                lll++;
                            }
                            break;
                        }
                    }
                    char wynik;
                    freeFrames[FreeFrame].setIsFree(false);

                    freeFrames[FreeFrame].setId(running.running.ID);
                    running.running.page_table[nrStr].setFrameNumber(FreeFrame);
                    running.running.page_table[nrStr].setV_or_i(true);
                    char tablica[] = getNewFrame(nrStr * 16);
                    char table_to_delete[] = new char[16];
                    for (int j = 0; j < 16; j++) {
                        table_to_delete[j] = RAM[j + (FreeFrame * 16)];
                        RAM[j + (FreeFrame * 16)] = tablica[j];
                    }
                    try {
                        saveFrameToDisc(table_to_delete, adr_to_delete);
                    } catch (IOException ex) {
                        Logger.getLogger(Zarzadzanie_pamiecia.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    wynik = RAM[(FreeFrame * 16) + Adres_Logiczny % 16];

                    return wynik;
                } else {
                    char wynik = ' ';
                    for (int i = 0; i < 8; i++) {
                        if (freeFrames[i].getisFree() == true) {
                            freeFrames[i].setIsFree(false);
                            SecCh.dodaj_do_kolejki(nrStr, running.running.ID);
                            freeFrames[i].setId(running.running.ID);
                            running.running.page_table[nrStr].setFrameNumber(i);
                            running.running.page_table[nrStr].setV_or_i(true);

                            char tablica[] = getNewFrame(nrStr * 16);
                            for (int j = 0; j < 16; j++) {
                                RAM[j + (i * 16)] = tablica[j];
                            }
                            wynik = RAM[(running.running.page_table[nrStr].getFrameNumber() * 16) + Adres_Logiczny % 16];
                            break;
                        }
                    }
                    return wynik;
                }
            }
        }

    }

    public void writeMemory(int Adres_Logiczny, char Value) {
        int FreeFrame = 9;
        //System.out.println("id "+Running.running.ID+" adres " + Adres_Logiczny);
        this.Tablica_str = running.running.page_table;
        if (Adres_Logiczny >= running.running.how_long) {
            running.running.blad = true;

        } else {
            int nrStr = Adres_Logiczny / 16;
            Stronica[] aha = running.running.page_table;
            boolean isInRam = aha[nrStr].getV_or_i();
            if (isInRam == true) {
                int FrameNr = running.running.page_table[nrStr].getFrameNumber();
                int adrFiz = FrameNr * 16 + (Adres_Logiczny % 16);
                //   if (RamIsFull()) {
                SecCh.make_space(nrStr, running.running.ID);
                //  }
                RAM[adrFiz] = Value;
            } else {
                if (RamIsFull()) {
                    int adr_to_delete = 0;
                    ob victim = SecCh.make_space(nrStr, running.running.ID);
                    List<PCB> lista = running.running.all_PCB;
                    int lll = 0;
                    for (PCB m : lista) {
                        if (m.ID == victim.getId()) {
                            for (Stronica str : m.page_table) {
                                if (lll == victim.getPagenr()) {
                                    FreeFrame = m.page_table[lll].getFrameNumber();
                                    adr_to_delete = m.where_in_file + lll * 16;
                                    str.setV_or_i(false);
                                    break;
                                }
                                lll++;
                            }
                            break;
                        }
                    }
                    char wynik;
                    freeFrames[FreeFrame].setIsFree(false);

                    freeFrames[FreeFrame].setId(running.running.ID);
                    running.running.page_table[nrStr].setFrameNumber(FreeFrame);
                    running.running.page_table[nrStr].setV_or_i(true);
                    char tablica[] = getNewFrame(nrStr * 16);
                    char table_to_delete[] = new char[16];
                    for (int j = 0; j < 16; j++) {
                        table_to_delete[j] = RAM[j + (FreeFrame * 16)];
                        RAM[j + (FreeFrame * 16)] = tablica[j];
                    }
                    try {
                        saveFrameToDisc(table_to_delete, adr_to_delete);
                    } catch (IOException ex) {
                        Logger.getLogger(Zarzadzanie_pamiecia.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    RAM[(FreeFrame * 16) + Adres_Logiczny % 16] = Value;

                } else {
                    char wynik = ' ';
                    for (int i = 0; i < 8; i++) {
                        if (freeFrames[i].getisFree() == true) {
                            freeFrames[i].setIsFree(false);
                            SecCh.dodaj_do_kolejki(nrStr, running.running.ID);
                            freeFrames[i].setId(running.running.ID);
                            running.running.page_table[nrStr].setFrameNumber(i);
                            running.running.page_table[nrStr].setV_or_i(true);

                            char tablica[] = getNewFrame(nrStr * 16);
                            for (int j = 0; j < 16; j++) {
                                RAM[j + (i * 16)] = tablica[j];
                            }
                            RAM[(running.running.page_table[nrStr].getFrameNumber() * 16) + Adres_Logiczny % 16] = Value;
                            break;
                        }
                    }

                }
            }
        }

    }

    public boolean RamIsFull() {

        for (int i = 0; i < 8; i++) {
            if (freeFrames[i].getisFree()) {
                return false;
            }

        }
        return true;
    }

    private char[] getNewFrame(int AdrLog) {
        int line;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("plik_wymiany.txt");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder x = null;
        try {
            x = new StringBuilder();
            int licznik1 = 0;
            int licznik2 = 0;
            while ((line = bufferedReader.read()) != -1) {
                if (licznik1 >= running.running.where_in_file + AdrLog) {
                    char ch = (char) line;
                    x.append(ch);

                    licznik2++;
                    if (licznik2 == 16) {
                        break;
                    }
                }
                licznik1++;
            }

            bufferedReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        char data[];
        String p;
        p = x.toString();
        data = p.toCharArray();
        return data;
    }

    public char[] getRAM(int adr, int size) throws Exception {
        if (adr > 127) {
            throw new Exception("zly adres");
        }
        if (size > 128) {
            throw new Exception("zly rozmiar");
        }
        if (adr + size > 128) {
            throw new Exception("zle parametry");
        }

        char t[] = new char[size];
        int licznik = 0;
        for (int i = 0; i < RAM.length; i++) {
            if (i >= adr) {
                t[licznik] = RAM[i];
                licznik++;
                if (licznik == size) {
                    break;
                }
            }

        }
        return t;
    }

    private void save_to_disk(char data[]) {
        String FileName = "plik_wymiany.txt";
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(FileName, true);
        } catch (IOException ex) {
        }
        BufferedWriter bufferedWriter
                = new BufferedWriter(fileWriter);

        for (int i = 0; i < data.length; i++) {

            try {

                bufferedWriter.write(data[i]);

            } catch (IOException ex) {
            }

        }

        try {
            bufferedWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(Stronicowanie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private char[] read_from_file(String plik, PCB b) {
        int line;
        int licznik = 0;
        b.where_in_file = (file_adr);
        adr_of_previous = file_adr;
        StringBuilder x = null;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(plik);
        } catch (FileNotFoundException ex) {
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        try {
            x = new StringBuilder();

            while ((line = bufferedReader.read()) != -1) {

                char ch = (char) line;
                if (ch != '\n' && ch != '\r') {
                    x.append(ch);
                    file_adr++;
                    licznik++;
                } else if (ch == '\n') {
                    x.append('#');
                    file_adr++;
                    licznik++;
                }
            }

            bufferedReader.close();
        } catch (IOException ex) {
        }
        int remaining = 16 - (file_adr % 16);
        if (remaining != 16) {////////dodane!!!!!!!!!!!!
            for (int i = 0; i < remaining; i++) {
                x.append(' ');
                file_adr++;
                licznik++;
            }
        }
        b.how_long = (licznik);

        char data[];
        String p;

        p = x.toString();

        data = p.toCharArray();

        if (data.length % 16 == 0) {
            b.table_size = (data.length / 16);
            Tablica_str = new Stronica[data.length / 16];
        } else {
            b.table_size = ((data.length / 16) + 1);
            Tablica_str = new Stronica[(data.length / 16) + 1];
        }
        b.page_table = Tablica_str;

        for (int i = 0; i < b.page_table.length; i++) {
            b.page_table[i] = new Stronica();
            b.page_table[i].setV_or_i(false);
        }

        return data;
    }
}
