package ru.nsu.ccfit.g20202.vartazaryan.utils;

import lombok.Getter;
import lombok.Setter;

public class Point2D
{
    @Setter
    @Getter
    private double x;
    @Setter
    @Getter
    private double y;

    public Point2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
}
