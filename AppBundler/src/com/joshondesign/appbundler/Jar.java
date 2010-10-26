/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.joshondesign.appbundler;

import java.io.File;

/**
 *
 * @author joshmarinacci
 */
public class Jar {
    private final String name;
    private boolean main;
    private String mainClass;
    private File file;

    public Jar(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
    void setMain(boolean main) {
        this.main = main;
    }

    void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public boolean isMain() {
        return this.main;
    }

    void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    String getMainClass() {
        return this.mainClass;
    }

}
