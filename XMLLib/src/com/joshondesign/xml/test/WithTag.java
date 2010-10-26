/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.joshondesign.xml.test;

import com.joshondesign.xml.Doc;
import com.joshondesign.xml.Elem;
import com.joshondesign.xml.XMLParser;
import com.joshondesign.xml.XMLWriter;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URI;

/**
 * Example that demonstrates the "withTag" method of element. 
 * @author collin fagan
 */
public class WithTag {
    
    public static void main(String... args) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        XMLWriter out = new XMLWriter(new PrintStream(outputStream), new URI(""));
        out.header(); //standard xml header
        out.start("foo", "version", "0.01", "type", "bar");
        for (int i = 0; i < 3; i++) {
            out.start("bar").attr("id", "" + i).text("asdf").end();
        }
        out.end();
        out.close();

        Doc doc = XMLParser.parse(outputStream.toString());
        for(Elem elem : doc.root().withTag("bar")){
            System.out.println(elem.text());
        }
    }
}
