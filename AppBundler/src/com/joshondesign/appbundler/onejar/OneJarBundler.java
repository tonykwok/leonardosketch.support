/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.joshondesign.appbundler.onejar;

import com.joshondesign.appbundler.AppDescription;
import com.joshondesign.appbundler.Jar;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joshmarinacci
 */
public class OneJarBundler {

    public static void start(AppDescription app, String DEST_DIR) {
        try {
            Manifest manifest = new Manifest();
            Attributes a = manifest.getMainAttributes();
            a.putValue("Main-Class", app.getMainClass());
            a.put(Attributes.Name.MANIFEST_VERSION,"1.0");
            new File(DEST_DIR).mkdirs();
            
            Set<String> writtenNames = new HashSet<String>();
            p("doing a one jar bundler");
            JarOutputStream jarOut = new JarOutputStream(
                    new FileOutputStream(new File(DEST_DIR,app.getName()+".onejar.jar")),
                    manifest);

            //Create a read buffer to transfer data from the input
            byte[] buf = new byte[4096];
            for(Jar jar : app.getJars()) {
                JarInputStream jarIn = new JarInputStream(new FileInputStream(jar.getFile()));
                //Iterate the entries
                JarEntry entry;
                while ((entry = jarIn.getNextJarEntry()) != null) {
                    //Exclude the manifest file from the old JAR
                    if ("META-INF/MANIFEST.MF".equals(entry.getName())) continue;
                    //Write the entry to the output JAR
                    p("writng entry: " + entry.getName());
                    if(!writtenNames.contains(entry.getName())) {
                        //TODO: compression is broken here
                        //entry.setMethod(JarEntry.DEFLATED);
                        jarOut.putNextEntry(entry);
                        int read;
                        while ((read = jarIn.read(buf)) != -1) {
                            jarOut.write(buf, 0, read);
                        }
                        jarOut.closeEntry();
                        writtenNames.add(entry.getName());
                    }
                }
                jarIn.close();
            }
            //Flush and close all the streams
            jarOut.flush();
            jarOut.close();

        } catch (Exception ex) {
            Logger.getLogger(OneJarBundler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void p(String string) {
        System.out.println(string);
    }

}
