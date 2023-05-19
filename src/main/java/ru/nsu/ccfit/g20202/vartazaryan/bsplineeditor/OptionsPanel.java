package ru.nsu.ccfit.g20202.vartazaryan.bsplineeditor;

import ru.nsu.ccfit.g20202.vartazaryan.utils.Settings;
import ru.nsu.ccfit.g20202.vartazaryan.wireframe.WireframeFrame;

import javax.swing.*;
import java.awt.*;

public class OptionsPanel extends JPanel
{
    private SplinePanel splinePanel;
    private WireframeFrame wireframeFrame;

    private JButton applyButton;

    public OptionsPanel(SplinePanel panel, WireframeFrame frame)
    {
        this.splinePanel = panel;
        this.wireframeFrame = frame;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setPreferredSize(new Dimension(640, 200));

        applyButton = new JButton("Apply");
        add(applyButton, BorderLayout.SOUTH);
        applyButton.addActionListener(e -> {
            wireframeFrame.createWireframe();
        });

        JLabel generatrix = new JLabel("Generatrix");
        add(generatrix);
        SpinnerNumberModel generatrixNumModel = new SpinnerNumberModel(5, 2, 10, 1);
        JSpinner generatrixNum = new JSpinner(generatrixNumModel);
        add(generatrixNum);

        generatrixNum.addChangeListener(e -> {
            Settings.GENERATRIX_NUM = (int) generatrixNum.getValue();
        });

        JLabel circles = new JLabel("Circles");
        add(circles);
        SpinnerNumberModel circlesNumModel = new SpinnerNumberModel(4, 2, 10, 1);
        JSpinner circlesNum = new JSpinner(circlesNumModel);
        add(circlesNum);

        circlesNum.addChangeListener(e -> {
            Settings.CIRCLES_NUM = (int) circlesNum.getValue();
        });

        JLabel accuracy = new JLabel("Accuracy");
        add(accuracy);
        SpinnerNumberModel accuracyNumModel = new SpinnerNumberModel(3, 1, 10, 1);
        JSpinner accuracyNum = new JSpinner(accuracyNumModel);
        add(accuracyNum);

        accuracyNum.addChangeListener(e -> {
            Settings.CIRCLES_ACCURACY = (int)accuracyNum.getValue();
        });

        JLabel segments = new JLabel("Segments");
        add(segments);
        SpinnerNumberModel segmentsNumModel = new SpinnerNumberModel(10, 1, 20, 1);
        JSpinner segmentsNum = new JSpinner(segmentsNumModel);
        add(segmentsNum);

        segmentsNum.addChangeListener(e -> {
            Settings.SEGMENTS_NUM = (int) segmentsNum.getValue();
            splinePanel.recreateSpline();
        });
    }
}
