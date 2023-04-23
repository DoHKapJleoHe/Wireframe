package ru.nsu.ccfit.g20202.vartazaryan.utils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static ru.nsu.ccfit.g20202.vartazaryan.utils.Constants.INDENT;

public class BSpline
{

    @Getter
    private List<Point> anchorPoints = new ArrayList<>();

    public BSpline()
    {
        addAnchorPoint(new Point(2*INDENT, 50));
        addAnchorPoint(new Point(5*INDENT, 10));
    }

    public void addAnchorPoint(Point point)
    {
        anchorPoints.add(point);
    }

}
