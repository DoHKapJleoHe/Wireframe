package ru.nsu.ccfit.g20202.vartazaryan.utils;

import lombok.Getter;
import lombok.Setter;

public class Point
{
    @Setter
    @Getter
    private double x;
    @Setter
    @Getter
    private double y;

    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
}
