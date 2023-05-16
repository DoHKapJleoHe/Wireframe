package ru.nsu.ccfit.g20202.vartazaryan.bsplineeditor;

import ru.nsu.ccfit.g20202.vartazaryan.wireframe.Wireframe;
import ru.nsu.ccfit.g20202.vartazaryan.wireframe.WireframeFrame;
import ru.nsu.ccfit.g20202.vartazaryan.wireframe.WireframePanel;

import javax.swing.*;
import java.awt.*;

public class OptionsPanel extends JPanel
{
    private SplinePanel splinePanel;
    private WireframeFrame wireframePanel;

    private JButton applyButton;

    public OptionsPanel(SplinePanel panel, WireframeFrame frame)
    {
        this.splinePanel = panel;
        this.wireframePanel = frame;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setPreferredSize(new Dimension(640, 200));

        applyButton = new JButton("Apply");
        add(applyButton, BorderLayout.SOUTH);
        applyButton.addActionListener(e -> {
            wireframePanel.createWireframe();
        });

    }

}
