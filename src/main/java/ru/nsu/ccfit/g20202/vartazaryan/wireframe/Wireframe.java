package ru.nsu.ccfit.g20202.vartazaryan.wireframe;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.ccfit.g20202.vartazaryan.utils.Matrix;
import ru.nsu.ccfit.g20202.vartazaryan.utils.Point2D;
import ru.nsu.ccfit.g20202.vartazaryan.utils.Vector4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.nsu.ccfit.g20202.vartazaryan.utils.Settings.*;

public class Wireframe
{
    @Getter
    private Matrix rotationMatrix;
    @Getter
    private Matrix cameraTranslateMatrix;
    @Getter
    private Matrix cameraPerspectiveMatrix;
    @Getter
    private Matrix translateMatrix;

    private Matrix normalizeMatrix;

    private double nearClip = 5.0;
    private final double rotationAngle = 5.0;

    private double Sh;
    private double Sw;

    @Getter
    private List<Vector4> wireframePoints;
    @Getter
    private List<Integer> edges;

    public Wireframe()
    {
        this.rotationMatrix = new Matrix(new double[][]{
                {1.0, 0, 0, 0},
                {0, 1.0, 0, 0},
                {0, 0, 1.0, 0},
                {0, 0, 0, 1.0}
        });

        setTranslateMatrix();
        setCameraTranslateMatrix();
        setCameraPerspectiveMatrix();
    }

    public void setRotationMatrix(java.awt.Point p1, java.awt.Point p2)
    {
        // firstly here i calculate axis and then i find normal-vector to the axis by changing x,y-coords to -(x,y-coords)
        Vector4 axis = new Vector4(-(p2.y - p1.y), -(p2.x - p1.x), 0, 1.0);
        if(axis.getX() == 0 && axis.getY() == 0)
            return;

        axis.normalize();

        double cos = Math.cos(Math.toRadians(rotationAngle));
        double sin = Math.sin(Math.toRadians(rotationAngle));
        double x = axis.getX(),
                y = axis.getY(),
                z = axis.getZ();

        Matrix rotation = new Matrix(new double[][]{
                {cos+(1-cos)*x*x, (1-cos)*x*y-sin*z, (1-cos)*x*z+sin*y, 0},
                {(1-cos)*y*x+sin*z, cos+(1-cos)*y*y, (1-cos)*y*z-sin*x, 0},
                {(1-cos)*z*x-sin*y, (1-cos)*z*y+sin*x, cos+(1-cos)*z*z, 0},
                {0, 0, 0, 1.0}
        });

        this.translateMatrix = rotation.multiplyMatrixMatrix(translateMatrix);

        rotationMatrix = rotation.multiplyMatrixMatrix(rotationMatrix);
    }

    public void setCameraTranslateMatrix()
    {
        this.cameraTranslateMatrix = new Matrix(new double[][]{
                {1.0, 0, 0, 0},
                {0, 1.0, 0, 0},
                {0, 0, 1.0, 10.0},
                {0, 0, 0, 1.0}
        });
    }

    public void setTranslateMatrix()
    {
        this.translateMatrix = new Matrix(new double[][]{
                {1.0, 0, 0, 0},
                {0, 1.0, 0, 0},
                {0, 0, 1.0, 0},
                {0, 0, 0, 1.0}
        });
    }

    public void setCameraPerspectiveMatrix()
    {
        cameraPerspectiveMatrix = new Matrix(new double[][]{
                {2 * nearClip, 0, 0, 0},
                {0, 2 * nearClip, 0, 0},
                {0, 0, 1.0, 0},
                {0, 0, 1.0, 0}
        });
    }

    private void setNormalizeMatrix()
    {
        double maxX = wireframePoints.get(0).getX(), minX = wireframePoints.get(0).getX();
        double maxY = wireframePoints.get(0).getY(), minY = wireframePoints.get(0).getY();
        double maxZ = wireframePoints.get(0).getZ(), minZ = wireframePoints.get(0).getZ();

        for(Vector4 v : wireframePoints)
        {
            if(v.getX() > maxX)
                maxX = v.getX();
            if(v.getY() > maxY)
                maxY = v.getY();
            if(v.getZ() > maxZ)
                maxZ = v.getZ();

            if(v.getX() < minX)
                minX = v.getX();
            if(v.getY() < minY)
                minY = v.getY();
            if(v.getZ() < minZ)
                minZ = v.getZ();
        }

        Sw = maxX;
        Sh = maxY;

        double distX = maxX - minX;
        double distY = maxY - minY;
        double distZ = maxZ - minZ;

        double res = Math.max(distX, Math.max(distY, distZ));
        if(res == 0.0)
            res = 1.0;

        normalizeMatrix = new Matrix(new double[][]{
                {1.0/res, 0, 0, 0},
                {0, 1.0/res, 0, 0},
                {0, 0, 1.0/res, 0},
                {0, 0, 0, 1.0}
        });
    }

    public void createWireframePoints(List<Point2D> bSplinePoints)
    {
        wireframePoints = new ArrayList<>();
        edges = new ArrayList<>();

        double angle = (double)360/(GENERATRIX_NUM*CIRCLES_ACCURACY);
           double curAngle,
                cos,
                sin;
        for(int i = 0; i < GENERATRIX_NUM*CIRCLES_ACCURACY; i++)
        {
            curAngle = i * angle;
            cos = Math.cos(Math.toRadians(curAngle));
            sin = Math.sin(Math.toRadians(curAngle));

            for(Point2D p : bSplinePoints)
            {
                Vector4 newPoint = new Vector4(p.getY()*cos, p.getY()*sin, p.getX(), 1.0);

                Vector4 planePoint = translateMatrix.multiplyMatrixVector(newPoint);
                planePoint = cameraTranslateMatrix.multiplyMatrixVector(planePoint);
                //planePoint = cameraPerspectiveMatrix.multiplyMatrixVector(planePoint);

                wireframePoints.add(planePoint);
            }
        }

        setNormalizeMatrix();

        wireframePoints.replaceAll(point -> normalizeMatrix.multiplyMatrixVector(point));

        int N = bSplinePoints.size();
        for(int i = 0; i < GENERATRIX_NUM*CIRCLES_ACCURACY; i += CIRCLES_ACCURACY)
        {
            for(int j = 0; j < N-1; j++)
            {
                edges.add(j + i*N);
                edges.add(j + 1 + i*N);
            }
        }

        for(int i = 0; i < GENERATRIX_NUM*CIRCLES_ACCURACY; i++)
        {
            edges.add(i*N);
            edges.add(((i+1)%(GENERATRIX_NUM*CIRCLES_ACCURACY))*N);
        }

        for(int i = 0; i < GENERATRIX_NUM*CIRCLES_ACCURACY; i++)
        {
            edges.add(i*N + N-1);
            edges.add(((i+1)%(GENERATRIX_NUM*CIRCLES_ACCURACY))*N + N-1);
        }

        if(CIRCLES_NUM > 2)
        {
            int step = N / (CIRCLES_NUM-1);

            for(int i = 1; i <= CIRCLES_NUM-2; i++)
            {
                for(int j = 0; j < GENERATRIX_NUM*CIRCLES_ACCURACY; j++)
                {
                    edges.add(j*N + i*step);
                    edges.add(((j+1)%(GENERATRIX_NUM*CIRCLES_ACCURACY))*N + i*step);
                }
            }
        }
    }

    public Map<Integer, List<Point2D>> getCube()
    {
        List<Vector4> cubePoints = new ArrayList<>();
        cubePoints.add(new Vector4(1.0, 1.0, 1.0, 1.0));
        cubePoints.add(new Vector4(1.0, 1.0, -1.0, 1.0));
        cubePoints.add(new Vector4(1.0, -1.0, 1.0, 1.0));
        cubePoints.add(new Vector4(1.0, -1.0, -1.0, 1.0));
        cubePoints.add(new Vector4(-1.0, 1.0, 1.0, 1.0));
        cubePoints.add(new Vector4(-1.0, 1.0, -1.0, 1.0));
        cubePoints.add(new Vector4(-1.0, -1.0, 1.0, 1.0));
        cubePoints.add(new Vector4(-1.0, -1.0, -1.0, 1.0));

        List<Point2D> wireframePoints = new ArrayList<>();
        for (Vector4 v : cubePoints)
        {
            Vector4 planePoint;

            planePoint = translateMatrix.multiplyMatrixVector(v);
            planePoint = cameraTranslateMatrix.multiplyMatrixVector(planePoint);
            planePoint = cameraPerspectiveMatrix.multiplyMatrixVector(planePoint);


            wireframePoints.add(new Point2D(planePoint.getX(), planePoint.getY()));
        }

        List<Point2D> edges = new ArrayList<>();
        for (int i = 0; i < cubePoints.size(); i++)
        {
            for (int j = i + 1; j < cubePoints.size(); j++)
            {
                int diff = 0;
                if (Math.abs(cubePoints.get(i).getX() - cubePoints.get(j).getX()) == 2)
                    diff++;
                if (Math.abs(cubePoints.get(i).getY() - cubePoints.get(j).getY()) == 2)
                    diff++;
                if (Math.abs(cubePoints.get(i).getZ() - cubePoints.get(j).getZ()) == 2)
                    diff++;

                if (diff == 1)
                    edges.add(new Point2D(i, j));
            }
        }

        Map<Integer, List<Point2D>> cube = new HashMap<>();
        cube.put(0, wireframePoints);
        cube.put(1, edges);

        return cube;
    }
}
