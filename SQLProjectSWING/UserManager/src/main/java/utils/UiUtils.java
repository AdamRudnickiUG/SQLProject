package utils;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class UiUtils {

    public static void minMaxPref(Component component, int width, int height) {
        Dimension dimension = new Dimension(width, height);
        component.setMinimumSize(dimension);
        component.setMaximumSize(dimension);
        component.setPreferredSize(dimension);
    }

    public static void minMaxPrefWidth(Component component, int width) {
        component.setMinimumSize(new Dimension(width, 0));
        component.setMaximumSize(new Dimension(width, 10000));
        component.setPreferredSize(new Dimension(width, 10000));
    }

    public static void minMaxPrefHeight(Component component, int height) {
        component.setMinimumSize(new Dimension(0, height));
        component.setMaximumSize(new Dimension(10000, height));
        component.setPreferredSize(new Dimension(10000, height));
    }

    public static void border(Component component, Color color, int top, int left, int bottom, int right) {
        String compClass = component.getClass().getSimpleName();

        if (compClass.equals("JPanel"))
            ((JPanel) component).setBorder(new MatteBorder(top, left, bottom, right, color));
        else if (compClass.equals("Box"))
            ((Box) component).setBorder(new MatteBorder(top, left, bottom, right, color));
        else
            throw new RuntimeException("Unsupported Component in UiUtils.border()");
    }
}
