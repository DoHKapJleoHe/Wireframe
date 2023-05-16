package ru.nsu.ccfit.g20202.vartazaryan.wireframe;

import com.sun.source.tree.UsesTree;
import lombok.Getter;
import lombok.Setter;
import ru.nsu.ccfit.g20202.vartazaryan.utils.Matrix;
import ru.nsu.ccfit.g20202.vartazaryan.utils.Point;
import ru.nsu.ccfit.g20202.vartazaryan.utils.Vector4;

public class Wireframe
{
    @Getter
    private Matrix rotationMatrix;
    @Getter
    private Matrix cameraTranslateMatrix;
    @Getter
    private Matrix cameraPerspectiveMatrix;
    private Matrix scaleMatrix;
    @Getter
    private Matrix translateMatrix;

    @Setter
    private double nearClip = 2.0;
    private final double rotationAngle = 5.0;

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
        // firstly here i calculate axis and then i find normal-vector to the axis by changing y-coord to -(y-coord)
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

    private void setScaleMatrix(double sx, double sy, double sz)
    {
        this.scaleMatrix = new Matrix(new double[][]{
                {sx, 0, 0, 0},
                {0, sy, 0, 0},
                {0, 0, sz, 0},
                {0, 0, 0, 1.0}
        });
    }

    public void setCameraTranslateMatrix()
    {
        this.cameraTranslateMatrix = new Matrix(new double[][]{
                {1.0, 0, 0, 0},
                {0, 1.0, 0, 0},
                {0, 0, 1.0, 5.0},
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
                {nearClip, 0, 0, 0},
                {0, nearClip, 0, 0},
                {0, 0, 1.0, 0},
                {0, 0, 1.0, 0}
        });
    }

    public Point createWireframePoint(Point curPoint)
    {
        Vector4 newPoint = new Vector4(curPoint.getX(), curPoint.getY(), 1, 1);

        //var res = perspectiveMatrix.multiplyMatrixMatrix(translateMatrix);


        return null;
    }
}
