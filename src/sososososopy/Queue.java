package sososososopy;

import java.util.LinkedList;

public class Queue {

    private String name;
    private Boolean readOpen;
    private Boolean writeOpen;
    private LinkedList<String> fifo;
    public MutexLocks lock;

    public Queue(String name, MutexLocks locks) {
        this.setName(name);
        this.setReadOpen(true);
        this.setWriteOpen(true);
        this.lock = locks;
        setFifo(new LinkedList<String>());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<String> getFifo() {
        return fifo;
    }

    public void setFifo(LinkedList<String> fifo) {
        this.fifo = fifo;
    }

    public Boolean getWriteOpen() {
        return writeOpen;
    }

    public void setWriteOpen(Boolean writeOpen) {
        this.writeOpen = writeOpen;
    }

    public Boolean getReadOpen() {
        return readOpen;
    }

    public void setReadOpen(Boolean readOpen) {
        this.readOpen = readOpen;
    }
}
