package ru.nsu.ccfit.g20202.vartazaryan.utils;

import lombok.Getter;
import lombok.Setter;

public class Vector4
{
    @Getter @Setter
    private double x;
    @Getter @Setter
    private double y;
    @Getter @Setter
    private double z;
    @Getter @Setter
    private double w;

    public Vector4(double x, double y, double z, double w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1;
    }

    public void normalize()
    {
        double norm = Math.sqrt(x*x + y*y + z*z);

        x /= norm;
        y /= norm;
        z /= norm;
    }

    public String toString()
    {
        return x + " " + y + " " + z + " " + w;
    }
}
