package com.ifuture.exercises.view;

import javax.swing.*;
import java.awt.*;

public class ExitButton extends JButton {

    public ExitButton() {
        super("Exit");
        setMinimumSize(new Dimension(90, 30));
        setPreferredSize(new Dimension(90, 30));
        setMaximumSize(new Dimension(90, 30));

        addActionListener(e -> System.exit(0));
    }
}
