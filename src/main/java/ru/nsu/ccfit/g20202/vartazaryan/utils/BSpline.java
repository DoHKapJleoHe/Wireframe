package ru.nsu.ccfit.g20202.vartazaryan.utils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class BSpline
{
    private final double[][] M = {{-1/6, 3/6, -3/6, 1/6}, {3/6, -6/6, 3/6, 0}, {-3/6, 0, 3/6, 0}, {1/6, 4/6, 1/6, 0}};
    @Getter
    private final List<Point> anchorPoints = new ArrayList<>();

    public BSpline()
    {
        addAnchorPoint(new Point(2, 5));
    }

    public void addAnchorPoint(Point point)
    {
        anchorPoints.add(point);
    }
}
