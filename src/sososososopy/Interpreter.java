/*
 * 
 */
package sososososopy;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jedrzej Magdans AD - dodawanie wartości rejestrów SB - odejmowanie
 * wartości rejestrów AX - dodawanie liczby do rejestru MO - wstawienie liczby
 * do rejestru IN - zwiększenie wartości rejestru o 1 DC - zmniejszenie wartości
 * rejestru o 1 MU - mnożenie wartości rejestrów MX - Mnożenie rejestru przez
 * podaną liczbę MV - zmiana wartości rejestru x na wartość rejestru y JP - skok
 * JE- skok dopóki rejA!=0 MF- tworzenie pliku AF- dopisywanie do pliku EF-
 * edycja pliku DF-usuwanie pliku CX - wysyłanie wiadomości CD- odczytywanie
 * wiadomości ilość znaczków pobrana z rozkazu CL- - || - rejestru UP- tworzenie
 * kolejki DP- usuwanie kolejki NP- rozkaz pusty WM - zapisywanie do rejestrów
 * HL-koniec programu
 *
 */
public class Interpreter {

    private int ProcesID = 0;
    int licznik = 0;
    int flaga;
    int licznik2 = 0;

    String[] rozkaz = new String[4];
    private Zarzadzanie_pamiecia zarz;
    private ProcesManager manager;
    private FileSystem disc;
    private Communication communication;
    private Running running;

    public Interpreter(Communication com, Zarzadzanie_pamiecia zarz, ProcesManager manager, FileSystem disc) {
        this.communication = com;
        this.disc = disc;
        this.zarz = zarz;
        this.manager = manager;
        running = new Running();
    }

    public Boolean program(String rozkaz[], int ProcesID) throws IOException, Exception {

        // Rozkazy arytmetyczne
        if (rozkaz[0].equals("AD")) {
            //  System.out.println("dodano rejestr B");

            if (rozkaz[1].equals("A")) {
                if (rozkaz[2].equals("B")) {
                    running.running.R1 += running.running.R2;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("C")) {
                    running.running.R1 += running.running.R3;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("D")) {
                    running.running.R1 += running.running.R4;
                    manager.increase_counters();
                }
            } else if (rozkaz[1].equals("B")) {
                if (rozkaz[2].equals("A")) {
                    running.running.R2 += running.running.R1;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("C")) {
                    running.running.R2 += running.running.R3;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("D")) {
                    running.running.R2 += running.running.R4;
                    manager.increase_counters();
                }
            } else if (rozkaz[1].equals("C")) {
                if (rozkaz[2].equals("A")) {
                    running.running.R3 += running.running.R1;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("B")) {
                    running.running.R3 += running.running.R2;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("D")) {
                    running.running.R3 += running.running.R4;
                    manager.increase_counters();
                }
            } else {
                if (rozkaz[2].equals("A")) {
                    running.running.R4 += running.running.R1;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("B")) {
                    running.running.R4 += running.running.R2;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("C")) {
                    running.running.R4 += running.running.R3;
                    manager.increase_counters();
                }
            }

            return true;

        } else if (rozkaz[0].equals("SB")) {
            //   System.out.println("odejmowanie");
            if (rozkaz[1].equals("A")) {
                if (rozkaz[2].equals("B")) {
                    running.running.R1 -= running.running.R2;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("C")) {
                    running.running.R1 -= running.running.R3;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("D")) {
                    running.running.R1 -= running.running.R4;
                    manager.increase_counters();
                }
            } else if (rozkaz[1].equals("B")) {
                if (rozkaz[2].equals("A")) {
                    running.running.R2 -= running.running.R1;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("C")) {
                    running.running.R2 -= running.running.R3;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("D")) {
                    running.running.R2 -= running.running.R4;
                    manager.increase_counters();
                }
            } else if (rozkaz[1].equals("C")) {
                if (rozkaz[2].equals("A")) {
                    running.running.R3 -= running.running.R1;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("B")) {
                    running.running.R3 -= running.running.R2;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("D")) {
                    running.running.R3 -= running.running.R4;
                    manager.increase_counters();
                }
            } else {
                if (rozkaz[2].equals("A")) {
                    running.running.R4 -= running.running.R1;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("B")) {
                    running.running.R4 -= running.running.R2;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("C")) {
                    running.running.R4 -= running.running.R3;
                    manager.increase_counters();
                }
            }

            return true;
        } else if (rozkaz[0].equals("AX")) {
            if (rozkaz[1].equals("A")) {
                running.running.R1 += Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            } else if (rozkaz[1].equals("B")) {
                running.running.R2 += Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            } else if (rozkaz[1].equals("C")) {
                running.running.R3 += Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            } else {
                running.running.R4 += Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            }

            return true;
        } else if (rozkaz[0].equals("AX")) {
            if (rozkaz[1].equals("A")) {
                running.running.R1 += Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            } else if (rozkaz[1].equals("B")) {
                running.running.R2 += Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            } else if (rozkaz[1].equals("C")) {
                running.running.R3 += Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            } else {
                running.running.R4 += Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            }

            return true;
        } else if (rozkaz[0].equals("MO")) {
            // System.out.println("wpisano");
            if (rozkaz[1].equals("A")) {
                running.running.R1 = Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            } else if (rozkaz[1].equals("B")) {
                running.running.R2 = Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            } else if (rozkaz[1].equals("C")) {
                running.running.R3 = Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            } else {
                running.running.R4 = Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            }

            return true;
        } else if (rozkaz[0].equals("IN")) {
            if (rozkaz[1].equals("A")) {
                running.running.R1 += 1;
                manager.increase_counters();
            } else if (rozkaz[1].equals("B")) {
                running.running.R2 += 1;
                manager.increase_counters();
            } else if (rozkaz[1].equals("C")) {
                running.running.R3 += 1;
                manager.increase_counters();
            } else {
                running.running.R4 += 1;
                manager.increase_counters();
            }

            return true;

        } else if (rozkaz[0].equals("DC")) {
            if (rozkaz[1].equals("A")) {
                running.running.R1 -= 1;

                manager.increase_counters();
            } else if (rozkaz[1].equals("B")) {
                running.running.R2 -= 1;

                manager.increase_counters();
            } else if (rozkaz[1].equals("C")) {
                running.running.R3 -= 1;

                manager.increase_counters();
            } else {
                running.running.R4 -= 1;

                manager.increase_counters();
            }

            return true;

        } else if (rozkaz[0].equals("MU")) {
            //System.out.println("mnozenie");
            if (rozkaz[1].equals("A")) {
                if (rozkaz[2].equals("B")) {
                    running.running.R1 *= running.running.R2;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("C")) {
                    running.running.R1 *= running.running.R3;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("D")) {
                    running.running.R1 *= running.running.R4;
                    manager.increase_counters();
                }
            } else if (rozkaz[1].equals("B")) {
                if (rozkaz[2].equals("A")) {
                    running.running.R2 *= running.running.R1;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("C")) {
                    running.running.R2 *= running.running.R3;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("D")) {
                    running.running.R2 *= running.running.R4;
                    manager.increase_counters();
                }
            } else if (rozkaz[1].equals("C")) {
                if (rozkaz[2].equals("A")) {
                    running.running.R3 *= running.running.R1;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("B")) {
                    running.running.R3 *= running.running.R2;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("D")) {
                    running.running.R3 *= running.running.R4;
                    manager.increase_counters();
                }
            } else {
                if (rozkaz[2].equals("A")) {
                    running.running.R4 *= running.running.R1;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("B")) {
                    running.running.R4 *= running.running.R2;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("C")) {
                    running.running.R4 *= running.running.R3;
                    manager.increase_counters();
                }
            }

            return true;
        } else if (rozkaz[0].equals("LO")) {
            if (rozkaz[1].equals("A")) {
                running.running.R1 = Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            } else if (rozkaz[1].equals("B")) {
                running.running.R2 = Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            } else if (rozkaz[1].equals("C")) {
                running.running.R3 = Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            } else {
                running.running.R4 = Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            }

            return true;
        } else if (rozkaz[0].equals("MX")) {
            if (rozkaz[1].equals("A")) {
                running.running.R1 *= Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            } else if (rozkaz[1].equals("B")) {
                running.running.R2 *= Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            } else if (rozkaz[1].equals("C")) {
                running.running.R3 *= Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            } else {
                running.running.R4 *= Integer.parseInt(rozkaz[2]);
                manager.increase_counters();
            }

            return true;

        } else if (rozkaz[0].equals("MV")) {
            if (rozkaz[1].equals("A")) {
                if (rozkaz[2].equals("B")) {
                    running.running.R1 = running.running.R2;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("C")) {
                    running.running.R1 = running.running.R3;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("D")) {
                    running.running.R1 = running.running.R4;
                    manager.increase_counters();
                }
            } else if (rozkaz[1].equals("B")) {
                if (rozkaz[2].equals("A")) {
                    running.running.R2 = running.running.R1;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("C")) {
                    running.running.R2 = running.running.R3;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("D")) {
                    running.running.R2 = running.running.R4;
                    manager.increase_counters();
                }
            } else if (rozkaz[1].equals("C")) {
                if (rozkaz[2].equals("A")) {
                    running.running.R3 = running.running.R1;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("B")) {
                    running.running.R3 = running.running.R2;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("D")) {
                    running.running.R3 = running.running.R4;
                    manager.increase_counters();
                }
            } else {
                if (rozkaz[2].equals("A")) {
                    running.running.R4 = running.running.R1;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("B")) {
                    running.running.R4 = running.running.R2;
                    manager.increase_counters();
                } else if (rozkaz[2].equals("C")) {
                    running.running.R4 = running.running.R3;
                    manager.increase_counters();
                }
            }

            return true;
        } else if (rozkaz[0].equals("JP")) {
            running.running.licznik_aktualny = Integer.parseInt(rozkaz[1]);
            manager.increase_counters();
            return true;
        } else if (rozkaz[0].equals("JE") && running.running.R1 == 0) {

            running.running.licznik_aktualny = Integer.parseInt(rozkaz[1]);
            manager.increase_counters();

            return true;
        }

        switch (rozkaz[0]) {
            case "MF":
                //    System.out.println("tworzenie i zapisywanie pliku");
                disc.saveOnDisc(rozkaz[1], "");
                //String str;
                //str= Integer.toString(running.running.R2);
                //disc.saveOnDisc(rozkaz[1], str);
                manager.increase_counters();
                // disc.pokazZamki(); // lockijii ------------------------------------------------------------------------------------
                return true;
            case "AF"://dopisz do pliku

                disc.appendFile(rozkaz[1], rozkaz[2]);
                //  disc.pokazZamki(); // locki------------------------------------------------------------------------------------
                return true;
            case "OF"://open file
                disc.readFile(rozkaz[1], FileSystem.FILE_EDIT);
                //  disc.pokazZamki(); // locki-----------------------------------------------------------------------------------
                return true;
            case "SR":
                String wiadomosc1 = rozkaz[1];
                for (int i = 0; i < wiadomosc1.length(); i++) {
                    char c = wiadomosc1.charAt(i);
                    zarz.writeMemory(Integer.parseInt(rozkaz[2]) + i, c);
                }
                return true;
            case "EF":
                String rejestr1;
                String rejestr2;
                String rejestr3;
                String rejestr4;
                rejestr1 = Integer.toString(running.running.R1);
                rejestr2 = Integer.toString(running.running.R2);
                rejestr3 = Integer.toString(running.running.R3);
                rejestr4 = Integer.toString(running.running.R4);
                if (rozkaz[2].equals("A")) {

                    disc.appendFile(rozkaz[1], rejestr1);
                } else if (rozkaz[2].equals("B")) {

                    disc.appendFile(rozkaz[1], rejestr2);
                } else if (rozkaz[2].equals("C")) {

                    disc.appendFile(rozkaz[1], rejestr3);
                } else if (rozkaz[2].equals("D")) {

                    disc.appendFile(rozkaz[1], rejestr4);
                }

                manager.increase_counters();

                //  disc.pokazZamki(); //locki---------------------------------------------------------------------------------------------
                return true;

            case "DF":
                disc.removeFile(rozkaz[1]);
                manager.increase_counters();
                // disc.pokazZamki();
                return true;
            case "CF":
                disc.closeFile(rozkaz[1]);
                return true;
            case "RF":
                disc.showFile(rozkaz[1]);
                //disc.showClusters();
                return true;

            case "CP": {
                try {
                    manager.new_proces(rozkaz[1], 0, rozkaz[2]);
                } catch (Exception ex) {
                    System.out.println("Nie udalo utworzyc sie procesu!");
                }
            }
            manager.increase_counters();
            return true;
            case "CX":
                communication.write(rozkaz[1], rozkaz[2]);

                return true;
            case "CD":
                communication.read(rozkaz[1], Integer.parseInt(rozkaz[2]), Integer.parseInt(rozkaz[3]));
                manager.increase_counters();
                String wiadomosc = communication.getMsgToSend();
                for (int i = 0; i < wiadomosc.length(); i++) {
                    char c = wiadomosc.charAt(i);
                    zarz.writeMemory(Integer.parseInt(rozkaz[3]) + i, c);

                }
                return true;
            case "CL":
                if (rozkaz[2].equals("A")) {
                    if (rozkaz[3].equals("B")) {
                        communication.read(rozkaz[1], running.running.R1, running.running.R2);
                        manager.increase_counters();

                        wiadomosc = communication.getMsgToSend();
                        if (wiadomosc != null) {
                            for (int i = 0; i < wiadomosc.length(); i++) {
                                char c = wiadomosc.charAt(i);
                                zarz.writeMemory(running.running.R2 + i, c);
                            }
                        }
                    } else if (rozkaz[3].equals("C")) {
                        communication.read(rozkaz[1], running.running.R1, running.running.R3);
                        manager.increase_counters();
                        wiadomosc = communication.getMsgToSend();

                        if (wiadomosc != null) {

                            for (int i = 0; i < wiadomosc.length(); i++) {
                                char c = wiadomosc.charAt(i);
                                zarz.writeMemory(running.running.R3 + i, c);

                            }
                        }
                    } else if (rozkaz[3].equals("D")) {
                        communication.read(rozkaz[1], running.running.R1, running.running.R4);
                        manager.increase_counters();
                        wiadomosc = communication.getMsgToSend();

                        if (wiadomosc != null) {
                            for (int i = 0; i < wiadomosc.length(); i++) {
                                char c = wiadomosc.charAt(i);
                                zarz.writeMemory(running.running.R4 + i, c);

                            }
                        }
                    }

                } else if (rozkaz[2].equals("B")) {
                    if (rozkaz[3].equals("A")) {
                        communication.read(rozkaz[1], running.running.R2, running.running.R1);
                        manager.increase_counters();
                        wiadomosc = communication.getMsgToSend();
                        if (wiadomosc != null) {
                            for (int i = 0; i < wiadomosc.length(); i++) {
                                char c = wiadomosc.charAt(i);
                                zarz.writeMemory(running.running.R1 + i, c);

                            }
                        }
                    } else if (rozkaz[3].equals("C")) {
                        communication.read(rozkaz[1], running.running.R2, running.running.R3);
                        manager.increase_counters();
                        wiadomosc = communication.getMsgToSend();
                        if (wiadomosc != null) {
                            for (int i = 0; i < wiadomosc.length(); i++) {
                                char c = wiadomosc.charAt(i);
                                zarz.writeMemory(running.running.R3 + i, c);
                            }
                        }
                    } else if (rozkaz[3].equals("D")) {
                        communication.read(rozkaz[1], running.running.R2, running.running.R4);
                        manager.increase_counters();
                        wiadomosc = communication.getMsgToSend();
                        if (wiadomosc != null) {
                            for (int i = 0; i < wiadomosc.length(); i++) {
                                char c = wiadomosc.charAt(i);
                                zarz.writeMemory(running.running.R4 + i, c);
                            }
                        }

                    }
                } else if (rozkaz[2].equals("C")) {
                    if (rozkaz[3].equals("B")) {
                        communication.read(rozkaz[1], running.running.R3, running.running.R2);
                        manager.increase_counters();
                        wiadomosc = communication.getMsgToSend();
                        if (wiadomosc != null) {
                            for (int i = 0; i < wiadomosc.length(); i++) {
                                char c = wiadomosc.charAt(i);
                                zarz.writeMemory(running.running.R2 + i, c);

                            }
                        }
                    } else if (rozkaz[3].equals("A")) {
                        communication.read(rozkaz[1], running.running.R3, running.running.R1);
                        manager.increase_counters();
                        wiadomosc = communication.getMsgToSend();
                        if (wiadomosc != null) {
                            for (int i = 0; i < wiadomosc.length(); i++) {
                                char c = wiadomosc.charAt(i);
                                zarz.writeMemory(running.running.R1 + i, c);
                            }
                        }
                    } else if (rozkaz[3].equals("D")) {
                        communication.read(rozkaz[1], running.running.R1, running.running.R4);
                        manager.increase_counters();
                        wiadomosc = communication.getMsgToSend();
                        if (wiadomosc != null) {
                            for (int i = 0; i < wiadomosc.length(); i++) {
                                char c = wiadomosc.charAt(i);
                                zarz.writeMemory(running.running.R4 + i, c);

                            }
                        }
                    }
                } else {
                    if (rozkaz[3].equals("B")) {
                        communication.read(rozkaz[1], running.running.R4, running.running.R2);
                        manager.increase_counters();
                        wiadomosc = communication.getMsgToSend();
                        if (wiadomosc != null) {
                            for (int i = 0; i < wiadomosc.length(); i++) {
                                char c = wiadomosc.charAt(i);
                                zarz.writeMemory(running.running.R2 + i, c);
                            }
                        }
                    } else if (rozkaz[3].equals("C")) {
                        communication.read(rozkaz[1], running.running.R4, running.running.R3);
                        manager.increase_counters();
                        wiadomosc = communication.getMsgToSend();
                        if (wiadomosc != null) {
                            for (int i = 0; i < wiadomosc.length(); i++) {
                                char c = wiadomosc.charAt(i);
                                zarz.writeMemory(running.running.R3 + i, c);
                            }
                        }
                    } else if (rozkaz[3].equals("A")) {
                        communication.read(rozkaz[1], running.running.R4, running.running.R1);
                        manager.increase_counters();
                        wiadomosc = communication.getMsgToSend();
                        if (wiadomosc != null) {
                            for (int i = 0; i < wiadomosc.length(); i++) {
                                char c = wiadomosc.charAt(i);
                                zarz.writeMemory(running.running.R1 + i, c);
                            }
                        }
                    }
                }
                return true;

            case "UP":
                communication.createQueue(rozkaz[1]);
                return true;

            case "DP":
                communication.deleteQueue(rozkaz[1]);
                return true;

            case "SA"://show all 
                communication.show_all();

                return true;
            case "NP":
                return true;
            case "WM":
                char c = rozkaz[2].charAt(0);
                zarz.writeMemory(Integer.parseInt(rozkaz[1]), c);
                return true;
            case "HL":

                manager.set_state(Running.running.ID, 2);

                manager.increase_counters();
                return true;

            default:
                return false;
        }

    }

    public void GO() throws Exception {

        char x = 0;
        int i = 0;
        if (Running.running.flaga == 0) {
            running.running.licznik2 = running.running.licznik_aktualny;
        } else {
            running.running.licznik_aktualny = running.running.licznik2;
            Running.running.flaga = 0;
        }
        rozkaz[i] = "";
        System.out.println();
        do {

            if (zarz.readMemory(running.running.licznik_aktualny) != ' ') {
                if (running.running.blad == true) {
                    System.out.println("Coś poszło nie tak! Proces został zakończony.");
                    manager.set_state(Running.running.ID, 2);

                    manager.increase_counters();
                    break;
                }
                rozkaz[i] += zarz.readMemory(running.running.licznik_aktualny);

                System.out.print(zarz.readMemory(running.running.licznik_aktualny));

                x = zarz.readMemory(running.running.licznik_aktualny + 1);

                running.running.licznik_aktualny++;

            } else {
                i++;
                running.running.licznik_aktualny++;

                rozkaz[i] = "";
            }

        } while (x != '#');
        System.out.println();
        running.running.licznik_aktualny++;
        //try {

        program(rozkaz, 1);
        //} catch (IOException ex) {
        //  System.out.println("excecption");
        //}
    }

}
