package com.liemily.reportgeneration.domain;

/**
 * Created by Emily Li on 18/08/2017.
 */
public enum REPORT_NAME {
    STOCK_REPORT;

    @Override
    public String toString() {
        String[] splitName = super.toString().toLowerCase().split("_");
        StringBuilder name = new StringBuilder(splitName[0]);
        for (int i = 1; i < splitName.length; i++) {
            String firstLetter = Character.toString(splitName[i].charAt(0));
            name.append(firstLetter.toUpperCase()).append(splitName[i].substring(1, splitName[i].length()));
        }
        return name.toString();
    }
}
