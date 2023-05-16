package ru.nsu.ccfit.g20202.vartazaryan.wireframe;

import ru.nsu.ccfit.g20202.vartazaryan.bsplineeditor.BSpline;

import javax.swing.*;
import java.awt.*;

public class WireframeFrame extends JFrame
{
    private int width = 900;
    private int height = 600;
    private WireframePanel wireframePanel;

    public WireframeFrame(BSpline bSpline)
    {
        wireframePanel = new WireframePanel(bSpline);
        setMinimumSize(new Dimension(width, height));

        add(wireframePanel);
        setVisible(true);
    }

    public void createWireframe() {
        wireframePanel.createWireframe();
    }
}
