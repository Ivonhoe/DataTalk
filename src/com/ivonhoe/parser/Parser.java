package com.ivonhoe.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by ivonhoe on 15-1-23.
 */
public abstract class Parser {

    public InputStream getInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream getInputStream(String fileName) {
        try {
            return new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract void onParser(InputStream inputStream);
}
