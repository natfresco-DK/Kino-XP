package com.example.KinoXP.utils;

public enum ScreenType {

    SMALL(20, 12),
    LARGE(25, 16);

    private final int rows;
    private final int seatsPerRow;

    ScreenType(int rows, int seatsPerRow) {
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
    }

    public int getRows() {
        return rows;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }
}