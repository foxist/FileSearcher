package com.ifuture.exercises.view;

import com.ifuture.exercises.model.Data;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("Поиск файлов");
        setPreferredSize(new Dimension(420, 300));
        setMinimumSize(new Dimension(420, 300));
        setMaximumSize(new Dimension(420, 300));
        getMainFrame();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void getMainFrame() {
        JPanel first = new JPanel();
        first.setLayout(new GridLayout(2, 1));
        final JTextField filePath = new JTextField(35);
        JPanel folderPanel = new JPanel();
        folderPanel.setLayout(new GridBagLayout());
        folderPanel.setBorder(BorderFactory.createTitledBorder("Укажите путь к папке/файлу:"));
        folderPanel.add(filePath, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                new Insets(0,0,0,0), 0, 0));

        JPanel secondPanel = new JPanel();
        secondPanel.setLayout(new GridBagLayout());
        secondPanel.setBorder(BorderFactory.createTitledBorder("Введите расширение файла:"));
        final JTextField expansionText = new JTextField(35);
        expansionText.setText(".log");
        secondPanel.add(expansionText, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(0,0,0,0), 0, 0));

        first.add(folderPanel);
        first.add(secondPanel);

        JPanel thirdPanel = new JPanel();
        final JTextArea searchText = new JTextArea(6, 35);
        thirdPanel.setBorder(BorderFactory.createTitledBorder("Введите искомый текст:"));
        thirdPanel.add(new JScrollPane(searchText), new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0,0,0,0), 0, 0));

        JPanel fourthPanel = new JPanel();
        SearchButton searchButton = new SearchButton();
        fourthPanel.setLayout(new GridBagLayout());
        fourthPanel.add(searchButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                new Insets(5,10,5,30), 0, 0));
        fourthPanel.add(new ExitButton(), new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets(5,30,5,10), 0, 0));

       setLayout(new BorderLayout());
       add(first, BorderLayout.NORTH);
       add(thirdPanel, BorderLayout.CENTER);
       add(fourthPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> {
            Data data = new Data(filePath.getText(), searchText.getText(), expansionText.getText());
            new ProgressBar(data);
        });
    }
}
