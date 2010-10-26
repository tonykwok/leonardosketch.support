package com.joshondesign.appbundler.mac;

import com.joshondesign.appbundler.AppDescription;
import com.joshondesign.appbundler.Bundler;
import com.joshondesign.appbundler.Jar;
import com.joshondesign.xml.XMLWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: Jun 29, 2010
 * Time: 4:54:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class MacBundler {

    public static void start(AppDescription app, String dest_dir) throws Exception {
        //create the dir structure
        File destDir = new File(dest_dir+"/mac/");
        File appDir = new File(destDir,app.getName()+".app");
        appDir.mkdirs();
        File contentsDir = new File(appDir,"Contents");
        contentsDir.mkdir();
        new File(contentsDir,"MacOS").mkdir();
        File resourcesDir = new File(contentsDir, "Resources");
        resourcesDir.mkdir();
        File javadir = new File(resourcesDir,"Java");
        javadir.mkdir();


        //File libdir = new File(outDir,"lib");
        //libdir.mkdir();
        for(Jar jar : app.getJars()) {
            File jarFile = new File(javadir,jar.getName());
            byte[] buf = new byte[1024*16];
            FileInputStream fin = new FileInputStream(jar.getFile());
            FileOutputStream fout = new FileOutputStream(jarFile);
            while(true) {
                int n = fin.read(buf);
                if(n < 0) break;
                fout.write(buf,0,n);
            }
            fin.close();
            fout.close();
            p("m copied jar: " + jar.getName());
        }

        // copy the icon
        File icon = new File("resources/Turtle.icns");
        File outIcon = new File(resourcesDir,"icon.icns");
        p("out icon = " + outIcon.getAbsolutePath());
        p("t = " + resourcesDir.getAbsolutePath());
        p("t = " + resourcesDir.exists());
        Bundler.copyStream(new FileInputStream(icon),new FileOutputStream(outIcon));
        
        //build the info plist
        processInfoPlist(app,contentsDir);

        //copy the pkginfo
        Bundler.copyStream(
                MacBundler.class.getResourceAsStream("PkgInfo.txt"),
                new FileOutputStream(new File(contentsDir,"PkgInfo")));

        
        // copy the java stub
        FileInputStream stub_path = new FileInputStream(
            //"/System/Library/Frameworks/JavaVM.framework/Versions/Current/Resources/MacOS/JavaApplicationStub"
            "resources/JavaApplicationStub");
        File stub_dest = new File(contentsDir,"MacOS/JavaApplicationStub");
        Bundler.copyStream(stub_path,new FileOutputStream(stub_dest));
        // make the stub executable
        String[] command = new String[3];
        command[0] = "chmod";
        command[1] = "755";
        command[2] = stub_dest.getAbsolutePath();
        p("calling: ");
        for(String s : command) {
            System.out.print(s + " ");
        }
        p("");
        Runtime.getRuntime().exec(command);


        // set the bundle bit
        Runtime.getRuntime().exec("/Developer/Tools/SetFile -a B "+appDir);
    }

    private static void processInfoPlist(AppDescription app, File contentsDir) throws Exception {

        XMLWriter out = new XMLWriter(new File(contentsDir,"Info.plist"));
        out.header();
        out.start("plist","version","0.9");
        out.start("dict");
        
        out.start("key").text("CFBundleName").end().start("string").text(app.getName()).end();
        out.start("key").text("CFBundleVersion").end().start("string").text("10.2").end();
        out.start("key").text("CFBundleAllowMixedLocalizations").end().start("string").text("true").end();
        out.start("key").text("CFBundleExecutable").end().start("string").text("JavaApplicationStub").end();
        out.start("key").text("CFBundleDevelopmentRegion").end().start("string").text("English").end();
        out.start("key").text("CFBundlePackageType").end().start("string").text("APPL").end();
        out.start("key").text("CFBundleSignature").end().start("string").text("????").end();
        out.start("key").text("CFBundleInfoDictionaryVersion").end().start("string").text("6.0").end();
        out.start("key").text("CFBundleIconFile").end().start("string").text("icon.icns").end();

        out.start("key").text("CFBundleInfoDictionaryVersion").end().start("string").text("6.0").end();
        out.start("key").text("CFBundleInfoDictionaryVersion").end().start("string").text("6.0").end();

        out.start("key").text("Java").end();
        out.start("dict");
        key(out,"MainClass",app.getMainClass());
        key(out,"JVMVersion","1.6+");

        out.start("key").text("ClassPath").end();
        out.start("array");
        for(Jar jar : app.getJars()) {
            out.start("string").text("$JAVAROOT/"+jar.getName()).end();
        }
        out.end();//array

        out.start("key").text("Properties").end();
        out.start("dict");
        key(out,"apple.laf.useScreenMenuBar","true");
        out.end();//dict

        out.end();//dict
        
        out.end().end(); //dict, plist
        out.close();
    }

    private static void key(XMLWriter out, String key, String value) {
        out.start("key").text(key).end().start("string").text(value).end();
    }

    private static void p(String s) {
        System.out.println(s);
    }
}
