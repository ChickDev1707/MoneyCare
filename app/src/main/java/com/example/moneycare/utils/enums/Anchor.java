package com.example.moneycare.utils.enums;

import java.util.Locale;


public enum Anchor {

    AUTO("auto"),
    CENTER("center"),
    CENTER_BOTTOM("center-bottom"),
    CENTER_TOP("center-top"),
    LEFT_BOTTOM("left-bottom"),
    LEFT_CENTER("left-center"),
    LEFT_TOP("left-top"),
    RIGHT_BOTTOM("right-bottom"),
    RIGHT_CENTER("right-center"),
    RIGHT_TOP("right-top");

    private final String value;

    Anchor(String value) {
        this.value = value;
    }

    public String getJsBase() {
        return String.format(Locale.US, "\"%s\"", value);
    }
}