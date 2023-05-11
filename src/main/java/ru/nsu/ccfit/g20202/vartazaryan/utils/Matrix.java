package ru.nsu.ccfit.g20202.vartazaryan.utils;

public class Matrix
{
    private double[][] data;
    private int width;
    private int height;

    public Matrix(double[][] matrix)
    {
        this.width = matrix[0].length;
        this.height = matrix.length;
        this.data = new double[matrix.length][matrix[0].length];

        for(int i = 0; i < matrix.length; i++)
        {
            for(int j  = 0; j < matrix[0].length; j++)
            {
                this.data[i][j] = matrix[i][j];
            }
        }
    }

    public double[] multiplyMatrixVector(double[] vector)
    {
        double[] result = new double[vector.length];

        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                result[i] += data[i][j] * vector[j];
            }
        }

        return result;
    }
}
