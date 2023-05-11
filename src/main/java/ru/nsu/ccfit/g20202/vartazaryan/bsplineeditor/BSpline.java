package ru.nsu.ccfit.g20202.vartazaryan.bsplineeditor;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.ccfit.g20202.vartazaryan.utils.Matrix;
import ru.nsu.ccfit.g20202.vartazaryan.utils.Point;

import java.util.ArrayList;
import java.util.List;


public class BSpline
{
    /**
     * The number of segments into which the segment [0, 1] will be divided
     */
    @Getter
    @Setter
    private int segmentsNum = 10;
    private final Matrix splineMatrix;

    /**
     * Points that will help approximating b-spline
     */
    @Getter
    private final List<Point> anchorPoints = new ArrayList<>();

    /**
     * This is points that will approximate b-spline piece
     */
    @Getter
    private ArrayList<Point> splinePoints;

    public BSpline()
    {
        splineMatrix = new Matrix(new double[][]{
                {-1.0/6, 3.0/6, -3.0/6, 1.0/6},
                {3.0/6, -6.0/6, 3.0/6, 0},
                {-3.0/6, 0, 3.0/6, 0},
                {1.0/6, 4.0/6, 1.0/6, 0}
        });
    }

    public void addAnchorPoint(Point point)
    {
        anchorPoints.add(point);
        createBSpline();
    }

    public void createBSpline()
    {
        splinePoints = new ArrayList<>(); //each time we create b-spline we have to create new array of points
        int anchorPointsNum = anchorPoints.size();

        double step = (double)1/segmentsNum; // step between each part of [0, 1] segment
        double x, y;

        if(anchorPointsNum < 4)
            return;

        for(int i = 1; i < anchorPointsNum - 2; i++)
        {
            /*
                i-th part ob b-spline can be built by 4 anchor points P(i-1), P(i), P(i+1), P(i+2):

                r_i = T * M * G -> T = [t^3, t^2, t, 1], 't' is a number from [0, 1]
                                -> M - is spline matrix
                                -> G = [P(i-1), P(i), P(i+1), P(i+2)]

                Firstly i multiply M with G (matrix multiplication is associative).
                To multiply them i separated x-coords and y-coords from each P and then separately
                multiplied with M.
            */
            double[] xCoords = {
                    anchorPoints.get(i-1).getX(),
                    anchorPoints.get(i).getX(),
                    anchorPoints.get(i+1).getX(),
                    anchorPoints.get(i+2).getX()
            };

            double[] yCoords = {
                    anchorPoints.get(i-1).getY(),
                    anchorPoints.get(i).getY(),
                    anchorPoints.get(i+1).getY(),
                    anchorPoints.get(i+2).getY()
            };

            double[] xVector = splineMatrix.multiplyMatrixVector(xCoords);
            double[] yVector = splineMatrix.multiplyMatrixVector(yCoords);

            /*
                Now i multiply T with the result vectors of M*G.
                If we multiply T vector we will get this:
                    t^3 * vector[0] + t^2 * vector[1] + t * vector[2] + vector[3]
                I take each number from [0, 1] division and calculate this polynomial(for x-coords and y-coords)
            */
            double t = 0.0;
            for(int k = 0; k < segmentsNum; k++)
            {
                t = 0.0 + k*step;
                x = t*t*t*xVector[0] + t*t*xVector[1] + t*xVector[2] + xVector[3];
                y = t*t*t*yVector[0] + t*t*yVector[1] + t*yVector[2] + yVector[3];

                splinePoints.add(new Point(x, y));
            }
        }
    }
}
