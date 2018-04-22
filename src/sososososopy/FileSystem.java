package sososososopy;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.openmbean.OpenDataException;

public class FileSystem {

    public static final int FILE_OPEN = 1;
    public static final int FILE_EDIT = 2;

    private int discCapacity;
    private int clusterCapacity;
    private Map<String, Integer> fileList;
    private Map<Integer, Cluster> clusterList;
    private Map<Integer, Integer> lockedClusters;
    private ArrayList<String> editFiles;
    private ProcesManager manager;
    public static List<File> listaZamkow = new ArrayList<>();

    public FileSystem(int discCapacity, int clusterCapacity, ProcesManager manager) {
        this.discCapacity = discCapacity;
        this.clusterCapacity = clusterCapacity;
        this.manager = manager;
    }

    public FileSystem create() {
        this.fileList = new HashMap<>();
        this.clusterList = new HashMap<>();
        this.lockedClusters = new HashMap<>();
        this.editFiles = new ArrayList<>();

        for (int i = 0; i < this.discCapacity; i++) {
            this.clusterList.put(i, new Cluster(i, -1, null));
            this.lockedClusters.put(i, 0);
        }
        return this;
    }

    //---------------------------------------------------------------------------------------------------LOCKI------------------------------------------------------------
    private int znajdzPlik(String nazwa) {
        int i;
        for (i = 0; i < listaZamkow.size(); i++) {
            if (listaZamkow.get(i).getName().equals(nazwa)) {
                break;
            }
        }

        return i;
    }

    public void pokazZamki() {
        if (listaZamkow.isEmpty()) {
            //System.out.println("Brak zamkow zwiazanych z plikami!");
        } else {
            for (File a : listaZamkow) {
                a.displayZamki();
            }
        }
    }

    //--------------------------------------------------------------------------------------KoniecLockow------------------------------------------------------------------
    private ArrayList<String> chunkData(String data, int bits) {
        ArrayList<String> outputArr = new ArrayList<>();
        if (data.length() < bits) {
            outputArr.add(data);
        } else {
            int chunksCount = (int) Math.ceil(data.length() / bits); //.ceil?
            int lastIndex = 0;
            for (int i = 0; i < chunksCount; i++) {
                StringBuilder s = new StringBuilder();
                for (int x = 0; x < bits; x++) {
                    s.append(data.charAt(lastIndex));
                    lastIndex = lastIndex + 1;
                }
                outputArr.add(s.toString());
            }
            String rest = data.substring(chunksCount * bits);
            if (rest.length() > 0) {
                outputArr.add(rest);
            }
        }
        return outputArr;
    }

    private ArrayList<Integer> alocateFreeClusters(int clusters) throws IOException {
        ArrayList<Integer> freeSpace = new ArrayList<>();
        for (Map.Entry<Integer, Integer> pair : this.lockedClusters.entrySet()) {
            if (freeSpace.size() == clusters) {
                break;
            }
            if (pair.getValue() == 0) {
                freeSpace.add(pair.getKey()); //?
            }
        }
        if (freeSpace.size() < clusters) {
            throw new IOException("There is not enough space on the disc");
        }
        return freeSpace;
    }

    public void saveOnDisc(String fileName, String data) throws IOException, OpenDataException {

        if (this.fileList.containsKey(fileName)) {
            if (this.editFiles.indexOf(fileName) != -1) {
                File przejsciowy = listaZamkow.get(znajdzPlik(fileName));
                // First remove, then save new
                this.removeFile(fileName, false);
                this.saveOnDisc(fileName, data);
                listaZamkow.remove(znajdzPlik(fileName));
                listaZamkow.add(przejsciowy);
                // pokazZamki();
                // System.out.println("klam: 1.1");
            } else {
                //System.out.println("klam: 1.2");
                throw new FileAlreadyExistsException("This file already exists and it's not open in edit mode!!!");
            }
        } else {
            // Chunk data to cluster capacity arrays
            ArrayList<String> str = chunkData(data, this.clusterCapacity);
            // Check is free space on disc
            ArrayList<Integer> freeClusters = this.alocateFreeClusters(str.size());

            int actualClusterIndex = 0;
            for (String s : str) {
                int clusterId = freeClusters.get(actualClusterIndex);
                int nextClusterId = -1;
                if (actualClusterIndex + 1 < freeClusters.size()) {
                    nextClusterId = freeClusters.get(actualClusterIndex + 1);
                }
                this.clusterList.put(clusterId, new Cluster(clusterId, nextClusterId, s));
                this.lockedClusters.put(clusterId, 1);
                actualClusterIndex++;
            }
            this.fileList.put(fileName, freeClusters.get(0));

            //------------------------------------------LOCKI--------------------------------------------------------------------
            File file = new File(fileName, manager);
            listaZamkow.add(file); // dodaje sobie na moja liste nazwe tego plika i tam tworzy sie lock dopasowany do nazwy tego pliku
            //pokazZamki();
            //------------------------------------------ Koniec LOCKI--------------------------------------------------------------------
            //System.out.println("klam : 2");
        }
    }

    private boolean isFileExists(String fileName) {
        return this.fileList.containsKey(fileName);
    }

    private ArrayList<Cluster> buildClusters(ArrayList<Cluster> clusters, int startClusterId) {
        int nextClusterId = this.clusterList.get(startClusterId).getNextClusterID();
        if (nextClusterId != -1) {
            clusters.add(this.clusterList.get(startClusterId));
            return this.buildClusters(clusters, this.clusterList.get(startClusterId).getNextClusterID());
        } else if (nextClusterId == -1) {
            clusters.add(this.clusterList.get(startClusterId));
        }
        return clusters;
    }

    public boolean removeFile(String file, boolean withEditFiles) {
        if (isFileExists(file)) {
            int dir = fileList.get(file);
            ArrayList<Cluster> clusters = this.buildClusters(new ArrayList<>(), dir);
            for (Cluster c : clusters) {
                this.clusterList.put(c.getClusterID(), new Cluster(c.getClusterID(), -1, null));
                this.lockedClusters.put(c.getClusterID(), 0);
            }
            fileList.remove(file);
            if (withEditFiles && editFiles.indexOf(file) != -1) {

                editFiles.remove(editFiles.indexOf(file));

                try {
                    listaZamkow.get(znajdzPlik(file)).getLock().unlock(manager.get_proces(Running.running.name)); //unlockuje proces ktory otwarl plik1
                } catch (Exception ex) {

                }

            }

            //------------------------------------------LOCKI--------------------------------------------------------------------
            listaZamkow.remove(znajdzPlik(file)); // usuwa z mojej list plik o nazwie file i jego locka 

            //------------------------------------------Koniec LOCKI--------------------------------------------------------------------
            return true;
        }
        return false;
    }

    public boolean removeFile(String file) {
        return removeFile(file, true);
    }

    public String readFile(String fileName) throws NoSuchFileException, OpenDataException {
        if (this.isFileExists(fileName)) {
            ArrayList<Cluster> clusters = this.buildClusters(new ArrayList<>(), this.fileList.get(fileName));
            StringBuilder output = new StringBuilder();
            for (Cluster c : clusters) {
                output.append(c.getData());
            }
            return output.toString();
        } else {
            Running.running.blad = true;
            throw new NoSuchFileException("There is no file with name: " + fileName);

        }
    }

    public String readFile(String fileName, int type) throws NoSuchFileException, OpenDataException {

        if (type == FILE_EDIT) {
            isFileOpenToEdit(fileName);
            if (!listaZamkow.isEmpty()) {
                try {
                    // Dodanie locka na plik

                    listaZamkow.get(znajdzPlik(fileName)).getLock().lock(manager.get_proces(Running.running.name)); // LOCK--------------------------------------------------------
                } catch (Exception ex) {
                    Logger.getLogger(FileSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.editFiles.add(fileName);
            System.out.println("Plik >" + fileName + "< zostal otwarty w trybie edycji");
            //pokazZamki();
        }

        return this.readFile(fileName);
    }

    public boolean appendFile(String file, String data) throws IOException, OpenDataException {
        String oldData = readFile(file);
        saveOnDisc(file, oldData.concat(data));

        return true;
    }

    public boolean moveFile(String fileName, String newFileName) throws OpenDataException, NoSuchFileException, IOException {

        if (this.editFiles.indexOf(fileName) == -1) {
            // First remove, then save new
            File temp = listaZamkow.get(znajdzPlik(fileName)); // zapamietuje stan zamka
            String data = this.readFile(fileName);
            this.removeFile(fileName);
            this.saveOnDisc(newFileName, data);

            System.out.println("Nazwa " + fileName + " zostala zmieniona na " + newFileName);

            //------------------------------------------LOCKI--------------------------------------------------------------------
            listaZamkow.get(znajdzPlik(newFileName)).zamek = temp.zamek;  // rozumiem ze tu jest zmiana nazwy pliku wiec ja tez ja musze sb zmienic :)

            //------------------------------------------Koniec LOCKI--------------------------------------------------------------------
            return true;

        } else {
            throw new OpenDataException("This file " + fileName + " is now open in edit mode!");
        }
    }

    public boolean closeFile(String fileName) {
        if (this.editFiles.indexOf(fileName) != -1) {
            this.editFiles.remove(this.editFiles.indexOf(fileName));

            //------------------------------------------LOCKI--------------------------------------------------------------------
            try {
                listaZamkow.get(znajdzPlik(fileName)).getLock().unlock(manager.get_proces(Running.running.name)); //unlockuje proces ktory otwarl plik1
            } catch (Exception ex) {

            }
            //------------------------------------------Koniec LOCKI--------------------------------------------------------------------
            return true;
        }
        return false;
    }

    private boolean isFileOpenToEdit(String fileName) throws OpenDataException {
        if (this.editFiles.indexOf(fileName) != -1) {
            try {

                listaZamkow.get(znajdzPlik(fileName)).getLock().lock(manager.get_proces(Running.running.name));
            } catch (Exception ex) {
                Logger.getLogger(FileSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
            throw new OpenDataException("This file " + fileName + " is already open!");
        }
        return false;
    }

    public int getDiscCapacity() {
        return discCapacity;
    }

    public int getClusterCapacity() {
        return clusterCapacity;
    }

    public void showClusters() {
        for (Map.Entry<Integer, Cluster> c : this.clusterList.entrySet()) {
            System.out.println("Cluster number: " + c.getValue().getClusterID() + " next cluster: " + c.getValue().getNextClusterID() + " with data: " + c.getValue().getData());
        }
    }

    public void showDirectories() throws NoSuchFileException, OpenDataException {
        if (!this.fileList.isEmpty()) {
            for (Map.Entry<String, Integer> c : this.fileList.entrySet()) {
                System.out.println("Filename: " + c.getKey() + " length: " + this.readFile(c.getKey()).length() + " start cluster: " + c.getValue());
            }
        } else {
            throw new NoSuchFileException("There are no files to show");
        }
    }

    public void showFile(String fileName) throws NoSuchFileException, OpenDataException {

        System.out.println("File name: " + fileName + " with data: " + readFile(fileName));
    }

    public void showFileWithSpecifiedLength(String fileName, int bits) throws NoSuchFileException, OpenDataException {
        if (this.isFileExists(fileName)) {
            for (Map.Entry<String, Integer> c : this.fileList.entrySet()) {
                if (fileName == null ? c.getKey() == null : fileName.equals(c.getKey())) {
                    System.out.println("Filename: " + c.getKey() + " with data: " + this.readFile(fileName).subSequence(0, bits));
                    if (bits > fileName.length()) {
                        System.out.println();
                        System.out.println("There are not that many bits. Anyway the data is: " + this.readFile(fileName));
                    }
                }
            }
        } else {
            throw new NoSuchFileException("There is no such file to show");
        }
    }

    public void showDiscBlock(int blockNumber) throws NoSuchFileException {
        if (!clusterList.isEmpty()) {
            for (Map.Entry<Integer, Cluster> c : this.clusterList.entrySet()) {
                if (c.getKey() == blockNumber && c.getValue().getData() == null) {
                    System.out.println("There is no data in that block");
                } else if (c.getKey() == blockNumber) {
                    System.out.println("Cluster number: " + c.getValue().getClusterID() + " with data: " + c.getValue().getData());
                }
            }
        } else {
            throw new NoSuchFileException("There is no data in that block");
        }
    }
}
