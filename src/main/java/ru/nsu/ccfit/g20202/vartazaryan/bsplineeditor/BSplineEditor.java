package ru.nsu.ccfit.g20202.vartazaryan.bsplineeditor;

import ru.nsu.ccfit.g20202.vartazaryan.wireframe.WireframeFrame;

import javax.swing.*;
import java.awt.*;

public class BSplineEditor extends JFrame
{
    private WireframeFrame wireframeFrame;

    private SplinePanel editorPanel;
    private OptionsPanel optionsPanel;

    public BSplineEditor()
    {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        setMinimumSize(new Dimension(900, 600));
        setBounds(380, 100, 900, 600);

        BSpline bSpline = new BSpline();

        editorPanel = new SplinePanel(bSpline);
        add(editorPanel, BorderLayout.CENTER);

        wireframeFrame = new WireframeFrame(bSpline);
        optionsPanel = new OptionsPanel(editorPanel, wireframeFrame);
        add(optionsPanel, BorderLayout.SOUTH);


        setVisible(true);
    }

    public static void main(String[] args)
    {
        BSplineEditor bSplineEditor = new BSplineEditor();
    }

}
