package com.example.planto;

public class Plant {
    private String commonName;
    private String scientificName;
    private String imageUrl;
    private String sunlight;
    private String other_name;
    private String cycle;
    private String watering;

    public Plant(String commonName, String scientificName, String imageUrl, String sunlight, String other_name, String cycle, String watering) {
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.sunlight = sunlight;
        this.other_name = other_name;
        this.imageUrl = imageUrl;
        this.cycle = cycle;
        this.watering = watering;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String sunlight() {
        return sunlight;
    }

    public String other_name() {
        return other_name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String cycle() {
        return cycle;
    }

    public String watering() {
        return watering;
    }
}