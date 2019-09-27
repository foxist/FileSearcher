package com.ifuture.exercises.model;

import java.io.*;
import java.util.*;

public class Data {
    private Stack<File> stackFolders;
    private String filePath, searchText, expansion;

    public Data(String filePath, String searchText, String expansion) {
        this.filePath = filePath;
        if (!this.filePath.endsWith("\\"))
            this.filePath += "\\";
        this.searchText = searchText;
        this.expansion = expansion;
        this.stackFolders = new Stack<>();
    }

    public void push(File file) {
        this.stackFolders.push(file);
    }

    public Stack<File> getDirs() {
        return this.stackFolders;
    }

    public String getExpansion() {
        return this.expansion;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getSearchText() {
        return this.searchText;
    }
}
