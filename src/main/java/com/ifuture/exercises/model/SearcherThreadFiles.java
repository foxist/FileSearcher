package com.ifuture.exercises.model;

import com.ifuture.exercises.io.FileText;
import com.ifuture.exercises.view.ProgressBar;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.concurrent.*;
import java.util.regex.*;

public class SearcherThreadFiles extends RecursiveTask<Integer> {
    private ProgressBar progressBar;
    private Data data;
    private File[] folders;
    private int from, to;
    private boolean found;

    public SearcherThreadFiles(ProgressBar progressBar, Data data, int from, int to) {
        this.progressBar = progressBar;
        this.data = data;
        this.found = false;
        folders = new File(this.progressBar.getData().getFilePath()).listFiles();
        this.from = from;
        this.to = to;
    }

    @Override
    protected Integer compute() {
        if ((to - from) <= folders.length / progressBar.getCountThread()) {
            progressBar.setLength(folders.length);
            for (int i = from; i < to; i++) {
                if (i <= folders.length - 1)
                    progressBar.getI().getAndSet(i + 1);
                treeFolders(folders[i]);
            }
            return progressBar.getI().get();
        } else {
            int mid = (from + to) / 2;
            SearcherThreadFiles threadFiles1 = new SearcherThreadFiles(progressBar, data, from, mid);
            threadFiles1.fork();
            SearcherThreadFiles threadFiles2 = new SearcherThreadFiles(progressBar, data, mid + 1, to);
            return threadFiles1.join() + threadFiles2.compute();
        }
    }

    private void treeFolders(File file) {
        File[] folders = file.listFiles();
        if (folders == null) {
            if (file.isFile())
                searchExpansionFiles(file);
            return;
        }
        for (final File f : folders) {
            if (!progressBar.getData().getDirs().equals(f))
                searchExpansionFiles(file);
            updateGUIInProgress(f);
            if (progressBar.isCancel())
                return;
            treeFolders(f);
        }
    }

    private void searchExpansionFiles(File file) {
        final String[] string = progressBar.getData().getExpansion().split(",");
        if (file.isDirectory())
            return;
        for (String str : string)
            if (file.toString().endsWith(str))
                if (isNecessaryFile(file))
                    progressBar.getData().push(file);
                int a = 0;
    }

    private boolean isNecessaryFile(File file) {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        try {
            String text = new FileText().convertFileToString(file);
            Pattern pattern = Pattern.compile(data.getSearchText());
            Matcher matcher = pattern.matcher(text);

            threadPool.execute(() -> found = matcher.find());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, file.getName() + " Файла не обнаружено!", "Ошибка!", JOptionPane.ERROR_MESSAGE);
        }
        threadPool.shutdown();

        try {
            threadPool.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Время ожидания закончилось!", "Предупреждение!", JOptionPane.WARNING_MESSAGE);
        }
        return found;
    }

    private void updateGUIInProgress(File file) {
        SwingUtilities.invokeLater(() -> {
            if (progressBar.isCancel())
                return;
            String absolutePath = file.getAbsolutePath();
            if (absolutePath.length() > 90)
                progressBar.getLabelInfo().setText("Searching files: " + absolutePath.substring(0, 90) + "...");
            else
                progressBar.getLabelInfo().setText("Searching files: " + absolutePath);

            progressBar.getProgressBar().setMaximum(progressBar.getLength());
            progressBar.getProgressBar().setValue(progressBar.getI().get());
            progressBar.getProgressBar().setStringPainted(true);
            progressBar.add(progressBar.getProgressBar(), BorderLayout.NORTH);
            progressBar.add(progressBar.getLabelInfo(), BorderLayout.CENTER);
            progressBar.revalidate();
        });
    }
}