package ru.nsu.ccfit.g20202.vartazaryan.bsplineeditor;

import javax.swing.*;
import java.awt.*;

public class BSplineEditor extends JFrame
{
    private SplinePanel editorPanel;
    private OptionsPanel optionsPanel;

    public BSplineEditor()
    {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        setBounds(380, 100, 900, 600);

        BSpline bSpline = new BSpline();

        editorPanel = new SplinePanel(bSpline);
        add(editorPanel, BorderLayout.CENTER);

        optionsPanel = new OptionsPanel();
        add(optionsPanel, BorderLayout.SOUTH);

        setVisible(true);
    }


    public static void main(String[] args) {
        BSplineEditor bSplineEditor = new BSplineEditor();
    }
}
