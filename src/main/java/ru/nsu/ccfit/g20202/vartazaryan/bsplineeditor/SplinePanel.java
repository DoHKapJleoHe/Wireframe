package ru.nsu.ccfit.g20202.vartazaryan.bsplineeditor;

import ru.nsu.ccfit.g20202.vartazaryan.utils.BSpline;
import ru.nsu.ccfit.g20202.vartazaryan.utils.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static ru.nsu.ccfit.g20202.vartazaryan.utils.Constants.*;

public class SplinePanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener
{
    // при зуме этот отсутп должен будет умножаться на коэффициент зума
    private BSpline bSpline;

    private double zoom = 1.0;
    private int INDENT = DEFAULT_INDENT;

    private int curX;
    private int curY;
    private int activePointIndex;
    private Action curAction = Action.NOTHING;

    public SplinePanel(BSpline spline)
    {
        setPreferredSize(new Dimension(640, 240));
        setBackground(Color.BLACK);

        bSpline = spline;

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    private void drawAxis(Graphics g)
    {
        g.setColor(Color.WHITE);
        g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
        g.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());

        int height = getHeight();
        int width = getWidth();

        int linesNumber = (width/2) / INDENT;
        for(int i = 1; i <= linesNumber; i++)
        {
            g.drawLine(width/2 - INDENT*i, height/2 - 3, width/2 - INDENT*i, height/2 + 3);
            g.drawLine(width/2 + INDENT*i, height/2 - 3, width/2 + INDENT*i, height/2 + 3);
        }

        linesNumber = (height/2) / INDENT;
        for(int i = 1; i <= linesNumber; i++)
        {
            g.drawLine(width/2 - 3, height/2 - INDENT*i, width/2 + 3, height/2 - INDENT*i);
            g.drawLine(width/2 - 3, height/2 + INDENT*i, width/2 + 3, height/2 + INDENT*i);
        }
    }

    private void drawSplineAnchorPoints(Graphics g)
    {
        g.setColor(Color.GREEN);
        var points = bSpline.getAnchorPoints();

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        for (Point p : points)
        {
            g.drawOval((int) (centerX + p.getX() * INDENT - RADIUS / 2), (int) (centerY - p.getY() * INDENT - RADIUS / 2), RADIUS, RADIUS);
        }

        g.setColor(Color.WHITE);
        for(int i = 0; i < points.size() - 1; i++)
        {
            Point p1 = points.get(i);
            Point p2 = points.get(i+1);
            g.drawLine((int)( centerX + p1.getX()*INDENT), (int)(centerY - p1.getY()*INDENT),
                       (int)( centerX + p2.getX()*INDENT), (int)(centerY - p2.getY()*INDENT));
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        drawAxis(g);
        drawSplineAnchorPoints(g);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        double centerX = getWidth()/2;
        double centerY = getHeight()/2;

        switch (curAction)
        {
            case POINT_MOVING -> {
                var curPoint = bSpline.getAnchorPoints().get(activePointIndex);
                curPoint.setX((e.getX() - centerX)/INDENT);
                curPoint.setY((centerY - e.getY())/INDENT);
            }
            case CANVAS_MOVING -> {

            }
        }

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        double centerX = getWidth()/2;
        double centerY = getHeight()/2;

        bSpline.addAnchorPoint(new Point((e.getX() - centerX)/INDENT, (centerY - e.getY())/INDENT));
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        curX = e.getX();
        curY = e.getY();

        checkPoint();
    }

    private void checkPoint()
    {
        var anchorPoints = bSpline.getAnchorPoints();
        int centerX = getWidth()/2;
        int centerY = getHeight()/2;

        int i = 0;
        for(Point p : anchorPoints)
        {
            double globalX = p.getX()*INDENT + centerX;
            double globalY = centerY - p.getY()*INDENT;

            if(curX <= globalX+RADIUS && curX >= globalX-RADIUS && curY <= globalY+RADIUS && curY >= globalY-RADIUS)
            {
                System.out.println("Point!");
                activePointIndex = i;
                curAction = Action.POINT_MOVING;
                break;
            }
            i++;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        curAction = Action.NOTHING;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int koef = e.getWheelRotation();
        INDENT -= koef;
        repaint();
    }

    enum Action
    {
        POINT_MOVING,
        CANVAS_MOVING,
        NOTHING
    }
}
