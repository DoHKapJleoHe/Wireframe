package ru.nsu.ccfit.g20202.vartazaryan.bsplineeditor;

import ru.nsu.ccfit.g20202.vartazaryan.utils.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static ru.nsu.ccfit.g20202.vartazaryan.utils.Settings.*;

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

    private final JScrollPane scrollPane = new JScrollPane();
    private int width = 640;
    private int height = 240;

    public SplinePanel(BSpline spline)
    {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);

        bSpline = spline;
        scrollPane.setMaximumSize(new Dimension(width, height));
        scrollPane.setViewportView(this);
        scrollPane.revalidate();

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
            Point p1 = convertToScreen(points.get(i));
            Point p2 = convertToScreen(points.get(i+1));

            g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
        }
    }

    private void drawSpline(Graphics g)
    {
        g.setColor(Color.RED);
        var points = bSpline.getSplinePoints();
        if (points == null)
            return;
        if(points.size() < 4)
            return;

        for(int i = 0; i < points.size() - 1; i++)
        {
            Point p1 = convertToScreen(points.get(i));
            Point p2 = convertToScreen(points.get(i+1));

            g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
        }
    }

    private Point convertToScreen(Point p)
    {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        double x = centerX + p.getX()*INDENT;
        double y = centerY - p.getY()*INDENT;

        return new Point(x, y);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        drawAxis(g);
        drawSplineAnchorPoints(g);
        drawSpline(g);
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
                bSpline.createBSpline();
            }
            case CANVAS_MOVING -> {
                int dx = curX - e.getX();
                int dy = curY - e.getY();

                Dimension panelSize = new Dimension(getWidth(), getHeight());
                java.awt.Point viewPos = scrollPane.getViewport().getViewPosition();
                int maxX = viewPos.x + scrollPane.getViewport().getWidth();
                int maxY = viewPos.y + scrollPane.getViewport().getHeight();

                if (dx < 0 && viewPos.x == 0) {
                    width -= dx;
                    centerX -= dx;
                    curX -= dx;
                } else if (dx >= 0 && maxX == panelSize.width) {
                    width += dx;
                }

                if (dy < 0 && viewPos.y == 0) {
                    height -= dy;
                    centerY -= dy;
                    curY -= dy;
                } else if (dy >= 0 && maxY == panelSize.height) {
                    height += dy;
                }

                scrollPane.getHorizontalScrollBar().setValue(viewPos.x + dx);
                scrollPane.getVerticalScrollBar().setValue(viewPos.y + dy);
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

        if(e.getButton() == MouseEvent.BUTTON1)
            bSpline.addAnchorPoint(new Point((e.getX() - centerX)/INDENT, (centerY - e.getY())/INDENT));
        if(e.getButton() == MouseEvent.BUTTON3)
            bSpline.deleteAnchorPoint();

        bSpline.createBSpline();
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

        //curAction = Action.CANVAS_MOVING;
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
