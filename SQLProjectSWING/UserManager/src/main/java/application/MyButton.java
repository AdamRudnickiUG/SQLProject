package application;

import javax.swing.*;
import java.awt.*;

public class MyButton extends JButton {
    public MyButton(int height, int width){
        Dimension dimension = new Dimension(width, height);
        this.setMinimumSize(dimension);
        this.setMaximumSize(dimension);
        this.setPreferredSize(dimension);
    }
}
