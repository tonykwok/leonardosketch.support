/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.joshondesign.appbundler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author joshmarinacci
 */
public class AppDescription {
    private List<Jar> jars;
    private String name;
    private Map<String,String> extensions;

    public AppDescription() {
        jars = new ArrayList<Jar>();
        extensions = new HashMap<String, String>();
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

    void addExtension(String fileExtension, String mimeType) {
        extensions.put(fileExtension,mimeType);
    }
    public Collection<String> getExtensions() {
        return extensions.keySet();
    }
    public String getExtensionMimetype(String ext) {
        return extensions.get(ext);
    }

}
