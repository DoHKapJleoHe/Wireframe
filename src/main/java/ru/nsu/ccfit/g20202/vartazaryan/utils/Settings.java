package ru.nsu.ccfit.g20202.vartazaryan.utils;

import lombok.Setter;

public class Settings
{
    /**
     * This constant represents length of a single segment
     * of coordinate plane in pixels
     */
    public static final int DEFAULT_INDENT = 30;

    /**
     * This constant represents radius of anchor point circle in pixels
     */
    public static final int RADIUS = 10;

    /**
     * This is number of longitudinal generatrix
     */
    @Setter
    public static int GENERATRIX_NUM = 5;

    @Setter
    public static int CIRCLES_NUM = 4;

    @Setter
    public static int CIRCLES_ACCURACY = 3;

    /**
     * The number of segments into which the segment [0, 1] will be divided
     */
    public static int SEGMENTS_NUM = 10;
}
