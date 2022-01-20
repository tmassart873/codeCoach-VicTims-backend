package com.victims.codecoachvictimsbackend.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Role {
    //empty ArrayList to be replaced by features list
    COACHEE("Coachee", Feature.GET_USER_PROFILE),
    COACH("Coach"),
    ADMIN("Admin");

    private final String label;
    private final List<Feature> features;

    Role(String label, Feature... features) {
        this.label = label;
        this.features = Arrays.asList(features);
    }

    public String getLabel() {
        return label;
    }

    public List<Feature> getFeatures() {
        return features;
    }
}
