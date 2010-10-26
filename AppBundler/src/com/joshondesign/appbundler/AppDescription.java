/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.joshondesign.appbundler;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author joshmarinacci
 */
public class AppDescription {
    private List<Jar> jars;
    private String name;

    public AppDescription() {
        jars = new ArrayList<Jar>();
    }

    void addJar(Jar jar) {
        this.jars.add(jar);
    }

    public Iterable<Jar> getJars() {
        return this.jars;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getMainClass() throws Exception {
        for(Jar jar : jars) {
            if(jar.isMain()) return jar.getMainClass();
        }
        throw new Exception("Error! Couldn't find a main class for the app");
    }

}
