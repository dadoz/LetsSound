package com.application.letssound.models;

/**
 * Created by davide on 11/02/16.
 */
public class Setting {
    private final boolean enabledStatus;
    private String label;
    private String description;

    public Setting(String s, String description, boolean b) {
        label = s;
        description = description;
        enabledStatus = b;

    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}
