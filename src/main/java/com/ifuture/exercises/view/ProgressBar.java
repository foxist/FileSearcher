package com.ifuture.exercises.view;

import com.ifuture.exercises.model.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

public class ProgressBar extends JFrame {
    private Data data;
    private JButton buttonCancel;
    private JProgressBar bar;
    private JLabel labelInfo;

    private AtomicInteger i;
    private int length, countThread;
    private boolean cancel;

    public Data getData() {
        return data;
    }

    public JLabel getLabelInfo() {
        return labelInfo;
    }

    public boolean isCancel() {
        return cancel;
    }

    public AtomicInteger getI() {
        return i;
    }

    public int getLength() {
        return length;
    }

    public int getCountThread() {
        return countThread;
    }

    public JProgressBar getProgressBar() {
        return bar;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public ProgressBar(Data data) {
        super("Searcher");
        setPreferredSize(new Dimension(800, 150));
        setMinimumSize(new Dimension(800, 150));
        setMaximumSize(new Dimension(800, 150));
        buttonCancel = new JButton("Cancel");
        bar = new JProgressBar();
        labelInfo = new JLabel("Searching...");
        this.data = data;
        this.length = new File(data.getFilePath()).listFiles().length;
        this.countThread = Runtime.getRuntime().availableProcessors();
        this.i = new AtomicInteger(0);
        getCreateProgressBar();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getThreadSearchFiles();
    }

    private void getCreateProgressBar() {
        bar.setSize(400, 40);
        labelInfo.setSize(700, 20);
        buttonCancel.setSize(200, 30);
        buttonCancel.setEnabled(false);
        setVisible(true);

        buttonCancel.addActionListener((e) -> cancel = true);
    }

    private void getThreadSearchFiles() {
        ForkJoinPool joinPool = new ForkJoinPool(countThread);
        joinPool.execute(() -> {
            updateGUIWhenStart();
            joinPool.invoke(new SearcherThreadFiles(this, data, 0, length));
            if (cancel) {
                setVisible(false);
                return;
            }
            updateGUIWhenFinish();
            if (data.getDirs().isEmpty() && i.get() == length)
                JOptionPane.showConfirmDialog(null, "Файлов с данными расширениями (" + data.getExpansion() + ") не найдено!",
                        "Предупреждение!", JOptionPane.OK_CANCEL_OPTION);
            else if (i.get() == length && !data.getDirs().isEmpty())
                new FolderFrame(data, countThread).setVisible(true);
            setVisible(false);
        });
        joinPool.shutdown();
    }

    private void updateGUIWhenStart() {
        SwingUtilities.invokeLater(() -> {
            buttonCancel.setEnabled(true);
            add(buttonCancel, BorderLayout.SOUTH);
            revalidate();
        });
    }

    private void updateGUIWhenFinish() {
        SwingUtilities.invokeLater(() -> {
            buttonCancel.setVisible(false);
            labelInfo.setText("Finished!");
            revalidate();
        });
    }
}
