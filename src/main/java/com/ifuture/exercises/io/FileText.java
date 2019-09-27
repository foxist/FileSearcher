package com.ifuture.exercises.io;

import java.io.*;

public class FileText {

    public String convertFileToString(File filename) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String str;
        StringBuilder converter = new StringBuilder();

        while ((str = br.readLine()) != null) {
            converter.append(str);
            converter.append("\n");
        }
        br.close();
        return converter.toString();
    }
}
