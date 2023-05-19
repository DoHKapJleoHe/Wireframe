package ru.nsu.ccfit.g20202.vartazaryan.bsplineeditor;

import ru.nsu.ccfit.g20202.vartazaryan.wireframe.WireframeFrame;

import javax.swing.*;
import java.awt.*;

public class BSplineEditor extends JFrame
{
    private WireframeFrame wireframeFrame;

    private SplinePanel editorPanel;
    private OptionsPanel optionsPanel;

    private JMenuBar menu;

    public BSplineEditor()
    {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        setMinimumSize(new Dimension(900, 700));
        setBounds(380, 100, 900, 600);

        BSpline bSpline = new BSpline();

        editorPanel = new SplinePanel(bSpline);
        add(editorPanel, BorderLayout.CENTER);

        wireframeFrame = new WireframeFrame(bSpline);
        optionsPanel = new OptionsPanel(editorPanel, wireframeFrame);
        add(optionsPanel, BorderLayout.SOUTH);


        /*MENU*/
        menu = new JMenuBar();
        add(menu, BorderLayout.NORTH);

        JMenu file = new JMenu("File");
        menu.add(file);

        JMenu about = new JMenu("About");
        menu.add(about);

        JMenuItem save = new JMenuItem("Save");
        JMenuItem load = new JMenuItem("Load");
        file.add(save);
        file.add(load);

        JMenuItem aboutItem = new JMenuItem("About");
        about.add(aboutItem);

        aboutItem.addActionListener(e -> {
            JOptionPane.showConfirmDialog(
                    null,
                    "Author: Vartazaryan Eduard Araevich, FIT NSU                                            ",
                    "About",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE);
        });

        setVisible(true);
    }

    public static void main(String[] args)
    {
        BSplineEditor bSplineEditor = new BSplineEditor();
    }

}
