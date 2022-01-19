package com.victims.codecoachvictimsbackend.security;

import java.util.ArrayList;
import java.util.List;

public enum Role {
    //empty ArrayList to be replaced by features list
    COACHEE("coachee", new ArrayList<>()),
    COACH("coach", new ArrayList<>()),
    ADMIN("admin", new ArrayList<>());

    private final String label;
    private final List<Feature> features;

    Role(String label, List<Feature> features) {
        this.label = label;
        this.features = features;
    }

    public String getLabel() {
        return label;
    }

    public List<Feature> getFeatures() {
        return features;
    }
}
