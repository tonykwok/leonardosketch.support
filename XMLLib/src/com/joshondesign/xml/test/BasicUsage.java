/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.joshondesign.xml.test;

import com.joshondesign.xml.*;
import java.io.File;

/**
 *
 * @author joshmarinacci
 */
public class BasicUsage {

    public static void main(String ... args) throws Exception {
        XMLWriter out = new XMLWriter(new File("foo.xml"));
        out.header(); //standard xml header
        out.start("foo", "version","0.01","type","bar");
        for(int i=0; i<3; i++) {
            out.start("bar").attr("id",""+i).text("asdf").end();
        }
        out.start("ick", "a","b").text("asdf").start("x", "c","d").end().end();
        out.end();
        out.close();

        
        Doc doc = XMLParser.parse(new File("foo.xml"));
        for(Elem e : doc.xpath("//bar")) {
            System.out.println("id = " + e.attr("id"));
        }

        out = new XMLWriter(System.out,null);
        out.write(doc);

        doc = XMLParser.translate(doc,new File("foo.xsl").toURI());
        out.write(doc);

        out.close();
    }

}
