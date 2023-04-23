package ru.nsu.ccfit.g20202.vartazaryan.bsplineeditor;

import ru.nsu.ccfit.g20202.vartazaryan.utils.BSpline;
import ru.nsu.ccfit.g20202.vartazaryan.utils.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static ru.nsu.ccfit.g20202.vartazaryan.utils.Constants.INDENT;
import static ru.nsu.ccfit.g20202.vartazaryan.utils.Constants.RADIUS;

public class SplinePanel extends JPanel implements MouseMotionListener, MouseListener
{
    // при зуме этот отсутп должен будет умножаться на коэффициент зума
    private BSpline bSpline;

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

        int centerX = getWidth()/2;
        int centerY = getHeight()/2;
        for(Point p : points)
        {
            g.drawOval(centerX + p.getX() - RADIUS/2, centerY - p.getY() - RADIUS/2, RADIUS, RADIUS);
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
        int centerX = getWidth()/2;
        int centerY = getHeight()/2;

        switch (curAction)
        {
            case POINT_MOVING -> {
                System.out.println("Moving");
                var curPoint = bSpline.getAnchorPoints().get(activePointIndex);
                curPoint.setX(e.getX() - centerX);
                curPoint.setY(centerY - e.getY());
            }
        }

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

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

        System.out.println("curX= " + curX + " curY= " + curY);

        int i = 0;
        for(Point p : anchorPoints)
        {
            int globalX = p.getX() + centerX;
            int globalY = centerY - p.getY();

            System.out.println("GlobalX= "+globalX+" GlobalY= "+globalY);

            if(curX <= globalX+RADIUS && curX >= globalX-RADIUS && curY <= globalY+RADIUS && curY >= globalY-RADIUS)
            {
                activePointIndex = i;
                curAction = Action.POINT_MOVING;
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

    enum Action
    {
        POINT_MOVING,
        CANVAS_MOVING,
        NOTHING
    }
}
