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

    public Vector4 multiplyMatrixVector(Vector4 point)
    {
        double[] pointArr = new double[]{point.getX(), point.getY(), point.getZ(), point.getW()};
        double[] result = new double[4];
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                result[i] += data[i][j] * pointArr[j];
            }
        }

        return new Vector4(result[0] / result[3], result[1] / result[3], result[2]/result[3], 1.0);
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

    public Matrix multiplyMatrixMatrix(Matrix matrix)
    {
        Matrix result = new Matrix(new double[][]{
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });

        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                for(int k = 0; k < 4; k++)
                {
                    result.data[i][j] += data[i][k] * matrix.data[k][j];
                }

            }
        }

        return result;
    }

    @Override
    public String toString()
    {
        return data[0][0] + " " + data[0][1] + " " + data[0][2] + " " + data[0][3] + "\n" +
               data[1][0] + " " + data[1][1] + " " + data[1][2] + " " + data[1][3] + "\n" +
               data[2][0] + " " + data[2][1] + " " + data[2][2] + " " + data[2][3] + "\n" +
               data[3][0] + " " + data[3][1] + " " + data[3][2] + " " + data[3][3] + "\n";
    }
}
