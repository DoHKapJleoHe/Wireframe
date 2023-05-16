package ru.nsu.ccfit.g20202.vartazaryan.wireframe;

import ru.nsu.ccfit.g20202.vartazaryan.bsplineeditor.BSpline;
import ru.nsu.ccfit.g20202.vartazaryan.utils.Matrix;
import ru.nsu.ccfit.g20202.vartazaryan.utils.Vector4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import static ru.nsu.ccfit.g20202.vartazaryan.utils.Settings.GENERATRIX_NUM;

public class WireframePanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener
{
    private Wireframe wireframe;
    private BSpline bSpline;
    private List<Vector4> cubePoints = new ArrayList<>();

    private Point prevPoint;

    public WireframePanel(BSpline spline)
    {
        this.bSpline = spline;
        this.wireframe = new Wireframe();
        setBackground(Color.WHITE);

        cubePoints.add(new Vector4(1.0, 1.0, 1.0, 1.0));
        cubePoints.add(new Vector4(1.0, 1.0, -1.0, 1.0));
        cubePoints.add(new Vector4(1.0, -1.0, 1.0, 1.0));
        cubePoints.add(new Vector4(1.0, -1.0, -1.0, 1.0));
        cubePoints.add(new Vector4(-1.0, 1.0, 1.0, 1.0));
        cubePoints.add(new Vector4(-1.0, 1.0, -1.0, 1.0));
        cubePoints.add(new Vector4(-1.0, -1.0, 1.0, 1.0));
        cubePoints.add(new Vector4(-1.0, -1.0, -1.0, 1.0));

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(4));

        Matrix wireframeTransform = wireframe.getTranslateMatrix();
        Matrix cameraTransform = wireframe.getCameraTranslateMatrix();
        Matrix cameraPerspectiveMatrix = wireframe.getCameraPerspectiveMatrix();
        Matrix rotationMatrix = wireframe.getRotationMatrix();
        List<Vector4> wireframePoints = new ArrayList<>();

        System.out.println("Transform");
        System.out.println(wireframeTransform.toString());
        System.out.println("Rotate");
        System.out.println(rotationMatrix.toString());

        int centerX = getWidth()/2;
        int centerY = getHeight()/2;

        for(Vector4 v : cubePoints)
        {
            Vector4 planePoint;

            planePoint = wireframeTransform.multiplyMatrixVector(v);
            //planePoint = rotationMatrix.multiplyMatrixVector(planePoint);
            planePoint = cameraTransform.multiplyMatrixVector(planePoint);
            planePoint = cameraPerspectiveMatrix.multiplyMatrixVector(planePoint);


            wireframePoints.add(planePoint);
            //g2d.drawOval((int) (centerX + planePoint.getX()*200), (int) (centerY - planePoint.getY()*200), 5, 5);
        }

        for(int i = 0; i < cubePoints.size(); i++)
        {
            for(int j = i + 1; j < cubePoints.size(); j++)
            {
                int diff = 0;
                if(Math.abs(cubePoints.get(i).getX() - cubePoints.get(j).getX()) == 2)
                    diff++;
                if(Math.abs(cubePoints.get(i).getY() - cubePoints.get(j).getY()) == 2)
                    diff++;
                if(Math.abs(cubePoints.get(i).getZ() - cubePoints.get(j).getZ()) == 2)
                    diff++;

                if (diff == 1)
                    g2d.drawLine(
                            (int) (centerX + wireframePoints.get(i).getX()*200),
                            (int) (centerY - wireframePoints.get(i).getY()*200),
                            (int) (centerX + wireframePoints.get(j).getX()*200),
                            (int) (centerY - wireframePoints.get(j).getY()*200)
                    );
            }
        }


        /*var points = bSpline.getSplinePoints();
        if(points == null)
            return;

        double angle = (double)360/GENERATRIX_NUM;
        double curAngle = 0.0;

        for(int i = 0; i < GENERATRIX_NUM; i++)
        {
            Point prevPoint = null;
            Point curPoint = null;
            curAngle = i * angle;

            for(Point p : points)
            {
                curPoint = wireframe.createWireframePoint(p);
            }
        }*/
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        prevPoint = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        wireframe.setRotationMatrix(prevPoint, e.getPoint());
        prevPoint = e.getPoint();

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }

    public void createWireframe() {
        repaint();
    }
}
