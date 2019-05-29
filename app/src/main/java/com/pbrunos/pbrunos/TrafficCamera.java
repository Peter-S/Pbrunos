package com.pbrunos.pbrunos;

public class TrafficCamera {

    String label;
    String image;
    String owner;
    double[] corrdinats;

    public TrafficCamera(String label, String image, String owner, double[] corrdinats) {
        this.label = label;
        this.image = image;
        this.owner = owner;
        this.corrdinats = corrdinats;
    }
}
