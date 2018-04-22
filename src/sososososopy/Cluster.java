package sososososopy;

public class Cluster {

    private int clusterID;
    private int nextClusterID;
    private String data;

    public Cluster(int clusterID, int nextClusterID, String data) {
        this.clusterID = clusterID;
        this.nextClusterID = nextClusterID;
        this.data = data;
    }

    public int getClusterID() {
        return clusterID;
    }

    public int getNextClusterID() {
        return nextClusterID;
    }

    public String getData() {
        return data;
    }
}
