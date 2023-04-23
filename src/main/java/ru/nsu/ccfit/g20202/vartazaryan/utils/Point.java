package ru.nsu.ccfit.g20202.vartazaryan.utils;

import lombok.Getter;
import lombok.Setter;

public class Point
{
    @Setter
    @Getter
    private int x;
    @Setter
    @Getter
    private int y;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}
