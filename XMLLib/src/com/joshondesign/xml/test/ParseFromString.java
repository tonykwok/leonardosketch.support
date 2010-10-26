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
 * Example that demonstrated reading XML content from a string.
 *
 * @author collin fagan
 */
public class ParseFromString {

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

        // get the string value of the outputStream
        String xml = outputStream.toString();
        Doc doc = XMLParser.parse(xml);
        for (Elem e : doc.xpath("//bar")) {
            System.out.println("id = " + e.attr("id"));
        }

        out = new XMLWriter(System.out, null);
        out.write(doc);
        out.close();
    }
}
