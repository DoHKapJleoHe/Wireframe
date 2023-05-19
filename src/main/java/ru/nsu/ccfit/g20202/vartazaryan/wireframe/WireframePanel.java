package ru.nsu.ccfit.g20202.vartazaryan.wireframe;

import ru.nsu.ccfit.g20202.vartazaryan.bsplineeditor.BSpline;
import ru.nsu.ccfit.g20202.vartazaryan.utils.Point2D;
import ru.nsu.ccfit.g20202.vartazaryan.utils.Vector4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WireframePanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener
{
    private Wireframe wireframe;
    private BSpline bSpline;

    private Point prevPoint;

    public WireframePanel(BSpline spline)
    {
        this.bSpline = spline;
        this.wireframe = new Wireframe();
        setBackground(Color.WHITE);


        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));

        int centerX = getWidth()/2;
        int centerY = getHeight()/2;

        if (bSpline.getAnchorPoint2DS().size() < 4 || bSpline.getSplinePoints() == null)
        {
            var cube = wireframe.getCube();
            var cubePoints = cube.get(0);
            var cubeEdges = cube.get(1);


            for(Point2D e : cubeEdges)
            {
                // e contains 2 points with coords meaning 2 points from cube points
                // e.getX() - first Point, e.getY() - second Point
                g2d.drawLine(
                        (int) (centerX + cubePoints.get((int) e.getX()).getX()*200), (int) (centerY - cubePoints.get((int) e.getX()).getY()*200),
                        (int) (centerX + cubePoints.get((int) e.getY()).getX()*200), (int) (centerY - cubePoints.get((int) e.getY()).getY()*200)
                );
            }
        }
        else
        {
            wireframe.createWireframePoints(bSpline.getSplinePoints());
            var points = wireframe.getWireframePoints();
            var edges = wireframe.getEdges();

            g2d.setColor(Color.BLACK);
            for(int i = 0; i < edges.size(); i +=2)
            {
                var p1 = points.get(edges.get(i));
                var p2 = points.get(edges.get(i+1));

                g2d.drawLine(
                        (int) (centerX + p1.getX()*200), (int) (centerY - p1.getY()*200),
                        (int) (centerX + p2.getX()*200), (int) (centerY - p2.getY()*200)
                );
            }
        }
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
        //wireframe.setNearClip(e.getWheelRotation());
        repaint();
    }

    public void createWireframe() {
        repaint();
    }
}
