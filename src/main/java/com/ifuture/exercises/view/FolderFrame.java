package com.ifuture.exercises.view;

import com.ifuture.exercises.io.FileText;
import com.ifuture.exercises.model.Data;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.*;

public class FolderFrame extends JFrame {
    private Data data;
    private int countThread;
    private AtomicInteger countInsert;
    private JTabbedPane tabbedPane;

    public FolderFrame(Data data, int countThread) {
        super("TreeFolder");
        setPreferredSize(new Dimension(600, 400));
        setMinimumSize(new Dimension(600, 400));
        setMaximumSize(new Dimension(600, 400));
        setLayout(new GridLayout(0, 2));

        this.data = data;
        this.countThread = countThread;
        this.countInsert = new AtomicInteger(0);
        this.tabbedPane = new JTabbedPane();

        add(getLeftPanel());
        add(getRightPanel());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private JPanel getLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        JTree tree = new JTree(data.getDirs());

        JScrollPane treeView = new JScrollPane(tree);
        leftPanel.add(treeView, BorderLayout.CENTER);
        tree.addTreeSelectionListener(new SelectionListener());

        return leftPanel;
    }

    private JPanel getRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        rightPanel.add(buttonPanel, BorderLayout.NORTH);

        JButton remove = new JButton("Remove");
        remove.addActionListener(e -> tabbedPane.removeTabAt(tabbedPane.getSelectedIndex()));
        buttonPanel.add(remove);

        rightPanel.add(tabbedPane, BorderLayout.CENTER);

        return rightPanel;
    }

    private JPanel createInsert(File file) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        ExecutorService threadPool = Executors.newFixedThreadPool(countThread);

        try {
            JTextPane text = new JTextPane();
            text.setText(new FileText().convertFileToString(file));
            Pattern pattern = Pattern.compile(data.getSearchText());
            Matcher matcher = pattern.matcher(text.getText());

            threadPool.execute(() -> {
                try {
                    while (matcher.find())
                        text.getHighlighter().addHighlight(matcher.start(), matcher.end(), new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN));
                } catch (BadLocationException e) {
                    JOptionPane.showMessageDialog(null, "Искомого текста не обнаружено!", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                }
            });
            JScrollPane scrollPane = new JScrollPane(text);
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, file.getName() + " Файла не обнаружено!", "Ошибка!", JOptionPane.ERROR_MESSAGE);
        }
        threadPool.shutdown();

        try {
            threadPool.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Время ожидания закончилось!", "Предупреждение!", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return panel;
    }

    class SelectionListener implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent e) {
            JTree tree = (JTree) e.getSource();
            File file = new File(String.valueOf(tree.getSelectionPath().getLastPathComponent()));
            if (file.isFile())
                tabbedPane.addTab("Insert " + countInsert.incrementAndGet(), createInsert(file));
        }
    }
}